package org.mineacademy.cowcannon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.cowcannon.command.TagCommand;
import org.mineacademy.cowcannon.hook.PlaceholderAPIHook;
import org.mineacademy.cowcannon.hook.ProtocolLibHook;
import org.mineacademy.cowcannon.listener.PlayerListener;
import org.mineacademy.cowcannon.model.Scheduler;
import org.mineacademy.cowcannon.setting.CowSettings;

public final class CowCannon extends JavaPlugin {

	private static final Map<UUID, String> playerTags = new HashMap<>();

	private Scheduler.Task task;
	private Scheduler.Task task2;
	private Scheduler.Task task3;
	private Scheduler.Task task4;
	private Scheduler.Task task5;
	private Scheduler.Task task6;

	@Override
	public void onEnable() {

		// Updated for the disappearance of safeguard in 1.20.5+ on Paper. Supports all versions including legacy and Spigot.
		final String bukkitVersion = Bukkit.getServer().getBukkitVersion(); // 1.20.6-R0.1-SNAPSHOT
		final String versionString = bukkitVersion.split("\\-")[0]; // 1.20.6
		final String[] versions = versionString.split("\\.");

		final int version = Integer.parseInt(versions[1]); // 20 in 1.20.6
		//final int subversion = versions.length == 3 ? Integer.parseInt(versions[2]) : 0; // 6 in 1.20.6

		getCommand("tag").setExecutor(new TagCommand());
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);

		CowSettings.getInstance().load();

		if (getServer().getPluginManager().getPlugin("ProtocolLib") != null)
			ProtocolLibHook.register();

		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
			PlaceholderAPIHook.registerHook();
	}

	@Override
	public void onDisable() {

		if (task != null)
			task.cancel();

		if (task2 != null)
			task2.cancel();

		if (task3 != null)
			task3.cancel();

		if (task4 != null)
			task4.cancel();

		if (task5 != null)
			task5.cancel();

		if (task6 != null)
			task6.cancel();

		this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
		this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
	}

	public static Map<UUID, String> getPlayerTags() {
		return playerTags;
	}

	public static CowCannon getInstance() {
		return getPlugin(CowCannon.class);
	}
}
