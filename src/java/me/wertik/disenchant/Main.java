package java.me.wertik.disenchant;

import java.me.wertik.disenchant.commands.Commands;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {return instance;}

    private Commands commands;

    @Override
    public void onEnable() {
        info("§5Heating up the disenchant plugin for ya. v. §f" + getDescription().getVersion());
        info("§f----------------------");

        instance = this;
        commands = new Commands();

        info("§aClasses loaded");

        getCommand("ve").setExecutor(commands);
        getCommand("vereload").setExecutor(commands);

        info("§aCommands registered");

        loadConfig();

        info("§aConfig loaded");

        info("§f----------------------");
        info("§5Done..");
    }

    private void info(String msg) {
        getServer().getConsoleSender().sendMessage("[Disenchant] " + msg);
    }

    public ItemStack disenchant(ItemStack item) {

        if (!item.hasItemMeta())
            return item;
        if (!item.getItemMeta().hasEnchants())
            return item;

        ItemMeta itemMeta = item.getItemMeta();

        for (Enchantment enchantment : itemMeta.getEnchants().keySet()) {
            itemMeta.removeEnchant(enchantment);
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    public void loadConfig() {
        // CF
        File configFile = new File(getDataFolder() + "/config.yml");

        if (!configFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
            info("§aGenerated default §f" + configFile.getName());
        }
    }

    public String getMessage(String name) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages."+name));
    }
}
