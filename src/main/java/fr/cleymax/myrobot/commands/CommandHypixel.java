package fr.cleymax.myrobot.commands;


import com.google.gson.JsonObject;
import fr.cleymax.myrobot.MyroBot;
import fr.cleymax.myrobot.api.MinecraftServer;
import fr.cleymax.myrobot.api.NameFetcher;
import fr.cleymax.myrobot.api.hypixel.HypixelLang;
import fr.cleymax.myrobot.api.hypixel.HypixelRank;
import fr.cleymax.myrobot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.hypixel.api.reply.GameCountsReply;
import net.hypixel.api.reply.GuildReply;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.reply.WatchdogStatsReply;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

/**
 * File <b>CommandHypixel</b> located on fr.cleymax.myrobot.commands CommandHypixel is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 01/10/2019 at 22:13
 */

public class CommandHypixel extends Command {

	private static final String  HYPIXEL_ICONS_URL = "https://www.cleymax.fr/assets/icons-hypixel.png";
	private static final String  HYPIXEL_ERROR_MSG = "Can't get information from hypixel api !";
	private              MyroBot main;

	public CommandHypixel(MyroBot main)
	{
		super("hypixel", true);
		this.main = main;
	}

	@Override
	public void execute(MessageReceivedEvent event, User user, String[] args)
	{
		switch (args.length)
		{
			case 0:
				event.getChannel().sendMessage(new EmbedBuilder().setDescription(main.getLoadingEmote().getAsMention() + " Chargement des données ...").build()).queue(message -> MinecraftServer.api("mc.hypixel.net", minecraftServer -> {
					if (minecraftServer.getPlayers() != null)
						message.editMessage(
								new EmbedBuilder()
										.setTitle("**Hypixel**")
										.setDescription(" \nIP: `mc.hypixel.net`\nVersion: `1.8.X → 1.14.X`\nCraft: `OFF`\n\nSite Web: [hypixel.net](https://hypixel.net/)\nForum: [hypixel.net/forums](https://hypixel.net/forums/)\nBoutique: [store.hypixel.net](https://store.hypixel.net/)\n\nConnectés: `" + minecraftServer.getPlayers().getOnline() + "` / `" + minecraftServer.getPlayers().getMax() + "` ")
										.setColor(new Color(16761401))
										.setFooter("Éxecuté par " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null)
										.setThumbnail(HYPIXEL_ICONS_URL)
										.build()
						).queue();
					else editWithError(message);
				}));
				break;
			case 1:
				switch (args[0].toLowerCase())
				{
					case "count":
					case "connected":
					case "connect":
					case "co":
						try
						{
							event.getChannel().sendMessage(new EmbedBuilder().setDescription("Joueur connectés sur Hypixel: **" + main.getHypixelAPI().getPlayerCount().get().getPlayerCount() + "**").build()).queue();
						}
						catch (InterruptedException | ExecutionException e)
						{
							main.getLogger().log(Level.SEVERE, HYPIXEL_ERROR_MSG, e);
							sendError(event.getChannel());
							Thread.currentThread().interrupt();
						}
						break;
					case "boosters":
					case "booster":
					case "boost":
						try
						{
							EmbedBuilder builder = new EmbedBuilder();
							builder.setTitle("**Hypixel**: *Boosters*");
							main.getHypixelAPI().getBoosters().get().getBoosters().forEach(booster -> builder.addField(booster.getGameType().getName(), booster.getAmount() + "%", true));
							event.getChannel().sendMessage(builder.build()).queue();
						}
						catch (InterruptedException | ExecutionException e)
						{
							main.getLogger().log(Level.SEVERE, HYPIXEL_ERROR_MSG, e);
							sendError(event.getChannel());
							Thread.currentThread().interrupt();
						}
						break;
					case "watchdog":
					case "watchdogs":
					case "anticheat":
					case "watch":
						try
						{
							EmbedBuilder       builder = new EmbedBuilder();
							WatchdogStatsReply w       = main.getHypixelAPI().getWatchdogStats().get();
							builder.setTitle("**Hypixel**: *WatchDog*");
							builder.setDescription("Total de ban: **" + w.getWatchdogTotal() + "**\n");
							builder.appendDescription("Total de ban des dernières minutes: **" + w.getWatchdogLastMinute() + "**\n");
							builder.appendDescription("Total de ban aujourd'hui: **" + w.getWatchdogRollingDaily() + "**\n\n");
							builder.appendDescription("Total de ban par le Staff: **" + w.getStaffTotal() + "**\n");
							builder.appendDescription("Total de ban par le Staff aujourd'hui: **" + w.getStaffRollingDaily() + "**");
							builder.setThumbnail(HYPIXEL_ICONS_URL);

							event.getChannel().sendMessage(builder.build()).queue();
						}
						catch (InterruptedException | ExecutionException e)
						{
							main.getLogger().log(Level.SEVERE, HYPIXEL_ERROR_MSG, e);
							sendError(event.getChannel());
							Thread.currentThread().interrupt();
						}
						break;
					case "server":
					case "serveur":
					case "serveurs":
					case "srv":
					case "servers":
						try
						{
							GameCountsReply gc      = main.getHypixelAPI().getGameCounts().get();
							EmbedBuilder    builder = new EmbedBuilder();
							builder.setTitle("**Hypixel**: *Serveurs*");
							StringBuilder b = new StringBuilder();
							gc.getGames().forEach((s, gameCount) -> b.append("- ").append(StringUtils.capitalize(s.toLowerCase())).append(": `").append(gameCount.getPlayers()).append("`\n"));
							builder.setDescription(b.toString());
							event.getChannel().sendMessage(builder.build()).queue();
						}
						catch (InterruptedException | ExecutionException e)
						{
							main.getLogger().log(Level.SEVERE, HYPIXEL_ERROR_MSG, e);
							sendError(event.getChannel());
							Thread.currentThread().interrupt();
						}
						break;
					default:
						sendHelp(event);
						break;
				}
				break;
			default:
				switch (args[0].toLowerCase())
				{
					case "g":
					case "guild":
						try
						{
							GuildReply g = main.getHypixelAPI().getGuildByName(args[1]).get();
							if (g.getGuild() == null)
							{
								event.getChannel().sendMessage(new EmbedBuilder().setDescription("Guild non trouvé sur Hypixel !").setColor(Color.RED).build()).queue();
								return;
							}
							GuildReply.Guild guild = g.getGuild();

							EmbedBuilder builder = new EmbedBuilder();
							builder.setDescription("**Hypixel**: *Guild* [" + g.getGuild().getName() + "](https://hypixel.net/guilds/" + g.getGuild().getName() + "/)\n\n");
							builder.appendDescription("Description: *" + (guild.getDescription() == null ? "Aucune" : guild.getDescription()) + "*\n");
							builder.appendDescription("Tag: *" + (guild.getTag() == null ? "Aucun" : guild.getTag()) + "*\n");
							builder.appendDescription("Coins: *" + guild.getCoins() + "*\n");
							builder.appendDescription("Xp: *" + guild.getExp() + "*\n");
							builder.appendDescription("Joignable: *" + translate(guild.getJoinable()) + "*\n\n");
							builder.appendDescription("Membres: (" + guild.getMembers().size() + ")\n");
							StringBuilder b = new StringBuilder();
							guild.getMembers().forEach(member -> b.append(member.getRank() != null ? "[" + member.getRank() + "] " : "").append(getPlayerName(member.getUuid())).append(", "));
							builder.appendDescription("  " + b.toString());
							event.getChannel().sendMessage(builder.build()).queue();
						}
						catch (InterruptedException | ExecutionException e)
						{
							main.getLogger().log(Level.SEVERE, HYPIXEL_ERROR_MSG, e);
							sendError(event.getChannel());
							Thread.currentThread().interrupt();
						}
						break;
					case "user":
					case "player":
						try
						{
							DecimalFormat        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
							DecimalFormatSymbols symbols   = formatter.getDecimalFormatSymbols();

							symbols.setGroupingSeparator(' ');
							formatter.setDecimalFormatSymbols(symbols);

							PlayerReply player = main.getHypixelAPI().getPlayerByName(args[1]).get();

							if (player == null || player.getPlayer() == null)
							{
								event.getChannel().sendMessage(new EmbedBuilder().setDescription("Le joueur n'a pas été trouvé !").setColor(Color.RED).build()).queue();
								return;
							}

							String sUuid        = player.getPlayer().get("uuid").getAsString();
							double networkExp   = player.getPlayer().has("networkExp") ? player.getPlayer().get("networkExp").getAsDouble() : 0;
							double karma        = player.getPlayer().has("karma") ? player.getPlayer().get("karma").getAsInt() : 0;
							String userLanguage = player.getPlayer().has("userLanguage") ? player.getPlayer().get("userLanguage").getAsString() : "Anglais";
							String rank         = player.getPlayer().has("newPackageRank") ? player.getPlayer().get("newPackageRank").getAsString() : "PLAYER";

							EmbedBuilder embedBuilder = new EmbedBuilder()
									.setDescription("\nExperience: `" + formatter.format(networkExp) + "`\nLevel: `" + Math.floor(getLevelFromExp((long) networkExp)) + "`\nKarma: `" + formatter.format(karma) + "`\nLangue: `" + HypixelLang.getLangByKey(userLanguage).getDisplayName() + "`\nRank Shop: `" + HypixelRank.getRankByKey(rank).getDisplayName() + "`")
									.setColor(new Color(16761401))
									.setFooter("Éxecuté par " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null)
									.setThumbnail("https://www.cleymax.fr/assets/logo-hypixel.png")
									.setAuthor(args[1], null, "https://crafatar.com/avatars/" + sUuid);

							final String key = "socialMedia";

							if (player.getPlayer().has(key) && player.getPlayer().get(key).getAsJsonObject().has("links"))
							{
								embedBuilder.appendDescription("\n\nRéseaux sociaux:\n");
								JsonObject socialMedia = player.getPlayer().get(key).getAsJsonObject().get("links").getAsJsonObject();
								socialMedia.entrySet().forEach(e -> embedBuilder.appendDescription("- " + StringUtils.capitalize(e.getKey().toLowerCase()) + ": **" + e.getValue().getAsString() + "**\n"));
							}
							event.getChannel().sendMessage(embedBuilder.build()).queue();
						}
						catch (InterruptedException | ExecutionException e)
						{
							main.getLogger().log(Level.SEVERE, HYPIXEL_ERROR_MSG, e);
							sendError(event.getChannel());
							Thread.currentThread().interrupt();
						}
						break;
					default:
						sendHelp(event);
						break;
				}
				break;
		}
	}

