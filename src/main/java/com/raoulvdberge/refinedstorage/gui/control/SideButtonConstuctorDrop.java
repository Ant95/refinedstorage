package com.raoulvdberge.refinedstorage.gui.control;

import com.raoulvdberge.refinedstorage.gui.GuiBase;
import com.raoulvdberge.refinedstorage.tile.TileConstructor;
import com.raoulvdberge.refinedstorage.tile.data.TileDataManager;
import net.minecraft.util.text.TextFormatting;

public class SideButtonConstuctorDrop extends SideButton {
    public SideButtonConstuctorDrop(GuiBase gui) {
        super(gui);
    }

    @Override
    protected void drawButtonIcon(int x, int y) {
        gui.drawTexture(x, y, 64 + (TileConstructor.DROP.getValue() ? 16 : 0), 16, 16, 16);
    }

    @Override
    public String getTooltip() {
        return GuiBase.t("sidebutton.refinedstorage:constructor.drop") + "\n" + TextFormatting.GRAY + GuiBase.t(TileConstructor.DROP.getValue() ? "gui.yes" : "gui.no");
    }

    @Override
    public void actionPerformed() {
        TileDataManager.setParameter(TileConstructor.DROP, !TileConstructor.DROP.getValue());
    }
}
