package tools

// The subset of tom.engine.TomBase (src/tom/engine/TomBase.t) the backend
// uses (plan J4.1a, « Sous-ensemble porté »). It lives in package tools:
// TomBase and SymbolTable use each other, and Go has no import cycle.

import (
	"path/filepath"
	"strconv"
	"strings"

	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/library/aterm"
)

%include { adt.tom }

// DefaultModuleName is DEFAULT_MODULE_NAME (TomBase.t:73).
const DefaultModuleName = "default"

// GoDeclPrefix is goDeclPrefix (TomBase.t:88): tom_<F>_, F the base name
// of the input file without its extension, each character outside
// [A-Za-z0-9_] replaced by '_'; "stdin" for "" or "-". port: a rune
// outside the BMP gives "__" (two UTF-16 chars in Java).
func GoDeclPrefix(inputFileName string) string {
	f := inputFileName
	if slash := strings.LastIndexAny(f, "/"+string(filepath.Separator)); slash >= 0 {
		f = f[slash+1:]
	}
	if dot := strings.LastIndexByte(f, '.'); dot > 0 {
		f = f[:dot]
	}
	if f == "" || f == "-" {
		f = "stdin"
	}
	var b strings.Builder
	for _, r := range f {
		switch {
		case r >= 'A' && r <= 'Z', r >= 'a' && r <= 'z', r >= '0' && r <= '9', r == '_':
			b.WriteRune(r)
		case r > 0xFFFF:
			b.WriteString("__")
		default:
			b.WriteByte('_')
		}
	}
	return "tom_" + b.String() + "_"
}

// GetTomType is getTomType (TomBase.t:107): ok is false where Java returns
// null (EmptyType, TypeVar).
func GetTomType(ty TomType) (string, bool) {
	%match {
		Type[TomType=s] << ty -> { return `s, true }
		EmptyType() << ty || TypeVar[] << ty -> { return "", false }
	}
	panic(&platform.RuntimeError{Msg: "getTomType error on term: " + platform.JavaString(ty)})
}

// GetTLType is getTLType (TomBase.t:118).
func GetTLType(ty TomType) string {
	%match(ty) {
		Type[TlType=tlType] -> { return GetTLCode(`tlType) }
	}
	panic(&platform.RuntimeError{Msg: "getTLType error on term: " + platform.JavaString(ty)})
}

// GetTLCode is getTLCode (TomBase.t:128).
func GetTLCode(ty TargetLanguageType) string {
	%match(ty) {
		TLType(str) -> { return `str }
	}
	panic(&platform.RuntimeError{Msg: "getTLCode error on term: " + platform.JavaString(ty)})
}

// GetSymbolCodomain is getSymbolCodomain (TomBase.t:138).
func GetSymbolCodomain(symbol TomSymbol) TomType {
	if symbol != nil {
		return symbol.GetTypesToType().GetCodomain()
	}
	return `EmptyType()
}

// GetSymbolDomain is getSymbolDomain (TomBase.t:149).
func GetSymbolDomain(symbol TomSymbol) TomTypeList {
	if symbol != nil {
		return symbol.GetTypesToType().GetDomain()
	}
	return `concTomType()
}

// TomNumberListToString is tomNumberListToString (TomBase.t:159). port:
// without Java's LRU cache (a performance matter only).
func TomNumberListToString(numberList TomNumberList) string {
	var buf strings.Builder
	for !numberList.IsEmptyconcTomNumber() {
		number := numberList.GetHeadconcTomNumber()
		numberList = numberList.GetTailconcTomNumber()
		%match(number) {
			Position(n) -> { buf.WriteString("Position" + strconv.FormatInt(`n, 10)) }
			MatchNumber(n) -> { buf.WriteString("Match" + strconv.FormatInt(`n, 10)) }
			PatternNumber(n) -> { buf.WriteString("Pattern" + strconv.FormatInt(`n, 10)) }
			ListNumber(n) -> { buf.WriteString("List" + strconv.FormatInt(`n, 10)) }
			IndexNumber(n) -> { buf.WriteString("Index" + strconv.FormatInt(`n, 10)) }
			Begin(n) -> { buf.WriteString("Begin" + strconv.FormatInt(`n, 10)) }
			End(n) -> { buf.WriteString("End" + strconv.FormatInt(`n, 10)) }
			Save(n) -> { buf.WriteString("Save" + strconv.FormatInt(`n, 10)) }
			AbsVar(n) -> { buf.WriteString("AbsVar" + strconv.FormatInt(`n, 10)) }
			RenamedVar(tomName) -> { buf.WriteString("RenamedVar" + numberIdentifier(`tomName)) }
			NameNumber(tomName) -> { buf.WriteString(numberIdentifier(`tomName)) }
		}
	}
	return buf.String()
}

