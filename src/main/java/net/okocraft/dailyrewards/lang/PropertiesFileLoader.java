package net.okocraft.dailyrewards.lang;

import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

class PropertiesFileLoader {

    private final Path filePath;
    private final Map<String, String> messages;

    PropertiesFileLoader(@NotNull Path filePath) {
        this.filePath = filePath;
        this.messages = new HashMap<>();
    }

    @NotNull @Unmodifiable Set<InvalidMessage> load() throws IOException {
        if (!Files.exists(filePath)) {
            return Collections.emptySet();
        }

        Set<InvalidMessage> invalidMessages = new HashSet<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            int currentLine = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                currentLine++;

                if (line.startsWith("#")) {
                    continue;
                }

                String[] split = line.split("=", 2);

                if (split.length < 2) {
                    invalidMessages.add(new InvalidMessage(currentLine, line, InvalidMessage.Reason.INVALID_FORMAT));
                    continue;
                }

                String key = split[0];

                if (messages.containsKey(key)) {
                    invalidMessages.add(new InvalidMessage(currentLine, line, InvalidMessage.Reason.DUPLICATE_KEY));
                } else {
                    messages.put(key, split[1]);
                }
            }
        }

        return Collections.unmodifiableSet(invalidMessages);
    }

    @NotNull LanguageManager.Translation toTranslation(@NotNull Locale locale) {
        return new LanguageManager.Translation(locale, messages);
    }

    @Nullable LanguageManager.Translation toTranslation() {
        Locale locale = parseLocaleFromFileName();
        return locale != null ? toTranslation(locale) : null;
    }

    private @Nullable Locale parseLocaleFromFileName() {
        String fileName = filePath.getFileName().toString();
        return Translator.parseLocale(fileName.substring(0, fileName.length() - 11)); // .properties
    }
}
