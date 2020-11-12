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
import net.okocraft.dailyrewards.reward.Reward;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GiveCommand extends AbstractCommand {

    private final DailyRewards plugin;

    public GiveCommand(@NotNull DailyRewards plugin) {
        super("give", "dailyrewards.command.give", Set.of("g"));
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
            plugin.getMessageBuilder().sendMessage(DefaultMessage.HELP_GIVE, sender);
            return CommandResult.NO_ARGUMENT;
        }

        Argument secondArgument = arguments.get(1);
        Player target = BukkitParser.PLAYER.parse(secondArgument);

        if (target == null) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_GIVE_PLAYER_NOT_FOUND, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .send(sender);
            return CommandResult.STATE_ERROR;
        }

        Argument thirdArgument = arguments.get(2);
        Reward reward =
                plugin.getRewardConfig().getRewards()
                        .stream()
                        .filter(r -> r.getName().equalsIgnoreCase(thirdArgument.get()))
                        .findFirst()
                        .orElse(null);

        if (reward == null) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_GIVE_REWARD_NOT_FOUND, sender)
                    .replace(Placeholders.REWARD_NAME, thirdArgument)
                    .send(sender);
            return CommandResult.STATE_ERROR;
        }

        plugin.getServer().getScheduler()
                .runTask(
                        plugin,
                        () -> plugin.getProcessors().getRewardsGiveProcessor().give(target, reward)
                );

        plugin.getMessageBuilder()
                .getMessageWithPrefix(DefaultMessage.COMMAND_GIVE_SUCCESS, sender)
                .replace(Placeholders.PLAYER, target)
                .replace(Placeholders.REWARD, reward)
                .send(sender);

        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull List<String> onTabCompletion(@NotNull CommandContext context) {
        List<Argument> arguments = context.getArguments();
        Sender sender = context.getSender();

        if (!sender.hasPermission(getPermission())) {
            return Collections.emptyList();
        }

        if (arguments.size() == 2) {
            Argument secondArguments = arguments.get(1);

            return StringUtil.copyPartialMatches(
                    secondArguments.get(),
                    plugin.getServer().getOnlinePlayers()
                            .stream()
                            .map(HumanEntity::getName)
                            .collect(Collectors.toUnmodifiableList()),
                    new ArrayList<>()
            );
        }

        if (arguments.size() == 3) {
            Argument thirdArguments = arguments.get(2);

            return StringUtil.copyPartialMatches(
                    thirdArguments.get(),
                    plugin.getRewardConfig().getRewards()
                            .stream()
                            .map(Reward::getName)
                            .collect(Collectors.toUnmodifiableList()),
                    new ArrayList<>()
            );
        }

        return Collections.emptyList();
    }
}
