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
    
    /*
    private void getTags(){
        // Matcher tag = Pattern.compile("</?\\p{Alpha}\\p{Alnum}*>").matcher(input);
        Matcher tag = Pattern.compile("</?\\p{Alpha}\\p{Alnum}*>").matcher(input);
        
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, new Color(255, 0, 0));
        while(tag.find()){
            int start = tag.start();
            String matched = tag.group();
            // System.out.println(matched);
            getName(matched);
            doc.setCharacterAttributes(start + now, matched.length(), attr, false);
            now += (start + matched.length());
            input = input.substring(tag.end());
            tag = Pattern.compile("</?\\p{Alpha}\\p{Alnum}*>").matcher(input);
        }
        if(tags.size() > 0) System.err.println("There is no end tag");
    }
    */
    
    private void getTags(){
        Pattern s_pat = Pattern.compile("</?(\\p{Alpha}\\p{Alnum}*)");
        Matcher start = s_pat.matcher(input);
        Pattern e_pat = Pattern.compile("^>");
        int last_end = 0;
        while(start.find()){
            if(start.group().startsWith("</")){
                if(start.group().equals(tags.pop())) System.err.println("hello world");
            }else{
                tags.push(start.group(1));
            }
            input = input.substring(start.end());
            System.out.println(last_end = trimEnd(start.start(), last_end + start.end()));
            start = s_pat.matcher(input);
        }
    }

    private int trimEnd(int start, int offset){
        Matcher end = Pattern.compile("^>").matcher(input);
        if(end.find()) input = input.substring(end.end());
        return offset + end.end();
    }
    
    /*
    private void getName(String tag){
        Matcher matcher = Pattern.compile("\\p{Alpha}\\p{Alnum}*").matcher(tag);
        String matched;
        try{
        if(matcher.find()){
            if(tag.startsWith("</")){
                matched = matcher.group();
                String poped = tags.pop();
                if(poped.equals(matched)){
                    System.out.println(matcher.group());
                    System.out.println(tag);
                }else{
                    // System.err.println("end tag " + poped + " expected");
                    System.out.println("a:" + tag);
                }
            }else{
                tags.push(matcher.group());
            }
        }
        }catch(NoSuchElementException e){
            System.out.println(tag);
        }
    }
    */
}
