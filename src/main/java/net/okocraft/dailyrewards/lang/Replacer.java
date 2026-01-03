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

package net.okocraft.dailyrewards.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Replacer(@NotNull String placeholder, @NotNull String replacement)  {

    @Contract("_, _ -> new")
    public static @NotNull Replacer create(@NotNull String placeholder, @NotNull String replacement) {
        return new Replacer(placeholder, replacement);
    }

    @NotNull
    public String replace(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        return str.replace(this.placeholder, this.replacement);
    }

    public void replace(@NotNull StringBuilder builder) {
        int length = this.placeholder.length();
        int startIndex = builder.indexOf(this.placeholder);

        while (-1 < startIndex) {
            builder.replace(startIndex, startIndex + length, this.replacement);
            startIndex = builder.indexOf(this.placeholder, startIndex + 1);
        }

    }
}
