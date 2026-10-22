/*

    TOM - To One Matching Compiler

    Copyright (C) 2004-2015 Inria Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
	  Julien Guyon			e-mail: Julien.Guyon@loria.fr
	  Bertrand Tavernier (CRIL Technology)

 */
package fr.loria.eclipse.gom.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * 
 * This class is a Rule to find expressions like "MyType = What I want" , to color the MyType word
 * @author Martin GRANDCOLAS 
 *
 */
public class TypeRule implements IRule {

	private IToken fToken;

	public TypeRule(IToken token) {
		fToken = token;
	}

	public IToken evaluate(ICharacterScanner scanner) {
		
		int c = scanner.read();
        if(Character.isLetter(c)) {
            int size = 0;
            do {
                c = scanner.read();
                ++size;
            } while(Character.isLetter(c));
            do {
                c = scanner.read();
                ++size;
            } while(Character.isWhitespace(c));
            if(c == '=' ) {
            	scanner.unread();
                return fToken;
            }
            for( ; size > 0 ; --size) {
                scanner.unread();
            }
        }
        scanner.unread();
        return Token.UNDEFINED;
	}

}
