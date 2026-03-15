package me.modmuss50.svw;

import me.modmuss50.svw.blocks.SimpleVoidWorldBlocks;
import me.modmuss50.svw.world.VoidWorldProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "simplevoidworld", name = "SimpleVoidWorld", version = "@MODVERSION@")
public class SimpleVoidWorld {

	public static VoidTab creativeTab;

	public static DimensionType type;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Config.load(event.getSuggestedConfigurationFile());

		creativeTab = new VoidTab();

		type = DimensionType.register("simplevoidworld", "void", Config.dimID, VoidWorldProvider.class, false);
		DimensionManager.registerDimension(Config.dimID, type);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
	}

	public static class VoidTab extends CreativeTabs {

		public VoidTab() {
			super("simplevoidworld.creative.tab");
		}

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(SimpleVoidWorldBlocks.portal);
		}
	}
}
