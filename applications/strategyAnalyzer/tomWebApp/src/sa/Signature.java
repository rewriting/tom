package sa;

import sa.rule.types.*;
import java.util.Set;
import java.util.HashSet;
import com.google.common.collect.HashBasedTable;

public class Signature {
  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static boolean tom_is_fun_sym_UnamedField( sa.rule.types.Field  t) {return  (t instanceof sa.rule.types.field.UnamedField) ;}private static  sa.rule.types.GomType  tom_get_slot_UnamedField_FieldType( sa.rule.types.Field  t) {return  t.getFieldType() ;}private static  sa.rule.types.GomType  tom_make_GomType( String  t0) { return  sa.rule.types.gomtype.GomType.make(t0) ;}private static boolean tom_is_fun_sym_Alternative( sa.rule.types.Alternative  t) {return  (t instanceof sa.rule.types.alternative.Alternative) ;}private static  String  tom_get_slot_Alternative_Name( sa.rule.types.Alternative  t) {return  t.getName() ;}private static  sa.rule.types.FieldList  tom_get_slot_Alternative_DomainList( sa.rule.types.Alternative  t) {return  t.getDomainList() ;}private static  sa.rule.types.GomType  tom_get_slot_Alternative_Codomain( sa.rule.types.Alternative  t) {return  t.getCodomain() ;}private static boolean tom_is_fun_sym_SortType( sa.rule.types.Production  t) {return  (t instanceof sa.rule.types.production.SortType) ;}private static  sa.rule.types.GomType  tom_get_slot_SortType_Type( sa.rule.types.Production  t) {return  t.getType() ;}private static  sa.rule.types.AlternativeList  tom_get_slot_SortType_AlternativeList( sa.rule.types.Production  t) {return  t.getAlternativeList() ;}private static boolean tom_is_fun_sym_Program( sa.rule.types.Program  t) {return  (t instanceof sa.rule.types.program.Program) ;}private static  sa.rule.types.ProductionList  tom_get_slot_Program_productionList( sa.rule.types.Program  t) {return  t.getproductionList() ;}private static  sa.rule.types.ProductionList  tom_get_slot_Program_functionList( sa.rule.types.Program  t) {return  t.getfunctionList() ;}private static  sa.rule.types.StratDeclList  tom_get_slot_Program_stratList( sa.rule.types.Program  t) {return  t.getstratList() ;}private static  sa.rule.types.Trs  tom_get_slot_Program_trs( sa.rule.types.Program  t) {return  t.gettrs() ;}private static boolean tom_is_fun_sym_ConcGomType( sa.rule.types.GomTypeList  t) {return  ((t instanceof sa.rule.types.gomtypelist.ConsConcGomType) || (t instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) ;}private static  sa.rule.types.GomTypeList  tom_empty_list_ConcGomType() { return  sa.rule.types.gomtypelist.EmptyConcGomType.make() ;}private static  sa.rule.types.GomTypeList  tom_cons_list_ConcGomType( sa.rule.types.GomType  e,  sa.rule.types.GomTypeList  l) { return  sa.rule.types.gomtypelist.ConsConcGomType.make(e,l) ;}private static  sa.rule.types.GomType  tom_get_head_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getHeadConcGomType() ;}private static  sa.rule.types.GomTypeList  tom_get_tail_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getTailConcGomType() ;}private static boolean tom_is_empty_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.isEmptyConcGomType() ;}   private static   sa.rule.types.GomTypeList  tom_append_list_ConcGomType( sa.rule.types.GomTypeList l1,  sa.rule.types.GomTypeList  l2) {     if( l1.isEmptyConcGomType() ) {       return l2;     } else if( l2.isEmptyConcGomType() ) {       return l1;     } else if(  l1.getTailConcGomType() .isEmptyConcGomType() ) {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,l2) ;     } else {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,tom_append_list_ConcGomType( l1.getTailConcGomType() ,l2)) ;     }   }   private static   sa.rule.types.GomTypeList  tom_get_slice_ConcGomType( sa.rule.types.GomTypeList  begin,  sa.rule.types.GomTypeList  end, sa.rule.types.GomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomType()  ||  (end==tom_empty_list_ConcGomType()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.gomtypelist.ConsConcGomType.make( begin.getHeadConcGomType() ,( sa.rule.types.GomTypeList )tom_get_slice_ConcGomType( begin.getTailConcGomType() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcField( sa.rule.types.FieldList  t) {return  ((t instanceof sa.rule.types.fieldlist.ConsConcField) || (t instanceof sa.rule.types.fieldlist.EmptyConcField)) ;}private static  sa.rule.types.FieldList  tom_empty_list_ConcField() { return  sa.rule.types.fieldlist.EmptyConcField.make() ;}private static  sa.rule.types.FieldList  tom_cons_list_ConcField( sa.rule.types.Field  e,  sa.rule.types.FieldList  l) { return  sa.rule.types.fieldlist.ConsConcField.make(e,l) ;}private static  sa.rule.types.Field  tom_get_head_ConcField_FieldList( sa.rule.types.FieldList  l) {return  l.getHeadConcField() ;}private static  sa.rule.types.FieldList  tom_get_tail_ConcField_FieldList( sa.rule.types.FieldList  l) {return  l.getTailConcField() ;}private static boolean tom_is_empty_ConcField_FieldList( sa.rule.types.FieldList  l) {return  l.isEmptyConcField() ;}   private static   sa.rule.types.FieldList  tom_append_list_ConcField( sa.rule.types.FieldList l1,  sa.rule.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  sa.rule.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  sa.rule.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   sa.rule.types.FieldList  tom_get_slice_ConcField( sa.rule.types.FieldList  begin,  sa.rule.types.FieldList  end, sa.rule.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end==tom_empty_list_ConcField()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( sa.rule.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcAlternative( sa.rule.types.AlternativeList  t) {return  ((t instanceof sa.rule.types.alternativelist.ConsConcAlternative) || (t instanceof sa.rule.types.alternativelist.EmptyConcAlternative)) ;}private static  sa.rule.types.AlternativeList  tom_empty_list_ConcAlternative() { return  sa.rule.types.alternativelist.EmptyConcAlternative.make() ;}private static  sa.rule.types.AlternativeList  tom_cons_list_ConcAlternative( sa.rule.types.Alternative  e,  sa.rule.types.AlternativeList  l) { return  sa.rule.types.alternativelist.ConsConcAlternative.make(e,l) ;}private static  sa.rule.types.Alternative  tom_get_head_ConcAlternative_AlternativeList( sa.rule.types.AlternativeList  l) {return  l.getHeadConcAlternative() ;}private static  sa.rule.types.AlternativeList  tom_get_tail_ConcAlternative_AlternativeList( sa.rule.types.AlternativeList  l) {return  l.getTailConcAlternative() ;}private static boolean tom_is_empty_ConcAlternative_AlternativeList( sa.rule.types.AlternativeList  l) {return  l.isEmptyConcAlternative() ;}   private static   sa.rule.types.AlternativeList  tom_append_list_ConcAlternative( sa.rule.types.AlternativeList l1,  sa.rule.types.AlternativeList  l2) {     if( l1.isEmptyConcAlternative() ) {       return l2;     } else if( l2.isEmptyConcAlternative() ) {       return l1;     } else if(  l1.getTailConcAlternative() .isEmptyConcAlternative() ) {       return  sa.rule.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,l2) ;     } else {       return  sa.rule.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,tom_append_list_ConcAlternative( l1.getTailConcAlternative() ,l2)) ;     }   }   private static   sa.rule.types.AlternativeList  tom_get_slice_ConcAlternative( sa.rule.types.AlternativeList  begin,  sa.rule.types.AlternativeList  end, sa.rule.types.AlternativeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAlternative()  ||  (end==tom_empty_list_ConcAlternative()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.alternativelist.ConsConcAlternative.make( begin.getHeadConcAlternative() ,( sa.rule.types.AlternativeList )tom_get_slice_ConcAlternative( begin.getTailConcAlternative() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcProduction( sa.rule.types.ProductionList  t) {return  ((t instanceof sa.rule.types.productionlist.ConsConcProduction) || (t instanceof sa.rule.types.productionlist.EmptyConcProduction)) ;}private static  sa.rule.types.ProductionList  tom_empty_list_ConcProduction() { return  sa.rule.types.productionlist.EmptyConcProduction.make() ;}private static  sa.rule.types.ProductionList  tom_cons_list_ConcProduction( sa.rule.types.Production  e,  sa.rule.types.ProductionList  l) { return  sa.rule.types.productionlist.ConsConcProduction.make(e,l) ;}private static  sa.rule.types.Production  tom_get_head_ConcProduction_ProductionList( sa.rule.types.ProductionList  l) {return  l.getHeadConcProduction() ;}private static  sa.rule.types.ProductionList  tom_get_tail_ConcProduction_ProductionList( sa.rule.types.ProductionList  l) {return  l.getTailConcProduction() ;}private static boolean tom_is_empty_ConcProduction_ProductionList( sa.rule.types.ProductionList  l) {return  l.isEmptyConcProduction() ;}   private static   sa.rule.types.ProductionList  tom_append_list_ConcProduction( sa.rule.types.ProductionList l1,  sa.rule.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  sa.rule.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  sa.rule.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   sa.rule.types.ProductionList  tom_get_slice_ConcProduction( sa.rule.types.ProductionList  begin,  sa.rule.types.ProductionList  end, sa.rule.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end==tom_empty_list_ConcProduction()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( sa.rule.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }    

  // All literal strings and string-valued constant expressions are interned.
  public final static String TRUE = "True";
  public final static String FALSE = "False";
  public final static String AND = "and";
  public final static String BOTTOM = "Bottom";
  public final static String FAIL = "bottom";
  public final static String BOTTOM2 = "Bottom2";
  public final static String BOTTOMLIST = "BottomList";
  public final static String EQ = "eq";
  public final static String APPL = "Appl";
  public final static String CONS = "Cons";
  public final static String NIL = "Nil";
  
  public final static String ENCODE = "encode";
  public final static String DECODE = "decode";

  // Types
  public final static GomType TYPE_BOOLEAN = tom_make_GomType("Bool");
  public final static GomType TYPE_TERM = tom_make_GomType("Term");
  public final static GomType TYPE_METASYMBOL = tom_make_GomType("MetaSymbol");
  public final static GomType TYPE_METATERM = tom_make_GomType("MetaTerm");
  public final static GomType TYPE_METALIST = tom_make_GomType("MetaList");

  // R=Codomain x C=SymbolName -> V=Domain List of types
  private HashBasedTable<GomType,String,GomTypeList> table;

  // set of names that correspond to defined symbols (i.e. not constructors)
  private HashSet<String> functions;
  // set of names that correspond to internals symbols (i.e. bottom, meta-symbols)
  private HashSet<String> internals;

  public Signature() {
    this.table = HashBasedTable.create();
    this.functions = new HashSet<String>();
    this.internals = new HashSet<String>();
  }

  public Signature(Signature from) {
    this.table = HashBasedTable.create(from.table);
    this.functions = new HashSet<String>(from.functions);
    this.internals = new HashSet<String>(from.internals);
  }

  public boolean isBooleanOperator(String opname) {
    String s = opname.intern();
    return s == Signature.AND || s == Signature.TRUE || s == Signature.FALSE || s == Signature.EQ;
  }

  public boolean isBooleanOperatorExceptEQ(String opname) {
    String s = opname.intern();
    return s == Signature.AND || s == Signature.TRUE || s == Signature.FALSE;
  }

  public boolean isBooleanOperatorEQ(String opname) {
    String s = opname.intern();
    return s == Signature.EQ;
  }

  /**
   * Add the symbols defined for a given type in the corresponding
   * entry in the signature Map. 
   * @param program the AST of the program containing the signature
   */
  public void setSignature(Program program) {
    {{if (tom_is_sort_Program(program)) {if (tom_is_sort_Program((( sa.rule.types.Program )program))) {if (tom_is_fun_sym_Program((( sa.rule.types.Program )(( sa.rule.types.Program )program)))) { sa.rule.types.ProductionList  tomMatch81_1=tom_get_slot_Program_productionList((( sa.rule.types.Program )program));if (tom_is_fun_sym_ConcProduction((( sa.rule.types.ProductionList )tomMatch81_1))) { sa.rule.types.ProductionList  tomMatch81_end_8=tomMatch81_1;do {{if (!(tom_is_empty_ConcProduction_ProductionList(tomMatch81_end_8))) { sa.rule.types.Production  tomMatch81_13=tom_get_head_ConcProduction_ProductionList(tomMatch81_end_8);if (tom_is_sort_Production(tomMatch81_13)) {if (tom_is_fun_sym_SortType((( sa.rule.types.Production )tomMatch81_13))) { sa.rule.types.AlternativeList  tomMatch81_12=tom_get_slot_SortType_AlternativeList(tomMatch81_13); sa.rule.types.GomType  tom_codomain=tom_get_slot_SortType_Type(tomMatch81_13);if (tom_is_fun_sym_ConcAlternative((( sa.rule.types.AlternativeList )tomMatch81_12))) { sa.rule.types.AlternativeList  tomMatch81_end_18=tomMatch81_12;do {{if (!(tom_is_empty_ConcAlternative_AlternativeList(tomMatch81_end_18))) { sa.rule.types.Alternative  tomMatch81_24=tom_get_head_ConcAlternative_AlternativeList(tomMatch81_end_18);if (tom_is_sort_Alternative(tomMatch81_24)) {if (tom_is_fun_sym_Alternative((( sa.rule.types.Alternative )tomMatch81_24))) { String  tom_name=tom_get_slot_Alternative_Name(tomMatch81_24); sa.rule.types.FieldList  tom_args=tom_get_slot_Alternative_DomainList(tomMatch81_24);if (tom_equal_term_GomType(tom_codomain, tom_get_slot_Alternative_Codomain(tomMatch81_24))) { sa.rule.types.ProductionList  tom_functionList=tom_get_slot_Program_functionList((( sa.rule.types.Program )program));

        GomTypeList domain = tom_empty_list_ConcGomType();
        {{if (tom_is_sort_FieldList(tom_args)) {if (tom_is_fun_sym_ConcField((( sa.rule.types.FieldList )(( sa.rule.types.FieldList )tom_args)))) { sa.rule.types.FieldList  tomMatch82_end_4=(( sa.rule.types.FieldList )tom_args);do {{if (!(tom_is_empty_ConcField_FieldList(tomMatch82_end_4))) { sa.rule.types.Field  tomMatch82_8=tom_get_head_ConcField_FieldList(tomMatch82_end_4);if (tom_is_sort_Field(tomMatch82_8)) {if (tom_is_fun_sym_UnamedField((( sa.rule.types.Field )tomMatch82_8))) {

            domain = tom_append_list_ConcGomType(domain,tom_cons_list_ConcGomType(tom_get_slot_UnamedField_FieldType(tomMatch82_8),tom_empty_list_ConcGomType()));
          }}}if (tom_is_empty_ConcField_FieldList(tomMatch82_end_4)) {tomMatch82_end_4=(( sa.rule.types.FieldList )tom_args);} else {tomMatch82_end_4=tom_get_tail_ConcField_FieldList(tomMatch82_end_4);}}} while(!(tom_equal_term_FieldList(tomMatch82_end_4, (( sa.rule.types.FieldList )tom_args))));}}}}

        addSymbol(tom_name,domain,tom_codomain);

        {{if (tom_is_sort_ProductionList(tom_functionList)) {if (tom_is_fun_sym_ConcProduction((( sa.rule.types.ProductionList )(( sa.rule.types.ProductionList )tom_functionList)))) { sa.rule.types.ProductionList  tomMatch83_end_4=(( sa.rule.types.ProductionList )tom_functionList);do {{if (!(tom_is_empty_ConcProduction_ProductionList(tomMatch83_end_4))) { sa.rule.types.Production  tomMatch83_9=tom_get_head_ConcProduction_ProductionList(tomMatch83_end_4);if (tom_is_sort_Production(tomMatch83_9)) {if (tom_is_fun_sym_SortType((( sa.rule.types.Production )tomMatch83_9))) { sa.rule.types.AlternativeList  tomMatch83_8=tom_get_slot_SortType_AlternativeList(tomMatch83_9); sa.rule.types.GomType  tom_codomain2=tom_get_slot_SortType_Type(tomMatch83_9);if (tom_is_fun_sym_ConcAlternative((( sa.rule.types.AlternativeList )tomMatch83_8))) { sa.rule.types.AlternativeList  tomMatch83_end_14=tomMatch83_8;do {{if (!(tom_is_empty_ConcAlternative_AlternativeList(tomMatch83_end_14))) { sa.rule.types.Alternative  tomMatch83_20=tom_get_head_ConcAlternative_AlternativeList(tomMatch83_end_14);if (tom_is_sort_Alternative(tomMatch83_20)) {if (tom_is_fun_sym_Alternative((( sa.rule.types.Alternative )tomMatch83_20))) {if (tom_equal_term_GomType(tom_codomain2, tom_get_slot_Alternative_Codomain(tomMatch83_20))) {

            if(tom_get_slot_Alternative_Name(tomMatch83_20)== tom_name&& tom_codomain==tom_codomain2) {
              setFunction(tom_name);
            }
          }}}}if (tom_is_empty_ConcAlternative_AlternativeList(tomMatch83_end_14)) {tomMatch83_end_14=tomMatch83_8;} else {tomMatch83_end_14=tom_get_tail_ConcAlternative_AlternativeList(tomMatch83_end_14);}}} while(!(tom_equal_term_AlternativeList(tomMatch83_end_14, tomMatch83_8)));}}}}if (tom_is_empty_ConcProduction_ProductionList(tomMatch83_end_4)) {tomMatch83_end_4=(( sa.rule.types.ProductionList )tom_functionList);} else {tomMatch83_end_4=tom_get_tail_ConcProduction_ProductionList(tomMatch83_end_4);}}} while(!(tom_equal_term_ProductionList(tomMatch83_end_4, (( sa.rule.types.ProductionList )tom_functionList))));}}}}

      }}}}if (tom_is_empty_ConcAlternative_AlternativeList(tomMatch81_end_18)) {tomMatch81_end_18=tomMatch81_12;} else {tomMatch81_end_18=tom_get_tail_ConcAlternative_AlternativeList(tomMatch81_end_18);}}} while(!(tom_equal_term_AlternativeList(tomMatch81_end_18, tomMatch81_12)));}}}}if (tom_is_empty_ConcProduction_ProductionList(tomMatch81_end_8)) {tomMatch81_end_8=tomMatch81_1;} else {tomMatch81_end_8=tom_get_tail_ConcProduction_ProductionList(tomMatch81_end_8);}}} while(!(tom_equal_term_ProductionList(tomMatch81_end_8, tomMatch81_1)));}}}}}}

  }
  
  /**
   * Create an expanded signature containing the symbols in the
   * current one (set from a program) and builtin symbols used in the
   * translation of strategies. This signature is mono-sorted (TERM only)
   * @return the expanded signature
   */
  public Signature expandSignature()  {
    Signature expandedSignature = new Signature();
    // clone everything, but forget types (use TERM instead)
    for(GomType type: getCodomains()) {
      for(String symbol: getSymbols()) {
        GomTypeList domain = tom_empty_list_ConcGomType();
        for(int i=0 ; i < getArity(symbol) ; i++) {
          domain = tom_cons_list_ConcGomType(TYPE_TERM,tom_append_list_ConcGomType(domain,tom_empty_list_ConcGomType()));
        }
        expandedSignature.addSymbol(symbol,domain,TYPE_TERM);
      }
    }

    if(Main.options.metalevel) {
      // add: Appl, Cons, Nil, BottomList
      expandedSignature.addSymbol(APPL,tom_cons_list_ConcGomType(TYPE_METASYMBOL,tom_cons_list_ConcGomType(TYPE_METALIST,tom_empty_list_ConcGomType())),TYPE_METATERM);
      expandedSignature.addSymbol(CONS,tom_cons_list_ConcGomType(TYPE_METATERM,tom_cons_list_ConcGomType(TYPE_METALIST,tom_empty_list_ConcGomType())),TYPE_METALIST);
      expandedSignature.addSymbol(NIL,tom_empty_list_ConcGomType(),TYPE_METALIST);
      expandedSignature.addSymbol(BOTTOMLIST,tom_cons_list_ConcGomType(TYPE_METALIST,tom_empty_list_ConcGomType()),TYPE_METALIST);
    }

    // add: True, False, and, eq
    expandedSignature.addSymbol(TRUE,tom_empty_list_ConcGomType(),TYPE_BOOLEAN);
    expandedSignature.addSymbol(FALSE,tom_empty_list_ConcGomType(),TYPE_BOOLEAN);
    expandedSignature.addFunctionSymbol(AND,tom_cons_list_ConcGomType(TYPE_BOOLEAN,tom_cons_list_ConcGomType(TYPE_BOOLEAN,tom_empty_list_ConcGomType())),TYPE_BOOLEAN);
    //     expandedSignature.addFunctionSymbol(EQ,`ConcGomType(TYPE_TERM,TYPE_TERM),TYPE_BOOLEAN);
    if(!Main.options.metalevel) {
      expandedSignature.addFunctionSymbol(EQ,tom_cons_list_ConcGomType(TYPE_TERM,tom_cons_list_ConcGomType(TYPE_TERM,tom_empty_list_ConcGomType())),TYPE_BOOLEAN);
    } else {
      expandedSignature.addFunctionSymbol(EQ,tom_cons_list_ConcGomType(TYPE_METATERM,tom_cons_list_ConcGomType(TYPE_METATERM,tom_empty_list_ConcGomType())),TYPE_BOOLEAN);
    }

    // add: bottom
    if(!Main.options.metalevel) {
      expandedSignature.addInternalSymbol(BOTTOM,tom_cons_list_ConcGomType(TYPE_TERM,tom_empty_list_ConcGomType()),TYPE_TERM);
    } else {
      expandedSignature.addInternalSymbol(BOTTOM,tom_cons_list_ConcGomType(TYPE_METATERM,tom_empty_list_ConcGomType()),TYPE_METATERM);
    }

    // for metalevel + Tom code
    if(Main.options.metalevel && Main.options.classname != null) {
      // add: encode, decode
      expandedSignature.addFunctionSymbol(ENCODE,tom_cons_list_ConcGomType(TYPE_TERM,tom_empty_list_ConcGomType()),TYPE_METATERM);
      expandedSignature.addFunctionSymbol(DECODE,tom_cons_list_ConcGomType(TYPE_METATERM,tom_empty_list_ConcGomType()),TYPE_TERM);
      // BOTTOM at the term level (at metalevel we already have Bottom)
      expandedSignature.addFunctionSymbol(FAIL,tom_cons_list_ConcGomType(TYPE_TERM,tom_empty_list_ConcGomType()),TYPE_TERM);
    }
    return expandedSignature;
  }


  /** Get the arity of a symbol
   * @param symbol the name of the symbol
   * @return the arity of the symbol
   */
  public int getArity(String symbol) {
    GomTypeList domain = getDomain(symbol);
    if(domain != null) {
      return domain.length();
    }
    return -1;
  }

  /**
   * Add a symbol (with the corresponding profile).
   * @param name the name of the symbol
   * @param domain the types of its arguments
   * @param codomain the return type 
   */
  public void addSymbol(String name, GomTypeList domain, GomType codomain) {
    table.put(codomain, name.intern(), domain);
  }

  public void addFunctionSymbol(String name, GomTypeList domain, GomType codomain) {
    addSymbol(name, domain, codomain);
    setFunction(name);
  }

  public void addInternalSymbol(String name, GomTypeList domain, GomType codomain) {
    addSymbol(name, domain, codomain);
    setInternal(name);
  }

  public void setFunction(String name) {
    functions.add(name);
  }

  public boolean isFunction(String name) {
    return functions.contains(name);
  }

  public void setInternal(String name) {
    internals.add(name);
  }

  public boolean isInternal(String name) {
    return internals.contains(name);
  }

  /** Get the list of all codomains
   * @return the list of codomains in the table
   */
  public Set<GomType> getCodomains() {
    return table.rowKeySet();
  }

  /** 
   * Get the unique codomain for symbol
   * @return the codomain or null if symbol does not exist
   */
  public GomType getCodomain(String symbol) {
    for(GomType type: getCodomains()) {
      if(getDomain(type,symbol) != null) {
        return type;
      }
    }
    return null;
  }

  /** 
   * Get the unique domain for symbol
   * @return the domain or null if symbol does not exist
   */
  public GomTypeList getDomain(String symbol) {
    GomType codomain = getCodomain(symbol);
    if(codomain != null) {
      return getDomain(codomain, symbol);
    }
    return null;
  }

  /** 
   * Get the unique domain for symbol and a codomain
   * @return the domain or null if symbol does not exist
   */
  private GomTypeList getDomain(GomType codomain, String symbol) {
    return table.get(codomain,symbol);
  }

  /** Get the set of all symbols
   * @return the set of symbols in the signature
   */
  public Set<String> getSymbols() {
    return table.columnKeySet();
  }

  /** Get the set of symbols for a given codomain
   * @codomain the type of the codomain
   * @return the list of symbols in the type
   */
  public Set<String> getSymbols(GomType codomain) {
    return table.row(codomain).keySet();
  }
  
  /** Get the set of all constructors (symbols but not functions)
   * @return the set of constructors in the signature
   */
  public Set<String> getConstructors() {
    HashSet<String> res = new HashSet<String>();
    for(String e:getSymbols()) {
      if(!isFunction(e) && !isInternal(e)) {
        res.add(e);
      }
    }
    return res;
  }

  /** Get the set of constructors for a given codomain
   * @codomain the type of the codomain
   * @return the list of constructors in the type
   */
  public Set<String> getConstructors(GomType codomain) {
    HashSet<String> res = new HashSet<String>();
    for(String e:getSymbols(codomain)) {
      if(!isFunction(e) && !isInternal(e)) {
        res.add(e);
      }
    }
    return res;
  }

  public String toString() {
    return "" + this.table;
  }
}
