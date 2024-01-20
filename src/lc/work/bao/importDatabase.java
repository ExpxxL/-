package lc.work.bao;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;

class importDatabase{
	// 实现导入题库的逻辑
	private Sqlite con;
	private String sql;
	public importDatabase() {
		con = new Sqlite();
	}
	void insertSingle_choice(JTable table,ArrayList<Question> questionBank,String content,String answer,int num) {
	        answer=answer.toUpperCase();
	        sql = "INSERT INTO single_choice (id,topic, answer) VALUES('" + num + "', '" + content + "', '" + answer + "');";
	        try {
				con.insert(sql);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	        //JOptionPane.showMessageDialog(null, "导入题库成功", "提示", JOptionPane.INFORMATION_MESSAGE);
	}
	void insertMultiplechoice(JTable table,ArrayList<Question> questionBank,String content,String answer,int num) {
	        answer=answer.toUpperCase();
	        //sql="INSERT INTO multiple_choice (topic, answer) VALUES('"+content+"', '"+answer+"');";
	        sql = "INSERT INTO multiple_choice (id,topic, answer) VALUES('" + num + "', '" + content + "', '" + answer + "');";
	        try {
				con.insert(sql);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	        //JOptionPane.showMessageDialog(null, "导入题库成功", "提示", JOptionPane.INFORMATION_MESSAGE);
	}
	void insertJudge(JTable table,ArrayList<Question> questionBank,String content,String answer,int num) {
	        answer=answer.toUpperCase();
	        sql = "INSERT INTO judge_questions (id,topic, answer) VALUES('" + num + "', '" + content + "', '" + answer + "');";
	        try {
				con.insert(sql);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	        //JOptionPane.showMessageDialog(null, "导入题库成功", "提示", JOptionPane.INFORMATION_MESSAGE);
	}
	void insertSubjective(JTable table,ArrayList<Question> questionBank,String content,String answer,int num) {
	        answer=answer.toUpperCase();
	        sql = "INSERT INTO subjective_questions (id,topic, answer) VALUES('" + num + "', '" + content + "', '" + answer + "');";
	        try {
				con.insert(sql);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	        //JOptionPane.showMessageDialog(null, "导入题库成功", "提示", JOptionPane.INFORMATION_MESSAGE);
	}
}