package com.github.goodtrailer.tekxit3bot;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.CertainMessageEvent;
import org.javacord.api.event.message.MessageCreateEvent;

public class Bot
{
	Database db;
	MessageCreateEvent currEvent;
	
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
    		FileInputStream fileIn = new FileInputStream("db.ser");
    		ObjectInputStream in = new ObjectInputStream(fileIn);
    		db = (Database) in.readObject();
    		in.close();
    		fileIn.close();
    	}
    	catch (Exception e) {
    		db = new Database();
    	}
    	
    	DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
    	
    	api.addMessageCreateListener(event -> {
    		currEvent = event;
    		String msg = event.getMessageContent();
    		if (msg.startsWith(BOT_PREFIX))
    		{
        		String[] msgArr = msg.substring(1).split(" ");
        		System.out.println(msgArr[0]);
        		switch (msgArr[0]) {
        		case "test":
        			Test();
        		}
    		}
    		
    	});

    	System.out.println("inviteurl:\n" + api.createBotInvite());
    }
    
    
    void Test()
    {
    	currEvent.getChannel().sendMessage("\"test\" right back at'chu.");
    }
	
	class Database implements Serializable
	{
		private static final long serialVersionUID = -4042618825238557738L;
		public ArrayList<String[]> points;
		public ArrayList<String[]> mods;
		public ArrayList<String[]> effects;
		public ArrayList<String[]> other;
		
		public Database()
		{
			points = new ArrayList<String[]>();
			mods = new ArrayList<String[]>();
			effects = new ArrayList<String[]>();
			other = new ArrayList<String[]>();
		}
	}
}