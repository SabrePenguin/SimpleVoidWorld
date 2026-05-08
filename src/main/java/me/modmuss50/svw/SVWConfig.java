package me.modmuss50.svw;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Config(modid = Tags.MODID, category = "")
public class SVWConfig {
	public static Ids ids = new Ids();

	public static Tweaks tweaks = new Tweaks();

	public static class Ids {
		@Config.Name("Dim id")
		@Config.Comment("This is the id of the dimension in the mod, this should be unique to Simple Void World")
		public int dimID = 43;
	}

	public static class Tweaks {
		@Config.Name("CreatureSpawn")
		@Config.Comment("When true this allows creatures to spawn in this dimension")
		public boolean creatureSpawn = false;
		@Config.Name("Dark mode")
		@Config.Comment("When set to true the sky and fog color are black this creates a seamless skybox")
		public boolean darkSky = false;
		@Config.Name("Its High Noon")
		@Config.Comment("When true this locks the at noon and creates an eternal day")
		public boolean eternalDay = false;
		@Config.Name("Respawn")
		@Config.Comment("When true this allows the player to respawn in this dimension")
		public boolean respawn = false;
		@Config.Name("Radius")
		@Config.Comment("The radius to search from the position for a portal")
		@Config.RangeInt(min = 0, max = 64)
		public int portalRadius = 8;
		public BackwardsCompatCategory compatibility = new BackwardsCompatCategory();

		public static class BackwardsCompatCategory {
			@Config.Name("Portal Default Height")
			@Config.Comment("When true this sets the default height of the portal to 64.")
			public boolean portalAtY64 = true;
			@Config.Name("Backwards Portal Compat")
			@Config.Comment({
					"When true this will search for old portals first.",
					"Requires Portal Default Height to be false."
			})
			public boolean backwardsCompat = false;
		}
	}

	@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Tags.MODID)
	public static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Tags.MODID)) {
				ConfigManager.sync(Tags.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
