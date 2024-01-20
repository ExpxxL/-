package lc.work.bao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class path_Interface extends JFrame {
	private FileSelectedListener fileSelectedListener;
    private JTextField filePathTextField;
    private String selectedFilePath; // 保存选中的文件路径
    
    public void setFileSelectedListener(FileSelectedListener listener) {
        this.fileSelectedListener = listener;
    }
    
    public path_Interface() {
        setTitle("文件选择器");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 100);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton chooseFileButton = new JButton("选择文件");
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            /*public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    selectedFilePath = selectedFile.getAbsolutePath();
                    filePathTextField.setText(selectedFilePath);
                }
            }*/
            /*public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    selectedFilePath = selectedFile.getAbsolutePath();
                    filePathTextField.setText(selectedFilePath);
                }
            }*/
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = fileChooser.getSelectedFile();
		            selectedFilePath = selectedFile.getAbsolutePath();
		            filePathTextField.setText(selectedFilePath);
		            if (fileSelectedListener != null) {
	                    fileSelectedListener.onFileSelected(selectedFilePath);
	                }
		            dispose(); // 选择完文件后关闭路径界面
               }
            }
        });
        panel.add(chooseFileButton);

        filePathTextField = new JTextField(20);
        panel.add(filePathTextField);

        add(panel);
    }
    public interface FileSelectedListener {
        void onFileSelected(String filePath);
    }
    public String getSelectedFilePath() {
        return selectedFilePath;
    }
}
