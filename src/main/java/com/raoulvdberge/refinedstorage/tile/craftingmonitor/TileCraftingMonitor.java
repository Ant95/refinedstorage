package com.raoulvdberge.refinedstorage.tile.craftingmonitor;

import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.NetworkNodeCraftingMonitor;
import com.raoulvdberge.refinedstorage.tile.TileNode;
import com.raoulvdberge.refinedstorage.tile.data.ITileDataConsumer;
import com.raoulvdberge.refinedstorage.tile.data.ITileDataProducer;
import com.raoulvdberge.refinedstorage.tile.data.TileDataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileCraftingMonitor extends TileNode<NetworkNodeCraftingMonitor> {
    public static final TileDataParameter<Boolean> VIEW_AUTOMATED = new TileDataParameter<>(DataSerializers.BOOLEAN, true, new ITileDataProducer<Boolean, TileCraftingMonitor>() {
        @Override
        public Boolean getValue(TileCraftingMonitor tile) {
            return tile.getNode().canViewAutomated();
        }
    }, new ITileDataConsumer<Boolean, TileCraftingMonitor>() {
        @Override
        public void setValue(TileCraftingMonitor tile, Boolean value) {
            tile.getNode().setViewAutomated(value);
            tile.getNode().markDirty();

            INetwork network = tile.getNode().getNetwork();

            if (network != null) {
                network.sendCraftingMonitorUpdate();
            }
        }
    });

    public TileCraftingMonitor() {
        dataManager.addWatchedParameter(VIEW_AUTOMATED);
    }

    @Override
    @Nonnull
    public NetworkNodeCraftingMonitor createNode(World world, BlockPos pos) {
        return new NetworkNodeCraftingMonitor(world, pos);
    }

    @Override
    public String getNodeId() {
        return NetworkNodeCraftingMonitor.ID;
    }
}
