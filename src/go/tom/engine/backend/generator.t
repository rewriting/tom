package backend

// The generator of the Go backend: AbstractGenerator → GenericGenerator →
// GoGenerator (src/tom/engine/backend/{Abstract,Generic,Go}Generator.t)
// flattened into one type (spec J4 §4.4, rules R1-R10 of plan J4.1b):
// this file holds the type, its constructor, the accessors of
// AbstractGenerator and the Go type helpers of GoGenerator.

import (
	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

%include { adt.tom }

// receiver is GoGenerator.RECEIVER (GoGenerator.t:65): the receiver of the
// methods generated for a %strategy.
const receiver = "tom__strat"

// goGenerator is the flattened generator: the fields of the three Java
// classes, a Java null as R7 says.
type goGenerator struct {
	// AbstractGenerator.t:54-57
	output      *tools.OutputCode
	opts        *platform.Options // Java: optionManager
	symbolTable *tools.SymbolTable
	prettyMode  bool
	// GenericGenerator.t:57-61
	isFsymMap  map[string]string
	nodeclMode bool
	inline     bool
	inlineplus bool
	modifier   string
	// GoGenerator.t:68-89
	currentClass    string          // Java: null is ""
	classFields     map[string]bool // Java: HashSet (membership only)
	conditionalType *string         // Java: null is nil (R7)
	declPrefix      string
}

// newGoGenerator is new GoGenerator(output, optionManager, symbolTable)
// (GoGenerator.t:91), with the constructors of GenericGenerator
// (GenericGenerator.t:63) and AbstractGenerator (AbstractGenerator.t:59).
func newGoGenerator(output *tools.OutputCode, opts *platform.Options, st *tools.SymbolTable) *goGenerator {
	g := &goGenerator{
		output:      output,
		opts:        opts,
		symbolTable: st,
		prettyMode:  opts.Bool("pretty"),
		isFsymMap:   map[string]string{},
		classFields: map[string]bool{},
		declPrefix:  "tom_",
	}
	g.nodeclMode = opts.Bool("noDeclaration")
	cCode := opts.Bool("cCode")
	jCode := opts.Bool("jCode")
	g.inline = opts.Bool("inline")
	g.inlineplus = opts.Bool("inlineplus")
	g.inline = g.inline || g.inlineplus
	g.inline = g.inline && (cCode || jCode) // always false with -go (R10)
	g.inlineplus = false // GoGenerator's constructor: the Go backend never inlines the hooks
	return g
}

// getSymbolTable is AbstractGenerator.getSymbolTable (AbstractGenerator.t:67):
// a single table for every module, the argument is ignored (R5).
func (g *goGenerator) getSymbolTable(moduleName string) *tools.SymbolTable {
	return g.symbolTable
}

// getSymbolFromName is AbstractGenerator.getSymbolFromName
// (AbstractGenerator.t:74): nil when unknown.
func (g *goGenerator) getSymbolFromName(tomName string) TomSymbol {
	return tools.GetSymbolFromName(tomName, g.symbolTable)
}

// getTermType is AbstractGenerator.getTermType(TomTerm) (AbstractGenerator.t:78).
func (g *goGenerator) getTermType(t TomTerm) TomType {
	return tools.GetTermType(t, g.symbolTable)
}

// getTermTypeBQ is AbstractGenerator.getTermType(BQTerm) (AbstractGenerator.t:82).
func (g *goGenerator) getTermTypeBQ(t BQTerm) TomType {
	return tools.GetTermTypeBQ(t, g.symbolTable)
}

// getUniversalType is AbstractGenerator.getUniversalType (AbstractGenerator.t:86).
func (g *goGenerator) getUniversalType() TomType {
	return g.symbolTable.GetUniversalType()
}

// getCodomain is AbstractGenerator.getCodomain (AbstractGenerator.t:90).
func (g *goGenerator) getCodomain(tSymbol TomSymbol) TomType {
	return tools.GetSymbolCodomain(tSymbol)
}

// setDeclPrefix is GoGenerator.setDeclPrefix (GoGenerator.t:96).
func (g *goGenerator) setDeclPrefix(prefix string) {
	g.declPrefix = prefix
}

// declName is GoGenerator.declName (GoGenerator.t:101), which masks
// AbstractGenerator.declName (AbstractGenerator.t:966): tom_<hook>_…
// becomes tom_<F>_<hook>_… (declaration and uses).
func (g *goGenerator) declName(name string) string {
	if len(name) >= 4 && name[:4] == "tom_" {
		return g.declPrefix + name[4:]
	}
	return g.declPrefix + name
}

// goType is GoGenerator.goType (GoGenerator.t:106): the Go spelling of a
// TomType; "any" when no target type is known.
func (g *goGenerator) goType(ty TomType) string {
	%match(ty) {
		Type[TlType=TLType(str)] -> { return `str }
	}
	return "any"
}

// goTLCode is GoGenerator.goTLCode (GoGenerator.t:113).
func (g *goGenerator) goTLCode(tlType TargetLanguageType) string {
	%match(tlType) {
		TLType(str) -> { return `str }
	}
	return "any"
}

// sortGoType is GoGenerator.sortGoType (GoGenerator.t:121): the Go type of
// a sort name: builtin, declared %typeterm, or any.
func (g *goGenerator) sortGoType(sort string, moduleName string) string {
	st := g.getSymbolTable(moduleName)
	if st.IsBuiltinType(sort) {
		return g.goType(st.GetBuiltinType(sort))
	}
	t := st.GetType(sort)
	if t == nil {
		return "any"
	}
	return g.goType(t)
}

// knownGoType is GoGenerator.knownGoType (GoGenerator.t:131): the Go type
// of a TomType, its target type, else the one of its sort; ok is false
// where Java returns null.
func (g *goGenerator) knownGoType(ty TomType, moduleName string) (string, bool) {
	%match(ty) {
		Type[TlType=TLType(str)] -> { return `str, true }
	}
	sort, ok := tools.GetTomType(ty)
	if !ok {
		return "", false
	}
	st := g.getSymbolTable(moduleName)
	if st.IsBuiltinType(sort) {
		return g.goType(st.GetBuiltinType(sort)), true
	}
	if t := st.GetType(sort); t != nil {
		%match(t) {
			Type[TlType=TLType(str)] -> { return `str, true }
		}
	}
	return "", false
}

