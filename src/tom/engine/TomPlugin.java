/*
 *
 * This interface must be implemented by all Tom plugins.
 *
 */

package jtom;

import jtom.adt.tomsignature.types.*;

public interface TomPlugin
{
    public abstract void setInput(TomTerm term);
    public abstract TomTerm getOutput();
    public abstract void run();
    public abstract OptionList declareOptions();
    public abstract OptionList requiredOptions();
    public abstract TomServer getServer();
    public abstract void setOption(Option option);
    public abstract void setOption(String name, String value);
}
