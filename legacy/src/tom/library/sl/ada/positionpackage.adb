with OmegaStrategy, SequenceStrategy, StrategyPackage, Ada.Text_IO;
use  OmegaStrategy, SequenceStrategy, StrategyPackage, Ada.Text_IO;

package body PositionPackage is

	function makeFromArrayWithoutCopy(arr: IntArrayPtr) return Position is
	res : Position := (omega => arr);
	begin
		return res;
	end makeFromArrayWithoutCopy;

	function make return Position is
	begin
		if PositionPackage.ROOT = null then
		  PositionPackage.ROOT := new Position'(makeFromArrayWithoutCopy(null));
		end if;
		return PositionPackage.ROOT.all;
	end make;

	procedure arraycopy(src: in IntArrayPtr; srcPos: in Natural; dest: in out IntArrayPtr; destPos: in Natural; length: in Natural) is
	begin
		dest(destPos..destPos+length-1) := src( srcPos .. srcPos+length-1 );
	end arraycopy;

	function makeFromSubarray(src : not null IntArrayPtr ; srcIndex: Integer ; length: Natural) return Position is
	arr : IntArrayPtr := null;
	begin
		if length /= 0 then
			arr := new IntArray(0.. length-1);
			arr.all := src(srcIndex .. srcIndex + length-1);
		end if;
		return makeFromArrayWithoutCopy(arr);
	end makeFromSubarray;

	function makeFromArray(arr: IntArrayPtr) return Position is
	begin
		if arr /= null then
			return makeFromSubarray(arr, arr'First, arr'Length);
		else
			return makeFromArrayWithoutCopy(null);
		end if;
	end makeFromArray;

	function makeFromPath(p : Position) return Position is
	begin
		return makeFromArrayWithoutCopy( toIntArray(p) );
	end makeFromPath;

	function clone(pos: Position) return Position is
	res : Position := Position'(omega => new IntArray'( pos.Omega.all ));
	begin
		return res;
	end clone;

	function hashCode(p: Position) return Integer is
	hashCode : Integer := 0;
	begin
		if p.omega /= null then
			for i in p.omega'First..p.omega'Last loop
			  hashCode := hashCode * 31 + p.omega(i);
			end loop;
		end if;
		return hashCode;
	end hashCode;


	function equals(p1, p2: Position) return Boolean is
	cp1 : Position := getCanonicalPath(p1);
	cp2 : Position := getCanonicalPath(p2);
	begin
		if length(cp1) = length(cp2) then
			for i in 0..length(cp1)-1 loop
				if cp1.omega( cp1.omega'First + i) /= cp2.omega( cp2.omega'First + i) then
					return false;
				end if;
			end loop;
			return true;
		end if;
		return false;
	end equals;

	function compare(p1,path: Position) return Integer is
	p2 : Position := makeFromPath(path);
	begin
		for i in 0..length(p1)-1 loop
			if i = length(p2) or else p1.omega(p1.omega'First + i) > p2.omega(p2.omega'First + i) then
				return 1;
			elsif p1.omega(p1.omega'First + i) < p2.omega(p2.omega'First + i) then
				return -1;
			end if;
		end loop;
		
		if length(p1) = length(p2) then
			return 0;
		else
			return -1;
		end if;
	end compare;

	function toString(p: Position) return String is
	r : access String := new String'("[");
	begin

		if p.omega /= null then
			for I in p.omega'First..p.omega'Last loop
				r := new String'(r.all & Integer'Image(p.omega(i)));
				
				if i /= p.omega'Last then
					r := new String'(r.all & ",");
				end if;
				
			end loop;
		end if;
		r := new String'(r.all & "]");
		return r.all;
	end toString;

	------------------------------------------
	-- implementation of the Path interface
	------------------------------------------

	function add(p1, path: Position) return Position is
	ap, merge : IntArrayPtr := null;
	begin
		if length(path) = 0 then
			return clone(p1);
		end if;
		
		ap := toIntArray(path);
		merge := new IntArray(0..length(p1)+length(path)-1);
		arraycopy(p1.omega, 0, merge, 0, length(p1));
		arraycopy(ap, 0, merge, length(p1), length(path));
		return makeFromArrayWithoutCopy(merge);
	end add;

	function sub(p1, p2: Position) return Position is
	begin
		return add(inverse(makeFromPath(p2)), p1) ;
	end sub;

	function inverse(p:Position) return Position is
	inv : IntArrayPtr := null;
	begin
		if p.omega /= null then
			inv := new IntArray(0..length(p)-1);
			for I in 0..length(p)-1 loop
				inv(length(p)-(i+1)) := -p.omega(i);
			end loop;
		end if;
		return makeFromArrayWithoutCopy(inv);
	end inverse;

	function length(p: Position) return Natural is
	begin
		if p.omega = null then
			return 0;
		else
			return p.omega'Length;
		end if;
	end length;

	function getHead(p: Position) return Integer is
	begin
		return p.omega(p.omega'First);
	end getHead;

	function getTail(p: Position) return Position is
		IndexOutOfBoundsException : exception;
	begin
		if length(p) = 0 then
			put_line("Empty list has no tail");
			raise IndexOutOfBoundsException;
		end if;
		return makeFromSubarray(p.omega, p.omega'First, length(p)-1);
	end getTail;

	function conc(p: Position; i: Integer) return Position is
	len : Natural := length(p);
	result : IntArrayPtr := new IntArray(0..len);
	begin
		if len /= 0 then
			arraycopy(p.omega, 0, result, 0, len);
		end if;
		result(len) := i;
		return makeFromArrayWithoutCopy(result);
	end conc;

	function getCanonicalPath(p: Position) return Position is
	len : Integer := length(p);
	stack : IntArrayPtr := null;
	top : Integer := -1;
	begin

		if len = 0 then
			return make;
		end if;

		stack := new IntArray(0..len-1);
		
		for i in 0..len-1 loop
			if top >=0 and then stack(top) = -p.omega(i) then
				top := top - 1;
			else
				top := top +1 ;
				stack(top) := p.omega(i);
			end if;
		end loop;
		return makeFromSubarray(stack, 0, top+1);
	end getCanonicalPath;

	function toIntArray(p: Position) return IntArrayPtr is
	len : Integer := length(p);
	arr : IntArrayPtr := null;
	begin
		if len /= 0 then
			arr := new IntArray(0..len-1);
			arraycopy(p.omega, 0, arr, 0, len);
		end if;
		return arr;
	end toIntArray;
	
	
	function up(pos: Position) return Position is
	begin
		return makeFromSubarray(pos.omega, 0, length(pos) - 1);
    end up;
    
    function down(pos: Position; i: Integer) return Position is
	arr : IntArrayPtr := new IntArray(0..length(pos));
    begin
		arr(arr'First..arr'Last-1) := pos.omega(pos.omega'Range);
		arr(arr'Last) := i;
		return makeFromArrayWithoutCopy(arr);
	end down;
    
	function hasPrefix(pos, pref: Position) return Boolean is
	prefixTab : IntArray := pref.omega.all; 
	begin
		if length(pos) < prefixTab'Length then
			return False;
		end if;
		
		for i in 0 .. prefixTab'Length -1 loop
			if  prefixTab(i) /= pos.omega(i) then
				return False;
			end if;
		end loop;
		
		return True;
		
	end hasPrefix;
	
	function changePrefix(pos, oldprefix, prefix: Position) return Position is
	arr : IntArrayPtr := new IntArray(0..length(pos)+length(prefix)-length(oldprefix)-1);
	begin
		if not hasPrefix(pos, oldprefix) then
			return pos;
		end if;
		
		arraycopy(prefix.omega, 0, arr, 0, length(prefix));
		arraycopy(pos.omega, 0, arr, length(prefix), length(pos));
		return makeFromArrayWithoutCopy(arr);
		
	end changePrefix;

	function getOmega(p: Position; v: Strategy'Class) return Strategy'Class is
		res : StrategyPtr := new Strategy'Class'( v ) ;
		tmp : StrategyPtr := null;
	begin
		for i in p.omega'Last..p.omega'First loop
			tmp := new Omega;
			makeOmega(Omega(tmp.all), p.omega(i), res);
			res := tmp;
		end loop;
		return res.all;
	end;
	
	function getOmegaPathAux(pos: Position; v: Strategy'Class; i : Integer) return StrategyPtr is
		ptr1,ptr2 : StrategyPtr := null;
	begin
		if  i > length(pos) then
			return new Strategy'Class'(v);
		else
			ptr1 := new Omega;
			makeOmega(Omega(ptr1.all), pos.omega(i), getOmegaPathAux(pos, v, i+1));
			ptr2 := new Strategy'Class'( v );
			return SequenceStrategy.make(ptr1, ptr2);
		end if;
	end;
	
	function getOmegaPath(pos: Position; v: Strategy'Class) return StrategyPtr is
	begin
		return getOmegaPathAux(pos, v, 1);
	end;
	
	--function getReplace(pos: Position; t: ObjectPtr) return StrategyPtr is
	--begin
	--	return null;
	--end;
	
	--function getSubterm return StrategyPtr is
	--begin
	--	return null;
	--end;


end PositionPackage;
