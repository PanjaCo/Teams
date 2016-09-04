package com.panjaco.teams.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.panjaco.teams.Team;
import com.panjaco.teams.Teams;

public class onPlayerDisconnect implements Listener{
	
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event){
		Player player = event.getPlayer();
		
		//Print out all teams
		for(Team t : Teams.allTeams){
			if(t.members.contains(player)){
				t.members.remove(player);
			}
		}
		
	}
	
}
