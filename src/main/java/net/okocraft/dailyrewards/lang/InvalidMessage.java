package net.okocraft.dailyrewards.lang;

import org.jetbrains.annotations.NotNull;

public record InvalidMessage(int line, @NotNull String str, @NotNull Reason reason) {
    public enum Reason {
        INVALID_FORMAT,
        DUPLICATE_KEY
    }
}
