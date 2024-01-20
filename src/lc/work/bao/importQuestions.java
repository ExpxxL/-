package lc.work.bao;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class importQuestions {//实现以文件为单位导入
    public void importQuestionsFromExcel(String filePath) {
    	String sql;
        ArrayList<Question> single_choicequestionBank = new ArrayList<>();
        ArrayList<Question> multiple_choicequestionBank = new ArrayList<>();
        ArrayList<Question> judgequestionBank = new ArrayList<>();
        ArrayList<Question> subjectivequestionBank = new ArrayList<>();
        Sqlite con = new Sqlite();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // 假设题目在第一个sheet中

            for (Row row : sheet) {
                Cell questionCell = row.getCell(0);
                Cell answerCell = row.getCell(1);
                Cell typeCell = row.getCell(2);

                String content = questionCell.getStringCellValue();
                String answer = answerCell.getStringCellValue();
                String type = typeCell.getStringCellValue();
                
                Question question = new Question(content, answer, type);
                
                switch(type) {
	                case "单选题":
	                	single_choicequestionBank.add(question);
	                	break;
	                case "多选题":
	                	multiple_choicequestionBank.add(question);
	                	break;
	                case "判断题":
	                	judgequestionBank.add(question);
	                	break;
	                case "主观题":
	                	subjectivequestionBank.add(question);
	                	break;
                }  
                //questionBank.add(question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try (Connection connection = con.getConnection()) {
        	int totalSingleChoiceCount = con.countsingle();
        	int totalMultipleChoiceCount = con.countmultiple();
        	int totalJudgeCount = con.countjudge();
        	int totalSubjectiveCount = con.countsubjective();
        	// 插入单选题
            sql = "INSERT INTO single_choice (id, topic, answer) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Question question : single_choicequestionBank) {
                	statement.setInt(1, totalSingleChoiceCount+1);
                    statement.setString(2, question.getContent());
                    statement.setString(3, question.getAnswer());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 插入多选题
            sql = "INSERT INTO multiple_choice (id, topic, answer) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Question question : multiple_choicequestionBank) {
                	statement.setInt(1, totalMultipleChoiceCount+1);
                    statement.setString(2, question.getContent());
                    statement.setString(3, question.getAnswer());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 插入判断题
            sql = "INSERT INTO judge_questions (id, topic, answer) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Question question : judgequestionBank) {
                	statement.setInt(1, totalJudgeCount+1);
                    statement.setString(2, question.getContent());
                    statement.setString(3, question.getAnswer());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 插入主观题
            sql = "INSERT INTO subjective_questions (id, topic, answer) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Question question : subjectivequestionBank) {
                	statement.setInt(1, totalSubjectiveCount+1);
                    statement.setString(2, question.getContent());
                    statement.setString(3, question.getAnswer());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //return questionBank;
    }
}