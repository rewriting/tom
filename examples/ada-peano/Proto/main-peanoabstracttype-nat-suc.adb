with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;
with Ada.Text_IO; use Ada.Text_IO ;

package body Main.PeanoAbstractType.Nat.Suc is

	function make(pred : NatPtr) return SucPtr is 
	
	this: SucPtr ;

	begin
		this := new Suc; 
		sucGomProto.all.initHashCode(pred);
		this.all := suc(build(main.PeanoAbstractType.factory.all,sucGomProto.all,SharedObjectPtr(this)));
		return this; 	

	end;

	procedure init(this: in out suc; pred: NatPtr; hashCode: Integer) is
	begin
		this.pred := pred;
		this.hashCode := hashCode;
	end;

	procedure initHashCode(this: in out suc; pred: NatPtr) is
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
			return equivalent(this.pred.all,peer.pred.all) ;
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

