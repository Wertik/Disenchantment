package java.me.wertik.disenchant.commands;

import java.me.wertik.disenchant.Main;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

    private Main plugin;

    public Commands() {
        plugin = Main.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("ve")) {

            if (sender instanceof Player) {

                Player p = (Player) sender;

                if (p.hasPermission("disenchant.use") || p.isOp()) {

                    if (!p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {

                        ItemStack item = p.getInventory().getItemInMainHand();

                        if (item.hasItemMeta()) {
                            if (item.getItemMeta().hasEnchants()) {

                                // Disenchant
                                p.getInventory().setItemInMainHand(plugin.disenchant(item));

                                // Effects                                if (plugin.getConfig().getBoolean("Effects.sound"))
                                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
                                if (plugin.getConfig().getBoolean("Effects.particle"))
                                    p.getLocation().getWorld().spigot().playEffect(p.getLocation(), Effect.WITCH_MAGIC, 0, 0, 1, 1, 1, 1, 20, 20);

                                p.sendMessage(plugin.getMessage("disenchanted"));
                                return true;

                            } else
                                p.sendMessage(plugin.getMessage("needs-to-be-enchanted"));
                        } else
                            p.sendMessage(plugin.getMessage("needs-to-be-enchanted"));
                    } else
                        p.sendMessage(plugin.getMessage("need-to-hold-an-item"));
                } else
                    p.sendMessage(plugin.getMessage("no-perms"));
            } else
                sender.sendMessage(plugin.getMessage("players-only"));

            return false;
        } else if (cmd.getName().equalsIgnoreCase("vereload")) {

            if (sender instanceof Player || sender.hasPermission("disenchant.reload") || sender.isOp()) {

                plugin.reloadConfig();
                sender.sendMessage(plugin.getMessage("reloaded"));
                return true;
            } else
                sender.sendMessage(plugin.getMessage("no-perms"));
        }
        return false;
    }
}
