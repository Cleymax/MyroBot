package fr.cleymax.myrobot.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import fr.cleymax.myrobot.MyroBot;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * File <b>MinecraftServer</b> located on fr.cleymax.myrobot.api is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 Cleymax.
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <contact@cleymax.fr>} Created the 03/12/2019
 */

public class MinecraftServer {

	private final String      host;
	private final boolean     online;
	private final boolean     status;
	private final String      source;
	private final String      took;
	private final Version     version;
	private final Players     players;
	private final JsonElement description;

	public MinecraftServer(String host, boolean online, boolean status, String source, String took, Version version, Players players, JsonElement description)
	{
		this.host = host;
		this.online = online;
		this.status = status;
		this.source = source;
		this.took = took;
		this.version = version;
		this.players = players;
		this.description = description;
	}

	public String getHost()
	{
		return host;
	}

	public boolean isOnline()
	{
		return online;
	}

	public boolean isStatus()
	{
		return status;
	}

	public String getSource()
	{
		return source;
	}

	public String getTook()
	{
		return took;
	}

	public Version getVersion()
	{
		return version;
	}

	public Players getPlayers()
	{
		return players;
	}

	public JsonElement getDescription()
	{
		return description;
	}

	public static class Version {

		private final String name;
		private final int    protocol;

		public Version(String name, int protocol)
		{
			this.name = name;
			this.protocol = protocol;
		}

		public String getName()
		{
			return name;
		}

		public int getProtocol()
		{
			return protocol;
		}
	}

	public static class Players {

		private transient DecimalFormat        formatter;
		private transient DecimalFormatSymbols symbols;

		private final Integer  max;
		private final Integer  online;
		private final String[] sample;

		public Players(int max, int online, String[] sample)
		{
			this.max = max;
			this.online = online;
			this.sample = sample;

		}

		public String getMax()
		{
			this.formatter = (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
			this.symbols = this.formatter.getDecimalFormatSymbols();
			this.symbols.setGroupingSeparator(' ');
			this.formatter.setDecimalFormatSymbols(symbols);
			return formatter.format(max);
		}

		public String getOnline()
		{
			this.formatter = (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
			this.symbols = this.formatter.getDecimalFormatSymbols();
			this.symbols.setGroupingSeparator(' ');
			this.formatter.setDecimalFormatSymbols(symbols);
			return formatter.format(online);
		}

		public String getSample()
		{
			return String.join("\n", sample);
		}
	}

	public static void api(String host, Consumer<MinecraftServer> consumer)
	{
		MyroBot.getInstance().getExecutorService().execute(() -> {
			try
			{
				consumer.accept(MyroBot.getInstance().getHttpClient().execute(new HttpGet("https://eu.mc-api.net/v3/server/ping/" + host + "/"), response -> new Gson().fromJson(EntityUtils.toString(response.getEntity(), "UTF-8"), MinecraftServer.class)));
			}
			catch (IOException e)
			{
				consumer.accept(new MinecraftServer(host, false, false, "", "-1", new Version("unknown", -1), null, null));
			}
		});
	}
}
