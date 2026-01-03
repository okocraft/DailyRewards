package net.okocraft.dailyrewards.command.subcommand;

import com.github.siroshun09.mccommand.common.AbstractCommand;
import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.lang.DefaultMessage;
import net.okocraft.dailyrewards.lang.Placeholders;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends AbstractCommand {

    private final DailyRewards plugin;

    public ReloadCommand(@NotNull DailyRewards plugin) {
        super("reload", "dailyrewards.command.reload");

        this.plugin = plugin;
    }


    @Override
    public void onExecution(@NotNull CommandSender sender, @NotNull List<String> args) {
        if (!sender.hasPermission(getPermission())) {
            plugin.getMessageBuilder().sendNoPermission(sender, this);
            return;
        }

        plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_START, sender);

        try {
            plugin.getGeneralConfig().reload();

            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_SUCCESS, sender)
                    .replace(Placeholders.FILE_NAME, plugin.getGeneralConfig().getFilePath())
                    .send(sender);
        } catch (Exception e) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_FAILURE, sender)
                    .replace(Placeholders.FILE_NAME, plugin.getGeneralConfig().getFilePath())
                    .send(sender);

            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_CANCELLED, sender);

            return;
        }

        try {
            plugin.getRewardConfig().reload();

            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_SUCCESS, sender)
                    .replace(Placeholders.FILE_NAME, plugin.getRewardConfig().getFilePath())
                    .send(sender);
        } catch (Exception e) {
            plugin.getMessageBuilder()
                    .getMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_FAILURE, sender)
                    .replace(Placeholders.FILE_NAME, plugin.getRewardConfig().getFilePath())
                    .send(sender);

            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_CANCELLED, sender);

            return;
        }

        plugin.getReceiveData().reload();
        plugin.getMessageBuilder()
                .getMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_SUCCESS, sender)
                .replace(Placeholders.FILE_NAME, plugin.getReceiveData().getFilePath())
                .send(sender);

        try {
            plugin.getLanguageManager().reload();

            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_LANG_SUCCESS, sender);
        } catch (IOException e) {
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_LANG_FAILURE, sender);
            plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_CANCELLED, sender);

            return;
        }

        plugin.getMessageBuilder().sendMessageWithPrefix(DefaultMessage.COMMAND_RELOAD_FINISH, sender);

    }

    @Override
    public @NotNull List<String> onTabCompletion(@NotNull CommandSender sender, @NotNull List<String> arguments) {
        return Collections.emptyList();
    }
}
