


import java.awt.Color;
import java.io.OutputStream;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * <p>Titre : Classe JTextAreaSystemOut</p>
 *
 * <p>Description : Cette classe va permettre d'afficher les informations qui
 * était sur la console sur le JTextArea </p>
 *
 * @author FLORENTIN , d'aprés un document de Michel Deriaz
 */
public class JTextAreaSystemOut extends OutputStream {
    //Attributs ------------------------------------------

    private boolean red;
    private SimpleAttributeSet textred;
    private StyledDocument styleretour;
    //Constructeur ---------------------------------------

    public JTextAreaSystemOut(JTextPane ta, boolean red) {
        this.red = red;
        styleretour = ta.getStyledDocument();
        textred = new SimpleAttributeSet();
        StyleConstants.setForeground(textred, Color.red);
    }

    /**
     * public abstract void write(int b)
     * throws IOException
     * Writes the specified byte to this output stream.
     * @param b int
     */
    public void write(int b) {
        try {
            if (red) {
                styleretour.insertString(styleretour.getLength(), new String(new byte[]{(byte) b}), textred);
            } else {
                styleretour.insertString(styleretour.getLength(), new String(new byte[]{(byte) b}), null);
            }
        } catch (BadLocationException ex) {
            System.err.println(ex.getMessage());
        }
    }
}