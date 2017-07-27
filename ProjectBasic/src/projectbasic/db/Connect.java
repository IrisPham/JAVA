/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbasic.db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Visual Studio
 */
public class Connect {  
    private final String URL = "jdbc:mysql://mysql.hostinger.vn/u587717341_demo";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String USER_AUTHOR = "u587717341_sv";
    private final String PASS_AUTHOR = "Hoai@n96";
    
    private Connection conn;
    private Statement st;
    private ResultSet rs;
    
    public Connect() {
        try {
            Class.forName(DRIVER);
            conn = (Connection) DriverManager.getConnection(URL, USER_AUTHOR,PASS_AUTHOR);
            st = (Statement) conn.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println(""+ e.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet getRs(String query) {
        ResultSet rss = null;
        try {
            rss = st.executeQuery(query);
        } catch (SQLException e){
            System.out.println(""+e.getMessage());
        }
        return rss;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }
}
