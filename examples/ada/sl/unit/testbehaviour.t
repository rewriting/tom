with ObjectPack, VisitablePackage, StrategyPackage, PositionPackage, AbstractStrategyBasicPackage, IntrospectorPackage, VisitFailurePackage;
use  ObjectPack, VisitablePackage, StrategyPackage, PositionPackage, AbstractStrategyBasicPackage, IntrospectorPackage, VisitFailurePackage;
    
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
    
package body TestBehaviour is

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
	
    %include { sl.tom }
   
	%typeterm Term {
	  implement 		{TermPtr}
	  is_sort(t) 		{$t.all in Term'Class}
	  equals(t1,t2) 	{$t1.all=$t2.all}
	}
	
	%op Term a() {
	  is_fsym(t) 	{$t.all in Term_A'Class}
	  make() 		{new Term_A}
	}

	%op Term b() {
	  is_fsym(t) 	{$t.all in Term_B'Class}
	  make() 		{new Term_B}
	}
	
	%op Term c() {
	  is_fsym(t) 	{$t.all in Term_C'Class}
	  make() 		{new Term_C}
	}
	
	%op Term f(x:Term) {
	  is_fsym(t) {$t.all in Term_F'Class}
	  get_slot(x, t) { Term_F($t.all).x }
	  make(t0)	 {new Term_F'(x => $t0)}
	}
	
	%op Term g(x:Term, y:Term) {
	  is_fsym(t) {$t.all in Term_G'Class}
	  get_slot(x, t) { Term_G($t.all).x }
	  get_slot(y, t) { Term_G($t.all).y }
	  make(t0,t1)	 {new Term_G'($t0,$t1)}
	}
	
	---------------------------------------------------------------------------
	-- Tests Functions
	---------------------------------------------------------------------------
	
	%strategy R1() extends `Identity() {
		visit Term {
		  f(a()) -> { return `f(b()); }
		  b() -> { return `c(); }
		}
	}

	%strategy R2() extends `Fail() {
		visit Term {
		  f(a()) -> { return `f(b()); }
		  b() -> { return `c(); }
		}
	}
	%strategy Builda() extends `Identity() {
		visit Term {
		  _ -> { return `a(); }
		}
	}
	%strategy Buildb() extends `Identity() {
		visit Term {
		  _ -> { return `b(); }
		}
	}
	
	---------------------------------------------------------------------------
	-- Old Behaviour (visitLight)
	---------------------------------------------------------------------------
    
    
	function test1 return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `Identity();
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function test2 return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `(R1());
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function test3 return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `One(R1());
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function test4 return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `OnceBottomUp((R2()));
	begin
	subject := TermPtr( s.visitLight(VisitablePtr(subject)) );
	subject := TermPtr( s.visitLight(VisitablePtr(subject)) );
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function test5 return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `All((R2()));
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function testChoice return TermPtr is
	subject : TermPtr := `f(b());
	s : StrategyPtr := `Choice(R2(),R1());
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function testChoiceSideEffect return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `Choice(Sequence(R1(),R2()),R1());
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function testNot return TermPtr is
	subject : TermPtr := `f(b());
	s : StrategyPtr := `Not(R2());
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function testNotSideEffect return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `Not(Sequence(R1(),R2()));
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function test6 return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Sequence(All(R2()),All(All(R1())));
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function test7 return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Omega(1,R2());
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function test8 return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Omega(2,R2());
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;


	function test11 return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `OnceTopDownId((R1()));
	begin
	subject :=  TermPtr( s.visitLight(VisitablePtr(subject)) );
	subject :=  TermPtr( s.visitLight(VisitablePtr(subject)) );
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function test12 return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Innermost((R2()));
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;

	function test13 return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Outermost((R2()));
	begin
	return  TermPtr( s.visitLight(VisitablePtr(subject)) );
	end;
	
	---------------------------------------------------------------------------
	-- New behaviour (visit)
	---------------------------------------------------------------------------
	
	function test1_New return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `Identity();
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function test2_New return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `(R1());
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function test3_New return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `One(R1());
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function test4_New return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `OnceBottomUp((R2()));
	begin
	subject := TermPtr( s.visit(VisitablePtr(subject)) );
	subject := TermPtr( s.visit(VisitablePtr(subject)) );
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function test5_New return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `All((R2()));
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function testChoice_New return TermPtr is
	subject : TermPtr := `f(b());
	s : StrategyPtr := `Choice(R2(),R1());
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function testChoiceSideEffect_New return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `Choice(Sequence(R1(),R2()),R1());
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function testNot_New return TermPtr is
	subject : TermPtr := `f(b());
	s : StrategyPtr := `Not(R2());
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function testNotSideEffect_New return TermPtr is
	subject : TermPtr := `f(a());
	s : StrategyPtr := `Not(Sequence(R1(),R2()));
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function test6_New return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Sequence(All(R2()),All(All(R1())));
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function test7_New return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Omega(1,R2());
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function test8_New return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Omega(2,R2());
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;


	function test11_New return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `OnceTopDownId((R1()));
	begin
	subject :=  TermPtr( s.visit(VisitablePtr(subject)) );
	subject :=  TermPtr( s.visit(VisitablePtr(subject)) );
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function test12_New return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Innermost((R2()));
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

	function test13_New return TermPtr is
	subject : TermPtr := `g(f(a()),b());
	s : StrategyPtr := `Outermost((R2()));
	begin
	return  TermPtr( s.visit(VisitablePtr(subject)) );
	end;

    ---------------------------------------------------------------------------
    -- Main
    ---------------------------------------------------------------------------
    
    procedure assertTermEquals(a,b: TermPtr) is
    begin

		assert( toString(Object'Class(a.all)) = toString(Object'Class(b.all)) ); 

    end;
    
    procedure run_test is
    begin
		
		 
		--Test1
		assertTermEquals (`f(a()), test1 );
		assertTermEquals (`f(a()), test1_New );

		--Test2
		assertTermEquals ( `f(b()) , test2 );
		assertTermEquals ( `f(b()) , test2_New );

		--Test3
		assertTermEquals ( `f(a()) , test3 );
		assertTermEquals ( `f(a()) , test3_New );

		--Test4
		assertTermEquals ( `g(f(c()),c()) , test4 );
		assertTermEquals ( `g(f(c()),c()) , test4_New );

		--Test5  
		assertTermEquals ( `g(f(b()),c()) , test5 );
		assertTermEquals ( `g(f(b()),c()) , test5_New );
		
		--TestChoice
		assertTermEquals ( `f(b()) , testChoice );
		assertTermEquals ( `f(b()) , testChoice_New );

		--TestChoiceSideEffect
		assertTermEquals ( `f(b()) , testChoiceSideEffect );
		assertTermEquals ( `f(b()) , testChoiceSideEffect_New );

		--TestNot
		assertTermEquals ( `f(b()) , testNot );
		assertTermEquals ( `f(b()) , testNot_New );

		--TestNotSideEffect
		assertTermEquals ( `f(a()) , testNotSideEffect );
		assertTermEquals ( `f(a()) , testNotSideEffect_New );

		--Test6
		assertTermEquals (`g(f(c()),c()) , test6 );
		assertTermEquals (`g(f(c()),c()) , test6_New );

		--Test7
		assertTermEquals ( `g(f(b()),b()) , test7 );
		assertTermEquals ( `g(f(b()),b()) , test7_New );

		--Test8
		assertTermEquals ( `g(f(a()),c()) , test8 );
		assertTermEquals ( `g(f(a()),c()) , test8_New );


		--Test11  
		assertTermEquals ( `g(f(c()),c()) , test11 );
		assertTermEquals ( `g(f(c()),c()) , test11_New );

		--Test12
		assertTermEquals ( `g(f(c()),c()) , test12 ); 
		assertTermEquals ( `g(f(c()),c()) , test12_New ); 

		--Test13
		assertTermEquals ( `g(f(c()),c()) , test13 );
		assertTermEquals ( `g(f(c()),c()) , test13_New );
		

    end;    
    
end TestBehaviour;
