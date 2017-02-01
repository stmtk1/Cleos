import javax.swing.JFrame;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.CardLayout;

class FrameEvent implements WindowListener{
    Container container;
    CleosFrame frame;
    public FrameEvent(Container container, CleosFrame frame){
        this.container = container;
        this.frame = frame;
    }
    
    public void windowActivated(WindowEvent e){
        // System.out.println("activated");
    }
    
    public void windowClosed(WindowEvent e){
        // System.out.println("Closed");
    }
    
    public void windowClosing(WindowEvent e){
    }
    
    public void windowDeactivated(WindowEvent e){
        // System.out.println("deactivate");
    }
    
    public void windowDeiconified(WindowEvent e){
        // System.out.println("deiconified");
    }
    
    public void windowIconified(WindowEvent e){
        // System.out.println("iconified");
    }
    
    public void windowOpened(WindowEvent e){
        // System.out.println("opened");
    }
    
    boolean confirmExit(){
        return true;
    }
}

class CleosFrame extends JFrame{
    CleosEditor editor;
    Container container;
    public CleosFrame(Container container){
        this.container = container;
        setting();
        addMenuBar();
        addEditor();
        setVisible(true);
    }
    
    void setting(){
        setSize(700, 700);
        setLayout(new CardLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new FrameEvent(container, this));
    }
    
    void addEditor(){
        editor = new CleosEditor();
        getContentPane().add(editor);
    }
    
    void addMenuBar(){
        CleosMenuBar bar = new CleosMenuBar(this, container);
        setJMenuBar(bar);
    }
    
    public void rewriteEditor(String str){
        editor.writeDocument(str);
    }

    public String readEditor(){
        return editor.readDocument();
    }
}
