package fr.cleymax.myrobot.api.hypixel;

import java.util.Arrays;

/**
 * File <b>HypixelLang</b> located on fr.cleymax.myrobot.api.hypixel HypixelLang is a part of MyroBot.
 * <p>
 * Copyright (c) 2019 MyroBot .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 02/10/2019 at 20:34
 */

public enum HypixelLang {

	ENGLISH("Anglais"),
	FRENCH("Français");


	private String displayName;

	HypixelLang(String displayName)
	{
		this.displayName = displayName;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public static HypixelLang getLangByKey(String key)
	{
		return Arrays.stream(values()).filter(lang -> lang.name().equalsIgnoreCase(key)).findFirst().orElse(ENGLISH);
	}
}
