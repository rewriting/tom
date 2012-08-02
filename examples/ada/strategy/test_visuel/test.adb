    with ObjectPack, VisitablePackage, StrategyPackage, PositionPackage, AbstractStrategyBasicPackage, IntrospectorPackage, VisitFailurePackage;
    use  ObjectPack, VisitablePackage, StrategyPackage, PositionPackage, AbstractStrategyBasicPackage, IntrospectorPackage, VisitFailurePackage;
    
    with Ada.Text_IO; use Ada.Text_IO;
    
    with SequenceStrategy;
    with OmegaStrategy;
    with IdentityStrategy;
    with FailStrategy;
    with ChoiceStrategy;
    with NoStrategy;
    with AllsStrategy;
    with OneStrategy;
    with OneIdStrategy;
    with MuVarStrategy;
    with MuStrategy;
    with SequenceIdStrategy;
    with ChoiceIdStrategy;
    with AllSeqStrategy;
    
    use SequenceStrategy;
    use OmegaStrategy;
    use IdentityStrategy;
    use FailStrategy;
    use ChoiceStrategy;
    use NoStrategy;
    use AllsStrategy;
    use OneStrategy;
    use OneIdStrategy;
    use MuVarStrategy;
    use MuStrategy;
    use SequenceIdStrategy;
    use ChoiceIdStrategy;
    use AllSeqStrategy;
    
    package body Test is
    
    -- Global definition of a Term
    type Term is abstract new Visitable and Object with null record;
    function getChildCount(v: access Term) return Integer;
    function getChildAt(v: access Term; i : Integer) return VisitablePtr;
    function setChildAt(v: access Term; i: in Integer; child: in VisitablePtr) return VisitablePtr;
    function getChildren(v: access Term) return ObjectPtrArrayPtr;
    
    function getChildCount(v: access Term) return Integer is
    begin
    return 0;
    end;
    
    function  getChildAt(v: access Term; i : Integer) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    raise IndexOutOfBoundsException;
    return null; --never executed
    end;
    
    function setChildAt(v: access Term; i: in Integer; child: in VisitablePtr) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    raise IndexOutOfBoundsException;
    return VisitablePtr(v);
    end;
    
    function getChildren(v: access Term) return ObjectPtrArrayPtr is
    begin
    return null;
    end;
    
    type TermPtr is access all Term'Class;
    
    -- Definition of the term a
    type Term_A is new Term and Visitable with null record;
    function toString(s: Term_A) return String;
    function setChildren(v: access Term_A ; children : ObjectPtrArrayPtr) return VisitablePtr;
    
    function toString(s: Term_A) return String is begin return "a"; end;
    
    function setChildren(v: access Term_A ; children : ObjectPtrArrayPtr) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    if children = null then
    return new Term_A;
    else
    raise IndexOutOfBoundsException;
    end if;
    end;
    
    -- Definition of the term b
    type Term_B is new Term and Visitable with null record;
    function toString(s: Term_B) return String;
    function setChildren(v: access Term_B ; children : ObjectPtrArrayPtr) return VisitablePtr;
    
    
    function toString(s: Term_B) return String is begin return "b"; end;
    
    function setChildren(v: access Term_B ; children : ObjectPtrArrayPtr) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    if children = null then
    return new Term_B;
    else
    raise IndexOutOfBoundsException;
    end if;
    end;
    
    -- Definition of the term c
    type Term_C is new Term and Visitable with null record;
    function toString(s: Term_C) return String;
    function setChildren(v: access Term_C ; children : ObjectPtrArrayPtr) return VisitablePtr;
    
    
    function toString(s: Term_C) return String is begin return "c"; end;
    
    function setChildren(v: access Term_C ; children : ObjectPtrArrayPtr) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    if children = null then
    return new Term_C;
    else
    raise IndexOutOfBoundsException;
    end if;
    end;
    
    -- Definition of the term f(x: Term; y: Term)
    type Term_F is new Term and Visitable with
    record
    x : TermPtr;
    y : TermPtr;
    end record;
    
    function  toString(s: Term_F) return String;
    function  setChildren(v: access Term_F ; children : ObjectPtrArrayPtr) return VisitablePtr;
    function  getChildCount(v: access Term_F) return Integer;
    function  getChildAt(v: access Term_F; i : Integer) return VisitablePtr;
    function  setChildAt(v: access  Term_F; i: in Integer; child: in VisitablePtr) return VisitablePtr;
    function  getChildren(v: access Term_F) return ObjectPtrArrayPtr;
    
    function toString(s: Term_F) return String is
    str : access String := new String'("f(");
    begin
    if s.x /= null then
    str := new String'(str.all & toString(s.x.all));
    else
    str := new String'(str.all & "null");
    end if;
    
    str := new String'(str.all & ",");
    
    if s.y /= null then
    str := new String'(str.all & toString(s.y.all));
    else
    str := new String'(str.all & "null");
    end if;
    
    str := new String'(str.all & ")");
    
    return str.all;
    end;
    
    function setChildren(v: access Term_F ; children : ObjectPtrArrayPtr) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    if children /= null and then children'Length = 2 then
    return new Term_F'(TermPtr(children(0)) , TermPtr(children(1)));
    else
    raise IndexOutOfBoundsException;
    end if;
    end;
    
    function  getChildCount(v: access Term_F) return Integer is
    begin
    return 2;
    end;
    
    function  getChildAt(v: access Term_F; i : Integer) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    if i = 0 then
    return VisitablePtr(v.x);
    elsif i = 1 then
    return VisitablePtr(v.y);
    else
    raise IndexOutOfBoundsException;
    end if;
    end;
    
    function setChildAt(v: access Term_F; i: in Integer; child: in VisitablePtr) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    if i = 0 then
    v.x := TermPtr(child);
    return VisitablePtr(v);
    elsif i = 1 then
    v.y := TermPtr(child);
    return VisitablePtr(v);
    else
    raise IndexOutOfBoundsException;
    end if;
    end;
    
    function  getChildren(v: access Term_F) return ObjectPtrArrayPtr is
    begin
    return new ObjectPtrArray'((ObjectPtr(v.x), ObjectPtr(v.y)));
    end;
    
    ---------------------------------------------------------------------------
    -- Mapping TOM
    ---------------------------------------------------------------------------
    
    function tom_equal_term_Strategy(t1: StrategyPtr; t2: StrategyPtr) return Boolean is 
    begin 
        return t1.all=t2.all; 
    end tom_equal_term_Strategy; 

    function tom_is_sort_Strategy(t: StrategyPtr) return Boolean is 
    begin 
        return t.all in Strategy'Class; 
    end tom_is_sort_Strategy; 

    
    function tom_equal_term_Position(t1: Position; t2: Position) return Boolean is 
    begin 
        return PositionPackage.equals(t1,t2); 
    end tom_equal_term_Position; 

    function tom_is_sort_Position(t: Position) return Boolean is 
    begin 
        return t in Position'Class; 
    end tom_is_sort_Position; 

    
    function tom_equal_term_int(t1: Integer; t2: Integer) return Boolean is 
    begin 
        return t1=t2; 
    end tom_equal_term_int; 

    function tom_is_sort_int(t: Integer) return Boolean is 
    begin 
        return True; 
    end tom_is_sort_int; 

    
    function tom_equal_term_char(t1: Character; t2: Character) return Boolean is 
    begin 
        return t1=t2; 
    end tom_equal_term_char; 

    function tom_is_sort_char(t: Character) return Boolean is 
    begin 
        return True; 
    end tom_is_sort_char; 

    
    function tom_equal_term_String(t1: access String; t2: access String) return Boolean is 
    begin 
        return t1.all = t2.all; 
    end tom_equal_term_String; 

    function tom_equal_term_String(t1: String; t2: access String) return Boolean is 
    begin 
        return t1 = t2.all; 
    end tom_equal_term_String; 

    function tom_is_sort_String(t: access String) return Boolean is 
    begin 
        return True; 
    end tom_is_sort_String; 

    
    function tom_make_Mu(var: StrategyPtr; v: StrategyPtr) return StrategyPtr is 
    begin 
        return newMu(var, v); 
    end tom_make_Mu; 

    function tom_make_Identity return StrategyPtr is 
    begin 
        return IdentityStrategy.newIdentity; 
    end tom_make_Identity; 

    function tom_make_One(v: StrategyPtr) return StrategyPtr is 
    begin 
        return OneStrategy.newOne(v); 
    end tom_make_One; 

    function tom_make_All(v: StrategyPtr) return StrategyPtr is 
    begin 
        return AllsStrategy.newAlls(v); 
    end tom_make_All; 

    function tom_make_Fail return StrategyPtr is 
    begin 
        return FailStrategy.newFail; 
    end tom_make_Fail; 

    function tom_make_Not(v: StrategyPtr) return StrategyPtr is 
    begin 
        return NoStrategy.newNo(v); 
    end tom_make_Not; 

    function tom_is_fun_sym_Sequence(t: StrategyPtr) return Boolean is 
    begin 
        return t.all in Sequence'Class; 
    end tom_is_fun_sym_Sequence; 

    function tom_empty_list_Sequence return StrategyPtr is 
    begin 
        return null; 
    end tom_empty_list_Sequence; 

    function tom_cons_list_Sequence(head: StrategyPtr; tail: StrategyPtr) return StrategyPtr is 
    begin 
        return  SequenceStrategy.make(head,tail) ; 
    end tom_cons_list_Sequence; 

    function tom_get_head_Sequence_Strategy(t: StrategyPtr) return StrategyPtr is 
    begin 
        return StrategyPtr( getChildAt(VisitablePtr(t), SequenceStrategy.FIRST)); 
    end tom_get_head_Sequence_Strategy; 

    function tom_get_tail_Sequence_Strategy(t: StrategyPtr) return StrategyPtr is 
    begin 
        return StrategyPtr( getChildAt(VisitablePtr(t), SequenceStrategy.SECOND)); 
    end tom_get_tail_Sequence_Strategy; 

    function tom_is_empty_Sequence_Strategy(t: StrategyPtr) return Boolean is 
    begin 
        return t = null; 
    end tom_is_empty_Sequence_Strategy; 

    function tom_append_list_Sequence(l1: StrategyPtr; l2: StrategyPtr) return StrategyPtr is 
    begin 
        if tom_is_empty_Sequence_Strategy(l1) then 
            return l2; 
        elsif tom_is_empty_Sequence_Strategy(l2) then 
            return l1; 
        elsif tom_is_empty_Sequence_Strategy(tom_get_tail_Sequence_Strategy(l1)) then 
            return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1),l2); 
        else 
            return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1),tom_append_list_Sequence(tom_get_tail_Sequence_Strategy(l1),l2)); 
        end if; 
    end tom_append_list_Sequence; 

    function tom_get_slice_Sequence(begining: StrategyPtr; ending: StrategyPtr; tail: StrategyPtr) return StrategyPtr is 
    begin 
        if tom_equal_term_Strategy(begining,ending) then 
            return tail; 
        else 
            return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(begining),tom_get_slice_Sequence(tom_get_tail_Sequence_Strategy(begining),ending,tail)); 
        end if; 
    end tom_get_slice_Sequence; 

    function tom_is_fun_sym_Choice(t: StrategyPtr) return Boolean is 
    begin 
        return t.all in Choice'Class; 
    end tom_is_fun_sym_Choice; 

    function tom_empty_list_Choice return StrategyPtr is 
    begin 
        return null; 
    end tom_empty_list_Choice; 

    function tom_cons_list_Choice(head: StrategyPtr; tail: StrategyPtr) return StrategyPtr is 
    begin 
        return  ChoiceStrategy.make(head,tail) ; 
    end tom_cons_list_Choice; 

    function tom_get_head_Choice_Strategy(t: StrategyPtr) return StrategyPtr is 
    begin 
        return StrategyPtr(getChildAt(VisitablePtr(t),ChoiceStrategy.FIRST)); 
    end tom_get_head_Choice_Strategy; 

    function tom_get_tail_Choice_Strategy(t: StrategyPtr) return StrategyPtr is 
    begin 
        return StrategyPtr(getChildAt(VisitablePtr(t),ChoiceStrategy.SECOND)); 
    end tom_get_tail_Choice_Strategy; 

    function tom_is_empty_Choice_Strategy(t: StrategyPtr) return Boolean is 
    begin 
        return t = null; 
    end tom_is_empty_Choice_Strategy; 

    function tom_append_list_Choice(l1: StrategyPtr; l2: StrategyPtr) return StrategyPtr is 
    begin 
        if tom_is_empty_Choice_Strategy(l1) then 
            return l2; 
        elsif tom_is_empty_Choice_Strategy(l2) then 
            return l1; 
        elsif tom_is_empty_Choice_Strategy(tom_get_tail_Choice_Strategy(l1)) then 
            return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1),l2); 
        else 
            return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1),tom_append_list_Choice(tom_get_tail_Choice_Strategy(l1),l2)); 
        end if; 
    end tom_append_list_Choice; 

    function tom_get_slice_Choice(begining: StrategyPtr; ending: StrategyPtr; tail: StrategyPtr) return StrategyPtr is 
    begin 
        if tom_equal_term_Strategy(begining,ending) then 
            return tail; 
        else 
            return tom_cons_list_Choice(tom_get_head_Choice_Strategy(begining),tom_get_slice_Choice(tom_get_tail_Choice_Strategy(begining),ending,tail)); 
        end if; 
    end tom_get_slice_Choice; 

    function tom_is_fun_sym_SequenceId(t: StrategyPtr) return Boolean is 
    begin 
        return t.all in SequenceId'Class; 
    end tom_is_fun_sym_SequenceId; 

    function tom_empty_list_SequenceId return StrategyPtr is 
    begin 
        return null; 
    end tom_empty_list_SequenceId; 

    function tom_cons_list_SequenceId(head: StrategyPtr; tail: StrategyPtr) return StrategyPtr is 
    begin 
        return SequenceIdStrategy.make(head,tail); 
    end tom_cons_list_SequenceId; 

    function tom_get_head_SequenceId_Strategy(t: StrategyPtr) return StrategyPtr is 
    begin 
        return StrategyPtr(getChildAt(VisitablePtr(t),ChoiceStrategy.FIRST)); 
    end tom_get_head_SequenceId_Strategy; 

    function tom_get_tail_SequenceId_Strategy(t: StrategyPtr) return StrategyPtr is 
    begin 
        return StrategyPtr(getChildAt(VisitablePtr(t),ChoiceStrategy.SECOND)); 
    end tom_get_tail_SequenceId_Strategy; 

    function tom_is_empty_SequenceId_Strategy(t: StrategyPtr) return Boolean is 
    begin 
        return t = null; 
    end tom_is_empty_SequenceId_Strategy; 

    function tom_append_list_SequenceId(l1: StrategyPtr; l2: StrategyPtr) return StrategyPtr is 
    begin 
        if tom_is_empty_SequenceId_Strategy(l1) then 
            return l2; 
        elsif tom_is_empty_SequenceId_Strategy(l2) then 
            return l1; 
        elsif tom_is_empty_SequenceId_Strategy(tom_get_tail_SequenceId_Strategy(l1)) then 
            return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1),l2); 
        else 
            return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1),tom_append_list_SequenceId(tom_get_tail_SequenceId_Strategy(l1),l2)); 
        end if; 
    end tom_append_list_SequenceId; 

    function tom_get_slice_SequenceId(begining: StrategyPtr; ending: StrategyPtr; tail: StrategyPtr) return StrategyPtr is 
    begin 
        if tom_equal_term_Strategy(begining,ending) then 
            return tail; 
        else 
            return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(begining),tom_get_slice_SequenceId(tom_get_tail_SequenceId_Strategy(begining),ending,tail)); 
        end if; 
    end tom_get_slice_SequenceId; 

    function tom_is_fun_sym_ChoiceId(t: StrategyPtr) return Boolean is 
    begin 
        return t.all in ChoiceId'Class; 
    end tom_is_fun_sym_ChoiceId; 

    function tom_empty_list_ChoiceId return StrategyPtr is 
    begin 
        return null; 
    end tom_empty_list_ChoiceId; 

    function tom_cons_list_ChoiceId(head: StrategyPtr; tail: StrategyPtr) return StrategyPtr is 
    begin 
        return ChoiceIdStrategy.make(head,tail); 
    end tom_cons_list_ChoiceId; 

    function tom_get_head_ChoiceId_Strategy(t: StrategyPtr) return StrategyPtr is 
    begin 
        return StrategyPtr(getChildAt(VisitablePtr(t),ChoiceStrategy.FIRST)); 
    end tom_get_head_ChoiceId_Strategy; 

    function tom_get_tail_ChoiceId_Strategy(t: StrategyPtr) return StrategyPtr is 
    begin 
        return StrategyPtr(getChildAt(VisitablePtr(t),ChoiceStrategy.SECOND)); 
    end tom_get_tail_ChoiceId_Strategy; 

    function tom_is_empty_ChoiceId_Strategy(t: StrategyPtr) return Boolean is 
    begin 
        return t = null; 
    end tom_is_empty_ChoiceId_Strategy; 

    function tom_append_list_ChoiceId(l1: StrategyPtr; l2: StrategyPtr) return StrategyPtr is 
    begin 
        if tom_is_empty_ChoiceId_Strategy(l1) then 
            return l2; 
        elsif tom_is_empty_ChoiceId_Strategy(l2) then 
            return l1; 
        elsif tom_is_empty_ChoiceId_Strategy(tom_get_tail_ChoiceId_Strategy(l1)) then 
            return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1),l2); 
        else 
            return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1),tom_append_list_ChoiceId(tom_get_tail_ChoiceId_Strategy(l1),l2)); 
        end if; 
    end tom_append_list_ChoiceId; 

    function tom_get_slice_ChoiceId(begining: StrategyPtr; ending: StrategyPtr; tail: StrategyPtr) return StrategyPtr is 
    begin 
        if tom_equal_term_Strategy(begining,ending) then 
            return tail; 
        else 
            return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(begining),tom_get_slice_ChoiceId(tom_get_tail_ChoiceId_Strategy(begining),ending,tail)); 
        end if; 
    end tom_get_slice_ChoiceId; 

    function tom_make_OneId(v: StrategyPtr) return StrategyPtr is 
    begin 
        return newOneId(v); 
    end tom_make_OneId; 

    
    function tom_make_Try(s: StrategyPtr) return StrategyPtr is 
    begin 
        return ( tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity,tom_empty_list_Choice)) ); 
    end tom_make_Try; 

    function tom_make_Repeat(s: StrategyPtr) return StrategyPtr is 
    begin 
        return ( tom_make_Mu(newMuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(newMuVar("_x"),tom_empty_list_Sequence)),tom_cons_list_Choice(tom_make_Identity,tom_empty_list_Choice))) ); 
    end tom_make_Repeat; 

    function tom_make_OnceTopDown(v: StrategyPtr) return StrategyPtr is 
    begin 
        return ( tom_make_Mu(newMuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(newMuVar("_x")),tom_empty_list_Choice))) ); 
    end tom_make_OnceTopDown; 

    function tom_make_RepeatId(v: StrategyPtr) return StrategyPtr is 
    begin 
        return ( tom_make_Mu(newMuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(newMuVar("_x"),tom_empty_list_SequenceId))) ); 
    end tom_make_RepeatId; 

    function tom_make_OnceTopDownId(v: StrategyPtr) return StrategyPtr is 
    begin 
        return ( tom_make_Mu(newMuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(newMuVar("_x")),tom_empty_list_ChoiceId))) ); 
    end tom_make_OnceTopDownId; 

    
    
    
    function tom_equal_term_Term(t1: TermPtr; t2: TermPtr) return Boolean is 
    begin 
        return t1.all=t2.all; 
    end tom_equal_term_Term; 

    function tom_is_sort_Term(t: TermPtr) return Boolean is 
    begin 
        return t.all in Term'Class; 
    end tom_is_sort_Term; 

    function tom_is_fun_sym_a(t: TermPtr) return Boolean is 
    begin 
        return t.all in Term_A'Class; 
    end tom_is_fun_sym_a; 

    function tom_make_a return TermPtr is 
    begin 
        return new Term_A; 
    end tom_make_a; 

    function tom_is_fun_sym_b(t: TermPtr) return Boolean is 
    begin 
        return t.all in Term_B'Class; 
    end tom_is_fun_sym_b; 

    function tom_make_b return TermPtr is 
    begin 
        return new Term_B; 
    end tom_make_b; 

    function tom_is_fun_sym_c(t: TermPtr) return Boolean is 
    begin 
        return t.all in Term_C'Class; 
    end tom_is_fun_sym_c; 

    function tom_make_c return TermPtr is 
    begin 
        return new Term_C; 
    end tom_make_c; 

    function tom_make_f(t0: TermPtr; t1: TermPtr) return TermPtr is 
    begin 
        return new Term_F'(t0,t1); 
    end tom_make_f; 

    
    
    ---------------------------------------------------------------------------
    -- Strategy TOM
    ---------------------------------------------------------------------------
    
    type RuleAB is new AbstractStrategyBasic with null record;
        overriding function toString(t: RuleAB) return String;
        overriding function getChildren(v: access RuleAB) return ObjectPtrArrayPtr;
        overriding function setChildren(v: access RuleAB ; children: ObjectPtrArrayPtr) return VisitablePtr;
        overriding function getChildCount(v: access RuleAB) return Integer;
        overriding function getChildAt(v: access RuleAB; i : Integer) return VisitablePtr;
        overriding function setChildAt(v: access RuleAB; i: in Integer; child: in VisitablePtr) return VisitablePtr;
        overriding function visitLight(str:access RuleAB; v: ObjectPtr; intro: access Introspector'Class) return ObjectPtr;

    function newRuleAB return StrategyPtr is
        newStrat : StrategyPtr := new RuleAB;
    begin
        makeAbstractStrategyBasic(AbstractStrategyBasic'Class(newStrat.all), tom_make_Fail);
        return newStrat;
    end newRuleAB;

    function toString(t: RuleAB) return String is begin return "RuleAB()"; end;


    function  getChildren(v: access RuleAB) return ObjectPtrArrayPtr is
        stratChilds : ObjectPtrArrayPtr := new ObjectPtrArray(0..getChildCount(v));
    begin
        stratChilds(0) := ObjectPtr( getChildAt(AbstractStrategyBasic(v.all)'Access,0) );
        return stratChilds;
    end getChildren;

    function setChildren(v: access RuleAB ; children: ObjectPtrArrayPtr) return VisitablePtr is
    begin
        return setChildAt(AbstractStrategyBasic(v.all)'Access, 0, VisitablePtr(children(0)));
    end setChildren;

    function getChildCount(v: access RuleAB) return Integer is
    begin
        return 1;
    end getChildCount;

    function getChildAt(v: access RuleAB; i : Integer) return VisitablePtr is
        IndexOutOfBoundsException: exception;
    begin
        if i = 0 then
            return getChildAt(AbstractStrategyBasic(v.all)'Access, 0);
        else
            raise IndexOutOfBoundsException;
        end if;
    end getChildAt;

    function setChildAt(v: access RuleAB; i: in Integer; child: in VisitablePtr) return VisitablePtr is
        IndexOutOfBoundsException: exception;
    begin
        if i = 0 then
            return setChildAt(AbstractStrategyBasic(v.all)'Access, 0, child);
        else
            raise IndexOutOfBoundsException;
        end if;
    end setChildAt;

    function tom_visit_Term(str: access RuleAB; arg: TermPtr; intro: access Introspector'Class) return TermPtr is 
    begin 
        if not (( null  = str.env)) then 
            return TermPtr(visit(str.any, str.env, intro)); 
        else 
            return TermPtr(visitLight(str.any, ObjectPtr(arg),intro)); 
        end if; 
    end tom_visit_Term; 

    function visit_Term(str: access RuleAB; tom_arg: TermPtr; intro: access Introspector'Class) return TermPtr is 
    begin 
        if tom_arg.all in Term'Class then 
            if tom_is_fun_sym_a(TermPtr(tom_arg)) then 
                return tom_make_b; 
            end if; 
        end if; 
        return tom_visit_Term(str,tom_arg,intro); 
    end visit_Term; 

    function visitLight(str: access RuleAB; v: ObjectPtr; intro: access Introspector'Class) return ObjectPtr is 
    begin 
        if v.all in Term'Class then 
            return ObjectPtr(visit_Term(str,TermPtr(v),intro)); 
        end if; 
        if not (( null  = str.env)) then 
            return ObjectPtr(visit(str.any, str.env, intro)); 
        else 
            return ObjectPtr(visitLight(str.any, v,intro)); 
        end if; 
    end visitLight; 


    function tom_make_RuleAB return StrategyPtr is 
    begin 
        return  newRuleAB; 
    end tom_make_RuleAB; 

    type RuleBC is new AbstractStrategyBasic with null record;
        overriding function toString(t: RuleBC) return String;
        overriding function getChildren(v: access RuleBC) return ObjectPtrArrayPtr;
        overriding function setChildren(v: access RuleBC ; children: ObjectPtrArrayPtr) return VisitablePtr;
        overriding function getChildCount(v: access RuleBC) return Integer;
        overriding function getChildAt(v: access RuleBC; i : Integer) return VisitablePtr;
        overriding function setChildAt(v: access RuleBC; i: in Integer; child: in VisitablePtr) return VisitablePtr;
        overriding function visitLight(str:access RuleBC; v: ObjectPtr; intro: access Introspector'Class) return ObjectPtr;

    function newRuleBC return StrategyPtr is
        newStrat : StrategyPtr := new RuleBC;
    begin
        makeAbstractStrategyBasic(AbstractStrategyBasic'Class(newStrat.all), tom_make_Fail);
        return newStrat;
    end newRuleBC;

    function toString(t: RuleBC) return String is begin return "RuleBC()"; end;


    function  getChildren(v: access RuleBC) return ObjectPtrArrayPtr is
        stratChilds : ObjectPtrArrayPtr := new ObjectPtrArray(0..getChildCount(v));
    begin
        stratChilds(0) := ObjectPtr( getChildAt(AbstractStrategyBasic(v.all)'Access,0) );
        return stratChilds;
    end getChildren;

    function setChildren(v: access RuleBC ; children: ObjectPtrArrayPtr) return VisitablePtr is
    begin
        return setChildAt(AbstractStrategyBasic(v.all)'Access, 0, VisitablePtr(children(0)));
    end setChildren;

    function getChildCount(v: access RuleBC) return Integer is
    begin
        return 1;
    end getChildCount;

    function getChildAt(v: access RuleBC; i : Integer) return VisitablePtr is
        IndexOutOfBoundsException: exception;
    begin
        if i = 0 then
            return getChildAt(AbstractStrategyBasic(v.all)'Access, 0);
        else
            raise IndexOutOfBoundsException;
        end if;
    end getChildAt;

    function setChildAt(v: access RuleBC; i: in Integer; child: in VisitablePtr) return VisitablePtr is
        IndexOutOfBoundsException: exception;
    begin
        if i = 0 then
            return setChildAt(AbstractStrategyBasic(v.all)'Access, 0, child);
        else
            raise IndexOutOfBoundsException;
        end if;
    end setChildAt;

    function tom_visit_Term(str: access RuleBC; arg: TermPtr; intro: access Introspector'Class) return TermPtr is 
    begin 
        if not (( null  = str.env)) then 
            return TermPtr(visit(str.any, str.env, intro)); 
        else 
            return TermPtr(visitLight(str.any, ObjectPtr(arg),intro)); 
        end if; 
    end tom_visit_Term; 

    function visit_Term(str: access RuleBC; tom_arg: TermPtr; intro: access Introspector'Class) return TermPtr is 
    begin 
        if tom_arg.all in Term'Class then 
            if tom_is_fun_sym_b(TermPtr(tom_arg)) then 
                return tom_make_c; 
            end if; 
        end if; 
        return tom_visit_Term(str,tom_arg,intro); 
    end visit_Term; 

    function visitLight(str: access RuleBC; v: ObjectPtr; intro: access Introspector'Class) return ObjectPtr is 
    begin 
        if v.all in Term'Class then 
            return ObjectPtr(visit_Term(str,TermPtr(v),intro)); 
        end if; 
        if not (( null  = str.env)) then 
            return ObjectPtr(visit(str.any, str.env, intro)); 
        else 
            return ObjectPtr(visitLight(str.any, v,intro)); 
        end if; 
    end visitLight; 


    function tom_make_RuleBC return StrategyPtr is 
    begin 
        return  newRuleBC; 
    end tom_make_RuleBC; 

    type RuleCA is new AbstractStrategyBasic with null record;
        overriding function toString(t: RuleCA) return String;
        overriding function getChildren(v: access RuleCA) return ObjectPtrArrayPtr;
        overriding function setChildren(v: access RuleCA ; children: ObjectPtrArrayPtr) return VisitablePtr;
        overriding function getChildCount(v: access RuleCA) return Integer;
        overriding function getChildAt(v: access RuleCA; i : Integer) return VisitablePtr;
        overriding function setChildAt(v: access RuleCA; i: in Integer; child: in VisitablePtr) return VisitablePtr;
        overriding function visitLight(str:access RuleCA; v: ObjectPtr; intro: access Introspector'Class) return ObjectPtr;

    function newRuleCA return StrategyPtr is
        newStrat : StrategyPtr := new RuleCA;
    begin
        makeAbstractStrategyBasic(AbstractStrategyBasic'Class(newStrat.all), tom_make_Fail);
        return newStrat;
    end newRuleCA;

    function toString(t: RuleCA) return String is begin return "RuleCA()"; end;


    function  getChildren(v: access RuleCA) return ObjectPtrArrayPtr is
        stratChilds : ObjectPtrArrayPtr := new ObjectPtrArray(0..getChildCount(v));
    begin
        stratChilds(0) := ObjectPtr( getChildAt(AbstractStrategyBasic(v.all)'Access,0) );
        return stratChilds;
    end getChildren;

    function setChildren(v: access RuleCA ; children: ObjectPtrArrayPtr) return VisitablePtr is
    begin
        return setChildAt(AbstractStrategyBasic(v.all)'Access, 0, VisitablePtr(children(0)));
    end setChildren;

    function getChildCount(v: access RuleCA) return Integer is
    begin
        return 1;
    end getChildCount;

    function getChildAt(v: access RuleCA; i : Integer) return VisitablePtr is
        IndexOutOfBoundsException: exception;
    begin
        if i = 0 then
            return getChildAt(AbstractStrategyBasic(v.all)'Access, 0);
        else
            raise IndexOutOfBoundsException;
        end if;
    end getChildAt;

    function setChildAt(v: access RuleCA; i: in Integer; child: in VisitablePtr) return VisitablePtr is
        IndexOutOfBoundsException: exception;
    begin
        if i = 0 then
            return setChildAt(AbstractStrategyBasic(v.all)'Access, 0, child);
        else
            raise IndexOutOfBoundsException;
        end if;
    end setChildAt;

    function tom_visit_Term(str: access RuleCA; arg: TermPtr; intro: access Introspector'Class) return TermPtr is 
    begin 
        if not (( null  = str.env)) then 
            return TermPtr(visit(str.any, str.env, intro)); 
        else 
            return TermPtr(visitLight(str.any, ObjectPtr(arg),intro)); 
        end if; 
    end tom_visit_Term; 

    function visit_Term(str: access RuleCA; tom_arg: TermPtr; intro: access Introspector'Class) return TermPtr is 
    begin 
        if tom_arg.all in Term'Class then 
            if tom_is_fun_sym_c(TermPtr(tom_arg)) then 
                return tom_make_a; 
            end if; 
        end if; 
        return tom_visit_Term(str,tom_arg,intro); 
    end visit_Term; 

    function visitLight(str: access RuleCA; v: ObjectPtr; intro: access Introspector'Class) return ObjectPtr is 
    begin 
        if v.all in Term'Class then 
            return ObjectPtr(visit_Term(str,TermPtr(v),intro)); 
        end if; 
        if not (( null  = str.env)) then 
            return ObjectPtr(visit(str.any, str.env, intro)); 
        else 
            return ObjectPtr(visitLight(str.any, v,intro)); 
        end if; 
    end visitLight; 


    function tom_make_RuleCA return StrategyPtr is 
    begin 
        return  newRuleCA; 
    end tom_make_RuleCA; 

    
    
    ---------------------------------------------------------------------------
    -- Main
    ---------------------------------------------------------------------------
    
    procedure doThis(strat: StrategyPtr; toVisit: TermPtr; expected: TermPtr) is
    resultat : TermPtr := null;
    begin
    resultat := TermPtr( visit(strat, VisitablePtr(toVisit)) );
    
    if resultat /= null then
    put(toString(resultat.all));
    else
    put("null");
    end if;
    
    put(" (");
    
    if expected = null then
    if resultat /= null then
    put("FAILED: expected Failure");
    else
    put("OK");
    end if;
    else
    if resultat = null then
    put("FAILED: expected " & toString(Object'Class(expected.all)));
    elsif toString(Object'Class(resultat.all)) /= toString(Object'Class(expected.all)) then
    put("FAILED: expected " & toString(Object'Class(expected.all)));
    else
    put("OK");
    end if;
    end if;
    
    put_line(")");
    
    exception when VisitFailurePackage.VisitFailure =>
    put("failure");
    
    put(" (");
    
    if expected = null then
    put("OK");
    else
    put("FAILED: expected " & toString(Object'Class(expected.all)));
    end if;
    
    put_line(")");
    end;
    
    procedure run_test is
    begin
    
    -- Identity & Fail
    put("(a -> b)[a] = ");
    doThis(tom_make_RuleAB, tom_make_a, tom_make_b);
    
    put("(a -> b)[b] = ");
    doThis(tom_make_RuleAB, tom_make_b, null);
    
    put("(a -> b)[f(a,a)] = ");
    doThis(tom_make_RuleAB, tom_make_f(tom_make_a,tom_make_a), null);
    
    put("(Identity)[a] = ");
    doThis(tom_make_Identity, tom_make_a, tom_make_a);
    
    put("(Identity)[b] = ");
    doThis(tom_make_Identity, tom_make_b, tom_make_b);
    
    put("(Identity)[f(a,a)] = ");
    doThis(tom_make_Identity, tom_make_f(tom_make_a,tom_make_a), tom_make_f(tom_make_a,tom_make_a));
    
    put("(Fail)[a] = ");
    doThis(tom_make_Fail, tom_make_a, null);
    
    put_line("");
    
    -- Sequence
    put("(Sequence(a -> b, b -> c))[a] = ");
    doThis(tom_cons_list_Sequence(tom_make_RuleAB,tom_cons_list_Sequence(tom_make_RuleBC,tom_empty_list_Sequence)), tom_make_a, tom_make_c);
    
    put("(Sequence(a -> b, c -> a))[a] = ");
    doThis(tom_cons_list_Sequence(tom_make_RuleAB,tom_cons_list_Sequence(tom_make_RuleCA,tom_empty_list_Sequence)), tom_make_a, null);
    
    put("(Sequence(b -> c, a -> b))[a] = ");
    doThis(tom_cons_list_Sequence(tom_make_RuleBC,tom_cons_list_Sequence(tom_make_RuleAB,tom_empty_list_Sequence)), tom_make_a, null);
    
    put_line("");
    
    -- Choice
    
    put("(Choice(a -> b, b -> c))[a] = ");
    doThis(tom_cons_list_Choice(tom_make_RuleAB,tom_cons_list_Choice(tom_make_RuleBC,tom_empty_list_Choice)), tom_make_a, tom_make_b);
    
    put("(Choice(b -> c, a -> b))[a] = ");
    doThis(tom_cons_list_Choice(tom_make_RuleBC,tom_cons_list_Choice(tom_make_RuleAB,tom_empty_list_Choice)), tom_make_a, tom_make_b);
    
    put("(Choice(b -> c, c -> a))[a] = ");
    doThis(tom_cons_list_Choice(tom_make_RuleBC,tom_cons_list_Choice(tom_make_RuleCA,tom_empty_list_Choice)), tom_make_a, null);
    
    put("(Choice(b -> c, Identity))[a] = ");
    doThis(tom_cons_list_Choice(tom_make_RuleBC,tom_cons_list_Choice(tom_make_Identity,tom_empty_list_Choice)), tom_make_a, tom_make_a);
    
    put_line("");
    
    -- Not
    put("(Not(a -> b))[a] = ");
    doThis(tom_make_Not(tom_make_RuleAB), tom_make_a, null);
    
    put("(Not(b -> c))[a] = ");
    doThis(tom_make_Not(tom_make_RuleBC), tom_make_a, tom_make_a);
    
    put_line("");
    
    -- Try
    put("(Try(b -> c))[a] = ");
    doThis(tom_make_Try(tom_make_RuleBC), tom_make_a, tom_make_a);
    
    put_line("");
    
    -- All
    put("(All(a -> b))[f(a,a)] = ");
    doThis(tom_make_All(tom_make_RuleAB), tom_make_f(tom_make_a,tom_make_a), tom_make_f(tom_make_b,tom_make_b));
    
    put("(All(a -> b))[f(a,b)] = ");
    doThis(tom_make_All(tom_make_RuleAB), tom_make_f(tom_make_a,tom_make_b), null);
    
    put("(All(a -> b))[a] = ");
    doThis(tom_make_All(tom_make_RuleAB), tom_make_a, tom_make_a);
    
    put("(All(Try(a -> b)))[f(a,c)] = ");
    doThis(tom_make_All(tom_make_Try(tom_make_RuleAB)), tom_make_f(tom_make_a,tom_make_c), tom_make_f(tom_make_b,tom_make_c));
    
    put_line("");
    
    -- One
    put("(One(a -> b))[f(a,a)] = ");
    doThis(tom_make_One(tom_make_RuleAB), tom_make_f(tom_make_a,tom_make_a), tom_make_f(tom_make_b,tom_make_a));
    
    put("(One(a -> b))[f(b,a)] = ");
    doThis(tom_make_One(tom_make_RuleAB), tom_make_f(tom_make_b,tom_make_a), tom_make_f(tom_make_b,tom_make_b));
    
    put("(One(a -> b))[a] = ");
    doThis(tom_make_One(tom_make_RuleAB), tom_make_a, null);
    
    put_line("");
    
    -- Repeat
    put("((Repeat(a -> b))[a] = ");
    doThis(tom_make_Repeat(tom_make_RuleAB), tom_make_a, tom_make_b);
    
    put("(Repeat(Choice(b -> c, a -> b)))[a] = ");
    doThis(tom_make_Repeat(tom_cons_list_Choice(tom_make_RuleBC,tom_cons_list_Choice(tom_make_RuleAB ,tom_empty_list_Choice))), tom_make_a, tom_make_c);
    
    put("((Repeat(b -> c))[a] = ");
    doThis(tom_make_Repeat(tom_make_RuleBC), tom_make_a, tom_make_a);
    
    end;    
    
    end Test;
