package fr.irisa.cairn.xtext.utils.standalone;

/**
 * Helper to map a plugin URI to its nsURI.
 * 
 * @author antoine
 * 
 */
public class PluginMapping {
	String plugin;
	String nsuri;

	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public String getNsuri() {
		return nsuri;
	}

	public void setNsuri(String nsuri) {
		this.nsuri = nsuri;
	}

}