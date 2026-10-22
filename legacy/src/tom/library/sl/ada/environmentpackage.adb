with Ada.Text_IO, Ada.Characters.Latin_1, VisitableIntrospectorPackage, VisitablePackage;
use  Ada.Text_IO, Ada.Characters.Latin_1, VisitableIntrospectorPackage, VisitablePackage;

package body EnvironmentPackage is

	--private
	procedure makeEnvironment(env: in out Environment; len: Integer ; intro: IntrospectorPtr) is
	begin
		env.omega := new IntArray(0..len);
		env.subterm := new ObjectPtrArray(0..len);
		env.current := 0; -- root is in subterm(0)
		env.omega(0) := 0; -- the first cell is not used
		env.introspector := intro;
	end;
	
	procedure makeEnvironment(env: in out Environment) is
	begin
		makeEnvironment(env, DEFAULT_LENGTH, getInstance);
	end;
	
	procedure makeEnvironment(env: in out Environment; intro: IntrospectorPtr) is
	begin
		makeEnvironment(env, DEFAULT_LENGTH, intro);
	end;
	
	function newEnvironment return EnvironmentPtr is
		ret : EnvironmentPtr := new Environment;
	begin
		makeEnvironment(ret.all);
		return ret;
	end;
	
	function newEnvironment(intro: IntrospectorPtr) return EnvironmentPtr is
		ret : EnvironmentPtr := new Environment;
	begin
		makeEnvironment(ret.all, intro);
		return ret;
	end;
	
	--private
	procedure ensureLength(env: in out Environment; minLength: Integer) is
		max : Integer := Integer'Max(env.omega'Length * 2 , minLength);
		newOmega : IntArrayPtr := new IntArray(0..max-1);
		newSubterm : ObjectPtrArrayPtr := new ObjectPtrArray(0..max-1);
	begin
		if minLength > env.omega'Length then
			newOmega( env.omega'Range ) := env.omega( env.omega'Range );
			newSubterm( env.omega'Range ) := env.subterm( env.subterm'Range );
			env.omega := newOmega;
			env.subterm := newSubterm;
		end if;
	end;
	
	function clone(env: Environment) return Environment is
	newEnv : Environment := env;
	begin
		newEnv.omega := new IntArray(env.omega'Range);
		newEnv.subterm := new ObjectPtrArray(env.subterm'Range);
		
		newEnv.omega( newEnv.omega'Range ) := env.omega( env.omega'Range );
		newEnv.subterm( newEnv.subterm'Range ) := env.subterm( env.subterm'Range );
		
		return newEnv;
	end;

	function equals(env1, env2 : Environment) return Boolean is
		c : Integer := env1.current;
	begin
		if env1.current = env2.current and then
			env1.omega(0..c) = env2.omega(0..c) and then env2.subterm(0..c) = env2.subterm(0..c) then
			return true;
		else
			return False;
		end if;
	end;
	
	function hashCode(env: Environment) return Integer is
	begin
		return 0;
	end;
	
	function getStatus(env: Environment) return Integer is
	begin
		return env.status;
	end;
	
	procedure setStatus(env: in out Environment; s: Integer) is
	begin
		env.status := s;
	end;
	
	function getRoot(env: Environment) return ObjectPtr is
	begin
		return env.subterm(env.subterm'First);
	end;
	
	procedure setRoot(env: in out Environment; r: ObjectPtr) is
	begin
		env.subterm(env.subterm'First) := r;
	end;
	
	function getCurrentStack(env: Environment) return ObjectPtrArray is
	begin
		return env.subterm(0..env.current-1);
	end;
	
	function getAncestor(env: Environment) return ObjectPtr is
	begin
		return env.subterm(env.current-1);
	end;
	
	function getSubject(env: Environment) return ObjectPtr is
	begin
		return env.subterm(env.current);
	end;
	
	procedure setSubject(env: in out Environment; root: ObjectPtr) is
	begin
		env.subterm(env.current) := root;
	end;
	
	function getIntrospector(env: Environment) return IntrospectorPtr is
	begin
		return env.introspector;
	end;
	
	procedure setIntrospector(env: in out Environment; i: IntrospectorPtr) is
	begin
		env.introspector := i;
	end;
	
	function getSubOmega(env: Environment) return Integer is
	begin
		return env.omega(env.current);
	end;
	
	function depth(env: Environment) return Integer is
	begin
		return env.current;
	end;
	
	function getPosition(env: Environment) return Position is
	begin
		return PositionPackage.makeFromSubarray(env.omega, env.omega'First+1, depth(env));
	end;
	

	procedure up(env: in out Environment) is
		childIndex : Integer := env.omega(env.current)-1;
		child : ObjectPtr := env.subterm(env.current);
	begin
		env.current := env.current - 1;
		env.subterm(env.current) := setChildAt(env.introspector, env.subterm(env.current) , childIndex, child);
	end;
	
	procedure upLocal(env: in out Environment) is
	begin
		env.current := env.current - 1;
	end;
	
	procedure down(env: in out Environment; n: Integer) is
		child : ObjectPtr := null;
	begin
		--put_line("before down: " & toString(env));
		if n > 0 then
			child := env.subterm(env.current);
			env.current := env.current + 1;
			if env.current = env.omega'length then
				ensureLength(env, env.current + 1);
			end if;
			env.omega(env.current) := n;
			env.subterm(env.current) := getChildAt(env.introspector, child, n-1);
		end if;
		--put_line("after down: " & toString(env));
	end;
	
	--private
	procedure genericFollowPath(env: in out Environment; p: Path'Class; local: Boolean) is
		normalizerdPathArray : IntArrayPtr := toIntArray( getCanonicalPath( p ) );
		len : Integer := normalizerdPathArray'Length;
	begin
		for i in normalizerdPathArray'Range loop
		
			if normalizerdPathArray(i) > 0 then
				down(env, normalizerdPathArray(i));
				if env.subterm(env.current).all in Position'Class and then i+1 < normalizerdPathArray'Last then
					genericFollowPath(env, Position(env.subterm(env.current).all), local);
				end if;
			elsif local then
				upLocal(env);
			else
				up(env);
			end if;
		
		end loop;
	end;
	
	procedure followPath(env: in out Environment; p: Path'Class) is
	begin
		genericFollowPath(env, p, false);
	end;
	
	procedure followPathLocal(env: in out Environment; p: Path'Class) is
	begin
		genericFollowPath(env, p, true);
	end;
	
	procedure goToPosition(env: in out Environment; p: Position) is
	begin
		followPath(env, PositionPackage.sub(p, getPosition(env)));
	end;
	
	overriding
	function toString(env: Environment) return String is
		str : access String := new String'("[");
	begin
		for i in 0..env.current loop
			str := new String'(str.all & Integer'Image( env.omega(i) ) );
			
			if i < env.current then
				str := new String'(str.all & "," );
			end if;
		end loop;
		
		str := new String'(str.all & "]" & LF & "[");
		
		for i in 0..env.current loop
			if env.subterm(i) = null then
				str := new String'(str.all & "null" );
			else
				str := new String'(str.all & ObjectPack.toString( env.subterm(i).all ) );
			end if;
			
			if i < env.current then
				str := new String'(str.all & "," );
			end if;
		end loop;
		
		str := new String'(str.all & "]");
		
		return str.all;
	end;
	
end EnvironmentPackage;


