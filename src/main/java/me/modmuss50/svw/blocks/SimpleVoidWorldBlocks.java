package me.modmuss50.svw.blocks;


import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = "simplevoidworld")
@GameRegistry.ObjectHolder("simplevoidworld")
public class SimpleVoidWorldBlocks {
	public static final BlockPortal portal = null;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockPortal());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(portal).setRegistryName(portal.getRegistryName()));
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		registerItemModel(Item.getItemFromBlock(portal), 0);
	}

	static void registerItemModel(Item item, int meta) {
		ResourceLocation loc = item.getRegistryName();
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(loc, "inventory"));
	}
}
