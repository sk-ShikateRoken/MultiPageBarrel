package shikateroken.multipagebarrel.Client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import shikateroken.multipagebarrel.memu.MultipageBarrelMenu;

public class MultipageBarrelScreen extends AbstractContainerScreen<MultipageBarrelMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "multipagebarrel","textures/gui/multi_page_barrel_gui.png");

    public MultipageBarrelScreen(MultipageBarrelMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 248;
        this.imageHeight = 258;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        // ページ切り替えボタン
        this.addRenderableWidget(Button.builder(Component.literal("<"), button -> {
            if (this.menu.currentPage > 0) this.menu.currentPage--;
        }).bounds(this.leftPos + 15, this.topPos + 193, 20, 20).build()); // 高さを少し上に調整

        this.addRenderableWidget(Button.builder(Component.literal(">"), button -> {
            if (this.menu.currentPage < this.menu.totalPages - 1) this.menu.currentPage++;
        }).bounds(this.leftPos + 213, this.topPos + 193, 20, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        // ページ数の表示
        guiGraphics.drawString(this.font, (this.menu.currentPage + 1)+"/"+(this.menu.totalPages), this.leftPos + 8, this.topPos + 175, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // もし自作テクスチャのキャンバスサイズが 512x512 等になった場合は、
        // 単純な blit ではなく、テクスチャサイズを指定する blit メソッドを使用する必要があります。
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 512, 512);
    }
}
