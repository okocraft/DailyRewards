package net.okocraft.dailyrewards.command.subcommand;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

abstract class AbstractCommand implements Command {

    private final String name;
    private final String permission;
    private final Set<String> aliases;

    AbstractCommand(@NotNull String name, @NotNull String permission, @NotNull Set<String> aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = Set.copyOf(aliases);
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getPermission() {
        return permission;
    }

    @Override
    public @NotNull @Unmodifiable Set<String> getAliases() {
        return aliases;
    }
}
