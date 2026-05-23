package shikateroken.multipagebarrel.registry;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import shikateroken.multipagebarrel.block.MultipageBarrelBlockEntity;

@EventBusSubscriber(modid = "multipagebarrel", bus = EventBusSubscriber.Bus.MOD)
public class MultipageBarrelCapabilities {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // ブロックエンティティに対して、アイテムハンドラー(IItemHandler)を公開する
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,                     // 公開する機能（アイテムのやり取り）
                MultipageBarrelBEs.MULTIPAGE_BARREL_BE.get(), // どのブロックエンティティか
                (blockEntity, side) -> {
                    // ホッパーやパイプがアクセスしてきた時に、インベントリの中身を渡す
                    if (blockEntity instanceof MultipageBarrelBlockEntity barrel) {
                        return barrel.getItemHandler();
                    }
                    return null;
                }
        );
    }
}