package net.okocraft.dailyrewards.lang;

import com.github.siroshun09.mccommand.common.Command;
import com.github.siroshun09.mccommand.common.argument.Argument;
import com.github.siroshun09.mcmessage.replacer.FunctionalPlaceholder;
import net.okocraft.dailyrewards.reward.Reward;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.nio.file.Path;
import java.util.UUID;

import static com.github.siroshun09.mcmessage.replacer.FunctionalPlaceholder.create;

public final class Placeholders {
    public static final FunctionalPlaceholder<Player> PLAYER = create("%player%", Player::getName);
    public static final FunctionalPlaceholder<Argument> PLAYER_NAME = create("%player%", Argument::get);
    public static final FunctionalPlaceholder<UUID> UUID = create("%uuid%", java.util.UUID::toString);
    public static final FunctionalPlaceholder<Reward> REWARD = create("%reward%", Reward::getName);
    public static final FunctionalPlaceholder<Argument> REWARD_NAME= create("%reward%", Argument::get);
    public static final FunctionalPlaceholder<Path> FILE_NAME = create("%file%", p -> p.getFileName().toString());
    public static final FunctionalPlaceholder<Command> COMMAND_PERM = create("%perm%", Command::getPermission);
    public static final FunctionalPlaceholder<Argument> ARG =  create("%arg%", Argument::get);
    public static final FunctionalPlaceholder<Plugin> VERSION = create("%version%", p -> p.getDescription().getVersion());
}
