package com.github.goodtrailer.tekxit3bot;

import java.io.Serializable;
import java.util.ArrayList;

public class Database implements Serializable {
	private static final long serialVersionUID = 6935896525619251314L;
	public ArrayList<String[]> points;
	public ArrayList<String[]> mods;
	public ArrayList<String[]> effects;
	public ArrayList<String> other;
	
	public Database()
	{
		points = new ArrayList<String[]>();				// { name, points }
		mods = new ArrayList<String[]>();				// { name, points }
		effects = new ArrayList<String[]>();			// { name, limit }
		other = new ArrayList<String>();				// name
	}
}
