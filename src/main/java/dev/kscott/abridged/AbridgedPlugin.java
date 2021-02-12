package dev.kscott.abridged;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.kscott.abridged.inject.PluginModule;
import dev.kscott.abridged.listeners.BlockListeners;
import dev.kscott.abridged.listeners.OskarListener;
import dev.kscott.abridged.listeners.PingListener;
import dev.kscott.abridged.listeners.PlayerListeners;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The base {@link AbridgedPlugin} class.
 */
public final class AbridgedPlugin extends JavaPlugin {

    /**
     * Sets up listeners.
     */
    @Override
    public void onEnable() {
        final @NonNull Injector injector = Guice.createInjector(
                new PluginModule(this)
        );

        this.getServer().getPluginManager().registerEvents(injector.getInstance(BlockListeners.class), this);
        this.getServer().getPluginManager().registerEvents(injector.getInstance(OskarListener.class), this);
        this.getServer().getPluginManager().registerEvents(injector.getInstance(PingListener.class), this);
        this.getServer().getPluginManager().registerEvents(injector.getInstance(PlayerListeners.class), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
