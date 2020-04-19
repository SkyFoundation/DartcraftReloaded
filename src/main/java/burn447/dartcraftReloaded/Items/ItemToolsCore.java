package burn447.dartcraftReloaded.Items;


import burn447.dartcraftReloaded.dartcraftReloaded;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;


//工具核心
public class ItemToolsCore extends Item
{
	//private final String name="tools_core";
	
	private static String name;
	
    public ItemToolsCore(String name)
    {
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