// numberIdentifier is the identifier of the name of a RenamedVar or a
// NameNumber in tomNumberListToString (TomBase.t:208-237). port: Java
// duplicates this code in the two cases; it is factored here.
func numberIdentifier(tomName TomName) string {
	identifier := "Empty"
	%match(tomName) {
		Name(name) -> { identifier = `name }
		PositionName(localNumberList) -> { identifier = TomNumberListToString(`localNumberList) }
	}
	return identifier
}

// GetModuleName is getModuleName (TomBase.t:408): ok is false where Java
// returns null.
func GetModuleName(optionList OptionList) (string, bool) {
	%match(optionList) {
		concOption(_*, ModuleName(moduleName), _*) -> { return `moduleName, true }
	}
	return "", false
}

// GetSlotName is getSlotName (TomBase.t:431). port: Java also prints
// "getSlotName: bad index error" on stdout, which carries the generated
// code here.
func GetSlotName(symbol TomSymbol, number int) TomName {
	pairNameDeclList := symbol.GetPairNameDeclList()
	for index := 0; !pairNameDeclList.IsEmptyconcPairNameDecl() && index < number; index++ {
		pairNameDeclList = pairNameDeclList.GetTailconcPairNameDecl()
	}
	if pairNameDeclList.IsEmptyconcPairNameDecl() {
		panic(&platform.RuntimeError{Msg: "getSlotName: bad index error"})
	}
	pairNameDecl := pairNameDeclList.GetHeadconcPairNameDecl()
	decl := pairNameDecl.GetSlotDecl()
	%match(decl) {
		GetSlotDecl[SlotName=name] -> { return `name }
	}
	return pairNameDecl.GetSlotName()
}

// GetSlotIndex is getSlotIndex (TomBase.t:450): -1 when absent.
func GetSlotIndex(tomSymbol TomSymbol, slotName TomName) int {
	index := 0
	pairNameDeclList := tomSymbol.GetPairNameDeclList()
	for !pairNameDeclList.IsEmptyconcPairNameDecl() {
		if pairNameDeclList.GetHeadconcPairNameDecl().GetSlotName() == slotName {
			return index
		}
		pairNameDeclList = pairNameDeclList.GetTailconcPairNameDecl()
		index++
	}
	return -1
}

// ElementAt is elementAt (TomBase.t:466). Java: IllegalArgumentException.
func ElementAt(l TomTypeList, index int) TomType {
	if 0 > index || index > l.Length() {
		panic(&platform.RuntimeError{Msg: "illegal list index: " + strconv.Itoa(index)})
	}
	for i := 0; i < index; i++ {
		l = l.GetTailconcTomType()
	}
	return l.GetHeadconcTomType()
}

