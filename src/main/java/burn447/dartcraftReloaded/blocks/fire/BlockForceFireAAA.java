package burn447.dartcraftReloaded.blocks.fire;

import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import burn447.dartcraftReloaded.dartcraftReloaded;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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

public class BlockForceFireAAA extends Block
{
	protected static final AxisAlignedBB REED_AABB = new AxisAlignedBB(0 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 16 / 16D);
	
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool UPPER = PropertyBool.create("up");
	
	private final Map<Block, Block> SMELT_MAP = Maps.<Block, Block>newIdentityHashMap();
	
	protected String name;
	    
	    /**
	     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
	     * metadata, such as fence connections.
	     */
	    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	    {
	        if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !Blocks.FIRE.canCatchFire(worldIn, pos.down(), EnumFacing.UP))
	        {
	            return state.withProperty(NORTH, this.A_canCatchFire(worldIn, pos.north(), EnumFacing.SOUTH))
	                        .withProperty(EAST,  this.A_canCatchFire(worldIn, pos.east(), EnumFacing.WEST))
	                        .withProperty(SOUTH, this.A_canCatchFire(worldIn, pos.south(), EnumFacing.NORTH))
	                        .withProperty(WEST,  this.A_canCatchFire(worldIn, pos.west(), EnumFacing.EAST))
	                        .withProperty(UPPER, this.A_canCatchFire(worldIn, pos.up(), EnumFacing.DOWN));
	        }
	        return this.getDefaultState();
	    }

	    public BlockForceFireAAA(String name)
	    {
	    	super(Material.FIRE);
	        
	        this.setRegistryName(name);
			this.setTranslationKey(name);
			this.name = name;
			
			
			this.setHardness(0.0F);
			this.setLightLevel(1.0F);
			this.setSoundType(SoundType.CLOTH);
			this.disableStats();


	        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(UPPER, Boolean.valueOf(false)));
	        this.setTickRandomly(true);
	        
	        this.init();
	    }

	    
	    /*===============================================================================*/
	    public void registerItemModel(Item itemBlock)
	    {
			dartcraftReloaded.proxy.registerItemRenderer(itemBlock, 0, name);
		}

		public Item createItemBlock()
		{
			return new ItemBlock(this).setRegistryName(getRegistryName());
		}
		/*===============================================================================*/


		public void init()
		{
			setFireInfo(Blocks.GRASS, Blocks.DIRT);
			setFireInfo(Blocks.DIRT, Blocks.NETHERRACK);
			setFireInfo(Blocks.STONE, Blocks.NETHERRACK);
			setFireInfo(Blocks.REDSTONE_BLOCK, Blocks.GLOWSTONE);
			
			setFireInfo(Blocks.WOOL, Blocks.DIAMOND_BLOCK);
		}


		public void setFireInfo(Block blockIn, Block blockOut)
		{
			if (blockIn == Blocks.AIR)
				throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
			this.SMELT_MAP.put(blockIn, blockOut);
		}
		
		
		@Override
	    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	    {
	        return REED_AABB;
	    }
		

	    /**
	     * @deprecated call via {@link IBlockState#getCollisionBoundingBox(IBlockAccess,BlockPos)} whenever possible.
	     * Implementing/overriding is fine.
	     */
	    @Nullable
	    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	    {
	        return NULL_AABB;
	    }
	    
	    
	    /**
	     * Returns if this block is collidable. Only used by fire, although stairs return that of the block that the stair
	     * is made of (though nobody's going to make fire stairs, right?)
	     */
	    /*
	    public boolean isCollidable()
	    {
	        return false;
	    }
	    */
	    
	    

	    /**
	     * Used to determine ambient occlusion and culling when rebuilding chunks for render
	     * @deprecated call via {@link IBlockState#isOpaqueCube()} whenever possible. Implementing/overriding is fine.
	     */
	    public boolean isOpaqueCube(IBlockState state)
	    {
	        return false;
	    }

	    /**
	     * @deprecated call via {@link IBlockState#isFullCube()} whenever possible. Implementing/overriding is fine.
	     */
	    public boolean isFullCube(IBlockState state)
	    {
	        return false;
	    }

	    /**
	     * Returns the quantity of items to drop on block destruction.
	     */
	    public int quantityDropped(Random random)
	    {
	        return 0;
	    }

	    /**
	     * How many world ticks before ticking
	     */
	    public int tickRate(World worldIn)
	    {
	        return 30;
	    }
	    
	    /*===============================================================================*/

	    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	    {
	    	/*
	    	this.tryCatchFireBlock(worldIn, pos.east());
            this.tryCatchFireBlock(worldIn, pos.west());
            this.tryCatchFireBlock(worldIn, pos.down());
            this.tryCatchFireBlock(worldIn, pos.up());
            this.tryCatchFireBlock(worldIn, pos.north());
            this.tryCatchFireBlock(worldIn, pos.south());
	    	*/
	    	//6个面

	    	

	    	if (worldIn.getGameRules().getBoolean("doFireTick"))
	    	{
	            if (!worldIn.isAreaLoaded(pos, 2)) return; // Forge: prevent loading unloaded chunks when spreading fire
	            if (!this.canPlaceBlockAt(worldIn, pos))
	            {
	                worldIn.setBlockToAir(pos);
	            }

	            Block block = worldIn.getBlockState(pos.down()).getBlock();
	            boolean flag = block.isFireSource(worldIn, pos.down(), EnumFacing.UP);

	            int i = ((Integer)state.getValue(AGE)).intValue();

	            if (!flag && worldIn.isRaining() && this.canDie(worldIn, pos) && rand.nextFloat() < 0.2F + (float)i * 0.03F)
	            {
	                worldIn.setBlockToAir(pos);
	            }
	            else
	            {
	                if (i < 15)
	                {
	                    state = state.withProperty(AGE, Integer.valueOf(i + rand.nextInt(3) / 2));
	                    worldIn.setBlockState(pos, state, 4);
	                }

	                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + rand.nextInt(10));

	                if (!flag)
	                {
	                    if (!this.canNeighborCatchFire(worldIn, pos))
	                    {
	                        if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) || i > 3)
	                        {
	                            worldIn.setBlockToAir(pos);
	                        }

	                        return;
	                    }

	                    if (!this.A_canCatchFire(worldIn, pos.down(), EnumFacing.UP) && i == 15 && rand.nextInt(4) == 0)
	                    {
	                        worldIn.setBlockToAir(pos);
	                        return;
	                    }
	                }

	                boolean flag1 = worldIn.isBlockinHighHumidity(pos);
	                int j = 0;

	                if (flag1)
	                {
	                    j = -50;
	                }

	                /*
	                this.tryCatchFire(worldIn, pos.east(), 300 + j, rand, i, EnumFacing.WEST);
	                this.tryCatchFire(worldIn, pos.west(), 300 + j, rand, i, EnumFacing.EAST);
	                this.tryCatchFire(worldIn, pos.down(), 250 + j, rand, i, EnumFacing.UP);
	                this.tryCatchFire(worldIn, pos.up(), 250 + j, rand, i, EnumFacing.DOWN);
	                this.tryCatchFire(worldIn, pos.north(), 300 + j, rand, i, EnumFacing.SOUTH);
	                this.tryCatchFire(worldIn, pos.south(), 300 + j, rand, i, EnumFacing.NORTH);
	                */
	                
	                
	                this.tryCatchFireBlock(worldIn, pos.east());
	                this.tryCatchFireBlock(worldIn, pos.west());
	                this.tryCatchFireBlock(worldIn, pos.down());
	                this.tryCatchFireBlock(worldIn, pos.up());
	                this.tryCatchFireBlock(worldIn, pos.north());
	                this.tryCatchFireBlock(worldIn, pos.south());


	                
	                for (int k = -1; k <= 1; ++k)
	                {
	                    for (int l = -1; l <= 1; ++l)
	                    {
	                        for (int i1 = -1; i1 <= 4; ++i1)
	                        {
	                            if (k != 0 || i1 != 0 || l != 0)
	                            {
	                                int j1 = 100;

	                                if (i1 > 1)
	                                {
	                                    j1 += (i1 - 1) * 100;
	                                }

	                                BlockPos blockpos = pos.add(k, i1, l);
	                                
	                                if (checkBlock(worldIn, blockpos))
	                                {
	                                    int l1 = ( 40 + worldIn.getDifficulty().getId() * 7) / (i + 30);

	                                    if (flag1)
	                                    {
	                                        l1 /= 2;
	                                    }

	                                    if (l1 > 0 && rand.nextInt(j1) <= l1 && (!worldIn.isRaining() || !this.canDie(worldIn, blockpos)))
	                                    {
	                                        int i2 = i + rand.nextInt(5) / 4;

	                                        if (i2 > 15)
	                                        {
	                                            i2 = 15;
	                                        }
	                                        
	                                        System.out.println("随便生成火999");

	                                        worldIn.setBlockState(blockpos, state.withProperty(AGE, Integer.valueOf(i2)), 3);
	                                    }
	                                }
	                                
	                                
	                                //int k1 = this.getNeighborEncouragement(worldIn, blockpos);
	                                
	                                //k1 +
	                                
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
	    
	    
	    /*===============================================================================*/
	    
	    
	    

	    protected boolean canDie(World worldIn, BlockPos pos)
	    {
	        return worldIn.isRainingAt(pos) || worldIn.isRainingAt(pos.west()) || worldIn.isRainingAt(pos.east()) || worldIn.isRainingAt(pos.north()) || worldIn.isRainingAt(pos.south());
	    }

	    public boolean requiresUpdates()
	    {
	        return false;
	    }


		    
	    public boolean checkBlock(IBlockAccess worldIn, BlockPos pos)
		{
			return this.SMELT_MAP.get(worldIn.getBlockState(pos).getBlock()) != null;
		}

	    
	    
	    
	    
	    private void tryCatchFireBlock(World worldIn, BlockPos pos)
	    {
	    	
	        if (checkBlock(worldIn, pos))
	        {
	        	Block obj = worldIn.getBlockState(pos).getBlock();
	    		//System.out.println("现在冶炼块我们有没有冶炼" + obj);
	    		if (this.SMELT_MAP.get(obj) == null)
	    			return;
	    		worldIn.setBlockState(pos, this.SMELT_MAP.get(obj).getDefaultState());
	    		return;
	        }
	    }


	    private boolean canNeighborCatchFire(World worldIn, BlockPos pos)
	    {
	        for (EnumFacing enumfacing : EnumFacing.values())
	        {
	            if (this.A_canCatchFire(worldIn, pos.offset(enumfacing), enumfacing.getOpposite()))
	            {
	                return true;
	            }
	        }

	        return false;
	    }

	    private int getNeighborEncouragement(World worldIn, BlockPos pos)
	    {
	        if (!worldIn.isAirBlock(pos))
	        {
	            return 0;
	        }
	        else
	        {
	            int i = 0;

	            for (EnumFacing enumfacing : EnumFacing.values())
	            {
	                i = Math.max(worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getFireSpreadSpeed(worldIn, pos.offset(enumfacing), enumfacing.getOpposite()), i);
	            }

	            return i;
	        }
	    }

	  
	    
	    /**
	     * Checks if the block can be caught on fire
	     */
	    @Deprecated // Use canCatchFire with face sensitive version below
	    public boolean canCatchFire(IBlockAccess worldIn, BlockPos pos)
	    {
	        return A_canCatchFire(worldIn, pos, EnumFacing.UP);
	    }

	    /**
	     * Checks if this block can be placed exactly at the given position.
	     */
	    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	    {
	        return worldIn.getBlockState(pos.down()).isTopSolid() || this.canNeighborCatchFire(worldIn, pos);
	    }

	    /**
	     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	     * block, etc.
	     */
	    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	    {
	        if (!worldIn.getBlockState(pos.down()).isTopSolid() && !this.canNeighborCatchFire(worldIn, pos))
	        {
	            worldIn.setBlockToAir(pos);
	        }
	    }

	    /**
	     * Called after the block is set in the Chunk data, but before the Tile Entity is set
	     */
	    
	    /*
	    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	    {
	        if (worldIn.provider.getDimensionType().getId() > 0 || !Blocks.PORTAL.trySpawnPortal(worldIn, pos))
	        {
	            if (!worldIn.getBlockState(pos.down()).isTopSolid() && !this.canNeighborCatchFire(worldIn, pos))
	            {
	                worldIn.setBlockToAir(pos);
	            }
	            else
	            {
	                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
	            }
	        }
	    }
	    */

	    /**
	     * Get the MapColor for this Block and the given BlockState
	     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
	     * Implementing/overriding is fine.
	     */
	    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	    {
	        return MapColor.TNT;
	    }

	    /**
	     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
	     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
	     * of whether the block can receive random update ticks
	     */
	    @SideOnly(Side.CLIENT)
	    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	    {
	        if (rand.nextInt(24) == 0)
	        {
	            worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
	        }

	        if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !this.A_canCatchFire(worldIn, pos.down(), EnumFacing.UP))
	        {
	            if (this.A_canCatchFire(worldIn, pos.west(), EnumFacing.EAST))
	            {
	                for (int j = 0; j < 2; ++j)
	                {
	                    double d3 = (double)pos.getX() + rand.nextDouble() * 0.10000000149011612D;
	                    double d8 = (double)pos.getY() + rand.nextDouble();
	                    double d13 = (double)pos.getZ() + rand.nextDouble();
	                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d8, d13, 0.0D, 0.0D, 0.0D);
	                }
	            }

	            if (this.A_canCatchFire(worldIn, pos.east(), EnumFacing.WEST))
	            {
	                for (int k = 0; k < 2; ++k)
	                {
	                    double d4 = (double)(pos.getX() + 1) - rand.nextDouble() * 0.10000000149011612D;
	                    double d9 = (double)pos.getY() + rand.nextDouble();
	                    double d14 = (double)pos.getZ() + rand.nextDouble();
	                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d4, d9, d14, 0.0D, 0.0D, 0.0D);
	                }
	            }

	            if (this.A_canCatchFire(worldIn, pos.north(), EnumFacing.SOUTH))
	            {
	                for (int l = 0; l < 2; ++l)
	                {
	                    double d5 = (double)pos.getX() + rand.nextDouble();
	                    double d10 = (double)pos.getY() + rand.nextDouble();
	                    double d15 = (double)pos.getZ() + rand.nextDouble() * 0.10000000149011612D;
	                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d5, d10, d15, 0.0D, 0.0D, 0.0D);
	                }
	            }

	            if (this.A_canCatchFire(worldIn, pos.south(), EnumFacing.NORTH))
	            {
	                for (int i1 = 0; i1 < 2; ++i1)
	                {
	                    double d6 = (double)pos.getX() + rand.nextDouble();
	                    double d11 = (double)pos.getY() + rand.nextDouble();
	                    double d16 = (double)(pos.getZ() + 1) - rand.nextDouble() * 0.10000000149011612D;
	                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d11, d16, 0.0D, 0.0D, 0.0D);
	                }
	            }

	            if (this.A_canCatchFire(worldIn, pos.up(), EnumFacing.DOWN))
	            {
	                for (int j1 = 0; j1 < 2; ++j1)
	                {
	                    double d7 = (double)pos.getX() + rand.nextDouble();
	                    double d12 = (double)(pos.getY() + 1) - rand.nextDouble() * 0.10000000149011612D;
	                    double d17 = (double)pos.getZ() + rand.nextDouble();
	                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
	                }
	            }
	        }
	        else
	        {
	            for (int i = 0; i < 3; ++i)
	            {
	                double d0 = (double)pos.getX() + rand.nextDouble();
	                double d1 = (double)pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
	                double d2 = (double)pos.getZ() + rand.nextDouble();
	                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	            }
	        }
	    }

	    /**
	     * Convert the given metadata into a BlockState for this Block
	     */
	    public IBlockState getStateFromMeta(int meta)
	    {
	        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
	    }

	    /**
	     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
	     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
	     */
	    @SideOnly(Side.CLIENT)
	    public BlockRenderLayer getRenderLayer()
	    {
	        return BlockRenderLayer.CUTOUT;
	    }

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
	    public int getMetaFromState(IBlockState state)
	    {
	        return ((Integer)state.getValue(AGE)).intValue();
	    }

	    protected BlockStateContainer createBlockState()
	    {
	        return new BlockStateContainer(this, new IProperty[] {AGE, NORTH, EAST, SOUTH, WEST, UPPER});
	    }

	    /*================================= Forge Start ======================================*/
	    /**
	     * Side sensitive version that calls the block function.
	     *
	     * @param world The current world
	     * @param pos Block position
	     * @param face The side the fire is coming from
	     * @return True if the face can catch fire.
	     */

	    /*
	    public boolean canCatchFire(IBlockAccess world, BlockPos pos, EnumFacing face)
	    {
	        return world.getBlockState(pos).getBlock().isFlammable(world, pos, face);
	    }
	    */
	    
	    
	    /*================================= Forge Start ======================================*/
	    
	
	    @Deprecated // Use tryCatchFire with face below
	    private void catchOnFire(World worldIn, BlockPos pos, int chance, Random random, int age)
	    {
	        this.tryCatchFireBlock(worldIn, pos);
	    }
	    
	    
	    
	    /*
	    @Deprecated // Use Block.getFlammability
	    public int getFlammability(Block blockIn)
	    {
	        Integer integer = this.flammabilities.get(blockIn);
	        return integer == null ? 0 : integer.intValue();
	    }
	    */
	    
	    //return this.SMELT_MAP.get(worldIn.getBlockState(pos).getBlock()) != null;
	   
	    
	    public boolean A_checkBlock(IBlockAccess worldIn, BlockPos pos, Block blockIn)
		{
	    	// this.SMELT_MAP.get(worldIn.getBlockState(pos).getBlock()) 
	    	//System.out.println(this.SMELT_MAP.get(blockIn));
	    	//return this.SMELT_MAP.get(blockIn) != null;
	    	
	    	//System.out.println(this.SMELT_MAP.get(worldIn.getBlockState(pos).getBlock()));
	    	return this.SMELT_MAP.get(worldIn.getBlockState(pos).getBlock()) != null;
		}
	    
	    public boolean A_getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	    {
	        return A_checkBlock(world, pos, this);
	    }
	    
	    public boolean A_isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	    {
	        return A_getFlammability(world, pos, face);
	    }
	    
	    
	    public boolean A_canCatchFire(IBlockAccess world, BlockPos pos, EnumFacing face)
	    {
	        //return world.getBlockState(pos).getBlock().A_isFlammable(world, pos, face);
	        //return world.getBlockState(pos).getBlock() == 
	    	return A_isFlammable(world, pos, face);
	    }
	    
	    
	    
	    /*================================= Forge Start ======================================*/
	    
	    
	    
	    
	    
	    
	    
	    /*================================= Forge Start ======================================*/

	    /**
	     * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
	     * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
	     * <p>
	     * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that
	     * does not fit the other descriptions and will generally cause other things not to connect to the face.
	     * 
	     * @return an approximation of the form of the given face
	     * @deprecated call via {@link IBlockState#getBlockFaceShape(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
	     * Implementing/overriding is fine.
	     */
	    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	    {
	        return BlockFaceShape.UNDEFINED;
	    }
		    
		
}