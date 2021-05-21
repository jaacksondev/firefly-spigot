package me.jaackson.fireflies;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public class FirefliesPlugin extends JavaPlugin {

    private static final Random RANDOM = new Random();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::tick, 0, 1);
    }

    private void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();
            if (world.getTime() < this.getConfig().getInt("min-time") || world.getTime() > this.getConfig().getInt("max-time"))
                return;

            Location loc = player.getLocation();
            for (int i = 0; i < this.getConfig().getInt("attempts"); ++i) {
                this.spawnFireflies(player, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), this.getConfig().getInt("radius"));
            }
        }
    }

    private void spawnFireflies(Player player, int x, int y, int z, int offset) {
        x += -offset + RANDOM.nextInt(2 * offset);
        y += -offset + RANDOM.nextInt(2 * offset);
        z += -offset + RANDOM.nextInt(2 * offset);
        Block block = player.getWorld().getBlockAt(x, y, z);
        if (block.getType() != Material.matchMaterial(Objects.requireNonNull(this.getConfig().getString("block"))))
            return;

        if (block.getLightFromSky() < this.getConfig().getInt("min-sky-light") && block.getLightFromSky() > this.getConfig().getInt("max-sky-light"))
            return;

        if (RANDOM.nextInt(this.getConfig().getInt("weight")) != 0)
            return;

        player.spawnParticle(
                Particle.valueOf(Objects.requireNonNull(this.getConfig().getString("particle"))),
                x, y, z,
                this.getConfig().getInt("count"),
                this.getConfig().getDouble("spawn-scale-x"),
                this.getConfig().getDouble("spawn-scale-y"),
                this.getConfig().getDouble("spawn-scale-z"),
                this.getConfig().getDouble("speed"),
                null);
    }
}
