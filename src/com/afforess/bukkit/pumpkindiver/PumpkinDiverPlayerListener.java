package com.afforess.bukkit.pumpkindiver;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PumpkinDiverPlayerListener extends PlayerListener{
	private static ArrayList<String> players = new ArrayList<String>(50);
	
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!players.contains(event.getPlayer().getName())) {
			players.add(event.getPlayer().getName());
			doPumpkinDiver(event.getPlayer().getName());
		}
    }
	
	public static void doPumpkinDiver(final String name) {
		final int currentAir = PumpkinDiver.server.getPlayer(name).getRemainingAir();
		final boolean wearingDivingHelmet = PumpkinDiver.server.getPlayer(name).getInventory().getHelmet().getTypeId() == Material.PUMPKIN.getId();
		if (wearingDivingHelmet) {
			PumpkinDiver.server.getPlayer(name).setMaximumAir(3000);
		}
		else {
			PumpkinDiver.server.getPlayer(name).setMaximumAir(300);
		}
		new Thread() {
			public void run() {
				try {
					sleep(150);
					Player player = PumpkinDiver.server.getPlayer(name);
					if (currentAir != player.getRemainingAir()) {
						onRemainingAirChange(player, currentAir);
					}
					if (wearingDivingHelmet && player.getInventory().getHelmet().getTypeId() != Material.PUMPKIN.getId()) {
						player.setMaximumAir(300);
					}
					else if (!wearingDivingHelmet && player.getInventory().getHelmet().getTypeId() == Material.PUMPKIN.getId()) {
						player.setMaximumAir(3000);
					}
					doPumpkinDiver(name);
				}
				catch (InterruptedException e) {
					doPumpkinDiver(name);
				}
				catch (NullPointerException e) {
					players.remove(name);
				}
			}
		}.start();
	}

	public static void onRemainingAirChange(Player player, int old) {
		if (player.getInventory().getHelmet().getTypeId() == Material.PUMPKIN.getId()) {

			//round up
			int oldPercent = (old + 299) / 300;
			int remaining = (player.getRemainingAir() + 299) / 300;
			//Ignore chat messages for when a player just puts on a helmet or just takes one off
			boolean ignore = old == 300 && player.getRemainingAir() == 3000 || old == 3000 && player.getRemainingAir() == 300;
			if (oldPercent != remaining && !ignore) {
				
				String message = "[";
				for (int i = 0; i < 10; i++) {
					if (i < remaining){
						message += ChatColor.BLUE.toString() + "|";
					}
					else {
						message += ChatColor.RED.toString() + "|";
					}
				}
				message += ChatColor.WHITE.toString() + "]";
				message += String.format(" %d%c Air Remaining.", remaining * 10, '%');
				player.sendMessage(message);
			}
		}
	}
}
