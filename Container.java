import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.text.BadLocationException;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;

class Container{
    public static void main(String args[]){
        new Container();
    }
    
    public Container(){
    	new CleosFrame(this);
    }
    
    public void addFrame(){
        new CleosFrame(this);
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

class CleosEditor extends JTextPane{
    DefaultStyledDocument doc;
    StyleContext sc;
    
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
       Html html = new Html(doc);
    }
    
    public void writeDocument(String input){
        try{
            clearDocument();
            doc.insertString(0, input, sc.getStyle(StyleContext.DEFAULT_STYLE));
            setColor();
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

class CleosMenuBar extends JMenuBar{
    public CleosMenuBar(CleosFrame frame, Container container){
        addFileMenu(frame, container);
    }
    
    void addFileMenu(CleosFrame frame, Container container){
        FileMenu menu = new FileMenu(frame, container);
        add(menu);
    }
}

class FileMenu extends JMenu implements ActionListener{
    JMenuItem open, save, named_save, new_window, close_window, save_close_window;
    File file;
    CleosFrame frame;
    Container container;
    boolean click_yes;
    
    public FileMenu(CleosFrame frame, Container container){
        super("ファイル");
        this.frame = frame;
        this.container = container;
        addOpenItem();
        addSaveItem();
        addNamedSave();
        addNewWindowItem();
        addCloseWindow();
        addSaveCloseWindow();
    }
    
    void addSaveCloseWindow(){
    	save_close_window = new JMenuItem("保存して閉じる");
    	save_close_window.addActionListener(this);
    	add(save_close_window);
    }
    
    void addCloseWindow(){
        close_window = new JMenuItem("ウィンドウを閉じる");
        close_window.addActionListener(this);
        add(close_window);
    }

    void addNamedSave(){
        named_save = new JMenuItem("名前をつけて保存");
        named_save.addActionListener(this);
        add(named_save);
    }
    
    void addOpenItem(){
        open = new JMenuItem("開く");
        open.addActionListener(this);
        add(open);
    }

    void addSaveItem(){
        save = new JMenuItem("上書き保存");
        save.addActionListener(this);
        add(save);
    }
    
    void addNewWindowItem(){
        new_window = new JMenuItem("新しいウィンドウ");
        new_window.addActionListener(this);
        add(new_window);
    }
    
    boolean chooseFile(boolean newFile){
        JFileChooser c = new JFileChooser();
        int selected;
        if(newFile){
            selected = c.showSaveDialog(this);
        }else{
            selected = c.showOpenDialog(this);
        }
        if(selected == JFileChooser.APPROVE_OPTION){
            file = c.getSelectedFile();
            return true;
        }
        return false;
    }

    void file_write(String input){
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bw.write(input, 0, input.length());
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    String file_read(){
        String line;
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            return sb.toString();
        }
    }
    
    boolean closeDialog(){
    	if(JOptionPane.showConfirmDialog(frame, "終了しますか？") == JOptionPane.YES_OPTION){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    void closing(){
    	if(closeDialog()){
    		System.exit(0);
    	}
    }
    void saveClosing(){
    	if(closeDialog()){
    		if(file == null && !chooseFile(true)) return;
            file_write(frame.readEditor());
    		System.exit(0);
    	}
    }
    
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == open){
            if(chooseFile(false)) frame.rewriteEditor(file_read());
        }else if(e.getSource() == save){
                if(file == null && !chooseFile(true)) return;
                file_write(frame.readEditor());
            
        }else if(e.getSource() == named_save){
            if(chooseFile(true)){
                file_write(frame.readEditor());
            }
        }else if(e.getSource() == new_window){
            container.addFrame();
        }else if(e.getSource() == close_window){
            closing();
        }else if(e.getSource() == save_close_window){
        	saveClosing();
        }
    }
}
