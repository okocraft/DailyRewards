package net.okocraft.dailyrewards;

import com.github.siroshun09.configapi.api.util.ResourceUtils;
import com.github.siroshun09.mccommand.bukkit.BukkitCommandFactory;
import net.okocraft.dailyrewards.command.RewardCommand;
import net.okocraft.dailyrewards.config.GeneralConfig;
import net.okocraft.dailyrewards.config.RewardConfig;
import net.okocraft.dailyrewards.data.ReceiveData;
import net.okocraft.dailyrewards.lang.LanguageManager;
import net.okocraft.dailyrewards.lang.MessageBuilder;
import net.okocraft.dailyrewards.listener.PlayerJoinListener;
import net.okocraft.dailyrewards.processor.Processors;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

public class DailyRewards extends JavaPlugin {

    private GeneralConfig generalConfig;
    private LanguageManager languageManager;
    private MessageBuilder messageBuilder;
    private RewardConfig rewardConfig;
    private ReceiveData receiveData;
    private Processors processors;
    private ScheduledExecutorService scheduler;

    private PlayerJoinListener playerJoinListener;

    @Override
    public void onLoad() {
        generalConfig = new GeneralConfig(this);
        languageManager = new LanguageManager(this);
        messageBuilder = new MessageBuilder(this);
        rewardConfig = new RewardConfig(this);
        receiveData = new ReceiveData(this);

        try {
            // save japanese message file before loading languages
            var jpFilename = "ja_JP.properties";
            var jpFilepath = getDataFolder().toPath().resolve("lang").resolve(jpFilename);
            ResourceUtils.copyFromJarIfNotExists(getFile().toPath(), jpFilename, jpFilepath);
            languageManager.reload();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void onEnable() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        processors = new Processors(this);

        PluginCommand command = getCommand("reward");

        if (command != null) {
            RewardCommand rewardCommand = new RewardCommand(this);
            BukkitCommandFactory.registerAsync(command, rewardCommand);
        } else {
            getLogger().severe("Could not get /reward command, please report this to https://github.com/okocraft/DailyRewards/issues.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (generalConfig.isAutoReceiveEnabled()) {
            playerJoinListener = new PlayerJoinListener(this);
            playerJoinListener.start();
        }
    }

    @Override
    public void onDisable() {
        if (generalConfig.isAutoReceiveEnabled()) {
            playerJoinListener.shutdown();
        }

        HandlerList.unregisterAll(this);
        scheduler.shutdownNow();
        getServer().getScheduler().cancelTasks(this);

        try {
            receiveData.close();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not close receive data.", e);
        }
    }

    public GeneralConfig getGeneralConfig() {
        return generalConfig;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public MessageBuilder getMessageBuilder() {
        return messageBuilder;
    }

    public RewardConfig getRewardConfig() {
        return rewardConfig;
    }

    public ReceiveData getReceiveData() {
        return receiveData;
    }

    public Processors getProcessors() {
        return processors;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }
}
