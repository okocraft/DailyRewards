package net.okocraft.dailyrewards.lang;

import com.github.siroshun09.mcmessage.translation.Translation;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.okocraft.dailyrewards.DailyRewards;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LanguageManager {

    private static final String FILE_EXTENSION = ".properties";
    public static final String JA_JP_FILENAME = "ja_JP.properties";

    private final DailyRewards plugin;
    private final Path directory;
    private final Map<Locale, Translation> translations = new HashMap<>();

    public LanguageManager(@NotNull DailyRewards plugin) {
        this.plugin = plugin;
        this.directory = plugin.getDataFolder().toPath().resolve("lang");
    }

    @NotNull
    public String getMessage(@NotNull DefaultMessage msg, @NotNull Audience receiver) {
        Locale locale = receiver.getOrDefaultFrom(Identity.LOCALE, Locale::getDefault);
        Translation translation = this.translations.get(locale);
        if (translation != null) {
            String message = translation.getMessage(msg.getKey());
            if (message != null) {
                return message;
            }
        }

        return msg.getMessage();
    }

    public void reload() throws IOException {
        if (!Files.isDirectory(directory)) {
            Files.createDirectories(directory);
        }

        loadDefaultLanguage();
        loadCustomLanguageFiles();

        plugin.getLogger().info(
                "Loaded languages: " + this.translations.values()
                        .stream()
                        .map(Translation::getLocale)
                        .map(Locale::toString)
                        .sorted()
                        .collect(Collectors.joining(", ")));
    }

    private void loadDefaultLanguage() throws IOException {
        Locale locale = DefaultMessage.getDefaultLocale();
        Path defFile = directory.resolve(locale.toString() + FILE_EXTENSION);

        Translation translation;
        if (Files.exists(defFile)) {
            translation = getLoadedLanguageFile(defFile).toTranslation(locale);
        } else {
            try (BufferedWriter writer = Files.newBufferedWriter(defFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                StringBuilder builder = new StringBuilder();
                for (DefaultMessage defMsg : DefaultMessage.values()) {
                    builder.setLength(0);
                    builder.append(defMsg.getKey()).append('=').append(defMsg.getMessage());
                    writer.write(builder.toString());
                    writer.newLine();
                }
            }

            translation = Translation.of(
                    locale,
                    Stream.of(DefaultMessage.values()).collect(Collectors.toMap(DefaultMessage::getKey, DefaultMessage::getMessage))
            );
        }

        this.translations.put(locale, translation);
    }

    private void loadCustomLanguageFiles() throws IOException {
        try (Stream<Path> files = Files.list(directory)) {
            files.filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .filter(p -> p.getFileName().toString().endsWith(FILE_EXTENSION))
                    .filter(p -> !p.getFileName().toString().equals(DefaultMessage.getDefaultLocale().toString() + FILE_EXTENSION))
                    .map(this::getLoadedLanguageFileUnsafe)
                    .filter(Objects::nonNull)
                    .map(PropertiesFileLoader::toTranslation)
                    .filter(Objects::nonNull)
                    .forEach(translation -> this.translations.put(translation.getLocale(), translation));
        }
    }

    private PropertiesFileLoader getLoadedLanguageFile(@NotNull Path path) throws IOException {
        PropertiesFileLoader loader = new PropertiesFileLoader(path);
        new PropertiesFileLoader(path).load().forEach(this::printInvalidMessage);
        return loader;
    }

    @Nullable
    private PropertiesFileLoader getLoadedLanguageFileUnsafe(@NotNull Path path) {
        try {
            return getLoadedLanguageFile(path);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not load " + path.getFileName().toString(), e);
            return null;
        }
    }

    private void printInvalidMessage(@NotNull InvalidMessage invalid) {
        plugin.getLogger().warning("Invalid message: " + invalid);
    }
}
