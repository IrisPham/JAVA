/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Visual-Studio
 */
public class connect_SqlServer {
    private Connection con;
    private Statement s; 
    public connect_SqlServer(){
        try{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String URL = "jdbc:sqlserver://DESKTOP-SIJ953I\\SQLEXPRESS:1433;databaseName=DATABASE_Chat;user=sa;password=sa2012";
        con = DriverManager.getConnection(URL);
        s = con.createStatement();
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Lỗi kết nối database");
        }
    }
    public Connection getConnection(){
        return con;
    }
    public Statement getStatement(){
        
        return s;
    }
    
}
