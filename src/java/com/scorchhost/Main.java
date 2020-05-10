package com.scorchhost;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;

public class Main
{
    public static void main(String[] args) throws LoginException
    {
        String configFile;

        // Set config file.
        if (args.length > 0)
        {
            configFile = args[0];
        }
        else
        {
            configFile = "config.xml";
        }

        //Specify the token
        String sToken = "<tokenHere>";

        // Initialize JDA.
        JDA jda = new JDABuilder(sToken).build();
        jda.addEventListener(new Discord(configFile));
    }
}