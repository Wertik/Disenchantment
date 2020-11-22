package me.wertik.disenchant.commands;

import me.wertik.disenchant.DisenchantPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.text.language.LanguageManager;

import java.util.concurrent.atomic.AtomicInteger;

public class DisenchantCommand extends MainCommand {

    private final DisenchantPlugin plugin;

    public DisenchantCommand(DisenchantPlugin plugin) {
        super("disenchant");
        this.plugin = plugin;
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player target;
        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("disenchantment.reload")) {
                plugin.reload(sender);
                return CommandResult.SUCCESS;
            }

            target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                plugin.getManager(LanguageManager.class).getPrefixed("Commands.Invalid-Player")
                        .replace("%param%", args[0])
                        .send(sender);
                return CommandResult.FAILURE;
            }

            if (!sender.hasPermission("disenchantment.use.others"))
                return CommandResult.NO_PERMISSION;
        } else {
            if (!(sender instanceof Player))
                return CommandResult.NO_CONSOLE;

            target = (Player) sender;

            if (!sender.hasPermission("disenchantment.use"))
                return CommandResult.NO_PERMISSION;
        }

        ItemStack item = target.getInventory().getItemInMainHand();

        if (item.getType().isAir()) {
            language.sendPrefixed(target, "Commands.Disenchant.No-Air");
            return CommandResult.FAILURE;
        }

        AtomicInteger count = new AtomicInteger();

        target.getInventory().setItemInMainHand(plugin.disenchant(item, count));
        language.getPrefixed("Commands.Disenchant.Done")
                .replace("%count%", count.get())
                .send(sender);

        plugin.play(target);
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% (player)";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "Disenchant an item.";
    }
}
