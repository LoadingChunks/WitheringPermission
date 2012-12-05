package net.loadingchunks.plugins.WitheringPermission;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WitheringPermission extends JavaPlugin {

	// ClassListeners
	private WitheringPermissionCommandExecutor commandExecutor;
	private WitheringPermissionEventListener eventListener;
	public HashMap<String, PermissionAttachment> attachments = new HashMap<String,PermissionAttachment>();
	// ClassListeners

	public void onDisable() {

	}

	public void onEnable() {
		this.eventListener = new WitheringPermissionEventListener(this);
		this.commandExecutor = new WitheringPermissionCommandExecutor(this);

		PluginManager pm = this.getServer().getPluginManager();

		getCommand("witherperm").setExecutor(commandExecutor);
		
		this.getConfig();
		this.getConfig().addDefault("prerequisite", "lc.vip");
		
		ArrayList<String> groups = new ArrayList<String>();
		groups.add("Donator_RegionRetail");
		
		this.getConfig().addDefault("groups", groups);
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		// you can register multiple classes to handle events if you want
		// just call pm.registerEvents() on an instance of each class
		pm.registerEvents(eventListener, this);
	}
}