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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * The abstract class of {@link Command}.
 */
public abstract class AbstractCommand implements Command {

    private final String name;
    private final String permission;
    private final Set<String> aliases;

    /**
     * Creates an {@link AbstractCommand}.
     *
     * @param name the command name
     * @param permission the permission to execute this command
     * @param aliases the aliases of this command
     */
    public AbstractCommand(@NotNull String name, @Nullable String permission, @Nullable Set<String> aliases) {
        Objects.requireNonNull(name);

        if (name.isEmpty()) {
            throw new IllegalArgumentException("'name' must not be empty.");
        }

        this.name = name;
        this.permission = permission != null ? permission : "";
        this.aliases = aliases != null ? Set.copyOf(aliases) : Collections.emptySet();
    }

    /**
     * Creates an {@link AbstractCommand}.
     *
     * @param name the command name
     * @param permission the permission to execute this command
     */
    public AbstractCommand(@NotNull String name, @Nullable String permission) {
        this(name, permission, null);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getPermission() {
        return permission;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Unmodifiable
    @Override
    public Set<String> getAliases() {
        return aliases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Command)) {
            return false;
        }

        Command that = (Command) o;

        return getName().equals(that.getName()) &&
                getPermission().equals(that.getPermission()) &&
                getAliases().equals(that.getAliases());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPermission(), getAliases());
    }

    @Override
    public String toString() {
        return "AbstractCommand{" +
                "name='" + name + '\'' +
                ", permission='" + permission + '\'' +
                ", aliases=" + aliases +
                '}';
    }
}
