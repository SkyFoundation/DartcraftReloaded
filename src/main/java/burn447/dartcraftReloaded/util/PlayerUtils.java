package burn447.dartcraftReloaded.util;

import burn447.dartcraftReloaded.config.ConfigHandler;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by BURN447 on 7/23/2018.
 */
public class PlayerUtils {

    @SubscribeEvent
    //@SideOnly(Side.CLIENT)
    public void onLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if(ConfigHandler.betaMessage) {
            event.player.sendMessage(new TextComponentString("二作BURN447并未将被模组完善,先由像素天空基金会接手"));
        }
        String name=event.player.getName();
        if(name.equals("BURN447")) {
            event.player.sendMessage(new TextComponentString("你好,作者."));
        }else if(name.equals("Mr_zhao")||name.equals("143")){
            event.player.sendMessage(new TextComponentString("青蛙大人好"));
        }else if (name.equals("Bluedart")){
            event.player.sendMessage(new TextComponentString("Welcome to China"));
        }
    }
}
