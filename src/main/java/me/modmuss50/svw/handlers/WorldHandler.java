package me.modmuss50.svw.handlers;

import me.modmuss50.svw.Tags;
import me.modmuss50.svw.world.CustomTimeData;
import me.modmuss50.svw.world.VoidWorldProvider;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class WorldHandler {
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.END && !event.world.isRemote) {
			if (event.world.provider instanceof VoidWorldProvider) {
				if (event.world.getGameRules().getBoolean("doDaylightCycle")) {
					CustomTimeData data = CustomTimeData.get(event.world);
					data.advancedTime();
					event.world.setWorldTime(data.getTime());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isRemote && event.getWorld().provider instanceof VoidWorldProvider provider) {
			provider.getCachedTime();
		}
	}

	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event) {
		if (!event.getWorld().isRemote && event.getWorld().provider instanceof VoidWorldProvider provider) {
			provider.clearCachedTime();
		}
	}
}
