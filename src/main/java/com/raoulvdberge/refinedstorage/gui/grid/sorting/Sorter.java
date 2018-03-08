package com.raoulvdberge.refinedstorage.gui.grid.sorting;

import com.raoulvdberge.refinedstorage.api.network.grid.GridType;
import com.raoulvdberge.refinedstorage.api.network.grid.IGrid;
import com.raoulvdberge.refinedstorage.gui.grid.GuiGrid;
import com.raoulvdberge.refinedstorage.gui.grid.filtering.GridFilterParser;
import com.raoulvdberge.refinedstorage.gui.grid.stack.IGridStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class Sorter implements Runnable {
    private static final GridSorting SORTING_QUANTITY = new GridSortingQuantity();
    private static final GridSorting SORTING_NAME = new GridSortingName();
    private static final GridSorting SORTING_ID = new GridSortingID();
    private static final GridSorting SORTING_INVENTORYTWEAKS = new GridSortingInventoryTweaks();
    private static final GridSorting SORTING_LAST_MODIFIED = new GridSortingLastModified();

    private boolean busy;

    private GuiGrid gui;

    @Override
    public void run() {
        IGrid grid = gui.getGrid();

        List<IGridStack> stacks = new ArrayList<>();

        if (grid.isActive()) {
            stacks.addAll(grid.getType() == GridType.FLUID ? GuiGrid.FLUIDS.values() : GuiGrid.ITEMS.values());

            List<Predicate<IGridStack>> filters = GridFilterParser.getFilters(
                grid,
                gui.getSearchField() != null ? gui.getSearchField().getText() : "",
                (grid.getTabSelected() >= 0 && grid.getTabSelected() < grid.getTabs().size()) ? grid.getTabs().get(grid.getTabSelected()).getFilters() : grid.getFilters()
            );

            Iterator<IGridStack> t = stacks.iterator();

            while (t.hasNext()) {
                IGridStack stack = t.next();

                for (Predicate<IGridStack> filter : filters) {
                    if (!filter.test(stack)) {
                        t.remove();

                        break;
                    }
                }
            }

            SORTING_NAME.setSortingDirection(grid.getSortingDirection());
            SORTING_QUANTITY.setSortingDirection(grid.getSortingDirection());
            SORTING_ID.setSortingDirection(grid.getSortingDirection());
            SORTING_INVENTORYTWEAKS.setSortingDirection(grid.getSortingDirection());
            SORTING_LAST_MODIFIED.setSortingDirection(grid.getSortingDirection());

            stacks.sort(SORTING_NAME);

            if (grid.getSortingType() == IGrid.SORTING_TYPE_QUANTITY) {
                stacks.sort(SORTING_QUANTITY);
            } else if (grid.getSortingType() == IGrid.SORTING_TYPE_ID) {
                stacks.sort(SORTING_ID);
            } else if (grid.getSortingType() == IGrid.SORTING_TYPE_INVENTORYTWEAKS) {
                stacks.sort(SORTING_INVENTORYTWEAKS);
            } else if (grid.getSortingType() == IGrid.SORTING_TYPE_LAST_MODIFIED) {
                stacks.sort(SORTING_LAST_MODIFIED);
            }
        }

        GuiGrid.STACKS = stacks;

        if (gui.getScrollbar() != null) {
            gui.getScrollbar().setEnabled(gui.getRows() > gui.getVisibleRows());
            gui.getScrollbar().setMaxOffset(gui.getRows() - gui.getVisibleRows());
        }

        if (gui.getTabPageLeft() != null) {
            gui.getTabPageLeft().visible = grid.getTotalTabPages() > 0;
        }

        if (gui.getTabPageRight() != null) {
            gui.getTabPageRight().visible = grid.getTotalTabPages() > 0;
        }

        this.busy = false;
    }

    public void startIfPossible(GuiGrid gui) {
        if (!busy) {
            this.busy = true;
            this.gui = gui;

            new Thread(this, "RS grid sorting").start();
        }
    }
}
