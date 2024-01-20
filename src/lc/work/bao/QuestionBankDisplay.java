package lc.work.bao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

class QuestionBankDisplay extends JFrame {
    private ArrayList<Question> questionBank;
    private DefaultTableModel tableModel;
    private JTable table;

    private JButton prevButton;
    private JButton nextButton;
    private JButton updataButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JTextField searchField;
    
    private JButton mainButton;
    private JButton exportButton;
    private JButton questionepButton;
    
    private JButton singleChoiceButton;
    private JButton multipleChoiceButton;
    private JButton judgeQuestionsButton;
    private JButton subjectiveQuestionsButton;

    private Sqlite con;
    
    private String content;
    private String answer;
    private String type;
    
    private String updataedcontent;
    private String updataedanswer;
    private String updataedtype;
    
    private int biaobiaozhi;

    // 添加这两个常量以确定每页显示的行数和当前页
    private static final int 每页行数 = 10;
    private int 当前页 = 0;

    public QuestionBankDisplay() {
        con = new Sqlite();
        this.questionBank = con.getSingle_choiceQuestionBank();
        biaobiaozhi=1;

        setTitle("题库展示");

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

        initializeTableData();

        prevButton = new JButton("上一页");
        nextButton = new JButton("下一页");
        updataButton = new JButton("修改");
        deleteButton =new JButton("删除");
        saveButton= new JButton("保存");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");
        mainButton = new JButton("主界面");
        exportButton = new JButton("出卷界面");
        questionepButton =new JButton("导出题库");
        singleChoiceButton = new JButton("显示单选题");
        multipleChoiceButton = new JButton("显示多选题");
        judgeQuestionsButton = new JButton("显示判断题");
        subjectiveQuestionsButton = new JButton("显示主观题");

        saveButton.setVisible(false);
        
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousPage();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextPage();
            }
        });

        updataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					updata();
				} catch (SQLException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					save();
				} catch (SQLException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					delete();
				} catch (SQLException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchQuestions();
            }
        });

        singleChoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                singleChoiceQuestion();
            }
        });

        multipleChoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multipleChoiceQuestionBank();
            }
        });

        judgeQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                judgeQuestionsQuestionBank();
            }
        });

        subjectiveQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subjectiveQuestionsBank();
            }
        });

        mainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuestionBank();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Questionexport();
            }
        });

        questionepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	exportSelectedQuestions();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2));
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);
        buttonPanel.add(updataButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        JPanel functionBar1 = new JPanel();
        functionBar1.setLayout(new GridLayout(20, 1));
        //functionBar1.add(mainButton);
        //functionBar1.add(produceButton);
        functionBar1.add(singleChoiceButton);
        functionBar1.add(multipleChoiceButton);
        functionBar1.add(judgeQuestionsButton);
        functionBar1.add(subjectiveQuestionsButton);
        functionBar1.add(saveButton);
        
        JPanel functionBar2 = new JPanel();
        functionBar2.setLayout(new GridLayout(20, 1));
        functionBar2.add(mainButton);
        functionBar2.add(exportButton);
        functionBar2.add(questionepButton);

        add(functionBar1, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(functionBar2, BorderLayout.EAST);

        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeTableData() {
        for (Question question : questionBank) {
            //tableModel.addRow(new Object[]{question.getContent(), question.getAnswer(), question.getType()});
        	tableModel.addRow(new Object[]{false,question.getContent(), question.getAnswer(), question.getType()});
        }
    }

    private void showPreviousPage() {
        if (当前页 > 0) {
            当前页--;
            updateTableData();
        } else {
            JOptionPane.showMessageDialog(this, "已经是第一页");
        }
    }

    private void showNextPage() {
        int 最大页 = questionBank.size() / 每页行数;
        if (当前页 < 最大页) {
            当前页++;
            updateTableData();
        } else {
            JOptionPane.showMessageDialog(this, "已经是最后一页");
        }
    }

    private void updata() throws SQLException {
    	int selectedIndex = table.getSelectedRow();
    	String sql;
    	 if (selectedIndex >= 0 && selectedIndex < questionBank.size()) {
        	 // 获取选中行的数据
            content = (String) table.getValueAt(selectedIndex, 1);
            answer = (String) table.getValueAt(selectedIndex, 2);
            type = (String) table.getValueAt(selectedIndex, 3);

            // 打印选中行的数据
            System.out.println("已选中要修改的：" );
            System.out.println("题目：" + content);
            System.out.println("答案：" + answer);
            System.out.println("类型：" + type);
  
            saveButton.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "请选择要修改的题目");
        }
    }
    
    private void save() throws SQLException {
    	 int selectedIndex = table.getSelectedRow();
    	 String sql;
    	    if (selectedIndex >= 0 && selectedIndex < questionBank.size()) {
    	        // 更新选中行的数据
    	    	updataedcontent = (String) table.getValueAt(selectedIndex, 1);
    	    	updataedanswer = (String) table.getValueAt(selectedIndex, 2);
    	    	updataedtype = (String) table.getValueAt(selectedIndex, 3);
    	    	
    	    	System.out.println("已经修改为：");
    	        System.out.println("题目：" + updataedcontent);
    	        System.out.println("答案：" + updataedanswer);
    	        System.out.println("类型：" + updataedtype);
    	        switch(biaobiaozhi) {
    	           case 1:
    	        	   sql = "UPDATE single_choice SET topic = ?, answer = ? WHERE topic = ? and answer = ?";
    	        	   con.updata(sql,updataedcontent,updataedanswer,content,answer);
    	        	   questionBank = con.getSingle_choiceQuestionBank();
    	               updateTableData();
    	               biaobiaozhi=1;
    	               break;
    	           case 2:
    	        	   sql = "UPDATE multiple_choice SET topic = ?, answer = ? WHERE topic = ? and answer = ?";
    	        	   con.updata(sql,updataedcontent,updataedanswer,content,answer);
    	        	   questionBank = con.getMultiple_choiceQuestionBank();
    	               updateTableData();
    	               biaobiaozhi=2;
    	               break;
    	           case 3:
    	        	   sql = "UPDATE judge_questions SET topic = ?, answer = ? WHERE topic = ? and answer = ?";
    	        	   con.updata(sql,updataedcontent,updataedanswer,content,answer);
    	        	   questionBank = con.getJudgeQuestionsQuestionBank();
    	               updateTableData();
    	               biaobiaozhi=3;
    	               break;
    	           case 4:
    	        	   sql = "UPDATE subjective_questions SET topic = ?, answer = ? WHERE topic = ? and answer = ?";
    	        	   con.updata(sql,updataedcontent,updataedanswer,content,answer);
    	        	   questionBank = con.getSubjectiveQuestionsBank();
    	               updateTableData();
    	               biaobiaozhi=4;
    	               break;
    	        }
    	        saveButton.setVisible(false);
    	    }else {
    	           JOptionPane.showMessageDialog(this, "请选择要修改的题目");
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

           // 打印选中行的数据
           System.out.println("已经删去：" );
           System.out.println("题目：" + content);
           System.out.println("答案：" + answer);
           System.out.println("类型：" + type);
           System.out.println("被删除行的 id：" + deletedId);
           
           switch(biaobiaozhi) {
           case 1:
        	   tableString="single_choice";
        	   sql="DELETE FROM single_choice WHERE topic='" + content + "'";
        	   con.delete(sql);
        	   questionBank = con.getSingle_choiceQuestionBank();
        	   con.updateIds(tableString,deletedId);
               updateTableData();
               biaobiaozhi=1;
               break;
           case 2:
        	   tableString="multiple_choice";
        	   sql="DELETE FROM multiple_choice WHERE topic='" + content + "'";        	   
        	   con.delete(sql);
        	   questionBank = con.getMultiple_choiceQuestionBank();
        	   con.updateIds(tableString,deletedId);
               updateTableData();
               biaobiaozhi=2;
               break;
           case 3:
        	   tableString="judge_questions";
        	   sql="DELETE FROM judge_questions WHERE topic='" + content + "'";
        	   con.delete(sql);
        	   questionBank = con.getJudgeQuestionsQuestionBank();
        	   con.updateIds(tableString,deletedId);
               updateTableData();
               biaobiaozhi=3;
               break;
           case 4:
        	   tableString="subjective_questions";
        	   sql="DELETE FROM subjective_questions WHERE topic='" + content + "'";
        	   con.delete(sql);
        	   questionBank = con.getSubjectiveQuestionsBank();
        	   con.updateIds(tableString,deletedId);
               updateTableData();
               biaobiaozhi=4;
               break;
           }
           //questionBank.remove(selectedIndex);
           //tableModel.removeRow(selectedIndex);
       } else {
           JOptionPane.showMessageDialog(this, "请选择要删除的题目");
       }
    }
    
    private void searchQuestions() {
        String keyword = searchField.getText().toLowerCase();
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            if (!tableModel.getValueAt(i, 1).toString().toLowerCase().contains(keyword)) {
                tableModel.removeRow(i);
            }
        }
    }

    private void singleChoiceQuestion() {
        questionBank = con.getSingle_choiceQuestionBank();
        updateTableData();
        biaobiaozhi=1;
    }

    private void multipleChoiceQuestionBank() {
        questionBank = con.getMultiple_choiceQuestionBank();
        updateTableData();
        biaobiaozhi=2;
    }

    private void judgeQuestionsQuestionBank() {
        questionBank = con.getJudgeQuestionsQuestionBank();
        updateTableData();
        biaobiaozhi=3;
    }

    private void subjectiveQuestionsBank() {
        questionBank = con.getSubjectiveQuestionsBank();
        updateTableData();
        biaobiaozhi=4;
    }

    private void showQuestionBank() {
    	this.dispose();
    	new QuestionBankManagementSystem();
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

    private void Questionexport() {
    	new PaperGenerator();
    }
    
    private void updateTableData() {
        int 起始索引 = 当前页 * 每页行数;
        int 结束索引 = Math.min(起始索引 + 每页行数, questionBank.size());

        tableModel.setRowCount(0);

        for (int i = 起始索引; i < 结束索引; i++) {
            Question question = questionBank.get(i);
            tableModel.addRow(new Object[]{false,question.getContent(), question.getAnswer(), question.getType()});
        }
    }
    
    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> {
            //ArrayList<Question> questionBank = new ArrayList<>();
            //questionBank.add(new Question("问题1", "答案1", "单选题"));
            //questionBank.add(new Question("问题2", "答案2", "多选题"));
            //questionBank.add(new Question("问题3", "答案3", "判断题"));

            new QuestionBankDisplay();
        //});
    }
}
