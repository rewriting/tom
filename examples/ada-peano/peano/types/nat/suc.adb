with peano.types.nat; use peano.types.nat;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;


package body peano.types.nat.suc is

	function make(_pred : Nat) return suc is 
	begin

		this.gomProto.initHashCode(_pred);
		return (suc) factory.build(gomProto);
	end;

	procedure init(this: suc, _pred: Nat, hashCode: Integer) is
	begin
		this._pred = _pred;
		this.hashCode = hashCode;
	end;

	procedure initHashCode(this: suc) is
	begin
		this._pred = _pred;
		this.hashCode = this.hashFunction() ;
	end;

	function symbolName(this: suc) is
	begin
		return "suc";
	end;

	function getArity(this: suc) is
	begin
		return 1;
	end;

	function duplicate(this: suc) is
	begin
		clone := new suc
        	clone.init(this._pred, this.hashCode);
		return clone;
	end;

	function compareToLPO(this: suc, o: Object'Class) is
	begin
		-- Compare directly
		ao : peano.PeanoAbstractType := (peano.PeanoAbstractType) o ;
		if ao = this then return 0; end if; 
		-- Compare by name
		symbCmp : Integer := this.symbolName().compareTo(ao.symbolName) ;
		if symbCmp!=0 then return symbCmp; end if;
		-- Compare childs
		tco : suc :=(suc) ao;
		int _predCMmp = (this._pred).compareToLPO(tco._pred);
		if _predCmp != 0 then return _predCmp; end if;

		Ada.Text_IO.Put("Unable to compare");

	end;

	function compareTo(this: suc, o: Object'Class) is
	begin

		-- Compare directly
		ao : peano.PeanoAbstractType := (peano.PeanoAbstractType) o ;
		if ao = this then return 0; end if; 

		-- Compare by hashCode
		if this.hashCode != ao.hashCode then return (hashCode < ao.hashCode())?-1:1; end if;


		-- Compare by name
		symbCmp : Integer := this.symbolName().compareTo(ao.symbolName) ;
		if symbCmp!=0 then return symbCmp; end if;
		-- Compare childs
		tco : suc :=(suc) ao;
		int _predCMmp = (this._pred).compareToLPO(tco._pred);
		if _predCmp != 0 then return _predCmp; end if;

		Ada.Text_IO.Put("Unable to compare");
	end;

	function hashCode(this: suc) is 
	begin
		return this.hashCode;
	end;

	function equivalent(this: suc, o: SharedObject'Class) is
	begin
		if o in suc then
			peer: suc := (suc) o;
			return this._pred = peer._pred ;
		end if;
		return false;
	end;

	function issuc(this: suc) is
	begin
		return true;
	end;

	function getpred(this: suc) is
	begin
		return this._pred;
	end;

	function setpred(this: suc, set_arg: Nat'Class) is
	begin
		return make(set_arg);
	end;

	function getChildCount(this: access Visitable'Class) return Integer is
	begin
		return 1;
	end;

	function getChildAt(this: access Visitable'Class, index: Integer) return VisitablePtr is
	begin
		case index is
			when 0 => return this.all._pred'Access;
			when others => raise Outofbound;
		end case;
	end;

	function setChildAt(this: access Visitable'Class, index: Integer, v:access Visitable'Class) return VisitablePtr is 
	begin
	
		case index is
			when 0 => return this.make(v.all)'Access;
			when others => raise OutofBounds;
		end case;
	end;


	function setChildren(this: access Visitable'Class, childs: ObjectPtrArrayPtr ) return ObjectPtrArrayPtr is
	begin
		if childs.all(0).all in Nat'Class then return make( childs.all(0).all)'Access ;
		else raise Wrongtype;
		end if;

	end;

	function getChildren(this: access Visitable'Class) return ObjectPtrArrayPtr is
	begin
		res : array Natural range <> of tom.library.sl.Visitable ;

		res(0) := this._pred;

		return(res)'Access;
	end;

	function hashFunction(this: suc) return Integer is
	begin 
		-- ?? TODO
	end;
