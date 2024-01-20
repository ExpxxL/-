package lc.work.bao;

import javax.swing.*;

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
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PaperGenerator extends JFrame{
    private JTextField subjectiveCountField;
    private JTextField judgeCountField;
    private JTextField multipleChoiceCountField;
    private JTextField singleChoiceCountField;

    private JButton generateButton;

    private Sqlite con; 
    
    public PaperGenerator(){
    	con = new Sqlite();
    	setTitle("出卷界面");
    	
    	initializeUI();
    	
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void initializeUI() {
    	 JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

         JLabel subjectiveLabel = new JLabel("单选题数量:");
         subjectiveCountField = new JTextField();

         JLabel judgeLabel = new JLabel("多选题数量:");
         judgeCountField = new JTextField();

         JLabel multipleChoiceLabel = new JLabel("判断题数量:");
         multipleChoiceCountField = new JTextField();

         JLabel singleChoiceLabel = new JLabel("主观题数量:");
         singleChoiceCountField = new JTextField();

         generateButton = new JButton("生成试卷");

         panel.add(subjectiveLabel);
         panel.add(singleChoiceCountField);

         panel.add(judgeLabel);
         panel.add(multipleChoiceCountField);

         panel.add(multipleChoiceLabel);
         panel.add(judgeCountField);

         panel.add(singleChoiceLabel);
         panel.add(subjectiveCountField);

         panel.add(generateButton);

         generateButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 try {
					generatePaper();
				} catch (SQLException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
             }
         });

         add(panel);
    }
    private void generatePaper() throws SQLException{
    	try {
    		ArrayList<Question> exportQuestions = new ArrayList<>();
    		
	    	int number1 = Integer.parseInt(singleChoiceCountField.getText());
	    	int number2 = Integer.parseInt(multipleChoiceCountField.getText());
	    	int number3 = Integer.parseInt(judgeCountField.getText());
	    	int number4 = Integer.parseInt(subjectiveCountField.getText());
	    	
	    	// 获取数据库中单选题的总数
	    	int totalSingleChoiceCount = con.countsingle();
	        int totalMultipleChoiceCount = con.countmultiple();
	        int totalJudgeCount = con.countjudge();
	        int totalSubjectiveCount = con.countsubjective();
	        System.out.println("totalSingleChoiceCount: " + totalSingleChoiceCount);
	        System.out.println("totalMultipleChoiceCount: " + totalMultipleChoiceCount);
	        System.out.println("totalJudgeCount: " + totalJudgeCount);
	        System.out.println("totalSubjectiveCount: " + totalSubjectiveCount);
	        
	        if (totalSingleChoiceCount < number1) {
	            JOptionPane.showMessageDialog(this, "题库中的单选题数量不足！");
	            return;
	        }
	        if (totalMultipleChoiceCount < number2) {
	            JOptionPane.showMessageDialog(this, "题库中的多选题数量不足！");
	            return;
	        }
	        if (totalJudgeCount < number3) {
	            JOptionPane.showMessageDialog(this, "题库中的判断单选题数量不足！");
	            return;
	        }
	        if (totalSubjectiveCount < number4) {
	            JOptionPane.showMessageDialog(this, "题库中的主观题数量不足！");
	            return;
	        }
	        
	        List<Integer> selectedIndices1 = new ArrayList<>();
            //List<Question> singleChoiceQuestions = new ArrayList<>();
            ArrayList<Question> singleChoiceQuestions = new ArrayList<>();
            
            List<Integer> selectedIndices2 = new ArrayList<>();
            ArrayList<Question> multipleChoiceQuestions = new ArrayList<>();
            
            List<Integer> selectedIndices3 = new ArrayList<>();
            ArrayList<Question> JudgeCQuestions = new ArrayList<>();
            
            List<Integer> selectedIndices4 = new ArrayList<>();
            ArrayList<Question> SubjectiveQuestions = new ArrayList<>();
            
            
            // 随机抽取不重复的单选题
            while (selectedIndices1.size() < number1) {
                //int randomIndex = (int) (Math.random() * totalSingleChoiceCount)+1;
            	Random random = new Random();
            	int randomIndex = random.nextInt(totalSingleChoiceCount) + 1;
                System.out.println("Generated SQL: " + "SELECT * FROM single_choice WHERE id='" + randomIndex + "'");
                System.out.println("Random Index: " + randomIndex);
                // 检查是否已经抽取过
                if (!selectedIndices1.contains(randomIndex)) {
                    selectedIndices1.add(randomIndex);
                    String type="单选题";
                    // 查询数据库获取随机抽取的单选题
                    String sql="SELECT * FROM single_choice WHERE id='" + randomIndex + "'";
                    Question question = con.select(sql,type);
                    System.out.println("Query Result: " + question);
                    //singleChoiceQuestions.add(question);
                 // 检查 question 对象是否为 null
                    if (question != null) {
                        singleChoiceQuestions.add(question);
                    }
                }
            }
            while (selectedIndices2.size() < number2) {
            	Random random = new Random();
            	int randomIndex = random.nextInt(totalMultipleChoiceCount) + 1;

                // 检查是否已经抽取过
                if (!selectedIndices2.contains(randomIndex)) {
                    selectedIndices2.add(randomIndex);
                    String type="多选题";
                    // 查询数据库获取随机抽取的单选题
                    //String sql="SELECT * FROM multiple_choice WHERE id='" + randomIndex + "'";
                    String sql="SELECT * FROM multiple_choice WHERE id='" + randomIndex + "'";
                    //String sql = "SELECT * FROM multiple_choice WHERE id=" + randomIndex;
                    Question question = con.select(sql,type);
                    //multipleChoiceQuestions.add(question);
                    if (question != null) {
                    	multipleChoiceQuestions.add(question);
                    }
                }
            }
            while (selectedIndices3.size() < number3) {
            	Random random = new Random();
            	int randomIndex = random.nextInt(totalJudgeCount) + 1;


                // 检查是否已经抽取过
                if (!selectedIndices3.contains(randomIndex)) {
                    selectedIndices3.add(randomIndex);
                    String type="判断题";
                    // 查询数据库获取随机抽取的单选题
                    String sql="SELECT * FROM judge_questions WHERE id='" + randomIndex + "'";
                    Question question = con.select(sql,type);
                    //JudgeCQuestions.add(question);
                    if (question != null) {
                    	JudgeCQuestions.add(question);
                    }
                }
            }
            while (selectedIndices4.size() < number4) {
            	Random random = new Random();
            	int randomIndex = random.nextInt(totalSubjectiveCount) + 1;


                // 检查是否已经抽取过
                if (!selectedIndices4.contains(randomIndex)) {
                    selectedIndices4.add(randomIndex);
                    String type="主观题";
                    // 查询数据库获取随机抽取的单选题
                    //String sql="SELECT * FROM subjective_questions WHERE id='" + randomIndex + "'";
                    String sql = "SELECT * FROM subjective_questions WHERE id='" + randomIndex + "'";
                    Question question = con.select(sql,type);
                    //SubjectiveQuestions.add(question);
                    if (question != null) {
                    	SubjectiveQuestions.add(question);
                    }
                }
            }
            exportQuestions.addAll(singleChoiceQuestions);
            exportQuestions.addAll(multipleChoiceQuestions);
            exportQuestions.addAll(JudgeCQuestions);
            exportQuestions.addAll(SubjectiveQuestions);
            
            exportQuestionsToWord(exportQuestions);
            
	        JOptionPane.showMessageDialog(this, "试卷生成成功！");
	        dispose();  // 关闭当前窗口
	        
        } catch (NumberFormatException e) {
            // 输入不是有效的数字
            JOptionPane.showMessageDialog(this, "请输入有效的数字！", "Error", JOptionPane.ERROR_MESSAGE);
        }
    	
    	
    }
    private void exportQuestionsToWord(ArrayList<Question> questions) {
        try {
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "没有选定的题目可供导出！");
                return;
            }

            System.out.println("导出的题目：");
            for (Question question : questions) {
                System.out.println("内容：" + question.getContent());
                System.out.println("答案：" + question.getAnswer());
                System.out.println("类型：" + question.getType());
                System.out.println("----------------------");
            }

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
    public static void main(String[] args) {

            new PaperGenerator();

    }
}
