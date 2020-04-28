package burn447.dartcraftReloaded.Items.Tools;

import burn447.dartcraftReloaded.dartcraftReloaded;
import net.minecraft.item.ItemHoe;

/**
 * Created by DouYan on 2020/4/19
 */
public class ItemForceHoe extends ItemHoe {
	private static String name;

	public ItemForceHoe(String name) {
		super(dartcraftReloaded.forceToolMaterial);

		this.setMaxDamage(1000);
		this.name = name;
		this.setRegistryName(name);
		this.setTranslationKey(name);
		this.setCreativeTab(dartcraftReloaded.creativeTab);
	}

	public void registerItemModel() {
		dartcraftReloaded.proxy.registerItemRenderer(this, 0, name);
	}

}
