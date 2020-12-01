package net.okocraft.dailyrewards.lang;

import com.github.siroshun09.mcmessage.translation.Translation;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum DefaultMessage implements com.github.siroshun09.mcmessage.message.DefaultMessage {

    PREFIX("prefix", "&8[&6DailyRewards&8]&7 "),

    COMMAND_GIVE_SUCCESS("command.give.success", "Added &b%reward%&7 to &b%player%&7"),
    COMMAND_GIVE_REWARD_NOT_FOUND("command.give.reward-not-found", "&cThe reward named &b%reward%&c was not found."),
    COMMAND_GIVE_PLAYER_NOT_FOUND("command.give.player-not-found", "&b%player%&c is not online."),

    COMMAND_RECEIVE_ONLY_PLAYER("command.only-player", "&cThis command can only be executed by the player."),

    COMMAND_RELOAD_SUCCESS("command.reload.success", "&b%file%&7 was reloaded."),
    COMMAND_RELOAD_FAILURE("command.reload.failure", "&c%file% could not be reloaded."),
    COMMAND_RELOAD_LANG_SUCCESS("command.reload.lang.success", "&bLanguage files &7was reloaded."),
    COMMAND_RELOAD_LANG_FAILURE("command.reload.lang.failure", "&cLanguage files could not be reloaded."),
    COMMAND_RELOAD_START("command.reload.start", "&7Files reloading...."),
    COMMAND_RELOAD_FINISH("command.reload.finish", "&aFiles have been reloaded."),
    COMMAND_RELOAD_CANCELLED("command.reload.cancelled", "&cFile reloading was cancelled."),

    COMMAND_RESET_ALL("command.reset.all", "&bAll players have been marked as unreceived."),
    COMMAND_RESET_PLAYER("command.reset.player", "&b%player%&7 (&b%uuid%&7) has been marked as unreceived."),
    COMMAND_RESET_TARGET_NOT_FOUND("command.reset.target-not-found", "&c%player% was not found."),
    COMMAND_RESET_TARGET_NOT_JOINED("command.reset.target-not-joined", "&c%player% has not yet logged in to this server."),
    COMMAND_RESET_NO_CHANGE("command.reset.no-change", "&cThe status of receipt was not changed."),

    COMMAND_SET_TRUE("command.set.true", "&b%player%&7 (&b%uuid%&7)  has been marked as received."),
    COMMAND_SET_FALSE("command.set.false", "&b%player%&7 (&b%uuid%&7) has been marked as unreceived."),
    COMMAND_SET_TARGET_NOT_FOUND("command.set.target-not-found", "%player% was not found."),
    COMMAND_SET_TARGET_NOT_JOINED("command.set.target-not-joined", "%player% has not yet logged in to this server."),
    COMMAND_SET_NO_CHANGE("command.set.no-change", "&cThe status of receipt was not changed."),
    COMMAND_SET_INVALID_BOOLEAN("command.invalid-boolean", "&cThe Boolean value must be &btrue&c or &bfalse&c."),

    ERROR_INVALID_ARGUMENT("command.invalid-argument", "&cInvalid argument: &b%arg%"),
    ERROR_NO_PERMISSION("command.no-permission", "&cYou don't have the permission: &b%perm%"),

    ERROR_ALREADY_RECEIVED("reward.already-received", "&7* You have already received your login bonus for today."),
    ERROR_DISABLED_WORLD_1("reward.disabled-world-1", "&7* You are in the world that does not receive a login bonus."),
    ERROR_DISABLED_WORLD_2("reward.disabled-world-2", "&7* Go to another world and run &b/reward."),
    ERROR_FAILED_TO_RUN_COMMAND_1("reward.failed-to-run-command-1", "Failed to execute the command."),
    ERROR_FAILED_TO_RUN_COMMAND_2("reward.failed-to-run-command-2", "Please report this to the server administrator."),

    HELP_TOP("help.top-line", "&7* &6DailyRewards &eVersion %version%"),
    HELP_EMPTY("help.empty-line", "&7*"),
    HELP_REWARD("help.reward", "&7* &b/dailyrewards&7 (Aliases:&b/reward /rw /dr&7)"),
    HELP_RECEIVE("help.receive", "&7* &b/dr receive&7 - Receive the login bonus."),
    HELP_GIVE("help.give", "&7* &b/dr give <player> <reward>&7 - Give the bonus."),
    HELP_RELOAD("help.reload", "&7* &b/dr reload&7 - Reload files."),
    HELP_RESET("help.reset", "&7* &b/dr reset <name/all>&7 - Reset the status of receipt."),
    HELP_SET("help.set", "&7* &b/dr set <name> <true/false>&7 - Change the status of receipt.");

    private final String key;
    private final String def;

    DefaultMessage(@NotNull String key, @NotNull String def) {
        this.key = key;
        this.def = def;
    }

    @Override
    public @NotNull String getKey() {
        return key;
    }

    @Override
    public @NotNull String get() {
        return def;
    }

    public static Locale getDefaultLocale() {
        return Translation.parseLocale("en_US");
    }
}
