package me.modmuss50.svw.world;

import me.modmuss50.svw.SVWConfig;
import me.modmuss50.svw.blocks.SimpleVoidWorldBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
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
		if (world.provider.getDimension() != SVWConfig.ids.dimID && entityIn instanceof EntityPlayer) {
			BlockPos result;
			if (SVWConfig.tweaks.compatibility.backwardsCompat && !SVWConfig.tweaks.compatibility.portalAtY64) {
				result = searchInRange(pos, SVWConfig.tweaks.portalRadius);
				if (result == null)
					result = searchInRange(pos, 0, 256, SVWConfig.tweaks.portalRadius);
			} else {
			 	result = SVWConfig.tweaks.compatibility.portalAtY64 ? searchInRange(pos, 0, 256, SVWConfig.tweaks.portalRadius) :
						searchInRange(pos, SVWConfig.tweaks.portalRadius);
			}
			if (result != null) {
				pos = result.toImmutable();
			} else {
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
				if (!foundBlock) {
					pos = ((EntityPlayer) entityIn).getBedLocation(world.provider.getDimension());
					if (pos == null) {
						pos = world.provider.getRandomizedSpawnPoint();
					}
				}
			}

		}
		if (world.provider.getDimension() == SVWConfig.ids.dimID) {
			BlockPos result;
			if (SVWConfig.tweaks.compatibility.backwardsCompat && !SVWConfig.tweaks.compatibility.portalAtY64) {
				result = searchInRange(pos, SVWConfig.tweaks.portalRadius);
				if (result == null)
					result = searchInRange(pos, 64, SVWConfig.tweaks.portalRadius);
			} else {
				result = SVWConfig.tweaks.compatibility.portalAtY64 ? searchInRange(pos, 64, SVWConfig.tweaks.portalRadius) : searchInRange(pos, SVWConfig.tweaks.portalRadius);
			}
			if (result != null) {
				pos = result.toImmutable();
			} else {
				//TODO look around for a free space so it doesnt place in a base?
				int y = SVWConfig.tweaks.compatibility.portalAtY64 ? 64 : pos.getY();
				pos = new BlockPos(pos.getX(), y, pos.getZ());
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
					for (EnumFacing facing : EnumFacing.HORIZONTALS) {
						world.setBlockState(pos.up().offset(facing), Blocks.TORCH.getDefaultState());
					}
				}
			}
		}

		entityIn.setLocationAndAngles((double) pos.getX() + 0.5, (double) pos.getY() + 1, (double) pos.getZ() + 0.5, entityIn.rotationYaw, 0.0F);
		entityIn.motionX = 0.0D;
		entityIn.motionY = 0.0D;
		entityIn.motionZ = 0.0D;

	}

	@SuppressWarnings("ConstantConditions")
	private BlockPos.MutableBlockPos searchInRange(BlockPos original, int minY, int maxY, int radius) {
		int originalX = original.getX();
		int originalZ = original.getZ();
		minY = Math.min(minY, 0);
		maxY = Math.max(maxY, 255);
		int newYCenter = (minY + maxY) / 2;
		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(original);
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = newYCenter; y <= maxY; y++) {
					blockPos.setPos(originalX + x, y, originalZ + z);
					if (world.getBlockState(blockPos).getBlock() == SimpleVoidWorldBlocks.portal) {
						return blockPos;
					}
				}
				for (int y = newYCenter; y >= minY; y--) {
					blockPos.setPos(originalX + x, y, originalZ + z);
					if (world.getBlockState(blockPos).getBlock() == SimpleVoidWorldBlocks.portal) {
						return blockPos;
					}
				}
			}
		}
		return null;
	}

	private BlockPos.MutableBlockPos searchInRange(BlockPos original, int source, int radius) {
		return searchInRange(original, source - radius, source + radius, radius);
	}

	private BlockPos.MutableBlockPos searchInRange(BlockPos original, int radius) {
		return searchInRange(original, original.getY() - radius, original.getY() + radius, radius);
	}
}
