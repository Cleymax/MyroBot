package fr.cleymax.myrobot.command;

import fr.cleymax.myrobot.MyroBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * File <b>Command</b> located on fr.cleymax.myrobot.command Command is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 01/10/2019 at 21:35
 */

public abstract class Command {

	public static final MyroBot instance = MyroBot.getInstance();
	private String     name;
	private String[]   aliases;
	private Permission permission;
	private boolean    owner, pv;

	public Command(String name, boolean pv)
	{
		this(name, new String[]{}, Permission.MESSAGE_WRITE, pv);
	}

	public Command(String name, String[] aliases, boolean pv)
	{
		this(name, aliases, Permission.MESSAGE_WRITE, pv);
	}

	public Command(String name, String[] aliases, Permission permission)
	{
		this(name, aliases, permission, false);
	}

	public Command(String name, String[] aliases, Permission permission, boolean pv)
	{
		this(name, aliases, permission, false, pv);
	}

	public Command(String name, String[] aliases, Permission permission, boolean owner, boolean pv)
	{
		this.name = name;
		this.aliases = aliases;
		this.permission = permission;
		this.owner = owner;
		this.pv = pv;
	}

	public abstract void execute(MessageReceivedEvent event, User user, String[] args);

	public void deleteIfNotPvMessage(Message message)
	{
		if (message.getChannelType() != ChannelType.PRIVATE)
			message.delete().reason("ExcaliaBot-Delete-Automatic").queue();
	}

	public String getName()
	{
		return name;
	}

	public String[] getAliases()
	{
		return aliases;
	}

	public Permission getPermission()
	{
		return permission;
	}

	public boolean isOwner()
	{
		return owner;
	}

	public boolean isPv()
	{
		return pv;
	}
}
