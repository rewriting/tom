/*
 *
 * This interface contains the options related methods of TomPlugin.
 *
 */

package jtom;

import jtom.adt.tomsignature.types.*;

public interface TomPluginOptions
{
    public abstract OptionList declareOptions();
    public abstract OptionList requiredOptions();
    public abstract void setOption(String name, String value);
}
