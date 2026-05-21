package tomparser

import "strings"

// This file implements a faithful Go simulation of the way the reference Java
// parser (stable/tom/engine/parser/antlr4/) tokenises and accounts for "water"
// (host source between TOM islands).
//
// Three pieces, mirroring the Java side:
//
//  1. tokenizeWater: splits a water chunk into the ANTLR tokens that
//     TomIslandLexer would have produced —
//       NL      = `[\n]+`        (channel HIDDEN)
//       WS      = `[ \r\t]+`     (channel HIDDEN)
//       visible = anything else
//     Each visible byte becomes a length-1 visible token. ANTLR's ID/INTEGER/…
//     rules would group multiple visible bytes into one token, but for the
//     end-position calculation the consecutive visible-tokens of one ID merge
//     identically anyway — splitting per byte yields the same final
//     hostblock after the merge step.
//
//  2. buildHostblocks: replays
//     stable/tom/engine/parser/antlr4/CstBuilder.java::buildHostblock for
//     each visible token. Hidden tokens are claimed left-to-right by the
//     first visible they're attributable to (mimics ANTLR's `usedToken`
//     set). The `lastChar` line/column uses Java's quirky rule:
//
//        if the last hidden right is exactly "\n" (single newline):
//            lastCharLine   = firstCharLine + 1
//            lastCharColumn = 1
//        else if there is at least one hidden right:
//            lastCharLine   = lastHiddenRight.line
//            lastCharColumn = lastHiddenRight.col + len(lastHiddenRight.text)
//        else (no hidden right):
//            lastCharLine   = visibleToken.line
//            lastCharColumn = visibleToken.col + len(visibleToken.text)
//
//  3. mergeHostblocks: replays
//     stable/tom/engine/parser/antlr4/CstConverter.java::simplifyCstBlockList,
//     which itself uses `mergeString` to pad the content between two
//     adjacent hostblocks with synthetic "\n" and " " until the column/line
//     of the first matches the end of the previous. The result is one
//     hostblock whose start = first.start, end = last.end, and content =
//     concatenation-with-padding.

type tokKind int

const (
	tokNL tokKind = iota
	tokWS
	tokVisible
)

type wtoken struct {
	kind tokKind
	text string
	line int // 1-based position of the first byte
	col  int // 1-based
}

type hostblock struct {
	content   string
	startLine int
	startCol  int
	endLine   int
	endCol    int
}

// tokenizeWater scans content from `start` and returns the ANTLR-equivalent
// token stream. The caller is expected to have already advanced p.cur past
// the same content; this function does not mutate parser state.
func tokenizeWater(content string, start position) []wtoken {
	cur := start
	var out []wtoken
	i := 0
	for i < len(content) {
		c := content[i]
		ts := cur
		switch {
		case c == '\n':
			j := i
			for j < len(content) && content[j] == '\n' {
				j++
				cur.line++
				cur.col = 1
			}
			out = append(out, wtoken{kind: tokNL, text: content[i:j], line: ts.line, col: ts.col})
			i = j
		case c == ' ' || c == '\t' || c == '\r':
			j := i
			for j < len(content) {
				cc := content[j]
				if cc != ' ' && cc != '\t' && cc != '\r' {
					break
				}
				j++
				cur.col++
			}
			out = append(out, wtoken{kind: tokWS, text: content[i:j], line: ts.line, col: ts.col})
			i = j
		default:
			// `//` line comment: skip to end-of-line (newline is left
			// for the next iteration to consume as tokNL). Java lexer
			// rule: SLCOMMENT : '//' ~[\r\n]* -> skip.
			if c == '/' && i+1 < len(content) && content[i+1] == '/' {
				j := i
				for j < len(content) && content[j] != '\n' {
					j++
					cur.col++
				}
				i = j
				continue
			}
			// `/* … */` block comment: skip the whole span, advancing
			// line/col through every byte. Java: MLCOMMENT : '/*' .*?
			// '*/' -> skip.
			if c == '/' && i+1 < len(content) && content[i+1] == '*' {
				j := i + 2
				cur.col += 2
				for j+1 < len(content) && !(content[j] == '*' && content[j+1] == '/') {
					if content[j] == '\n' {
						cur.line++
						cur.col = 1
					} else {
						cur.col++
					}
					j++
				}
				if j+1 < len(content) {
					j += 2 // consume `*/`
					cur.col += 2
				}
				i = j
				continue
			}
			out = append(out, wtoken{kind: tokVisible, text: string(c), line: ts.line, col: ts.col})
			i++
			cur.col++
		}
	}
	return out
}

