/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.server;

import chat.Group.*;
import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;
import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

public class chat_Server extends javax.swing.JFrame {

    //Biến lưu trữ đối tượng Server + Client
    public ServerSocket client_;
    public Socket clientSocket_;

    //Biến lưu trữ đối tượng kết nối database
    public Statement s;
    public Connection con;

    //Biến lưu trữ số lương client đang kết nối đến Server 
    HashMap<Integer, PrintWriter> idClient;

    //Khoi tao doi luong luong server
    Thread starter; // Luồng Server Chính!

    //Biến TableModel dùng để load dữ liệu lên bảng table
    private DefaultTableModel tableMode = new DefaultTableModel();
    //Mảng lưu trữ số lượng id đang kết nối đến Server
    ArrayList<String> countIdCLientConnection;
    int countID = 0;

    //Biến dùng để gửi thông tin khi người đó không online
    HashMap<Integer, String> allClientOnServer;

    /*test*/
    ArrayList<String> danhsachban;
    int i = 0;

    startServer m;
    boolean isServerOpened = false;

    public chat_Server() {
        setTitle("Server CTU Talks");
        setBackground(Color.WHITE);
        initComponents();
        // Tạo thư mục trong đường dẫn
        String url = "C:\\Users\\Visual-Studio\\Documents\\CTU Talks";
        File directory = new File(url);
        directory.mkdir();
        //Tạo thư mục image trong CTU Talks
        File directory2 = new File(directory, "image");
        directory2.mkdir();
        //Tạo thư mục file trong CTU Talks
        File directory3 = new File(directory, "file");
        directory3.mkdir();
        setLocationRelativeTo(this);
        allClientOnServer = new HashMap<>();
    }

    /*Khởi tạo một luồng thực thi startServer*/
    public class startServer implements Runnable {

        @Override
        public void run() {
            /*Khởi tạo danh sách chứa các dòng gửi data từ Server cho Client (chưa có luồng gửi)!*/
            idClient = new HashMap<>();

            /*Khởi tạo danh sách chưa các User trên hệ thống 
            /*Khởi tạo danh sách id đang online trên hệ thống*/
            countIdCLientConnection = new ArrayList<>();
            /*Khởi tạo danh sách chứa các dòng id + name Runable Client (Dung de quan li luong!)!*/
            //idRunable = new HashMap<>();

            try {
//                InetAddress add = InetAddress.getByName("11.10.11.37");
//                client_ = new ServerSocket(9999,50,add);
                //Khoi tao Server voi port 9999
                client_ = new ServerSocket(9999);
                jTAlog.append("Server đang chạy...\n");
                while (!Thread.currentThread().isInterrupted()) {
                    //Chờ đợi client chấp nhận
                    clientSocket_ = client_.accept();
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    Statement sa = s;
                    jTAlog.append("Client    :" + clientSocket_.toString() + " đã connect!\n");
                    jTAlog.append("System    :Đã hoàn thành bước khởi tạo!\n");
                    jTAlog.append("System    :Bắt đầu tạo luồng cho Client!\n");
                    //Khởi tạo luồng dùng để đọc tin nhắn từ Client!
                    Thread ListennerOnly = new Thread(new xuliClientOnly(clientSocket_, sa));
                    ListennerOnly.start();
                    //idRunable.put(ListennerOnly.getId(),ListennerOnly.getName());
                }
            } catch (IOException e) {
                jTAlog.append("Không thể tạo Server! Lỗi class ServerStart!");
            }
        }

    }

    //Class xử lí yêu cầu từ Client!
    public class xuliClientOnly implements Runnable, Chat_Server_Interface {

        //Biến lưu trữ Kết nối Client + gửi  + nhận sms
        Socket clientSockett;
        BufferedReader fromClientt;
        PrintWriter sentClientt;
        Statement sa;
        //Mảng Lưu trữ đoạn sms
        String[] data;
        //Biến Lưu trữ tên Luồng Client + Id Client
        int idRunable = 0;
        String nameRunble;

