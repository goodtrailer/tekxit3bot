package com.github.goodtrailer.tekxit3bot;

import java.io.Serializable;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class Bot implements MessageCreateListener
{
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
    	
    }

	public void onMessageCreate(MessageCreateEvent event) {
		
	}
	
	class Database implements Serializable
	{
		private static final long serialVersionUID = -4042618825238557738L;
		
	}
}