package fr.cleymax.myrobot.commands;

import fr.cleymax.myrobot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * File <b>CommandPing</b> located on fr.cleymax.myrobot.commands CommandPing is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 01/10/2019 at 21:56
 */

public class CommandPing extends Command {

	public CommandPing()
	{
		super("ping", true);
	}

	@Override
	public void execute(MessageReceivedEvent event, User user, String[] args)
	{
		event.getJDA().getRestPing().queue(ping -> event.getChannel().sendMessage(new EmbedBuilder().setDescription("Ping: **" + ping + "**ms.").build()).queue());
	}
}
