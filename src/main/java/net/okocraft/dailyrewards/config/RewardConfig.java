package net.okocraft.dailyrewards.config;

import com.github.siroshun09.configapi.yaml.YamlConfiguration;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.reward.Reward;
import net.okocraft.dailyrewards.reward.RewardLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class RewardConfig {

    private final YamlConfiguration yaml;
    private final List<Reward> rewards;

    public RewardConfig(@NotNull DailyRewards plugin) {
        this.yaml = YamlConfiguration.create(plugin.getDataFolder().toPath().resolve("rewards.yml"));

        try (yaml) {
            yaml.load();
            this.rewards = RewardLoader.load(yaml);
        } catch (IOException e) {
            throw new RuntimeException("Could not load rewards.yml", e);
        }
    }

    public void reload() throws IOException {
        rewards.clear();

        try (yaml) {
            yaml.load();
            rewards.addAll(RewardLoader.load(yaml));
        }
    }

    @NotNull
    public List<Reward> getRewards() {
        return rewards;
    }

    public @NotNull Path getFilePath() {
        return yaml.getPath();
    }
}
