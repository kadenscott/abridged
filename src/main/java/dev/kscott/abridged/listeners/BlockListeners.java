package dev.kscott.abridged.listeners;

import com.google.inject.Inject;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

/**
 * Listens on {@link org.bukkit.event.block.BlockEvent}s.
 */
public class BlockListeners implements Listener {

    /**
     * The base block regeneration time, in ticks.
     */
    private static final int BASE_REGEN_TIME = 5*20;

    /**
     * {@link Plugin} reference.
     */
    private final @NonNull Plugin plugin;

    /**
     * {@link Random} instance.
     */
    private final @NonNull Random random;

    /**
     * Constructs {@link BlockListeners}.
     *
     * @param plugin {@link Plugin} reference.
     */
    @Inject
    public BlockListeners(
            final @NonNull Plugin plugin
    ) {
        this.plugin = plugin;
        this.random = new Random();
    }

    /**
     * Listens on {@link BlockBreakEvent} and reconstructs the block a few seconds later.
     *
     * @param event {@link BlockBreakEvent}.
     */
    @EventHandler
    public void onBlockBreak(final @NonNull BlockBreakEvent event) {
        final @NonNull Block block = event.getBlock();

        final @NonNull BlockState state = block.getState(true);

        final int regenTimeModifier = random.nextInt(20)-10;

        new BukkitRunnable() {
            @Override
            public void run() {
                state.update(true);
            }
        }.runTaskLater(plugin, BASE_REGEN_TIME + regenTimeModifier);
    }

}
