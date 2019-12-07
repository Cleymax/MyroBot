package fr.cleymax.myrobot.api;

import com.electronwill.nightconfig.core.file.FileConfig;
import fr.cleymax.myrobot.MyroBot;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File <b>Config</b> located on fr.cleymax.myrobot.api Config is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 03/10/2019 at 22:30
 */

public final class Config {

	private       FileConfig settings;

	public Config()
	{
		File file = new File("./settings.json");
		Logger logger = Logger.getLogger(getClass().getSimpleName());
		if (!file.exists())
		{
			try
			{
				if (file.createNewFile())
				{
					logger.info("Configuration file was been created at " + file.getAbsolutePath());
				}
			}
			catch (IOException e)
			{
				logger.log(Level.SEVERE, "Can't create configuration file !", e);
			}

			try
			{
				InputStream in = MyroBot.getResource("settings.json");

				try (OutputStream out = new FileOutputStream(file))
				{
					byte[] buf = new byte[1024 * 4];
					int    len = in.read(buf);

					while (len != -1)
					{
						out.write(buf, 0, len);
						len = in.read(buf);
					}
				}

				in.close();
			}
			catch (Exception e)
			{
				logger.log(Level.SEVERE, "Error in copying the default configuration file !", e);
			}
		}

		settings = FileConfig.of("settings.json");
		settings.load();

		logger.info("The configuration has been loaded!");
	}

	public void init()
	{
		settings.load();
	}

	public void stop()
	{
		settings.close();
	}

	public FileConfig getSettings()
	{
		return settings;
	}
}
