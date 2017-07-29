/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbasic.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import projectbasic.interfaces.Person;
import projectbasic.model.Users;
import projectbasic.view.Login;

/**
 *
 * @author Visual Studio
 */
public class LoginController implements Person{
    private Users users;
    private Login login;
    
    public LoginController(Login login) {
        this.login = login;
        this.login.setActonForLoginButton(new LoginAction());
    }
    class LoginAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
           users = login.getUser();
           if(users == null){
               JOptionPane.showConfirmDialog(null,"Vui lòng không để ô nào trống!");
               return;
           }
           login(users.getUserName(),users.getUsersPass());
        }
    }
    @Override
    public void login(String userName, String userPass) {
//        if(.equals("admin") && .equals("admin")){
////               JOptionPane.showConfirmDialog(null,"Bạn đã đăng nhập thành công!");
////Làm cái j đó đối với admin
//               return;
//           }
    }
}
