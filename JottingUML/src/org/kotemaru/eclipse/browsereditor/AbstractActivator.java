package org.kotemaru.eclipse.browsereditor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * 基本、自動生成のまま。
 * @author @kotemaru.org
 */
public abstract class AbstractActivator extends AbstractUIPlugin {

	// The shared instance
	private static AbstractActivator plugin;
	
	/**
	 * PluginIDを返す。通常はActivatorのフルクラス名。
	 * @return
	 */
	public abstract String getPluginId();
	
	/**
	 * Preference実装クラスを返す。
	 * @return
	 */
	public abstract AbstractPreference getPreference();
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static AbstractActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(getDefault().getPluginId(), path);
	}
}
