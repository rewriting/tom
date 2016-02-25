
package sa.rule.types.addlist;



public final class ConsConcAdd extends sa.rule.types.addlist.ConcAdd implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsConcAdd";


  private ConsConcAdd() {}
  private int hashCode;
  private static ConsConcAdd gomProto = new ConsConcAdd();
  
   private sa.rule.types.Term[] children;
                
private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static boolean tom_is_fun_sym_Add( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Add) ;}private static  sa.rule.types.AddList  tom_get_slot_Add_addlist( sa.rule.types.Term  t) {return  t.getaddlist() ;}private static boolean tom_is_fun_sym_Empty( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Empty) ;}private static boolean tom_is_fun_sym_ConcAdd( sa.rule.types.AddList  t) {return  ((t instanceof sa.rule.types.addlist.ConsConcAdd) || (t instanceof sa.rule.types.addlist.EmptyConcAdd)) ;}private static  sa.rule.types.AddList  tom_empty_list_ConcAdd() { return  sa.rule.types.addlist.EmptyConcAdd.make() ;}private static  sa.rule.types.AddList  tom_cons_list_ConcAdd( sa.rule.types.Term  e,  sa.rule.types.AddList  l) { return  sa.rule.types.addlist.ConsConcAdd.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.getHeadConcAdd() ;}private static  sa.rule.types.AddList  tom_get_tail_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.getTailConcAdd() ;}private static boolean tom_is_empty_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.isEmptyConcAdd() ;}   private static   sa.rule.types.AddList  tom_append_list_ConcAdd( sa.rule.types.AddList l1,  sa.rule.types.AddList  l2) {     if( l1.isEmptyConcAdd() ) {       return l2;     } else if( l2.isEmptyConcAdd() ) {       return l1;     } else if(  l1.getTailConcAdd() .isEmptyConcAdd() ) {       return  sa.rule.types.addlist.ConsConcAdd.make( l1.getHeadConcAdd() ,l2) ;     } else {       return  sa.rule.types.addlist.ConsConcAdd.make( l1.getHeadConcAdd() ,tom_append_list_ConcAdd( l1.getTailConcAdd() ,l2)) ;     }   }   private static   sa.rule.types.AddList  tom_get_slice_ConcAdd( sa.rule.types.AddList  begin,  sa.rule.types.AddList  end, sa.rule.types.AddList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAdd()  ||  (end==tom_empty_list_ConcAdd()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.addlist.ConsConcAdd.make( begin.getHeadConcAdd() ,( sa.rule.types.AddList )tom_get_slice_ConcAdd( begin.getTailConcAdd() ,end,tail)) ;   }   

































































































































































































































































































































































































































































































































































































































































































  private sa.rule.types.Term _HeadConcAdd;
  private sa.rule.types.AddList _TailConcAdd;

  /**
   * Constructor that builds a term rooted by ConsConcAdd
   *
   * @return a term rooted by ConsConcAdd
   */

    public static sa.rule.types.AddList make(sa.rule.types.Term e, sa.rule.types.AddList l) {
  if (true) {{
  {{if (tom_is_sort_Term(e)) {if (tom_is_sort_Term((( sa.rule.types.Term )e))) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )(( sa.rule.types.Term )e)))) {
 return l; }}}}{if (tom_is_sort_Term(e)) {if (tom_is_sort_Term((( sa.rule.types.Term )e))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )e)))) { sa.rule.types.AddList  tomMatch1_4=tom_get_slot_Add_addlist((( sa.rule.types.Term )e));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch1_4))) {
 return tom_append_list_ConcAdd(tomMatch1_4,tom_append_list_ConcAdd(l,tom_empty_list_ConcAdd())); }}}}}}


  if(!l.isEmptyConcAdd()) {
    int cmp = e.compareToLPO(l.getHeadConcAdd());
    if(cmp == 0) { // eliminate identical elements: t + t -> t
      // optim: can be removed
      return l;
    } else if(cmp > 0) { // recursive insertion
      sa.rule.types.AddList newTail = l.getTailConcAdd();
      return realMake(l.getHeadConcAdd(), tom_cons_list_ConcAdd(e,tom_append_list_ConcAdd(newTail,tom_empty_list_ConcAdd())));
    } else { // head insertion
      return realMake(e,l);
    }
  }
}}
      return realMake( e,  l);
    }
  
  private static ConsConcAdd realMake(sa.rule.types.Term _HeadConcAdd, sa.rule.types.AddList _TailConcAdd) {

    // use the proto as a model
    gomProto.initHashCode( _HeadConcAdd,  _TailConcAdd);
    return (ConsConcAdd) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcAdd
   * @param _TailConcAdd
   * @param hashCode hashCode of ConsConcAdd
   */
  private void init(sa.rule.types.Term _HeadConcAdd, sa.rule.types.AddList _TailConcAdd, int hashCode) {
    this._HeadConcAdd = _HeadConcAdd;
    this._TailConcAdd = _TailConcAdd;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcAdd
   * @param _TailConcAdd
   */
  private void initHashCode(sa.rule.types.Term _HeadConcAdd, sa.rule.types.AddList _TailConcAdd) {
    this._HeadConcAdd = _HeadConcAdd;
    this._TailConcAdd = _TailConcAdd;

    this.hashCode = hashFunction();
  }

  /* name and arity */

  /**
   * Returns the name of the symbol
   *
   * @return the name of the symbol
   */
  @Override
  public String symbolName() {
    return "ConsConcAdd";
  }

  /**
   * Returns the arity of the symbol
   *
   * @return the arity of the symbol
   */
  private int getArity() {
    return 2;
  }

  /**
   * Copy the object and returns the copy
   *
   * @return a clone of the SharedObject
   */
  public shared.SharedObject duplicate() {
    ConsConcAdd clone = new ConsConcAdd();
    clone.init( _HeadConcAdd,  _TailConcAdd, hashCode);
    return clone;
  }
  

  /**
   * Compares two terms. This functions implements a total lexicographic path ordering.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare children
   */
  @Override
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    sa.rule.RuleAbstractType ao = (sa.rule.RuleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    ConsConcAdd tco = (ConsConcAdd) ao;
    int _HeadConcAddCmp = (this._HeadConcAdd).compareToLPO(tco._HeadConcAdd);
    if(_HeadConcAddCmp != 0) {
      return _HeadConcAddCmp;
    }

    int _TailConcAddCmp = (this._TailConcAdd).compareToLPO(tco._TailConcAdd);
    if(_TailConcAddCmp != 0) {
      return _TailConcAddCmp;
    }

    throw new RuntimeException("Unable to compare");
  }

 /**
   * Compares two terms. This functions implements a total order.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare children
   */
  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    sa.rule.RuleAbstractType ao = (sa.rule.RuleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    ConsConcAdd tco = (ConsConcAdd) ao;
    int _HeadConcAddCmp = (this._HeadConcAdd).compareTo(tco._HeadConcAdd);
    if(_HeadConcAddCmp != 0) {
      return _HeadConcAddCmp;
    }

    int _TailConcAddCmp = (this._TailConcAdd).compareTo(tco._TailConcAdd);
    if(_TailConcAddCmp != 0) {
      return _TailConcAddCmp;
    }

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  /**
   * Returns hashCode
   *
   * @return hashCode
   */
  @Override
  public final int hashCode() {
    return hashCode;
  }

  /**
   * Checks if a SharedObject is equivalent to the current object
   *
   * @param obj SharedObject to test
   * @return true if obj is a ConsConcAdd and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsConcAdd) {

      ConsConcAdd peer = (ConsConcAdd) obj;
      return _HeadConcAdd==peer._HeadConcAdd && _TailConcAdd==peer._TailConcAdd && true;
    }
    return false;
  }


   //AddList interface
  /**
   * Returns true if the term is rooted by the symbol ConsConcAdd
   *
   * @return true, because this is rooted by ConsConcAdd
   */
  @Override
  public boolean isConsConcAdd() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Term
   *
   * @return the attribute sa.rule.types.Term
   */
  @Override
  public sa.rule.types.Term getHeadConcAdd() {
    return _HeadConcAdd;
  }

  /**
   * Sets and returns the attribute sa.rule.types.AddList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Term which just has been set
   */
  @Override
  public sa.rule.types.AddList setHeadConcAdd(sa.rule.types.Term set_arg) {
    return make(set_arg, _TailConcAdd);
  }
  
  /**
   * Returns the attribute sa.rule.types.AddList
   *
   * @return the attribute sa.rule.types.AddList
   */
  @Override
  public sa.rule.types.AddList getTailConcAdd() {
    return _TailConcAdd;
  }

  /**
   * Sets and returns the attribute sa.rule.types.AddList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.AddList which just has been set
   */
  @Override
  public sa.rule.types.AddList setTailConcAdd(sa.rule.types.AddList set_arg) {
    return make(_HeadConcAdd, set_arg);
  }
  
  /* Visitable */
  /**
   * Returns the number of children of the term
   *
   * @return the number of children of the term
   */
  public int getChildCount() {
    return getChildren().length;
  }

  /**
   * Returns the child at the specified index
   *
   * @param index index of the child to return; must be
             nonnegative and less than the childCount
   * @return the child at the specified index
   * @throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable getChildAt(int index) {
    return getChildren()[index];
 }

  /**
   * Set the child at the specified index
   *
   * @param index index of the child to set; must be
             nonnegative and less than the childCount
   * @param v child to set at the specified index
   * @return the child which was just set
   * @throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    
      tom.library.sl.Visitable[] children = getChildren();
      sa.rule.types.Term[] new_children = new sa.rule.types.Term[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.Term) children[i]);
      }
     new_children[index] = (sa.rule.types.Term) v;
     return fromArray(new_children);
                  
  }

  /**
   * Set children to the term
   *
   * @param children array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "children" is different than 2
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    if (children.length == getChildCount() ) {
      
               sa.rule.types.Term[] typed_children = new sa.rule.types.Term[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.Term) children[i];
              }
              return fromArray(typed_children);
              
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Returns the whole children of the term
   *
   * @return the children of the term
   */
  public tom.library.sl.Visitable[] getChildren() {
    
        if (children == null) {
          children = toArray(new sa.rule.types.Term[]{});
        }
        return java.util.Arrays.copyOf(children,children.length);
      
  }

    /**
     * Compute a hashcode for this term.
     * (for internal use)
     *
     * @return a hash value
     */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-1641202094<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadConcAdd.hashCode() << 8);
    a += (_TailConcAdd.hashCode());

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);
    /* ------------------------------------------- report the result */
    return c;
  }

  /**
    * function that returns functional version of the current operator
    * need for initializing the Enumerator
    */
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AddList>,tom.library.enumerator.Enumeration<sa.rule.types.AddList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AddList>,tom.library.enumerator.Enumeration<sa.rule.types.AddList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AddList>,tom.library.enumerator.Enumeration<sa.rule.types.AddList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Term> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AddList>,tom.library.enumerator.Enumeration<sa.rule.types.AddList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.AddList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.AddList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Term,tom.library.enumerator.F<sa.rule.types.AddList,sa.rule.types.AddList>>) 
        new tom.library.enumerator.F<sa.rule.types.Term,tom.library.enumerator.F<sa.rule.types.AddList,sa.rule.types.AddList>>() {
          public tom.library.enumerator.F<sa.rule.types.AddList,sa.rule.types.AddList> apply(final sa.rule.types.Term t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.AddList,sa.rule.types.AddList>() {
          public sa.rule.types.AddList apply(final sa.rule.types.AddList t2) {
            return make(t1,t2);
          }
        };
          }
        }),t1),t2).pay();
          }
        };
          }
        };
  }

}
