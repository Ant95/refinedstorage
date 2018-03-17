package com.raoulvdberge.refinedstorage.gui.control;

import com.raoulvdberge.refinedstorage.gui.GuiBase;
import com.raoulvdberge.refinedstorage.tile.craftingmonitor.ICraftingMonitor;
import net.minecraft.util.text.TextFormatting;

public class SideButtonCraftingMonitorViewAutomated extends SideButton {
    private ICraftingMonitor craftingMonitor;

    public SideButtonCraftingMonitorViewAutomated(GuiBase gui, ICraftingMonitor craftingMonitor) {
        super(gui);

        this.craftingMonitor = craftingMonitor;
    }

    @Override
    public String getTooltip() {
        return GuiBase.t("sidebutton.refinedstorage:crafting_monitor.view_automated") + "\n" + TextFormatting.GRAY + GuiBase.t("gui." + (craftingMonitor.canViewAutomated() ? "yes" : "no"));
    }

    @Override
    protected void drawButtonIcon(int x, int y) {
        gui.drawTexture(x, y, craftingMonitor.canViewAutomated() ? 0 : 16, 144, 16, 16);
    }

    @Override
    public void actionPerformed() {
        craftingMonitor.onViewAutomatedChanged(!craftingMonitor.canViewAutomated());
    }
}
