package net.okocraft.dailyrewards.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.okocraft.dailyrewards.DailyRewards;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

public class ReceiveData implements Closeable {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String FILE_NAME = "data.json";

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

        JsonObject json;

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            json = GSON.fromJson(reader, JsonObject.class);
        }

        String path = DATE_TIME_FORMATTER.format(date);

        if (json.has(path)) {
            JsonElement element = json.get(path);

            if (element.isJsonArray()) {
                element.getAsJsonArray().iterator().forEachRemaining(el -> addUuidOrIgnore(el.getAsString()));
            }
        }
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
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();

        receivedPlayers.stream().map(UUID::toString).forEach(array::add);
        json.add(DATE_TIME_FORMATTER.format(date), array);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            GSON.toJson(json, writer);
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
