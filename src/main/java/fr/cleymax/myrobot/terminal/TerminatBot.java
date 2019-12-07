package fr.cleymax.myrobot.terminal;

import fr.cleymax.myrobot.MyroBot;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

/**
 * File <b>TerminatBot</b> located on fr.cleymax.myrobot.terminal TerminatBot is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 03/10/2019 at 23:16
 */

public final class TerminatBot extends Thread {

	private final MyroBot main;

	public TerminatBot(MyroBot main)
	{
		this.main = main;
	}

	@Override
	public void run()
	{
		while (true)
		{
			Scanner  scanner = new Scanner(System.in);
			String   line    = scanner.nextLine();
			String   command = line.split(" ")[0];
			String[] args    = ArrayUtils.remove(line.split(" "), 0);

			switch (command.toLowerCase())
			{
				case "stop":
					this.main.getJda().shutdownNow();
					this.main.getConfig().getSettings().save();
					this.main.getConfig().getSettings().close();
					System.out.println("Bye");
					System.exit(0);
					break;
				case "game":
					String game = StringUtils.join(args, " ");
					this.main.getJda().getPresence().setActivity(Activity.playing(game));
					System.out.println("Change activity to " + game);
					break;
				default:
					System.out.println("Command not found !");
					break;
			}
		}
	}
}
