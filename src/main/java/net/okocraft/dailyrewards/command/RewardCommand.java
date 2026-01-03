package net.okocraft.dailyrewards.command;

import com.github.siroshun09.mccommand.common.Command;
import com.github.siroshun09.mccommand.common.SubCommandHolder;
import com.github.siroshun09.mccommand.common.argument.Argument;
import com.github.siroshun09.mccommand.common.context.CommandContext;
import com.github.siroshun09.mccommand.common.context.SimpleCommandContext;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.command.subcommand.GiveCommand;
import net.okocraft.dailyrewards.command.subcommand.ReceiveCommand;
import net.okocraft.dailyrewards.command.subcommand.ReloadCommand;
import net.okocraft.dailyrewards.command.subcommand.ResetCommand;
import net.okocraft.dailyrewards.command.subcommand.SetCommand;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import net.okocraft.dailyrewards.lang.Placeholders;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RewardCommand implements BasicCommand {

    private static final List<DefaultMessage> HELPS =
            List.of(
                    DefaultMessage.HELP_REWARD, DefaultMessage.HELP_RECEIVE, DefaultMessage.HELP_GIVE,
                    DefaultMessage.HELP_SET, DefaultMessage.HELP_RESET, DefaultMessage.HELP_RELOAD
            );

    private final DailyRewards plugin;
    private final SubCommandHolder subCommandHolder;

    public RewardCommand(@NotNull DailyRewards plugin) {
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

    public void onExecution(@NotNull CommandContext context) {
        CommandSender sender = context.getSender();

        if (!sender.hasPermission(this.permission())) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.ERROR_NO_PERMISSION, sender)
                    .replace(Placeholders.COMMAND_PERM, this.permission())
                    .send(sender);
            return;
        }

        if (context.getArguments().isEmpty()) {
            sendUsage(sender);
            return;
        }

        Argument firstArgument = context.getArguments().get(0);
        Optional<Command> subCommand = subCommandHolder.searchOptional(firstArgument);

        if (subCommand.isPresent()) {
            subCommand.get().onExecution(context);
        } else {
            if (!firstArgument.get().equalsIgnoreCase("help")) {
                plugin.getMessageBuilder()
                        .getMessageWithPrefix(DefaultMessage.ERROR_INVALID_ARGUMENT, sender)
                        .replace(Placeholders.ARG, firstArgument)
                        .send(sender);
            }
            sendUsage(sender);
        }
    }

    public @NotNull List<String> onTabCompletion(@NotNull CommandContext context) {
        CommandSender sender = context.getSender();
        List<Argument> args = context.getArguments();

        if (args.isEmpty() || !sender.hasPermission(this.permission())) {
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

    private void sendUsage(@NotNull Audience receiver) {
        plugin.getMessageBuilder()
                .getMessage(DefaultMessage.HELP_TOP, receiver)
                .replace(Placeholders.VERSION, plugin)
                .send(receiver);

        plugin.getMessageBuilder().sendMessage(DefaultMessage.HELP_EMPTY, receiver);
        HELPS.forEach(help -> plugin.getMessageBuilder().sendMessage(help, receiver));
    }

    @Override
    public void execute(@NotNull CommandSourceStack source, String @NotNull [] args) {
        this.onExecution(createContext(source.getSender(), args));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack source, String @NotNull [] args) {
        return this.onTabCompletion(createContext(source.getSender(), args));
    }

    @Override
    public @NotNull String permission() {
        return "dailyrewards.command";
    }

    private static CommandContext createContext(@NotNull CommandSender sender, @NotNull String[] args) {
        return SimpleCommandContext.newBuilder()
                .setSender(sender)
                .setArguments(args)
                .build();
    }
}
