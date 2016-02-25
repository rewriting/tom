with VisitFailurePackage, VisitablePackage, EnvironmentPackage;
use  VisitFailurePackage, VisitablePackage, EnvironmentPackage;

with Ada.Text_IO; use Ada.Text_IO;
package body MuVarStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(c: MuVar) return String is
		str : access String := new String'("[");
	begin
		if c.name = null then
			str := new String'(str.all & "null,");
		else
			str := new String'(str.all & c.name.all & ",");
		end if;
		
		if c.instance = null then
			str := new String'(str.all & "null]");
		else
			str := new String'(str.all & toString(Object'Class(c.instance.all)) & "]");
		end if;
		
		return str.all;
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access MuVar; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
	begin
		if str.instance /= null then
			return visitLight(str.instance, any, i);
		else
			raise VisitFailure;
		end if;
	end;
	
	overriding
	function visit(str: access MuVar; i: access Introspector'Class) return Integer is
	begin
		if str.instance /= null then
			return visit(str.instance, i);
		else
			return EnvironmentPackage.FAILURE;
		end if;
	end;
	
	----------------------------------------------------------------------------
	
	procedure makeMuVar(c : in out MuVar; s: access String) is
	begin
		initSubterm(c);
		if s /= null then
			c.name := new String'(s.all);
		else
			c.name := null;
		end if;
	end;

	function newMuVar(s : access String) return StrategyPtr is
		ret : StrategyPtr := new MuVar;
	begin
		makeMuVar(MuVar(ret.all), s);
		return ret;
	end;
	
	function newMuVar(s : String) return StrategyPtr is
	begin
		return newMuVar(new String'(s));
	end;
	
	function equals(m : access MuVar; o : ObjectPtr) return Boolean is
		mptr : access MuVar := null;
	begin
		if o /= null then
			if o.all in MuVar'Class then
				mptr := MuVar(o.all)'Access;
				if mptr.name /= null then
					return m.name.all = mptr.name.all;
				else
					if mptr.name = m.name and then mptr.instance = m.instance then
						return true;
					end if;
				end if;
			end if;
		end if;
		return false;
	end;
	
	function hashCode(m : access MuVar) return Integer is
	begin
		return 0;
	end;

	function getInstance(m: access MuVar) return StrategyPtr is
	begin
		return m.instance;
	end;

	procedure setInstance(m: access MuVar; s: StrategyPtr) is
	begin
		m.instance := s;
	end;

	procedure setName(m: access MuVar; n: access String) is
	begin
		if n /= null then
			m.name := new String'(n.all);
		else
			m.name := null;
		end if;
	end;

	function isExpanded(m: access Muvar) return Boolean is
	begin
		if m.instance = null then
			return false;
		else
			return true;
		end if;
	end;

	function getName(m: access Muvar) return access String is
	begin
		return m.name;
	end;


	----------------------------------------------------------------------------
	
	

end MuVarStrategy;
