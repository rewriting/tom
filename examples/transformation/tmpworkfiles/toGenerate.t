%typeterm ResolveWorkDefinitionPlace extends Place {
  implement { ResolveWorkDefinitionPlace }
  is_sort(t) { $t instanceof ResolveWorkDefinitionPlace }
}
=>
`TypeTermDecl(Name(currentTypeName),declarationList,ot);
`TypeTermDecl(Name("ResolveWorkDefinitionPlace"),concDeclaration(IsSortDecl(BQVariable(concOption(OriginTracking(Name("t"),8,"Transfo1.t")),Name("t"),Type(concTypeOption(),"ResolveWorkDefinitionPlace",EmptyTargetLanguageType())),Code(" {0} instanceof ResolveWorkDefinitionPlace "),OriginTracking(Name("is_sort"),8,"Transfo1.t"))),OriginTracking(Name("ResolveWorkDefinitionPlace"),6,"Transfo1.t"))

(Voir fichier astSymbol)


%op Place ResolveWorkDefinitionPlace(o:WorkDefinition, name:String) {
  is_fsym(t) { $t instanceof ResolveWorkDefinitionPlace }
  get_slot(name, t)  { ((ResolveWorkDefinitionPlace)$t).name }
  get_slot(o, t)  { ((ResolveWorkDefinitionPlace)$t).o }
  make(o,name) { new ResolveWorkDefinitionPlace(o,name) }
}
=>
TomSymbol astSymbol = ASTFactory.makeSymbol(name.getText(), `Type(concTypeOption(),type.getText(),EmptyTargetLanguageType()), types, ASTFactory.makePairNameDeclList(pairNameDeclList), options);
`Symbol(Name("ResolveWorkDefinitionPlace"),TypesToType(concTomType(Type(concTypeOption(),"WorkDefinition",EmptyTargetLanguageType()),Type(concTypeOption(),"String",EmptyTargetLanguageType())),Type(concTypeOption(),"Place",EmptyTargetLanguageType())),concPairNameDecl(PairNameDecl(Name("o"),GetSlotDecl(Name("ResolveWorkDefinitionPlace"),Name("o"),BQVariable(concOption(OriginTracking(Name("t"),10,"Transfo1.t")),Name("t"),Type(concTypeOption(),"Place",EmptyTargetLanguageType())),Code(" ((ResolveWorkDefinitionPlace){0}).o "),OriginTracking(Name("get_slot"),10,"Transfo1.t"))),PairNameDecl(Name("name"),GetSlotDecl(Name("ResolveWorkDefinitionPlace"),Name("name"),BQVariable(concOption(OriginTracking(Name("t"),9,"Transfo1.t")),Name("t"),Type(concTypeOption(),"Place",EmptyTargetLanguageType())),Code(" ((ResolveWorkDefinitionPlace){0}).name "),OriginTracking(Name("get_slot"),9,"Transfo1.t")))),concOption(OriginTracking(Name("ResolveWorkDefinitionPlace"),7,"Transfo1.t"),DeclarationToOption(IsFsymDecl(Name("ResolveWorkDefinitionPlace"),BQVariable(concOption(OriginTracking(Name("t"),8,"Transfo1.t")),Name("t"),Type(concTypeOption(),"Place",EmptyTargetLanguageType())),Code(" {0} instanceof ResolveWorkDefinitionPlace "),OriginTracking(Name("is_fsym"),8,"Transfo1.t"))),DeclarationToOption(MakeDecl(Name("ResolveWorkDefinitionPlace"),Type(concTypeOption(),"Place",EmptyTargetLanguageType()),concBQTerm(BQVariable(concOption(OriginTracking(Name("o"),11,"Transfo1.t")),Name("o"),Type(concTypeOption(),"WorkDefinition",EmptyTargetLanguageType())),BQVariable(concOption(OriginTracking(Name("name"),11,"Transfo1.t")),Name("name"),Type(concTypeOption(),"String",EmptyTargetLanguageType()))),ExpressionToInstruction(Code(" new ResolveWorkDefinitionPlace(o,name) ")),OriginTracking(Name("make"),11,"Transfo1.t")))))

(Voir fichier astSymbol)


private static class ResolveWorkDefinitionPlace extends petrinetsemantics.DDMMPetriNet.impl.PlaceImpl {
  public String name;
  public simplepdl.WorkDefinition o;
  public ResolveWorkDefinitionPlace(simplepdl.WorkDefinition o, String name) {
    this.name = name;
    this.o = o;
  }
}
=>
`TargetLanguageToCode(TL("\n\nprivate static class ResolveWorkDefinitionPlace extends petrinetsemantics.DDMMPetriNet.impl.PlaceImpl {\n  public String name;\n  public simplepdl.WorkDefinition o;\n  public ResolveWorkDefinitionPlace(simplepdl.WorkDefinition o, String name) {\n    this.name = name;\n    this.o = o;\n  }\n}\n\n",TextPosition(35,1),TextPosition(46,1)))

////


%typeterm ResolveWorkDefinitionPlace {
  implement { ResolveWorkDefinitionPlace }
  is_sort(t) { $t instanceof ResolveWorkDefinitionPlace }
}

private static boolean tom_is_sort_ResolveWorkDefinitionPlace(Object t) {
return  t instanceof ResolveWorkDefinitionPlace ;
}
////
private static  petrinetsemantics.DDMMPetriNet.Place  tom_make_ResolveWorkDefinitionPlace( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  o,  String  name) { 
return  new ResolveWorkDefinitionPlace(o,name) ;
}

////

  /**
    *
    * @param resolveNode temporary ResolveNode that should be replaced
    * @param newNode node (stored in the HashMap) that will replace the ResolveNode
    * @param translator the TimplePDLToPetri3
    */
  public static void resolveInverseLinks(EObject resolveNode, Node newNode, SimplePDLToPetri translator) {
    /* collect arcs having ResolveWorkDefinitionPlace and ResolveWorkDefinitionTransition */

    ECrossReferenceAdapter adapter = new ECrossReferenceAdapter(); //create an adapter
    translator.pn.eAdapters().add(adapter); //attach it to PetriNet

    /*
     * 'references' will contains a set of objects (i.e.
     * EStructuralFeature.Setting) from which we can retrieve 
     * (thanks to getEObject()) objects that
     * contains references (i.e. pointers) to resolveNode
     */
    Collection<EStructuralFeature.Setting> references = adapter.getInverseReferences(resolveNode);

    // for each type of Resolve
    boolean toSet = (false
        | resolveNode instanceof ResolveWorkDefinitionPlace 
        | resolveNode instanceof ResolveWorkDefinitionTransition
        | resolveNode instanceof ResolveProcessTransition
        );

    for (EStructuralFeature.Setting setting:references) {
      // current is an object that references resolveNode
      EObject current = setting.getEObject();
      if (current instanceof Arc) {
        Arc newCurrent = (Arc)current;
          // for each field of Arc
        if(newCurrent.getSource().equals(resolveNode) && toSet) {
          newCurrent.setSource(newNode); 
        } else if(newCurrent.getTarget().equals(resolveNode) && toSet) {
          newCurrent.setTarget(newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      }
    }

  }


  /*
   * Strategy that replaces all Resolve nodes by a normal node
   */
  %strategy Resolve(translator:SimplePDLToPetri) extends Identity() {
    visit Place {
      pr@ResolveWorkDefinitionPlace[o=o,name=name] -> {
        Place res = (Place) translator.table.get(`o).get(`name);
        resolveInverseLinks(`pr, res, translator);
        return res;
      }
    }

    visit Transition {
      tr@ResolveWorkDefinitionTransition[o=o,name=name] -> {
        Transition res = (Transition) translator.table.get(`o).get(`name);
        resolveInverseLinks(`tr, res, translator);
        return res;
      }
      ptr@ResolveProcessTransition[o=o,name=name] -> {
        Transition res = (Transition) translator.table.get(`o).get(`name);
        resolveInverseLinks(`ptr, res, translator);
        return res;
      }

    }

  }

=>
