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

	procedure setChildren(v: in out VisitableBuiltin ; children : ObjectPtrArray) is
		error : exception;
	begin
		if children'Length > 0 then
			raise Error;
		end if;
	end;
	
	function getChildren(v: VisitableBuiltin) return ObjectPtrArray is
		ret : ObjectPtrArray(0..0);
	begin
		ret(0) := new VisitableBuiltin;
		return ret;
	end;
	
	function getChildAt(v: VisitableBuiltin; i : Integer) return VisitableBuiltin is
		error : exception;
		ret : VisitableBuiltin;
	begin
		raise Error;
		return ret;
	end;
	
	procedure setChildAt(v: in out VisitableBuiltin; i: in Integer; child: in VisitableBuiltin) is
		error : exception;
	begin
		raise Error;
	end;
	
	function getChildCount(v: VisitableBuiltin) return Integer is
	begin
		return 0;
	end;
	
	function toString(vb: VisitableBuiltin) return String is
	begin
		return toString(vb.builtin.all);
	end;
    

end VisitableBuiltinPackage;
