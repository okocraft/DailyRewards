package net.okocraft.dailyrewards;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.okocraft.dailyrewards.command.RewardCommand;
import net.okocraft.dailyrewards.config.GeneralConfig;
import net.okocraft.dailyrewards.config.RewardConfig;
import net.okocraft.dailyrewards.data.ReceiveData;
import net.okocraft.dailyrewards.lang.LanguageManager;
import net.okocraft.dailyrewards.lang.MessageBuilder;
import net.okocraft.dailyrewards.listener.PlayerJoinListener;
import net.okocraft.dailyrewards.processor.Processors;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class DailyRewards extends JavaPlugin {

    private GeneralConfig generalConfig;
    private LanguageManager languageManager;
    private MessageBuilder messageBuilder;
    private RewardConfig rewardConfig;
    private ReceiveData receiveData;
    private Processors processors;

    private PlayerJoinListener playerJoinListener;

    @Override
    public void onLoad() {
        generalConfig = new GeneralConfig(this);
        languageManager = new LanguageManager(this);
        messageBuilder = new MessageBuilder(this);
        rewardConfig = new RewardConfig(this);
        receiveData = new ReceiveData(this);

        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(LanguageManager.JA_JP_FILENAME)) {
            Objects.requireNonNull(in);

            // save japanese message file before loading languages
            Path jpFilepath = getDataFolder().toPath().resolve("lang").resolve(LanguageManager.JA_JP_FILENAME);
            Files.copy(in, jpFilepath);

            languageManager.reload();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void onEnable() {
        processors = new Processors(this);

        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> event.registrar().register("dailyrewards", List.of("dr"), new RewardCommand(this))
        );

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

        receiveData.save(this.getSLF4JLogger());
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

}
