
package body AbstractStrategyCombinatorPackage is

	procedure initSubterm(sc : in out AbstractStrategyCombinator) is
	begin
		sc.arguments := null;
	end;
		
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str : Strategy'Class) is
		strptr : StrategyPtr := new Strategy'Class'(str);
	begin
		sc.arguments := new ObjectPtrArray'( 0 => ObjectPtr(strptr) );
	end;
	
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str1,str2 : Strategy'Class) is
		strptr1 : StrategyPtr := new Strategy'Class'(str1);
		strptr2 : StrategyPtr := new Strategy'Class'(str2);
	begin
		sc.arguments := new ObjectPtrArray'( ObjectPtr(strptr1), ObjectPtr(strptr2)  );
	end;
	
	
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str1,str2,str3 : Strategy'Class) is
		strptr1 : StrategyPtr := new Strategy'Class'(str1);
		strptr2 : StrategyPtr := new Strategy'Class'(str2);
		strptr3 : StrategyPtr := new Strategy'Class'(str3);
	begin
		sc.arguments := new ObjectPtrArray'( ObjectPtr(strptr1), ObjectPtr(strptr2), ObjectPtr(strptr3)  );
	end;
	
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str : ObjectPtrArray) is
	begin
		sc.arguments := new ObjectPtrArray( str'range );
		
		for i in str'range loop
			sc.arguments(i) := new Object'Class'( str(i).all );
		end loop;
		
	end;
	
	function getVisitors(sc: AbstractStrategyCombinator) return ObjectPtrArray is
		arr :  ObjectPtrArray(sc.arguments'range);
	begin
		for i in arr'range loop
			arr(i) := new Object'Class'( sc.arguments(i).all );
		end loop;
		
		return arr;
	end;
	
	function getVisitor(sc: AbstractStrategyCombinator ; i : Integer) return Strategy'Class is
	begin
		return Strategy'Class(sc.arguments(i).all);
	end;
	
	----------------------------------------------------------------------------
	-- Visitable implementation
	----------------------------------------------------------------------------
	
	overriding
	function getChildCount(v: AbstractStrategyCombinator) return Integer is
	begin
		return v.arguments'Length;
	end;
	
	overriding
	function getChildAt(v: AbstractStrategyCombinator; i : Integer) return Visitable'Class is
	begin
		return Visitable'Class( v.arguments(i).all );
	end;
	
	overriding
	procedure setChildAt(v: in out AbstractStrategyCombinator; i: in Integer; child: in Visitable'Class) is
	begin
		v.arguments(i) := new Object'Class'( Object'Class(child) );
	end;
	
	overriding
	procedure setChildren(v: in out AbstractStrategyCombinator ; children : ObjectPtrArray) is
	begin
		v.arguments := new ObjectPtrArray(children'range);
		
		for i in children'range loop
			v.arguments(i) := new Object'Class'( children(i).all );
		end loop;
	end;
	
	overriding
	function getChildren(v: AbstractStrategyCombinator) return ObjectPtrArray is
	begin
		return v.arguments.all;
	end;
	
	----------------------------------------------------------------------------
	
end AbstractStrategyCombinatorPackage;

