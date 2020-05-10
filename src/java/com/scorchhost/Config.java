package com.scorchhost;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

class cmdTemplate<Reply, Func, CoolDown>
{
    Reply reply;
    Func func;
    CoolDown coolDown;

    public cmdTemplate(Reply r, Func f, CoolDown cd)
    {
        this.reply = r;
        this.func = f;
        this.coolDown = cd;
    }

    public Reply getReply()
    {
        return this.reply;
    }

    public Func getFunc()
    {
        return this.func;
    }

    public CoolDown getCoolDown()
    {
        return this.coolDown;
    }

    public void setReply(Reply t)
    {
        this.reply = t;
    }

    public void setFunc(Func t)
    {
        this.func = t;
    }

    public void setCoolDown(CoolDown t)
    {
        this.coolDown = t;
    }
}

public class Config
{
    protected Document doc = null;

    // Create the hash maps.
    protected Map<String, cmdTemplate<String, String, Integer>> cmdsMap = new HashMap<>();
    protected Map<String, Object> configMap = new HashMap<>();

    public Config(String sConfigFile)
    {
        // Build the config.
        try
        {
            // Read the config file.
            File configFile = new File(sConfigFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(configFile);

            this.doc.getDocumentElement().normalize();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Map all the configs.
        this.initializeConfig();
    }

    public Document getDoc()
    {
        return this.doc;
    }

    public Map getCmdsMap()
    {
        return this.cmdsMap;
    }

    public Map getConfigMap()
    {
        return this.configMap;
    }

    public void initializeConfig()
    {
        NodeList cmds = this.doc.getElementsByTagName("Commands").item(0).getChildNodes();

        for (int i = 0; i < cmds.getLength(); i++)
        {
            // We've determine if it's a command.
            if (cmds.item(i).getNodeName().equals("command"))
            {
                Element cmdEle = (Element) cmds.item(i);
                String replyWith = "";
                String func = "";
                int cooldown = 0;

                NodeList details = cmds.item(i).getChildNodes();

                // Gain the details.
                for (int o = 0; o < details.getLength(); o++)
                {
                    if (details.item(o).getNodeName().equals("reply"))
                    {
                        replyWith = details.item(o).getTextContent();
                    }
                    else if (details.item(o).getNodeName().equals("function"))
                    {
                        func = details.item(o).getTextContent();
                    }
                    else if (details.item(o).getNodeName().equals("cooldown"))
                    {
                        cooldown = Integer.parseInt(details.item(o).getTextContent());
                    }
                }

                this.cmdsMap.putIfAbsent(cmdEle.getAttribute("name"), new cmdTemplate<>(replyWith, func, cooldown));
            }
        }
    }
}