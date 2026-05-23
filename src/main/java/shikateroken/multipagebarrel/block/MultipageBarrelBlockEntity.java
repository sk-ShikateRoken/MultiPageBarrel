package shikateroken.multipagebarrel.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import shikateroken.multipagebarrel.Config.MultipageBarrelConfig;
import shikateroken.multipagebarrel.memu.MultipageBarrelMenu;
import shikateroken.multipagebarrel.registry.MultipageBarrelBEs;

import java.util.ArrayList;
import java.util.List;

public class MultipageBarrelBlockEntity extends BlockEntity implements MenuProvider {
    public int  slotsparpage = 104;
    private final ItemStackHandler itemHandler;
    public MultipageBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(MultipageBarrelBEs.MULTIPAGE_BARREL_BE.get(), pos, state);

        // Configからページ数を取得し、104スロットを掛ける
        int totalSlots = MultipageBarrelConfig.MAX_PAGES.get() * slotsparpage;

        this.itemHandler = new ItemStackHandler(totalSlots) {


            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }


    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", itemHandler.serializeNBT(registries));
    }



    @Override
    public Component getDisplayName() {
        return Component.translatable("block.multipagebarrel.multipage_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new MultipageBarrelMenu(id, playerInv, this.worldPosition);
    }
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("Inventory"));
        int configuredSize = MultipageBarrelConfig.MAX_PAGES.get() * slotsparpage;

        // 3. セーブデータのサイズと、現在のConfigのサイズが違う場合の処理
        if (itemHandler.getSlots() != configuredSize) {

            // 【退避】現在のアイテムを一時リストに保存する
            List<ItemStack> backup = new ArrayList<>();
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                backup.add(itemHandler.getStackInSlot(i));
            }

            // 【変更】サイズを現在のConfigに合わせる（この瞬間、itemHandlerの中身は空になります）
            itemHandler.setSize(configuredSize);

            // 【復元】退避していたアイテムを、新しいサイズに収まる範囲だけで戻す
            int copyCount = Math.min(backup.size(), configuredSize);
            for (int i = 0; i < copyCount; i++) {
                itemHandler.setStackInSlot(i, backup.get(i));
            }
            // ※ ページ数を減らしたことによって copyCount に収まらなかった（あふれた）アイテムは、
            // 新しいインベントリにセットされないため、ドロップせずそのまま消滅します。
    }
}
}
