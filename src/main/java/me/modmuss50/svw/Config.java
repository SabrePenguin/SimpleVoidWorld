package me.modmuss50.svw;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

	public static Configuration config;
	public static String CATEGORY_IDS = "IDs";
	public static String CATEGORY_TWEAKS = "TWEAKS";

	public static int dimID = 43;
	public static boolean darkSky = true;
	public static boolean eternalDay = true;
	
	public static boolean respawn = true;
	public static boolean creatureSpawn = true;

	public static int portalRadius = 8;
	public static boolean portalAtY64 = true;
	public static boolean backwardsCompat = false;

	public static void load(File configFile) {
		config = new Configuration(configFile);
		config.load();

		dimID = config.get(CATEGORY_IDS, "Dim id", 43, "This is the id of the dimension in the mod, this should be unique to Simple Void World").getInt();

		darkSky = config.get(CATEGORY_TWEAKS, "Dark mode", false, "When set to true the sky and fog color are black this creates a seamless skybox").getBoolean();
		eternalDay = config.get(CATEGORY_TWEAKS, "Its High Noon", false, "When true this locks the at noon and creates an eternal day").getBoolean();

		respawn = config.get(CATEGORY_TWEAKS, "Respawn", false, "When true this allows the player to respawn in this dimension").getBoolean();
		creatureSpawn = config.get(CATEGORY_TWEAKS, "CreatureSpawn", false, "When true this allows creatures to spawn in this dimension").getBoolean();

		portalRadius = config.getInt(CATEGORY_TWEAKS, "Radius", 8, 0, 64, "The radius to search from the position for a portal");
		portalAtY64 = config.get(CATEGORY_TWEAKS, "Portal Default Height", true, "When true this sets the default height of the portal to 64.").getBoolean();
		backwardsCompat = config.getBoolean(CATEGORY_TWEAKS, "Backwards Portal Compat", false, "When true this will search for old portals first.\nRequires portalAtY64 to be false.");

		config.save();
	}

}
