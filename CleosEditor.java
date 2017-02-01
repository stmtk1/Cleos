import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.text.BadLocationException;

class CleosEditor extends JTextPane{
    DefaultStyledDocument doc;
    StyleContext sc;
    public String extention;
    
    public CleosEditor(){
       setting(); 
    }
    
    void setting(){
        setSize(10, 10); // CardLayoutなのでSizeは関係ない
        setDocument();
    }
    
    void setDocument(){
        sc = new StyleContext();
        doc = new DefaultStyledDocument(sc);
        setDocument(doc);
    }

    public void setColor(){
        if(extention.equals("html")){
            Html html = new Html(doc);
        }
    }
    
    public void writeDocument(String input){
        try{
            clearDocument();
            doc.insertString(0, input, sc.getStyle(StyleContext.DEFAULT_STYLE));
        }catch(BadLocationException e){
            e.printStackTrace();
        }
    }
    
    void clearDocument(){
        int length = doc.getLength(); 
        try{
            if(length != 0) doc.remove(0, length);
        }catch(BadLocationException e){
            e.printStackTrace();
        }
    }
    public String readDocument(){
        int length = doc.getLength();
        String str = "";
        try{
            if(length != 0) str = doc.getText(0, length - 1);
        }catch(BadLocationException e){
            e.printStackTrace();
        }finally{
            return str;
        }
    }
}
