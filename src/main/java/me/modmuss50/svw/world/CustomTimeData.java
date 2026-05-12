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

	public CustomTimeData() {
		super(NAME);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.time = nbt.getLong("time");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setLong("time", time);
		return compound;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
		this.markDirty();
	}

	public void advancedTime() {
		this.time++;
		this.markDirty();
	}

	public static CustomTimeData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		WorldSavedData data = storage.getOrLoadData(CustomTimeData.class, NAME);
		if (data == null) {
			data = new CustomTimeData();
			storage.setData(NAME, data);
		}
		if (data instanceof CustomTimeData instance) {
			return instance;
		} else {
			throw new RuntimeException("Not a void world");
		}
	}
}
