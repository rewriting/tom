with VisitablePackage; use VisitablePackage;

package Main.PeanoAbstractType is

	type PeanoAbstractType is abstract new Visitable with

		record
			factory : shared.SharedObjectFactory := shared.SharedObjectFactory.getInstance; 
		end record;

	function symbolName return String is abstract;

--	 function toString

	-- SharedObjectWithID

end Main.PeanoAbstractType;
	