// buildHostblocks turns a token stream into the list of HOSTBLOCK records
// that the Java parser would produce, one per visible token, with hidden
// tokens claimed left-to-right by the first visible they belong to.
func buildHostblocks(tokens []wtoken) []hostblock {
	// Collect visible-token indices.
	var visibles []int
	for i, t := range tokens {
		if t.kind == tokVisible {
			visibles = append(visibles, i)
		}
	}
	if len(visibles) == 0 {
		return nil
	}

	used := make([]bool, len(tokens))
	var out []hostblock

	for vi, idx := range visibles {
		// Hidden left = tokens between the previous visible (or start) and
		// this visible, not already used.
		leftStart := 0
		if vi > 0 {
			leftStart = visibles[vi-1] + 1
		}
		var leftSb strings.Builder
		firstHiddenIdx := -1
		for k := leftStart; k < idx; k++ {
			if used[k] {
				continue
			}
			if firstHiddenIdx < 0 {
				firstHiddenIdx = k
			}
			leftSb.WriteString(tokens[k].text)
			used[k] = true
		}

		// Hidden right = tokens after this visible up to the next visible
		// (or end of stream).
		rightEnd := len(tokens)
		if vi+1 < len(visibles) {
			rightEnd = visibles[vi+1]
		}
		var rightSb strings.Builder
		var lastHidden *wtoken
		for k := idx + 1; k < rightEnd; k++ {
			if used[k] {
				continue
			}
			rightSb.WriteString(tokens[k].text)
			used[k] = true
			lastHidden = &tokens[k]
		}

		// firstCharLine/Column.
		var firstLine, firstCol int
		if firstHiddenIdx >= 0 {
			firstLine = tokens[firstHiddenIdx].line
			firstCol = tokens[firstHiddenIdx].col
		} else {
			firstLine = tokens[idx].line
			firstCol = tokens[idx].col
		}

		// lastCharLine/Column — Java's three-case rule.
		var lastLine, lastCol int
		switch {
		case lastHidden == nil:
			lastLine = tokens[idx].line
			lastCol = tokens[idx].col + len(tokens[idx].text)
		case lastHidden.text == "\n":
			lastLine = firstLine + 1
			lastCol = 1
		default:
			lastLine = lastHidden.line
			lastCol = lastHidden.col + len(lastHidden.text)
		}

		out = append(out, hostblock{
			content:   leftSb.String() + tokens[idx].text + rightSb.String(),
			startLine: firstLine, startCol: firstCol,
			endLine: lastLine, endCol: lastCol,
		})
	}
	return out
}

// mergeHostblocks folds a sequence of hostblocks into one, padding the
// content between adjacent records with synthetic "\n" and " " (the
// `mergeString` algorithm from CstConverter.java).
func mergeHostblocks(blocks []hostblock) hostblock {
	cur := blocks[0]
	for _, next := range blocks[1:] {
		var sb strings.Builder
		sb.WriteString(cur.content)
		line, col := cur.endLine, cur.endCol
		for line < next.startLine {
			sb.WriteByte('\n')
			line++
			col = 1
		}
		for col < next.startCol {
			sb.WriteByte(' ')
			col++
		}
		sb.WriteString(next.content)
		cur = hostblock{
			content:   sb.String(),
			startLine: cur.startLine, startCol: cur.startCol,
			endLine: next.endLine, endCol: next.endCol,
		}
	}
	return cur
}
