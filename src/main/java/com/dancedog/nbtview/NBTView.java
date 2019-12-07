package com.dancedog.nbtview;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * This project was designed to parse NBT (Named Binary Tag) files as JSON. This is my first independent Java project, so don't expect it to be
 * pretty.
 *
 * @author DanceDog / Ben
 * @since 2019-09-16
 */
public class NBTView {

	static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Read NBT data from a file (automatic GUNZIP)
	 *
	 * @param path Absolute path the the NBT file (.nbt, .dat, etc)
	 * @return JsonObject A JsonObject representing the NBT data
	 */
	public static JsonObject readFile(String path)
	{
		Path       filePath = Paths.get(path);
		JsonObject output   = new JsonObject();
		try
		{
			// Read the file as a byte array
			byte[] byteArray = Files.readAllBytes(filePath);

			// Detect & unzip GZIP'd data
			if (byteArray[0] == 31 && byteArray[1] == -117)
			{
				ByteArrayInputStream  byteIn  = new ByteArrayInputStream(byteArray);
				GZIPInputStream       gzipIn  = new GZIPInputStream(byteIn);
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

				int    res = 0;
				byte[] buf = new byte[1024];
				while (res >= 0)
				{
					res = gzipIn.read(buf, 0, buf.length);
					if (res > 0)
					{
						byteOut.write(buf, 0, res);
					}
				}
				byteArray = byteOut.toByteArray();
			}

			// Parse NBT as JSON
			output = new Parser(byteArray).parse();

		}
		catch (IOException e)
		{
			Logger.getGlobal().log(Level.SEVERE, "Can't read the file !", e);
			output.addProperty("nbtviewError", e.getMessage());
		}

		return output;
	}

	private NBTView()
	{
	}
}
