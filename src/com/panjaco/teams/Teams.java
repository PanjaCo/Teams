package com.panjaco.teams;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.panjaco.teams.Team.Status;
import com.panjaco.teams.events.onPlayerAttack;
import com.panjaco.teams.events.onPlayerDisconnect;


public class Teams extends JavaPlugin{
	
	public static Teams plugin;
	
	public static ArrayList<Team> allTeams = new ArrayList<Team>();
	
	//Currently the creating a team command will not be used, as I need to learn hash maps
	//	to dynamically make new ArrayLists
	
	
	public static Team blueTeam = new Team("Blue Team", ChatColor.BLUE, Status.open);
	public static Team redTeam = new Team("Red Team", ChatColor.RED, Status.open);
	public static Team goldTeam = new Team("Gold Team", ChatColor.GOLD, Status.closed);
	
	public void onEnable(){
		PluginDescriptionFile descFile = getDescription();
		Logger logger = getLogger();
		logger.info("[Teams] Enabled");
		
		plugin = this;
		
		//Register commands
		//getCommand("votekick").setExecutor(new startKick(this));
		
		Bukkit.getPluginManager().registerEvents(new onPlayerDisconnect(), this);
		Bukkit.getPluginManager().registerEvents(new onPlayerAttack(), this);
		
	}
	
	public void onDisable(){
		PluginDescriptionFile descFile = getDescription();
		Logger logger = getLogger();
		logger.info("[Teams] Disabled");
	}
	
	public void loadConfiguration(){
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
		if(label.equalsIgnoreCase("teams")){
			//Usage: /teams list
			if(args[0].equalsIgnoreCase("list")){
				sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.GREEN + ChatColor.BOLD + "All teams:");
				for(Team t : allTeams){
					sender.sendMessage(t.color + t.name + " - " + t.status + " - " + t.id);
				}
				
				
			//Usage: /teams info <team id>
			}else if(args[0].equals("info")){
				for(Team t :allTeams){
					if(t.id == Integer.parseInt(args[1])){
						sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.GREEN + t.name + ":");
						sender.sendMessage(ChatColor.GREEN + "Team Color: " + t.color + t.color.name());
						sender.sendMessage(ChatColor.UNDERLINE + "" + ChatColor.GREEN + "Team Members:");
						for(Player p : t.members){
							sender.sendMessage(ChatColor.RED + p.getDisplayName());
						}
					}
				}
				
				
				
				//Usage: /teams add <player> <team id>
			}else if(args[0].equalsIgnoreCase("add")){
				Player player;
				try{
					player = Bukkit.getPlayer(args[1]);
				}catch(Exception e){
					return true;
				}
				
				for(Team t : allTeams){
					if(t.members.contains(player)){
						sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + player.getDisplayName() + " is already in a team!");
						return true;
					}
				}
				
				for(Team t : allTeams){
					if(t.id == Integer.parseInt(args[2])){
						t.members.add(player);
						sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.GREEN + player.getDisplayName() + " has been added to the " + t.name);
						player.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.GREEN + "You have joined the " + t.name);
						return true;
					}
				}
				
				//Usage: /teams join <team id>
			}else if(args[0].equalsIgnoreCase("join")){
				Player player = (Player) sender;
				
				for(Team t : allTeams){
					if(t.members.contains(player)){
						sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + "You are already in a team!");
						return true;
					}
				}
				
				for(Team t : allTeams){
					if(t.id == Integer.parseInt(args[1])){
						if(t.status == Status.open || t.invited.contains(player)){
							t.members.add(player);
							if(t.invited.contains(player)){
								t.invited.remove(player);
							}
							player.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.GREEN + " You have joined the " + t.name);
						}else{
							sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.GREEN + "This team requires an invite");
						}
					}
				}
				
				//Usage: /teams invite <player>
			}else if(args[0].equalsIgnoreCase("invite")){
				Player inviter = (Player) sender;
				Player player;
				
				try{
					player = Bukkit.getPlayer(args[1]);
				}catch(Exception e){
					return true;
				}
				
				for(Team t : allTeams){
					if(t.members.contains(player)){
						sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + player.getDisplayName() + " is already in a team!");
						return true;
					}
				}
				for(Team t : allTeams){
					if(t.members.contains(inviter)){					
						if(!t.invited.contains(player)){
							t.invited.add(player);
							player.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.GREEN + "You have been invited to the " + t.name + " by " + inviter.getDisplayName());
							inviter.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.GREEN + player.getDisplayName() + " has been invited!");
							inviteRemoval(t, player);
						}
						
						return true;
					}
				}
				sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + "You are not in a team!");
				return true;
				
				
			}else if(args[0].equalsIgnoreCase("leave")){
				Player player = (Player) sender;
				
				for(Team t : allTeams){
					if(t.members.contains(player)){
						t.members.remove(player);
						player.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + "You have left the " + t.name);
						return true;
					}
				}
			}else if(args[0].equalsIgnoreCase("help")){
				sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.GREEN + "Help Menu");
				sender.sendMessage(ChatColor.GOLD + "/teams add <player> <team id>" + ChatColor.RED + " - " + ChatColor.GREEN + "Add a player to a designated team");
				sender.sendMessage(ChatColor.GOLD + "/teams info <team id>" + ChatColor.RED + " - " + ChatColor.GREEN + "See all of the info on a designated team");
				sender.sendMessage(ChatColor.GOLD + "/teams invite <player>" + ChatColor.RED + " - " + ChatColor.GREEN + "Invite a player to your team");
				sender.sendMessage(ChatColor.GOLD + "/teams join <team id>" + ChatColor.RED + " - " + ChatColor.GREEN + "Join a designated team");
				sender.sendMessage(ChatColor.GOLD + "/teams leave" + ChatColor.RED + " - " + ChatColor.GREEN + "Leave the current team you are in");
				sender.sendMessage(ChatColor.GOLD + "/teams list" + ChatColor.RED + " - " + ChatColor.GREEN + "List all of the teams");
				
			}else if(args[0].equalsIgnoreCase("kick")){
				Player player = (Player) sender;
				Player target;
				
				try{
					target = Bukkit.getPlayer(args[1]);
				}catch(Exception e){
					player.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + args[1] + " could not be found!");
					return true;
				}
				
				
				for(Team t : allTeams){
					if(t.members.contains(player) && t.members.contains(target)){
						t.members.remove(target);
						sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + "You have kicked " + target.getDisplayName());
						player.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + "You have been kicked!");
						return true;
					}
				}
				
				sender.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + "You are not in the same team as " + args[1]);
				return true;
				
			}
		}
		
		return true;
	}
	
	public void inviteRemoval(Team team, Player player){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				
				//After two minutes, it will run this code :D
				
				team.invited.remove(player);
				player.sendMessage(ChatColor.AQUA + "[Teams] " + ChatColor.RED + "Your invitation to the " + team.name + " has expired!");
				
			}
		}, 2400L);	//2 minutes
		//20L = 1 second
	}
	
}
