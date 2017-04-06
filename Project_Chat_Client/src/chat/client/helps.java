
//Phần xử lí ký tự client
/*
Chat: gửi chat
Connect: gửi thông báo kết nối
Disconnect: gửi thông báo ngắt kết nối
Friend: gửi yêu cầu trả về danh sách bạn
Regis:Hoten:sdt:mk gửi yêu cầu đăng ký tài khoản
ChatLoad:Load tn offline
repLogin: Kiểm tra đăng nhập
AddFriend : Yêu cầu gửi thông tin kết bạn
RequestAddFriends : Trả lời yêu cầu kết bạn online
DeleteFriends: Yêu cầu xóa bạn
info : yêu cầu trả về thông tin user
allUser : Trả vê danh sách các người dùng sử dụng hệ thống để kết bạn!

ListCreateGroup: Yêu cầu trả về danh sách bạn để load lên panelCreateGroupLog
*/
//Phần xử lí ký tự Server gửi client
/*
Chat: gửi chat
Connect: gửi thông báo kết nối
Disconnect: gửi thông báo ngắt kết nối
Friend: gửi yêu cầu trả về danh sách bạn
repRegis:OK:sdt:mk gửi yêu cầu đăng ký tài khoản
ChatLoad:Load tn offline
repLogin: Kiểm tra đăng nhập
AddFriend : Yêu cầu gửi thông tin kết bạn
RequestAddFriends : Trả lời yêu cầu kết bạn online
DeleteFriends: Yêu cầu xóa bạn
info : yêu cầu trả về thông tin user

ListCreateGroup: Yêu cầu trả về danh sách bạn để load lên panelCreateGroupLog
*/
/*Phần lỗi cần xử lí*/
/*
- Giao diện chat:
    +Xử lí, cài icon các sự kiện!
    +Xử lí các sticker
    +Xử lí menuItem
    +Xử lí Mục Tin nhắn
    +Xử lí Mục Danh bạ
    +Xử lí mục Nhóm
    +Xử Lí muc Thêm
        + Thêm bạn
        + Thêm Nhóm
    + Xử lí Gửi file
    + Xử lí nhận File
    + Xử lí nút nhận ảnh
    + Xử lí nút gửi ảnh!
    +Xử lí nút Gửi
- Kiểm tra tổng thể
*/