        //Phương thức khởi tạo
        public xuliClientOnly(Socket a, Statement sa) {
            clientSockett = a;
            this.sa = sa;
            try {
                //Khởi tạo để gửi data.
                fromClientt = new BufferedReader(new InputStreamReader(clientSockett.getInputStream()));
                sentClientt = new PrintWriter(clientSockett.getOutputStream());
            } catch (IOException e) {
            }
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = fromClientt.readLine()) != null) {
                    data = message.split(":");
                    switch (data[0]) {
                        case "info":
                            information(Integer.parseInt(data[1]));
                            break;
                        //Regis:sdt:mk gửi yêu cầu đăng ký tài khoản
                        case "Regis":
                            reGis();
                            break;
                        case "Chat":
                            //@@@@ //cách nhận diện sms zs Sticker và Emoji
                            if (data.length == 4) {
                                //Gửi tin nhắn sms
                                sentMessageClientOnly(Integer.parseInt(data[1]), Integer.parseInt(data[2]), data[3], "");
                            } else {
                                //Load Sticker or Emoji
                                sentMessageClientOnly(Integer.parseInt(data[1]), Integer.parseInt(data[2]), data[3], data[4]);
                            }
                            break;
                        case "ChatGroup":
                            if (data.length == 5) {
                                //Gửi tin nhắn sms
                                sentMessageGroup(data);
                            } else {
                                //Load Sticker or Emoji
////                                sentMessageGroup(data);
                                System.out.println("gửi sticker");
                            }

                            break;
                        case "Connect":
                            connection(data[1], data[2]);
                            break;
                        case "Disconnect":
                            diConnection();
                            break;
                        case "Friend":
                            sentListFriends(Integer.parseInt(data[1]));
                            break;
                        case "DeleteFriends":
                            //DeleteFriends:"+Name+":"+Num+":"+Pass);
                            deleteFriends(data[1], Integer.parseInt(data[2]), data[3]);
                            break;
                        case "ChatLoad":
                            sentListChatSms(data[1], data[2]);
                            break;
                        case "allUser":
                            //thông tin all User on Server
                            allClientOnServer.clear();
                            try {
                                ResultSet rs = s.executeQuery("Select userNum,userName from Users");
                                while (rs.next()) {
                                    allClientOnServer.put(rs.getInt(1), rs.getString(2));
                                }
                            } catch (SQLException e) {
                                JOptionPane.showConfirmDialog(rootPane, "Lỗi khi load danh sách offline");
                            }
                            sentClientt.println("allUser:" + allClientOnServer);
                            sentClientt.flush();
                            break;
                        case "AddFriend":
                            addFriends(data);
                            break;
                        case "loadAddFriends":
                            loadAddFriends(data);
                            break;
                        case "addGroupName":
                            addGroupName(data[1], data[2], data[3]);
                            break;
                        case "loadGroupName":
                            loadGroupName(data);
                            break;
                        case "ListCreateGroup":
                            listCreateGroup(data);
                            break;
                        case "group":
                            handingGroup(data);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                jTAlog.append("Loi chỗ luồng gửi dữ liệu");
            }
        }

        //Phương thức dùng để xử lí tất cả về thông tin đăng kí, kết nối, ngắt kết nối
        @Override
        public void information(Integer info) {
            String nameInfo = null;
            try {
                PrintWriter writer = new PrintWriter(idClient.get(info));
                //Đối tượng rs trả về một row có họ tên user
                ResultSet rs = s.executeQuery("select userName from Users where userNum = " + info);
                while (rs.next()) {
                    nameInfo = rs.getString(1);
                }
                writer.println("info:" + nameInfo);
                writer.flush();
            } catch (SQLException e) {
                JOptionPane.showConfirmDialog(rootPane, "Lỗi khi trả về information!");
            }
        }

