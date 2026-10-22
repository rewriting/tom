package sl

// Type-check helpers used by the Go strategy mappings (the `.tom`
// counterparts of tom.library.sl.*). Each `IsX(t any) bool` answers
// the question "is this subject an instance of strategy X?" — Tom's
// `is_fsym` hooks compile to a single expression, so we need a one-
// shot helper rather than the multi-statement Go type-assertion
// form.

func IsIdentity(t any) bool   { _, ok := t.(*Identity); return ok }
func IsFail(t any) bool       { _, ok := t.(*Fail); return ok }
func IsMu(t any) bool         { _, ok := t.(*Mu); return ok }
func IsMuVar(t any) bool      { _, ok := t.(*MuVar); return ok }
func IsAll(t any) bool        { _, ok := t.(*All); return ok }
func IsOne(t any) bool        { _, ok := t.(*One); return ok }
func IsOneId(t any) bool      { _, ok := t.(*OneId); return ok }
func IsSequence(t any) bool   { _, ok := t.(*Sequence); return ok }
func IsSequenceId(t any) bool { _, ok := t.(*SequenceId); return ok }
func IsChoice(t any) bool     { _, ok := t.(*Choice); return ok }
func IsChoiceId(t any) bool   { _, ok := t.(*ChoiceId); return ok }
func IsOmega(t any) bool      { _, ok := t.(*Omega); return ok }
func IsIfThenElse(t any) bool { _, ok := t.(*IfThenElse); return ok }
func IsNot(t any) bool        { _, ok := t.(*Not); return ok }
func IsStrategy(t any) bool   { _, ok := t.(Strategy); return ok }
