package burn447.dartcraftReloaded.Items.Tools;

import burn447.dartcraftReloaded.dartcraftReloaded;
import net.minecraft.item.Item;

/**
 * Created by DouYan on 2020/4/19
 */
public class ItemForcesMultifunctionalTools extends ItemMultifunctionalTools
{
	private static String name;
	
    public ItemForcesMultifunctionalTools(String name)
    {
    	super(Item.ToolMaterial.IRON,1024);
        this.name = name;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setCreativeTab(dartcraftReloaded.creativeTab);
    }
    
    public void registerItemModel() 
    {
        dartcraftReloaded.proxy.registerItemRenderer(this, 0, name);
    }
}
