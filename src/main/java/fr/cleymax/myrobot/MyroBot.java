package fr.cleymax.myrobot;

import fr.cleymax.myrobot.api.Config;
import fr.cleymax.myrobot.command.CommandManager;
import fr.cleymax.myrobot.commands.*;
import fr.cleymax.myrobot.listener.ChannelListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;
import net.hypixel.api.HypixelAPI;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * File <b>MyroBot</b> located on fr.cleymax.myrobot MyroBot is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 01/10/2019 at 21:12
 */

public class MyroBot {

	private static MyroBot instance;

	private final Logger logger = Logger.getLogger(getClass().getSimpleName());

	private final HypixelAPI     hypixelAPI;
	private final CommandManager commandManager;
	private final Config         config;
	private final JDA            jda;

	private final ExecutorService executorService;
	private final HttpClient      httpClient;

	private Emote             loadingEmote;
	private Map<UUID, String> nameCache;


	private MyroBot() throws LoginException, InterruptedException
	{
		instance = this;

		this.config = new Config();
		this.config.init();

		this.executorService = Executors.newCachedThreadPool();
		this.httpClient = HttpClientBuilder.create().build();

		this.hypixelAPI = new HypixelAPI(UUID.fromString(this.config.getSettings().get("hypixel.api_key")));

		this.commandManager = new CommandManager();
		this.commandManager.register(new CommandPing());
		this.commandManager.register(new CommandHypixel(this));
		this.commandManager.register(new CommandYoutube());
		this.commandManager.register(new CommandExcalia());
		this.commandManager.register(new CommandPaladium());
		this.commandManager.register(new CommandSkyblock(this));

		JDABuilder jdaBuilder = new JDABuilder(this.config.getSettings().get("discord.bot_token").toString()).setStatus(OnlineStatus.ONLINE).addEventListeners(commandManager).addEventListeners(new ChannelListener(this));

		if (this.getConfig().getSettings().contains("discord.game"))
			jdaBuilder.setActivity(Activity.of(Activity.ActivityType.valueOf(this.config.getSettings().get("discord.game.type")), this.getConfig().getSettings().get("discord.game.name")));

		this.jda = jdaBuilder.build();
		jda.awaitReady();

		this.nameCache = new HashMap<>();

		loadingEmote = jda.getGuildById("336564349143613454").getEmoteById("651481926670221331");
	}

	public static void main(String[] args) throws LoginException, InterruptedException
	{
		new MyroBot();
	}

	public static InputStream getResource(String filename)
	{
		try
		{
			URL url = MyroBot.class.getClassLoader().getResource(filename);
			if (url == null)
			{
				return null;
			} else
			{
				URLConnection connection = url.openConnection();
				connection.setUseCaches(false);
				return connection.getInputStream();
			}
		}
		catch (IOException var4)
		{
			return null;
		}
	}

	public HypixelAPI getHypixelAPI()
	{
		return hypixelAPI;
	}

	public Map<UUID, String> getNameCache()
	{
		return nameCache;
	}

	public Config getConfig()
	{
		return config;
	}

	public JDA getJda()
	{
		return jda;
	}

	public Emote getLoadingEmote()
	{
		return loadingEmote;
	}

	public ExecutorService getExecutorService()
	{
		return executorService;
	}

	public HttpClient getHttpClient()
	{
		return httpClient;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public static MyroBot getInstance()
	{
		return instance;
	}
}
