package net.okocraft.dailyrewards.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.command.subcommand.Command;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class RewardCommand implements BasicCommand {

    private static final List<DefaultMessage> HELPS =
            List.of(
                    DefaultMessage.HELP_REWARD, DefaultMessage.HELP_RECEIVE, DefaultMessage.HELP_GIVE,
                    DefaultMessage.HELP_SET, DefaultMessage.HELP_RESET, DefaultMessage.HELP_RELOAD
            );

    private final DailyRewards plugin;
    private final List<Command> subCommands;
    private final Map<String, Command> subCommandMap;

    public RewardCommand(@NotNull DailyRewards plugin) {
        this.plugin = plugin;
        this.subCommands = List.of(
                new GiveCommand(plugin),
                new ReceiveCommand(plugin),
                new ReloadCommand(plugin),
                new ResetCommand(plugin),
                new SetCommand(plugin)
        );

        Map<String, Command> subCommandMap = new HashMap<>();
        for (Command subCommand : this.subCommands) {
            subCommandMap.put(subCommand.getName(), subCommand);
            for (String alias : subCommand.getAliases()) {
                subCommandMap.put(alias, subCommand);
            }
        }

        this.subCommandMap = Collections.unmodifiableMap(subCommandMap);
    }

    public void onExecution(@NotNull CommandSender sender, @NotNull List<String> args) {
        if (!sender.hasPermission(this.permission())) {
            plugin.getMessageBuilder().sendNoPermission(sender, this.permission());
            return;
        }

        if (args.isEmpty()) {
            sendUsage(sender);
            return;
        }

        Command subCommand = this.findSubCommand(args.getFirst());
        if (subCommand != null) {
            subCommand.onExecution(sender, args);
        } else {
            if (!args.getFirst().equalsIgnoreCase("help")) {
                plugin.getMessageBuilder()
                        .getMessageWithPrefix(DefaultMessage.ERROR_INVALID_ARGUMENT, sender)
                        .replace(Placeholders.ARG, args.getFirst())
                        .send(sender);
            }
            sendUsage(sender);
        }
    }

    public @NotNull List<String> onTabCompletion(@NotNull CommandSender sender, @NotNull List<String> args) {
        if (args.isEmpty() || !sender.hasPermission(this.permission())) {
            return Collections.emptyList();
        }

        String firstArgument = args.getFirst();

        if (args.size() == 1) {
            return StringUtil.copyPartialMatches(
                    firstArgument,
                    this.subCommands.stream()
                            .filter(c -> sender.hasPermission(c.getPermission()))
                            .map(Command::getName)
                            .collect(Collectors.toList()),
                    new ArrayList<>());
        } else {
            Command subCommand = this.findSubCommand(args.getFirst());
            return subCommand != null ?
                    subCommand.onTabCompletion(sender, args) :
                    Collections.emptyList();
        }
    }

    private @Nullable Command findSubCommand(@NotNull String name) {
        return this.subCommandMap.get(name.toLowerCase(Locale.ENGLISH));
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
        this.onExecution(source.getSender(), List.of(args));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack source, String @NotNull [] args) {
        return this.onTabCompletion(source.getSender(), List.of(args));
    }

    @Override
    public @NotNull String permission() {
        return "dailyrewards.command";
    }
}
