package net.okocraft.dailyrewards.data;

import net.okocraft.dailyrewards.DailyRewards;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReceiveData {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String FILE_NAME = "data.yml";

    private final Path filePath;
    private final Set<UUID> receivedPlayers = new HashSet<>();
    private LocalDate date = LocalDate.now();

    public ReceiveData(@NotNull DailyRewards plugin) {
        this.filePath = plugin.getDataFolder().toPath().resolve(FILE_NAME);

        if (Files.exists(this.filePath)) {
            this.reload();
        }
    }

    public @NotNull Path getFilePath() {
        return filePath;
    }

    public @NotNull Set<UUID> getReceivedPlayers() {
        return receivedPlayers;
    }

    public boolean isReceived(@NotNull UUID uuid) {
        return receivedPlayers.contains(uuid);
    }

    public boolean setReceived(@NotNull UUID uuid, boolean bool) {
        if (bool) {
            return receivedPlayers.add(uuid);
        } else {
            return receivedPlayers.remove(uuid);
        }
    }

    public boolean isNotToday() {
        return !LocalDate.now().equals(date);
    }

    public boolean reset() {
        boolean hasElement = !receivedPlayers.isEmpty();

        receivedPlayers.clear();
        date = LocalDate.now();

        return hasElement;
    }

    public void reload() {
        reset();

        if (!Files.exists(filePath)) {
            return;
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(this.filePath.toFile());
        yaml.getStringList(DATE_TIME_FORMATTER.format(date)).forEach(this::addUuidOrIgnore);
    }

    public void save(Logger logger) {
        YamlConfiguration yaml = new YamlConfiguration();

        yaml.set(
                DATE_TIME_FORMATTER.format(date),
                receivedPlayers.stream().map(UUID::toString).toList()
        );

        try {
            yaml.save(this.filePath.toFile());
        } catch (IOException e) {
            logger.error("Could not save data.", e);
        }
    }

    private void addUuidOrIgnore(@NotNull String str) {
        try {
            UUID uuid = UUID.fromString(str);
            receivedPlayers.add(uuid);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
