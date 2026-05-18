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

	private int rainTime = 0;
	private int thunderTime = 0;
	private int clearTime = 0;
	private boolean isRaining = false;
	private boolean isThundering = false;

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

		this.rainTime = nbt.getInteger("rainTime");
		this.thunderTime = nbt.getInteger("thunderTime");
		this.clearTime = nbt.getInteger("clearTime");
		this.isRaining = nbt.getBoolean("raining");
		this.isThundering = nbt.getBoolean("thundering");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setLong("time", time);
		compound.setLong("lastCheckedTime", lastCheckedTime);
		compound.setDouble("accumulatedTime", accumulatedTime);

		compound.setInteger("rainTime", rainTime);
		compound.setInteger("thunderTime", thunderTime);
		compound.setInteger("clearTime", clearTime);
		compound.setBoolean("raining", isRaining);
		compound.setBoolean("thundering", isThundering);
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

	public boolean isRaining() {
		return isRaining;
	}

	public void setRaining(boolean raining) {
		isRaining = raining;
	}

	public int getRainTime() {
		return rainTime;
	}

	public void setRainTime(int rainTime) {
		this.rainTime = rainTime;
	}

	public boolean isThundering() {
		return isThundering;
	}

	public void setThundering(boolean thundering) {
		isThundering = thundering;
	}

	public int getThunderTime() {
		return thunderTime;
	}

	public void setThunderTime(int thunderTime) {
		this.thunderTime = thunderTime;
	}

	public int getClearTime() {
		return clearTime;
	}

	public void setClearTime(int clearTime) {
		this.clearTime = clearTime;
	}
}
