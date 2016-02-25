with VisitablePackage, ObjectPack;
use  VisitablePackage, ObjectPack;
package body VisitableBuiltinPackage is


	function makeVisitableBuiltinPackage(o: ObjectPtr) return VisitableBuiltin is
		ret : VisitableBuiltin;
	begin
		ret.builtin := o;
		return ret;
	end;
	
	function getBuiltin(v: VisitableBuiltin) return ObjectPtr is
	begin
		return v.builtin;
	end;

	function setChildren(v: access VisitableBuiltin ; children : ObjectPtrArrayPtr) return VisitablePtr is
		IndexOutOfBoundsException : exception;
	begin
		if children /= null then
			raise IndexOutOfBoundsException;
		end if;
		return VisitablePtr(v);
	end;
	
	function getChildren(v: access VisitableBuiltin) return ObjectPtrArrayPtr is
		ret : ObjectPtrArrayPtr := new ObjectPtrArray(0..0);
	begin
		ret(0) := new VisitableBuiltin;
		return ret;
	end;
	
	function getChildAt(v: access VisitableBuiltin; i : Integer) return VisitablePtr is
		error : exception;
	begin
		raise Error;
		return null;
	end;
	
	function setChildAt(v: access VisitableBuiltin; i: in Integer; child: in VisitablePtr) return VisitablePtr is
		error : exception;
	begin
		raise Error;
		return VisitablePtr(v);
	end;
	
	function getChildCount(v: access VisitableBuiltin) return Integer is
	begin
		return 0;
	end;
	
	function toString(vb: VisitableBuiltin) return String is
	begin
		return toString(vb.builtin.all);
	end;
    

end VisitableBuiltinPackage;
