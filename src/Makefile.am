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
#    Christophe Ringeissen	e-mail: Christophe.Ringeissen@loria.fr
#    Marian Vittek		e-mail: vittek@guma.ii.fmph.uniba.sk
#

SUBDIRS = jtom

EXTRA_DIST = TODO ALGO 

bootstep:
	@$(MAKE) clean
	@$(MAKE) 
	@$(MAKE) install


BOOTSTRAPDIR     = jtom
BOOTSTRAPDIRCOPY = /tmp/jtom.bootstrap

bootstrap:
	@echo
	@echo "BOOTSTRAPPING PHASE 1"
	@echo
	$(MAKE) bootstep

	@echo
	@echo "BOOTSTRAPPING PHASE 2"
	@echo
	$(MAKE) bootstep
	rm -rf $(BOOTSTRAPDIRCOPY)
	cp -rp $(BOOTSTRAPDIR) $(BOOTSTRAPDIRCOPY)

	@echo
	@echo "BOOTSTRAPPING PHASE 3"
	@echo
	$(MAKE) bootstep

	@echo
	@echo "BOOTSTRAPPING CHECK"
	@echo "-------------------"
	diff -r $(BOOTSTRAPDIR) $(BOOTSTRAPDIRCOPY)
	@rm -rf $(BOOTSTRAPDIRCOPY)

bootinstall :
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

