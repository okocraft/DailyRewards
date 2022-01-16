/*
 *     Copyright 2020 Siroshun09
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.github.siroshun09.mcmessage.loader;

import com.github.siroshun09.mcmessage.message.KeyedMessage;
import com.github.siroshun09.mcmessage.message.Message;
import com.github.siroshun09.mcmessage.translation.Translation;
import com.github.siroshun09.mcmessage.util.InvalidMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class PropertiesFileLoader implements LanguageLoader {

    private final Path filePath;
    private final Map<String, Message> messages;

    PropertiesFileLoader(@NotNull Path filePath) {
        this.filePath = filePath;
        this.messages = new HashMap<>();
    }

    @Override
    public @NotNull @Unmodifiable Set<InvalidMessage> load() throws IOException {
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
                    messages.put(key, Message.of(split[1]));
                }
            }
        }

        return Collections.unmodifiableSet(invalidMessages);
    }

    @Override
    public void save(@NotNull Iterable<? extends KeyedMessage> keyedMessages) throws IOException {
        Objects.requireNonNull(keyedMessages);

        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            StringBuilder builder = new StringBuilder();

            for (KeyedMessage msg : keyedMessages) {
                builder.setLength(0);
                builder.append(msg.getKey()).append('=').append(msg.get());
                writer.write(builder.toString());
                writer.newLine();
            }
        }
    }

    @Override
    public @Nullable Message getMessage(@NotNull String key) {
        Objects.requireNonNull(key);
        return messages.get(key);
    }

    @Override
    public @NotNull @Unmodifiable Set<KeyedMessage> getMessages() {
        return messages.entrySet().stream()
                .map(entry -> KeyedMessage.of(entry.getKey(), entry.getValue().get()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @NotNull Translation toTranslation(@NotNull Locale locale) {
        return Translation.of(locale, messages);
    }

    @Override
    public @Nullable Locale parseLocaleFromFileName() {
        String fileName = filePath.getFileName().toString();
        return Translation.parseLocale(fileName.substring(0, fileName.length() - 11)); // .properties
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof PropertiesFileLoader) {
            PropertiesFileLoader that = (PropertiesFileLoader) o;
            return filePath.equals(that.filePath) &&
                    messages.equals(that.messages);

        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, messages);
    }

    @Override
    public String toString() {
        return "PropertiesFileLoader{" +
                "filePath=" + filePath +
                ", messages=" + messages +
                '}';
    }
}
