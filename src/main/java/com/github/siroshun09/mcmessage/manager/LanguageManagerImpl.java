/*
 *     Copyright 2021 Siroshun09
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

package com.github.siroshun09.mcmessage.manager;

import com.github.siroshun09.mcmessage.loader.FileType;
import com.github.siroshun09.mcmessage.loader.LanguageLoader;
import com.github.siroshun09.mcmessage.message.DefaultMessage;
import com.github.siroshun09.mcmessage.message.TranslatedMessage;
import com.github.siroshun09.mcmessage.translation.Translation;
import com.github.siroshun09.mcmessage.translation.TranslationRegistry;
import com.github.siroshun09.mcmessage.util.InvalidMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

class LanguageManagerImpl implements LanguageManager {

    private final Path directory;
    private final Locale defaultLocale;
    private final FileType fileType;
    private final Logger logger;
    private final TranslationRegistry registry;

    LanguageManagerImpl(@NotNull Path directory, @NotNull Locale defaultLocale,
                        @NotNull FileType fileType, @NotNull Logger logger) {
        this.directory = Objects.requireNonNull(directory);
        this.defaultLocale = Objects.requireNonNull(defaultLocale);
        this.fileType = Objects.requireNonNull(fileType);
        this.logger = Objects.requireNonNull(logger);
        this.registry = TranslationRegistry.create();
    }

    @Override
    public @NotNull TranslatedMessage getTranslatedMessage(@NotNull DefaultMessage defaultMessage,
                                                           @NotNull Locale locale) {
        return registry.getMessage(defaultMessage, locale);
    }

    @Override
    public void loadOrSaveDefaultLanguageFile(
            @NotNull Iterable<? extends DefaultMessage> defaultMessages) throws IOException {
        var path = directory.resolve(defaultLocale.toString() + fileType.getExtension());

        Translation translation;

        if (Files.exists(path)) {
            translation = getLoadedLanguageFile(path).toTranslation(defaultLocale);
        } else {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE)) {
                var builder = new StringBuilder();

                for (var defMsg : defaultMessages) {
                    builder.setLength(0);
                    builder.append(defMsg.getKey()).append('=').append(defMsg.get());
                    writer.write(builder.toString());
                    writer.newLine();
                }
            }

            translation = Translation.of(defaultLocale, defaultMessages);
        }

        registry.register(translation);
    }

    @Override
    public void loadCustomLanguageFiles() throws IOException {
        try (Stream<Path> files = Files.list(directory)) {
            files.filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .filter(fileType::match)
                    .filter(p -> !p.getFileName().toString().equals(defaultLocale.toString() + fileType.getExtension()))
                    .map(this::getLoadedLanguageFileUnsafe)
                    .filter(Objects::nonNull)
                    .map(LanguageLoader::toTranslation)
                    .filter(Objects::nonNull)
                    .forEach(registry::register);
        }
    }

    private @NotNull LanguageLoader getLoadedLanguageFile(@NotNull Path path) throws IOException {
        var loader = fileType.createLoader(path);
        loader.load().forEach(this::printInvalidMessage);
        return loader;
    }

    private @Nullable LanguageLoader getLoadedLanguageFileUnsafe(@NotNull Path path) {
        try {
            return getLoadedLanguageFile(path);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not load " + path.getFileName().toString(), e);
            return null;
        }
    }

    private void printInvalidMessage(@NotNull InvalidMessage invalid) {
        logger.warning("Invalid message: " + invalid);
    }
}
