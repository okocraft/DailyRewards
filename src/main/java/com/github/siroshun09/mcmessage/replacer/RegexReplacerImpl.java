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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

class RegexReplacerImpl implements RegexReplacer {

    private final Pattern pattern;
    private final String replacement;

    RegexReplacerImpl(@NotNull Pattern pattern, @NotNull String replacement) {
        this.pattern = Objects.requireNonNull(pattern);
        this.replacement = Objects.requireNonNull(replacement);
    }

    @Override
    public @NotNull Pattern getPattern() {
        return pattern;
    }

    @Override
    public @NotNull String getReplacement() {
        return replacement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof RegexReplacerImpl) {
            RegexReplacerImpl that = (RegexReplacerImpl) o;
            return Objects.equals(getPattern(), that.getPattern()) &&
                    Objects.equals(getReplacement(), that.getReplacement());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPattern(), getReplacement());
    }

    @Override
    public String toString() {
        return "RegexReplacerImpl{" +
                "pattern=" + pattern +
                ", replacement='" + replacement + '\'' +
                '}';
    }
}
