package me.jaackson.spigotdemo;

import me.jaackson.spigotdemo.fireflies.FireflyListener;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;
    public FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        instance = this;

        this.config.addDefault("particle", Particle.END_ROD.name());
        this.config.addDefault("speed", 0.05);
        this.config.addDefault("chance", 25);
        this.config.addDefault("block", Material.GRASS.name());
        this.config.addDefault("attempts", 500);

        this.config.addDefault("min-sky-light", 12);
        this.config.addDefault("max-sky-light", 15);
        this.config.addDefault("min-time", 13000);
        this.config.addDefault("max-time", 999);

        this.config.addDefault("spawn-scale-x", 5);
        this.config.addDefault("spawn-scale-y", 5);
        this.config.addDefault("spawn-scale-z", 5);
        this.config.addDefault("radius", 32);

        this.config.options().copyDefaults(true);
        this.saveConfig();

        this.getServer().getPluginManager().registerEvents(new FireflyListener(), this);
        System.out.println("Enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("Disabled!");
    }

    public static Main getInstance() {
        return instance;
    }

}
