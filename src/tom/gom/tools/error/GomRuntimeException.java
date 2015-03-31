/*
 * Gom
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.tools.error;

public class GomRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public GomRuntimeException() {
    super();
  }

  public GomRuntimeException(String arg0) {
    super(arg0);
  }

  public GomRuntimeException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public GomRuntimeException(Throwable arg0) {
    super(arg0);
  }
}
