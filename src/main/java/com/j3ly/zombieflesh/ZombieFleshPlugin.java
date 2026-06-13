package com.j3ly.zombieflesh;

import java.util.List;
import java.util.Random;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ZombieFleshPlugin extends JavaPlugin {

    private final Random random = new Random();

    private final EntityListener entityListener = new EntityListener() {
        @Override
        public void onEntityDeath(EntityDeathEvent event) {
            Entity entity = event.getEntity();
            if (entity instanceof Zombie) {
                List<ItemStack> drops = event.getDrops();
                drops.clear(); // Remove default drops

                // 50/50 chance to drop feather or rotten flesh (ID 305)
                if (random.nextBoolean()) {
                    drops.add(new ItemStack(288, 1)); // Feather
                } else {
                    drops.add(new ItemStack(305, 1)); // Rotten Flesh
                }
            }
        }
    };

    private final PlayerListener playerListener = new PlayerListener() {
        @Override
        public void onPlayerInteract(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            ItemStack item = player.getItemInHand();

            if (item != null && item.getTypeId() == 305) {
                // Heal 3 hearts (6 health), max 20
                double newHealth = Math.min(player.getHealth() + 6, 20);
                player.setHealth((int) newHealth);
                // Consume item
                int amount = item.getAmount();
                if (amount > 1) {
                    item.setAmount(amount - 1);
                } else {
                    player.setItemInHand(null);
                }
            }
        }
    };

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
    }

    @Override
    public void onDisable() {
    }
}