// GetSlotType is getSlotType (TomBase.t:476): nil when symbol is not a Symbol.
func GetSlotType(symbol TomSymbol, slotName TomName) TomType {
	%match(symbol) {
		Symbol[TypesToType=TypesToType(typeList, _)] -> { return ElementAt(`typeList, GetSlotIndex(symbol, slotName)) }
	}
	return nil
}

// GetSymbolFromName is getSymbolFromName(String, SymbolTable) (TomBase.t:536).
func GetSymbolFromName(tomName string, symbolTable *SymbolTable) TomSymbol {
	return symbolTable.GetSymbolFromName(tomName)
}

// GetTermType is getTermType(TomTerm, SymbolTable) (TomBase.t:540).
func GetTermType(t TomTerm, symbolTable *SymbolTable) TomType {
	%match(t) {
		(TermAppl|RecordAppl)[NameList=concTomName(headName, _*)] -> {
			head := `headName
			var tomName string
			if head.IsAntiName() {
				tomName = head.GetName().GetString()
			} else {
				tomName = head.GetString()
			}
			if tomSymbol := symbolTable.GetSymbolFromName(tomName); tomSymbol != nil {
				return tomSymbol.GetTypesToType().GetCodomain()
			}
			return `EmptyType()
		}
		(Variable|VariableStar)[AstType=ty] -> { return `ty }
		AntiTerm(term) -> { return GetTermType(`term, symbolTable) }
	}
	panic(&platform.RuntimeError{Msg: "getTermType error on term: " + platform.JavaString(t)})
}

// GetTermTypeBQ is getTermType(BQTerm, SymbolTable) (TomBase.t:569).
func GetTermTypeBQ(t BQTerm, symbolTable *SymbolTable) TomType {
	%match(t) {
		(BQVariable|BQVariableStar)[AstType=ty] -> { return `ty }
		FunctionCall[AstType=ty] -> { return `ty }
		ExpressionToBQTerm(expr) -> { return GetTermTypeExpr(`expr, symbolTable) }
		ListHead[Codomain=ty] -> { return `ty }
		ListTail[Variable=term] -> { return GetTermTypeBQ(`term, symbolTable) }
		Subterm[AstName=Name(name), SlotName=slotName] -> {
			return GetSlotType(symbolTable.GetSymbolFromName(`name), `slotName)
		}
		(BQAppl|BuildConstant|BuildTerm|BuildEmptyList|BuildConsList|BuildAppendList|BuildEmptyArray|BuildConsArray|BuildAppendArray)[AstName=Name(name)] -> {
			if tomSymbol := symbolTable.GetSymbolFromName(`name); tomSymbol != nil {
				return tomSymbol.GetTypesToType().GetCodomain()
			}
			return `EmptyType()
		}
	}
	panic(&platform.RuntimeError{Msg: "getTermType error on term: " + platform.JavaString(t)})
}

// GetTermTypeExpr is getTermType(Expression, SymbolTable) (TomBase.t:603).
// port: Java also prints the error on stdout, which carries the generated
// code here.
func GetTermTypeExpr(t Expression, symbolTable *SymbolTable) TomType {
	%match(t) {
		(GetHead|GetSlot|GetElement)[Codomain=ty] -> { return `ty }
		BQTermToExpression[AstTerm=bqterm] -> { return GetTermTypeBQ(`bqterm, symbolTable) }
		Conditional[Cond=cond] -> { return GetTermTypeExpr(`cond, symbolTable) }
		IsFsym[Variable=bqterm] -> { return GetTermTypeBQ(`bqterm, symbolTable) }
		GetTail[Variable=term] -> { return GetTermTypeBQ(`term, symbolTable) }
		GetSliceList[VariableBeginAST=term] -> { return GetTermTypeBQ(`term, symbolTable) }
		GetSliceArray[SubjectListName=term] -> { return GetTermTypeBQ(`term, symbolTable) }
		Cast[AstType=ty] -> { return `ty }
		AddOne[Variable=bqterm] -> { return GetTermTypeBQ(`bqterm, symbolTable) }
		Integer[] -> { return symbolTable.GetType("int") }
	}
	panic(&platform.RuntimeError{Msg: "getTermType error on term: " + platform.JavaString(t)})
}

// The generated mapping code (adt.tom) names aterm types; this keeps the import used.
var _ = aterm.GetInstance
