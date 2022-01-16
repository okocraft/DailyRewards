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
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * An interface that represents context of the executed command.
 */
public interface CommandContext {

    /**
     * Gets the executed command.
     *
     * @return the executed command
     */
    @NotNull Command getCommand();

    /**
     * Gets the commander.
     *
     * @return the commander
     */
    @NotNull Sender getSender();

    /**
     * Gets the given arguments.
     *
     * @return the given arguments
     */
    @NotNull @Unmodifiable List<Argument> getArguments();

    /**
     * Gets the string used to specify the command.
     * <p>
     * It may always return the same string as {@link Command#getName()}.
     *
     * @return the string used to specify the command
     */
    @NotNull String getLabel();
}
