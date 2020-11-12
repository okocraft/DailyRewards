package net.okocraft.dailyrewards.reward;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class Reward {

    private final String name;
    private final String message;
    private final String permission;
    private final List<String> commands;

    Reward(@NotNull String name, @NotNull String message,
                  @NotNull String permission, @NotNull @Unmodifiable List<String> commands) {
        this.name = name;
        this.message = message;
        this.permission = permission;
        this.commands = commands;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    @NotNull
    public String getPermission() {
        return permission;
    }

    @NotNull
    @Unmodifiable
    public List<String> getCommands() {
        return commands;
    }
}
