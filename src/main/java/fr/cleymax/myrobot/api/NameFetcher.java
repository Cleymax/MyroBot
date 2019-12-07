package fr.cleymax.myrobot.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * File <b>NameFetcher</b> located on fr.cleymax.myrobot.api NameFetcher is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 02/10/2019 at 17:32
 */

public class NameFetcher implements Callable<String> {
	private static final String     PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
	private final        JsonParser jsonParser  = new JsonParser();
	private final        UUID       uuid;

	public NameFetcher(UUID uuid)
	{
		this.uuid = uuid;
	}

	@Override
	public String call() throws Exception
	{
		HttpURLConnection connection   = (HttpURLConnection) new URL(PROFILE_URL + uuid.toString().replace("-", "")).openConnection();
		JsonObject        response     = jsonParser.parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
		String            name         = response.get("name").getAsString();
		String            cause        = response.get("cause").getAsString();
		String            errorMessage = response.get("errorMessage").getAsString();

		if (name == null || (cause != null && cause.length() > 0))
		{
			throw new IllegalStateException(errorMessage);
		}
		return name;
	}
}