/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Group;

import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author Visual-Studio
 */
public class panelListGroupMember extends abstract_panel_user_group {
    public panelListGroupMember(String userName, String userNum, JLabel lbUserAdd) {
        super(userName, userNum, lbUserAdd);
    }
    @Override
    public void selectUser(String userName, String userNum, JLabel lbUserAdd, ArrayList<String> listAddUser) { 
    }
}

