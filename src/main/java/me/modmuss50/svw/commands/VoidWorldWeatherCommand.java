package me.modmuss50.svw.commands;

import me.modmuss50.svw.SVWConfig;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandWeather;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@ParametersAreNonnullByDefault
public class VoidWorldWeatherCommand extends CommandWeather {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		int dim = sender.getEntityWorld().provider.getDimension();
		boolean senderInVoid = dim == SVWConfig.ids.dimID;
		if (senderInVoid && !SVWConfig.tweaks.weather.syncWorldWeather) {
			Optional<WorldServer> voidWorld = Arrays.stream(server.worlds)
					.filter(w -> w.provider.getDimension() == SVWConfig.ids.dimID)
					.findFirst();
			if (!voidWorld.isPresent()) {
				super.execute(server, sender, args);
			}
			WorldInfo worldinfo = voidWorld.get().getWorldInfo();
			if (args.length >= 1 && args.length <= 2)
			{
				int i = (300 + (new Random()).nextInt(600)) * 20;

				if (args.length == 2)
				{
					i = parseInt(args[1], 1, 1000000) * 20;
				}

				if ("clear".equalsIgnoreCase(args[0]))
				{
					worldinfo.setCleanWeatherTime(i);
					worldinfo.setRainTime(0);
					worldinfo.setThunderTime(0);
					worldinfo.setRaining(false);
					worldinfo.setThundering(false);
					notifyCommandListener(sender, this, "commands.weather.clear");
				}
				else if ("rain".equalsIgnoreCase(args[0]))
				{
					worldinfo.setCleanWeatherTime(0);
					worldinfo.setRainTime(i);
					worldinfo.setThunderTime(i);
					worldinfo.setRaining(true);
					worldinfo.setThundering(false);
					notifyCommandListener(sender, this, "commands.weather.rain");
				}
				else
				{
					if (!"thunder".equalsIgnoreCase(args[0]))
					{
						throw new WrongUsageException("commands.weather.usage");
					}

					worldinfo.setCleanWeatherTime(0);
					worldinfo.setRainTime(i);
					worldinfo.setThunderTime(i);
					worldinfo.setRaining(true);
					worldinfo.setThundering(true);
					notifyCommandListener(sender, this, "commands.weather.thunder");
				}
			}
			else
			{
				throw new WrongUsageException("commands.weather.usage");
			}
		} else {
			super.execute(server, sender, args);
		}
	}
}
