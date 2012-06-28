package tom.mapping.dsl.launch;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String	PLUGIN_ID	= "fr.irisa.cairn.graphAdapter.launch"; //$NON-NLS-1$

	// The shared instance
	private static Activator	plugin;

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return Activator.plugin;
	}

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		// plugin.ge

		// IEditorPart activeEditor =
		// plugin.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		// plugin.getWorkbench().getActiveWorkbenchWindow().setActivePage(plugin.getWorkbench().getActiveWorkbenchWindow().getActivePage());
		// plugin.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		// .showView(IConsoleConstants.ID_CONSOLE_VIEW);
		Activator.plugin.getWorkbench().getDisplay();

		// GraphAdapterConsole.out.display(plugin.getWorkbench().getActiveWorkbenchWindow().getActivePage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		Activator.plugin = null;
		super.stop(context);
	}

}
