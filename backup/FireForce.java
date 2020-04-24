
package burn447.dartcraftReloaded.blocks.fire;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class FireForce extends Block{

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UPPER = PropertyBool.create("up");
    private final Map<Block, Integer> encouragements = Maps.newIdentityHashMap();
    private final Map<Block, Integer> flammabilities = Maps.newIdentityHashMap();

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return !worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !canCatchFire(worldIn, pos.down(), EnumFacing.UP) ? state.withProperty(NORTH, this.canCatchFire(worldIn, pos.north(), EnumFacing.SOUTH)).withProperty(EAST, this.canCatchFire(worldIn, pos.east(), EnumFacing.WEST)).withProperty(SOUTH, this.canCatchFire(worldIn, pos.south(), EnumFacing.NORTH)).withProperty(WEST, this.canCatchFire(worldIn, pos.west(), EnumFacing.EAST)).withProperty(UPPER, this.canCatchFire(worldIn, pos.up(), EnumFacing.DOWN)) : this.getDefaultState();
    }

    protected FireForce() {
        super(Material.FIRE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UPPER, false));
        this.setTickRandomly(true);
    }


    public void setFireInfo(Block blockIn, int encouragement, int flammability) {
        if (blockIn == Blocks.AIR) {
            throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        } else {
            this.encouragements.put(blockIn, encouragement);
            this.flammabilities.put(blockIn, flammability);
        }
    }

    /** @deprecated */
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    /** @deprecated */
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /** @deprecated */
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public int quantityDropped(Random random) {
        return 0;
    }


    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.getGameRules().getBoolean("doFireTick")) {
            if (!worldIn.isAreaLoaded(pos, 2)) {
                return;
            }

            if (!this.canPlaceBlockAt(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            }

            Block block = worldIn.getBlockState(pos.down()).getBlock();
            boolean flag=true;
            //boolean flag = block.isFireSource(worldIn, pos.down(), EnumFacing.UP);
            int i = (Integer)state.getValue(AGE);
            if (!flag && rand.nextFloat() < 0.2F + (float)i * 0.03F) {
                worldIn.setBlockToAir(pos);
            } else {
                if (i < 15) {
                    state = state.withProperty(AGE, i + rand.nextInt(3) / 2);
                    worldIn.setBlockState(pos, state, 4);
                }

                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + rand.nextInt(10));
                if (!flag) {
                    if (!this.canNeighborCatchFire(worldIn, pos)) {
                        if ( i > 3) {
                            worldIn.setBlockToAir(pos);
                        }

                        return;
                    }

                    if (!this.canCatchFire(worldIn, pos.down(), EnumFacing.UP) && i == 15 && rand.nextInt(4) == 0) {
                        worldIn.setBlockToAir(pos);
                        return;
                    }
                }

                boolean flag1 = worldIn.isBlockinHighHumidity(pos);
                int j = 0;
                if (flag1) {
                    j = -50;
                }

                this.tryCatchFire(worldIn, pos.east(), 300 + j, rand, i, EnumFacing.WEST);
                this.tryCatchFire(worldIn, pos.west(), 300 + j, rand, i, EnumFacing.EAST);
                this.tryCatchFire(worldIn, pos.down(), 250 + j, rand, i, EnumFacing.UP);
                this.tryCatchFire(worldIn, pos.up(), 250 + j, rand, i, EnumFacing.DOWN);
                this.tryCatchFire(worldIn, pos.north(), 300 + j, rand, i, EnumFacing.SOUTH);
                this.tryCatchFire(worldIn, pos.south(), 300 + j, rand, i, EnumFacing.NORTH);

                for(int k = -1; k <= 1; ++k) {
                    for(int l = -1; l <= 1; ++l) {
                        for(int i1 = -1; i1 <= 4; ++i1) {
                            if (k != 0 || i1 != 0 || l != 0) {
                                int j1 = 100;
                                if (i1 > 1) {
                                    j1 += (i1 - 1) * 100;
                                }

                                BlockPos blockpos = pos.add(k, i1, l);
                                int k1 = this.getNeighborEncouragement(worldIn, blockpos);
                                if (k1 > 0) {
                                    int l1 = (k1 + 40 + worldIn.getDifficulty().getId() * 7) / (i + 30);
                                    if (flag1) {
                                        l1 /= 2;
                                    }

                                    if (l1 > 0 && rand.nextInt(j1) <= l1) {
                                        int i2 = i + rand.nextInt(5) / 4;
                                        if (i2 > 15) {
                                            i2 = 15;
                                        }

                                        worldIn.setBlockState(blockpos, state.withProperty(AGE, i2), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
/*
    protected boolean canDie(World worldIn, BlockPos pos) {
        return worldIn.isRainingAt(pos) || worldIn.isRainingAt(pos.west()) || worldIn.isRainingAt(pos.east()) || worldIn.isRainingAt(pos.north()) || worldIn.isRainingAt(pos.south());
    }
    */
    public boolean requiresUpdates() {
        return false;
    }

    public int getFlammability(Block blockIn) {
        Integer integer = (Integer)this.flammabilities.get(blockIn);
        return integer == null ? 0 : integer;
    }

    public int getEncouragement(Block blockIn) {
        Integer integer = (Integer)this.encouragements.get(blockIn);
        return integer == null ? 0 : integer;
    }


    private void tryCatchFire(World worldIn, BlockPos pos, int chance, Random random, int age, EnumFacing face) {
        int i = this.flammabilities.get(worldIn.getBlockState(pos).getBlock());
        if (random.nextInt(chance) < i) {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            if (random.nextInt(age + 10) < 5 && !worldIn.isRainingAt(pos)) {
                int j = age + random.nextInt(5) / 4;
                if (j > 15) {
                    j = 15;
                }

                worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, j), 3);
            } else {
                worldIn.setBlockToAir(pos);
            }
            /*
            if (iblockstate.getBlock() == Blocks.TNT) {
                Blocks.TNT.onPlayerDestroy(worldIn, pos, iblockstate.withProperty(BlockTNT.EXPLODE, true));
            }
            */
        }

    }

    private boolean canNeighborCatchFire(World worldIn, BlockPos pos) {
        EnumFacing[] var3 = EnumFacing.values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            EnumFacing enumfacing = var3[var5];
            if (this.canCatchFire(worldIn, pos.offset(enumfacing), enumfacing.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private int getNeighborEncouragement(World worldIn, BlockPos pos) {
        if (!worldIn.isAirBlock(pos)) {
            return 0;
        } else {
            int i = 0;
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                EnumFacing enumfacing = var4[var6];
                i = Math.max(this.encouragements.get(worldIn.getBlockState(pos.offset(enumfacing)).getBlock()), i);
            }

            return i;
        }
    }

    public boolean isCollidable() {
        return false;
    }

    /** @deprecated */
    @Deprecated
    public boolean canCatchFire(IBlockAccess worldIn, BlockPos pos) {
        return this.canCatchFire(worldIn, pos, EnumFacing.UP);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isTopSolid() || this.canNeighborCatchFire(worldIn, pos);
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.getBlockState(pos.down()).isTopSolid() && !this.canNeighborCatchFire(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }

    }
    /*
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.provider.getDimensionType().getId() > 0 || !Blocks.PORTAL.trySpawnPortal(worldIn, pos)) {
            if (!worldIn.getBlockState(pos.down()).isTopSolid() && !this.canNeighborCatchFire(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            } else {
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
            }
        }

    }
    */

    /** @deprecated */
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.TNT;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(24) == 0) {
            worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }

        int j1;
        double d7;
        double d12;
        double d17;
        if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !canCatchFire(worldIn, pos.down(), EnumFacing.UP)) {
            if (canCatchFire(worldIn, pos.west(), EnumFacing.EAST)) {
                for(j1 = 0; j1 < 2; ++j1) {
                    d7 = (double)pos.getX() + rand.nextDouble() * 0.10000000149011612D;
                    d12 = (double)pos.getY() + rand.nextDouble();
                    d17 = (double)pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (canCatchFire(worldIn, pos.east(), EnumFacing.WEST)) {
                for(j1 = 0; j1 < 2; ++j1) {
                    d7 = (double)(pos.getX() + 1) - rand.nextDouble() * 0.10000000149011612D;
                    d12 = (double)pos.getY() + rand.nextDouble();
                    d17 = (double)pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (canCatchFire(worldIn, pos.north(), EnumFacing.SOUTH)) {
                for(j1 = 0; j1 < 2; ++j1) {
                    d7 = (double)pos.getX() + rand.nextDouble();
                    d12 = (double)pos.getY() + rand.nextDouble();
                    d17 = (double)pos.getZ() + rand.nextDouble() * 0.10000000149011612D;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (canCatchFire(worldIn, pos.south(), EnumFacing.NORTH)) {
                for(j1 = 0; j1 < 2; ++j1) {
                    d7 = (double)pos.getX() + rand.nextDouble();
                    d12 = (double)pos.getY() + rand.nextDouble();
                    d17 = (double)(pos.getZ() + 1) - rand.nextDouble() * 0.10000000149011612D;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (canCatchFire(worldIn, pos.up(), EnumFacing.DOWN)) {
                for(j1 = 0; j1 < 2; ++j1) {
                    d7 = (double)pos.getX() + rand.nextDouble();
                    d12 = (double)(pos.getY() + 1) - rand.nextDouble() * 0.10000000149011612D;
                    d17 = (double)pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }
        } else {
            for(j1 = 0; j1 < 3; ++j1) {
                d7 = (double)pos.getX() + rand.nextDouble();
                d12 = (double)pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
                d17 = (double)pos.getZ() + rand.nextDouble();
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }

    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, meta);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue(AGE);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{AGE, NORTH, EAST, SOUTH, WEST, UPPER});
    }

    public boolean canCatchFire(IBlockAccess world, BlockPos pos, EnumFacing face) {
     return getFlammability(world.getBlockState(pos).getBlock())>0;
    }

    /** @deprecated */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
