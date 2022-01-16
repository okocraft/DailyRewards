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

import com.github.siroshun09.mcmessage.MessageHoldable;
import com.github.siroshun09.mcmessage.message.KeyedMessage;
import com.github.siroshun09.mcmessage.translation.Translation;
import com.github.siroshun09.mcmessage.util.InvalidMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public interface LanguageLoader extends MessageHoldable {

    @Contract("_ -> new")
    static @NotNull LanguageLoader fromPropertiesFile(@NotNull Path filePath) {
        Objects.requireNonNull(filePath);

        if (Files.exists(filePath)) {
            return new PropertiesFileLoader(filePath);
        } else {
            throw new IllegalStateException("The file not found: " + filePath.toAbsolutePath());
        }
    }

    @NotNull @Unmodifiable Set<InvalidMessage> load() throws IOException;

    void save(@NotNull Iterable<? extends KeyedMessage> keyedMessages) throws IOException;

    default @Nullable Translation toTranslation() {
        Locale locale = parseLocaleFromFileName();
        return locale != null ? toTranslation(locale) : null;
    }

    @NotNull Translation toTranslation(@NotNull Locale locale);

    @Nullable Locale parseLocaleFromFileName();
}
