package shikateroken.multipagebarrel.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import shikateroken.multipagebarrel.block.MultipageBarrelBlockEntity;

import java.util.function.Supplier;

import static shikateroken.multipagebarrel.Multipagebarrel.MODID;

public class MultipageBarrelBEs {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);

    public static final Supplier<BlockEntityType<MultipageBarrelBlockEntity>> MULTIPAGE_BARREL_BE = BLOCK_ENTITIES.register("multipage_barrel",
            () -> BlockEntityType.Builder.of(MultipageBarrelBlockEntity::new, MultipageBarrelBlocks.MULTIPAGE_BARREL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
