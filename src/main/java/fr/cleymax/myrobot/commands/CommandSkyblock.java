package fr.cleymax.myrobot.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.cleymax.myrobot.MyroBot;
import fr.cleymax.myrobot.api.NameFetcher;
import fr.cleymax.myrobot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.hypixel.api.HypixelAPI;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * File <b>CommandSkyblock</b> located on fr.cleymax.myrobot CommandSkyblock is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 10/11/2019 at 21:02
 */

public class CommandSkyblock extends Command {

	private final MyroBot main;

	public CommandSkyblock(MyroBot myroBot)
	{
		super("skyblock", new String[]{"hskyblock", "sb"}, true);
		this.main = myroBot;
	}

	@Override
	public void execute(MessageReceivedEvent event, User user, String[] args)
	{
		if (args.length == 0)
		{
			sendHelp(event);
			return;
		}
		HypixelAPI hypixelAPI = this.main.getHypixelAPI();

		if (args.length == 1)
		{
			switch (args[0].toLowerCase())
			{
				case "new":
				case "news":
					try
					{
						EmbedBuilder builder = new EmbedBuilder().setColor(new Color(16761401))
								.setTitle("**Hypixel**: *Skyblock* News")
								.setThumbnail("https://www.cleymax.fr/assets/logo-hypixel.png")
								.setFooter("Éxecuté par " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null);

						JsonArray news = hypixelAPI.getSkyBlockNews().get().getItems();
						news.forEach(jsonElement -> {
							JsonObject new_ = jsonElement.getAsJsonObject();
							builder.appendDescription("[" + new_.get("title").getAsString() + "](" + new_.get("link").getAsString() + ")\n → " + new_.get("text").getAsString().replaceAll("\n", " ") + "\n\n");
						});
						event.getChannel().sendMessage(builder.build()).queue();
					}
					catch (InterruptedException | ExecutionException e)
					{
						event.getChannel().sendMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
					}
					break;
				default:
					sendHelp(event);
					break;
			}
		}
		switch (args[0].toLowerCase())
		{
			case "profiles":
			case "profile":
			case "profil":
				try
				{
					EmbedBuilder builder = new EmbedBuilder().setColor(new Color(16761401))
							.setTitle("**Hypixel**: *Skyblock* Profiles")
							.setThumbnail("https://www.cleymax.fr/assets/logo-hypixel.png")
							.setFooter("Éxecuté par " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null);

					JsonObject player   = hypixelAPI.getPlayerByName(args[1]).get().getPlayer();
					JsonObject stats    = player.get("stats").getAsJsonObject();
					JsonObject skyblock = stats.get("SkyBlock").getAsJsonObject();
					JsonObject profiles = skyblock.get("profiles").getAsJsonObject();
					profiles.entrySet().forEach(entry -> {
						JsonObject profile = entry.getValue().getAsJsonObject();
						builder.appendDescription(" - **" + profile.get("cute_name").getAsString() + "** (id: _" + profile.get("profile_id").getAsString() + "_)\n");
					});
					event.getChannel().sendMessage(builder.build()).queue();
				}
				catch (InterruptedException | ExecutionException e)
				{
					event.getChannel().sendMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
				}
				break;
			case "user":
			case "users":
			case "joueur":
			case "joueurs":
			case "player":
			case "players":
				String profil = "";
				if (args.length == 3)
					profil = args[2];

				try
				{
					if (profil.equals(""))
					{
						JsonObject player   = hypixelAPI.getPlayerByName(args[1]).get().getPlayer();
						JsonObject stats    = player.get("stats").getAsJsonObject();
						JsonObject skyblock = stats.get("SkyBlock").getAsJsonObject();
						JsonObject profiles = skyblock.get("profiles").getAsJsonObject();
						if (profiles.entrySet().iterator().hasNext())
						{
							profil = profiles.entrySet().iterator().next().getKey();
						} else
						{
							event.getChannel().sendMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
							return;
						}
					}
					JsonObject        profile = hypixelAPI.getSkyBlockProfile(profil).get().getProfile();
					Map<String, UUID> members = new HashMap<>();
					profile.get("members").getAsJsonObject().entrySet().forEach(e -> {
						System.out.println("Member: " + fromTrimmed(e.getKey()).toString());
						try
						{
							members.put(new NameFetcher(fromTrimmed(e.getKey())).call(),fromTrimmed(e.getKey()));
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							event.getChannel().sendMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
						}
					});
					EmbedBuilder builder = new EmbedBuilder().setColor(new Color(16761401))
							.setTitle("**Hypixel**: *Skyblock* Profiles")
							.setThumbnail("https://www.cleymax.fr/assets/logo-hypixel.png")
							.setFooter("Éxecuté par " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null);

					builder.appendDescription("**Membres:** (" + members.size() + ")\n");
					StringBuilder b = new StringBuilder();
					members.forEach((s, uuid) -> b.append(s + ", "));
					builder.appendDescription("  " + b.toString());
					event.getChannel().sendMessage(builder.build()).queue();
				}
				catch (InterruptedException | ExecutionException e)
				{
					e.printStackTrace();
					event.getChannel().sendMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
				}
				break;
		}

	}

	public UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException
	{
		if (trimmedUUID == null) throw new IllegalArgumentException();
		StringBuilder builder = new StringBuilder(trimmedUUID.trim());
		try
		{
			builder.insert(20, "-");
			builder.insert(16, "-");
			builder.insert(12, "-");
			builder.insert(8, "-");
		}
		catch (StringIndexOutOfBoundsException e)
		{
			throw new IllegalArgumentException();
		}

		return UUID.fromString(builder.toString());
	}

	private void sendHelp(MessageReceivedEvent event)
	{
		event.getChannel().sendMessage(new EmbedBuilder()
				.setColor(new Color(16761401))
				.setThumbnail("https://www.cleymax.fr/assets/icons-hypixel.png")
				.setTitle("Aide: *.skyblock*")
				.addField(".skyblock help", "Besoin d'aide pour cette commande ?", false)
				.addField(".skyblock news", "Afficher les nouveautés du Skyblock !", false)
				.build()
		).queue();
	}
}
