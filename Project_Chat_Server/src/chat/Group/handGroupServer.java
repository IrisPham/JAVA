/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Group;

import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTextArea;

/**
 *
 * @author Visual-Studio
 */
//For Server
//    Tạo nhóm :  				group:createGroup:
//    Xóa nhóm :  				group:deletegrGroup:
//    Load nhóm : 				group:loadGroup:
//    Nhận thông báo mời nhóm :                 group:notifiGroup:
//    Gửi tin nhắn nhóm :                       group:chatGroup:sms:
//Load tin nhắn nhóm off :                      group:chatGroup:sms:loadSms:
//    Gửi icon cảm xúc :                        group:chatGroup:emoij:
//    Gửi Sicker : 				group:chatGroup:sticker:
//    Gủi hình ảnh : 				group:chatGroup:picture:
public class handGroupServer {

    public static void deletegrGroup(String[] data, PrintWriter sentClientt, Statement sa, HashMap idClient, HashMap allClientOnServer) {
        try {
            sa.execute("delete from ChatGroup2 where userNumSent = " + data[3] + " and groupsName = N'" + data[2] + "'; ");
            sentClientt.println("group:deletegrGroup:OK:" + data[2]);
            sentClientt.flush();
            //Dòng để để đếm số dòng là bạn bè!
            //Nhận vào danh ArrayList
            //"group:deletegrGroup:"+nameGroup_+":"+phoneNumberOfUser_+":"+memberUserNum
            //Để kiểm tra coi ai là bạn và cập nhật lại danh sách bạn có bao nhiêu người
            //cắt chuỗi ra
            String[] tamp;
            String tamp2 = data[4];
            String tamp3 = tamp2.substring(1, tamp2.length() - 1);
            tamp = tamp3.split(",");
            for (int i = 0; i < tamp.length; i++) {
                int userNum = Integer.parseInt(tamp[i].trim());
                if (idClient.containsKey(Integer.parseInt(tamp[i].trim())) && userNum != Integer.parseInt(data[3])) {
                    ResultSet rs = sa.executeQuery("select userNumSent,memberGroup from ChatGroup2 where userNumSent = " + tamp[i] + " and userNumReceive is null and groupsName = N'" + data[2] + "';");
                    String[] data2;
                    String memberGroup = null;
                    ArrayList<String> memberGroup2 = new ArrayList<>();
                    while (rs.next()) {
                        memberGroup = rs.getString(2);
                    }
                    String data3 = memberGroup;
                    String data4 = data3.substring(1, data3.length() - 1);
                    data2 = data4.split(",");
                    for (int j = 0; j < data2.length; j++) {
                        int x, y;
                        x = Integer.parseInt(data2[j].trim());
                        y = Integer.parseInt(data[3].trim());
                        if (x != y) {
                            memberGroup2.add(data2[j]);
                        }
                    }
                    //Cập nhật lại danh sách thành viên trong nhóm cho tất cả mọi người
                    sa.execute("update ChatGroup2 set memberGroup = '" + memberGroup2 + "' where userNumSent = " + tamp[i] + " and userNumReceive is null and groupsName = N'" + data[2] + "';");
                    PrintWriter writer2 = new PrintWriter((Writer) idClient.get(Integer.parseInt(tamp[i].trim())));
                    writer2.println("group:deletegrGroup:reLoadListGroup:" + tamp[i].trim());
                    writer2.flush();
                    writer2.println("group:chatGroup:sms:Đã rời nhóm:" + allClientOnServer.get(Integer.parseInt(tamp[i])));
                    writer2.flush();
                }
            }
        } catch (SQLException e) {
            sentClientt.println("group:deletegrGroup:NO:" + data[2] + ":Lỗi " + e.getMessage());
            sentClientt.flush();
        }
    }

