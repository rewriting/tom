package body VisitableIntrospectorPackage is
	procedure setChildren(intro: in out VisitableIntrospector; o: in out Visitable'Class; children: ObjectPtrArrayPtr) is
	begin
		VisitablePackage.setChildren(Visitable'Class(o), children);
	end;
	
	function getChildren(intro: VisitableIntrospector; o: Object'Class) return ObjectPtrArrayPtr is
	begin
		return VisitablePackage.getChildren(Visitable'Class(o));
	end;
	
	procedure setChildAt(intro: in out VisitableIntrospector; o: in out Visitable'Class; i: Integer; child: Object'Class) is
	begin
		VisitablePackage.setChildAt(Visitable'Class(o), i, Visitable'Class(child));
	end;
	
	function getChildAt(intro: VisitableIntrospector; o: Object'Class; i: Integer) return Object'Class is
	begin
		return VisitablePackage.getChildAt(Visitable'Class(o), i);
	end;
	
	function getChildCount(intro: VisitableIntrospector; o: Object'Class) return Integer is
	begin
		return VisitablePackage.getChildCount(Visitable'Class(o));
	end;
	
	function getInstance return IntrospectorPtr is
	begin
		return VisitableIntrospectorPackage.mapping;
	end;
	
	function toString(vi : VisitableIntrospector) return String is
	begin
		return "VisitableIntrospector()";
	end;

end VisitableIntrospectorPackage;
