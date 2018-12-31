package com.github.goodtrailer.tekxit3bot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Bot
{
	Database db;
	MessageCreateEvent currEvent;
	String currMsg;
	String[] currMsgArr;
	
	final String BOT_PREFIX = "/";
	
	
    public static void main( String[] args )
    {
    	String token = "NTI3Njg4MzI1NDI4MDE5MjMw.Dwmycg.JI6cIwfQXZ0hc7eiPJ5fUR-7Nok";
        new Bot(token);
        try {
        System.in.read();
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public Bot (String token)
    {
    	try {
    		FileInputStream fileIn = new FileInputStream("/temp/db.ser");
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
        		}
    		}
    		
    	});

    	System.out.println("inviteurl:\n" + api.createBotInvite());
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
	    		}
	    		catch (IOException e)
	    		{
	    			e.printStackTrace();
	    		}
	    		catch (NumberFormatException e)
	    		{
	    			msgToSend = "put an integer for the points part";
	    		}
    	}
    	currEvent.getChannel().sendMessage(msgToSend);
    }
    
    void Points()
    {
    	System.out.println("Points()");
    	String msgToSend = "";
    	if (currMsgArr.length == 1)
    	{
    		for (int i = 0; i < db.points.size(); i++)
    		{
    			msgToSend += "**· " + db.points.get(i)[0] + ":**        " + db.points.get(i)[1] + "\n";
    		}
    	}
    	else if (currMsgArr.length == 4 && (currMsgArr[1].equalsIgnoreCase("add") || currMsgArr[1].equalsIgnoreCase("remove")))
    		for (int i = 0; i < db.points.size(); i++)
    			if (db.points.get(i)[0].equalsIgnoreCase(currMsgArr[2]))
    			{
    				if (currMsgArr[1].equalsIgnoreCase("add"))
    					db.points.get(i)[1] = Integer.toString(Integer.parseInt(db.points.get(i)[1])
    										+ Integer.parseInt(currMsgArr[3]));
    				else if (currMsgArr[1].equalsIgnoreCase("remove"))
    					db.points.get(i)[1] = Integer.toString(Integer.parseInt(db.points.get(i)[1])
								- Integer.parseInt(currMsgArr[3]));
    				msgToSend = "**"
    						+ currMsgArr[2]
    						+ "** currently has **"
    						+ Integer.parseInt(db.points.get(i)[1])
    						+ "** points";
    				try {
    					SerializeDB();
    				} catch (IOException e) { e.printStackTrace(); }
    				break;
    			}
    	currEvent.getChannel().sendMessage(":\n" + msgToSend);
    }
    
    void SerializeDB() throws IOException
    {
    	FileOutputStream fileOut = new FileOutputStream("/temp/db.ser");
    	ObjectOutputStream out = new ObjectOutputStream(fileOut);
    	out.writeObject(db);
    	out.close();
    	fileOut.close();
    	System.out.println("Serialized data is saved in /temp/db.ser");
    }
}