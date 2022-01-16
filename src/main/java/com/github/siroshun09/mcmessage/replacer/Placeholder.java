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

package com.github.siroshun09.mcmessage.replacer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An interface that represents a placeholder.
 */
public interface Placeholder {

    /**
     * Creates new {@link Placeholder}.
     *
     * @param placeholder a placeholder string
     * @return new {@link Placeholder}
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull Placeholder create(@NotNull String placeholder) {
        return new PlaceholderImpl(placeholder);
    }

    /**
     * Gets the placeholder string.
     *
     * @return the placeholder string
     */
    @NotNull String getPlaceholder();

    /**
     * Creates {@link Replacer} with the replacement string.
     *
     * @param replacement a replacement string
     * @return new {@link Replacer}
     */
    default @NotNull Replacer toReplacer(@NotNull String replacement) {
        Objects.requireNonNull(replacement);

        return Replacer.create(getPlaceholder(), replacement);
    }

}
