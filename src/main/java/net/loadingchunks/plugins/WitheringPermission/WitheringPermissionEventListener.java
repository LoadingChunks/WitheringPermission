package net.loadingchunks.plugins.WitheringPermission;

/*
    This file is part of WitheringPermission

    Foobar is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Foobar is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class WitheringPermissionEventListener implements Listener {

	private WitheringPermission plugin;

	public WitheringPermissionEventListener(WitheringPermission plugin) {
		this.plugin = plugin;
	}

	// This is just one possible event you can hook.
	// See http://jd.bukkit.org/apidocs/ for a full event list.

	// All event handlers must be marked with the @EventHandler annotation 
	// The method name does not matter, only the type of the event parameter
	// is used to distinguish what is handled.

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		PermissionAttachment attachment = p.addAttachment(this.plugin);
		this.plugin.attachments.put(p.getName(),attachment);
		
		if(p.hasPermission(this.plugin.getConfig().getString("prerequisite")))
			return;
		
		PermissionUser pex = PermissionsEx.getUser(p.getName());
		
		for(String group : this.plugin.getConfig().getStringList("groups"))
		{
			PermissionGroup pgroup = PermissionsEx.getPermissionManager().getGroup(group);
			if(!pex.inGroup(pgroup))
				continue; // Skip it, no point setting them when they're not in the group.
			
			for(String perm : pgroup.getOwnPermissions(p.getWorld().getName()))
			{
				attachment.setPermission(perm, false);
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		PermissionAttachment perm = this.plugin.attachments.get(p.getName());
		if(perm != null)
			p.removeAttachment(perm);
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		Player p = event.getPlayer();
		PermissionAttachment perm = this.plugin.attachments.get(p.getName());
		if(perm != null)
			p.removeAttachment(perm);		
	}
}
