# Discord Bot V2

## Description
This is a simple Discord bot I made for fun in Java. It is currently used on KG's Discord server. It offers an easy way to create commands along with options like a cool down and the ability to execute a custom function. Everything is handled in an XML config file. You may specify the path to the config file via command-line (argument #1).

## JAR File
There is currently no JAR file included. I've built one on my own, but since the Discord API token is hard-coded into the project, I cannot provide a JAR file. Feel free to use my project and compile your own JAR file.

## To Do
* Have the Discord API token be handled in the config file.
* Once the above task is done, include a JAR file.
* Optimizations + better support.
* Add custom functions.

## Run In Java Via JAR
Here is the command line I use for my Linux server:

```
/usr/bin/java -jar /home/roy/my-discord-bot.jar "/etc/discordbot/config.xml"
```

The config file is located at `/etc/discordbot/config.xml` in this case.

## Credits
* [Christian Deacon](https://www.linkedin.com/in/christian-deacon-902042186/) - Creator.