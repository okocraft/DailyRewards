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

package com.github.siroshun09.mccommand.common.argument;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a command argument.
 */
public interface Argument {

    /**
     * Creates an {@link Argument} from an index and a string.
     * <p>
     * The {@link Argument} returned by this method implements {@link Object#hashCode()}, {@link Object#equals(Object)}, and {@link Object#toString()}.
     *
     * @param index    the position of this argument
     * @param argument the argument
     * @return {@link Argument}
     */
    static Argument of(int index, @NotNull String argument) {
        return new Argument() {
            @Override
            public int getIndex() {
                return index;
            }

            @Override
            public @NotNull String get() {
                return argument;
            }

            @Override
            public int hashCode() {
                return Objects.hash(index, argument);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }

                if (!(o instanceof Argument)) {
                    return false;
                }

                Argument that = (Argument) o;

                return index == that.getIndex() && argument.equals(that.get());
            }

            @Override
            public String toString() {
                return "Argument{" +
                        "index=" + index +
                        ", argument=" + argument +
                        '}';
            }
        };
    }

    /**
     * Gets the position of this argument.
     *
     * @return the position of this argument
     */
    int getIndex();

    /**
     * Gets this argument as a string.
     *
     * @return this argument as a string
     */
    @NotNull
    String get();
}