        @Override
        public void connection(String userNum, String userPass) {
            String num = userNum;
            //String pass = userPass;
            //Phần kết nối
            idClient.put(Integer.parseInt(userNum), sentClientt);
            countIdCLientConnection.add(num);
            countID++;
            jTAlog.append("\n");
            jTAlog.append("Client    :" + userNum + " đã kết nối!\n");
            //Phần kiểm tra kết nối
            PrintWriter writer = new PrintWriter(idClient.get(Integer.parseInt(userNum)));
            int sdt = 0;
            String matkhau = null;
            try {
                //Đối tượng rs trả về một row có số địn thoại zs pass để kiểm tra
                ResultSet rs = s.executeQuery("select userNum,userPass from Users where userNum = " + userNum + " and userPass = '" + userPass + "'");
                while (rs.next()) {
                    sdt = rs.getInt(1);
                    matkhau = rs.getString(2);
                }
            } catch (SQLException e) {
                JOptionPane.showConfirmDialog(rootPane, "Lỗi khi connect or đăng kí tài khoản!");
            }
            //Xác thực số điện thoại và pass sau đó gửi kết quả
            if (Integer.parseInt(userNum) == sdt && userPass.equals(matkhau)) {
                tableMode = (DefaultTableModel) table.getModel();
                int rows = tableMode.getRowCount();
                for (int i = rows - 1; i >= 0; i--) {
                    tableMode.removeRow(i);
                }
                handingTable();
                labelStatutNum.setText(String.valueOf(idClient.size()));
                //repLogin:OK:sdt:mk gửi thông báo đăng nhập thành công
                writer.println("repLogin:OK");
                writer.flush();
            } else {
                tableMode = (DefaultTableModel) table.getModel();
                int rows = tableMode.getRowCount();
                for (int i = rows - 1; i >= 0; i--) {
                    tableMode.removeRow(i);
                }
                handingTable();
                countIdCLientConnection.remove(num);
                idClient.remove(Integer.parseInt(userNum));
                labelStatutNum.setText(String.valueOf(idClient.size()));
                jTAlog.append("Client    :" + userNum + " tài khoản không tồn tại!\n");
                //repLogin:OK:sdt:mk gửi thông báo đăng nhập không thành công!
                writer.println("repLogin:NO");
                writer.flush();
            }
        }

        @Override
        public void diConnection() {
            jTAlog.append("Client :" + data[1] + " đã ngắt kết nối!\n");
            idClient.remove(Integer.parseInt(data[1]));
            countIdCLientConnection.remove(data[1]);
            labelStatutNum.setText(String.valueOf(idClient.size()));
            tableMode = (DefaultTableModel) table.getModel();
            int rows = tableMode.getRowCount();
            for (int i = rows - 1; i >= 0; i--) {
                tableMode.removeRow(i);
            }
            handingTable();
        }

