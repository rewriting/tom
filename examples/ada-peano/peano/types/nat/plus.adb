with peano.types.nat; use peano.types.nat;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;


package body peano.types.nat.plus is

	function make is 
	begin

		this.gomProto.initHashCode(_x1,_x2);
		return (plus) factory.build(gomProto);
	end;

	procedure init is
	begin
		this._x1 = _x1;
		this._x2 = _x2;
		this.hashCode = hashCode;
	end;

	procedure initHashCode is
	begin
		this._x1 = _x1;
		this._x2 = _x2;

		this.hashCode = this.hashFunction() ;
	end;

	function symbolName is
	begin
		return "plus";
	end;

	function getArity is
	begin
		return 2;
	end;

	function duplicate is
	begin
		clone := new plus
        	clone.init(this._x1, this._x2, this.hashCode);
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
		tco : plus := (plus) ao;

		_x1Cmp : Integer := (this._x1).compareToLPO(tco._x1)
		if _x1Cmp != 0 then return _x1Cmp ; end if;

		_x2Cmp : Integer := (this._x2).compareToLPO(tco._x2)
		if _x2Cmp != 0 then return _x2Cmp ; end if;


		Ada.Text_IO.Put("Unable to compare");

	end;

	function compareTo is
	begin

		-- Compare directly
		ao : peano.PeanoAbstractType := (peano.PeanoAbstractType) o ;
		if ao = this then return 0; end if; 

		-- Compare by hashCode
		-- TODO
		if this.hashCode != ao.hashCode then return (this.hashCode < ao.hashCode())?-1:1; end if;


		-- Compare by name
		symbCmp : Integer := this.symbolName().compareTo(ao.symbolName) ;
		if symbCmp!=0 then return symbCmp; end if;
		-- Compare childs
	tco : plus := (plus) ao;

		_x1Cmp : Integer := (this._x1).compareToLPO(tco._x1)
		if _x1Cmp != 0 then return _x1Cmp ; end if;

		_x2Cmp : Integer := (this._x2).compareToLPO(tco._x2)
		if _x2Cmp != 0 then return _x2Cmp ; end if;

		

		Ada.Text_IO.Put("Unable to compare");
	end;

	function hashCode is 
	begin
		return this.hashCode;
	end;

	function equivalent is
	begin
		if o in plus then
			peer: plus := (plus) o;
			return this._x1 = peer._pred ;
		end if;
		return false;
	end;

	function isplus is
	begin
		return true;
	end;

	function getx1 is
	begin
		return this._x1;
	end;

	function setx1 is
	begin
		return make(set_arg,this._x2);
	end;

	function getx2 is
	begin
		return this._x2;
	end;

	function setx2 is
	begin
		return make(this._x1,set_arg);
	end;

	function getChildcount is
	begin
		return 2;
	end;

	function getChildAt is
	begin
		case index is
			when 0 => return make(v.all, this._x2)'Access;
			when 1 => return make((this.all._x1, v.all)'Access;
			when others => raise Outofbound;
		end case;
	end;

	function setChildren is
	begin
		if childs.all(0).all in Nat and child.all(1).all in Nat
			then return make(childs.all(0).all, childs.all(1).all)'Access;
			else raise Incorrecttype;
		end if;
	end;

	function getChildren is
	begin
		res: array Natural range <> of tom.library.sl.Visitable;
		res(0) := this.all._x1 ;
		res(1) := this.all._x2 ;
		return res'Access;
	end;


	function hashFunction is
	begin 
		-- ?? TODO
	end;