	private String translate(Boolean bol)
	{
		return (Boolean.TRUE.equals(bol) ? "oui" : "non");
	}

	private void sendError(MessageChannel channel)
	{
		channel.sendMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
	}

	private void editWithError(Message message)
	{
		message.editMessage(new EmbedBuilder().setDescription("Erreur lors de la réception des données !").setColor(Color.RED).build()).queue();
	}

	public double getLevelFromExp(long exp)
	{
		return (exp < 0 ? 1 : Math.floor(1 + -3.5 + Math.sqrt(12.25 + 0.0008 * exp)));
	}


	private String getPlayerName(UUID uuid)
	{
		if (!main.getNameCache().containsKey(uuid))
		{
			try
			{
				String name = new NameFetcher(uuid).call();
				if (name == null)
					return "Pseudo non trouvé";

				main.getNameCache().put(uuid, name);
				return name;
			}
			catch (Exception e)
			{
				return "Pseudo non trouvé";
			}
		} else
		{
			return main.getNameCache().get(uuid);
		}
	}

	private void sendHelp(MessageReceivedEvent event)
	{
		event.getChannel().sendMessage(new EmbedBuilder()
				.setColor(16761401)
				.setThumbnail(HYPIXEL_ICONS_URL)
				.setTitle("Aide: *.hypixel*")
				.addField(".hypixel help", "Besoin d'aide pour cette commande ?", false)
				.addField(".hypixel server", "Liste des joueurs sur chaque type de serveur.", false)
				.addField(".hypixel count", "Nombre de connectés sur le serveur en ce moment.", false)
				.addField(".hypixel booster", "Liste de tous les boosters actifs sur le serveur.", false)
				.addField(".hypixel watchdog", "Statistique de l'anticheat.", false)
				.addField(".hypixel guild <nom,id>", "Récupérer les informations sur une guild.", false)
				.addField(".hypixel user <nom>", "Récupérer les informations sur un joueur.", false)
				.build()
		).queue();
	}
}
