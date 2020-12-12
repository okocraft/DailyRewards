package net.okocraft.dailyrewards.listener;

import net.okocraft.dailyrewards.DailyRewards;
import net.okocraft.dailyrewards.config.Setting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

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
        plugin.getScheduler().schedule(
                () -> {
                    if (event.getPlayer().hasPermission(AUTO_RECEIVE_PERMISSION)) {
                        plugin.getProcessors().getPlayerReceiveProcessor().tryReceive(event.getPlayer());
                    }
                },
                plugin.getGeneralConfig().get(Setting.AUTO_RECEIVE_DELAY),
                TimeUnit.SECONDS
        );
    }
}