    public static void loadGroup(String[] data, PrintWriter sentClientt, Statement sa) {
        //Load tên group, sdt thành viên trong group!
        //Loại bỏ dòng khởi tạo = null;
        ArrayList<String> nameGroup = new ArrayList<>();
        try {
            ResultSet rs = sa.executeQuery("select distinct groupsName from ChatGroup2 where userNumSent = " + data[2] + ";");
            while (rs.next()) {
                nameGroup.add(rs.getString(1));
                sentClientt.println("group:loadGroup:" + rs.getString(1));
                sentClientt.flush();
            }
            for (int i = 0; i < nameGroup.size(); i++) {
                ResultSet rss = sa.executeQuery("select groupsName,memberGroup from ChatGroup2 where userNumSent = " + data[2] + " and userNumReceive IS NULL and groupsName = N'" + nameGroup.get(i) + "';");
                while (rss.next()) {
                    sentClientt.println("group:loadGroup:" + rss.getString(1) + ":" + rss.getString(2) + ":1");
                    sentClientt.flush();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sms(String[] data,Statement sa, HashMap idClient,JTextArea jTAlog) {
        //Gửi tin nhắn nếu Client đang online
        jTAlog.append("Client    :" + data[3] + " Sent to Group: " + data[6] + "\n");
        //Dòng để để đếm số dòng là bạn bè!
        //Nhận vào danh ArrayList
        //"group:deletegrGroup:"+nameGroup_+":"+phoneNumberOfUser_+":"+memberUserNum
        //Để kiểm tra coi ai là bạn và cập nhật lại danh sách bạn có bao nhiêu người
        //cắt chuỗi ra
        //group:chatGroup:sms:01639997155:[1639997155,1639997156,1639997154]:Pla Pla Pla:Phân Tihcs
        //group:chatGroup:sms:01639997155:Pla Pla Pla:Phân Tihcs
        String[] tamp;
        String tamp2 = data[4];
        String tamp3 = tamp2.substring(1, tamp2.length() - 1);
        tamp = tamp3.split(",");
        for (int i = 0; i < tamp.length; i++) {
            int userNum = Integer.parseInt(tamp[i].trim());
            //Nếu nó online thì sao, còn không online thì sao
            //nếu no online và nó khác số địn thoại chính thì gửi cho nó và lưu vào CSDL
            //Còn nếu nó không online thì lưu vào CSDL với tên người gửi và tin nhắn
            if (idClient.containsKey(Integer.parseInt(tamp[i].trim())) && userNum != Integer.parseInt(data[3])) {
                try {
                    int max = 0;
                    ResultSet rs = sa.executeQuery("Select MAX(stt) from ChatGroup2 where userNumSent = " + data[3] + " and userNumReceive IS NOT NULL");
                    while (rs.next()) {
                        max = rs.getInt(1);
                    }
                    max++;
                    //phải inser từng dòng
                    sa.execute("insert into ChatGroup2 values(" + data[3] + "," + tamp[i].trim() + ",N'" + data[5] + "',N'" + data[6] + "',null," + max + ");");
                } catch (SQLException e) {
                    jTAlog.append("System    :Lỗi khi lưu tin nhắn vào database!");
                }
                PrintWriter writer2 = new PrintWriter((Writer) idClient.get(Integer.parseInt(tamp[i].trim())));
                writer2.println("group:chatGroup:sms:" + data[3] + ":" + data[5] + ":" + data[6]);
                writer2.flush();
            } else {
                ////Còn nếu nó không online thì lưu vào CSDL với tên người gửi và tin nhắn
                if (userNum != Integer.parseInt(data[3])) {
                    try {
                        int max = 0;
                        ResultSet rs = sa.executeQuery("Select MAX(stt) from ChatGroup2 where userNumSent = " + data[3] + " and userNumReceive IS NOT NULL");
                        while (rs.next()) {
                            max = rs.getInt(1);
                        }
                        max++;
                        //phải inser từng dòng
                        sa.execute("insert into ChatGroup2 values(" + data[3] + "," + tamp[i].trim() + ",N'" + data[5] + "',N'" + data[6] + "',null," + max + ");");
                    } catch (SQLException e) {
                        jTAlog.append("System    :Lỗi khi lưu tin nhắn vào database!");
                    }
                }
            }
        }
    }
    
    public static void loadSms(String[] data,Statement sa, HashMap idClient,JTextArea jTAlog) {
        try {
            ResultSet rss = sa.executeQuery("select TOP 20 userNumSent,userNumReceive,textChat,stt\n"
                    + "from \n"
                    + "(select TOP 20 userNumSent,userNumReceive,textChat,stt \n"
                    + "	from ChatGroup2 where userNumReceive is not null and  groupsName = N'" + data[5] + "' ORDER BY stt DESC)  A ORDER BY stt ASC");
            while (rss.next()) {
                PrintWriter writers = new PrintWriter((Writer) idClient.get(Integer.parseInt(data[4])));
                writers.println("group:chatGroup:sms:loadSms:" + rss.getString(1) + ":" + rss.getString(2) + ":" + rss.getString(3));
                writers.flush();
            }
        } catch (NumberFormatException | SQLException e) {
            jTAlog.append("System    : Lỗi " + e.getMessage() + " khi gửi tin nhắn offline");
        }
    }
}
