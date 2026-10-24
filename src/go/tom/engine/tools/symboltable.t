package tools

// The subset of tom.engine.tools.SymbolTable (src/tom/engine/tools/SymbolTable.t)
// the backend uses (plan J4.1a, « Sous-ensemble porté »), for the Go
// target only (D5): the types of the other targets, builtinToWrapper and
// what the phases before the backend use (getSymbolFromType,
// updateConstrainedSymbolCodomain, isResolveSymbol, isNumericType…) are
// not ported.

import (
	"fmt"
	"sort"

	. "tom/engine/adt"
	"tom/engine/bridge"
	"tom/engine/platform"
	"tom/library/aterm"
)

%include { adt.tom }

// The builtin sorts (SymbolTable.t:51-61).
const (
	typeInt       = "int"
	typeLong      = "long"
	typeFloat     = "float"
	typeChar      = "char"
	typeDouble    = "double"
	typeString    = "String"
	typeBoolean   = "boolean"
	typeUniversal = "universal"
	typeVoid      = "void"
	typeIntArray  = "intarray"
	intArrayOp    = "concInt"
)

// typeUnknown is TYPE_UNKNOWN (SymbolTable.t:63).
func typeUnknown() TomType {
  return `Type(concTypeOption(), "unknown type", EmptyTargetLanguageType())
}

// SymbolTable is tom.engine.tools.SymbolTable: a single table for every
// module (getSymbolTable(moduleName) ignores the module, spec J4 §3.1).
type SymbolTable struct {
	mapSymbolName map[string]TomSymbol // Java: TreeMap (KeySymbols sorts the names)
	mapTypeName   map[string]TomType   // Java: TreeMap (GetTypeEntries sorts the names)
	usedKeyEntry  map[KeyEntry]bool    // Java: HashSet (membership; the terms are maximally shared)
	mapInliner    map[string]string    // Java: HashMap (SymbolTable.t:559)
}

// NewSymbolTable is new SymbolTable() then init(optionManager) with -go
// (SymbolTable.t:79): the builtin types are those of Go.
func NewSymbolTable() *SymbolTable {
	return &SymbolTable{
		mapSymbolName: map[string]TomSymbol{},
		mapTypeName:   map[string]TomType{},
		usedKeyEntry:  map[KeyEntry]bool{},
		mapInliner:    map[string]string{},
	}
}

// FromBundle is the symbol table the bridge hands over (spec J4 §4.2): the
// symbols of its table line (fromTerm), its declared types (putType, keyed
// by their name, as AstBuilder.t:175 does) and the used marks of the
// earlier phases (the Collector of the backend adds its own).
func FromBundle(b *bridge.Bundle) *SymbolTable {
	st := NewSymbolTable()
	st.FromTerm(b.Table)
	for _, ty := range b.Types {
		%match(ty) {
			Type[TomType=name] -> { st.PutType(`name, ty) }
		}
	}
	for _, ke := range b.Used {
		st.usedKeyEntry[ke] = true
	}
	return st
}

// PutSymbol is putSymbol (SymbolTable.t:113).
func (st *SymbolTable) PutSymbol(name string, astSymbol TomSymbol) {
	st.mapSymbolName[name] = astSymbol
}

// GetSymbolFromName is getSymbolFromName (SymbolTable.t:117): nil when unknown.
func (st *SymbolTable) GetSymbolFromName(name string) TomSymbol {
	return st.mapSymbolName[name]
}

// PutType is putType (SymbolTable.t:131).
func (st *SymbolTable) PutType(name string, astType TomType) {
	st.mapTypeName[name] = astType
}

// GetUsedKeyEntries is getUsedKeyEntries (SymbolTable.t:149): the used
// marks sorted by their text. port: Go compares bytes, Java UTF-16 chars
// (the same order on BMP text).
func (st *SymbolTable) GetUsedKeyEntries() []KeyEntry {
	res := make([]KeyEntry, 0, len(st.usedKeyEntry))
	for ke := range st.usedKeyEntry {
		res = append(res, ke)
	}
	sort.Slice(res, func(i, j int) bool { return fmt.Sprint(res[i]) < fmt.Sprint(res[j]) })
	return res
}

// GetTypeEntries is getTypeEntries (SymbolTable.t:160): the declared types
// in the order of their names.
func (st *SymbolTable) GetTypeEntries() []TomType {
	names := make([]string, 0, len(st.mapTypeName))
	for n := range st.mapTypeName {
		names = append(names, n)
	}
	sort.Strings(names)
	res := make([]TomType, len(names))
	for i, n := range names {
		res[i] = st.mapTypeName[n]
	}
	return res
}

