package fr.cleymax.myrobot.command;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * File <b>CommandParser</b> located on fr.cleymax.myrobot.command CommandParser is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 01/10/2019 at 21:40
 *
 * Bot command parser
 */

public class CommandParser {

	/**
	 * Parse a new Command
	 *
	 * @param content - The content of a message
	 * @return a instance of the Command
	 */
	public CommandContainer parse(String content)
	{
		ArrayList<String> split = new ArrayList<>(Arrays.asList(content.substring(1).split(" ")));
		return new CommandContainer(split.get(0), new String[split.size() - 1]);
	}

	public static final class CommandContainer {

		private final String   label;
		private final String[] args;

		/**
		 * Initialize a Command container
		 * @param label - Label of the command
		 * @param args - Arguments of the command
		 */
		@ConstructorProperties({"label", "args"})
		public CommandContainer(String label, String[] args)
		{
			this.label = label;
			this.args = args;
		}

		/**
		 * Get the label
		 * @return command'label
		 */
		public String getLabel()
		{
			return label;
		}

		/**
		 * Get the argument
		 * @return command'arguments
		 */
		public String[] getArgs()
		{
			return args;
		}
	}
}
