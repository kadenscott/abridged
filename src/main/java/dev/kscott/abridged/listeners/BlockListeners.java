package dev.kscott.abridged.listeners;

import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Random;

/**
 * Listens on {@link org.bukkit.event.block.BlockEvent}s.
 */
public class BlockListeners implements Listener {

    /**
     * An array of {@link Material}s that can be dispensed by a dropper.
     */
    private static final Material[] dropperItemTypes = new Material[]{
            Material.BLAZE_ROD,
            Material.BLAZE_ROD,
            Material.BLAZE_ROD,
            Material.BLAZE_ROD,
            Material.ENDER_PEARL,
            Material.ENDER_PEARL,
            Material.ENDER_PEARL,
            Material.ENDER_PEARL,
            Material.ENDER_PEARL,
            Material.PORKCHOP,
            Material.PORKCHOP,
            Material.PORKCHOP,
            Material.PORKCHOP,
            Material.PORKCHOP,
            Material.DIAMOND_SWORD,
            Material.DIAMOND_PICKAXE,
    };

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

    /**
     * Cancels all dropper interactions.
     *
     * @param event {@link PlayerInteractEvent}.
     */
    @EventHandler
    public void onDropperOpen(final @NonNull PlayerInteractEvent event) {
        final @Nullable Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        if (block.getType() != Material.DROPPER) {
            return;
        }

        event.setCancelled(true);
    }

    /**
     * Drops a predefined item when droppers are activated.
     *
     * @param event {@link BlockDispenseEvent}.
     */
    @EventHandler
    public void onDropperDrop(final @NonNull BlockDispenseEvent event) {
        final int choice = random.nextInt(dropperItemTypes.length);
        final @NonNull Material material = dropperItemTypes[choice-1];

        final int amount = random.nextInt(2)+1;

        final @NonNull ItemStack stack = new ItemStack(material);
        stack.setAmount(amount);

        event.setItem(stack);
    }

}
