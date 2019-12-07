package fr.cleymax.myrobot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * File <b>CommandManager</b> located on fr.cleymax.myrobot.command CommandManager is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 01/10/2019 at 21:27
 * <p>
 * Class to manage the different orders of the bot
 */

public final class CommandManager extends ListenerAdapter {

	private final Logger        logger = Logger.getLogger(getClass().getSimpleName());
	private final CommandParser parser = new CommandParser();

	private Set<Command> commands;

	/**
	 * Initialize a new Command manager
	 */
	public CommandManager()
	{
		this.commands = new HashSet<>();
	}

	/**
	 * Register a new Command
	 * @param command - Class for command
	 */
	public void register(Command command)
	{
		this.commands.add(command);
	}

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event)
	{
		if (event.getMessage().getContentRaw().startsWith("."))
		{
			logger.info("[CMD] " + event.getAuthor().getName() + ": " + event.getMessage().getContentRaw());
			CommandParser.CommandContainer container = parser.parse(event.getMessage().getContentRaw());
			for (Command command : commands)
			{
				if (command.getName().equalsIgnoreCase(container.getLabel()) || Arrays.asList(command.getAliases()).contains(container.getLabel()))
				{
					if (event.isFromGuild())
					{
						if (event.getMember().hasPermission(command.getPermission()))
							command.execute(event, event.getAuthor(), container.getArgs());
						else event.getChannel().sendMessage(new EmbedBuilder()
								.setDescription("Tu n'as pas la permission de faire cette commande !")
								.setThumbnail("https://cleymax.fr/assets/cancel-32.png")
								.setFooter("Éxecuté par " + event.getAuthor().getAsTag())
								.build()
						).queue();
					}

					if (command.isPv() && event.isFromType(ChannelType.PRIVATE))
						command.execute(event, event.getAuthor(), container.getArgs());
				}
			}
		}
	}
}