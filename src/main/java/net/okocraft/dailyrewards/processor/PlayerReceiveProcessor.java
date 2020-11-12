package net.okocraft.dailyrewards.processor;

import com.github.siroshun09.mccommand.bukkit.sender.BukkitSender;
import com.github.siroshun09.mcmessage.MessageReceiver;
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
        MessageReceiver messageReceiver = new BukkitSender(receiver);

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
                plugin.getMessageBuilder().sendMessage(DefaultMessage.ERROR_ALREADY_RECEIVED, messageReceiver);
                return;
            case DISABLED_WORLD:
                playSound(
                        receiver,
                        plugin.getGeneralConfig().getCannotReceiveSound(),
                        plugin.getGeneralConfig().getCannotReceiveSoundVolume(),
                        plugin.getGeneralConfig().getCannotReceiveSoundPitch()
                );
                plugin.getMessageBuilder().sendMessage(DefaultMessage.ERROR_DISABLED_WORLD_1, messageReceiver);
                plugin.getMessageBuilder().sendMessage(DefaultMessage.ERROR_DISABLED_WORLD_2, messageReceiver);
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
