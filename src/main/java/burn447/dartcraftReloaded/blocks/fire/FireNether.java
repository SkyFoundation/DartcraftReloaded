package burn447.dartcraftReloaded.blocks.fire;

import net.minecraft.init.Blocks;

public class FireNether extends BlockForceFire{
    public FireNether(String name) {
        super(name);
    }

    @Override
    public void init() {
        setFireInfo(Blocks.GRASS,Blocks.DIRT);
        setFireInfo(Blocks.DIRT, Blocks.NETHERRACK);
        setFireInfo(Blocks.STONE,Blocks.NETHERRACK);
        setFireInfo(Blocks.REDSTONE_BLOCK, Blocks.GLOWSTONE);
    }

}
