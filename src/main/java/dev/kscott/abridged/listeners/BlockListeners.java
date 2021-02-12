package dev.kscott.abridged.listeners;

import com.google.inject.Inject;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
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
    private static final int BASE_REGEN_TIME = 5 * 20;

    /**
     * {@link Plugin} reference.
     */
    private final @NonNull Plugin plugin;

    /**
     * {@link Random} instance.
     */
    private final @NonNull Random random;

    /**
     * {@link BukkitAudiences} reference.
     */
    private final @NonNull BukkitAudiences audiences;

    /**
     * Constructs {@link BlockListeners}.
     *
     * @param plugin {@link Plugin} reference.
     */
    @Inject
    public BlockListeners(
            final @NonNull Plugin plugin,
            final @NonNull BukkitAudiences audiences
    ) {
        this.plugin = plugin;
        this.random = new Random();
        this.audiences = audiences;
    }

    /**
     * Listens on chest open.
     *
     * @param event {@link PlayerInteractEvent}.
     */
    @EventHandler
    public void onChestOpen(final @NonNull InventoryOpenEvent event) {
        final @NonNull String title = event.getView().getTitle();

        if (title.equals("Obsidian")) {
            final @NonNull Inventory inventory = event.getInventory();
            inventory.clear();

            final @NonNull ItemStack itemStack = new ItemStack(Material.OBSIDIAN);
            itemStack.setAmount(10);

            inventory.addItem(itemStack);
        } else if (title.equals("Lighter")) {
            final @NonNull Inventory inventory = event.getInventory();
            inventory.clear();

            final @NonNull ItemStack itemStack = new ItemStack(Material.FLINT_AND_STEEL);

            inventory.addItem(itemStack);
        }


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

        final int regenTimeModifier = random.nextInt(20) - 10;

        new BukkitRunnable() {
            @Override
            public void run() {
                state.update(true);
            }
        }.runTaskLater(plugin, BASE_REGEN_TIME + regenTimeModifier);
    }

    /**
     * Handles the lever toggle.
     *
     * @param event {@link PlayerInteractEvent}.
     */
    @EventHandler
    public void onLeverToggle(final @NonNull PlayerInteractEvent event) {
        final @Nullable Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        if (block.getType() != Material.LEVER) {
            return;
        }

        if (block.getWorld().getEnvironment() != World.Environment.THE_END) {
            return;
        }

        final @NonNull Collection<EnderDragon> dragons = PlayerListeners.endDragonLocation.getNearbyEntitiesByType(EnderDragon.class, 5, 5, 5);

        if (!dragons.isEmpty()) {
            this.audiences.player(event.getPlayer()).sendMessage(
                    MiniMessage.get().parse("<red>" + event.getPlayer().getName() + " has completed the game!")
            );
            for (final @NonNull EnderDragon dragon : dragons) {
                dragon.setHealth(0);
            }
        }

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
        final int choice = random.nextInt(dropperItemTypes.length - 1);
        final @NonNull Material material = dropperItemTypes[choice];

        final int amount = random.nextInt(2) + 1;

        final @NonNull ItemStack stack = new ItemStack(material);
        stack.setAmount(amount);

        event.setItem(stack);
    }

}
