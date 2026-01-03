package net.okocraft.dailyrewards.config;

import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.reward.Reward;
import net.okocraft.dailyrewards.reward.RewardLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public class RewardConfig {

    private final Path filepath;
    private final YamlConfiguration yaml;
    private final List<Reward> rewards;

    public RewardConfig(@NotNull DailyRewards plugin) {
        this.filepath = plugin.getDataFolder().toPath().resolve("rewards.yml");
        this.yaml = YamlConfiguration.loadConfiguration(this.filepath.toFile());
        this.rewards = RewardLoader.load(this.yaml);
    }

    public void reload() throws Exception {
        rewards.clear();

        yaml.load(this.filepath.toFile());
        rewards.addAll(RewardLoader.load(yaml));
    }

    @NotNull
    public List<Reward> getRewards() {
        return rewards;
    }

    public @NotNull Path getFilePath() {
        return this.filepath;
    }
}
