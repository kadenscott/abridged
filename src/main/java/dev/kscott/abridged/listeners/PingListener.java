package dev.kscott.abridged.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on the ServerListPingEvent.
 */
public class PingListener implements Listener {

    /**
     * The {@link Component} to display on the MOTD.
     */
    private static final @NonNull Component motdComponent = MiniMessage.get().parse(
            "<dark_aqua>Minecraft:</dark_aqua> <aqua>A bridge(d).</aqua>\n" +
                    "<gold><bold>https://chat.ksc.sh</bold></gold>"
    );

    /**
     * Displays custom MOTD on ping.
     *
     * @param event {@link PaperServerListPingEvent}.
     */
    @EventHandler
    public void onServerListPingEvent(final @NonNull PaperServerListPingEvent event) {
        event.setMaxPlayers(-5);
        event.setMotd(LegacyComponentSerializer.legacySection().serialize(motdComponent));
    }

}
