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
