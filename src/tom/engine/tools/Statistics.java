/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
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

*/

package jtom.tools;

import java.util.*;

public class Statistics {

  public ArrayList infoParser;
  public ArrayList infoChecker;
  public ArrayList infoVerifier;
  public ArrayList infoCompiler;
  public ArrayList infoGenerator;

  public Statistics() {
    infoParser    = new ArrayList();
    infoChecker   = new ArrayList();
    infoVerifier  = new ArrayList();
    infoCompiler  = new ArrayList();
    infoGenerator = new ArrayList();
  }

    // Checker
  public int numberSlotsExpanded = 0;
  public int numberVariablesDetected = 0;
  public void initInfoChecker() {
      // 6 messages are possibles - each message on 1 line
    infoChecker.add(" - " + numberSlotsExpanded     + " slots expanded");
    infoChecker.add(" - " + numberVariablesDetected + " variables detected");
  }

    // Parser
  public int numberTomBlocsRecognized = 0;
  public int numberMatchRulesRecognized = 0;
  public int numberRuleRulesRecognized = 0; 
  public int numberMatchBlocsRecognized = 0;
  public int numberRuleBlocsRecognized = 0;
  public int numberStrangerBlocsRecognized = 0;
  public void initInfoParser() {
      // 6 messages are possibles - each message on 1 line
    infoParser.add(" - " + numberTomBlocsRecognized + " blocs in TOM language");
    infoParser.add(" - " + numberStrangerBlocsRecognized + " blocs in target language");
    infoParser.add(" - " + numberRuleBlocsRecognized + " %rule blocs");
    infoParser.add(" - " + numberRuleRulesRecognized + " rules recognized in %rule constructs");
    infoParser.add(" - " + numberMatchBlocsRecognized + " %match blocs");
    infoParser.add(" - " + numberMatchRulesRecognized + " rules recognized in %match constructs");
  }

    // Verifier
  public int numberRulesTested = 0;
  public int numberMatchesTested = 0;
  public int numberOperatorDefinitionsTested = 0;
  public int numberTypeDefinitonsTested = 0;
  public int numberApplStructuresTested = 0;
  public void initInfoVerifier() {
      // 6 messages are possibles - each message on 1 line
    infoVerifier.add(" - " + numberTypeDefinitonsTested + " type definitions tested");
    infoVerifier.add(" - " + numberOperatorDefinitionsTested + " operator definitions tested");
    infoVerifier.add(" - " + numberApplStructuresTested + " 'Appl' or 'Variable' structures tested");
    infoVerifier.add(" - " + numberRulesTested + " rules tested in %rule constructs");
    infoVerifier.add(" - " + numberMatchesTested + " rules tested in %match constructs");
  }

    // Compiler
  public int numberRuleSetsTransformed = 0;
  public int numberMakeTermReplaced = 0;
  public int numberMatchCompiledIntoAutomaton = 0;
  public void initInfoCompiler() {
      // 6 messages are possibles - each message on 1 line
    infoCompiler.add(" - " + numberMakeTermReplaced + " %make replaced");
    infoCompiler.add(" - " + numberRuleSetsTransformed + " %rule transformed into Function + Match + MakeTerm");
    infoCompiler.add(" - " + numberMatchCompiledIntoAutomaton + " %match compiled into an automaton");
  }

    // Generator
  public int numberPartsGoalLanguage = 0;
  public int numberIfThenElseTranformed = 0;
  public int numberPartsCopied = 0;
  public void initInfoGenerator() {
      // 6 messages are possibles - each message on 1 line
    infoGenerator.add(" - " + numberPartsGoalLanguage + " parts transformed into the target language");
    infoGenerator.add(" - " + numberIfThenElseTranformed + " 'IfThenElse' parts transformed");
    infoGenerator.add(" - " + numberPartsCopied + " parts directly copied");
  }

  
}
