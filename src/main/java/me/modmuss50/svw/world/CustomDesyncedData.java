package me.modmuss50.svw.world;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

@MethodsReturnNonnullByDefault
public class CustomDesyncedData extends WorldSavedData {
	private static final String NAME = "CUSTOM_WORLD_DESYNC";
	private long time = 0;
	private long lastCheckedTime = -1;
	private double accumulatedTime = 0;

	public CustomDesyncedData() {
		super(NAME);
	}

	public CustomDesyncedData(String name) {
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

	public static CustomDesyncedData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		WorldSavedData data = storage.getOrLoadData(CustomDesyncedData.class, NAME);
		if (data == null) {
			CustomDesyncedData customDesyncedData = new CustomDesyncedData();
			if (!world.isRemote && world.getMinecraftServer() != null) {
				customDesyncedData.setLastCheckedTime(world.getMinecraftServer().getWorld(0).getTotalWorldTime());
			}
			storage.setData(NAME, customDesyncedData);
			return customDesyncedData;
		}
		if (data instanceof CustomDesyncedData instance) {
			return instance;
		} else {
			throw new RuntimeException("Not a void world");
		}
	}
}