        @Override
        public void reGis() {
            jTAlog.append(data[0] + ":" + data[1] + ":" + data[2] + ":" + data[3] + " Regis\n");
            int sdt = 0;
            try {
                ResultSet rs = sa.executeQuery("Select userNum from Users where userNum = " + data[2]);
                while (rs.next()) {
                    sdt = rs.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
            if (Integer.parseInt(data[2]) != sdt) {
                try {
                    sa.execute("insert into Users values (N'" + data[1] + "'," + data[2] + ",'" + data[3] + "');");
                } catch (SQLException e) {
                    jTAlog.append("System    :" + data[1] + " lỗi khi đăng kí  \n");
                }
                //repRegis:OK:sdt:mk gửi yêu cầu đăng ký tài khoản
                sentClientt.println("repRegis:OK:" + data[2] + ":" + data[3]);
                sentClientt.flush();
                //thông tin all User on Server
                allClientOnServer.clear();
                try {
                    ResultSet rs = s.executeQuery("Select userNum,userName from Users");
                    while (rs.next()) {
                        allClientOnServer.put(rs.getInt(1), rs.getString(2));
                    }
                } catch (SQLException e) {
                    JOptionPane.showConfirmDialog(rootPane, "Lỗi khi load danh sách offline");
                }
                sentClientt.println("allUser:" + allClientOnServer);
                sentClientt.flush();
            } else {
                System.out.println("repRegis:NO:" + data[2] + ":" + data[3]);
                sentClientt.println("repRegis:NO:" + data[2] + ":" + data[3]);
                sentClientt.flush();
                jTAlog.append("System    :" + data[1] + " đăng kí thất bại  \n");
            }
        }

        //Phương thức dùng để gửi chat or emoji or Sticker
        @Override
        public void sentMessageClientOnly(Integer x, Integer y, String message, String Sticker) {
            Integer ix = x;
            Integer iy = y;
            int i = 0;
            //Gửi tin nhắn nếu Client đang online
            if (idClient.containsKey(iy)) {
                jTAlog.append("Client    :" + ix + " Sent to: " + iy + "\n");
                try {
                    ResultSet rs = sa.executeQuery("Select MAX(stt) from ChatOnly");
                    while (rs.next()) {
                        i = rs.getInt(1);
                    }
                    i++;
                    sa.execute("insert into ChatOnly values (" + ix + "," + iy + ",'" + message + ":" + Sticker + "'," + i + ")");
                } catch (SQLException e) {
                    jTAlog.append("System    :Lỗi khi lưu tin nhắn vào database!");
                }
                //Lưu trữ 2 mã điện thoại
                ArrayList<PrintWriter> idOnly = new ArrayList<>();
                //lấy về đối lượng PrintWriter trong HashMap thêm vào mảng
                idOnly.add(0, idClient.get(x));
                idOnly.add(1, idClient.get(y));
                PrintWriter writer = new PrintWriter(idOnly.get(1));
                writer.println("Chat:" + ix + ":" + iy + ":" + message + ":" + Sticker);
                writer.flush();
            } else //Gửi tin nhắn nếu CLient offlient
            {
                jTAlog.append("Client    :" + ix + " Sent to: " + iy + " offline \n");
                try {
                    ResultSet rs = sa.executeQuery("Select MAX(stt) from ChatOnly");
                    while (rs.next()) {
                        i = rs.getInt(1);
                    }
                    i++;
                    sa.execute("insert into ChatOnly values (" + ix + "," + iy + ",'" + message + ":" + Sticker + "'," + i + ")");
                } catch (SQLException e) {
                    jTAlog.append("System    :Lỗi khi lưu tin nhắn vào database!");
                }
            }

        }

        @Override
        public void sentMessageGroup(String[] data) {
            //"ChatGroup:" + sdt + ":" + userNumMemGroup + ":" + jTextFieldchat.getText() + ":" + txtStatutHoTen.getText().trim());
            //ChatGroup:01639997154:[1639997155, 1639997156]:ádfsdf:TestNhoms
            int i, j;
            ArrayList<PrintWriter> userNumMemGroup = new ArrayList<>();
            String[] tamp;
            String tamp2 = data[2];
            String tamp3 = tamp2.substring(1, tamp2.length() - 1);
            tamp = tamp3.split(",");
            System.out.println(idClient.size());
            for (i = 0; i < tamp.length; i++) {
                //Gửi tin nhắn cho những ai online
                if (idClient.containsKey(Integer.parseInt(tamp[i].trim()))) {
                    userNumMemGroup.add(idClient.get(Integer.parseInt(tamp[i].trim())));
                }
                //Gửi tin nhắn cho những ai offline
            }
            System.out.println(userNumMemGroup.size());
            System.out.println(userNumMemGroup);
//            for (j = 0 ; j < userNumMemGroup.size(); j++){
//                PrintWriter writer = new PrintWriter(userNumMemGroup.get(i));
//                Gửi lại cho mỗi client nằm trong nhóm một tin nhắn
//                ChatGroup:số điện thoại người gửi:tin nhắn:tên nhóm
//                writer.println("ChatGroup:"+data[1]+":"+data[2]+":"+data[3]+":"+data[4]);
//                writer.flush();
//            }
            //Gửi tin nhắn sms 
            //sentServer_.println("ChatGroup:"+sdt+":"+userNumMemGroup+":
            //"+jTextFieldchat.getText()+":"+txtStatutHoTen.getText().trim());
            //gửi cho nhiều người
//                                for (int j = 1 ; j < userNumMemGroup.size(); j++){
//                                    
//                                }
            //Gửi tin nhắn nếu Client đang online
//                                if(idClient.containsKey(iy)){
//                                    jTAlog.append("Client    :"+ix+" Sent to: "+iy+"\n");
//                                    try {
//                                        sa.execute("insert into ChatOnly values ("+ix+","+iy+",'"+message+":"+Sticker+"')");
//                                    } catch (SQLException e) {
//                                        jTAlog.append("System    :Lỗi khi lưu tin nhắn vào database!");
//                                    }         
//                                    //Lưu trữ 2 mã điện thoại
//                                    ArrayList<PrintWriter> idOnly = new ArrayList<>();
//                                    //lấy về đối lượng PrintWriter trong HashMap thêm vào mảng
//                                    idOnly.add(0,idClient.get(x));
//                                    idOnly.add(1,idClient.get(y));
//                                    PrintWriter writer = new PrintWriter(idOnly.get(1));
//                                    writer.println("Chat:"+ix+":"+iy+":"+message+":"+Sticker);
//                                    writer.flush();
//                                }else
//                                    //Gửi tin nhắn nếu CLient offlient
//                                {
//                                    jTAlog.append("Client    :"+ix+" Sent to: "+iy+" offline \n");
//                                    try {
//                                        sa.execute("insert into ChatOnly values ("+ix+","+iy+",'"+message+":"+Sticker+"')");
//                                    } catch (SQLException e) {
//                                        jTAlog.append("System    :Lỗi khi lưu tin nhắn vào database!");
//                                    }         
//                                }
//                            }else{
//                                //Load Sticker or Emoji
//                                sentMessageClientOnly(Integer.parseInt(data[1]),Integer.parseInt(data[2]),data[3],data[4]);
//                            }
        }

        //Phương thức dùng để xử lí tất cả về bạn bè
        @Override
        public void sentListFriends(Integer idFriends) {
            try {
                //Dòng để để đếm số dòng là bạn bè!
                //Để kiểm tra coi ai là bạn và cập nhật lại danh sách bạn có bao nhiêu người
                ResultSet rs = sa.executeQuery("select count(*) from (Select userName,userNum,userPass from Users,(select chatFriendNum from Friends where userNum = " + idFriends + " and addFriends = 1) as A where userNum = A.chatFriendNum) as B");
                rs.next();
                if (rs.getInt(1) == 0) {
                    jTAlog.append("Test trường hợp không có bạn!" + idFriends + "\n");
                    //Không có bạn bè
                    //Nếu có yêu cầu trả về danh sách bạn mk không có bạn bè thì gửi
                    //yêu cầu xóa panel Danhban
                    //Nếu họ đang online
                    if (idClient.containsKey(idFriends)) {
                        PrintWriter writer2 = new PrintWriter(idClient.get(idFriends));
                        writer2.println("Friend:updateDanhBa");
                        writer2.flush();
                    }
                    //Nếu họ offline thì thôi không làm j cả
                } else {
                    jTAlog.append("Test trường hợp có bạn!" + idFriends + "\n");
                    //Có bạn bè
                    //Nếu họ đang online
                    //Chọn trong SQL ra và cập nhật lại bạn bè
                    if (idClient.containsKey(idFriends)) {
                        try {
                            ResultSet rss = sa.executeQuery("Select userName,userNum,userPass from Users,(select chatFriendNum from Friends where userNum = " + idFriends + " and addFriends = 1) as A where userNum = A.chatFriendNum");
                            while (rss.next()) {
                                PrintWriter writer2 = new PrintWriter(idClient.get(idFriends));
                                writer2.println("Friend:" + rss.getString(1) + ":" + rss.getInt(2) + ":" + rss.getString(3));
                                writer2.flush();
                            }
                            jTAlog.append("System    : Đã send! danh sach ban cho " + idFriends + "\n");
                        } catch (SQLException e) {
                            jTAlog.append("Lỗi trả về danh sách bạn cho " + idFriends + "!" + e.getMessage() + "\n");
                        }
                    }
                    //Họ ofline thì không làm j cả

                }
            } catch (SQLException e) {
                jTAlog.append("Lỗi trả về danh sách bạn!" + e.getMessage() + "\n");
            }
        }

        @Override
        public void deleteFriends(String userName, Integer userNum, String userPass) {
            try {
                sa.execute("Delete from ChatGroup2 where userNumSent = " + userNum + " or userNumReceive = " + userNum);
                sa.execute("Delete from ChatOnly where userNumSent = " + userNum + " or userNumReceive = " + userNum);
                sa.execute("Delete from Friends where userNum = " + userNum + " or chatFriendNum = " + userNum);
                sentClientt.println("repDeleteFriends:OK");
                sentClientt.flush();
            } catch (SQLException e) {
                sentClientt.println("repDeleteFriends:NO");
                sentClientt.flush();
                JOptionPane.showConfirmDialog(rootPane, "Lỗi ràng buộc khi xóa bạn!");
            }
        }

        @Override
        public void sentListChatSms(String meOne, String meTow) {
            try {
                ResultSet rss = sa.executeQuery("select userNumSent,userNumReceive,textChat from (select TOP 15 userNumSent,userNumReceive,textChat,stt from ChatOnly where userNumSent = " + meOne + " or userNumSent = " + meTow + " ORDER BY stt DESC)A ORDER BY stt ASC");
                while (rss.next()) {
                    if (Integer.parseInt(meOne) == rss.getInt(1) || Integer.parseInt(meTow) == rss.getInt(1)) {
                        if (Integer.parseInt(meOne) == rss.getInt(2) || Integer.parseInt(meTow) == rss.getInt(2)) {
                            PrintWriter writers = new PrintWriter(idClient.get(Integer.parseInt(meOne)));
                            writers.println("ChatLoad:" + rss.getInt(1) + ":" + rss.getInt(2) + ":" + rss.getString(3));
                            writers.flush();
                        }
                    }
                }
                jTAlog.append("Đã send!\n");
            } catch (SQLException e) {
                jTAlog.append("Lỗi trả về TexT Chat offline !" + e.getMessage());
            }
        }

        @Override
        public void addFriends(String[] data) {
            //AddFriend:"+sdt+":"+userNum+":0")
            //01639997154:01639997155:0
            Integer ix = Integer.parseInt(data[1]);
            Integer iy = Integer.parseInt(data[2]);
            //Gửi yêu cầu kết bạn chưa biết kết quả đã được chấp nhận hay chưa!
            if (Integer.parseInt(data[3]) == 0) {
                try {
                    s.execute("insert into Friends values (" + data[1] + "," + data[2] + ",0)");
                } catch (SQLException e) {
                    jTAlog.append("System    :Lỗi khi gửi yêu cầu kết bạn offline!");
                }
                //onlient  && ofline
                //Gửi yêu cầu kết bạn nếu ng đó đang online
                if (idClient.containsKey(iy)) {
                    jTAlog.append("Client    :" + ix + " Request add friends: " + iy + "online \n");
                    //Lưu trữ 2 mã điện thoại
                    ArrayList<PrintWriter> idOnly = new ArrayList<>();
                    //lấy về đối lượng PrintWriter trong HashMap thêm vào mảng
                    idOnly.add(0, idClient.get(ix));
                    idOnly.add(1, idClient.get(iy));
                    PrintWriter writer = new PrintWriter(idOnly.get(1));
                    writer.println("AddFriend:" + iy + ":" + ix + ":0");
                    writer.flush();
                } else {
                    //Gửi tin nhắn nếu CLient offlient
                    jTAlog.append("Client    :" + ix + " Request add friends: " + iy + " offline \n");
                    //Không làm gì cả vì đã lưu yêu cầu ở trên rồi!
                }
            } else {
                //01639997155:01639997154:1
                //AddFriend:"+sdt+":"+userNum+":1
                //Cập nhật lại CSDL cho 2 bạn đã là bạn bè
                try {
                    s.execute("insert into Friends values (" + data[1] + "," + data[2] + ",1)");
                    s.execute("update Friends set addFriends = 1 where  userNum = " + data[2] + " and chatFriendNum = " + data[1]);
                } catch (SQLException e) {
                    jTAlog.append("System    :Lỗi khi thêm và update bạn mới!");
                }
                //Gửi tiếp yêu cầu cập nhật lại danh bạ chính chủ
                sentListFriends(Integer.parseInt(data[2]));
            }

        }

        @Override
        public void loadAddFriends(String[] data) {
            int id = Integer.parseInt(data[1]);
            try {
                ResultSet rs = sa.executeQuery("select * from Friends where chatFriendNum = " + data[1] + " and addFriends = 0;");
                while (rs.next()) {
                    PrintWriter writer2 = new PrintWriter(idClient.get(id));
                    writer2.println("AddFriend:" + rs.getInt(2) + ":" + rs.getInt(1) + ":0");
                    writer2.flush();
                }
                jTAlog.append("Đã send! danh sách yêu cầu kết bạn\n");
            } catch (SQLException e) {
                jTAlog.append("Lỗi trả về danh sách yêu cầu kết bạn!" + e.getMessage());
            }
        }

        //Phương thức dùng để xử lí tất cả về nhóm
        private void handingGroup(String[] data) {
            switch (data[1]) {
                case "createGroup":
                    break;
                case "deletegrGroup":
                    handGroupServer.deletegrGroup(data, sentClientt, sa, idClient, allClientOnServer);
                    break;
                case "notifiGroup":
                    break;
                case "loadGroup":
                    handGroupServer.loadGroup(data, sentClientt, sa);
                    break;
                case "chatGroup":
                    switch (data[2]) {
                        case "sms":
                            if (!"loadSms".equals(data[3])) {
                                handGroupServer.sms(data, sa, idClient, jTAlog);
                            } else {
                                handGroupServer.loadSms(data, sa, idClient, jTAlog);
                            }
                            break;
//                        case "emoij":
//                            if (!"loadSms".equals(data[3])) {
//                                handGroupServer.sms(data, sa, idClient, jTAlog);
//                            } else {
//                                handGroupServer.loadSms(data, sa, idClient, jTAlog);
//                            }
//                            break;
//                        case "sticker":
//                            if (!"loadSms".equals(data[3])) {
//                                handGroupServer.sms(data, sa, idClient, jTAlog);
//                            } else {
//                                handGroupServer.loadSms(data, sa, idClient, jTAlog);
//                            }
//                            break;
//                        case "picture":
//                            if (!"loadSms".equals(data[3])) {
//                                handGroupServer.sms(data, sa, idClient, jTAlog);
//                            } else {
//                                handGroupServer.loadSms(data, sa, idClient, jTAlog);
//                            }
//                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void addGroupName(String userNumSent, String groupsName, String memberGroup) {
            //sentServer_.println("addGroupName:" + sdt + ":" + nameGroupT + ":" + listAddUser);
            int j = 0;
            try {
                ResultSet r = sa.executeQuery("select MAX(stt) from ChatGroup2");
                while (r.next()) {
                    j = r.getInt(1);
                }
                j++;
                ResultSet rs = sa.executeQuery("select count(*) from ChatGroup2 where userNumSent = " + userNumSent + " and groupsName = N'" + groupsName + "'");
                rs.next();
                if (rs.getInt(1) == 0) {
                    //Tên Group chưa tồn tại - cho phép thêm
                    jTAlog.append("System    :" + userNumSent + " đã tạo nhóm " + groupsName + "\n");
                    sa.execute("insert into ChatGroup2 (userNumSent,groupsName,memberGroup,stt) values (" + userNumSent + ",N'" + groupsName + "','" + memberGroup + "'," + j + ");");
                    PrintWriter writer2 = new PrintWriter(idClient.get(Integer.parseInt(userNumSent)));
                    writer2.println("addGroupName:OK");
                    writer2.flush();
                } else {
                    //Tên Group tồn tại - không thêm  
                    jTAlog.append("System    :" + userNumSent + " : " + groupsName + "Tạo nhóm thất bại\n");
                    sentClientt.println("addGroupName:NO");
                    sentClientt.flush();
                }
            } catch (SQLException e) {
                jTAlog.append("System    : Lỗi khi xử lí thêm nhóm \n");
            }
        }

        @Override
        public void listCreateGroup(String[] data) {
            String idFriends = data[1];
            try {
                //Dòng để để đếm số dòng là bạn bè!
                //Để kiểm tra coi ai là bạn và cập nhật lại danh sách bạn có bao nhiêu người
                ResultSet rs = sa.executeQuery("select count(*) from (Select userName,userNum,userPass from Users,(select chatFriendNum from Friends where userNum = " + idFriends + " and addFriends = 1) as A where userNum = A.chatFriendNum) as B");
                rs.next();
                if (rs.getInt(1) == 0) {
                    //Không có bạn bè
                    //Nếu có yêu cầu trả về danh sách bạn mk không có bạn bè thì gửi
                    //yêu cầu xóa panelCreateGroup
                    sentClientt.println("ListCreateGroup:updateList");
                    sentClientt.flush();
                    jTAlog.append("ListCreateGroup:updateList:NoFriends!" + idFriends + "\n");
                } else {
                    jTAlog.append("ListCreateGroup:updateList:Friends!" + idFriends + "\n");
                    //Có bạn bè
                    //Chọn trong SQL ra và cập nhật lại bạn bè
                    try {
                        ResultSet rss = sa.executeQuery("Select userName,userNum,userPass from Users,(select chatFriendNum from Friends where userNum = " + idFriends + " and addFriends = 1) as A where userNum = A.chatFriendNum");
                        while (rss.next()) {
                            sentClientt.println("ListCreateGroup:" + rss.getString(1) + ":" + rss.getInt(2) + ":" + rss.getString(3));
                            sentClientt.flush();
                        }
                        jTAlog.append("System    : Đã send! danh sach Group cho" + idFriends + "\n");
                    } catch (SQLException e) {
                        jTAlog.append("Lỗi trả về danh sách bạn cho " + idFriends + "!" + e.getMessage() + "\n");
                    }
                }
            } catch (SQLException e) {
                jTAlog.append("Lỗi trả về danh sách bạn!" + e.getMessage() + "\n");
            }
        }

        @Override
        public void loadGroupName(String[] data) {
            //Load tên group, sdt thành viên trong group!
            //Loại bỏ dòng khởi tạo = null;
            ArrayList<String> nameGroup = new ArrayList<>();
            try {
                ResultSet rs = sa.executeQuery("select distinct groupsName from ChatGroup2 where userNumSent = " + data[1] + ";");
                while (rs.next()) {
                    nameGroup.add(rs.getString(1));
                    sentClientt.println("loadGroupName:" + rs.getString(1));
                    sentClientt.flush();
                }
                for (int i = 0; i < nameGroup.size(); i++) {
                    ResultSet rss = sa.executeQuery("select groupsName,memberGroup from ChatGroup2 where userNumSent = " + data[1] + " and userNumReceive IS NULL and groupsName = N'" + nameGroup.get(i) + "';");
                    while (rss.next()) {
                        sentClientt.println("loadGroupName:" + rss.getString(1) + ":" + rss.getString(2) + ":1");
                        sentClientt.flush();
                        System.out.println("loadGroupName:" + rss.getString(1) + ":" + rss.getString(2) + ":1");
                    }
                }
                ;
            } catch (SQLException e) {
            }
        }
    }

    //Phương thức dùng để hiện lên bảng Client đang online
    private void handingTable() {
        try {
            String[] colo = {"Họ Tên", "Số điện thoại"};
            tableMode.setColumnIdentifiers(colo);
            table.setModel(tableMode);
            for (int i = 0; i < countIdCLientConnection.size(); i++) {
                ResultSet rs = s.executeQuery("select userName,userNum from Users where userNum = " + countIdCLientConnection.get(i) + ";");
                while (rs.next()) {
                    Object[] row = new Object[2];
                    row[0] = rs.getString(1);
                    row[1] = rs.getInt(2);
                    tableMode.addRow(row);
                }
            }
        } catch (SQLException e) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTAlog = new javax.swing.JTextArea();
        jButtonConnected = new javax.swing.JButton();
        buttonDisConnection = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        labelStatutNum = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTAlog.setColumns(20);
        jTAlog.setRows(5);
        jScrollPane1.setViewportView(jTAlog);

        jButtonConnected.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonConnected.setText("Chạy Server");
        jButtonConnected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConnectedActionPerformed(evt);
            }
        });

        buttonDisConnection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buttonDisConnection.setText("Dừng Server");
        buttonDisConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDisConnectionActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Họ tên", "Số điện thoại"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        table.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Có:");

