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

class Html{
    String input;
    LinkedList<String> tags;
    DefaultStyledDocument doc;
    int now;
    MutableAttributeSet normal, error;
    public Html(String input){
        tags = new LinkedList<String>();
        this.input = input;
        getTags();
    }
    
    public Html(DefaultStyledDocument doc){
        now = 0;
        this.doc = doc;
        normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, new Color(255, 0, 0));
        tags = new LinkedList<String>();
        try{
            input = doc.getText(0, doc.getLength() - 1);
            trimDoctype();
            getTags();
        }catch(BadLocationException e){
            e.printStackTrace();
        }
    }

    private void trimDoctype(){
        Matcher doctype = Pattern.compile("^\\p{Space}*<!DOCTYPE\\p{Space}+html>").matcher(input);
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, new Color(255, 0, 0));
        if(doctype.find()) doc.setCharacterAttributes(doctype.start(), doctype.group().length(), attr, false);
    }
    
    private void getTags(){
        Pattern s_pat = Pattern.compile("</?(\\p{Alpha}\\p{Alnum}*)");
        Matcher start = s_pat.matcher(input);
        int last_end = 0, length;
        String matched;
        boolean parsed;
        while(start.find()){
            matched = start.group();
            parsed = handleStack(matched, start.group(1));
            input = input.substring(start.end());
            length = matched.length();
            length += trimAttr();
            length += trimEnd();
            doc.setCharacterAttributes(last_end + start.start(), length, normal, false);
            last_end += start.start() + length;
            start = s_pat.matcher(input);
       }
    }

    private boolean handleStack(String match, String stack){
            if(match.startsWith("</")){
                if(stack.equals(tags.pop())) return false;
            }else{
                tags.push(stack);
            }
            return true;
    }

    private int trimEnd(){
        Matcher end = Pattern.compile("^>").matcher(input);
        if(end.find()){
            input = input.substring(end.end());
            return end.group().length();
        }else{
            System.err.println("expected end");
            return 0;
        }
    }

    private int trimAttr(){
        Pattern pattern = Pattern.compile("^\\p{Space}+\\p{Alpha}+=\\\".*\\\"");
        Matcher attr = pattern.matcher(input);
        int length = 0;
        while(attr.find()){
            length += attr.group().length();
            input = input.substring(attr.end());
            attr = pattern.matcher(input);
        }
        return length;
    }
}
