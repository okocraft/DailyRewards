package net.okocraft.dailyrewards.command;

import com.github.siroshun09.mccommand.common.AbstractCommand;
import com.github.siroshun09.mccommand.common.Command;
import com.github.siroshun09.mccommand.common.CommandResult;
import com.github.siroshun09.mccommand.common.SubCommandHolder;
import com.github.siroshun09.mccommand.common.argument.Argument;
import com.github.siroshun09.mccommand.common.context.CommandContext;
import com.github.siroshun09.mccommand.common.sender.Sender;
import com.github.siroshun09.mcmessage.MessageReceiver;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.command.subcommand.GiveCommand;
import net.okocraft.dailyrewards.command.subcommand.ReceiveCommand;
import net.okocraft.dailyrewards.command.subcommand.ReloadCommand;
import net.okocraft.dailyrewards.command.subcommand.ResetCommand;
import net.okocraft.dailyrewards.command.subcommand.SetCommand;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import net.okocraft.dailyrewards.lang.Placeholders;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RewardCommand extends AbstractCommand {

    private static final List<DefaultMessage> HELPS =
            List.of(
                    DefaultMessage.HELP_REWARD, DefaultMessage.HELP_RECEIVE, DefaultMessage.HELP_GIVE,
                    DefaultMessage.HELP_SET, DefaultMessage.HELP_RESET, DefaultMessage.HELP_RELOAD
            );

    private final DailyRewards plugin;
    private final SubCommandHolder subCommandHolder;

    public RewardCommand(@NotNull DailyRewards plugin) {
        super("dailyrewards", "dailyrewards.command", Set.of("rewards", "reward", "rw", "dr"));
        this.plugin = plugin;
        this.subCommandHolder =
                SubCommandHolder.of(
                        new GiveCommand(plugin),
                        new ReceiveCommand(plugin),
                        new ReloadCommand(plugin),
                        new ResetCommand(plugin),
                        new SetCommand(plugin)
                );
    }

    @Override
    public @NotNull CommandResult onExecution(@NotNull CommandContext context) {
        Sender sender = context.getSender();

        if (!sender.hasPermission(getPermission())) {
            plugin.getMessageBuilder().sendNoPermission(sender, this);
            return CommandResult.NO_PERMISSION;
        }

        if (context.getArguments().isEmpty()) {
            sendUsage(sender);
            return CommandResult.NO_ARGUMENT;
        }

        Argument firstArgument = context.getArguments().get(0);
        Optional<Command> subCommand = subCommandHolder.searchOptional(firstArgument);

        if (subCommand.isPresent()) {
            return subCommand.get().onExecution(context);
        } else {
            if (!firstArgument.get().equalsIgnoreCase("help")) {
                plugin.getMessageBuilder()
                        .getMessageWithPrefix(DefaultMessage.ERROR_INVALID_ARGUMENT, sender)
                        .replace(Placeholders.ARG, firstArgument)
                        .send(sender);
            }
            sendUsage(sender);
            return CommandResult.INVALID_ARGUMENTS;
        }
    }

    @Override
    public @NotNull List<String> onTabCompletion(@NotNull CommandContext context) {
        Sender sender = context.getSender();
        List<Argument> args = context.getArguments();

        if (args.isEmpty() || !sender.hasPermission(getPermission())) {
            return Collections.emptyList();
        }

        Argument firstArgument = context.getArguments().get(0);

        if (args.size() == 1) {
            return StringUtil.copyPartialMatches(
                    firstArgument.get(),
                    subCommandHolder.getSubCommands()
                            .stream()
                            .filter(c -> sender.hasPermission(c.getPermission()))
                            .map(Command::getName)
                            .collect(Collectors.toList()),
                    new ArrayList<>());
        } else {
            return subCommandHolder
                    .searchOptional(firstArgument)
                    .map(cmd -> cmd.onTabCompletion(context))
                    .orElse(Collections.emptyList());
        }
    }

    private void sendUsage(@NotNull MessageReceiver receiver) {
        plugin.getMessageBuilder()
                .getMessage(DefaultMessage.HELP_TOP, receiver)
                .replace(Placeholders.VERSION, plugin)
                .send(receiver);

        plugin.getMessageBuilder().sendMessage(DefaultMessage.HELP_EMPTY, receiver);
        HELPS.forEach(help -> plugin.getMessageBuilder().sendMessage(help, receiver));
    }
}