        labelStatutNum.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelStatutNum.setText("0");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("người đang kết nối.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelStatutNum)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jButtonConnected)
                        .addGap(27, 27, 27)
                        .addComponent(buttonDisConnection))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonConnected)
                            .addComponent(buttonDisConnection))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(labelStatutNum)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //xử lí sự kiện button Chạy Server
    private void jButtonConnectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConnectedActionPerformed
        //Khai báo một đối lượng connecttion of class Connection
        connect_SqlServer cnet = new connect_SqlServer();
        con = cnet.getConnection();
        s = cnet.getStatement();
        try {
            ResultSet rs = s.executeQuery("Select userNum,userName from Users");
            while (rs.next()) {
                allClientOnServer.put(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(rootPane, "Lỗi khi load danh sách offline");
        }
//        //Khoi tao Luong Server;
        m = new startServer();
        starter = new Thread(m);
        isServerOpened = true;
        starter.start();
    }//GEN-LAST:event_jButtonConnectedActionPerformed
    //Xử Lí sự kiện button Dừng ser ver
    private void buttonDisConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDisConnectionActionPerformed
        //Tắt Server          
        int output = JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc chắn muốn dừng Server!", "Nguy Hiểm !!", JOptionPane.YES_NO_OPTION);
        if (output == JOptionPane.YES_OPTION) {
            starter.interrupt();
            isServerOpened = false;
            System.out.println(starter.isInterrupted());
            jTAlog.append("Server đã dừng!\n");
        }
    }//GEN-LAST:event_buttonDisConnectionActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
        } catch (ParseException | UnsupportedLookAndFeelException e) {
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new chat_Server().setVisible(true);
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDisConnection;
    private javax.swing.JButton jButtonConnected;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTAlog;
    private javax.swing.JLabel labelStatutNum;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
