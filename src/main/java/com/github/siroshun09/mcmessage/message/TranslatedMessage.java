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

package com.github.siroshun09.mcmessage.message;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public interface TranslatedMessage extends Message {

    @Contract("_, _ -> new")
    static @NotNull TranslatedMessage of(@NotNull String message, @NotNull Locale locale) {
        return new TranslatedMessageImpl(message, locale);
    }

    @Contract("_, _ -> new")
    static @NotNull TranslatedMessage of(@NotNull Message message, @NotNull Locale locale) {
        Objects.requireNonNull(message);
        return new TranslatedMessageImpl(message.get(), locale);
    }

    @NotNull Locale getLocale();
}
