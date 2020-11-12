package net.okocraft.dailyrewards.command.subcommand;

import com.github.siroshun09.mccommand.common.AbstractCommand;
import com.github.siroshun09.mccommand.common.CommandResult;
import com.github.siroshun09.mccommand.common.context.CommandContext;
import com.github.siroshun09.mccommand.common.sender.Sender;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
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
    public @NotNull CommandResult onExecution(@NotNull CommandContext context) {
        Sender sender = context.getSender();

        if (!sender.hasPermission(getPermission())) {
            plugin.getMessageBuilder().sendNoPermission(sender, this);
            return CommandResult.NO_PERMISSION;
        }

        Player target = plugin.getServer().getPlayer(sender.getUUID());

        if (target != null) {
            plugin.getProcessors().getPlayerReceiveProcessor().tryReceive(target);
            return CommandResult.SUCCESS;
        } else {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RECEIVE_ONLY_PLAYER, sender);
            return CommandResult.NOT_PLAYER;
        }
    }

    @Override
    public @NotNull List<String> onTabCompletion(@NotNull CommandContext context) {
        return Collections.emptyList();
    }
}
