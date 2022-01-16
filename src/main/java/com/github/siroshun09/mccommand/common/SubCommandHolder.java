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

package com.github.siroshun09.mccommand.common;

import com.github.siroshun09.mccommand.common.argument.Argument;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class that holds the subcommands
 */
public class SubCommandHolder {

    private final List<Command> subCommands;

    private SubCommandHolder(@NotNull Collection<Command> subCommands) {
        this.subCommands = List.copyOf(subCommands);
    }

    /**
     * Create a {@link SubCommandHolder} from a collection of subcommands.
     *
     * @param subCommands the subcommands in the holder
     * @return a new {@link SubCommandHolder}
     */
    @Contract("_ -> new")
    @NotNull
    public static SubCommandHolder of(@NotNull Collection<Command> subCommands) {
        Objects.requireNonNull(subCommands);
        return new SubCommandHolder(subCommands);
    }

    /**
     * Create a {@link SubCommandHolder} from specified subcommands.
     *
     * @param subCommands the subcommands in holder
     * @return a new {@link SubCommandHolder}
     */
    @Contract("_ -> new")
    @NotNull
    public static SubCommandHolder of(@NotNull Command... subCommands) {
        Objects.requireNonNull(subCommands);
        return new SubCommandHolder(Arrays.asList(subCommands));
    }

    /**
     * Gets subcommands that is included in this holder.
     *
     * @return the included subcommands in this holder
     */
    @Unmodifiable
    @NotNull
    public List<Command> getSubCommands() {
        return subCommands;
    }

    /**
     * Gets the command whose name matches the string contained in the {@link Argument}.
     *
     * @param argument the argument to search
     * @return the search result
     */
    @NotNull
    public Optional<Command> searchOptional(@NotNull Argument argument) {
        Objects.requireNonNull(argument);
        return searchOptional(argument.get());
    }

    /**
     * Gets the command whose name matches the string.
     *
     * @param str the string to search
     * @return the search result
     */
    @NotNull
    public Optional<Command> searchOptional(@NotNull String str) {
        Objects.requireNonNull(str);

        Optional<Command> cmd = subCommands.stream().filter(c -> c.getName().equalsIgnoreCase(str)).findFirst();

        if (cmd.isPresent()) {
            return cmd;
        } else {
            return subCommands.stream()
                    .filter(c -> c.getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(str)))
                    .findFirst();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubCommandHolder)) return false;
        SubCommandHolder that = (SubCommandHolder) o;
        return Objects.equals(subCommands, that.subCommands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subCommands);
    }

    @Override
    public String toString() {
        return "SubCommandHolder{" +
                "subCommands=" + subCommands +
                '}';
    }
}
