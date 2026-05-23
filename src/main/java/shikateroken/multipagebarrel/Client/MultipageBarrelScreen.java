package shikateroken.multipagebarrel.Client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import shikateroken.multipagebarrel.memu.MultipageBarrelMenu;

public class MultipageBarrelScreen extends AbstractContainerScreen<MultipageBarrelMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_27.png"); // バニラのラージチェストUIを流用

    public MultipageBarrelScreen(MultipageBarrelMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageHeight = 167; // ラージチェストなどの高さに合わせる
    }

    @Override
    protected void init() {
        super.init();

        // ページ切り替えボタン
        this.addRenderableWidget(Button.builder(Component.literal("<"), button -> {
            if (this.menu.currentPage > 0) this.menu.currentPage--;
        }).bounds(this.leftPos - 20, this.topPos + 20, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), button -> {
            if (this.menu.currentPage < MultipageBarrelMenu.PAGES - 1) this.menu.currentPage++;
        }).bounds(this.leftPos + this.imageWidth, this.topPos + 20, 20, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        // ページ数の表示
        guiGraphics.drawString(this.font, "Page " + (this.menu.currentPage + 1), this.leftPos + 100, this.topPos + 6, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // 背景の描画 (generic_54は6行なので、3行用を使用するかテクスチャを自作する必要があります)
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
