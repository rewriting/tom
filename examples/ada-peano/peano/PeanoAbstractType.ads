package peano.PeanoAbstractType is interface 

	type PeanoAbstractType is abstract tagged with Object and Visitable 
		record
			factory : shared.SharedObjectFactory = shared.SharedObjectFactory.getInstance(); 
		end record;

	function symbolName return String is abstract;

	-- function toString

	-- SharedObjectWithID

end peano.PeanoAbstractType;
	
