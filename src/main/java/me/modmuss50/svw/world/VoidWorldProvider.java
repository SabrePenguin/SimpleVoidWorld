package me.modmuss50.svw.world;

import mcp.MethodsReturnNonnullByDefault;
import me.modmuss50.svw.SVWConfig;
import me.modmuss50.svw.SimpleVoidWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class VoidWorldProvider extends WorldProvider {
	private CustomTimeData cachedTime;
	private long time = 0;

	public CustomTimeData getCachedTime() {
		if (cachedTime == null) {
			cachedTime = CustomTimeData.get(world);
		}
		return cachedTime;
	}

	public void clearCachedTime() {
		cachedTime = null;
	}

	@Override
	public DimensionType getDimensionType() {
		return SimpleVoidWorld.type;
	}

	@Override
	public IChunkGenerator createChunkGenerator() {
		return new VoidChunkGenerator(world);
	}

	@Override
	public boolean canRespawnHere() {
		return SVWConfig.tweaks.respawn;
	}

	@Override
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
		if (SVWConfig.tweaks.darkSky) {
			return new Vec3d(0D, 0D, 0D);
		}
		return super.getFogColor(p_76562_1_, p_76562_2_);
	}

	@Override
	public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
		if (SVWConfig.tweaks.darkSky) {
			return new Vec3d(0D, 0D, 0D);
		}
		return super.getSkyColor(cameraEntity, partialTicks);
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		double speedup = SVWConfig.tweaks.time.worldTimeModifier;
		double effectiveTicks = (worldTime * speedup) + (partialTicks * speedup);
		double d = (effectiveTicks % 24000)/ 24000 - 0.25;
		if (d < 0) {
			d += 1;
		}
		if (d > 1) {
			d -= 1;
		}
		float f = (float) d;
		float f1 = 1f - (float)((Math.cos(f * Math.PI) + 1) / 2);
		f = f + (f1 - f) / 3;
		return f;
	}

	@Override
	public long getWorldTime() {
		if (SVWConfig.tweaks.eternalDay) {
			return 6000;
		}
		if (!SVWConfig.tweaks.time.syncWorldTime) {
			if (world != null) {
				return world.isRemote ? this.time : getCachedTime().getTime();
			}
		}
		return super.getWorldTime();
	}

	@Override
	public void setWorldTime(long time) {
		if (!SVWConfig.tweaks.time.syncWorldTime && !SVWConfig.tweaks.eternalDay) {
			if (world != null) {
				if (world.isRemote) {
					this.time = time;
				} else {
					CustomTimeData cached = getCachedTime();
					cached.setTime(time);
					if (world.getMinecraftServer() != null) {
						cached.setLastCheckedTime(world.getMinecraftServer().getWorld(0).getTotalWorldTime());
					}
				}
			}
		} else {
			super.setWorldTime(time);
		}
	}

	@Override
	public boolean isDaytime() {
		if (SVWConfig.tweaks.eternalDay) {
			return true;
		}
		return super.isDaytime();
	}

	@Nullable
	@Override
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
		if (SVWConfig.tweaks.darkSky) {
			return null;
		}
		return super.calcSunriseSunsetColors(celestialAngle, partialTicks);
	}


	public int getRespawnDimension(EntityPlayerMP player)
	{
		if (SVWConfig.tweaks.respawn) return SVWConfig.ids.dimID;
		else return 0;
	}

}
