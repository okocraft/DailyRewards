package net.okocraft.dailyrewards.command.subcommand;

import com.github.siroshun09.mccommand.common.AbstractCommand;
import com.github.siroshun09.mccommand.common.context.CommandContext;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ReceiveCommand extends AbstractCommand {

    private final DailyRewards plugin;

    public ReceiveCommand(@NotNull DailyRewards plugin) {
        super("receive", "dailyrewards.command.receive", Set.of("r", "rc"));

        this.plugin = plugin;
    }

    @Override
    public void onExecution(@NotNull CommandContext context) {
        CommandSender sender = context.getSender();

        if (!sender.hasPermission(getPermission())) {
            plugin.getMessageBuilder().sendNoPermission(sender, this);
            return;
        }

        if (sender instanceof Player target) {
            plugin.getProcessors().getPlayerReceiveProcessor().tryReceive(target);
        } else {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RECEIVE_ONLY_PLAYER, sender);
        }
    }

    @Override
    public @NotNull List<String> onTabCompletion(@NotNull CommandContext context) {
        return Collections.emptyList();
    }
}
