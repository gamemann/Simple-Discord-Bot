package com.scorchhost;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Discord extends ListenerAdapter
{
    protected Map<Long, Boolean> coolDownList = new HashMap<>();

    protected Config cfg;
    protected Map cmdsMap;

    public Discord(String configFile)
    {
        // Initialize the config and get the hash maps.
        this.cfg = new Config(configFile);
        this.cfg.initializeConfig();
        this.cmdsMap = this.cfg.getCmdsMap();
    }

    public void onMessageReceived(MessageReceivedEvent event)
    {
        // Check if owner is a bot.
        if (event.getAuthor().isBot())
        {
            return;
        }

        // Check if user is on cooldown.
        if (coolDownList.get(event.getAuthor().getIdLong()) != null)
        {
            if (coolDownList.get(event.getAuthor().getIdLong()).booleanValue())
            {
                return;
            }
        }

        cmdTemplate temp = (cmdTemplate) this.cmdsMap.get(event.getMessage().getContentRaw());

        // Check to see if it's a valid command.
        if (temp != null)
        {
            // Send message.
            if (!temp.getReply().toString().isEmpty())
            {
                event.getChannel().sendMessage(temp.getReply().toString()).queue();
            }

            //System.out.println((int) temp.getCoolDown());

            // Set cool down if any.
            if ((int) temp.getCoolDown() > 0)
            {
                // Set user to cooldown.
                coolDownList.put(event.getAuthor().getIdLong(), true);

                // Create Timer Task.
                TimerTask tt = new TimerTask()
                {
                    public void run()
                    {
                        coolDownList.put(event.getAuthor().getIdLong(), false);
                        //System.out.println("Reset user for cooldown " + event.getAuthor().getIdLong());
                    }
                };

                // Create timer.
                Timer timer = new Timer();

                timer.schedule(tt, (int) temp.getCoolDown() * 1000);
            }

            // Execute function if any.
            if (!temp.getFunc().toString().isEmpty())
            {
                //Discord main = new Discord(null);

                try
                {
                    java.lang.reflect.Method Method = getClass().getDeclaredMethod(temp.getFunc().toString(), MessageReceivedEvent.class);
                    Method.invoke(this, event);
                }
                catch (NoSuchMethodException e)
                {
                    System.out.println(temp.getFunc().toString() + " doesn't exist?");
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void funcTest(MessageReceivedEvent event)
    {
        System.out.println("Message function sent from " + event.getAuthor().getAsTag());
    }
}