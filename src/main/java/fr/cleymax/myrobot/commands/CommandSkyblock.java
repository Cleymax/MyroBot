package fr.cleymax.myrobot.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.cleymax.myrobot.MyroBot;
import fr.cleymax.myrobot.api.NameFetcher;
import fr.cleymax.myrobot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.hypixel.api.HypixelAPI;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * File <b>CommandSkyblock</b> located on fr.cleymax.myrobot CommandSkyblock is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 10/11/2019 at 21:02
 * Revue par Bierque Jason (DoctaEnkoda)
 */

public class CommandSkyblock extends Command {

	private static final String  HYPIXEL_ICONS_URL = "https://www.cleymax.fr/assets/icons-hypixel.png";
	private final        MyroBot main;

	public CommandSkyblock(MyroBot myroBot)
	{
		super("skyblock", new String[]{"hskyblock", "sb"}, true);
		this.main = myroBot;
	}

	@Override
	public void execute(MessageReceivedEvent event, User user, String[] args)
	{
		HypixelAPI hypixelAPI = this.main.getHypixelAPI();

        if (args.length == 0) {  sendHelp(event); return };

        switch (args[0].toLowerCase()) {
            case "new":
			case "news":
				EmbedBuilder builder = new EmbedBuilder().setColor(new Color(16761401))
					.setTitle("**Hypixel**: *Skyblock* News")
					.setThumbnail("https://www.cleymax.fr/assets/logo-hypixel.png")
					.setFooter(getFooter(event.getAuthor()), null);

				JsonArray news = hypixelAPI.getSkyBlockNews().join().getItems();
				news.forEach(jsonElement -> {
					JsonObject aNews = jsonElement.getAsJsonObject();
					builder.appendDescription("[" + aNews.get("title").getAsString() + "](" + aNews.get("link").getAsString() + ")\n → " + aNews.get("text").getAsString().replaceAll("\n", " ") + "\n\n");
				});
				event.getChannel().sendMessage(builder.build()).queue();
            break;
            case "profile":
			case "profil":
				EmbedBuilder builder = new EmbedBuilder().setColor(new Color(16761401))
					.setTitle("**Hypixel**: *Skyblock* Profiles")
					.setThumbnail("https://www.cleymax.fr/assets/logo-hypixel.png")
					.setFooter(getFooter(event.getAuthor()), null);

				JsonObject player = hypixelAPI.getPlayerByName(args[1]).join().getPlayer();
				JsonObject stats = player.get("stats").getAsJsonObject();
				JsonObject skyblock = stats.get("SkyBlock").getAsJsonObject();
				JsonObject profiles = skyblock.get("profiles").getAsJsonObject();
				profiles.entrySet().forEach(entry -> {
					JsonObject profile = entry.getValue().getAsJsonObject();
					builder.appendDescription(" - **" + profile.get("cute_name").getAsString() + "** (id: _" + profile.get("profile_id").getAsString() + "_)\n");
				});
				event.getChannel().sendMessage(builder.build()).queue();
			break;
			case "user":
			case "users":
			case "joueur":
			case "joueurs":
			case "player":
			case "players":
				String profil = "";
				if (args.length == 3) { profil = args[2]; }
				if (profil.equals("")) {
					JsonObject playerO   = hypixelAPI.getPlayerByName(args[1]).join().getPlayer();
					JsonObject statsO    = playerO.get("stats").getAsJsonObject();
					JsonObject skyblockO = statsO.get("SkyBlock").getAsJsonObject();
					JsonObject profilesO = skyblockO.get("profiles").getAsJsonObject();
					if (profilesO.entrySet().iterator().hasNext()) {
                        profil = profilesO.entrySet().iterator().next().getKey();
                    } else { 
                        sendError(event.getChannel());
						return;
					}
				}
				JsonObject profile = hypixelAPI.getSkyBlockProfile(profil).join().getProfile();
				Map<String, UUID> members = new HashMap<>();
				profile.get("members").getAsJsonObject().entrySet().forEach(e -> {
					try {
						members.put(new NameFetcher(fromTrimmed(e.getKey())).call(), fromTrimmed(e.getKey()));
					} catch (Exception ex) {
                        sendError(event.getChannel());
                        return;
					}
				});
				EmbedBuilder embedBuilder = new EmbedBuilder().setColor(new Color(16761401))
					.setTitle("**Hypixel**: *Skyblock* Profiles")
					.setThumbnail(HYPIXEL_ICONS_URL)
					.setFooter(getFooter(event.getAuthor()), null);

				embedBuilder.appendDescription("**Membres:** (" + members.size() + ")\n");
				StringBuilder b = new StringBuilder();
				members.forEach((s, uuid) -> b.append(s).append(", "));
				embedBuilder.appendDescription("  " + b.toString());
				event.getChannel().sendMessage(embedBuilder.build()).queue();
			break;
        
            default:
                sendHelp(event);
             break;
        }
	}

	private String getFooter(User author)
	{
		return "Éxecuté par " + author.getAsTag();
	}

	private void sendError(MessageChannel channel)
	{
		channel.sendMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
	}

	public UUID fromTrimmed(String trimmedUUID)
	{
		if (trimmedUUID == null) throw new IllegalArgumentException();
		StringBuilder builder = new StringBuilder(trimmedUUID.trim());
		try {
			builder.insert(20, "-");
			builder.insert(16, "-");
			builder.insert(12, "-");
			builder.insert(8, "-");
		} catch (StringIndexOutOfBoundsException e) {
			throw new IllegalArgumentException();
		}

		return UUID.fromString(builder.toString());
	}

	private void sendHelp(MessageReceivedEvent event) {
		event.getChannel().sendMessage(new EmbedBuilder()
				.setColor(new Color(16761401))
				.setThumbnail(HYPIXEL_ICONS_URL)
				.setTitle("Aide: *.skyblock*")
				.addField(".skyblock help", "Besoin d'aide pour cette commande ?", false)
				.addField(".skyblock news", "Afficher les nouveautés du Skyblock !", false)
				.build()
		).queue();
	}
}
