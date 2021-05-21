package me.jaackson.fireflies;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public class FirefliesPlugin extends JavaPlugin implements Listener {

    private static final Random RANDOM = new Random();
    private final FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        this.config.addDefault("particle", Particle.END_ROD.name());
        this.config.addDefault("block", Material.GRASS.name());
        this.config.addDefault("speed", 0.05);

        this.config.addDefault("weight", 25);
        this.config.addDefault("attempts", 500);
        this.config.addDefault("radius", 32);

        this.config.addDefault("spawn-scale-x", 5);
        this.config.addDefault("spawn-scale-y", 5);
        this.config.addDefault("spawn-scale-z", 5);

        this.config.addDefault("min-sky-light", 12);
        this.config.addDefault("max-sky-light", 15);
        this.config.addDefault("min-time", 13000);
        this.config.addDefault("max-time", 23500);

        this.config.options().copyDefaults(true);
        this.saveConfig();

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEvent(ServerTickEndEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            World world = player.getWorld();
            if (world.getTime() < this.config.getInt("min-time") || world.getTime() > this.config.getInt("max-time"))
                return;

            Location loc = player.getLocation();
            for (int m = 0; m < this.config.getInt("attempts"); ++m) {
                this.spawnFireflies(world, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), this.config.getInt("radius"));
            }
        });
    }

    private void spawnFireflies(World world, int x, int y, int z, int offset) {
        x += RANDOM.nextInt(offset) - RANDOM.nextInt(offset);
        y += RANDOM.nextInt(offset) - RANDOM.nextInt(offset);
        z += RANDOM.nextInt(offset) - RANDOM.nextInt(offset);
        Block block = world.getBlockAt(x, y, z);
        if (block.getType() != Enum.valueOf(Material.class, Objects.requireNonNull(this.config.getString("block"))))
            return;

        if (block.getLightFromSky() < this.config.getInt("min-sky-light") && block.getLightFromSky() > this.config.getInt("max-sky-light"))
            return;

        if (RANDOM.nextInt(this.config.getInt("weight")) != 0)
            return;

        world.spawnParticle(
                Enum.valueOf(Particle.class, Objects.requireNonNull(this.config.getString("particle"))),
                x, y, z,
                1,
                this.config.getDouble("spawn-scale-x"),
                this.config.getDouble("spawn-scale-y"),
                this.config.getDouble("spawn-scale-z"),
                this.config.getDouble("speed"),
                null);
    }
}
