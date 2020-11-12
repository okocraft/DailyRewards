package net.okocraft.dailyrewards.command.subcommand;

import com.github.siroshun09.mccommand.bukkit.argument.parser.BukkitParser;
import com.github.siroshun09.mccommand.common.AbstractCommand;
import com.github.siroshun09.mccommand.common.CommandResult;
import com.github.siroshun09.mccommand.common.argument.Argument;
import com.github.siroshun09.mccommand.common.argument.parser.BasicParser;
import com.github.siroshun09.mccommand.common.context.CommandContext;
import com.github.siroshun09.mccommand.common.sender.Sender;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import net.okocraft.dailyrewards.lang.Placeholders;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SetCommand extends AbstractCommand {

    private static final List<String> TRUE_OR_FALSE = List.of("true", "false");

    private final DailyRewards plugin;

    public SetCommand(@NotNull DailyRewards plugin) {
        super("set", "dailyrewards.command.set", Set.of("s"));

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

        if (arguments.size() < 3) {
            plugin.getMessageBuilder().sendMessage(DefaultMessage.HELP_SET, sender);
            return CommandResult.NO_ARGUMENT;
        }

        Argument secondArgument = arguments.get(1);
        OfflinePlayer target = BukkitParser.OFFLINE_PLAYER.parse(secondArgument);

        if (target == null) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_SET_TARGET_NOT_FOUND, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .send(sender);

            return CommandResult.INVALID_ARGUMENTS;
        }

        if (!target.hasPlayedBefore()) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_SET_TARGET_NOT_JOINED, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .send(sender);

            return CommandResult.STATE_ERROR;
        }

        Argument thirdArgument = arguments.get(2);
        Boolean bool = BasicParser.BOOLEAN.parse(thirdArgument);

        if (bool == null) {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_SET_INVALID_BOOLEAN, sender);
            return CommandResult.INVALID_ARGUMENTS;
        }

        boolean changed = plugin.getReceiveData().setReceived(target.getUniqueId(), bool);

        plugin.getReceiveData().saveAsync();

        if (changed) {
            DefaultMessage msg = bool ? DefaultMessage.COMMAND_SET_TRUE : DefaultMessage.COMMAND_SET_FALSE;

            plugin.getMessageBuilder()
                    .getMessageWithPrefix(msg, sender)
                    .replace(Placeholders.PLAYER_NAME, Objects.requireNonNullElse(target.getName(), secondArgument.get()))
                    .replace(Placeholders.UUID, target.getUniqueId())
                    .send(sender);

            return CommandResult.SUCCESS;
        } else {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_SET_NO_CHANGE, sender);
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

            plugin.getServer().getOnlinePlayers()
                    .stream()
                    .map(HumanEntity::getName)
                    .collect(Collectors.toCollection(() -> result));

            plugin.getReceiveData().getReceivedPlayers()
                    .stream()
                    .map(plugin.getServer()::getOfflinePlayer)
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(name -> !result.contains(name))
                    .collect(Collectors.toCollection(() -> result));

            return StringUtil.copyPartialMatches(
                    arguments.get(1).get(),
                    result,
                    new ArrayList<>()
            );
        }

        if (arguments.size() == 3) {
            return StringUtil.copyPartialMatches(
                    arguments.get(2).get(),
                    TRUE_OR_FALSE,
                    new ArrayList<>()
            );
        }

        return Collections.emptyList();
    }
}
