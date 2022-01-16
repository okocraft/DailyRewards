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

package com.github.siroshun09.mcmessage.builder;

import com.github.siroshun09.mcmessage.MessageReceiver;
import com.github.siroshun09.mcmessage.message.Message;
import com.github.siroshun09.mcmessage.replacer.FunctionalPlaceholder;
import com.github.siroshun09.mcmessage.replacer.Placeholder;
import com.github.siroshun09.mcmessage.replacer.Replacer;
import com.github.siroshun09.mcmessage.util.Colorizer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlainTextBuilder implements Builder<Message> {

    private final StringBuilder builder;
    private boolean colorize;

    public PlainTextBuilder(@NotNull Message original) {
        Objects.requireNonNull(original);
        this.builder = new StringBuilder(original.get());
    }

    public @NotNull PlainTextBuilder append(@NotNull String str) {
        builder.append(str);
        return this;
    }

    public @NotNull PlainTextBuilder append(@NotNull Message message) {
        return append(message.get());
    }

    public @NotNull PlainTextBuilder addPrefix(@NotNull String str) {
        builder.insert(0, str);
        return this;
    }

    public @NotNull PlainTextBuilder addPrefix(@NotNull Message message) {
        return addPrefix(message.get());
    }

    @NotNull
    public PlainTextBuilder replace(@NotNull Replacer replacer) {
        replacer.replace(builder);
        return this;
    }

    @NotNull
    public PlainTextBuilder replace(@NotNull Placeholder placeholder, @NotNull String replacement) {
        return replace(placeholder.toReplacer(replacement));
    }

    @NotNull
    public <T> PlainTextBuilder replace(@NotNull FunctionalPlaceholder<T> placeholder, @NotNull T value) {
        return replace(placeholder.toReplacer(value));
    }

    @NotNull
    public PlainTextBuilder setColorize(boolean colorize) {
        this.colorize = colorize;
        return this;
    }

    @Override
    public @NotNull Message build() {
        if (colorize) {
            Colorizer.colorize(builder);
        }

        return builder::toString;
    }

    public void send(@NotNull MessageReceiver messageReceiver) {
        messageReceiver.sendMessage(build());
    }
}
