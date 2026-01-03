package net.okocraft.dailyrewards.processor;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import net.okocraft.dailyrewards.lang.Placeholders;
import net.okocraft.dailyrewards.reward.Reward;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RewardsGiveProcessor {

    private final DailyRewards plugin;

    public RewardsGiveProcessor(@NotNull DailyRewards plugin) {
        this.plugin = plugin;
    }

    public void give(@NotNull Player target) {
        for (Reward reward : plugin.getRewardConfig().getRewards()) {
            if (!target.hasPermission(reward.getPermission())) {
                continue;
            }

            give(target, reward);
        }
    }

    public void give(@NotNull Player target, @NotNull Reward reward) {
        boolean fail = false;

        for (String command : reward.getCommands()) {
            String toRun = Placeholders.PLAYER.toReplacer(target).replace(command);
            boolean success = plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), toRun);

            if (success) {
                plugin.getLogger().info(reward.getName() + " has been given to " + target.getName());
            } else {
                fail = true;
                plugin.getLogger().warning("Failed to execute the command: " + toRun);
            }
        }

        target.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(reward.getMessage()));

        if (fail) {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.ERROR_FAILED_TO_RUN_COMMAND_1, target);
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.ERROR_FAILED_TO_RUN_COMMAND_2, target);
        }
    }
}
