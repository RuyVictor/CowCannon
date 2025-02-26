package org.mineacademy.cowcannon.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.mineacademy.cowcannon.CowCannon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class ProtocolLibHook {

	public static void register() {
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();

		manager.addPacketListener(new PacketAdapter(CowCannon.getInstance(), PacketType.Play.Server.PLAYER_INFO) {

			@Override
			public void onPacketSending(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				event.getPlayer().sendMessage(packet.getPlayerInfoActions().read(0).toString());
				event.getPlayer().sendMessage(String.valueOf(packet.getPlayerInfoActions().read(0).contains(EnumWrappers.PlayerInfoAction.ADD_PLAYER)));

				if (packet.getPlayerInfoActions().read(0).contains(EnumWrappers.PlayerInfoAction.ADD_PLAYER)) {
					List<PlayerInfoData> list = packet.getPlayerInfoDataLists().read(1);

					PlayerInfoData data = list.get(0);

					if (data == null)
						return;

					UUID uniqueId = data.getProfile().getUUID();

					if (CowCannon.getPlayerTags().containsKey(uniqueId)) {
						String tag = CowCannon.getPlayerTags().get(uniqueId);

						PlayerInfoData newPlayerInfo = new PlayerInfoData(
								new WrappedGameProfile(uniqueId, tag),
								data.getLatency(),
								data.getGameMode(),
								WrappedChatComponent.fromLegacyText(tag));

						if (tag != null)
							packet.getPlayerInfoDataLists().write(1, Collections.singletonList(newPlayerInfo));
					}
				}
			}
		});
	}
}
