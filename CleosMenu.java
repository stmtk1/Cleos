import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CleosMenuBar extends JMenuBar{
    private static final long serialVersionUID = 101L;
    public String extention;
    public CleosMenuBar(CleosFrame frame, Container container){
        addFileMenu(frame, container);
    }
    
    void addFileMenu(CleosFrame frame, Container container){
        FileMenu menu = new FileMenu(frame, container);
        add(menu);
    }
}

class FileMenu extends JMenu implements ActionListener{
    private static final long serialVersionUID = 102L;
    JMenuItem open, save, named_save, new_window, close_window, save_close_window;
    File file;
    CleosFrame frame;
    Container container;
    boolean click_yes;
    
    private String getExtention(){
        Matcher extention = Pattern.compile("\\.(\\p{Alnum}+)").matcher(file.getName());
        if(extention.find()){
            return extention.group(1);
        }else{
            return "";
        }
    }

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
        }
        return sb.toString();
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
            frame.updateExtention(getExtention());
    		System.exit(0);
    	}
    }
    
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == open){
            if(chooseFile(false)){
                frame.rewriteEditor(file_read());
                frame.updateExtention(getExtention());
            }
        }else if(e.getSource() == save){
                if(file == null && !chooseFile(true)) return;
                file_write(frame.readEditor());
                frame.updateExtention(getExtention());
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
