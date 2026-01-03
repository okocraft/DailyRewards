package net.okocraft.dailyrewards.config;

import net.okocraft.dailyrewards.DailyRewards;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Locale;

public class GeneralConfig {

    private final Path filepath;
    private final YamlConfiguration yaml;

    private Sound receiveSound;
    private Sound cannotReceiveSound;

    public GeneralConfig(@NotNull DailyRewards plugin) {
        this.filepath = plugin.getDataFolder().toPath().resolve("config.yml");
        this.yaml = YamlConfiguration.loadConfiguration(this.filepath.toFile());
        setSounds();
    }

    public void reload() throws Exception {
        yaml.load(this.filepath.toFile());
        setSounds();
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
        return this.filepath;
    }

    private void setSounds() {
        this.receiveSound = this.getSound("sound.receive.sound", Sound.ENTITY_PLAYER_LEVELUP);
        this.cannotReceiveSound = this.getSound("sound.cannot-receive.sound", Sound.BLOCK_ANVIL_PLACE);
    }

    private float getFloat(@NotNull String path, float def, float min, float max) {
        float value = (float) yaml.getDouble(path, def);
        return min <= value && value <= max ? value : def;
    }

    private @NotNull Sound getSound(@NotNull String path, @NotNull Sound def) {
        String value = this.yaml.getString(path);
        if (value == null || value.isEmpty()) {
            return def;
        }

        NamespacedKey key;
        if (value.contains(":") || value.contains(".")) { // namespaced key
            key = NamespacedKey.fromString(value);
            if (key == null) {
                return def;
            }
        } else { // enum name (for backward compatibility)
            key = NamespacedKey.minecraft(value.replace("_", ".").toLowerCase(Locale.ENGLISH));
        }

        try {
            return Registry.SOUNDS.getOrThrow(key);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }
}
