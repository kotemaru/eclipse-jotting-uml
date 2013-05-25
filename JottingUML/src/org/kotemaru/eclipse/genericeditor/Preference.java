package org.kotemaru.eclipse.genericeditor;

import org.kotemaru.eclipse.browsereditor.AbstractPreference;

public class Preference extends AbstractPreference {

	public static final String BALLOON = "directionsBalloon";
	public static final String ROUTE_DEFAULT = "lineRouteDefault";
	public static final String FONT_FAMILY = "fontFamily";
	public static final String FONT_FAMILY_P = "fontFamilyP";
	public static final String FONT_FAMILY_M = "fontFamilyM";
	
	public static final Item[] ITEMS = {
		new Item(BALLOON, Boolean.class, true),
		new Item(ROUTE_DEFAULT, String.class, "N"),
		new Item(FONT_FAMILY, String.class, "arial,sans-serif"),
		new Item(FONT_FAMILY_P, String.class, "arial,sans-serif"),
		new Item(FONT_FAMILY_M, String.class, "monospace"),
	};
	
	
	public Preference() {
		this.
	}


	@Override
	public Item[] getItems() {
		return ITEMS;
	}
}