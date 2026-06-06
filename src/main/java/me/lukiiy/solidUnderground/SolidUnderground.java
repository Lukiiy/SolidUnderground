package me.lukiiy.solidUnderground;

import net.minecraft.server.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SolidUnderground extends JavaPlugin {
    private static SolidUnderground instance = null;

    private final Map<Player, RunData> runMap = new HashMap<>();
    private State state = State.INACTIVE;
    private int worldTickTask = -1;

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

        AtomicBoolean warnedGettingDark = new AtomicBoolean(false);
        AtomicBoolean warnedNight = new AtomicBoolean(false);

        worldTickTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            long time = world.getTime();

            if (time < 11000) {
                warnedGettingDark.set(false);
                warnedNight.set(false);
            }

            if (time >= 11000 && !warnedGettingDark.get()) {
                warnedGettingDark.set(true);

                Bukkit.getServer().broadcastMessage("§cIt's getting dark. All players must head into the hole!");
            }

            if (time >= 13000 && !warnedNight.get()) {
                warnedNight.set(true);

                Bukkit.getServer().broadcastMessage("§cIt's dark. All players must be in the hole.");
            }

            if (time >= 15000 && time < 23000) Arrays.stream(Bukkit.getServer().getOnlinePlayers())
                    .filter(p -> {
                        RunData data = runMap.get(p);

                        return data != null && !data.inHole;
                    }).forEach(p -> p.damage(2));
        }, 20L, 20L); // every second
    }

    public void eliminate(Player player) {
        if (runMap.get(player) == null) return;

        runMap.get(player).times.put("end", getElapsedTime());
    }

    public State getState() {
        return state;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public String getFormattedTime(long time) {
        if (time == 0) return "0:00";

        long total = getElapsedTime() / 1000;
        long hours = total / 3600;
        long minutes = (total % 3600) / 60;
        long seconds = total % 60;

        if (hours > 0) return String.format("%d:%02d:%02d", hours, minutes, seconds);
        else return String.format("%d:%02d", minutes, seconds);
    }

    public String getFormattedTime() {
        return getFormattedTime(getElapsedTime());
    }
}
