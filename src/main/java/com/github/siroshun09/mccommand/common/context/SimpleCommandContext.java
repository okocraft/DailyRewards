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

package com.github.siroshun09.mccommand.common.context;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@link CommandContext} implementation class.
 */
public class SimpleCommandContext implements CommandContext {

    private final CommandSender sender;
    private final List<String> arguments;

    /**
     * Creates a {@link SimpleCommandContext}
     *
     * @param sender    the commander
     * @param arguments the given arguments
     */
    public SimpleCommandContext(@NotNull CommandSender sender, @NotNull List<String> arguments) {
        this.sender = sender;
        this.arguments = List.copyOf(arguments);
    }

    /**
     * Gets the new {@link SimpleCommandContextBuilder}
     *
     * @return the new {@link SimpleCommandContextBuilder}
     */
    @NotNull
    public static SimpleCommandContextBuilder newBuilder() {
        return new SimpleCommandContextBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public CommandSender getSender() {
        return sender;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CommandContext)) {
            return false;
        }

        CommandContext that = (CommandContext) o;

        return getSender().equals(that.getSender()) &&
               getArguments().equals(that.getArguments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getArguments());
    }

    @Override
    public String toString() {
        return "SimpleCommandContext{" +
               "sender=" + sender +
               ", arguments=" + arguments +
               '}';
    }

    /**
     * Builder class of {@link CommandContext}.
     */
    public static class SimpleCommandContextBuilder {
        private CommandSender sender;
        private List<String> arguments;

        private SimpleCommandContextBuilder() {
        }

        /**
         * Sets the commander.
         *
         * @param sender the commander
         * @return the builder instance
         */
        public SimpleCommandContextBuilder setSender(CommandSender sender) {
            this.sender = sender;
            return this;
        }

        /**
         * Sets the arguments.
         *
         * @param arguments the given arguments
         * @return the builder instance
         * @throws NullPointerException if arguments is null
         */
        public SimpleCommandContextBuilder setArguments(@NotNull String[] arguments) {
            Objects.requireNonNull(arguments);
            List<String> result = new ArrayList<>();

            for (String argument : arguments) {
                String arg = Objects.requireNonNull(argument);
                result.add(arg);
            }

            this.arguments = result;
            return this;
        }

        /**
         * Builds the {@link CommandContext}
         *
         * @return the {@link CommandContext}
         * @throws NullPointerException if any of the values are not set
         */
        @NotNull
        public CommandContext build() {
            Objects.requireNonNull(sender);
            Objects.requireNonNull(arguments);

            return new SimpleCommandContext(sender, arguments);
        }
    }
}
