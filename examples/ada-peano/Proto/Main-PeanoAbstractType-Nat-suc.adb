with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;


package body Main.PeanoAbstractType.Nat.Suc is

	function make(pred : SharedObjectPtr) return Suc is 
	 tempAdr : SharedObjectPtr ;
	 tempPhy : SharedObject'Class := pred.all ;
	begin

		initHashCode(sucGomProto.all,pred);
		build(main.PeanoAbstractType.factory.all,sucGomProto.all,tempAdr,tempPhy);
		return suc(tempPhy) ;	

	end;

	procedure init(this: in out suc; pred: SharedObjectPtr; hashCode: Integer) is
	begin
		this.pred := pred;
		this.hashCode := hashCode;
	end;

	procedure initHashCode(this: in out suc; pred: SharedObjectPtr) is
	begin
		this.pred := pred;
		this.hashCode := this.hashFunction ;
	end;

	function symbolName(this: suc) return String is
	begin
		return "suc";
	end;

	function toString(this: suc) return String is
	begin

	return this.pred.toString&"+1" ; 

	end;

	function getArity(this: suc) return Integer is
	begin
		return 1;
	end;

	function duplicate(this: suc) return SharedObject'Class is
	clone : access suc := new suc ;	
	begin
        	clone.init(this.pred, this.hashCode);
		return clone.all;
	end;

	function equivalent(this: suc; o: SharedObject'Class) return Boolean is
	
	peer : suc ;
	
	begin
		if o in suc then
			peer := suc(o) ;
			return this.pred = peer.pred ;
		end if;
		return false;
	end;

	function issuc(this: suc) return Boolean is
	begin
		return true;
	end;

	function hashFunction(this: suc) return Integer is
	begin

	return this.pred.hashCode + 1 ;
	
	end;

end Main.PeanoAbstractType.Nat.Suc ; 

