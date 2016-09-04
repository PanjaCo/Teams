package com.panjaco.teams.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.panjaco.teams.Team;
import com.panjaco.teams.Teams;

import net.md_5.bungee.api.ChatColor;

public class onPlayerAttack implements Listener{
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event){
		Player player = (Player) event.getDamager();
		Player target = (Player) event.getEntity();
		double damage = event.getFinalDamage();
		for(Team t : Teams.allTeams){
			if(t.members.contains(player) && t.members.contains(target)){
				event.setCancelled(true);
			}
		}
	}
	
}
