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

package com.github.siroshun09.mcmessage.message;

import com.github.siroshun09.mcmessage.builder.PlainTextBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface Message {

    @Contract("_ -> new")
    static @NotNull Message of(@NotNull String message) {
        Objects.requireNonNull(message);
        return new MessageImpl(message);
    }

    @NotNull String get();

    default @NotNull PlainTextBuilder toPlainTextBuilder() {
        return new PlainTextBuilder(this);
    }

}
