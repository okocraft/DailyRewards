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
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * An interface that represents placeholder using regex.
 */
public interface RegexPlaceholder extends Placeholder {

    /**
     * Creates new {@link RegexPlaceholder}.
     *
     * @param pattern a {@link Pattern}
     * @return new {@link RegexPlaceholder}
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull RegexPlaceholder create(@NotNull Pattern pattern) {
        return new RegexPlaceholderImpl(pattern);
    }

    /**
     * Creates new {@link RegexPlaceholder}.
     *
     * @param pattern a regex string to compile to {@link Pattern}
     * @return new {@link RegexPlaceholder}
     * @throws PatternSyntaxException if the expression's syntax is invalid
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull RegexPlaceholder create(@NotNull String pattern) throws PatternSyntaxException {
        Objects.requireNonNull(pattern);
        return create(Pattern.compile(pattern));
    }

    /**
     * Gets the {@link Pattern}.
     *
     * @return the {@link Pattern}
     */
    @NotNull Pattern getPattern();

    /**
     * Gets the regex string.
     *
     * @return the regex string
     * @see Pattern#toString()
     */
    @Override
    default @NotNull String getPlaceholder() {
        return getPattern().toString();
    }

    /**
     * Creates {@link RegexPlaceholder} with the replacement string.
     *
     * @param replacement a replacement string
     * @return new {@link RegexPlaceholder}
     */
    @Override
    default @NotNull RegexReplacer toReplacer(@NotNull String replacement) {
        return RegexReplacer.create(getPattern(), replacement);
    }

}
