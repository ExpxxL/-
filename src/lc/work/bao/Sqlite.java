package lc.work.bao;

import java.sql.*;
import java.util.ArrayList;

class Sqlite {
    
    //数据库操作变量
    static PreparedStatement ps = null;
    static ResultSet rs=null;
    static Statement stmt = null;
    
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";  
    private static final String DB_URL = "jdbc:sqlite:project.db";
    
    static Connection conn = null;//数据库连接
    
    Sqlite(){
        try {
            if (conn == null) {
                //sqlite
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("成功连接到SQLite数据库！");
             }
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception ex) {
        	// 处理 Class.forName 错误
            ex.printStackTrace();
        }
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    
    public ResultSet login(){
    	String sql ="SElECT username,password FROM user_information";
		try {
			stmt=conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return rs;
    }
    
    public ArrayList<Question> getSingle_choiceQuestionBank() {
    	//题库界面展示
        ArrayList<Question> questionBank = new ArrayList<>();
        String sql = "SELECT topic,answer FROM Single_choice";
        String type="单选题";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String content = rs.getString("topic");//获取题目
                String answer = rs.getString("answer"); // 获取答案

                Question question = new Question(content, answer, type);
                questionBank.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return questionBank;
    }
    
    public ArrayList<Question> getMultiple_choiceQuestionBank() {
    	//题库界面展示
        ArrayList<Question> questionBank = new ArrayList<>();
        String sql = "SELECT topic,answer FROM multiple_choice";
        String type="多选题";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String content = rs.getString("topic");//获取题目
                String answer = rs.getString("answer"); // 获取答案

                Question question = new Question(content, answer, type);
                questionBank.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return questionBank;
    }
    
    public ArrayList<Question> getJudgeQuestionsQuestionBank() {
    	//题库界面展示
        ArrayList<Question> questionBank = new ArrayList<>();
        String sql = "SELECT topic,answer FROM judge_questions";
        String type="判断题";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String content = rs.getString("topic");//获取题目
                String answer = rs.getString("answer"); // 获取答案

                Question question = new Question(content, answer, type);
                questionBank.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return questionBank;
    }
    
    public ArrayList<Question> getSubjectiveQuestionsBank() {
    	//题库界面展示
        ArrayList<Question> questionBank = new ArrayList<>();
        String sql = "SELECT topic,answer FROM subjective_questions";
        String type="主观题";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String content = rs.getString("topic");//获取题目
                String answer = rs.getString("answer"); // 获取答案

                Question question = new Question(content, answer, type);
                questionBank.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return questionBank;
    }
    public void insert(String sql) throws SQLException {
    	//插入函数
    	ps = conn.prepareStatement(sql);
    	ps.execute();
    	ps.close();
    }
    
    public void updata(String sql,String content,String answer,String yuancontent,String yuananswer) throws SQLException
    {
    	//修改数据
    	try (PreparedStatement ps = conn.prepareStatement(sql)) {
	    	ps.setString(1, content);
	        ps.setString(2, answer);
	        ps.setString(3, yuancontent);
	        ps.setString(4, yuananswer);
	        int rowsUpdated=ps.executeUpdate();
	        System.out.println("Rows updated: " + rowsUpdated);
    	}
    }
    public void delete(String sql) throws SQLException
    {
    	//删除
    	ps = conn.prepareStatement(sql);
    	ps.execute();
    	ps.close();
        return;
    }
    public Question select(String sql, String type) throws SQLException {
        Question question = null;
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String content = rs.getString("topic"); // 获取题目
                String answer = rs.getString("answer"); // 获取答案

                question = new Question(content, answer, type);
            }
        }

        return question;
    }

    public void finalize() throws SQLException
    {
    	//善后操作
        rs.close();
        stmt.close();
    }
    public int countsingle() {
    	//计算单选题表的数量
        int i = 0;  // 初始值设为0
        String sql = "SELECT topic,answer FROM Single_choice";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public int countmultiple() {
    	//计算多选题表的数量
    	int i = 0;	
    	String sql = "SELECT topic,answer FROM multiple_choice";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
            	i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }
    public int countjudge() {
    	//计算判断题表的数量
    	int i = 0;	
    	String sql = "SELECT topic,answer FROM judge_questions";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
            	i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }
    public int countsubjective() {
    	//计算主观题表的数量
    	int i = 0;	
    	String sql = "SELECT topic,answer FROM subjective_questions";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
            	i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }
    
    public int getIdByContentAndType(String content, String type) {
    	//返回对应数据的id
        int id = -1; // 默认为-1表示未找到
        String sql = null;
        switch (type) {
			case "单选题": {
				sql = "SELECT id FROM single_choice WHERE topic=?";
				break;
			}
			case "多选题": {
				sql = "SELECT id FROM multiple_choice WHERE topic=?";		
				break;
			}
			case "判断题": {
				sql = "SELECT id FROM judge_questions WHERE topic=?";
				break;
			}
			case "主观题": {
				sql = "SELECT id FROM subjective_questions WHERE topic=?";
				break;
			}
        }
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, content);
            rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    
    public void updateIds(String tableName,int deletedId) {
    	//删除后，将id也重新排序
        String updateSql = "UPDATE '" + tableName + "' SET id = id - 1 WHERE id > ?";

        try {
            ps = conn.prepareStatement(updateSql);
            ps.setInt(1, deletedId);  // deletedId 是你删除的那一行的 id
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
