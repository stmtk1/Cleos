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
    MutableAttributeSet include, define, func;
    int last_end;
    boolean in_function;
    
    public C(DefaultStyledDocument doc){
        in_function = false;
        last_end = 0;
        this.doc = doc;
        include = new SimpleAttributeSet();
        StyleConstants.setForeground(include, new Color(255, 0, 0));
        define = new SimpleAttributeSet();
        StyleConstants.setForeground(define, new Color(0, 255, 0));
        func = new SimpleAttributeSet();
        StyleConstants.setForeground(func, new Color(0, 0, 255));
        try{
            input = doc.getText(0, doc.getLength() - 1);
            parse();
        }catch(BadLocationException e){
            e.printStackTrace();
        }
    }

    void parse(){
        
        for(int i = 0; i < 10; i++){
       // while(input.length() != 0){
            trimInclude();
            trimDefine();
            trimFuncPreDef();
            trimFuncStart();
        }
    }
    
    void trimInclude(){
        Pattern pattern = Pattern.compile("\\A\\s*#include\\p{Space}*[<\\\"]\\p{Alnum}+\\.h[>\\\"]");
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            doc.setCharacterAttributes(last_end + matcher.start(), matcher.group().length(), include, false);
            input = input.substring(matcher.end());
            last_end += matcher.end();
            matcher = pattern.matcher(input);
        }
    }
    
    void trimDefine(){
        Pattern pattern = Pattern.compile("\\A\\s*#define\\p{Space}+[\\p{Upper}\\p{Digit}]+\\p{Space}+\\p{Digit}+(\\.\\p{Digit}+)?");
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            doc.setCharacterAttributes(last_end + matcher.start(), matcher.group().length(), define, false);
            input = input.substring(matcher.end());
            last_end += matcher.end();
            matcher = pattern.matcher(input);
        }
    }
    

    void trimFuncStart(){
        Pattern pattern = Pattern.compile("\\A\\s*\\p{Alpha}+\\p{Space}+\\p{Alpha}\\p{Alnum}*\\(\\)\\{");
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            doc.setCharacterAttributes(last_end + matcher.start(), matcher.group().length(), func, false);
            input = input.substring(matcher.end());
            matcher = pattern.matcher(input);
            in_function = true;
        }
    }
    
    void trimFuncPreDef(){
        Pattern pattern = Pattern.compile("\\A\\s*\\p{Alpha}+\\p{Space}+\\p{Alpha}\\p{Alnum}*\\(\\);");
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            System.out.println(matcher.group());
            input = input.substring(matcher.end());
            matcher = pattern.matcher(input);
        }
    }
}
