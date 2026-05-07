package me.modmuss50.svw.world;

import me.modmuss50.svw.Config;
import me.modmuss50.svw.blocks.SimpleVoidWorldBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

public class WorldTeleporter extends Teleporter {

	BlockPos pos;
	WorldServer world;

	public WorldTeleporter(WorldServer worldIn, BlockPos pos) {
		super(worldIn);
		this.pos = pos;
		world = worldIn;
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw) {
		if (world.provider.getDimension() != Config.dimID && entityIn instanceof EntityPlayer) {
			boolean foundBlock = false;
			BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(pos.getX(), 0, pos.getZ());
			for (int y = 0; y < 256; y++) {
				mutableBlockPos.setY(y);
				if (world.getBlockState(mutableBlockPos).getBlock() == SimpleVoidWorldBlocks.portal) {
					pos = new BlockPos(pos.getX(), y + 1, pos.getZ());
					foundBlock = true;
					break;
				}
			}
			if(!foundBlock){
				pos = ((EntityPlayer) entityIn).getBedLocation(world.provider.getDimension());
				if(pos == null){
					pos = world.provider.getRandomizedSpawnPoint();
				}
			}

		}
		if (world.provider.getDimension() == Config.dimID) {
			BlockPos result = nearestBlock();
			if (result != null) {
				pos = result;
			}
			//TODO look around for a free space so it doesnt place in a base?
			pos = new BlockPos(pos.getX(), 64, pos.getZ());
			if (world.getBlockState(pos).getBlock() != SimpleVoidWorldBlocks.portal) {
				int color = world.rand.nextInt(15);
				for (int x = -3; x < 4; x++) {
					for (int z = -3; z < 4; z++) {
						if (world.isAirBlock(pos.add(x, 0, z))) {
							world.setBlockState(pos.add(x, 0, z), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(color));
						}

					}
				}
				world.setBlockState(pos, SimpleVoidWorldBlocks.portal.getDefaultState());
				for(EnumFacing facing : EnumFacing.HORIZONTALS){
					world.setBlockState(pos.up().offset(facing), Blocks.TORCH.getDefaultState());
				}

			}
		}

		entityIn.setLocationAndAngles((double) pos.getX() + 0.5, (double) pos.getY() + 1, (double) pos.getZ() + 0.5, entityIn.rotationYaw, 0.0F);
		entityIn.motionX = 0.0D;
		entityIn.motionY = 0.0D;
		entityIn.motionZ = 0.0D;

	}

	@SuppressWarnings("ConstantConditions")
	@Nullable
	private BlockPos nearestBlock() {
		int radius = Config.portalRadius;
		int originalX = pos.getX();
		int originalZ = pos.getZ();
		BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos(originalX, 64, originalZ);
		for (int y = 0; y <= radius; y++) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					mutPos.setPos(originalX + x, 64 + y, originalZ + z);
					if (world.getBlockState(mutPos).getBlock() == SimpleVoidWorldBlocks.portal) {
						mutPos.setY(mutPos.getY());
						return mutPos;
					}
				}
			}
		}
		for (int y = -1; y >= -radius; y--) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					mutPos.setPos(originalX + x, 64 + y, originalZ + z);
					if (world.getBlockState(mutPos).getBlock() == SimpleVoidWorldBlocks.portal) {
						mutPos.setY(mutPos.getY());
						return mutPos;
					}
				}
			}
		}
		return null;
	}
}
