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

package com.github.siroshun09.mccommand.common.argument.parser;

import com.github.siroshun09.mccommand.common.argument.Argument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Parses an argument from a String
 *
 * @param <T> the value type
 */
@FunctionalInterface
public interface ArgumentParser<T> {

    /**
     * Creates an instance of the parser from {@link Function}.
     *
     * @param function the {@link Function} to parse {@link Argument} to a specific type.
     * @param <R> the value type
     * @return the {@link ArgumentParser} instance
     */
    static <R> ArgumentParser<R> of(@NotNull Function<Argument, R> function) {
        return function::apply;
    }

    /**
     * Parses an {@link Argument} to a specified type and returns it.
     *
     * @param argument the argument to be parsed
     * @return the value to be parsed, or {@code null} if the parsing fails
     */
    @Nullable
    T parse(@NotNull Argument argument);
}
