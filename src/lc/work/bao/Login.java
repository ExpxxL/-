package lc.work.bao;

import java.sql.*;
import javax.swing.*;

class Login {
	//实现登录功能
	Sqlite con = new Sqlite();
    int panbie = 0;
    JPanel panel = new JPanel();
    JLabel label = new JLabel("请输入管理员凭据：");
    JTextField usernameField = new JTextField(10);
    JPasswordField passwordField = new JPasswordField(10);

    public Login() {
        panel.add(label);
        panel.add(new JLabel("用户名："));
        panel.add(usernameField);
        panel.add(new JLabel("密码："));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "管理员登录", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String enteredUsername = usernameField.getText();
            String enteredPassword = new String(passwordField.getPassword());
            ResultSet rs = con.login();
            try {
                while (rs.next()) {
                    String db_username = rs.getString("username");
                    String db_password = rs.getString("password");
                    if (enteredUsername.equals(db_username) && enteredPassword.equals(db_password)) {
                        panbie = 1;
                        // 登录成功
                    }
                }
                if(panbie==0) {
                	JOptionPane.showMessageDialog(null, "凭据无效。程序退出。");
                    System.out.println("凭据无效。程序退出...");
                    System.exit(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } 
        }
        else {
        System.out.println("登录失败...");
        System.exit(0);
        }
        try {
            con.finalize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("登录成功...");
    }
}