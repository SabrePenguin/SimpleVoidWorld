package me.modmuss50.svw.handlers;

import me.modmuss50.svw.SVWConfig;
import me.modmuss50.svw.Tags;
import me.modmuss50.svw.world.CustomDesyncedData;
import me.modmuss50.svw.world.VoidWorldProvider;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class WorldHandler {

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isRemote && event.getWorld().provider instanceof VoidWorldProvider provider) {
			CustomDesyncedData customDesyncedData = provider.getCachedTime();
			if (event.getWorld().getMinecraftServer() != null) {
				long currentOverworldTime = event.getWorld().getMinecraftServer().getWorld(0).getTotalWorldTime();
				long lastSaved = customDesyncedData.getLastCheckedTime();
				if (lastSaved != -1 && currentOverworldTime > lastSaved) {
					long missedTicks = currentOverworldTime - lastSaved;
					double speedup = SVWConfig.tweaks.time.worldTimeModifier;
					long adjustedTicks = (long) (missedTicks * speedup);
					customDesyncedData.setTime(customDesyncedData.getTime() + adjustedTicks);
				}
				customDesyncedData.setLastCheckedTime(currentOverworldTime);
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
