package shikateroken.multipagebarrel.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import shikateroken.multipagebarrel.Config.MultipageBarrelConfig;
import shikateroken.multipagebarrel.memu.MultipageBarrelMenu;
import shikateroken.multipagebarrel.registry.MultipageBarrelBEs;

public class MultipageBarrelBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler;
    public MultipageBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(MultipageBarrelBEs.MULTIPAGE_BARREL_BE.get(), pos, state);

        // Configからページ数を取得し、27スロットを掛ける
        int totalSlots = MultipageBarrelConfig.MAX_PAGES.get() * 27;

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

        // 【重要】Configのページ数を変更した後にワールドに入った時、
        // 古いデータと現在のConfigのサイズを一致させるための安全処理
        int configuredSize = MultipageBarrelConfig.MAX_PAGES.get() * 27;
        if (itemHandler.getSlots() != configuredSize) {
            itemHandler.setSize(configuredSize);
        }
    }
}
