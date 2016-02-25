with VisitablePackage, EnvironmentPackage, AbstractStrategyBasicPackage;
use  VisitablePackage, EnvironmentPackage, AbstractStrategyBasicPackage;

with Ada.Text_IO; use Ada.Text_IO;
package body AllsStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(o: Alls) return String is
	begin
		return "Alls()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access Alls; any: ObjectPtr; intro: access Introspector'Class) return ObjectPtr is
		childs : ObjectPtrArrayPtr := null;
		childCount : Integer := getChildCount(intro, any);
	begin
		for i in 0..childCount-1 loop
			declare
				oldChild : ObjectPtr := getChildAt(intro, any, i);
				newChild : ObjectPtr := visitLight(StrategyPtr(str.arguments(ARG)), oldChild, intro);
			begin
				if childs /= null then
					childs(i) := newChild;
				else
					childs := getChildren(intro, any);
					childs(i) := newChild;
				end if;
			end;
		end loop;
		
		if childs = null then
			return any;
		else
			return setChildren(intro, any, childs);
		end if;
	end;
	
	overriding
	function visit(str: access Alls; intro: access Introspector'Class) return Integer is
		any : ObjectPtr := null;
		childs: ObjectPtrArrayPtr := null;
		childCount : Integer;
	begin
		setIntrospector(str.env.all, IntrospectorPtr(intro));
		any := getSubject(str.env.all);
		
		childCount := getChildCount(intro, any);
		
		for i in 0 .. childCount-1 loop
			declare
				oldChild : ObjectPtr := getChildAt(intro, any, i);
				newChild : ObjectPtr := null;
				status : Integer;
			begin

				down(str.env.all, i+1);
				
				status := visit(StrategyPtr(str.arguments(ARG)), intro);

				if status /= EnvironmentPackage.SUCCESS then
					upLocal(str.env.all);
					return status;
				end if;
				
				newChild := getSubject(str.env.all);
				
				if childs /= null then
					childs(i) := newChild;
				else
					childs := getChildren(intro, any);
					childs(i) := newChild;
				end if;

				upLocal(str.env.all);

			end;
		end loop;
		
		if childs /= null then
			setSubject(str.env.all, setChildren(intro, any, childs));
		end if;

		return EnvironmentPackage.SUCCESS;
		
	end;
	
	----------------------------------------------------------------------------

	procedure makeAlls(i : in out Alls; v: StrategyPtr) is
	begin
		initSubterm(i,v);
	end;
	
	function newAlls(v: StrategyPtr) return StrategyPtr is
		id : StrategyPtr := new Alls;
	begin
		makeAlls(Alls(id.all), v);
		return id;
	end;

	----------------------------------------------------------------------------
end AllsStrategy;
