package shikateroken.multipagebarrel.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import shikateroken.multipagebarrel.block.MultipageBarrelBlock;

import java.util.function.Supplier;

public class MultipageBarrelBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("multipagebarrel");

    public static final Supplier<Block> MULTIPAGE_BARREL = BLOCKS.register("multipage_barrel",
            () -> new MultipageBarrelBlock(BlockBehaviour.Properties.of()
                    .strength(2.5f) // ブロックの硬さと爆破耐性（バニラの樽と同じくらい）
                    .sound(SoundType.WOOD) // 叩いた時や壊した時の音（木材）
                     ));

    // イベントバスに登録するためのメソッドを用意しておく
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
