package org.cyclops.integrateddynamics.core.client.gui.subgui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.api.client.gui.subgui.ISubGuiBox;

import java.util.List;

/**
 * A sub gui that simply renders a box.
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public abstract class SubGuiBox extends AbstractGui implements ISubGuiBox {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID,
            IntegratedDynamics._instance.getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI) + "sub_gui.png");

    private final Box type;

    protected List<Button> buttonList = Lists.newArrayList();
    protected final SubGuiHolder subGuiHolder = new SubGuiHolder();

    public SubGuiBox(Box type) {
        this.type = type;
    }

    @Override
    public void init(int guiLeft, int guiTop) {
        buttonList.clear();
        subGuiHolder.init(guiLeft, guiTop);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < this.buttonList.size(); ++i) {
            this.buttonList.get(i).render(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(int guiLeft, int guiTop, TextureManager textureManager, FontRenderer fontRenderer, float partialTicks, int mouseX, int mouseY) {
        textureManager.bindTexture(TEXTURE);
        GlStateManager.color3f(1, 1, 1);

        int textureWidth = 19;
        int textureHeight = textureWidth;

        int x = guiLeft + getX();
        int y = guiTop + getY();
        int width = getWidth();
        int height = getHeight();
        int tx = type.getX();
        int ty = type.getY();

        // Corners
        this.blit(x, y, tx, tx, 1, 1); // top left
        this.blit(x + width - 1, y, tx + textureWidth - 1, ty, 1, 1); // top right
        this.blit(x, y + height - 1, 0, tx + textureHeight - 1, ty + 1, 1); // bottom left
        this.blit(x + width - 1, y + height - 1, tx + textureWidth - 1, ty + textureHeight - 1, 1, 1); // bottom right

        int i, j;

        // Sides
        i = 1;
        while(i < width - 1) {
            int currentWidth = Math.max(1, Math.min(width - i, textureWidth - 2) - 1);
            this.blit(x + i, y, tx + 1, ty, currentWidth, 1);
            this.blit(x + i, y + height - 1, tx + 1, ty + textureHeight - 1, currentWidth, 1);
            i += currentWidth;
        }

        i = 1;
        while(i < height - 1) {
            int currentHeight = Math.max(1, Math.min(height - i, textureHeight - 2) - 1);
            this.blit(x, y + i, tx, ty + 1, 1, currentHeight);
            this.blit(x + width - 1, y + i, tx + textureWidth - 1, ty + 1, 1, currentHeight);
            i += currentHeight;
        }

        // Center
        i = 1;
        while(i < width - 1) {
            int currentWidth = Math.max(1, Math.min(width - i, textureWidth - 2) - 1);
            j = 1;
            while (j < height - 1) {
                int currentHeight = Math.max(1, Math.min(height - j, textureHeight - 2) - 1);
                this.blit(x + i, y + j, tx + 1, ty + 1, currentWidth, currentHeight);
                j += currentHeight;
            }
            i += currentWidth;
        }

        // Draw buttons
        drawScreen(mouseX, mouseY, partialTicks);

        subGuiHolder.drawGuiContainerBackgroundLayer(guiLeft, guiTop, textureManager, fontRenderer, partialTicks, mouseX, mouseY);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int guiLeft, int guiTop, TextureManager textureManager, FontRenderer fontRenderer, int mouseX, int mouseY) {
        subGuiHolder.drawGuiContainerForegroundLayer(guiLeft, guiTop, textureManager, fontRenderer, mouseX, mouseY);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        return subGuiHolder.charTyped(typedChar, keyCode);
    }

    @Override
    public boolean keyPressed(int typedChar, int keyCode, int modifiers) {
        return subGuiHolder.keyPressed(typedChar, keyCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        subGuiHolder.mouseClicked(mouseX, mouseY, mouseButton);
        for (int i = 0; i < this.buttonList.size(); ++i) {
            Button guibutton = this.buttonList.get(i);
            if (guibutton.mouseClicked(mouseX, mouseY, mouseButton)) {
                guibutton.playDownSound(Minecraft.getInstance().getSoundHandler());
                this.actionPerformed(guibutton);
                return true;
            }
        }
        return false;
    }

    protected void actionPerformed(Button guibutton) {

    }

    public static enum Box {

        LIGHT(0, 0),
        DARK(0, 19);

        private final int x, y;

        private Box(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

    }

    @EqualsAndHashCode(callSuper = false)
    @Data
    public static class Base extends SubGuiBox {

        private final int x, y, width, height;

        public Base(Box type, int x, int y, int width, int height) {
            super(type);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public void init(int guiLeft, int guiTop) {

        }

        @Override
        public void tick() {

        }

        @Override
        public boolean charTyped(char typedChar, int keyCode) {
            return false;
        }

        @Override
        public boolean keyPressed(int typedChar, int keyCode, int modifiers) {
            return false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }

    }

}
