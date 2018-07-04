package com.raoulvdberge.refinedstorage.block;

import com.raoulvdberge.refinedstorage.RSGui;
import com.raoulvdberge.refinedstorage.tile.TileConstructor;
import com.raoulvdberge.refinedstorage.util.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockConstructor extends BlockCable {
    public static final AxisAlignedBB HEAD_NORTH_AABB = RenderUtils.getBounds(0, 0, 0, 16, 16, 2);
    public static final AxisAlignedBB HEAD_EAST_AABB = RenderUtils.getBounds(14, 0, 0, 16, 16, 16);
    public static final AxisAlignedBB HEAD_SOUTH_AABB = RenderUtils.getBounds(0, 0, 14, 16, 16, 16);
    public static final AxisAlignedBB HEAD_WEST_AABB = RenderUtils.getBounds(0, 0, 0, 2, 16, 16);
    public static final AxisAlignedBB HEAD_DOWN_AABB = RenderUtils.getBounds(0, 0, 0, 16, 2, 16);
    public static final AxisAlignedBB HEAD_UP_AABB = RenderUtils.getBounds(0, 14, 0, 16, 16, 16);

    public BlockConstructor() {
        super("constructor");
    }

    @Override
    public List<AxisAlignedBB> getCollisionBoxes(TileEntity tile, IBlockState state) {
        List<AxisAlignedBB> boxes = new ArrayList<>();

        switch (state.getValue(getDirection().getProperty())) {
            case NORTH:
                boxes.add(HOLDER_NORTH_AABB);
                boxes.add(HEAD_NORTH_AABB);
                break;
            case EAST:
                boxes.add(HOLDER_EAST_AABB);
                boxes.add(HEAD_EAST_AABB);
                break;
            case SOUTH:
                boxes.add(HOLDER_SOUTH_AABB);
                boxes.add(HEAD_SOUTH_AABB);
                break;
            case WEST:
                boxes.add(HOLDER_WEST_AABB);
                boxes.add(HEAD_WEST_AABB);
                break;
            case UP:
                boxes.add(HOLDER_UP_AABB);
                boxes.add(HEAD_UP_AABB);
                break;
            case DOWN:
                boxes.add(HOLDER_DOWN_AABB);
                boxes.add(HEAD_DOWN_AABB);
                break;
        }

        return boxes;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileConstructor();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hitCablePart(state, world, pos, hitX, hitY, hitZ)) {
            return false;
        }

        if (!world.isRemote) {
            tryOpenNetworkGui(RSGui.CONSTRUCTOR, player, world, pos, side);
        }

        return true;
    }

    @Override
    public boolean hasConnectivityState() {
        return true;
    }

    @Override
    @Nullable
    public Direction getDirection() {
        return Direction.ANY;
    }
}
