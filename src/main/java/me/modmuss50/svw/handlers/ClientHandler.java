package me.modmuss50.svw.handlers;

import me.modmuss50.svw.blocks.SimpleVoidWorldBlocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = "simplevoidworld", value = Side.CLIENT)
public class ClientHandler {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		registerItemModel(Item.getItemFromBlock(SimpleVoidWorldBlocks.portal), 0);
	}

	static void registerItemModel(Item item, int meta) {
		ResourceLocation loc = item.getRegistryName();
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(loc, "inventory"));
	}
}
