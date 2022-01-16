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

package com.github.siroshun09.mccommand.bukkit.argument.parser;

import com.github.siroshun09.mccommand.common.argument.parser.ArgumentParser;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Parsers for Bukkit.
 */
public final class BukkitParser {

    private BukkitParser() {
        throw new UnsupportedOperationException();
    }

    /**
     * An instance of {@link ArgumentParser} that parses an {@link com.github.siroshun09.mccommand.common.argument.Argument} to {@link Player}
     */
    public static final ArgumentParser<Player> PLAYER = new PlayerParser();

    /**
     * An instance of {@link ArgumentParser} that parses an {@link com.github.siroshun09.mccommand.common.argument.Argument} to {@link OfflinePlayer}
     */
    public static final ArgumentParser<OfflinePlayer> OFFLINE_PLAYER = new OfflinePlayerParser();
}
