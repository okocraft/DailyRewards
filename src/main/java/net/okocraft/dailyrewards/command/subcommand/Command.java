package net.okocraft.dailyrewards.command.subcommand;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface Command {

    @NotNull String getName();

    @NotNull String getPermission();

    default @NotNull @Unmodifiable Set<String> getAliases() {
        return Collections.emptySet();
    }

    void onExecution(@NotNull CommandSender sender, @NotNull List<String> arguments);

    @NotNull List<String> onTabCompletion(@NotNull CommandSender sender, @NotNull List<String> arguments);
}
