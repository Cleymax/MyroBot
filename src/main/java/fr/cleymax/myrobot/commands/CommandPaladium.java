package fr.cleymax.myrobot.commands;

import fr.cleymax.myrobot.api.MinecraftServer;
import fr.cleymax.myrobot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * File <b>CommandPaladium</b> located on fr.cleymax.myrobot.commands CommandPaladium is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 04/10/2019 at 15:19
 * <p>
 * https://paladium-pvp.fr
 * </p>
 */

public final class CommandPaladium extends Command {

	public CommandPaladium()
	{
		super("paladium", true);
	}

	@Override
	public void execute(MessageReceivedEvent event, User user, String[] args)
	{
		deleteIfNotPvMessage(event.getMessage());

		event.getChannel().sendMessage(new EmbedBuilder().setDescription(instance.getLoadingEmote().getAsMention() + " Chargement des données ...").build()).queue(message -> MinecraftServer.api("proxy.paladium-pvp.fr", minecraftServer -> {
			if (minecraftServer.getPlayers() != null)
				message.editMessage(
						new EmbedBuilder()
								.setTitle("**Paladium**")
								.setDescription(" \nIP: `LAUNCHER`\nVersion: `1.7.10`\nCraft: `OFF`\n\nSite Web: [paladium-pvp.fr](https://paladium-pvp.fr)\nBoutique: [store.paladium-pvp.fr](https://store.paladium-pvp.fr/)\nDiscord: https://discord.gg/paladium\n\nConnectés: `" + minecraftServer.getPlayers().getOnline() + "`/`" + minecraftServer.getPlayers().getMax() + "`")
								.setColor(new Color(0xFA191A))
								.setFooter("Éxecuté par " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null)
								.setThumbnail("https://www.cleymax.fr/assets/icons-paladium.png")
								.build()
				).queue();
			else
				message.editMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
		}));
	}
}
