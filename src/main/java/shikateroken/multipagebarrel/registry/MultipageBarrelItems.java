package shikateroken.multipagebarrel.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MultipageBarrelItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("multipagebarrel");

    public static final Supplier<Item> MULTIPAGE_BARREL_ITEM = ITEMS.register("multipage_barrel",
            () -> new BlockItem(MultipageBarrelBlocks.MULTIPAGE_BARREL.get(), new Item.Properties()){
                @Override
                public boolean canFitInsideContainerItems() {
                    return false;
                }
            });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);

    }
}
