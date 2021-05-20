package me.jaackson.spigotdemo.fireflies;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import me.jaackson.spigotdemo.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.Random;

public class FireflyListener implements Listener {

    private static final Random RANDOM = new Random();

    @EventHandler
    public void onEvent(ServerTickEndEvent event) {
        Bukkit.getOnlinePlayers().forEach(this::tick);
    }

    private void tick(Player player) {
        World world = player.getWorld();
        FileConfiguration config = Main.getInstance().config;
        if (world.getTime() < config.getInt("min-time") && world.getTime() > config.getInt("max-time"))
            return;

        Location loc = player.getLocation();
        for (int m = 0; m < config.getInt("attempts"); ++m) {
            this.animate(world, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), config.getInt("radius"));
        }
    }

    public void animate(World world, int x, int y, int z, int offset) {
        FileConfiguration config = Main.getInstance().config;
        x += RANDOM.nextInt(offset) - RANDOM.nextInt(offset);
        y += RANDOM.nextInt(offset) - RANDOM.nextInt(offset);
        z += RANDOM.nextInt(offset) - RANDOM.nextInt(offset);
        Block block = world.getBlockAt(x, y, z);
        if (block.getType() != Enum.valueOf(Material.class, Objects.requireNonNull(config.getString("block"))))
            return;

        if (block.getLightFromSky() < config.getInt("min-sky-light") && block.getLightFromSky() > config.getInt("max-sky-light"))
            return;

        if (RANDOM.nextInt(config.getInt("chance")) != 0)
            return;

        world.spawnParticle(
                Enum.valueOf(Particle.class, Objects.requireNonNull(config.getString("particle"))),
                x, y, z,
                1,
                config.getDouble("spawn-scale-x"),
                config.getDouble("spawn-scale-y"),
                config.getDouble("spawn-scale-z"),
                config.getDouble("speed"),
                null);
    }
}
