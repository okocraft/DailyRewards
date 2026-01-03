package net.okocraft.dailyrewards.command.subcommand;

import com.github.siroshun09.mccommand.common.AbstractCommand;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import net.okocraft.dailyrewards.lang.Placeholders;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
    public void onExecution(@NotNull CommandSender sender, @NotNull List<String> args) {
        if (!sender.hasPermission(getPermission())) {
            plugin.getMessageBuilder().sendNoPermission(sender, this);
            return;
        }

        if (args.size() < 3) {
            plugin.getMessageBuilder().sendMessage(DefaultMessage.HELP_SET, sender);
            return;
        }

        String secondArgument = args.get(1);
        OfflinePlayer target = this.plugin.getServer().getOfflinePlayerIfCached(secondArgument);

        if (target == null) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_SET_TARGET_NOT_FOUND, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .send(sender);

            return;
        }

        Boolean bool = switch (args.get(2).toLowerCase(Locale.ROOT)) {
           case "true" -> true;
           case "false" -> false;
           default -> null;
        };

        if (bool == null) {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_SET_INVALID_BOOLEAN, sender);
            return;
        }

        boolean changed = plugin.getReceiveData().setReceived(target.getUniqueId(), bool);

        this.plugin.getServer().getAsyncScheduler().runNow(
                this.plugin,
                ignored -> this.plugin.getReceiveData().save(this.plugin.getSLF4JLogger())
        );

        if (changed) {
            DefaultMessage msg = bool ? DefaultMessage.COMMAND_SET_TRUE : DefaultMessage.COMMAND_SET_FALSE;

            plugin.getMessageBuilder()
                    .getMessageWithPrefix(msg, sender)
                    .replace(Placeholders.PLAYER_NAME, Objects.requireNonNullElse(target.getName(), secondArgument))
                    .replace(Placeholders.UUID, target.getUniqueId())
                    .send(sender);

        } else {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_SET_NO_CHANGE, sender);
        }
    }

    @Override
    public @NotNull List<String> onTabCompletion(@NotNull CommandSender sender, @NotNull List<String> args) {
        if (!sender.hasPermission(getPermission())) {
            return Collections.emptyList();
        }

        if (args.size() == 2) {
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
                    args.get(1),
                    result,
                    new ArrayList<>()
            );
        }

        if (args.size() == 3) {
            return StringUtil.copyPartialMatches(
                    args.get(2),
                    TRUE_OR_FALSE,
                    new ArrayList<>()
            );
        }

        return Collections.emptyList();
    }
}
