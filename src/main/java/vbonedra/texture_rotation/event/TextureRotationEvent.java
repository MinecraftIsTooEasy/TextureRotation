package vbonedra.texture_rotation.event;

import vbonedra.texture_rotation.TextureRotationMod;
import com.google.common.eventbus.Subscribe;
import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.event.listener.IInitializationListener;
import net.minecraft.Minecraft;
import net.xiaoyu233.fml.reload.event.MITEEvents;
import net.xiaoyu233.fml.reload.event.SoundsRegisterEvent;

public class TextureRotationEvent extends Handlers {
//    @Subscribe public void onSoundsRegister(SoundsRegisterEvent event) {}

    public static void register() {
        MITEEvents.MITE_EVENT_BUS.register(new TextureRotationEvent());
    }
}
