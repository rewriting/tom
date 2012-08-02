    with ObjectPack, VisitablePackage, StrategyPackage, PositionPackage, AbstractStrategyBasicPackage, IntrospectorPackage, VisitFailurePackage, EnvironmentPackage, PathPackage;
    use  ObjectPack, VisitablePackage, StrategyPackage, PositionPackage, AbstractStrategyBasicPackage, IntrospectorPackage, VisitFailurePackage, EnvironmentPackage, PathPackage;
    
    with Ada.Text_IO, Ada.Assertions;
    use  Ada.Text_IO, Ada.Assertions;
    
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
    
    package body TestEnvironment is
    
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
    
    -- Definition of the term g(x: Term; y: Term)
    type Term_g is new Term and Visitable with
    record
    x : TermPtr;
    y : TermPtr;
    end record;
    
    function  toString(s: Term_G) return String;
    function  setChildren(v: access Term_G ; children : ObjectPtrArrayPtr) return VisitablePtr;
    function  getChildCount(v: access Term_G) return Integer;
    function  getChildAt(v: access Term_G; i : Integer) return VisitablePtr;
    function  setChildAt(v: access  Term_G; i: in Integer; child: in VisitablePtr) return VisitablePtr;
    function  getChildren(v: access Term_G) return ObjectPtrArrayPtr;
    
    function toString(s: Term_G) return String is
    str : access String := new String'("g(");
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
    
    function setChildren(v: access Term_G ; children : ObjectPtrArrayPtr) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    if children /= null and then children'Length = 2 then
    return new Term_G'(TermPtr(children(0)) , TermPtr(children(1)));
    else
    raise IndexOutOfBoundsException;
    end if;
    end;
    
    function  getChildCount(v: access Term_G) return Integer is
    begin
    return 2;
    end;
    
    function  getChildAt(v: access Term_G; i : Integer) return VisitablePtr is
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
    
    function setChildAt(v: access Term_G; i: in Integer; child: in VisitablePtr) return VisitablePtr is
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
    
    function  getChildren(v: access Term_G) return ObjectPtrArrayPtr is
    begin
    return new ObjectPtrArray'((ObjectPtr(v.x), ObjectPtr(v.y)));
    end;
    
    -- Definition of the term f(x: Term)
    type Term_F is new Term and Visitable with
    record
    x : TermPtr;
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
    
    str := new String'(str.all & ")");
    
    return str.all;
    end;
    
    function setChildren(v: access Term_F ; children : ObjectPtrArrayPtr) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    if children /= null and then children'Length = 1 then
    return new Term_F'( x => TermPtr(children(0)) );
    else
    raise IndexOutOfBoundsException;
    end if;
    end;
    
    function  getChildCount(v: access Term_F) return Integer is
    begin
    return 1;
    end;
    
    function  getChildAt(v: access Term_F; i : Integer) return VisitablePtr is
    IndexOutOfBoundsException : exception;
    begin
    if i = 0 then
    return VisitablePtr(v.x);
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
    else
    raise IndexOutOfBoundsException;
    end if;
    end;
    
    function  getChildren(v: access Term_F) return ObjectPtrArrayPtr is
    begin
    return new ObjectPtrArray'( ( 0 => ObjectPtr(v.x)) );
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

    function tom_make_a return TermPtr is 
    begin 
        return new Term_A; 
    end tom_make_a; 

    function tom_make_b return TermPtr is 
    begin 
        return new Term_B; 
    end tom_make_b; 

    function tom_make_g(t0: TermPtr; t1: TermPtr) return TermPtr is 
    begin 
        return new Term_G'(t0,t1); 
    end tom_make_g; 

    
    
    ---------------------------------------------------------------------------
    -- Main
    ---------------------------------------------------------------------------
    
    procedure assertTermEquals(a,b: TermPtr) is
    begin
    
    assert( toString(Object'Class(a.all)) = toString(Object'Class(b.all)) ); 
    
    end;
    
    procedure run_test is
    e1,e2 : Environment;
    subject : TermPtr := null;
    begin
    
    -- testEqualEnvironmentPackage
    
    e1 := newEnvironment.all;
    e2 := newEnvironment.all;
    assert( equals(e1, e2) );
    subject := tom_make_g(tom_make_a,tom_make_b);
    e1.setSubject(ObjectPtr( subject ));
    e2.setSubject(ObjectPtr( subject ));
    assert( equals(e1, e2) );
    e1.down(2);
    e2.down(2);
    assert( equals(e1, e2) );
    e1.up;
    e2.up;
    e1.down(1);
    e2.down(1);
    assert( equals(e1, e2) );
    
    
    -- testUpAndDown
    e1 := newEnvironment.all;
    e2 := newEnvironment.all;
    subject := tom_make_g(tom_make_a,tom_make_b);
    e1.setSubject(ObjectPtr( subject ));
    e2.setSubject(ObjectPtr( subject ));
    e1.down(2);
    e2.down(2);
    
    assertTermEquals(TermPtr(e1.getRoot), tom_make_g(tom_make_a,tom_make_b));
    
    assert( equals( e1.getPosition , makeFromArray( new IntArray'((0=>2)) ) ) );
    assertTermEquals( TermPtr(e1.getSubject) , tom_make_b);
    e1.up;
    e2.up;
    
    assertTermEquals( TermPtr(e1.getRoot) , tom_make_g(tom_make_a,tom_make_b));
    assert( equals(e1.getPosition, PositionPackage.make) );
    assertTermEquals( TermPtr(e1.getSubject) , tom_make_g(tom_make_a,tom_make_b));
    e1.down(1);
    e2.down(1);
    
    assertTermEquals( TermPtr(e1.getRoot) , tom_make_g(tom_make_a,tom_make_b));
    assert( equals(e1.getPosition , makeFromArray( new IntArray'((0=>1)) ) ) );
    assertTermEquals( TermPtr(e1.getSubject) ,tom_make_a);
    
    end;    
    
    end TestEnvironment;
    
