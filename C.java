import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedList;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.util.NoSuchElementException;

class C{
    String input;
    LinkedList<String> tags;
    DefaultStyledDocument doc;
    MutableAttributeSet normal, error;
    
    public C(DefaultStyledDocument doc){
        this.doc = doc;
        normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, new Color(255, 0, 0));
        try{
            input = doc.getText(0, doc.getLength() - 1);
            System.out.println(input);
        }catch(BadLocationException e){
            e.printStackTrace();
        }
    }
    
    void lexer(){
        Matcher matcher = Pattern.compile("^#include\\p{Space}*").matcher(input);
    }
}