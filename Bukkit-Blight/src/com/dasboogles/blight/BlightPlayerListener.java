package com.dasboogles.blight;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BlightPlayerListener extends PlayerListener {
	private final BlightPlugin plugin;

    public BlightPlayerListener(BlightPlugin instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {

    }
}
