package me.modmuss50.svw.world;

import me.modmuss50.svw.SVWConfig;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;

public class DecoupledWeatherWorldInfo extends DerivedWorldInfo {
	private final CustomDesyncedData data;
	public DecoupledWeatherWorldInfo(WorldInfo info, CustomDesyncedData desyncedData) {
		super(info);
		this.data = desyncedData;
	}

	private boolean syncWeather() {
		return SVWConfig.tweaks.weather.syncWorldWeather;
	}

	@Override
	public boolean isRaining() {
		if (syncWeather())
			return super.isRaining();
		else
			return data.isRaining();
	}

	@Override
	public void setRaining(boolean isRaining) {
		if (syncWeather())
			super.setRaining(isRaining);
		else
			data.setRaining(isRaining);
	}

	@Override
	public int getRainTime() {
		if (syncWeather())
			return super.getRainTime();
		else
			return data.getRainTime();
	}

	@Override
	public void setRainTime(int time) {
		if (syncWeather())
			super.setRainTime(time);
		else
			data.setRainTime(time);
	}

	@Override
	public boolean isThundering() {
		if (syncWeather())
			return super.isThundering();
		else
			return data.isThundering();
	}

	@Override
	public void setThundering(boolean thunderingIn) {
		if (syncWeather())
			super.setThundering(thunderingIn);
		else
			data.setThundering(thunderingIn);
	}

	@Override
	public int getThunderTime() {
		if (syncWeather())
			return super.getThunderTime();
		else
			return data.getThunderTime();
	}

	@Override
	public void setThunderTime(int time) {
		if (syncWeather())
			super.setThunderTime(time);
		else
			data.setThunderTime(time);
	}

	@Override
	public void setCleanWeatherTime(int cleanWeatherTimeIn) {
		if (syncWeather())
			super.setCleanWeatherTime(cleanWeatherTimeIn);
		else
			data.setClearTime(cleanWeatherTimeIn);
	}

	@Override
	public int getCleanWeatherTime() {
		if (syncWeather())
			return super.getCleanWeatherTime();
		else
			return data.getClearTime();
	}
}
