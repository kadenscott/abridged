package dev.kscott.abridged.inject;

import com.google.inject.AbstractModule;
import dev.kscott.abridged.AbridgedPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides plugin-related objects.
 */
public class PluginModule extends AbstractModule {

    /**
     * {@link AbridgedPlugin} reference.
     */
    private final @NonNull AbridgedPlugin plugin;

    /**
     * Constructs {@link PluginModule}.
     *
     * @param plugin {@link AbridgedPlugin} reference.
     */
    public PluginModule(final @NonNull AbridgedPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Configures {@link PluginModule} to provide plugin instances.
     */
    public void configure() {
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(JavaPlugin.class).toInstance(this.plugin);
        this.bind(AbridgedPlugin.class).toInstance(this.plugin);
    }
}
