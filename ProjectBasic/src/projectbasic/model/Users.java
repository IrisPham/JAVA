/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbasic.model;

/**
 *
 * @author Visual Studio
 */
public class Users {
    private String userName;
    private String usersPass;

    public Users(String userName, String usersPass) {
        this.userName = userName;
        this.usersPass = usersPass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUsersPass() {
        return usersPass;
    }

    public void setUsersPass(String usersPass) {
        this.usersPass = usersPass;
    }
}
