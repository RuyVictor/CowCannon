package org.mineacademy.cowcannon.command;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.mineacademy.cowcannon.CowCannon;

import java.util.*;

public final class TagCommand implements CommandExecutor {

	@Override
	public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");

			return true;
		}

		if (args.length != 1)
			return false;

		// /tag <X>
		String tag = args[0];
		Player player = (Player) sender;
		UUID uniqueId = player.getUniqueId();

		if ("reset".equals(tag)) {
			CowCannon.getPlayerTags().remove(uniqueId);

			player.sendMessage(ChatColor.GREEN + "Your tag has been reset.");

		} else {
			tag = ChatColor.translateAlternateColorCodes('&', tag);

			if (tag.length() > 16)
				tag = tag.substring(0, 16);

			CowCannon.getPlayerTags().put(uniqueId, tag);
			player.sendMessage(ChatColor.GREEN + "Your tag has been set to " + tag + ChatColor.GREEN + ".");
		}

		ServerPlayer handle = ((CraftPlayer) player).getHandle();

		for (Player online : Bukkit.getOnlinePlayers()) {
			ServerGamePacketListenerImpl connection = ((CraftPlayer) online).getHandle().connection;

			connection.sendPacket(new ClientboundPlayerInfoRemovePacket(Arrays.asList(handle.getUUID())));

			connection.sendPacket(new ClientboundPlayerInfoUpdatePacket(
					ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, handle));

			ServerEntity npcServerEntity = new ServerEntity(handle.serverLevel(), handle, 0, false, packet -> {
			}, new HashSet<>());

			if (!online.equals(player)) {
				connection.sendPacket(new ClientboundRemoveEntitiesPacket(handle.getId()));
				connection.sendPacket(new ClientboundAddEntityPacket(handle, npcServerEntity));
			}
		}

		return true;
	}
}
