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

package com.github.siroshun09.mccommand.common.sender;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Interface that represents a console.
 *
 * @see Sender
 */
public interface ConsoleSender extends Sender {

    /**
     * the {@link UUID} of console.
     */
    UUID CONSOLE_UUID = new UUID(0, 0);

    /**
     * the name of console.
     */
    String CONSOLE_NAME = "Console";

    /**
     * {@inheritDoc}
     */
    @Override
    default @NotNull UUID getUUID() {
        return CONSOLE_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default @NotNull String getName() {
        return CONSOLE_NAME;
    }
}