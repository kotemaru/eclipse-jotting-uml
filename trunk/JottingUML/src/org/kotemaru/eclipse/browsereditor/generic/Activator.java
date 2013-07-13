package org.kotemaru.eclipse.browsereditor.generic;

import org.kotemaru.eclipse.browsereditor.AbstractActivator;
import org.kotemaru.eclipse.browsereditor.AbstractPreference;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.kotemaru.eclipse.jottinguml.BrowserPlugin";

	private static Preference preference = new Preference();
	
	@Override
	public String getPluginId() {
		return PLUGIN_ID;
	}

	@Override
	public AbstractPreference getPreference() {
		return preference;
	}
}
