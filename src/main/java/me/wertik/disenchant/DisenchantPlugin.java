package me.wertik.disenchant;

import com.google.common.base.Strings;
import me.wertik.disenchant.commands.DisenchantCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.DevportPlugin;
import space.devport.utils.UsageFlag;
import space.devport.utils.xseries.XSound;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DisenchantPlugin extends DevportPlugin {

    private XSound sound;

    private float volume;
    private float pitch;

    @Override
    public void onPluginEnable() {
        loadOptions();

        new DisenchantLanguage(this);
        addMainCommand(new DisenchantCommand(this));
    }

    @Override
    public void onPluginDisable() {

    }

    @Override
    public void onReload() {
        loadOptions();
    }

    public void play(Player target) {
        getSound().ifPresent(x -> x.play(target, volume, pitch));
    }

    private void loadOptions() {

        this.sound = null;

        ConfigurationSection section = getConfig().getConfigurationSection("sound");

        if (section == null)
            return;

        String soundName = section.getString("type");

        if (!Strings.isNullOrEmpty(soundName)) {
            XSound.matchXSound(soundName).ifPresent(x -> {
                this.sound = x;
                this.volume = section.getLong("volume");
                this.pitch = section.getLong("pitch");
            });
        }

        if (this.sound == null)
            ConsoleOutput.getInstance().warn("Sound type invalid.");
    }

    public Optional<XSound> getSound() {
        return Optional.ofNullable(sound);
    }

    @Override
    public UsageFlag[] usageFlags() {
        return new UsageFlag[]{UsageFlag.COMMANDS, UsageFlag.CONFIGURATION, UsageFlag.LANGUAGE};
    }

    public ItemStack disenchant(ItemStack item, AtomicInteger count) {

        if (item == null || item.getItemMeta() == null || !item.getItemMeta().hasEnchants())
            return item;

        ItemMeta meta = item.getItemMeta();

        count.set(meta.getEnchants().size());

        meta.getEnchants().keySet().forEach(meta::removeEnchant);

        item.setItemMeta(meta);

        return item;
    }
}
