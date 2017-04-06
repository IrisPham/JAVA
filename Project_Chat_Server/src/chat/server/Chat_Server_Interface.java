/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.server;
public interface Chat_Server_Interface{
    //Phương thức dùng để trả về thông tin user
    public void information(Integer info);
    //Phương thức dùng để đăng ký tài khoản
    public void reGis();
    //Phương thức dùng để kết nối Client
    public void connection(String userNum,String userPass);
    //Phương thức dùng để ngắt kết nối Client
    public void diConnection();
    //Phương thức dùng để gửi SMS face to face
    public void sentMessageClientOnly(Integer x, Integer y,String message,String Sticker);
    //Phuong thuc dung de gui tin nhan all (Nhom)
    public void sentMessageGroup(String[] data);
    //Phương thức dùng để gửi về danh sách bạn bè
    public void sentListFriends(Integer idFriends);
    //Phương thức yêu cầu xóa bạn
    public void deleteFriends(String userName,Integer userNum,String userPass);
    //Phương thức dùng để gửi về tin nhắn offline
    public void sentListChatSms(String meOne, String meTow);
    //Phương thức dùng để thêm một bạn
    public void addFriends(String[] data);
    public void loadAddFriends(String[] data);
    
    //Phương thức dùng để xử lí tất cả về nhóm
    public void listCreateGroup(String[] data);
    public void addGroupName(String userNumSent,String groupsName,String memberGroup);
    public void loadGroupName(String[] data);
}
