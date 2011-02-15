import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.CommonToken;
import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Tree treeparsing= new CommonTree();
        FileInputStream fichier = new FileInputStream(args[0]);
        String pattern = "%match";
        boolean isTomParsing = false;
        int readstate = 0;
        StringBuffer TomParsing = null;
        StringBuffer readString = null;
        char readChar;
        int state = 0;
        while (readstate != -1) {
            if (isTomParsing == false) {
                readstate = fichier.read();
                readChar = (char) readstate;
                if (readChar == pattern.charAt(state)) {
                    state++;
                } else {
                    state = 0;
                    readString.append(readChar);
                }
                if (state == 5) {
                    isTomParsing = true;
                    //ajouter readString au noeud et le réinitialiser,ici je dois en faite ajouter readString à la noeud, je cherche encore.
                    Tree leaf=new CommonTree();
                    treeparsing.addChild(leaf);
                }
            } else {
                    GParser tomparse = new GParser(fichier,treeparsing);
                    isTomParsing = false;
                }
            }
        //Affichage de l'arbre
        treeparsing.toString();
        }        
    }

