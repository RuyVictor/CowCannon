package org.mineacademy.cowcannon.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.haoshoku.nick.api.NickAPI;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");

        for (Player online : Bukkit.getOnlinePlayers()) {
            NickAPI.setNick(online, "TESTE");
            online.sendMessage(Component.text("Player " + online.getName()));
        }
    }
}