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

with System, System.Storage_Elements;
use  System, System.Storage_Elements;
    
package body TestSLModes is

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
    -- Main
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

	%strategy R3() extends `Fail() {
		visit Term {
			f(b()) -> { return `f(c()); }
			c() -> { return `a(); }
		}
	}

	%strategy R4() extends `Identity() {
		visit Term {
			f(b()) -> { return `f(c()); }
			c() -> { return `a(); }
		}
	}
    
	procedure assertTermEquals(a,b: TermPtr ; str : String) is
	begin
		assert( toString(Object'Class(a.all)) = toString(Object'Class(b.all)) , str ); 

	end;
	
    procedure testVisitLight(strat: StrategyPtr; toVisit: TermPtr; expected: TermPtr) is
   		resultat : TermPtr := null;
    begin
    	resultat := TermPtr( visitLight(strat, VisitablePtr(toVisit)) );


    	if expected = null then
    		assert(resultat = null, "Expected to fail " & toString(Object'Class(strat.all)));
		else
			assert(resultat /= null, "Should not be null " & toString(Object'Class(strat.all)));
			assertTermEquals(resultat, expected, "Not the result expected " & toString(Object'Class(strat.all)));
    	end if;
    	
    	exception when VisitFailurePackage.VisitFailure =>
    		assert(expected = null, "Not expected to fail " & toString(Object'Class(strat.all)));
    end;
    
    procedure testVisit(strat: StrategyPtr; toVisit: TermPtr; expected: TermPtr) is
   		resultat : TermPtr := null;
    begin
    	resultat := TermPtr( visit(strat, VisitablePtr(toVisit)) );


    	if expected = null then
    		assert(resultat = null, "Expected to fail " & toString(Object'Class(strat.all)));
		else
			assert(resultat /= null, "Should not be null " & toString(Object'Class(strat.all)));
			assertTermEquals(resultat, expected, "Not the result expected "  & toString(Object'Class(strat.all)));
    	end if;
    	
    	exception when VisitFailurePackage.VisitFailure =>
    		assert(expected = null, "Not expected to fail "  & toString(Object'Class(strat.all)));
    end;

	procedure run_test is
	begin


		--          Strategy  ,        Term     ,     expected result
		
		testVisit( `Identity(),        `f(a())  ,       `f(a())           );		
		testVisit( `R1()      ,        `f(a())  ,       `f(b())           );
		testVisit( `R1()      ,        `f(b())  ,       `f(b())           );
		testVisit( `R2()      ,        `f(a())  ,       `f(b())           );
		testVisit( `R2()      ,        `f(b())  ,         null            );
		
		testVisit( `One(R1()) ,        `f(b())  ,       `f(c())           );
		testVisit( `One(R2()) ,        `f(a())  ,         null            );
		
		testVisit( `OnceBottomUp((R2())) , `g(f(a()),b()) ,`g(f(b()),b()) );
		testVisit( `OnceBottomUp((R2())) , `g(f(b()),b()) ,`g(f(c()),b()) );
		testVisit( `OnceBottomUp((R2())) , `g(f(c()),b()) ,`g(f(c()),c()) );
		testVisit( `OnceBottomUp((R2())) , `g(f(c()),c()) ,    null       );
		
		testVisit( `All((R2())) , `g(f(a()),b())  ,   `g(f(b()),c())      );
		testVisit( `All((R2())) , `g(f(b()),c())  ,       null            );
		testVisit( `All((R2())) , `g(f(b()),b())  ,       null            );
		
		testVisit( `Omega(0,R2()) , `f(a())         ,       `f(b())       );
		testVisit( `Omega(1,R2()) , `g(f(a()),b())  ,    `g(f(b()),b())   );
		testVisit( `Omega(2,R2()) , `g(f(a()),b())  ,    `g(f(a()),c())   );
		testVisit( `Omega(1,R2()) , `g(f(b()),b())  ,         null        );
		testVisit( `Omega(3,R2()) , `g(f(b()),b())  ,         null        );
		
		testVisit( `Sequence(R2(),R3())       , `f(a())  ,   `f(c())      );
		testVisit( `Sequence(R2(),Identity()) , `f(c())  ,     null       );
		testVisit( `Sequence(Identity(),R3()) , `f(c())  ,     null       );
		
		testVisit( `Not(R2())       ,       `f(b())   ,     `f(b())       );
		testVisit( `Not(R2())       ,       `f(a())   ,      null         );
		
		testVisit( `SequenceId(R1(),R4())       ,   `f(a()) ,  `f(c())    );
		testVisit( `SequenceId(R1(),Identity()) ,   `f(c()) ,  `f(c())    );

		testVisit( `Choice(R2(),R3()) , `f(a())     ,       `f(b())       );
		testVisit( `Choice(R3(),R2()) , `f(a())     ,       `f(b())       );
		testVisit( `Choice(R3(),R3()) , `f(a())     ,         null        );
		
		testVisit( `ChoiceId(R2(),R3())       , `f(a()) ,    `f(b())      );
		testVisit( `ChoiceId(Identity(),R2()) , `f(a()) ,    `f(b())      );
		testVisit( `ChoiceId(R2(),R2())       , `f(b()) ,      null       );
		
		-- The same but with visitLight
		
		testVisitLight( `Identity(),        `f(a())  ,       `f(a())           );		
		testVisitLight( `R1()      ,        `f(a())  ,       `f(b())           );
		testVisitLight( `R1()      ,        `f(b())  ,       `f(b())           );
		testVisitLight( `R2()      ,        `f(a())  ,       `f(b())           );
		testVisitLight( `R2()      ,        `f(b())  ,         null            );
		
		testVisitLight( `One(R1()) ,        `f(b())  ,       `f(c())           );
		testVisitLight( `One(R2()) ,        `f(a())  ,         null            );
		
		testVisitLight( `OnceBottomUp((R2())) , `g(f(a()),b()) ,`g(f(b()),b()) );
		testVisitLight( `OnceBottomUp((R2())) , `g(f(b()),b()) ,`g(f(c()),b()) );
		testVisitLight( `OnceBottomUp((R2())) , `g(f(c()),b()) ,`g(f(c()),c()) );
		testVisitLight( `OnceBottomUp((R2())) , `g(f(c()),c()) ,    null       );
		
		testVisitLight( `All((R2())) , `g(f(a()),b())  ,   `g(f(b()),c())      );
		testVisitLight( `All((R2())) , `g(f(b()),c())  ,       null            );
		testVisitLight( `All((R2())) , `g(f(b()),b())  ,       null            );
		
		testVisitLight( `Omega(0,R2()) , `f(a())         ,       `f(b())       );
		testVisitLight( `Omega(1,R2()) , `g(f(a()),b())  ,    `g(f(b()),b())   );
		testVisitLight( `Omega(2,R2()) , `g(f(a()),b())  ,    `g(f(a()),c())   );
		testVisitLight( `Omega(1,R2()) , `g(f(b()),b())  ,         null        );
		testVisitLight( `Omega(3,R2()) , `g(f(b()),b())  ,         null        );
		
		testVisitLight( `Sequence(R2(),R3())       , `f(a())  ,   `f(c())      );
		testVisitLight( `Sequence(R2(),Identity()) , `f(c())  ,     null       );
		testVisitLight( `Sequence(Identity(),R3()) , `f(c())  ,     null       );
		
		testVisitLight( `Not(R2())       ,       `f(b())   ,     `f(b())       );
		testVisitLight( `Not(R2())       ,       `f(a())   ,      null         );
		
		testVisitLight( `SequenceId(R1(),R4())       ,   `f(a()) ,  `f(c())    );
		testVisitLight( `SequenceId(R1(),Identity()) ,   `f(c()) ,  `f(c())    );

		testVisitLight( `Choice(R2(),R3()) , `f(a())     ,       `f(b())       );
		testVisitLight( `Choice(R3(),R2()) , `f(a())     ,       `f(b())       );
		testVisitLight( `Choice(R3(),R3()) , `f(a())     ,         null        );
		
		testVisitLight( `ChoiceId(R2(),R3())       , `f(a()) ,    `f(b())      );
		testVisitLight( `ChoiceId(Identity(),R2()) , `f(a()) ,    `f(b())      );
		testVisitLight( `ChoiceId(R2(),R2())       , `f(b()) ,      null       );
		
	end;    
    
end TestSLModes;

