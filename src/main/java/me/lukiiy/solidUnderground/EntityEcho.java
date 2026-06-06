package me.lukiiy.solidUnderground;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class EntityEcho extends EntityListener {
    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();

        SolidUnderground.getInstance().eliminate(player);
        Bukkit.getServer().broadcastMessage(player.getDisplayName() + " §fhas been §celiminated§f! §6[" + SolidUnderground.getInstance().getFormattedTime() + "]");
    }
}
