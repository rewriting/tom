
import org.antlr.runtime.*;

public class HostParser {

    private CharStream input;
    private StringBuffer hostContent;

    public HostParser(CharStream input) {
        this.input = input;
        hostContent = new StringBuffer();
    }

    public void parse() {
        ConstructWatcher match = new ConstructWatcher("%match");
        ConstructWatcher op = new ConstructWatcher("%op");
        ConstructWatcher[] listconstructors = ConstructWatcher[2];
        listconstructors[0] = match;
        listconstructors[1] = op;
        while (true) {
            char truc = (char) input.LA(1);
            match.take(truc);
            op.take(truc);
            if (truc == (char) -1) {
                break;
            }
            for (int i = 0; i < listconstructors.length(); i++) {
                {
                    if (listconstructors[i].state == listconstructors[i].keyword.length()) {
                        //
                    }
                }
                hostContent.append(truc);
                input.consume();
            }
        }
    }

    public String getCode() {
        return hostContent.toString();
    }
}
