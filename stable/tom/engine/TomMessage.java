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

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author julien
 */
public class TomMessage {
  
  private final static String RESOURCE_BUNDLE = "jtom.TomMessageResources";
    // Resource bundle.
  private static ResourceBundle resourceBundle =  ResourceBundle.getBundle(RESOURCE_BUNDLE);

    // Message level
  public static int TOM_INFO = 0;
    // Default error line
  public static int DEFAULT_ERROR_LINE_NUMBER = 1; 

  public static String getMessage(String key) {
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException e) {
      return "!" + key + "!";
    }
  }

  public static String getMessage(String key, Object[] details) {
    try {
      return MessageFormat.format(resourceBundle.getString(key), details);
    } catch (MissingResourceException e) {
      return "!" + key + "!";
    }
  }
  
  public static ResourceBundle getResourceBundle() {
    return resourceBundle;
  }
}