// GetType is getType (SymbolTable.t:164): a declared type, else the
// builtin int, else nil.
func (st *SymbolTable) GetType(name string) TomType {
	if ty, ok := st.mapTypeName[name]; ok {
		return ty
	}
	if name == typeInt {
		return st.GetIntType()
	}
	return nil
}

// IsUsedSymbolConstructor is isUsedSymbolConstructor(TomSymbol) (SymbolTable.t:175).
func (st *SymbolTable) IsUsedSymbolConstructor(symbol TomSymbol) bool {
	ke := `UsedSymbolConstructor(symbol)
	return st.usedKeyEntry[ke]
}

// IsUsedSymbolDestructor is isUsedSymbolDestructor(TomSymbol) (SymbolTable.t:179).
func (st *SymbolTable) IsUsedSymbolDestructor(symbol TomSymbol) bool {
	ke := `UsedSymbolDestructor(symbol)
	return st.usedKeyEntry[ke]
}

// IsUsedSymbolAC is isUsedSymbolAC (SymbolTable.t:183).
func (st *SymbolTable) IsUsedSymbolAC(symbol TomSymbol) bool {
	ke := `UsedSymbolAC(symbol)
	return st.usedKeyEntry[ke]
}

// IsUsedType is isUsedType(TomType) (SymbolTable.t:187).
func (st *SymbolTable) IsUsedType(ty TomType) bool {
	ke := `UsedType(ty)
	return st.usedKeyEntry[ke]
}

// SetUsedSymbolConstructor is setUsedSymbolConstructor(TomSymbol) (SymbolTable.t:191).
func (st *SymbolTable) SetUsedSymbolConstructor(symbol TomSymbol) {
	ke := `UsedSymbolConstructor(symbol)
	st.usedKeyEntry[ke] = true
}

// SetUsedSymbolDestructor is setUsedSymbolDestructor(TomSymbol) (SymbolTable.t:195).
func (st *SymbolTable) SetUsedSymbolDestructor(symbol TomSymbol) {
	ke := `UsedSymbolDestructor(symbol)
	st.usedKeyEntry[ke] = true
}

// SetUsedType is setUsedType(TomType) (SymbolTable.t:203).
func (st *SymbolTable) SetUsedType(ty TomType) {
	ke := `UsedType(ty)
	st.usedKeyEntry[ke] = true
}

// SetUsedSymbolConstructorName is setUsedSymbolConstructor(String) (SymbolTable.t:207).
func (st *SymbolTable) SetUsedSymbolConstructorName(name string) {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		st.SetUsedSymbolConstructor(symbol)
	}
}

// SetUsedSymbolDestructorName is setUsedSymbolDestructor(String) (SymbolTable.t:214).
func (st *SymbolTable) SetUsedSymbolDestructorName(name string) {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		st.SetUsedSymbolDestructor(symbol)
	}
}

// SetUsedTypeName is setUsedType(String) (SymbolTable.t:228).
func (st *SymbolTable) SetUsedTypeName(name string) {
	if ty := st.GetType(name); ty != nil {
		st.SetUsedType(ty)
	}
}

// IsUsedSymbolConstructorName is isUsedSymbolConstructor(String) (SymbolTable.t:235).
func (st *SymbolTable) IsUsedSymbolConstructorName(name string) bool {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		return st.IsUsedSymbolConstructor(symbol)
	}
	return false
}

// IsUsedSymbolDestructorName is isUsedSymbolDestructor(String) (SymbolTable.t:243).
func (st *SymbolTable) IsUsedSymbolDestructorName(name string) bool {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		return st.IsUsedSymbolDestructor(symbol)
	}
	return false
}

// IsUsedTypeName is isUsedType(String) (SymbolTable.t:251).
func (st *SymbolTable) IsUsedTypeName(name string) bool {
	if ty := st.GetType(name); ty != nil {
		return st.IsUsedType(ty)
	}
	return false
}

// GetIntType is getIntType (SymbolTable.t:259); Java: the other targets (D5).
func (st *SymbolTable) GetIntType() TomType { return NewType(`concTypeOption(), typeInt, "int64") }

// GetIntArrayType is getIntArrayType (SymbolTable.t:268).
func (st *SymbolTable) GetIntArrayType() TomType { return NewType(`concTypeOption(), typeIntArray, "[]int64") }

// GetLongType is getLongType (SymbolTable.t:277).
func (st *SymbolTable) GetLongType() TomType { return NewType(`concTypeOption(), typeLong, "int64") }

