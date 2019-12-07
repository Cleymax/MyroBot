package fr.cleymax.myrobot.api;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import fr.cleymax.myrobot.MyroBot;

import java.io.*;

/**
 * File <b>Config</b> located on fr.cleymax.myrobot.api Config is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 03/10/2019 at 22:30
 */

public final class Config {

	private FileConfig settings;

	public Config()
	{
		File file = new File("./settings.json");
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			try
			{
				InputStream  in  = MyroBot.getResource("settings.json");
				OutputStream out = new FileOutputStream(file);

				byte[] buf = new byte[1024 * 4];
				int    len = in.read(buf);

				while (len != -1)
				{
					out.write(buf, 0, len);
					len = in.read(buf);
				}

				out.close();
				in.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		settings = FileConfig.of("settings.json");
		settings.load();
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
