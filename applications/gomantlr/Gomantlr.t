/*
 *
 * Gomantlr
 *
 * Copyright (c) 2006-2014, Universite de Lorraine, Inria
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
 * Eric Deplagne <Eric.Deplagne@loria.fr>
 *
 **/

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;

import org.antlr.tool.GrammarAST;
import org.antlr.tool.Grammar;
import org.antlr.tool.ANTLRLexer;
import org.antlr.tool.ANTLRParser;

import org.antlr.Tool;

import antlr.TokenStreamRewriteEngine;

import aterm.*;
import aterm.pure.*;

import antlrgrammar.types.AntlrGrammar;

import tom.gom.adt.gom.types.GomModule;

import java.io.*;

import utils.AST2ATerm;
//import utils.Tree2ATerm;
import utils.PrintGomModule;

import aterm2antlrgrammar.ATerm2AntlrGrammar;

import aterm2antlrgrammar.exceptions.AntlrWrongGrammarException;

import gomantlr.GenerateGom;

public class Gomantlr {

    //%include { antlrgrammar/AntlrGrammar.tom }
    //%include { adt/gom/Gom.tom}

    public static void main(String[] args) {
        try {
            File grammarFile = new File(args[0]);

            String grammarFileBaseName = grammarFile.getName();

            String grammarFilePath = grammarFile.getParent();
            if(grammarFilePath==null) {
                grammarFilePath="";
            }

            String grammarFileName = grammarFilePath+grammarFileBaseName+".g";
            String gomFileName = grammarFilePath+"Gomantlr_"+grammarFileBaseName+".gom";
            
            FileReader fr = null;
            try {
                fr = new FileReader(grammarFileName);
            }
            catch (IOException ioe) {
                System.err.println("IO exception on file "+grammarFileName+": " + ioe);
            }
            
            BufferedReader br = new BufferedReader(fr);
            String toolArgs[]= new String[1];
            toolArgs[0]=grammarFileName;
            
            Tool tool=new Tool(toolArgs);
            
            Grammar grammar=new Grammar(tool,grammarFileName,br);
            Grammar lexerGrammar=new Grammar(grammar.getLexerGrammar());
            
            GrammarAST ast=grammar.getGrammarTree();
            GrammarAST lexerAST=lexerGrammar.getGrammarTree();
            
            ATerm aterm=AST2ATerm.getATerm(ast,ANTLRParser._tokenNames);
            ATerm lexerATerm=AST2ATerm.getATerm(lexerAST,ANTLRParser._tokenNames);

            //System.out.println(aterm);
            //System.out.println(lexerATerm);

            AntlrGrammar antlrGrammar;
            try {
                antlrGrammar=ATerm2AntlrGrammar.getAntlrGrammar(aterm);
            } catch (AntlrWrongGrammarException e) {
                antlrGrammar=e.getAntlrGrammar();
            }

            AntlrGrammar antlrLexerGrammar;
            try {
                antlrLexerGrammar=ATerm2AntlrGrammar.getAntlrGrammar(lexerATerm);
            } catch (AntlrWrongGrammarException e) {
                antlrLexerGrammar=e.getAntlrGrammar();
            }

            //System.out.println(antlrGrammar);
            //System.out.println(antlrLexerGrammar);

            GomModule gomModule=GenerateGom.generateGom(antlrGrammar);
            
            FileOutputStream outputStream=new FileOutputStream(gomFileName);
            PrintStream printStream=new PrintStream(outputStream);

            PrintGomModule.printGomModule(gomModule,printStream);
        } catch (Exception e) {
            System.err.println("exception: " + e);
            e.printStackTrace();
        }
    }
}
