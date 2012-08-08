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
	
	%op Term f(x:Term, y:Term) {
	  is_fsym(t) {$t.all in Term_F'Class}
	  get_slot(x, t) { $t.x }
	  get_slot(y, t) { $t.y }
	  make(t0,t1)	 {new Term_F'($t0,$t1)}
	}
	
	---------------------------------------------------------------------------
	-- Strategy TOM
	---------------------------------------------------------------------------
	
	%strategy RuleAB() extends Fail() {
		visit Term {
			a() -> b()
		}
	}
    
	%strategy RuleBC() extends Fail() {
		visit Term {
			b() -> c()
		}
	}
    
	%strategy RuleCA() extends Fail() {
		visit Term {
			c() -> a()
		}
	}
    
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
		doThis(`RuleAB(), `a(), `b());

		put("(a -> b)[b] = ");
		doThis(`RuleAB(), `b(), null);

		put("(a -> b)[f(a,a)] = ");
		doThis(`RuleAB(), `f(a(), a()), null);

		put("(Identity)[a] = ");
		doThis(`Identity(), `a(), `a());

		put("(Identity)[b] = ");
		doThis(`Identity(), `b(), `b());

		put("(Identity)[f(a,a)] = ");
		doThis(`Identity(), `f(a(),a()), `f(a(),a()));

		put("(Fail)[a] = ");
		doThis(`Fail(), `a(), null);

		put_line("");

		-- Sequence
		put("(Sequence(a -> b, b -> c))[a] = ");
		doThis(`Sequence(RuleAB(), RuleBC()), `a(), `c());

		put("(Sequence(a -> b, c -> a))[a] = ");
		doThis(`Sequence(RuleAB(), RuleCA()), `a(), null);

		put("(Sequence(b -> c, a -> b))[a] = ");
		doThis(`Sequence(RuleBC(), RuleAB()), `a(), null);

		put_line("");

		-- Choice

		put("(Choice(a -> b, b -> c))[a] = ");
		doThis(`Choice(RuleAB(), RuleBC()), `a(), `b());

		put("(Choice(b -> c, a -> b))[a] = ");
		doThis(`Choice(RuleBC(), RuleAB()), `a(), `b());

		put("(Choice(b -> c, c -> a))[a] = ");
		doThis(`Choice(RuleBC(), RuleCA()), `a(), null);

		put("(Choice(b -> c, Identity))[a] = ");
		doThis(`Choice(RuleBC(), Identity()), `a(), `a());

		put_line("");

		-- Not
		put("(Not(a -> b))[a] = ");
		doThis(`Not(RuleAB()), `a(), null);

		put("(Not(b -> c))[a] = ");
		doThis(`Not(RuleBC()), `a(), `a());

		put_line("");

		-- Try
		put("(Try(b -> c))[a] = ");
		doThis(`Try(RuleBC()), `a(), `a());

		put_line("");

		-- All
		put("(All(a -> b))[f(a,a)] = ");
		doThis(`All(RuleAB()), `f(a(), a()), `f(b(), b()));

		put("(All(a -> b))[f(a,b)] = ");
		doThis(`All(RuleAB()), `f(a(), b()), null);

		put("(All(a -> b))[a] = ");
		doThis(`All(RuleAB()), `a(), `a());

		put("(All(Try(a -> b)))[f(a,c)] = ");
		doThis(`All(Try(RuleAB())), `f(a(), c()), `f(b(), c()));

		put_line("");

		-- One
		put("(One(a -> b))[f(a,a)] = ");
		doThis(`One(RuleAB()), `f(a(), a()), `f(b(), a()));

		put("(One(a -> b))[f(b,a)] = ");
		doThis(`One(RuleAB()), `f(b(), a()), `f(b(), b()));

		put("(One(a -> b))[a] = ");
		doThis(`One(RuleAB()), `a(), null);
		
		put_line("");

		-- Repeat
		put("((Repeat(a -> b))[a] = ");
		doThis(`Repeat(RuleAB()), `a(), `b());
		
		put("(Repeat(Choice(b -> c, a -> b)))[a] = ");
		doThis(`Repeat(Choice(RuleBC(), RuleAB() )), `a(), `c());
		
		put("((Repeat(b -> c))[a] = ");
		doThis(`Repeat(RuleBC()), `a(), `a());
	
    end;    
    
end Test;
