package com.panjaco.teams;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Team {
	
	
	enum Status{
		open,
		closed,
	};
	
	public String name;	//Team name
	public ChatColor color;	//Team Color
	public Status status;	//Team join mode(public/private team)
	public ArrayList<Player> members = new ArrayList<Player>();
	public ArrayList<Player> invited = new ArrayList<Player>();
	
	//public static int id = 0;
	//public static int idT = 0;
	
    static final AtomicLong NEXT_ID = new AtomicLong(0);
    final long idz = NEXT_ID.getAndIncrement();
    int id = 0;
	
	
	//Boolean status;

	
	public Team(){
		name = "Default";
		color = ChatColor.BLUE;
		status = status.open;
		members.clear();
		invited.clear();
		id = (int) idz;
		//id = idT;
		//idT++;
		if(!Teams.allTeams.contains(this)){
			Teams.allTeams.add(this);
		}

	}
	
	public Team(String nameC, ChatColor colorC, Status statusC){
		name = nameC;
		color = colorC;
		status = statusC;
		invited.clear();
		members.clear();
		id = (int) idz;
		//id = idT;
		//idT++;
		if(!Teams.allTeams.contains(this)){
			Teams.allTeams.add(this);
		}

	}
	
	
	
}
