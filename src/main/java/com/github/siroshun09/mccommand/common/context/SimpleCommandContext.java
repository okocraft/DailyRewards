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

import com.github.siroshun09.mccommand.common.Command;
import com.github.siroshun09.mccommand.common.argument.Argument;
import com.github.siroshun09.mccommand.common.sender.Sender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@link CommandContext} implementation class.
 */
public class SimpleCommandContext implements CommandContext {

    private final Command command;
    private final Sender sender;
    private final List<Argument> arguments;
    private final String label;

    /**
     * Creates a {@link SimpleCommandContext}
     *
     * @param command   the executed command
     * @param sender    the commander
     * @param arguments the given arguments
     * @param label     the string used to specify the command
     */
    public SimpleCommandContext(@NotNull Command command, @NotNull Sender sender,
                                @NotNull List<Argument> arguments, @NotNull String label) {
        this.command = command;
        this.sender = sender;
        this.arguments = List.copyOf(arguments);
        this.label = label;
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
    public Command getCommand() {
        return command;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Sender getSender() {
        return sender;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Argument> getArguments() {
        return arguments;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getLabel() {
        return label;
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

        return getCommand().equals(that.getCommand()) &&
                getSender().equals(that.getSender()) &&
                getArguments().equals(that.getArguments()) &&
                getLabel().equals(that.getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommand(), getSender(), getArguments(), getLabel());
    }

    @Override
    public String toString() {
        return "SimpleCommandContext{" +
                "command=" + command +
                ", sender=" + sender +
                ", arguments=" + arguments +
                ", label='" + label + '\'' +
                '}';
    }

    /**
     * Builder class of {@link CommandContext}.
     */
    public static class SimpleCommandContextBuilder {
        private Command command;
        private Sender sender;
        private List<Argument> arguments;
        private String label;

        private SimpleCommandContextBuilder() {
        }

        /**
         * Sets the executed command.
         *
         * @param command the executed command
         * @return the builder instance
         */
        public SimpleCommandContextBuilder setCommand(Command command) {
            this.command = command;
            return this;
        }

        /**
         * Sets the commander.
         *
         * @param sender the commander
         * @return the builder instance
         */
        public SimpleCommandContextBuilder setSender(Sender sender) {
            this.sender = sender;
            return this;
        }

        /**
         * Sets the arguments.
         *
         * @param arguments the given arguments
         * @return the builder instance
         */
        public SimpleCommandContextBuilder setArguments(List<Argument> arguments) {
            this.arguments = arguments;
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
            List<Argument> result = new ArrayList<>();

            for (int i = 0; i < arguments.length; i++) {
                String arg = Objects.requireNonNull(arguments[i]);
                result.add(Argument.of(i, arg));
            }

            return setArguments(result);
        }

        /**
         * Sets the string used to specify the command.
         *
         * @param label the string used to specify the command
         * @return the builder instance
         */
        public SimpleCommandContextBuilder setLabel(String label) {
            this.label = label;

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
            Objects.requireNonNull(command);
            Objects.requireNonNull(sender);
            Objects.requireNonNull(arguments);
            Objects.requireNonNull(label);

            return new SimpleCommandContext(command, sender, arguments, label);
        }
    }
}
