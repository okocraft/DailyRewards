package net.okocraft.dailyrewards.lang;

import net.kyori.adventure.audience.Audience;
import net.okocraft.dailyrewards.DailyRewards;
import org.jetbrains.annotations.NotNull;

public class MessageBuilder {

    private final DailyRewards plugin;

    public MessageBuilder(@NotNull DailyRewards plugin) {
        this.plugin = plugin;
    }

    public @NotNull PlainTextBuilder getMessage(@NotNull DefaultMessage msg, @NotNull Audience receiver) {
        return new PlainTextBuilder(plugin.getLanguageManager().getMessage(msg, receiver));
    }

    public @NotNull PlainTextBuilder getMessageWithPrefix(@NotNull DefaultMessage msg, @NotNull Audience receiver) {
        return getMessage(msg, receiver)
                .addPrefix(plugin.getLanguageManager().getMessage(DefaultMessage.PREFIX, receiver));
    }

    public void sendMessage(@NotNull DefaultMessage message, @NotNull Audience receiver) {
        getMessage(message, receiver).send(receiver);
    }

    public void sendMessageWithPrefix(@NotNull DefaultMessage message, @NotNull Audience receiver) {
        getMessageWithPrefix(message, receiver).send(receiver);
    }

    public void sendNoPermission(@NotNull Audience receiver, @NotNull String commandName) {
        getMessageWithPrefix(DefaultMessage.ERROR_NO_PERMISSION, receiver)
                .replace(Placeholders.COMMAND_PERM, commandName)
                .send(receiver);
    }
}
