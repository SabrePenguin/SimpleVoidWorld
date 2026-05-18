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
				pos = result;
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
				pos = result;
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
	private BlockPos searchInRange(BlockPos original, int minY, int maxY, int radius) {
		int originalX = original.getX();
		int originalZ = original.getZ();
		minY = Math.max(minY, 0);
		maxY = Math.min(maxY, 255);
		int newYCenter = (minY + maxY) / 2;
		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(originalX, newYCenter, originalZ);
		if (world.getBlockState(blockPos).getBlock() == SimpleVoidWorldBlocks.portal) {
			return blockPos.toImmutable();
		}
		BlockPos closest = null;
		double closestDistance = 256;
		for (int layer = 1; layer <= newYCenter; layer++) {
			int xzShell = Math.min(layer, radius);
			for (int x = -xzShell; x <= xzShell; x++) {
				for (int z = -xzShell; z <= xzShell; z++) {
					for (int yOffset = -layer; yOffset <= layer; yOffset++) {
						int y = yOffset + newYCenter;
						if (y < minY || y > maxY)
							continue;
						boolean xEdge = Math.abs(x) != xzShell;
						boolean zEdge = Math.abs(z) != xzShell;
						boolean yEdge = Math.abs(yOffset) != layer;
						if (xEdge && zEdge && yEdge)
							continue;
						blockPos.setPos(originalX + x, y, originalZ + z);
						world.getChunkProvider().provideChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
						if (world.getBlockState(blockPos).getBlock() == SimpleVoidWorldBlocks.portal) {
							double dist = getDistance(original, blockPos);
							if (dist < closestDistance) {
								closestDistance = dist;
								closest = blockPos.toImmutable();
							}
						}
					}
				}
			}
			if (closest != null) {
				return closest;
			}
		}
		return null;
	}

	private BlockPos searchInRange(BlockPos original, int source, int radius) {
		return searchInRange(original, source - radius, source + radius, radius);
	}

	private BlockPos searchInRange(BlockPos original, int radius) {
		return searchInRange(original, original.getY() - radius, original.getY() + radius, radius);
	}

	private double getDistance(BlockPos first, BlockPos second) {
		int xDist = Math.abs(first.getX() - second.getX());
		int yDist = Math.abs(first.getY() - second.getY());
		int zDist = Math.abs(first.getZ() - second.getZ());
		long total = ((long) xDist * xDist) + ((long) yDist * yDist) + ((long) zDist * zDist);
		return Math.sqrt(total);
	}
}
