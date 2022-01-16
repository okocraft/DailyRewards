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

import com.github.siroshun09.mcmessage.MessageReceiver;
import com.github.siroshun09.mcmessage.loader.FileType;
import com.github.siroshun09.mcmessage.message.DefaultMessage;
import com.github.siroshun09.mcmessage.message.TranslatedMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.logging.Logger;

public interface LanguageManager {

    @Contract("_, _, _, _ -> new")
    static @NotNull LanguageManager create(@NotNull Path directory, @NotNull Locale defaultLocale,
                                           @NotNull FileType fileType, @NotNull Logger logger) {
        return new LanguageManagerImpl(directory, defaultLocale, fileType, logger);
    }

    @NotNull TranslatedMessage getTranslatedMessage(@NotNull DefaultMessage defaultMessage,
                                                    @NotNull Locale locale);

    default @NotNull TranslatedMessage getTranslatedMessage(@NotNull DefaultMessage defaultMessage,
                                                            @NotNull MessageReceiver messageReceiver) {
        return getTranslatedMessage(defaultMessage, messageReceiver.getLocale());
    }

    void loadOrSaveDefaultLanguageFile(@NotNull Iterable<? extends DefaultMessage> defaultMessages) throws IOException;

    void loadCustomLanguageFiles() throws IOException;
}
