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

import java.util.function.Function;

public interface FunctionalPlaceholder<T> extends Placeholder {

    @Contract("_, _ -> new")
    static @NotNull <T> FunctionalPlaceholder<T> create(@NotNull String placeholder, @NotNull Function<T, String> function) {
        return new FunctionalPlaceholderImpl<>(placeholder, function);
    }

    @NotNull Function<T, String> getFunction();

    default @NotNull Replacer toReplacer(@NotNull T value) {
        return Replacer.create(getPlaceholder(), getFunction().apply(value));
    }

}
