package burn447.dartcraftReloaded.Potion.Potions;

import net.minecraft.potion.Potion;

/**
 * Created by BURN447 on 6/14/2018.
 */
public class PotionBleeding extends Potion {

	public PotionBleeding() {
		super(true, 0);
		this.setRegistryName("bleeding");
		this.setPotionName("Bleeding");
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public boolean isBeneficial() {
		return false;
	}

	@Override
	public boolean hasStatusIcon() {
		return true;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
}
