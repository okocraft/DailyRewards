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

/**
 * Enumeration class of command execution results.
 */
public enum CommandResult {

    /**
     * If an exception is thrown while executing the command.
     */
    EXCEPTION_OCCURRED,

    /**
     * If the argument is invalid.
     */
    INVALID_ARGUMENTS,

    /**
     * If the sender is not the player.
     */
    NOT_PLAYER,

    /**
     * If there are no arguments.
     */
    NO_ARGUMENT,

    /**
     * If the sender does not have the permission to execute command.
     */
    NO_PERMISSION,

    /**
     * If the state is wrong.
     */
    STATE_ERROR,

    /**
     * If the command is successfully executed.
     */
    SUCCESS
}
