package com.github.goodtrailer.tekxit3bot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Bot
{
	Database db;
	MessageCreateEvent currEvent;
	String currMsg;
	String[] currMsgArr;
	String home;
	
	final String BOT_PREFIX = "/";
	
	
    public static void main( String[] args )
    {
    	String token = null;
        new Bot(token);
    }
    
    public Bot (String token)
    {
    	home = System.getProperty("user.home");
    	new File(home + "/.tekxit3bot").mkdir();
    	
    	try (Stream<String> stream = Files.lines(Paths.get(home + "/.tekxit3bot/token.txt"))) {
    		token = stream.iterator().next();
    		System.out.println(token);
    	} catch (IOException e) { }
    	
    	try {
    		FileInputStream fileIn = new FileInputStream(home + "/.tekxit3bot/db.ser");
    		ObjectInputStream in = new ObjectInputStream(fileIn);
    		db = (Database) in.readObject();
    		in.close();
    		fileIn.close();
    	}
    	catch (Exception e) {
    		db = new Database();
    		System.out.println("Didn't find serialized database");
    	}
	
		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		api.addMessageCreateListener(event -> {
			currEvent = event;
			currMsg = event.getMessageContent();
			if (currMsg.startsWith(BOT_PREFIX))
			{
				currMsgArr = currMsg.substring(1).split(" ");
				System.out.println(currMsgArr[0]);
				switch (currMsgArr[0]) {
				case "test":
					Test();
					break;
				case "points":
					Points();
					break;
				case "mods":
					ModList();
					break;
				case "disconnect":
					api.disconnect();
					break;
				}
			}
			
		});
		
    }
    
    void Test()
    {
    	currEvent.getChannel().sendMessage("\"test\" right back at'chu.");
    }
    
    
    
    void ModList()
    {
    	System.out.println("ModList()");
    	String msgToSend = ":";
    	if (currMsgArr.length == 1)
    	{
    		for (String[] mod:db.mods)
    		{
    			msgToSend += "\n**" + mod[0] + ":**        " + mod[1] + "mp";
    		}
    	}
    	else if (currMsgArr.length == 3)
    	{
    		System.out.println("removing...");
    		if (currMsgArr[1].equalsIgnoreCase("remove"))
	    		try
	    		{
	    			for (String[] mod:db.mods)
	    				if (mod[0].equalsIgnoreCase(currMsgArr[2]))
	    				{
	    					db.mods.remove(mod);
	    					SerializeDB();
							msgToSend = "Removed the **" + currMsgArr[2] + "** mod.";
	    					break;
	    				}
	    		} catch (IOException e) { e.printStackTrace(); }
    	}
    	else if (currMsgArr.length == 4)
    	{
    		System.out.println("should be adding...");
    		if (currMsgArr[1].equalsIgnoreCase("add"))
	    		try
	    		{
	    			Integer.parseInt(currMsgArr[3]);
	    			db.mods.add(new String[] { currMsgArr[2], currMsgArr[3] });
	    			SerializeDB();
					msgToSend = "Added the **" + currMsgArr[2] + "** mod for **" + currMsgArr[3] + "mp**!";
	    		}
	    		catch (IOException e)
	    		{
	    			e.printStackTrace();
	    		}
	    		catch (NumberFormatException e)
	    		{
	    			msgToSend = "Put an integer for the points parameter.";
	    		}
    	}
    	currEvent.getChannel().sendMessage(msgToSend);
    }
    
    void Points()
    {
    	System.out.println("Points()");
    	String msgToSend = "";
		
		boolean foundPlayer = false;
		
    	if (currMsgArr.length == 1)
		{
    		for (int i = 0; i < db.points.size(); i++)
    			msgToSend += "**· " + db.points.get(i)[0] + ":**        " + db.points.get(i)[1] + "mp\n";
		}
		
		else if (currMsgArr.length == 3 && currMsgArr[1].equalsIgnoreCase("remove"))
		{
			for (int i = 0; i < db.points.size(); i++)
				if (db.points.get(i)[0].equalsIgnoreCase(currMsgArr[2]))
					db.points.remove(i);
		}
    	else if (currMsgArr.length == 4 && (currMsgArr[1].equalsIgnoreCase("add") || currMsgArr[1].equalsIgnoreCase("subtract")))
		{
    		for (int i = 0; i < db.points.size(); i++)
    			if (db.points.get(i)[0].equalsIgnoreCase(currMsgArr[2]))
    			{
    				if (currMsgArr[1].equalsIgnoreCase("add"))
    					db.points.get(i)[1] = Integer.toString(Integer.parseInt(db.points.get(i)[1]) + Integer.parseInt(currMsgArr[3]));
    				else if (currMsgArr[1].equalsIgnoreCase("subtract"))
    					db.points.get(i)[1] = Integer.toString(Integer.parseInt(db.points.get(i)[1]) - Integer.parseInt(currMsgArr[3]));
    				
					
					msgToSend = "**"
    						+ currMsgArr[2]
    						+ "** currently has **"
    						+ Integer.parseInt(db.points.get(i)[1])
    						+ "** points";
					
					foundPlayer = true;
					
    				try {
    					SerializeDB();
    				} catch (IOException e) { e.printStackTrace(); }
    				break;
    			}
			
			if (!foundPlayer)
			{
				if (currMsgArr[1].equalsIgnoreCase("add"))
					db.points.add(new String[] { currMsgArr[2], currMsgArr[3] });
    			else if (currMsgArr[1].equalsIgnoreCase("subtract"))
					db.points.add(new String[] { currMsgArr[2], "" + (-1 * Integer.parseInt(currMsgArr[3])) });
				
				
				msgToSend = "**"
    						+ currMsgArr[2]
    						+ "** currently has **"
    						+ db.points.get(db.points.size() - 1)[1]
    						+ "** points";
				try {
    				SerializeDB();
    			} catch (IOException e) { e.printStackTrace(); }
			}
		}
			
    	currEvent.getChannel().sendMessage(":\n" + msgToSend);
    }
    
    void SerializeDB() throws IOException
    {
    	FileOutputStream fileOut = new FileOutputStream(home + "/.tekxit3bot/db.ser");
    	ObjectOutputStream out = new ObjectOutputStream(fileOut);
    	out.writeObject(db);
    	out.close();
    	fileOut.close();
    	System.out.println("Serialized data is saved in " + home + "/.tekxit3bot/db.ser");
    }
}