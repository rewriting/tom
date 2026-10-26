package platform

import (
	"fmt"
	"strconv"
	"strings"
)

// Message is a TomMessage (or a PluginPlatformMessage): the name of its
// Java field and its java.text.MessageFormat pattern.
type Message struct {
	Name    string
	Pattern string
}

// The messages of the backend path (J4.1b) and of the options.
var (
	// BackendInactivated is TomMessage.backendInactivated (TomMessage.java:206).
	BackendInactivated = Message{"backendInactivated", "The BackendPlugin is not activated and thus WILL NOT RUN:No output will be generated "}
	// BackendIOException is TomMessage.backendIOException (TomMessage.java:208).
	BackendIOException = Message{"backendIOException", "BackendPlugin : IOException occurs writting to file ''{0}'': {1} "}
	// GoFormatFailed is TomMessage.goFormatFailed (Go only: -p formats the Go).
	GoFormatFailed = Message{"goFormatFailed", "gofmt: {0}: the generated Go is left unformatted"}
	// ExceptionMessage is TomMessage.exceptionMessage (TomMessage.java:72).
	ExceptionMessage = Message{"exceptionMessage", "{0}: Unhandled exception\n"}
	// NotReturnedPluginOption is TomMessage.notReturnedPluginOption (TomMessage.java:101).
	NotReturnedPluginOption = Message{"notReturnedPluginOption", "TomOptionManager: getOptionFromName did not return a PluginOption"}
	// OptionNotFound is TomMessage.optionNotFound (TomMessage.java:80).
	OptionNotFound = Message{"optionNotFound", "TomOptionManager: Option ''{0}'' not found"}
	// TomGenerationPhase is TomMessage.tomGenerationPhase (TomMessage.java:298).
	TomGenerationPhase = Message{"tomGenerationPhase", "TOM generation phase ({0,number,integer} ms)"}
	// PlatformStopped is PluginPlatformMessage.platformStopped (PluginPlatformMessage.java:91).
	PlatformStopped = Message{"platformStopped", "Platform stopped"}
)

// Text formats the message as java.text.MessageFormat.format does for the
// patterns of the compiler: {n} and {n,number,integer} arguments ("{n}"
// stays when the argument is missing), a doubled apostrophe for a quote,
// and single apostrophes for quoted text. port: a number is written with the grouping separator ',' (Java
// uses the default locale's; only INFO messages show numbers).
func (m Message) Text(args ...any) string {
	var b strings.Builder
	p := m.Pattern
	for i := 0; i < len(p); i++ {
		switch c := p[i]; c {
		case '\'':
			if i+1 < len(p) && p[i+1] == '\'' {
				b.WriteByte('\'')
				i++
				continue
			}
			j := strings.IndexByte(p[i+1:], '\'')
			if j < 0 {
				b.WriteString(p[i+1:])
				return b.String()
			}
			b.WriteString(p[i+1 : i+1+j])
			i += j + 1
		case '{':
			j := strings.IndexByte(p[i:], '}')
			if j < 0 {
				panic(&RuntimeError{Msg: "unmatched braces in the pattern of " + m.Name})
			}
			spec := strings.Split(p[i+1:i+j], ",")
			n, err := strconv.Atoi(strings.TrimSpace(spec[0]))
			if err != nil {
				panic(&RuntimeError{Msg: "bad argument number in the pattern of " + m.Name})
			}
			if n < len(args) {
				b.WriteString(formatArg(args[n]))
			} else {
				b.WriteString("{" + spec[0] + "}")
			}
			i += j
		default:
			b.WriteByte(c)
		}
	}
	return b.String()
}

// formatArg is MessageFormat's text of an argument: "null" for nil, a
// grouped number for an integer, the string itself, fmt.Sprint otherwise.
func formatArg(a any) string {
	switch v := a.(type) {
	case nil:
		return "null"
	case int:
		return groupDigits(strconv.FormatInt(int64(v), 10))
	case int64:
		return groupDigits(strconv.FormatInt(v, 10))
	case string:
		return v
	default:
		return fmt.Sprint(v)
	}
}

// groupDigits inserts ',' every three digits from the right ("-1234567" →
// "-1,234,567").
func groupDigits(s string) string {
	sign := ""
	if strings.HasPrefix(s, "-") {
		sign, s = "-", s[1:]
	}
	var b strings.Builder
	for i := 0; i < len(s); i++ {
		if i > 0 && (len(s)-i)%3 == 0 {
			b.WriteByte(',')
		}
		b.WriteByte(s[i])
	}
	return sign + b.String()
}
