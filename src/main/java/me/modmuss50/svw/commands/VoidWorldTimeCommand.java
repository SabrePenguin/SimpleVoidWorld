package me.modmuss50.svw.commands;

import me.modmuss50.svw.SVWConfig;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandTime;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class VoidWorldTimeCommand extends CommandTime {
	protected ICommandSender commandSender;

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		this.commandSender = sender;
		super.execute(server, sender, args);
	}

	@Override
	protected void setAllWorldTimes(MinecraftServer server, int time) {
		boolean senderInVoid = commandSender.getEntityWorld().provider.getDimension() == SVWConfig.ids.dimID;
		for (WorldServer world: server.worlds) {
			boolean currentWorldVoid = world.provider.getDimension() == SVWConfig.ids.dimID;
			if ((senderInVoid && currentWorldVoid) || (!senderInVoid && !currentWorldVoid))
				world.setWorldTime(time);
		}
	}

	@Override
	protected void incrementAllWorldTimes(MinecraftServer server, int amount) {
		boolean senderInVoid = commandSender.getEntityWorld().provider.getDimension() == SVWConfig.ids.dimID;
		for (WorldServer world: server.worlds) {
			boolean currentWorldVoid = world.provider.getDimension() == SVWConfig.ids.dimID;
			if ((senderInVoid && currentWorldVoid) || (!senderInVoid && !currentWorldVoid))
				world.setWorldTime(world.getWorldTime() + amount);
		}
	}
}
