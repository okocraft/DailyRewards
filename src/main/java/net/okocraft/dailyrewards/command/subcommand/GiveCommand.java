package net.okocraft.dailyrewards.command.subcommand;

import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import net.okocraft.dailyrewards.lang.Placeholders;
import net.okocraft.dailyrewards.reward.Reward;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GiveCommand extends AbstractCommand {

    private final DailyRewards plugin;

    public GiveCommand(@NotNull DailyRewards plugin) {
        super("give", "dailyrewards.command.give", Set.of("g"));
        this.plugin = plugin;
    }

    @Override
    public void onExecution(@NotNull CommandSender sender, @NotNull List<String> args) {
        if (!sender.hasPermission(getPermission())) {
            plugin.getMessageBuilder().sendNoPermission(sender, this.getPermission());
            return;
        }

        if (args.size() < 3) {
            plugin.getMessageBuilder().sendMessage(DefaultMessage.HELP_GIVE, sender);
            return;
        }

        String secondArgument = args.get(1);
        Player target = this.plugin.getServer().getPlayer(secondArgument);

        if (target == null) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_GIVE_PLAYER_NOT_FOUND, sender)
                    .replace(Placeholders.PLAYER_NAME, secondArgument)
                    .send(sender);
            return;
        }

        String thirdArgument = args.get(2);
        Reward reward =
                plugin.getRewardConfig().getRewards()
                        .stream()
                        .filter(r -> r.getName().equalsIgnoreCase(thirdArgument))
                        .findFirst()
                        .orElse(null);

        if (reward == null) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_GIVE_REWARD_NOT_FOUND, sender)
                    .replace(Placeholders.REWARD_NAME, thirdArgument)
                    .send(sender);
            return;
        }

        target.getScheduler().run(
                this.plugin,
                ignored -> plugin.getProcessors().getRewardsGiveProcessor().give(target, reward),
                null
        );

        plugin.getMessageBuilder()
                .getMessageWithPrefix(DefaultMessage.COMMAND_GIVE_SUCCESS, sender)
                .replace(Placeholders.PLAYER, target.getName())
                .replace(Placeholders.REWARD, reward.getName())
                .send(sender);

    }

    @Override
    public @NotNull List<String> onTabCompletion(@NotNull CommandSender sender, @NotNull List<String> args) {
        if (!sender.hasPermission(getPermission())) {
            return Collections.emptyList();
        }

        if (args.size() == 2) {
            String secondArguments = args.get(1);

            return StringUtil.copyPartialMatches(
                    secondArguments,
                    plugin.getServer().getOnlinePlayers()
                            .stream()
                            .map(HumanEntity::getName)
                            .toList(),
                    new ArrayList<>()
            );
        }

        if (args.size() == 3) {
            String thirdArguments = args.get(2);

            return StringUtil.copyPartialMatches(
                    thirdArguments,
                    plugin.getRewardConfig().getRewards()
                            .stream()
                            .map(Reward::getName)
                            .toList(),
                    new ArrayList<>()
            );
        }

        return Collections.emptyList();
    }
}
