package fr.cleymax.myrobot.commands;

import fr.cleymax.myrobot.api.MinecraftServer;
import fr.cleymax.myrobot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * File <b>CommandExcalia</b> located on fr.cleymax.myrobot.commands CommandExcalia is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 04/10/2019 at 15:08
 */

public class CommandExcalia extends Command {

	public CommandExcalia()
	{
		super("excalia", true);
	}

	@Override
	public void execute(MessageReceivedEvent event, User user, String[] args)
	{
		deleteIfNotPvMessage(event.getMessage());

		event.getChannel().sendMessage(
				new EmbedBuilder().setDescription(instance.getLoadingEmote().getAsMention() + " Chargement des données ...").build()
		).queue(message -> MinecraftServer.api("play.excaliamc.fr", minecraftServer -> {
			if (minecraftServer.getPlayers() != null)
				message.editMessage(
						new EmbedBuilder()
								.setTitle("**Excalia**")
								.setDescription(" \nIP: `play.excaliamc.fr`\nVersion: `1.12.2`\nCraft: `OFF`\n\nSite Web: [excaliamc.fr](https://excaliamc.fr/)\nBoutique: [excaliamc.fr/shop](https://excaliamc.fr/shop)\nDiscord: https://discord.gg/Cre5Fzt\n\nConnectés: `" + (minecraftServer.getPlayers() != null ? minecraftServer.getPlayers().getOnline() + "` / `" + minecraftServer.getPlayers().getMax() + "` " : "Non trouvé`"))
								.setColor(new Color(0xA5BF46))
								.setFooter("Éxecuté par " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null)
								.setThumbnail("https://www.cleymax.fr/assets/icons-excalia.png")
								.build()
				).queue();
			else
				message.editMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
		}));
	}
}
