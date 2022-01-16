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

package com.github.siroshun09.mcmessage.translation;

import com.github.siroshun09.mcmessage.message.TranslatedMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

class TranslationRegistryImpl implements TranslationRegistry {

    private final Map<Locale, Translation> translations = new HashMap<>();

    TranslationRegistryImpl() {
    }

    @Override
    public void register(@NotNull Translation translation) {
        Objects.requireNonNull(translation);

        synchronized (translations) {
            translations.put(translation.getLocale(), translation);
        }
    }

    @Override
    public @NotNull Collection<Translation> getTranslations() {
        return translations.values();
    }

    @Override
    public @Nullable TranslatedMessage getMessage(@NotNull String key, @NotNull Locale locale) {
        Translation translation = translations.get(locale);
        return translation != null ? translation.getMessage(key) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof TranslationRegistryImpl) {
            TranslationRegistryImpl that = (TranslationRegistryImpl) o;
            return translations.equals(that.translations);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(translations);
    }

    @Override
    public String toString() {
        return "TranslationRegistryImpl{" +
                "translations=" + translations +
                '}';
    }
}
