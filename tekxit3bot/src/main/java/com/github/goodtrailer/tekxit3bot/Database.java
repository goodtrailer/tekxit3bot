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
		points.add(new String[] { "Brandon", "0" });
		points.add(new String[] { "Austin", "0" });
		points.add(new String[] { "Alden", "0" });
		points.add(new String[] { "Kyle", "0" });
		mods = new ArrayList<String[]>();				// { name, points }
		effects = new ArrayList<String[]>();			// { name, limit }
		other = new ArrayList<String>();				// name
	}
}
