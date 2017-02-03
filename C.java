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
    MutableAttributeSet include, define;
    int last_end;
    
    public C(DefaultStyledDocument doc){
        last_end = 0;
        this.doc = doc;
        include = new SimpleAttributeSet();
        StyleConstants.setForeground(include, new Color(255, 0, 0));
        define = new SimpleAttributeSet();
        StyleConstants.setForeground(define, new Color(0, 255, 0));
        try{
            input = doc.getText(0, doc.getLength() - 1);
            trimInclude();
            trimDefine();
        }catch(BadLocationException e){
            e.printStackTrace();
        }
    }
    
    void trimInclude(){
        Pattern pattern = Pattern.compile("^\\s*#include\\p{Space}*[<\\\"]\\p{Alnum}+\\.h[>\\\"]");
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            doc.setCharacterAttributes(last_end + matcher.start(), matcher.group().length(), include, false);
            input = input.substring(matcher.end());
            last_end += matcher.end();
            matcher = pattern.matcher(input);
        }
    }
    
    void trimDefine(){
        Pattern pattern = Pattern.compile("^\\s*#define\\p{Space}+[\\p{Upper}\\p{Digit}]+\\p{Space}+\\p{Digit}+(\\.\\p{Digit}+)?");
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            doc.setCharacterAttributes(last_end + matcher.start(), matcher.group().length(), define, false);
            input = input.substring(matcher.end());
            last_end += matcher.end();
            matcher = pattern.matcher(input);
        }
    }
}
