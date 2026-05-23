package shikateroken.multipagebarrel.memu;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import shikateroken.multipagebarrel.Config.MultipageBarrelConfig;
import shikateroken.multipagebarrel.block.MultipageBarrelBlockEntity;
import shikateroken.multipagebarrel.registry.MultipageBarrelMenus;

import java.util.function.Supplier;

public class MultipageBarrelMenu extends AbstractContainerMenu {
    public int currentPage = 0;
    public static final int PAGES = 2;
    private final boolean isClientSide;
    private final BlockPos blockPos;
    private final Level level;
    public final int totalPages;

    public MultipageBarrelMenu(int id, Inventory playerInv, BlockPos pos) {
        super(MultipageBarrelMenus.MULTIPAGE_BARREL_MENU.get(), id);
        this.isClientSide = playerInv.player.level().isClientSide();

        this.blockPos = pos;
        this.level = playerInv.player.level();
        //コンフィグからページ数を獲得
        this.totalPages = MultipageBarrelConfig.MAX_PAGES.get();
        int totalSlots;
        totalSlots = this.totalPages * 104;

        BlockEntity blockEntity = playerInv.player.level().getBlockEntity(pos);
        if (blockEntity instanceof MultipageBarrelBlockEntity barrel) {
            IItemHandler handler = barrel.getItemHandler();

            // 樽のインベントリ（全ページ分、すべて同じX,Y座標に配置する）
            for (int page = 0; page < this.totalPages; page++) {
                for (int row = 0; row < 8; ++row) {
                    for (int col = 0; col < 13; ++col) {
                        int index = (page * 104) + (row * 13) + col;
                        this.addSlot(new PagedSlot(handler, index, 8 + col * 18, 19 + row * 18, page, () -> this.currentPage, this.isClientSide));
                    }
                }
            }
        } else {
            // クライアント側でBEが取得できない場合のダミー
            IItemHandler dummy = new ItemStackHandler(totalSlots);
            for (int i = 0; i < totalSlots; i++) {
                this.addSlot(new PagedSlot(dummy, i, 8 + (i % 13) * 18, 19 + ((i % 104) / 13) * 18, i / 104, () -> this.currentPage, this.isClientSide));
            }
        }

        // プレイヤーのインベントリ
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 44 + col * 18, 176 + row * 18));
            }
        }
        // プレイヤーのホットバー
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, 44 + col * 18, 234));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Shiftクリックの処理 (簡略化)
        ItemStack itemstack = ItemStack.EMPTY;
        int totalSlots = this.totalPages * 104;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index < totalSlots) {
                if (!this.moveItemStackTo(slotStack, totalSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, totalSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // 距離チェックを入れるのが望ましい
    }

    // --- カスタムスロットクラス ---
    public static class PagedSlot extends SlotItemHandler {
        private final int pageIndex;
        private final Supplier<Integer> currentPage;
        private final boolean isClient;

        public PagedSlot(IItemHandler itemHandler, int index, int x, int y, int pageIndex, Supplier<Integer> currentPage, boolean isClient) {
            super(itemHandler, index, x, y);
            this.pageIndex = pageIndex;
            this.currentPage = currentPage;
            this.isClient = isClient;
        }

        @Override
        public boolean isActive() {
            // サーバーは常にアクティブとして扱い（クリック判定を通すため）、クライアントは現在のページのみ表示・クリック可能にする
            return !this.isClient || this.currentPage.get() == this.pageIndex;
        }
    }
    @Override
    public void removed(Player player) {
        super.removed(player);

        // サーバー側で閉じる音を鳴らす
        if (!this.isClientSide) {
            this.level.playSound(null, this.blockPos, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }
    }
}
