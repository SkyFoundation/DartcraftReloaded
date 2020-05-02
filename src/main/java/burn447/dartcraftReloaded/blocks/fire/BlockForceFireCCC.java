package burn447.dartcraftReloaded.blocks.fire;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import burn447.dartcraftReloaded.dartcraftReloaded;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockForceFireCCC extends Block
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    protected static final AxisAlignedBB REED_AABB = new AxisAlignedBB(0 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 16 / 16D);
    
    public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool UPPER = PropertyBool.create("up");
	
	private final Map<Block, Block> SMELT_MAP = Maps.<Block, Block>newIdentityHashMap();
    
    protected String name;
    
    
    public BlockForceFireCCC(String name)
    {
    	super(Material.FIRE);
    	
    	
    	//super(Material.FIRE);
        
        this.setRegistryName(name);
		this.setTranslationKey(name);
		this.name = name;
		
		this.setHardness(0.0F);
		this.setLightLevel(1.0F);
		this.setSoundType(SoundType.CLOTH);
		this.disableStats();
		
		this.setCreativeTab(dartcraftReloaded.creativeTab);

		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0))
				.withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false))
				.withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false))
				.withProperty(UPPER, Boolean.valueOf(false)));
		
		
        //this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
        this.setTickRandomly(true);
        
        this.init();
    }
    
    /*===========================================*/
    
    public void registerItemModel(Item itemBlock)
    {
		dartcraftReloaded.proxy.registerItemRenderer(itemBlock, 0, name);
	}

	public Item createItemBlock()
	{
		return new ItemBlock(this).setRegistryName(getRegistryName());
	}
	
	/*===========================================*/
	
	
	
	
	
	
	
	
	public void init()
	{
		setFireInfo(Blocks.GRASS, Blocks.DIRT);
		setFireInfo(Blocks.DIRT, Blocks.NETHERRACK);
		setFireInfo(Blocks.STONE, Blocks.NETHERRACK);
		setFireInfo(Blocks.REDSTONE_BLOCK, Blocks.GLOWSTONE);
		
		
		setFireInfo(Blocks.WOOL, Blocks.GLOWSTONE);
	}

	public void setFireInfo(Block blockIn, Block blockOut)
	{
		if (blockIn == Blocks.AIR)
			throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
		this.SMELT_MAP.put(blockIn, blockOut);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*===========================================*/
	
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return REED_AABB;
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
		return NULL_AABB;
	}
    
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	/*===========================================*/
	
	
	

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	Block obj = worldIn.getBlockState(pos.down()).getBlock();
		
		System.out.println("现在冶炼块我们有没有冶炼" + obj);
		
		if (this.SMELT_MAP.get(obj) == null)
			return;
		worldIn.setBlockState(pos, this.SMELT_MAP.get(obj).getDefaultState());
		return;
		
    }
    
    
    
    
    
    /*=======================================*/
    
    //破坏方块下面自己方块掉了
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	 this.checkForDrop(worldIn, pos, state);
    }
    
    protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
    	//能方块上面放自己方块的。
    	if (canCatchFire(worldIn, pos.down()))
        {
    		return true;
        }
        else
        {
        	//破坏方块下面自己方块掉了
        	this.dropBlockAsItem(worldIn, pos, state, 0);
        	worldIn.setBlockToAir(pos);
        	return false;
        }
    }
    
    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return this.canCatchFire(worldIn, pos.down());
	}
    
    public boolean canCatchFire(IBlockAccess worldIn, BlockPos pos)
	{
		return this.SMELT_MAP.get(worldIn.getBlockState(pos).getBlock()) != null;
	}
	
    
    
    
    
    
    
    
    
    
    /*=======================================*/
    
    
    @Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		for (int i = 0; i < 3; ++i)
		{
			double d0 = (double) pos.getX() + rand.nextDouble();
			double d1 = (double) pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
			double d2 = (double) pos.getZ() + rand.nextDouble();
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
    
    
    @Override
	public int quantityDropped(Random random)
    {
		return 0;
	}
    
    
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }
    
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(AGE)).intValue();
    }
    
    
    @Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { AGE, NORTH, EAST, SOUTH, WEST, UPPER });
	}

    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}