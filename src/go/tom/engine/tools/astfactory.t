package tools

// The subset of tom.engine.tools.ASTFactory (src/tom/engine/tools/ASTFactory.t)
// that the symbol table and the backend use (plan J4.1a).

import (
	"strings"

	. "tom/engine/adt"
	"tom/library/aterm"
)

%include { adt.tom }

// NewType is ASTFactory.makeType (ASTFactory.t:307): the type tomName
// implemented by the target-language type tlName. (adt.MakeType is the
// constructor of the operator Type.)
func NewType(typeOptions TypeOptionList, tomName string, tlName string) TomType {
	return `Type(typeOptions, tomName, TLType(tlName))
}

// MakeSingleLineCode is ASTFactory.makeSingleLineCode (ASTFactory.t:387).
func MakeSingleLineCode(code string, pretty bool) string {
	if !pretty {
		code = strings.NewReplacer("\n", " ", "\t", " ", "\r", " ").Replace(code)
	}
	return code
}

// The generated mapping code (adt.tom) names aterm types; this keeps the import used.
var _ = aterm.GetInstance
