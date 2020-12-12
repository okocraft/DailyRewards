package net.okocraft.dailyrewards.config;

import com.github.siroshun09.configapi.bukkit.BukkitYaml;
import com.github.siroshun09.configapi.bukkit.BukkitYamlFactory;
import com.github.siroshun09.configapi.common.configurable.Configurable;
import net.okocraft.dailyrewards.DailyRewards;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public class GeneralConfig {

    private final BukkitYaml yaml;

    private Sound receiveSound;
    private Sound cannotReceiveSound;

    public GeneralConfig(@NotNull DailyRewards plugin) {
        this.yaml = BukkitYamlFactory.loadUnsafe(plugin, "config.yml");

        setSounds();
    }

    public void reload() throws IOException {
        yaml.reload();
        setSounds();
    }

    public @NotNull <T> T get(@NotNull Configurable<T> configurable) {
        return yaml.get(configurable);
    }

    @NotNull
    public Sound getReceiveSound() {
        return receiveSound;
    }

    @NotNull
    public Sound getCannotReceiveSound() {
        return cannotReceiveSound;
    }

    public @NotNull Path getFilePath() {
        return yaml.getPath();
    }

    private void setSounds() {
        try {
            receiveSound = Sound.valueOf(yaml.getString("sound.receive.sound", "ENTITY_PLAYER_LEVELUP"));
        } catch (IllegalArgumentException e) {
            receiveSound = Sound.ENTITY_PLAYER_LEVELUP;
        }

        try {
            cannotReceiveSound = Sound.valueOf(yaml.getString("sound.cannot-receive.sount", "BLOCK_ANVIL_PLACE"));
        } catch (IllegalArgumentException e) {
            cannotReceiveSound = Sound.BLOCK_ANVIL_PLACE;
        }
    }
}
