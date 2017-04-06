/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.client;

/**
 *
 * @author Visual-Studio
 */
public interface chat_client_interface {
    //Phương thức yêu cầu Server trả về thông tin (tên)
    public void information(String info);
    //Phương thức yêu cầu Server trả về all danh sách người dùng đang sử dụng CTU Talks
    public void allUser(String allUser);
    //Dùng để chat 1 người với 1 người
    public void chatOnLy(String[] data);
    //Phương thức dùng để chat nhóm
    public void chatGroup(String[] data);
    //Phương thức yêu cầu Server Load tin nhắn offline
    public void getLoadChat(String userSent,String userRe,String textChat);
    //Phương thức yêu cầu Server trả về danh sách bạn
    public void getListFriend(String userName,Integer userNum, String userPass);
    //Phương thức xử lí kết quả xóa bạn từ Server gửi về
    public void repDeleteFriends(String Statut);
    //Phương thức xử lí danh sách chat
    public void getListChatFriend(String userName,Integer userNum, String userPass);
    //Phương thức dùng để loadListFriend lên panelCreateGroup
    public  void ListCreateGroup(String userName,String userNum,String userPass);
    //Phương thức dùng để check xem tạo nhóm thành công hay bại và load danh sách nhóm
    public void addGroupName(String check);
    
}
