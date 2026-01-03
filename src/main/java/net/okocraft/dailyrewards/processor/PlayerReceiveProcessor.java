package net.okocraft.dailyrewards.processor;

import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerReceiveProcessor {

    private final DailyRewards plugin;

    public PlayerReceiveProcessor(@NotNull DailyRewards plugin) {
        this.plugin = plugin;
    }

    public void tryReceive(@NotNull Player receiver) {
        PlayerCheckProcessor.Result checkResult = plugin.getProcessors().getPlayerCheckProcessor().check(receiver);

        switch (checkResult) {
            case OK:
                break;
            case NOT_ONLINE:
                return;
            case ALREADY_RECEIVED:
                playSound(
                        receiver,
                        plugin.getGeneralConfig().getCannotReceiveSound(),
                        plugin.getGeneralConfig().getCannotReceiveSoundVolume(),
                        plugin.getGeneralConfig().getCannotReceiveSoundPitch()
                );
                plugin.getMessageBuilder().sendMessage(DefaultMessage.ERROR_ALREADY_RECEIVED, receiver);
                return;
            case DISABLED_WORLD:
                playSound(
                        receiver,
                        plugin.getGeneralConfig().getCannotReceiveSound(),
                        plugin.getGeneralConfig().getCannotReceiveSoundVolume(),
                        plugin.getGeneralConfig().getCannotReceiveSoundPitch()
                );
                plugin.getMessageBuilder().sendMessage(DefaultMessage.ERROR_DISABLED_WORLD_1, receiver);
                plugin.getMessageBuilder().sendMessage(DefaultMessage.ERROR_DISABLED_WORLD_2, receiver);
                return;
            default:
                plugin.getLogger().warning("Unknown check result: " + checkResult);
                return;
        }

        plugin.getReceiveData().setReceived(receiver.getUniqueId(), true);

        playSound(
                receiver,
                plugin.getGeneralConfig().getReceiveSound(),
                plugin.getGeneralConfig().getReceiveSoundVolume(),
                plugin.getGeneralConfig().getReceiveSoundPitch()
        );

        plugin.getServer().getScheduler().runTask(
                plugin,
                () -> plugin.getProcessors()
                        .getRewardsGiveProcessor()
                        .give(receiver)
        );

        plugin.getReceiveData().saveAsync();
    }

    private void playSound(@NotNull Player player, @NotNull Sound sound, float volume, float pitch) {
        if (plugin.getGeneralConfig().isSoundEnabled()) {
            player.playSound(player.getLocation(), sound, SoundCategory.MASTER, volume, pitch);
        }
    }
}
