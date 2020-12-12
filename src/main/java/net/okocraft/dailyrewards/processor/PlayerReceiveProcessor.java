package net.okocraft.dailyrewards.processor;

import com.github.siroshun09.configapi.common.configurable.Configurable;
import com.github.siroshun09.mccommand.bukkit.sender.BukkitSender;
import com.github.siroshun09.mcmessage.MessageReceiver;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.config.Setting;
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

        if (checkResult != PlayerCheckProcessor.Result.OK) {
            switch (checkResult) {
                case NOT_ONLINE:
                    return;
                case ALREADY_RECEIVED:
                    plugin.getMessageBuilder().sendMessage(DefaultMessage.ERROR_ALREADY_RECEIVED, messageReceiver);
                    break;
                case DISABLED_WORLD:
                    plugin.getMessageBuilder().sendMessage(DefaultMessage.ERROR_DISABLED_WORLD_1, messageReceiver);
                    plugin.getMessageBuilder().sendMessage(DefaultMessage.ERROR_DISABLED_WORLD_2, messageReceiver);
                    break;
                default:
                    plugin.getLogger().warning("Unknown check result: " + checkResult);
                    return;
            }

            playSound(
                    receiver,
                    plugin.getGeneralConfig().getCannotReceiveSound(),
                    Setting.SOUND_CANNOT_RECEIVE_VOLUME,
                    Setting.SOUND_CANNOT_RECEIVE_PITCH
            );

            return;
        }

        plugin.getReceiveData().setReceived(receiver.getUniqueId(), true);

        playSound(
                receiver,
                plugin.getGeneralConfig().getReceiveSound(),
                Setting.SOUND_RECEIVE_VOLUME,
                Setting.SOUND_RECEIVE_PITCH
        );

        plugin.getServer().getScheduler().runTask(
                plugin,
                () -> plugin.getProcessors()
                        .getRewardsGiveProcessor()
                        .give(receiver)
        );

        plugin.getReceiveData().saveAsync();
    }

    private void playSound(@NotNull Player player, @NotNull Sound sound,
                           @NotNull Configurable<Float> volume, @NotNull Configurable<Float> pitch) {
        if (plugin.getGeneralConfig().get(Setting.ENABLE_SOUND)) {
            player.playSound(
                    player.getLocation(),
                    sound,
                    SoundCategory.MASTER,
                    getAndCheckRange(volume, 0f, 200f),
                    getAndCheckRange(pitch, 0.5f, 2.0f));
        }
    }

    private float getAndCheckRange(@NotNull Configurable<Float> configurable, float min, float max) {
        float value = plugin.getGeneralConfig().get(configurable);
        return min <= value && value <= max ? value : configurable.getDefault();
    }
}
