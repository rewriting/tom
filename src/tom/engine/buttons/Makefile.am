#
#    TOM - To One Matching Compiler
#
#    Copyright (C) 2000-2001  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
#			     Nancy, France.
#
#    This program is free software; you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation; either version 2 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program; if not, write to the Free Software
#    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
#
#    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
#

buttonsdir = $(datadir)/buttons

BUTTONS      = rb.gif rbp.gif rbr.gif rbrs.gif rbs.gif
BUTTONS_SRC  = $(foreach b, ${BUTTONS}, ${srcdir}/$b)

EXTRA_DIST   = $(BUTTONS)
buttons_DATA = $(BUTTONS)

installdir   = ${prefix}/classes/jtom/buttons

install-data-hook:
	$(mkinstalldirs) $(installdir)
	${INSTALL} $(BUTTONS_SRC) $(installdir)

bootinstall:
