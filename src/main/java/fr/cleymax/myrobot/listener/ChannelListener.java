package fr.cleymax.myrobot.listener;


import fr.cleymax.myrobot.MyroBot;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * File <b>ChannelListener</b> located on fr.cleymax.myrobot.listener ChannelListener is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 04/10/2019 at 15:39
 */

public final class ChannelListener extends ListenerAdapter {

	private final MyroBot main;

	public ChannelListener(MyroBot main)
	{
		this.main = main;
	}

	@Override
	public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event)
	{
		if (this.main.getConfig().getSettings().contains("channel.public"))
		{
			List<String> publicChannel = this.main.getConfig().getSettings().get("channel.public");
			if (!publicChannel.isEmpty())
			{
				if (publicChannel.contains(event.getChannelJoined().getId()))
				{
					if (needChannel(event, publicChannel))
					{
						event.getGuild().createVoiceChannel("\uD83D\uDCE2 Vocal public ").queue(channel -> {
							int position = event.getChannelJoined().getPosition();
							channel.getManager().setParent(event.getChannelJoined().getParent()).queue(aVoid -> {
								channel.getManager().setPosition(position).queue(aVoid1 -> {
									publicChannel.add(channel.getId());
									this.main.getConfig().getSettings().set("channel.public", publicChannel);
									this.main.getConfig().getSettings().save();
								});
							});
						});
					}
				}
			}
		}
	}

	@Override
	public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event)
	{
		if (this.main.getConfig().getSettings().contains("channel.public"))
		{

			List<String> publicChannel = this.main.getConfig().getSettings().get("channel.public");
			if (!publicChannel.isEmpty())
			{
				if (publicChannel.contains(event.getChannelJoined().getId()))
				{
					if (needChannelMove(event, publicChannel))
					{
						event.getGuild().createVoiceChannel("\uD83D\uDCE2 Vocal public ").queue(channel -> {
							int position = event.getChannelJoined().getPosition();
							channel.getManager().setParent(event.getChannelJoined().getParent()).queue(aVoid -> {
								channel.getManager().setPosition(position).queue(aVoid1 -> {
									publicChannel.add(channel.getId());
									this.main.getConfig().getSettings().set("channel.public", publicChannel);
									this.main.getConfig().getSettings().save();
								});
							});
						});
					}
				}
			}

			if (publicChannel.size() != 1)
			{
				if (publicChannel.contains(event.getChannelLeft().getId()) && event.getChannelLeft().getMembers().isEmpty())
				{
					event.getChannelLeft().delete().queue(o -> {
						publicChannel.remove(event.getChannelLeft().getId());
						this.main.getConfig().getSettings().set("channel.public", publicChannel);
						this.main.getConfig().getSettings().save();
					});
				}
			}
		}
	}

	@Override
	public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event)
	{
		if (this.main.getConfig().getSettings().contains("channel.public"))
		{
			List<String> publicChannel = this.main.getConfig().getSettings().get("channel.public");
			if (publicChannel.size() != 1)
			{
				if (publicChannel.contains(event.getChannelLeft().getId()) && event.getChannelLeft().getMembers().isEmpty())
				{
					event.getChannelLeft().delete().queue(o -> {
						publicChannel.remove(event.getChannelLeft().getId());
						this.main.getConfig().getSettings().set("channel.public", publicChannel);
						this.main.getConfig().getSettings().save();
					});
				}
			}
		}
	}


	private boolean needChannelMove(GuildVoiceMoveEvent event, List<String> publicChannel)
	{
		boolean created = false;
		for (String voiceChannel : publicChannel)
		{
			VoiceChannel vc = event.getGuild().getVoiceChannelById(voiceChannel);
			if (vc != null)
			{
				created = !vc.getMembers().isEmpty();
			}
		}
		return created;
	}

	private boolean needChannel(GuildVoiceJoinEvent event, List<String> publicChannel)
	{
		boolean created = false;
		for (String voiceChannel : publicChannel)
		{
			VoiceChannel vc = event.getGuild().getVoiceChannelById(voiceChannel);
			if (vc != null)
				created = !vc.getMembers().isEmpty();
		}
		return created;
	}
}
