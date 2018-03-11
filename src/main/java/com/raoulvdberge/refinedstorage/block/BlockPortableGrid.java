package com.raoulvdberge.refinedstorage.block;

import com.raoulvdberge.refinedstorage.RS;
import com.raoulvdberge.refinedstorage.RSGui;
import com.raoulvdberge.refinedstorage.item.ItemBlockPortableGrid;
import com.raoulvdberge.refinedstorage.tile.grid.portable.TilePortableGrid;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPortableGrid extends BlockBase {
    private static final AxisAlignedBB PORTABLE_GRID_AABB = new AxisAlignedBB(0, 0, 0, 1, 13.2F / 16F, 1);

    public static final PropertyEnum TYPE = PropertyEnum.create("type", PortableGridType.class);
    public static final PropertyEnum DISK_STATE = PropertyEnum.create("disk_state", PortableGridDiskState.class);
    public static final PropertyBool CONNECTED = PropertyBool.create("connected");

    public BlockPortableGrid() {
        super("portable_grid");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePortableGrid();
    }

    @Override
    public Item createItem() {
        return new ItemBlockPortableGrid();
    }

    @Override
    @Nullable
    public Direction getDirection() {
        return Direction.HORIZONTAL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return PORTABLE_GRID_AABB;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (!world.isRemote) {
            ((TilePortableGrid) world.getTileEntity(pos)).onPassItemContext(stack);
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(((TilePortableGrid) world.getTileEntity(pos)).getAsItem());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TilePortableGrid portableGrid = (TilePortableGrid) world.getTileEntity(pos);

        return super.getActualState(state, world, pos)
            .withProperty(DISK_STATE, portableGrid.getDiskState())
            .withProperty(CONNECTED, portableGrid.isConnected());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return createBlockStateBuilder()
            .add(TYPE)
            .add(DISK_STATE)
            .add(CONNECTED)
            .build();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, meta == 0 ? PortableGridType.NORMAL : PortableGridType.CREATIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE) == PortableGridType.NORMAL ? 0 : 1;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(RS.INSTANCE, RSGui.PORTABLE_GRID, world, pos.getX(), pos.getY(), pos.getZ());

            ((TilePortableGrid) world.getTileEntity(pos)).onOpened();
        }

        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
