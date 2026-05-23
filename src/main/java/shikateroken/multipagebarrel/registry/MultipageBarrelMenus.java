package shikateroken.multipagebarrel.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import shikateroken.multipagebarrel.memu.MultipageBarrelMenu;

import java.util.function.Supplier;

import static shikateroken.multipagebarrel.Multipagebarrel.MODID;

public class MultipageBarrelMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    public static final Supplier<MenuType<MultipageBarrelMenu>> MULTIPAGE_BARREL_MENU = MENUS.register("multipage_barrel",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MultipageBarrelMenu(windowId, inv, data.readBlockPos())));
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
