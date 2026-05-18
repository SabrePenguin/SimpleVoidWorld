package me.modmuss50.svw.world;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

@MethodsReturnNonnullByDefault
public class CustomTimeData extends WorldSavedData {
	private static final String NAME = "CUSTOM_WORLD_TIME";
	private long time = 0;
	private long lastCheckedTime = -1;
	private double accumulatedTime = 0;

	public CustomTimeData() {
		super(NAME);
	}

	public CustomTimeData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.time = nbt.getLong("time");
		if (nbt.hasKey("lastCheckedTime")) {
			this.lastCheckedTime = nbt.getLong("lastCheckedTime");
		}
		if (nbt.hasKey("accumulatedTime")) {
			this.accumulatedTime = nbt.getDouble("accumulatedTime");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setLong("time", time);
		compound.setLong("lastCheckedTime", lastCheckedTime);
		compound.setDouble("accumulatedTime", accumulatedTime);
		return compound;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
		this.markDirty();
	}

	public long getLastCheckedTime() {
		return lastCheckedTime;
	}

	public void setLastCheckedTime(long lastCheckedTime) {
		this.lastCheckedTime = lastCheckedTime;
	}

	public double getAccumulatedTime() {
		return accumulatedTime;
	}

	public void setAccumulatedTime(double accumulatedTime) {
		this.accumulatedTime = accumulatedTime;
	}

	public static CustomTimeData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		WorldSavedData data = storage.getOrLoadData(CustomTimeData.class, NAME);
		if (data == null) {
			CustomTimeData customTimeData = new CustomTimeData();
			if (!world.isRemote && world.getMinecraftServer() != null) {
				customTimeData.setLastCheckedTime(world.getMinecraftServer().getWorld(0).getTotalWorldTime());
			}
			storage.setData(NAME, customTimeData);
			return customTimeData;
		}
		if (data instanceof CustomTimeData instance) {
			return instance;
		} else {
			throw new RuntimeException("Not a void world");
		}
	}
}
