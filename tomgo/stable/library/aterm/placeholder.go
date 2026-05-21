package aterm

import (
	"tom/tomgo/stable/library/sharedobjects"
)

// ATermPlaceholder is the `<type>` form used in patterns. After a
// pattern is parsed into a real term tree, every `<...>` becomes
// a placeholder with the type sub-term recorded inside.
type ATermPlaceholder struct {
	placeholderType ATerm
	hash            uint32
	factory         *Factory
}

func (p *ATermPlaceholder) Type() int                { return PLACEHOLDER }
func (p *ATermPlaceholder) Hash() uint32             { return p.hash }
func (p *ATermPlaceholder) Factory() *Factory        { return p.factory }
func (p *ATermPlaceholder) GetPlaceholder() ATerm    { return p.placeholderType }
func (p *ATermPlaceholder) String() string           { return "<" + p.placeholderType.String() + ">" }
func (p *ATermPlaceholder) IsEqual(o ATerm) bool      { return ATerm(p) == o }
func (p *ATermPlaceholder) GetAnnotations() *ATermList { return p.factory.emptyList }
func (p *ATermPlaceholder) SetAnnotations(_ *ATermList) ATerm { return p }
func (p *ATermPlaceholder) HasAnnotations() bool             { return false }
func (p *ATermPlaceholder) Match(pattern string) []any       { return matchPattern(p, pattern) }
func (p *ATermPlaceholder)MatchTerm(pat ATerm) []any        { return matchPatternTerm(p, pat) }
func (p *ATermPlaceholder) Make(args []any) ATerm            { return makePattern(p, args) }

func (p *ATermPlaceholder) Equivalent(other sharedobjects.Term) bool {
	o, ok := other.(*ATermPlaceholder)
	return ok && p.placeholderType == o.placeholderType
}

func (p *ATermPlaceholder) Duplicate() sharedobjects.Term {
	clone := *p
	return &clone
}
