package burn447.dartcraftReloaded.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;

import java.util.Map;

public final class EnchantUtils {

	private EnchantUtils() {
	}

	public static boolean removeEnchant(ItemStack stack, Enchantment enchantment) {
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		enchantments.remove(enchantment);
		EnchantmentHelper.setEnchantments(enchantments, stack);
		return false;
	}

	public static void incrementLevel(ItemStack stack, Enchantment enchantment) {
		for (NBTBase nbt : stack.getEnchantmentTagList()) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
			int oldLevel = enchantments.get(enchantment);
			enchantments.remove(enchantment);
			enchantments.put(enchantment, oldLevel + 1);
			EnchantmentHelper.setEnchantments(enchantments, stack);
		}
	}
}
