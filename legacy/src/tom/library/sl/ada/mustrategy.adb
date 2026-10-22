with VisitFailurePackage, VisitablePackage, EnvironmentPackage, MuVarStrategy;
use  VisitFailurePackage, VisitablePackage, EnvironmentPackage, MuVarStrategy;

with Ada.Text_IO; use Ada.Text_IO;
package body MuStrategy is
	
	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(c: Mu) return String is
	begin
		return "Mu";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access Mu; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
	begin
		if not(str.expanded) then
			expand(StrategyPtr(str));
			str.expanded := true;
		end if;

		return visitLight(StrategyPtr(str.arguments(V)), any, i);
	end;
	
	overriding
	function visit(str: access Mu; i: access Introspector'Class) return Integer is
	begin
		if not(str.expanded) then
			expand(StrategyPtr(str));
			str.expanded := true;
		end if;

		return visit(StrategyPtr(str.arguments(V)), i);
	end;
	
	function isExpanded(str: access Mu) return Boolean is
	begin
		return isExpanded( MuVar(str.arguments(V).all)'Access );
	end;
	
	procedure expand(any,parent: StrategyPtr ; childNumber : Integer; set: in out StrategyStr_Sets.Set; stack : in out StrategyStr_LL.List) is
		use StrategyStr_LL;
	begin
		if set.contains(any) then
			return;
		else
			declare
				cur: StrategyStr_Sets.Cursor;
				suc: Boolean;
			begin
				-- the call of insert with 3 parameter prevent from raising
				-- an exception when parent is already in the set
				set.Insert(parent, cur, suc);
			end;
		end if;
		
		if any.all in Mu'Class then
			declare
				anyMu : MuPtr := MuPtr(any);
				vptr : StrategyPtr := StrategyPtr( any.getChildAt(MuStrategy.V) );
			begin
				stack.prepend(anyMu);
				expand(vptr, StrategyPtr(anyMu),0,set,stack);
				expand(StrategyPtr(any.getChildAt(MuStrategy.VAR)),null,0,set,stack);
				stack.delete_first;
				return;
			end;
		else
			if any.all in MuVar'Class then
				declare
					muvariable : MuVarPtr := MuVarPtr(any);
					n : access String := getName(muvariable);
					visitptr : VisitablePtr; -- not used
				begin
					if not(muvariable.isExpanded) then
						declare
							curseur : Cursor;
							m : MuPtr;
							tmpstr : access String;
						begin
							curseur := stack.First;

							loop
								m := Element(curseur);
								
								tmpstr := MuVarPtr(m.arguments(MuStrategy.VAR)).getName;
								
								if (tmpstr = null and then n = null) or else (tmpstr/=null and then n/=null and then tmpstr.all = n.all) then
									muvariable.setInstance(StrategyPtr(m));
									if parent /= null then
										visitptr := parent.setChildAt(childNumber, VisitablePtr(m.arguments(MuStrategy.V)));
									end if;
									return;
								end if;
								
								exit when curseur = stack.Last;
								curseur := Next(curseur);
							end loop;
						end;
					end if;
				end;
			end if;
		end if;
		
		declare
			childCount : Integer := any.getChildCount - 1;
		begin
			for i in 0..childCount loop
				expand( StrategyPtr(any.getChildAt(i)), any, i, set, stack);
			end loop;
		end;
	end;
	
	procedure expand(s: StrategyPtr) is
		newSet : StrategyStr_Sets.Set;
		newLL : StrategyStr_LL.List;
	begin
		expand(s, null, 0, newSet, newLL);
	end;
	
	----------------------------------------------------------------------------
	
	procedure makeMu(c : in out Mu; var,v: StrategyPtr) is
	begin
		initSubterm(c, var, v);
	end;

	function newMu(var, v: StrategyPtr) return StrategyPtr is
		ret : StrategyPtr := new Mu;
	begin
		makeMu(Mu(ret.all), var, v);
		return ret;
	end;
	----------------------------------------------------------------------------
	
	

end MuStrategy;
