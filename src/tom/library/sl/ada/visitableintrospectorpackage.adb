package body VisitableIntrospectorPackage is
	procedure setChildren(intro: access VisitableIntrospector; o: ObjectPtr; children: ObjectPtrArrayPtr) is
	begin
		VisitablePackage.setChildren(Visitable'Class(o.all), children);
	end;
	
	function getChildren(intro: access VisitableIntrospector; o: ObjectPtr) return ObjectPtrArrayPtr is
	begin
		return VisitablePackage.getChildren(Visitable'Class(o.all));
	end;
	
	procedure setChildAt(intro: access VisitableIntrospector; o: ObjectPtr; i: Integer; child: ObjectPtr) is
	begin
		VisitablePackage.setChildAt(Visitable'Class(o.all), i, VisitablePtr(child));
	end;
	
	function getChildAt(intro: access VisitableIntrospector; o: ObjectPtr; i: Integer) return ObjectPtr is
	begin
		return ObjectPtr( VisitablePackage.getChildAt(Visitable'Class(o.all), i) );
	end;
	
	function getChildCount(intro: access VisitableIntrospector; o: ObjectPtr) return Integer is
	begin
		return VisitablePackage.getChildCount(Visitable'Class(o.all));
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
