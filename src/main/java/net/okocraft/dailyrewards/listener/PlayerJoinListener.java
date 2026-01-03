package net.okocraft.dailyrewards.listener;

import io.papermc.paper.util.Tick;
import net.okocraft.dailyrewards.DailyRewards;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class PlayerJoinListener implements Listener {

    private static final String AUTO_RECEIVE_PERMISSION = "dailyrewards.autoreceive";

    private final DailyRewards plugin;

    public PlayerJoinListener(@NotNull DailyRewards plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void shutdown() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        event.getPlayer().getScheduler().runDelayed(
                this.plugin,
                ignored -> {
                    if (event.getPlayer().hasPermission(AUTO_RECEIVE_PERMISSION)) {
                        plugin.getProcessors().getPlayerReceiveProcessor().tryReceive(event.getPlayer());
                    }
                },
                null,
                Tick.tick().fromDuration(Duration.ofSeconds(plugin.getGeneralConfig().getAutoReceiveDelay()))
        );
    }
}
