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

class RegexPlaceholderImpl implements RegexPlaceholder {

    private final Pattern pattern;

    RegexPlaceholderImpl(@NotNull Pattern pattern) {
        this.pattern = Objects.requireNonNull(pattern);
    }

    @Override
    public @NotNull Pattern getPattern() {
        return pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof RegexPlaceholderImpl) {
            RegexPlaceholderImpl that = (RegexPlaceholderImpl) o;
            return getPattern().pattern().equals(that.getPattern().pattern());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPattern().pattern());
    }

    @Override
    public String toString() {
        return "RegexPlaceholderImpl{" +
                "pattern=" + pattern +
                '}';
    }
}