// codomainGoType is GoGenerator.codomainGoType (GoGenerator.t:153): the Go
// type of the codomain of an operator; ok is false where Java returns null.
func (g *goGenerator) codomainGoType(opName TomName, moduleName string) (string, bool) {
	%match(opName) {
		Name(name) -> {
			symbol := g.getSymbolTable(moduleName).GetSymbolFromName(`name)
			if symbol != nil {
				return g.knownGoType(tools.GetSymbolCodomain(symbol), moduleName)
			}
		}
	}
	return "", false
}

// expressionGoType is GoGenerator.expressionGoType (GoGenerator.t:170): the
// Go type of an expression, when it can be read from the expression itself
// (the shapes built for Conditional by the variadic and array generators);
// ok is false where Java returns null.
func (g *goGenerator) expressionGoType(exp Expression, moduleName string) (string, bool) {
	%match(exp) {
		BQTermToExpression(BQVariable[AstType=ty]) -> { return g.knownGoType(`ty, moduleName) }
		BQTermToExpression(BuildEmptyList(opName)) -> { return g.codomainGoType(`opName, moduleName) }
		BQTermToExpression(BuildEmptyArray(opName, _)) -> { return g.codomainGoType(`opName, moduleName) }
		GetHead[Codomain=ty] -> { return g.knownGoType(`ty, moduleName) }
		GetTail[Opname=opName] -> { return g.codomainGoType(`opName, moduleName) }
		GetElement[Codomain=ty] -> { return g.knownGoType(`ty, moduleName) }
	}
	return "", false
}

// generateTypedExpression is GoGenerator.generateTypedExpression
// (GoGenerator.t:183): exp generated with conditionalType set to goType,
// restored afterwards. goType is nil for Java's null: a caller holding a
// (string, bool) passes &s when ok, nil otherwise.
func (g *goGenerator) generateTypedExpression(deep int, goType *string, exp Expression, moduleName string) {
	saved := g.conditionalType
	g.conditionalType = goType
	defer func() { g.conditionalType = saved }()
	g.generateExpression(deep, exp, moduleName)
}

// optString is the *string of a Java String that can be null: &s when ok,
// nil otherwise (R7), for generateTypedExpression.
func optString(s string, ok bool) *string {
	if ok {
		return &s
	}
	return nil
}

// The generated mapping code (adt.tom) names aterm types; this keeps the import used.
var _ = aterm.GetInstance
