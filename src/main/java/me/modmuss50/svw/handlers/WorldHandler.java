package me.modmuss50.svw.handlers;

import me.modmuss50.svw.SVWConfig;
import me.modmuss50.svw.Tags;
import me.modmuss50.svw.world.CustomDesyncedData;
import me.modmuss50.svw.world.DecoupledWeatherWorldInfo;
import me.modmuss50.svw.world.VoidWorldProvider;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class WorldHandler {

	@SuppressWarnings("deprecation")
	private static final Field WORLD_INFO_FIELD = ReflectionHelper.findField(World.class, "worldInfo", "field_72986_A");

	static {
		WORLD_INFO_FIELD.setAccessible(true);
	}

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld();
		if (!world.isRemote && world.provider instanceof VoidWorldProvider provider) {
			CustomDesyncedData customDesyncedData = provider.getCachedTime();
			if (world.getMinecraftServer() != null) {
				long currentOverworldTime = world.getMinecraftServer().getWorld(0).getTotalWorldTime();
				long lastSaved = customDesyncedData.getLastCheckedTime();
				if (lastSaved != -1 && currentOverworldTime > lastSaved) {
					long missedTicks = currentOverworldTime - lastSaved;
					double speedup = SVWConfig.tweaks.time.worldTimeModifier;
					long adjustedTicks = (long) (missedTicks * speedup);
					customDesyncedData.setTime(customDesyncedData.getTime() + adjustedTicks);
				}
				customDesyncedData.setLastCheckedTime(currentOverworldTime);
			}
			try {
				WorldInfo original = world.getWorldInfo();
				if (!(original instanceof DecoupledWeatherWorldInfo)) {
					DecoupledWeatherWorldInfo customInfo = new DecoupledWeatherWorldInfo(original, customDesyncedData);
					WORLD_INFO_FIELD.set(world, customInfo);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Could not replace worldInfo");
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
