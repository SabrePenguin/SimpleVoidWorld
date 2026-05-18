package me.modmuss50.svw.handlers;

import me.modmuss50.svw.SVWConfig;
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
		if (event.phase == TickEvent.Phase.END && !event.world.isRemote && !SVWConfig.tweaks.time.syncWorldTime) {
			if (event.world.provider instanceof VoidWorldProvider provider) {
				if (event.world.getGameRules().getBoolean("doDaylightCycle")) {
					CustomTimeData data = CustomTimeData.get(event.world);
					event.world.setWorldTime(data.getTime());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isRemote && event.getWorld().provider instanceof VoidWorldProvider provider) {
			CustomTimeData customTimeData = provider.getCachedTime();
			if (event.getWorld().getMinecraftServer() != null) {
				long currentOverworldTime = event.getWorld().getMinecraftServer().getWorld(0).getTotalWorldTime();
				long lastSaved = customTimeData.getLastCheckedTime();
				if (lastSaved != -1 && currentOverworldTime > lastSaved) {
					long missedTicks = currentOverworldTime - lastSaved;
					customTimeData.setTime(customTimeData.getTime() + missedTicks);
				}
				customTimeData.setLastCheckedTime(currentOverworldTime);
			}
		}
	}

	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event) {
		if (!event.getWorld().isRemote && event.getWorld().provider instanceof VoidWorldProvider provider) {
			provider.clearCachedTime();
		}
	}
}
