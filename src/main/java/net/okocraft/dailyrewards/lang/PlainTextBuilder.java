package net.okocraft.dailyrewards.lang;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlainTextBuilder {

    private final StringBuilder builder;

    public PlainTextBuilder(@NotNull String original) {
        Objects.requireNonNull(original);
        this.builder = new StringBuilder(original);
    }

    public @NotNull PlainTextBuilder append(@NotNull String str) {
        builder.append(str);
        return this;
    }

    public @NotNull PlainTextBuilder addPrefix(@NotNull String str) {
        builder.insert(0, str);
        return this;
    }

    public @NotNull PlainTextBuilder replace(@NotNull Replacer replacer) {
        replacer.replace(builder);
        return this;
    }

    public @NotNull PlainTextBuilder replace(@NotNull String placeholder, @NotNull String replacement) {
        return this.replace(Replacer.create(placeholder, replacement));
    }

    public void send(@NotNull Audience receiver) {
        receiver.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(this.builder.toString()));
    }
}
