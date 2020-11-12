package net.okocraft.dailyrewards.reward;

import com.github.siroshun09.configapi.common.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class RewardLoader {

    private RewardLoader() {
        throw new UnsupportedOperationException();
    }

    public static @NotNull List<Reward> load(@NotNull Configuration config) {
        List<Reward> result = new ArrayList<>();

        for (String rootKey : config.getKeys()) {
            Reward reward = new Reward(
                    rootKey,
                    config.getString(rootKey + ".message", "&7* ログインボーナスを受け取りました。"),
                    config.getString(rootKey + ".permission", "dailyrewards.reward.default"),
                    List.copyOf(config.getStringList(rootKey + ".commands"))
            );

            result.add(reward);
        }

        return result;
    }
}
