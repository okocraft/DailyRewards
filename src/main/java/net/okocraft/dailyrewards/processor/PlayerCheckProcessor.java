package net.okocraft.dailyrewards.processor;

import net.okocraft.dailyrewards.DailyRewards;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCheckProcessor {

    private final DailyRewards plugin;

    public PlayerCheckProcessor(@NotNull DailyRewards plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public Result check(@NotNull Player target) {
        if (plugin.getReceiveData().isNotToday()) {
            plugin.getReceiveData().reset();
            plugin.getReceiveData().saveAsync();
        }

        if (plugin.getReceiveData().isReceived(target.getUniqueId())) {
            return Result.ALREADY_RECEIVED;
        }

        if (plugin.getGeneralConfig().isDisabledWorld(target.getWorld().getName())) {
            return Result.DISABLED_WORLD;
        }

        if (target.isOnline()) {
            return Result.OK;
        } else {
            return Result.NOT_ONLINE;
        }
    }

    public enum Result {
        OK,
        ALREADY_RECEIVED,
        DISABLED_WORLD,
        NOT_ONLINE
    }
}