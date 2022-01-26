package net.okocraft.dailyrewards.config;

import com.github.siroshun09.configapi.yaml.YamlConfiguration;
import net.okocraft.dailyrewards.DailyRewards;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public class GeneralConfig {

    private final YamlConfiguration yaml;

    private Sound receiveSound;
    private Sound cannotReceiveSound;

    public GeneralConfig(@NotNull DailyRewards plugin) {
        this.yaml = YamlConfiguration.create(plugin.getDataFolder().toPath().resolve("config.yml"));

        try (yaml) {
            yaml.load();
            setSounds();
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.yml", e);
        }
    }

    public void reload() throws IOException {
        try (yaml) {
            yaml.load();
            setSounds();
        }
    }

    public boolean isAutoReceiveEnabled() {
        return yaml.getBoolean("auto-receive.enable", true);
    }

    public long getAutoReceiveDelay() {
        return yaml.getLong("auto-receive.delay", 3);
    }

    public boolean isDisabledWorld(@NotNull String name) {
        return yaml.getStringList("disabled-worlds").contains(name);
    }

    public boolean isSoundEnabled() {
        return yaml.getBoolean("sound.enable", true);
    }

    @NotNull
    public Sound getReceiveSound() {
        return receiveSound;
    }

    public float getReceiveSoundVolume() {
        return getFloat("sound.receive.volume", 100f, 0f, 200f);
    }

    public float getReceiveSoundPitch() {
        return getFloat("sound.receive.pitch", 1.0f, 0.5f, 2.0f);
    }

    @NotNull
    public Sound getCannotReceiveSound() {
        return cannotReceiveSound;
    }

    public float getCannotReceiveSoundVolume() {
        return getFloat("sound.cannot-receive.volume", 100f, 0f, 200f);
    }

    public float getCannotReceiveSoundPitch() {
        return getFloat("sound.cannot-receive.pitch", 1.0f, 0.5f, 2.0f);
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

    private float getFloat(@NotNull String path, float def, float min, float max) {
        float value = (float) yaml.getDouble(path, def);
        return min <= value && value <= max ? value : def;
    }
}
