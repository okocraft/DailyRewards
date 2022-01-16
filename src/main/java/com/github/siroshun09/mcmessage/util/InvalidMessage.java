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

package com.github.siroshun09.mcmessage.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InvalidMessage {

    private final int line;
    private final String str;
    private final Reason reason;

    public InvalidMessage(int line, @NotNull String str, @NotNull Reason reason) {
        this.line = line;
        this.str = Objects.requireNonNull(str);
        this.reason = Objects.requireNonNull(reason);
    }

    public int getLine() {
        return line;
    }


    public @NotNull String getString() {
        return str;
    }

    public @NotNull Reason getReason() {
        return reason;
    }

    public enum Reason {
        INVALID_FORMAT,
        DUPLICATE_KEY
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof InvalidMessage) {
            InvalidMessage that = (InvalidMessage) o;
            return getLine() == that.getLine() &&
                    str.equals(that.str) &&
                    getReason() == that.getReason();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLine(), str, getReason());
    }

    @Override
    public String toString() {
        return "InvalidMessage{" +
                "line=" + line +
                ", str='" + str + '\'' +
                ", reason=" + reason +
                '}';
    }
}
