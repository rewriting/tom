/*
 *
 * The TomStarterInterface interface. All starters must implement it.
 *
 */

package jtom.starter;

import jtom.*;

public interface TomStarterInterface
{
    public abstract String[] processArguments(String[] argumentList);
    public abstract void extractOptionList();
}
