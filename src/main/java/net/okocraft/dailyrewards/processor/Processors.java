package net.okocraft.dailyrewards.processor;

import net.okocraft.dailyrewards.DailyRewards;
import org.jetbrains.annotations.NotNull;

public class Processors {

    private final PlayerCheckProcessor playerCheckProcessor;
    private final PlayerReceiveProcessor playerReceiveProcessor;
    private final RewardsGiveProcessor rewardsGiveProcessor;

    public Processors(@NotNull DailyRewards plugin) {
        this.playerCheckProcessor = new PlayerCheckProcessor(plugin);
        this.playerReceiveProcessor = new PlayerReceiveProcessor(plugin);
        this.rewardsGiveProcessor = new RewardsGiveProcessor(plugin);
    }

    @NotNull
    public PlayerCheckProcessor getPlayerCheckProcessor() {
        return playerCheckProcessor;
    }

    @NotNull
    public PlayerReceiveProcessor getPlayerReceiveProcessor() {
        return playerReceiveProcessor;
    }

    @NotNull
    public RewardsGiveProcessor getRewardsGiveProcessor() {
        return rewardsGiveProcessor;
    }
}
