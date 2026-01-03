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
