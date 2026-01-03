package net.okocraft.dailyrewards.lang;

import com.github.siroshun09.mcmessage.replacer.FunctionalPlaceholder;
import com.github.siroshun09.mcmessage.replacer.Placeholder;
import net.okocraft.dailyrewards.reward.Reward;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.nio.file.Path;
import java.util.UUID;

import static com.github.siroshun09.mcmessage.replacer.FunctionalPlaceholder.create;

public final class Placeholders {
    public static final FunctionalPlaceholder<Player> PLAYER = create("%player%", Player::getName);
    public static final Placeholder PLAYER_NAME = Placeholder.create("%player%");
    public static final FunctionalPlaceholder<UUID> UUID = create("%uuid%", java.util.UUID::toString);
    public static final FunctionalPlaceholder<Reward> REWARD = create("%reward%", Reward::getName);
    public static final Placeholder REWARD_NAME= Placeholder.create("%reward%");
    public static final FunctionalPlaceholder<Path> FILE_NAME = create("%file%", p -> p.getFileName().toString());
    public static final Placeholder COMMAND_PERM = Placeholder.create("%perm%");
    public static final Placeholder ARG =  Placeholder.create("%arg%");
    public static final FunctionalPlaceholder<Plugin> VERSION = create("%version%", p -> p.getDescription().getVersion());
}
