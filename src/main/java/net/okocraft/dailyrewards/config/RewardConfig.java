package net.okocraft.dailyrewards.config;

import com.github.siroshun09.configapi.bukkit.BukkitYaml;
import com.github.siroshun09.configapi.bukkit.BukkitYamlFactory;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.reward.Reward;
import net.okocraft.dailyrewards.reward.RewardLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class RewardConfig {
    private final BukkitYaml yaml;
    private final List<Reward> rewards;

    public RewardConfig(@NotNull DailyRewards plugin) {
        this.yaml = BukkitYamlFactory.loadUnsafe(plugin, "rewards.yml");
        this.rewards = RewardLoader.load(yaml);
    }

    public void reload() throws IOException {
        yaml.reload();
        rewards.clear();
        rewards.addAll(RewardLoader.load(yaml));
    }

    @NotNull
    public List<Reward> getRewards() {
        return rewards;
    }

    public @NotNull Path getFilePath() {
        return yaml.getPath();
    }
}
