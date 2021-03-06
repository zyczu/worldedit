/*
 * WorldEdit
 * Copyright (C) 2012 sk89q <http://www.sk89q.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.bukkit;

import com.sk89q.worldedit.LocalSession;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.Charset;

/**
 * Handles incoming WorldEditCui init message
 * @author zml2008
 */
public class CUIChannelListener implements PluginMessageListener {
    public static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");
    private final WorldEditPlugin plugin;

    public CUIChannelListener(WorldEditPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        LocalSession session = plugin.getSession(player);
        if (session.hasCUISupport()) { // Already initialized
            return;
        }

        String[] text = new String(message, UTF_8_CHARSET).split("\\|");
        if (text.length > 1 && text[0].equalsIgnoreCase("v")) { // enough fields and right message
            plugin.setPluginChannelCUI(player.getName(), true);
            session.setCUISupport(true);
            try {
                session.setCUIVersion(Integer.parseInt(text[1]));
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Error while reading CUI init message: " + e.getMessage());
            }
        }
    }
}
