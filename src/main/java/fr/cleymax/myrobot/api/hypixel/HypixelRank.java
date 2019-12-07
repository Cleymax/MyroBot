package fr.cleymax.myrobot.api.hypixel;

import java.util.Arrays;

/**
 * File <b>HypixelRank</b> located on fr.cleymax.myrobot.api.hypixel HypixelRank is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 02/10/2019 at 20:26
 */

public enum HypixelRank {

	PLAYER("Joueur"),
	VIP("Vip"),
	VIP_PLUS("Vip+"),
	MVP("Mvp"),
	MVP_PLUS("Mvp+");


	private String displayName;

	HypixelRank(String displayName)
	{
		this.displayName = displayName;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public static HypixelRank getRankByKey(String key)
	{
		return Arrays.stream(values()).filter(rank -> rank.name().equalsIgnoreCase(key)).findFirst().orElse(PLAYER);
	}
}