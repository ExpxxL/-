package lc.work.bao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.poi.xwpf.usermodel.*;

public class QuestionBankManagementSystem extends JFrame {
    private ArrayList<Question> questionBank;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField questionField;
    private JTextField answerField;
    private JComboBox<String> questionTypeComboBox;
    private String content;
    private String answer;
    private String type;
    private Sqlite con; 

    public QuestionBankManagementSystem() {
    	con = new Sqlite();
        questionBank = new ArrayList<>();

        setTitle("试卷题库管理系统");

        tableModel = new DefaultTableModel();
        tableModel.addColumn("选择");
        tableModel.addColumn("题目内容");
        tableModel.addColumn("答案");
        tableModel.addColumn("题目类型");

        table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : super.getColumnClass(column);
            }
        };

        TableColumnModel columnModel = table.getTableHeader().getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(columnModel.getColumn(0).getPreferredWidth() * 1);
        columnModel.getColumn(1).setPreferredWidth(columnModel.getColumn(1).getPreferredWidth() * 10);
        columnModel.getColumn(2).setPreferredWidth(columnModel.getColumn(2).getPreferredWidth() * 1);
        columnModel.getColumn(3).setPreferredWidth(columnModel.getColumn(3).getPreferredWidth() * 1);

        int originalRowHeight = table.getRowHeight();
        table.setRowHeight(originalRowHeight * 2);
        table.getColumnModel().getColumn(1).setCellRenderer(new MultilineTableCellRenderer());

        questionField = new JTextField(20);
        answerField = new JTextField(20);

        String[] questionTypes = {"单选题", "多选题", "判断题", "主观题", ""};
        questionTypeComboBox = new JComboBox<>(questionTypes);

        JButton addButton = new JButton("添加题目");
        JButton deleteButton = new JButton("删除题目");
        JButton exportButton = new JButton("导出题库");
        JButton importButton = new JButton("导入题库");
        JButton showButton = new JButton("题库展示");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuestion();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					deleteQuestion();
				} catch (SQLException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportSelectedQuestions();
            }
        });

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importQuestionBank();
            }
        });

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuestionBank();
            }
        });

        JPanel functionBar1 = new JPanel();
        functionBar1.setLayout(new GridLayout(20, 1));
        functionBar1.add(showButton);

        JPanel functionBar2 = new JPanel();
        functionBar2.setLayout(new GridLayout(20, 1));
        functionBar2.add(importButton);
        functionBar2.add(exportButton);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        inputPanel.add(new JLabel("题目内容："));
        inputPanel.add(questionField);
        inputPanel.add(new JLabel("答案："));
        inputPanel.add(answerField);
        inputPanel.add(new JLabel("题目类型："));
        inputPanel.add(questionTypeComboBox);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(functionBar1, BorderLayout.WEST);
        add(functionBar2, BorderLayout.EAST);

        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addQuestion() {
        content = questionField.getText();
        answer = answerField.getText();
        type = (String) questionTypeComboBox.getSelectedItem();

        if(content.length()>0||answer.length()>0) {
	    	if(content.length()>1000) {
	    		JOptionPane.showMessageDialog(QuestionBankManagementSystem.this, "题目长度过长");
	    	}
	    	else {
	        	switch (type) {
				 
		        	case "单选题":{
		            		if(answer.length()>1) {
		            			JOptionPane.showMessageDialog(QuestionBankManagementSystem.this, "答案长度过长");
		            		}
		            		else {
		            			Question question = new Question(content, answer, type);
		        	            questionBank.add(question);

		        	            tableModel.addRow(new Object[]{false, content, answer, type});
		        	            clearFields();
		            			int totalSingleChoiceCount = con.countsingle();
				                new importDatabase().insertSingle_choice(table,questionBank,content,answer,totalSingleChoiceCount+1);
				                JOptionPane.showMessageDialog(null, "导入题库成功", "提示", JOptionPane.INFORMATION_MESSAGE);
		            		}
		            		break;
		            	}
		        	case "多选题":{
		        		if(answer.length()>4) {
		        			JOptionPane.showMessageDialog(QuestionBankManagementSystem.this, "答案长度过长");
		        		}
		        		else {
		        			Question question = new Question(content, answer, type);
		    	            questionBank.add(question);

		    	            tableModel.addRow(new Object[]{false, content, answer, type});
		    	            clearFields();
		        			int totalMultipleChoiceCount = con.countmultiple();
		        			new importDatabase().insertMultiplechoice(table,questionBank,content,answer,totalMultipleChoiceCount+1);
		        			JOptionPane.showMessageDialog(null, "导入题库成功", "提示", JOptionPane.INFORMATION_MESSAGE);
			                break;
		        			}
		        		}
		        	case "判断题":{
			        		Question question = new Question(content, answer, type);
				            questionBank.add(question);
	
				            tableModel.addRow(new Object[]{false, content, answer, type});
				            clearFields();
		        			int totalJudgeCount = con.countjudge();
		            		new importDatabase().insertJudge(table,questionBank,content,answer,totalJudgeCount+1);
		        			JOptionPane.showMessageDialog(null, "导入题库成功", "提示", JOptionPane.INFORMATION_MESSAGE);
			                break;
		            	}
		        	case "主观题":{
			        		Question question = new Question(content, answer, type);
				            questionBank.add(question);
	
				            tableModel.addRow(new Object[]{false, content, answer, type});
				            clearFields();
		        			int totalSubjectiveCount = con.countsubjective();
		            		new importDatabase().insertSubjective(table,questionBank,content,answer,totalSubjectiveCount+1);
		        			JOptionPane.showMessageDialog(null, "导入题库成功", "提示", JOptionPane.INFORMATION_MESSAGE);	
			            	break;
		            	}
				}
	        }
        }
        else {
        	JOptionPane.showMessageDialog(null, "添加题目失败", "提示", JOptionPane.INFORMATION_MESSAGE);	
        }
    }

    private void deleteQuestion() throws SQLException {
        int[] selectedRows = table.getSelectedRows();
        for (int selectedIndex : selectedRows) {
            content = (String) table.getValueAt(selectedIndex, 1);
            answer = (String) table.getValueAt(selectedIndex, 2);
            type = (String) table.getValueAt(selectedIndex, 3);
            delete();
            
            questionBank.remove(selectedIndex);
            tableModel.removeRow(selectedIndex);
        }
    }

    private void delete() throws SQLException {
    	String tableString;
    	int selectedIndex = table.getSelectedRow();
    	String sql;
   	 if (selectedIndex >= 0 && selectedIndex < questionBank.size()) {
       	 // 获取选中行的数据
           content = (String) table.getValueAt(selectedIndex, 1);
           answer = (String) table.getValueAt(selectedIndex, 2);
           type = (String) table.getValueAt(selectedIndex, 3);
           int deletedId = con.getIdByContentAndType(content, type);

           switch(type) {
           case "单选题":
        	   tableString="single_choice";
        	   sql="DELETE FROM single_choice WHERE topic='" + content + "'";
        	   con.delete(sql);
        	   questionBank = con.getSingle_choiceQuestionBank();
        	   con.updateIds(tableString,deletedId);
               break;
           case "多选题":
        	   tableString="multiple_choice";
        	   sql="DELETE FROM multiple_choice WHERE topic='" + content + "'";        	   
        	   con.delete(sql);
        	   questionBank = con.getMultiple_choiceQuestionBank();
        	   con.updateIds(tableString,deletedId);
               break;
           case "判断题":
        	   tableString="judge_questions";
        	   sql="DELETE FROM judge_questions WHERE topic='" + content + "'";
        	   con.delete(sql);
        	   questionBank = con.getJudgeQuestionsQuestionBank();
        	   con.updateIds(tableString,deletedId);
               break;
           case "主观题":
        	   tableString="subjective_questions";
        	   sql="DELETE FROM subjective_questions WHERE topic='" + content + "'";
        	   con.delete(sql);
        	   questionBank = con.getSubjectiveQuestionsBank();
        	   con.updateIds(tableString,deletedId);
               break;
           }
       } else {
           JOptionPane.showMessageDialog(this, "请选择要删除的题目");
       }
    }
    private void exportSelectedQuestions() {
        int rowCount = table.getRowCount();
        ArrayList<Question> selectedQuestions = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            boolean isSelected = (boolean) table.getValueAt(i, 0);
            if (isSelected) {
                String content = (String) table.getValueAt(i, 1);
                String answer = (String) table.getValueAt(i, 2);
                String type = (String) table.getValueAt(i, 3);

                Question question = new Question(content, answer, type);
                selectedQuestions.add(question);
            }
        }

        if (!selectedQuestions.isEmpty()) {
            exportQuestionsToWord(selectedQuestions);
        } else {
            JOptionPane.showMessageDialog(this, "请先选择要导出的题目");
        }
    }

    private void exportQuestionsToWord(ArrayList<Question> questions) {
        try {
            XWPFDocument document = new XWPFDocument();

            for (Question question : questions) {
                addQuestionToDocument(document, question.getContent(), question.getAnswer(), question.getType());
            }

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
            	File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                filePath = filePath.endsWith(".docx") ? filePath : filePath + ".docx";
	            try (FileOutputStream out = new FileOutputStream(filePath)) {
	                document.write(out);
	                JOptionPane.showMessageDialog(this, "选定的题目导出成功！");
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "请选择正确的导出路径！");
	        }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "导出题目失败：" + e.getMessage());
        }
    }

    // 添加题目到文档的方法
    private void addQuestionToDocument(XWPFDocument document, String content, String answer, String type) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();

        run.setText("题目内容：" + content);
        run = paragraph.createRun();
        run.setText("答案：" + answer);
        run = paragraph.createRun();
        run.setText("题目类型：" + type);

        document.createParagraph(); // 添加一个空行
    }

    private void importQuestionBank() {
    	//只能execl表形式
    	path_Interface ip = new path_Interface();
    	ip.setFileSelectedListener(new path_Interface.FileSelectedListener() {
            @Override
            public void onFileSelected(String filePath) {
                System.out.println(filePath);
                if (filePath != null && filePath.endsWith(".xlsx")) {
                    importQuestions iq = new importQuestions();
                    iq.importQuestionsFromExcel(filePath);
                } else {
                    System.err.println("无效的文件路径或文件类型错误！");
                }
            }
        });
    }

    
    private void showQuestionBank() {
        this.dispose();
        new QuestionBankDisplay();
    }

    private void clearFields() {
        questionField.setText("");
        answerField.setText("");
        questionTypeComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new Login();
        new QuestionBankManagementSystem();
    }
}
