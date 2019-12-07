package fr.cleymax.myrobot.commands;

import fr.cleymax.myrobot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * File <b>CommandYoutube</b> located on fr.cleymax.myrobot.commands CommandYoutube is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 02/10/2019 at 21:01
 */

public class CommandYoutube extends Command {


	public CommandYoutube()
	{
		super("youtube", new String[]{"yt"}, true);
	}

	@Override
	public void execute(MessageReceivedEvent event, User user, String[] args)
	{
		event.getChannel().sendMessage(new EmbedBuilder().setColor(new Color(192,57,43)).setDescription("Lien vers la chaîne [Youtube de Myrolame](https://www.youtube.com/user/Myrolame)").build()).queue();
	}
}
