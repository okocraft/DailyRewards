package net.okocraft.dailyrewards.command.subcommand;

import com.github.siroshun09.mccommand.bukkit.argument.parser.BukkitParser;
import com.github.siroshun09.mccommand.common.AbstractCommand;
import com.github.siroshun09.mccommand.common.CommandResult;
import com.github.siroshun09.mccommand.common.argument.Argument;
import com.github.siroshun09.mccommand.common.context.CommandContext;
import com.github.siroshun09.mccommand.common.sender.Sender;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import net.okocraft.dailyrewards.lang.Placeholders;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ResetCommand extends AbstractCommand {

    private final DailyRewards plugin;

    public ResetCommand(@NotNull DailyRewards plugin) {
        super("reset", "dailyrewards.command.reset", Set.of("re"));

        this.plugin = plugin;
    }


    @Override
    public @NotNull CommandResult onExecution(@NotNull CommandContext context) {
        Sender sender = context.getSender();

        if (!sender.hasPermission(getPermission())) {
            plugin.getMessageBuilder().sendNoPermission(sender, this);
            return CommandResult.NO_PERMISSION;
        }

        List<Argument> arguments = context.getArguments();

        if (arguments.size() < 2) {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.HELP_RESET, sender);
            return CommandResult.NO_ARGUMENT;
        }

        Argument secondArgument = arguments.get(1);

        if (secondArgument.get().equalsIgnoreCase("all")) {
            if (plugin.getReceiveData().reset()) {
                plugin.getReceiveData().saveAsync();
                plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RESET_ALL, sender);
                return CommandResult.SUCCESS;
            } else {
                plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RESET_NO_CHANGE, sender);
                return CommandResult.STATE_ERROR;
            }
        }

        OfflinePlayer target = BukkitParser.OFFLINE_PLAYER.parse(secondArgument);

        if (target == null) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_RESET_TARGET_NOT_FOUND, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .send(sender);

            return CommandResult.INVALID_ARGUMENTS;
        }

        if (false && !target.hasPlayedBefore()) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_RESET_TARGET_NOT_JOINED, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .send(sender);

            return CommandResult.STATE_ERROR;
        }

        boolean changed = plugin.getReceiveData().setReceived(target.getUniqueId(), false);

        plugin.getReceiveData().saveAsync();

        if (changed) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_RESET_PLAYER, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .replace(Placeholders.UUID, target.getUniqueId())
                    .send(sender);

            return CommandResult.SUCCESS;
        } else {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RESET_NO_CHANGE, sender);
            return CommandResult.STATE_ERROR;
        }
    }

    @Override
    public @NotNull List<String> onTabCompletion(@NotNull CommandContext context) {
        List<Argument> arguments = context.getArguments();
        Sender sender = context.getSender();

        if (!sender.hasPermission(getPermission())) {
            return Collections.emptyList();
        }

        if (arguments.size() == 2) {
            List<String> result = new ArrayList<>();

            result.add("all");

            plugin.getReceiveData().getReceivedPlayers()
                    .stream()
                    .map(plugin.getServer()::getOfflinePlayer)
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(() -> result));

            return StringUtil.copyPartialMatches(
                    arguments.get(1).get(),
                    result,
                    new ArrayList<>()
            );
        }

        return Collections.emptyList();
    }
}
