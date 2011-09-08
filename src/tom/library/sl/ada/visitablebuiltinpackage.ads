with VisitablePackage, ObjectPack;
use  VisitablePackage, ObjectPack;
package VisitableBuiltinPackage is

	type VisitableBuiltin is new Visitable and Object with
	record
		builtin : ObjectPtr := null;
	end record;
	
	
	function  toString(vb: VisitableBuiltin) return String;
	
	procedure setChildren(v: in out VisitableBuiltin ; children : ObjectPtrArrayPtr);
	function  getChildren(v: VisitableBuiltin) return ObjectPtrArrayPtr;
	
	function  getChildAt(v: VisitableBuiltin; i : Integer) return VisitablePtr;
	procedure setChildAt(v: in out VisitableBuiltin; i: in Integer; child: in VisitablePtr);
	
	function  getChildCount(v: VisitableBuiltin) return Integer;
	
	function  makeVisitableBuiltinPackage(o: ObjectPtr) return VisitableBuiltin;
	function  getBuiltin(v: VisitableBuiltin) return ObjectPtr;
    

end VisitableBuiltinPackage;
