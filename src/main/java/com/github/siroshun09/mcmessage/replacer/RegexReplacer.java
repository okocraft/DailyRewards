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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public interface RegexReplacer extends Replacer {

    @Contract("_, _ -> new")
    static @NotNull RegexReplacer create(@NotNull Pattern pattern, @NotNull String replacement) {
        return new RegexReplacerImpl(pattern, replacement);
    }

    @Contract("_, _ -> new")
    static @NotNull RegexReplacer create(@NotNull String pattern, @NotNull String replacement) throws PatternSyntaxException {
        return create(Pattern.compile(pattern), replacement);
    }

    @NotNull Pattern getPattern();

    @Override
    default @NotNull String getPlaceholder() {
        return getPattern().pattern();
    }

    @Override
    default @NotNull String replace(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        return getPattern().matcher(str).replaceAll(getReplacement());
    }

    @Override
    default @NotNull StringBuilder replace(@NotNull StringBuilder builder) {
        var matcher = getPattern().matcher(builder);
        var replaced = matcher.replaceAll(getReplacement());
        builder.setLength(0);
        return builder.append(replaced);
    }

    @Override
    @NotNull
    default StringBuffer replace(@NotNull StringBuffer buffer) {
        var matcher = getPattern().matcher(buffer);
        var replaced = matcher.replaceAll(getReplacement());
        buffer.setLength(0);
        return buffer.append(replaced);
    }
}
