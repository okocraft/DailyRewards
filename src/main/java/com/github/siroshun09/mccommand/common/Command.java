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

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a command.
 */
public interface Command {

    /**
     * Gets the name of this command.
     *
     * @return the name of this command
     */
    @NotNull
    String getName();

    /**
     * Gets the permission to execute this command.
     *
     * @return the permission to execute this command
     */
    @NotNull
    String getPermission();

    /**
     * Gets aliases of this command.
     *
     * @return aliases of this command
     */
    @NotNull
    @Unmodifiable
    default Set<String> getAliases() {
        return Collections.emptySet();
    }

    /**
     * The method to call when this command is executed.
     *
     * @param sender    the sender of the executed command
     * @param arguments the arguments of the executed command
     */
    void onExecution(@NotNull CommandSender sender, @NotNull List<String> arguments);

    /**
     * The method to call when command tab completion is requested.
     *
     * @param sender    the sender of the command line when tab completion is requested
     * @param arguments the arguments of the command line when tab completion is requested
     * @return the tab completion result
     */
    @NotNull
    List<String> onTabCompletion(@NotNull CommandSender sender, @NotNull List<String> arguments);
}
