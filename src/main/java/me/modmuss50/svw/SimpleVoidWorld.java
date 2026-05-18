package me.modmuss50.svw;

import me.modmuss50.svw.blocks.SimpleVoidWorldBlocks;
import me.modmuss50.svw.commands.VoidWorldTimeCommand;
import me.modmuss50.svw.commands.VoidWorldWeatherCommand;
import me.modmuss50.svw.world.VoidWorldProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Tags.MODID, name = Tags.MODNAME, version = Tags.VERSION)
public class SimpleVoidWorld {

	public static VoidTab creativeTab;

	public static DimensionType type;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		creativeTab = new VoidTab();

		type = DimensionType.register("simplevoidworld", "void", SVWConfig.ids.dimID, VoidWorldProvider.class, false);
		DimensionManager.registerDimension(SVWConfig.ids.dimID, type);
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new VoidWorldTimeCommand());
		event.registerServerCommand(new VoidWorldWeatherCommand());
	}

	public static class VoidTab extends CreativeTabs {

		public VoidTab() {
			super("simplevoidworld.creative.tab");
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(SimpleVoidWorldBlocks.portal);
		}
	}
}
