/*
 *
 * This interface must be implemented by all Tom plugins.
 *
 */

package jtom;

import aterm.*;
import jtom.adt.tomsignature.types.*;

public interface TomPlugin extends TomPluginOptions
{
    public abstract void setInput(ATerm term);
    public abstract ATerm getOutput();
    public abstract void run();
}
