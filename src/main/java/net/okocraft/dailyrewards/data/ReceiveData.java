package net.okocraft.dailyrewards.data;

import com.github.siroshun09.configapi.bukkit.BukkitYamlFactory;
import net.okocraft.dailyrewards.DailyRewards;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ReceiveData implements Closeable {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String FILE_NAME = "data.yml";

    private final DailyRewards plugin;
    private final Path filePath;
    private final Set<UUID> receivedPlayers = new HashSet<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private LocalDate date = LocalDate.now();

    public ReceiveData(@NotNull DailyRewards plugin) {
        this.plugin = plugin;
        this.filePath = plugin.getDataFolder().toPath().resolve(FILE_NAME);

        if (Files.exists(filePath)) {
            try {
                reload();
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING, "Could not load data.", e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            plugin.getLogger().log(Level.SEVERE, "", e);
        }

        save();
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

    public void reload() throws IOException {
        reset();

        if (!Files.exists(filePath)) {
            return;
        }

        BukkitYamlFactory
                .load(filePath)
                .getStringList(DATE_TIME_FORMATTER.format(date))
                .forEach(this::addUuidOrIgnore);
    }

    public void saveAsync() {
        executor.execute(() -> {
            try {
                save();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not save data.", e);
            }
        });
    }

    public void save() throws IOException {
        var yaml = BukkitYamlFactory.getBukkitYaml(filePath);

        yaml.set(
                DATE_TIME_FORMATTER.format(date),
                receivedPlayers
                        .stream()
                        .map(UUID::toString)
                        .collect(Collectors.toUnmodifiableList())
        );

        yaml.save();
    }

    private void addUuidOrIgnore(@NotNull String str) {
        try {
            UUID uuid = UUID.fromString(str);
            receivedPlayers.add(uuid);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
