package shikateroken.multipagebarrel.Client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import shikateroken.multipagebarrel.Multipagebarrel;
import shikateroken.multipagebarrel.registry.MultipageBarrelMenus;

@EventBusSubscriber(modid = Multipagebarrel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MultipageBarrelMenus.MULTIPAGE_BARREL_MENU.get(), MultipageBarrelScreen::new);
    }
}
