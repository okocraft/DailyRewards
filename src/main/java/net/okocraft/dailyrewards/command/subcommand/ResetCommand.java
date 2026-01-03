package net.okocraft.dailyrewards.command.subcommand;

import com.github.siroshun09.mccommand.common.AbstractCommand;
import com.github.siroshun09.mccommand.common.argument.Argument;
import com.github.siroshun09.mccommand.common.context.CommandContext;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import net.okocraft.dailyrewards.lang.Placeholders;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
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
    public void onExecution(@NotNull CommandContext context) {
        CommandSender sender = context.getSender();

        if (!sender.hasPermission(getPermission())) {
            plugin.getMessageBuilder().sendNoPermission(sender, this);
            return;
        }

        List<Argument> arguments = context.getArguments();

        if (arguments.size() < 2) {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.HELP_RESET, sender);
            return;
        }

        Argument secondArgument = arguments.get(1);

        if (secondArgument.get().equalsIgnoreCase("all")) {
            if (plugin.getReceiveData().reset()) {
                this.plugin.getServer().getAsyncScheduler().runNow(
                        this.plugin,
                        ignored -> this.plugin.getReceiveData().save(this.plugin.getSLF4JLogger())
                );
                plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RESET_ALL, sender);
                return;
            } else {
                plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RESET_NO_CHANGE, sender);
                return;
            }
        }

        OfflinePlayer target = this.plugin.getServer().getOfflinePlayerIfCached(secondArgument.get());

        if (target == null) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_RESET_TARGET_NOT_FOUND, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .send(sender);

            return;
        }

        boolean changed = plugin.getReceiveData().setReceived(target.getUniqueId(), false);

        this.plugin.getServer().getAsyncScheduler().runNow(
                this.plugin,
                ignored -> this.plugin.getReceiveData().save(this.plugin.getSLF4JLogger())
        );

        if (changed) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_RESET_PLAYER, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .replace(Placeholders.UUID, target.getUniqueId())
                    .send(sender);

        } else {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RESET_NO_CHANGE, sender);
        }
    }

    @Override
    public @NotNull List<String> onTabCompletion(@NotNull CommandContext context) {
        List<Argument> arguments = context.getArguments();
        CommandSender sender = context.getSender();

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
