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


