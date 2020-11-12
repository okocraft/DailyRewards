package net.okocraft.dailyrewards.processor;

import com.github.siroshun09.mcmessage.util.Colorizer;
import net.okocraft.dailyrewards.DailyRewards;
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
        for (String command : reward.getCommands()) {
            String toRun = Placeholders.PLAYER.toReplacer(target).replace(command);
            boolean success = plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), toRun);

            if (!success) {
                plugin.getLogger().warning("Failed to execute the command: " + toRun);
            }
        }

        target.sendMessage(Colorizer.colorize(reward.getMessage()));
    }
}
