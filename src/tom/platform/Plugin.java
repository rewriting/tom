/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.platform;

import aterm.ATerm;

/**
 * This interface must be implemented by all Tom plugins.
 * It provides methods to access options (inherited from the
 * TomPluginOptions interface) as well as methods to run
 * the compilation (one to "feed" the plugin, another one
 * to run it and a third one to retrieve the processed term).
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public interface Plugin extends OptionOwner
{
    /**
     * Sets the input of the plugin.
     *
     * @param arg the input argument
     */
    public abstract void setArg(Object arg);

    /**
     * Retrieves the output of the plugin.
     *
     * @return the output argument
     */
    public abstract Object getArg();

    /**
     * Runs the plugin.
     */
    public abstract void run();
}
