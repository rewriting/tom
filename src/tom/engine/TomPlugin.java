package jtom;

import aterm.*;

/**
 * This interface must be implemented by all Tom plugins.
 * It provides methods to access options (inherited from the
 * TomPluginOptions interface) as well as methods to run
 * the compilation (one to "feed" the plugin, another one
 * to run it and a third one to retrieve the processed term).
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public interface TomPlugin extends TomPluginOptions
{
    /**
     * Sets the input of the plugin.
     *
     * @param term the input ATerm
     */
    public abstract void setTerm(ATerm term);

    /**
     * Retrieves the output of the plugin.
     *
     * @return the output ATerm
     */
    public abstract ATerm getTerm();

    /**
     * Runs the plugin.
     */
    public abstract void run();
}
