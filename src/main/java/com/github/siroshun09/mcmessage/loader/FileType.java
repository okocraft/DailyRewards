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

package com.github.siroshun09.mcmessage.loader;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public enum FileType {
    PROPERTIES(".properties");

    private final String extension;

    FileType(@NotNull String extension) {
        this.extension = extension;
    }

    public @NotNull String getExtension() {
        return extension;
    }

    public boolean match(@NotNull Path path) {
        return match(path.getFileName().toString());
    }

    public boolean match(@NotNull String path) {
        return path.endsWith(extension);
    }

    public @NotNull LanguageLoader createLoader(@NotNull Path path) {
        Objects.requireNonNull(path);

        if (this == FileType.PROPERTIES) {
            return LanguageLoader.fromPropertiesFile(path);
        } else {
            throw new IllegalArgumentException("Could not create the language loader.");
        }
    }
}
