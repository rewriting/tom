with peano.types.nat; use peano.types.nat;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;


package body peano.types.nat.suc is

	function make is 
	begin

		this.gomProto.initHashCode(_pred);
		return (suc) factory.build(gomProto);
	end;

	procedure init is
	begin
		this._pred = _pred;
		this.hashCode = hashCode;
	end;

	procedure initHashCode is
	begin
		this._pred = _pred;
		this.hashCode = this.hashFunction() ;
	end;

	function symbolName is
	begin
		return "suc";
	end;

	function getArity is
	begin
		return 1;
	end;

	function duplicate is
	begin
		clone := new suc
        	clone.init(this._pred, this.hashCode);
		return clone;
	end;

	function compareToLPO is
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

	function compareTo is
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

	function hashCode is 
	begin
		return this.hashCode;
	end;

	function equivalent is
	begin
		if o in suc then
			peer: suc := (suc) o;
			return this._pred = peer._pred ;
		end if;
		return false;
	end;

	function issuc is
	begin
		return true;
	end;

	function getpred is
	begin
		return this._pred;
	end;

	function setpred is
	begin
		return make(set_arg);
	end;

	function getChildCount is
	begin
		return 1;
	end;

	function getChildAt is
	begin
		case index is
			when 0 => return this.all._pred'Access;
			when others => raise Outofbound;
		end case;
	end;

	function setChildren is
	begin
		if childs.all(0).all in Nat then return make( childs.all(0).all)'Access ;
		else raise Wrongtype;
		end if;

	end;

	function getChildren is
	begin
		res : array Natural range <> of tom.library.sl.Visitable ;

		res(0) := this._pred;

		return(res)'Access;


	function hashFunction is
	begin 
		-- ?? TODO
	end;

