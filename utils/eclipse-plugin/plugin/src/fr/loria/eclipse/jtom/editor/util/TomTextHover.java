/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2004 INRIA
	   			       Nancy, France.

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
	
*/

package fr.loria.eclipse.jtom.editor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import tom.engine.adt.tomoption.types.Option;
import tom.engine.adt.tomoption.types.OptionList;
import tom.engine.adt.tomslot.types.PairNameDeclList;
import tom.engine.adt.tomsignature.types.TomEntry;
import tom.engine.adt.tomsignature.types.TomEntryList;
import tom.engine.adt.tomsignature.types.TomSymbolTable;
import tom.engine.adt.tomname.types.TomName;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.tomtype.types.TomTypeList;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;

import aterm.ATerm;
import aterm.ParseError;
import aterm.pure.SingletonFactory;

public class TomTextHover implements ITextHover {
	
	private static class TomTextHoverType {}
	
	public static final TomTextHoverType SORT_HOVER= new TomTextHoverType();
	public static final TomTextHoverType SIGNATURE_HOVER= new TomTextHoverType();
	
	private long timeStamp;
	private String symbolTableFileName;
	private HashMap<String, String> mapOpToSignature;
	private HashSet<String> sortSet;
	TomTextHoverType type;
	
	public TomTextHover(String fileName, TomTextHoverType type) {
		this.symbolTableFileName = fileName;
		this.type=type;
		initializeKeyMap();
	}

	private void initializeKeyMap() {
		InputStream inputStream = null;
		
		mapOpToSignature = new HashMap<String, String>();
		sortSet = new HashSet<String>();
		if(symbolTableFileName!= null) {
			try {
				inputStream = new FileInputStream(symbolTableFileName);
				File f = new File(symbolTableFileName);
				if(f.exists()) {
					timeStamp = f.lastModified();
				} else {
					timeStamp = -1;
					return;
				}
			
				try {
	
					ATerm term = SingletonFactory.getInstance().readFromFile(inputStream);
					TomEntryList symbols = TomSymbolTable.fromTerm(term).getEntryList();  
						
					TomEntry entry;
					while(!symbols.isEmptyconcTomEntry()) {
						entry = symbols.getHeadconcTomEntry();
						mapOpToSignature.put(entry.getStrName(), findSignature(entry.getStrName(), entry.getAstSymbol()));
						symbols = symbols.getTailconcTomEntry();
					}
				} catch (ParseError e) {
					System.err.println("Error: A parse error occurred in the Symbol table file:"+e);
				} catch (IOException e) {
					System.err.println("Error: Could not read Symbol table from input: " + symbolTableFileName);
				} catch (RuntimeException e) {
					System.err.println("Error: "+e.getMessage());
				}
			
			}
			catch (FileNotFoundException e) {
				System.out.println("Error: Failed to open file: " + symbolTableFileName + " for reading");
				return;
			}
			finally{
				try {
					if (inputStream != null){
						inputStream.close();
					}
				} catch (Exception e) {					
					//e.printStackTrace();
				}
			}
		}
	}
	/**
	 * @param symbol
	 * @return
	 */
	private String findSignature(String symbolName, TomSymbol symbol) {
		String res = "";
		sortSet.add(symbol.getTypesToType().getCodomain().getAstName());
		//System.out.println(symbol.getTypesToType().getCodomain().getString());
		if(isListSymbol(symbol)) {
			res += "List of "+symbol.getTypesToType().getDomain().getHeadconcTomType().getAstName()+": "+symbol.getTypesToType().getCodomain().getAstName();
		} else if(isArraySymbol(symbol)) {
			res += "Array of "+symbol.getTypesToType().getDomain().getHeadconcTomType().getAstName()+": "+symbol.getTypesToType().getCodomain().getAstName();
		} else {
			res += symbolName+"(";
			ArrayList<String> slotAList = new ArrayList<String>();
			PairNameDeclList slotList = symbol.getPairNameDeclList();
			TomName name;
			while(!slotList.isEmptyconcPairNameDecl()) {
				name = slotList.getHeadconcPairNameDecl().getSlotName();
				if(!name.isEmptyName()) {
					slotAList.add(name.getString());
				} else {
					slotAList.add("");
				}
				slotList = slotList.getTailconcPairNameDecl();
			}
			
			int i =0;
			TomTypeList typeList = symbol.getTypesToType().getDomain();
			if(!typeList.isEmptyconcTomType()) {
			  while(!typeList.isEmptyconcTomType()) {
			    res += slotAList.get(i).equals("")?
						typeList.getHeadconcTomType().getAstName()+", ":
						slotAList.get(i)+":"+typeList.getHeadconcTomType().getAstName()+", ";
				  typeList = typeList.getTailconcTomType();
				  i++;
			  }
			  res = res.substring(0, res.length()-2);
			}
			res += "): "+symbol.getTypesToType().getCodomain().getAstName();
		}
		return res;
	}

		/**
	 * @param symbol
	 * @return
	 */
	private boolean isArraySymbol(TomSymbol symbol) {
		if(symbol==null) {
      return false;
    }
		OptionList optionList = symbol.getOptions();
		while(!optionList.isEmptyconcOption()) {
			Option opt = optionList.getHeadconcOption();
      if(opt.isDeclarationToOption() &&
      		(opt.getAstDeclaration().isMakeAddArray() ||
      		 opt.getAstDeclaration().isMakeEmptyArray())) {
      	return true;
      }
      optionList = optionList.getTailconcOption();
		}
		return false;
	}

		/**
	 * @param symbol
	 * @return
	 */
	private boolean isListSymbol(TomSymbol symbol) {
		if(symbol==null) {
      return false;
    }
		OptionList optionList = symbol.getOptions();
		while(!optionList.isEmptyconcOption()) {
			Option opt = optionList.getHeadconcOption();
      if(opt.isDeclarationToOption() &&
      		(opt.getAstDeclaration().isMakeAddList() ||
      		 opt.getAstDeclaration().isMakeEmptyList())) {
      	return true;
      }
      optionList = optionList.getTailconcOption();
		}
		return false;
	}

	/* (non-Javadoc)
	* Method declared on ITextHover
	*/
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		if (hoverRegion != null) {
			try {
				if (hoverRegion.getLength() > 0) {
					reinitializeKeyMap();
					String selection = textViewer.getDocument().get(hoverRegion.getOffset(), hoverRegion.getLength()).trim();
					if (type.equals(SIGNATURE_HOVER)) {
				    //System.out.println("looking for "+textViewer.getDocument().get(hoverRegion.getOffset(), hoverRegion.getLength()).trim());
					  String result =(String)mapOpToSignature.get(selection); 
					  if (result != null){
						  return result;
					  } else {
						  return selection+": no information available";
					  }
					} else if(type.equals(SORT_HOVER)){
						return (sortSet.contains(selection))?"Sort "+selection:selection+": undefined Sort";
					}
				}
			} catch (BadLocationException x) {
			}
		}
		return null;
	}
	
	/**
	 * reinitialize information based on file timestamp
	 */
	private void reinitializeKeyMap() {
		if(timeStamp < new File(symbolTableFileName).lastModified()) {
			initializeKeyMap();
		}
		
	}

	/* (non-Javadoc)
	 * Method declared on ITextHover
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
	    return WordFinder.findWord(textViewer.getDocument(), offset);
	}

} //class TomTextHover
