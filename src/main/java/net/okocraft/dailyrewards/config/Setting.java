package net.okocraft.dailyrewards.config;

import com.github.siroshun09.configapi.common.configurable.BooleanValue;
import com.github.siroshun09.configapi.common.configurable.FloatValue;
import com.github.siroshun09.configapi.common.configurable.LongValue;
import com.github.siroshun09.configapi.common.configurable.StringList;
import com.github.siroshun09.configapi.common.configurable.StringValue;

import java.util.Collections;

import static com.github.siroshun09.configapi.common.configurable.Configurable.create;
import static com.github.siroshun09.configapi.common.configurable.Configurable.createStringList;

public final class Setting {

    public static final BooleanValue ENABLE_AUTO_RECEIVE = create("auto-receive.enable", true);
    public static final LongValue AUTO_RECEIVE_DELAY = create("auto-receive.delay", 3L);
    public static final StringList DISABLED_WORLDS = createStringList("disabled-worlds", Collections.emptyList());
    public static final BooleanValue ENABLE_SOUND = create("sound.enable", true);
    public static final StringValue SOUND_RECEIVE = create("sound.receive.sound", "ENTITY_PLAYER_LEVELUP");
    public static final FloatValue SOUND_RECEIVE_VOLUME = create("sound.receive.volume", 100f);
    public static final FloatValue SOUND_RECEIVE_PITCH = create("sound.receive.pitch", 1.0f);
    public static final StringValue SOUND_CANNOT_RECEIVE = create("sound.cannot-receive.sound", "BLOCK_ANVIL_PLACE");
    public static final FloatValue SOUND_CANNOT_RECEIVE_VOLUME = create("sound.cannot-receive.volume", 100f);
    public static final FloatValue SOUND_CANNOT_RECEIVE_PITCH = create("sound.cannot-receive.pitch", 1.0f);
}
