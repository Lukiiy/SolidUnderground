package me.lukiiy.solidUnderground;

import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SolidUnderground extends JavaPlugin {
    private static SolidUnderground instance = null;

    private Map<Player, RunData> runMap = new HashMap<>();
    private State state = State.INACTIVE;

    private long startTime = 0;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() { }

    public static SolidUnderground getInstance() {
        return instance;
    }

    public void start(World world) {
        if (state == State.ACTIVE) return;

        if (world.getSeed() != 404) {
            getServer().broadcastMessage("§cThe world's seed is not \"404\"!");
            return;
        }

        state = State.ACTIVE;
        startTime = System.currentTimeMillis();
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();

        Location hole = new Location(world, -25, 66, 21);

        nmsWorld.spawnMonsters = 3; // hard difficulty
        world.setWeatherDuration(0);
        world.setThundering(false);
        world.setTime(0);

        getServer().broadcastMessage("§aThe run has started!");

        Arrays.stream(getServer().getOnlinePlayers()).forEach(p -> {
            RunData data = new RunData();

            data.times.put("start", startTime);

            p.teleport(world.getSpawnLocation());
            runMap.put(p, data);
        });
    }

    public State getState() {
        return state;
    }

    public long getStartTime() {
        return startTime;
    }
}