// GetFloatType is getFloatType (SymbolTable.t:286).
func (st *SymbolTable) GetFloatType() TomType { return NewType(`concTypeOption(), typeFloat, "float32") }

// GetCharType is getCharType (SymbolTable.t:295).
func (st *SymbolTable) GetCharType() TomType { return NewType(`concTypeOption(), typeChar, "rune") }

// GetDoubleType is getDoubleType (SymbolTable.t:306).
func (st *SymbolTable) GetDoubleType() TomType { return NewType(`concTypeOption(), typeDouble, "float64") }

// GetBooleanType is getBooleanType (SymbolTable.t:317).
func (st *SymbolTable) GetBooleanType() TomType { return NewType(`concTypeOption(), typeBoolean, "bool") }

// GetStringType is getStringType (SymbolTable.t:332).
func (st *SymbolTable) GetStringType() TomType { return NewType(`concTypeOption(), typeString, "string") }

// GetUniversalType is getUniversalType (SymbolTable.t:343).
func (st *SymbolTable) GetUniversalType() TomType { return NewType(`concTypeOption(), typeUniversal, "any") }

// GetVoidType is getVoidType (SymbolTable.t:358).
func (st *SymbolTable) GetVoidType() TomType { return NewType(`concTypeOption(), typeVoid, "") }

// IsIntType is isIntType (SymbolTable.t:371).
func (st *SymbolTable) IsIntType(ty string) bool { return ty == typeInt }

// IsIntArrayType is isIntArrayType (SymbolTable.t:375).
func (st *SymbolTable) IsIntArrayType(ty string) bool { return ty == typeIntArray }

// IsLongType is isLongType (SymbolTable.t:379).
func (st *SymbolTable) IsLongType(ty string) bool { return ty == typeLong }

// IsFloatType is isFloatType (SymbolTable.t:383).
func (st *SymbolTable) IsFloatType(ty string) bool { return ty == typeFloat }

// IsCharType is isCharType (SymbolTable.t:387).
func (st *SymbolTable) IsCharType(ty string) bool { return ty == typeChar }

// IsStringType is isStringType (SymbolTable.t:391).
func (st *SymbolTable) IsStringType(ty string) bool { return ty == typeString }

// IsBooleanType is isBooleanType (SymbolTable.t:395).
func (st *SymbolTable) IsBooleanType(ty string) bool { return ty == typeBoolean }

// IsDoubleType is isDoubleType (SymbolTable.t:399).
func (st *SymbolTable) IsDoubleType(ty string) bool { return ty == typeDouble }

// IsVoidType is isVoidType (SymbolTable.t:403).
func (st *SymbolTable) IsVoidType(ty string) bool { return ty == typeVoid }

// IsUnknownType is isUnknownType (SymbolTable.t:407).
func (st *SymbolTable) IsUnknownType(ty string) bool {
	t := `Type(concTypeOption(), ty, EmptyTargetLanguageType())
	return t == typeUnknown()
}

// IsBuiltinType is isBuiltinType (SymbolTable.t:425); Java: builtinToWrapper
// (SymbolTable.t:411) is Java only (D5).
func (st *SymbolTable) IsBuiltinType(ty string) bool {
	return st.IsIntType(ty) || st.IsLongType(ty) || st.IsCharType(ty) ||
		st.IsStringType(ty) || st.IsBooleanType(ty) || st.IsDoubleType(ty) ||
		st.IsFloatType(ty)
}

// GetBuiltinType is getBuiltinType (SymbolTable.t:460). port: Java also
// prints "Not a builtin type" on stdout, which carries the generated code
// here.
func (st *SymbolTable) GetBuiltinType(ty string) TomType {
	switch {
	case st.IsIntType(ty):
		return st.GetIntType()
	case st.IsLongType(ty):
		return st.GetLongType()
	case st.IsCharType(ty):
		return st.GetCharType()
	case st.IsStringType(ty):
		return st.GetStringType()
	case st.IsBooleanType(ty):
		return st.GetBooleanType()
	case st.IsDoubleType(ty):
		return st.GetDoubleType()
	case st.IsFloatType(ty):
		return st.GetFloatType()
	}
	panic(&platform.RuntimeError{Msg: "getBuiltinType error on term: " + ty})
}

// KeySymbols is keySymbolIterable (SymbolTable.t:484): the symbol names, sorted.
// port: Go compares bytes, Java's TreeMap UTF-16 chars (the same order on BMP text).
func (st *SymbolTable) KeySymbols() []string {
	names := make([]string, 0, len(st.mapSymbolName))
	for n := range st.mapSymbolName {
		names = append(names, n)
	}
	sort.Strings(names)
	return names
}

// FromTerm is fromTerm (SymbolTable.t:493).
func (st *SymbolTable) FromTerm(table TomSymbolTable) {
	list := table.GetEntryList()
	for !list.IsEmptyconcTomEntry() {
		entry := list.GetHeadconcTomEntry()
		st.PutSymbol(entry.GetStrName(), entry.GetAstSymbol())
		list = list.GetTailconcTomEntry()
	}
}

// ToTerm is toTerm (SymbolTable.t:502): each entry is put in front of the
// list, so the names come in the reverse order.
func (st *SymbolTable) ToTerm() TomSymbolTable {
	list := `concTomEntry()
	for _, name := range st.KeySymbols() {
		symbol := st.GetSymbolFromName(name)
		entry := `Entry(name, symbol)
		list = `concTomEntry(entry, list*)
	}
	return `Table(list)
}

// The prefixes of the inliners (SymbolTable.t:561-574): one map, keyed by
// prefix+name, as in Java (make_ + "empty_list_l" is make_empty_list_ + "l").
const (
	prefixIsFsym          = "is_fsym_"
	prefixGetSlot         = "get_slot_"
	prefixGetHead         = "get_head_"
	prefixGetTail         = "get_tail_"
	prefixGetElementArray = "get_element_array_"
	prefixGetSizeArray    = "get_size_array_"
	prefixIsEmptyList     = "is_empty_list_"
	prefixIsSort          = "is_sort_"
	prefixMake            = "make_"
	prefixMakeEmptyArray  = "make_empty_array_"
	prefixMakeEmptyList   = "make_empty_list_"
	prefixMakeAddArray    = "make_append_"
	prefixMakeAddList     = "make_insert_"
	prefixEqualTerm       = "equal_"
)

// putInliner is putInliner (SymbolTable.t:576).
func (st *SymbolTable) putInliner(prefix, opname, code string) {
	st.mapInliner[prefix+opname] = code
}

// getInliner is getInliner (SymbolTable.t:580): ok is false where Java
// returns null.
func (st *SymbolTable) getInliner(prefix, opname string) (code string, ok bool) {
	code, ok = st.mapInliner[prefix+opname]
	return code, ok
}

// PutIsFsym is putIsFsym (SymbolTable.t:584).
func (st *SymbolTable) PutIsFsym(opname, code string) { st.putInliner(prefixIsFsym, opname, code) }

// GetIsFsym is getIsFsym (SymbolTable.t:588).
func (st *SymbolTable) GetIsFsym(opname string) (string, bool) { return st.getInliner(prefixIsFsym, opname) }

// PutIsSort is putIsSort (SymbolTable.t:592).
func (st *SymbolTable) PutIsSort(ty, code string) { st.putInliner(prefixIsSort, ty, code) }

// GetIsSort is getIsSort (SymbolTable.t:596).
func (st *SymbolTable) GetIsSort(ty string) (string, bool) { return st.getInliner(prefixIsSort, ty) }

// PutResolveGetSlot is putResolveGetSlot (SymbolTable.t:601): an empty
// code (the backend generates it), which is not « absent ».
func (st *SymbolTable) PutResolveGetSlot(opname, slotname string) {
	st.putInliner(prefixGetSlot, opname+slotname, "")
}

// GetResolveGetSlot is getResolveGetSlot (SymbolTable.t:604).
func (st *SymbolTable) GetResolveGetSlot(opname, slotname string) (string, bool) {
	return st.getInliner(prefixGetSlot, opname+slotname)
}

// PutGetSlot is putGetSlot (SymbolTable.t:608).
func (st *SymbolTable) PutGetSlot(opname, slotname, code string) {
	st.putInliner(prefixGetSlot, opname+slotname, code)
}

// GetGetSlot is getGetSlot (SymbolTable.t:611).
func (st *SymbolTable) GetGetSlot(opname, slotname string) (string, bool) {
	return st.getInliner(prefixGetSlot, opname+slotname)
}

// PutGetHead is putGetHead (SymbolTable.t:615).
func (st *SymbolTable) PutGetHead(opname, code string) { st.putInliner(prefixGetHead, opname, code) }

// GetGetHead is getGetHead (SymbolTable.t:618).
func (st *SymbolTable) GetGetHead(opname string) (string, bool) { return st.getInliner(prefixGetHead, opname) }

// PutGetTail is putGetTail (SymbolTable.t:622).
func (st *SymbolTable) PutGetTail(opname, code string) { st.putInliner(prefixGetTail, opname, code) }

// GetGetTail is getGetTail (SymbolTable.t:625).
func (st *SymbolTable) GetGetTail(opname string) (string, bool) { return st.getInliner(prefixGetTail, opname) }

// PutGetElementArray is putGetElementArray (SymbolTable.t:629).
func (st *SymbolTable) PutGetElementArray(opname, code string) {
	st.putInliner(prefixGetElementArray, opname, code)
}

// GetGetElementArray is getGetElementArray (SymbolTable.t:632).
func (st *SymbolTable) GetGetElementArray(opname string) (string, bool) {
	return st.getInliner(prefixGetElementArray, opname)
}

// PutGetSizeArray is putGetSizeArray (SymbolTable.t:636).
func (st *SymbolTable) PutGetSizeArray(opname, code string) {
	st.putInliner(prefixGetSizeArray, opname, code)
}

// GetGetSizeArray is getGetSizeArray (SymbolTable.t:640).
func (st *SymbolTable) GetGetSizeArray(opname string) (string, bool) {
	return st.getInliner(prefixGetSizeArray, opname)
}

// PutIsEmptyList is putIsEmptyList (SymbolTable.t:645).
func (st *SymbolTable) PutIsEmptyList(opname, code string) { st.putInliner(prefixIsEmptyList, opname, code) }

// GetIsEmptyList is getIsEmptyList (SymbolTable.t:648).
func (st *SymbolTable) GetIsEmptyList(opname string) (string, bool) {
	return st.getInliner(prefixIsEmptyList, opname)
}

// PutMake is putMake (SymbolTable.t:652).
func (st *SymbolTable) PutMake(opname, code string) { st.putInliner(prefixMake, opname, code) }

// GetMake is getMake (SymbolTable.t:655).
func (st *SymbolTable) GetMake(opname string) (string, bool) { return st.getInliner(prefixMake, opname) }

// PutMakeEmptyArray is putMakeEmptyArray (SymbolTable.t:659).
func (st *SymbolTable) PutMakeEmptyArray(opname, code string) {
	st.putInliner(prefixMakeEmptyArray, opname, code)
}

// GetMakeEmptyArray is getMakeEmptyArray (SymbolTable.t:662).
func (st *SymbolTable) GetMakeEmptyArray(opname string) (string, bool) {
	return st.getInliner(prefixMakeEmptyArray, opname)
}

// PutMakeEmptyList is putMakeEmptyList (SymbolTable.t:666).
func (st *SymbolTable) PutMakeEmptyList(opname, code string) {
	st.putInliner(prefixMakeEmptyList, opname, code)
}

// GetMakeEmptyList is getMakeEmptyList (SymbolTable.t:669).
func (st *SymbolTable) GetMakeEmptyList(opname string) (string, bool) {
	return st.getInliner(prefixMakeEmptyList, opname)
}

// PutMakeAddArray is putMakeAddArray (SymbolTable.t:673).
func (st *SymbolTable) PutMakeAddArray(opname, code string) {
	st.putInliner(prefixMakeAddArray, opname, code)
}

// GetMakeAddArray is getMakeAddArray (SymbolTable.t:676).
func (st *SymbolTable) GetMakeAddArray(opname string) (string, bool) {
	return st.getInliner(prefixMakeAddArray, opname)
}

// PutMakeAddList is putMakeAddList (SymbolTable.t:680).
func (st *SymbolTable) PutMakeAddList(opname, code string) {
	st.putInliner(prefixMakeAddList, opname, code)
}

// GetMakeAddList is getMakeAddList (SymbolTable.t:683).
func (st *SymbolTable) GetMakeAddList(opname string) (string, bool) {
	return st.getInliner(prefixMakeAddList, opname)
}

// PutEqualTerm is putEqualTerm (SymbolTable.t:687).
func (st *SymbolTable) PutEqualTerm(ty, code string) { st.putInliner(prefixEqualTerm, ty, code) }

// GetEqualTerm is getEqualTerm (SymbolTable.t:690).
func (st *SymbolTable) GetEqualTerm(ty string) (string, bool) { return st.getInliner(prefixEqualTerm, ty) }

// GetIntArrayOp is getIntArrayOp (SymbolTable.t:699).
func (st *SymbolTable) GetIntArrayOp() string { return intArrayOp }

var _ = aterm.GetInstance
