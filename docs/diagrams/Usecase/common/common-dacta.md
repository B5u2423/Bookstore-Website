## Use-case name: Đăng nhập

- Actor: Người dùng hệ thống (KH có tài khoản, Quản lý, Nhân viên)
- Description: Người dùng muốn thực thi các chức năng của hệ thống cấn đăng nhập
- Trigger: Người dùng đăng nhập hệ thống
- Preconditions: Người dùng muốn đăng nhập hệ thống. Người dùng đã có tài khoản và chưa đăng nhập.
- Postconditions: Người dùng đăng nhập thành công.
- Normal follow
  1. Người dùng bấm vào nút đăng nhập.
  2. Truy cập vào trang đăng nhập.
  3. Điền các thông tin đăng nhập : email, mật
khẩu.
  4. Bấm “Đăng nhập”.
  5. Chuyển về trang chủ của giao diện nếu tài khoản là khách hàng/nhân viên, chuyển về trang quản trị nếu tài khoản là quản lý.
- Exception flow:
  5a. Nếu email hay mật khẩu không chính xác, hệ thống sẽ hiển thị thông báo “Email hoặc mật khẩu không chính xác”.

## Use-case name: Thay đổi thông tin

- Actor: Người dùng hệ thống (KH có tài khoản, Quản lý, Nhân viên)
- Description: Người dùng thay đổi thông tin
cá nhân.
- Trigger: Người dùng muốn thay đổi thông tin cá nhân.
- Precondition: Người dùng đã đăng nhập thành công.
- Postcondition: Thay đổi thông tin cá nhân thành công.
- Normal flow:
  1. Tại trang chủ của giao diện tài khoản, bấm vào tên người dùng trên thanh menu
  2. Hiển thị bảng menu.
  3. Bấm vào “Tài khoản”.
  4. Chuyển trang đến trang thông tin cá nhân.
  5. Bấm vào biểu tượng chỉnh sửa bên cạnh các trường thông tin muốn sửa đổi.
  6. Nhập nội dung muốn chỉnh sửa vào trường input tương ứng.
  7. Bấm “Lưu”.
  8. Pop-up thông báo thành công xuất hiện.
- Exception flow
  8a. Nếu người dùng để trống bất kỳ trường thông tin nào. Hệ thống sẽ thông báo yêu cầu điền đầy đủ.

## Use-case name: Thay đổi mật khẩu

- Actor: Người dùng hệ thống (KH có tài khoản, Quản lý, Nhân viên)
- Description: Người dùng thay đổi mật khẩu.
- Trigger: Người dùng muốn thay đổi mật khẩu.
- Precondition: người dùng đăng nhập thành công.
- Postcondition: Thay đổi mật khẩu thành công.
- Normal flow: 
  1. Tại trang chủ của giao diện tài khoản, bấm vào tên người dùng trên thanh menu
  2. Hiển thị bảng menu.
  3. Bấm vào “tài khoản”.
  4. Chuyển trang đến trang thông tin cá nhân.
  5. Bấm nút “Đổi mật khẩu” trên thanh menu.
  6. Chuyển đến trang đổi mật khẩu.
  7. Nhập mật khẩu hiện tại, mật khẩu mới và lặp lại mật khẩu mới.
  8. Bấm “Cập nhật”.
  9. Pop-up thông báo thành công xuất hiện.
- Exception flow:
  8a. Nếu điền sai mật khẩu hiện tại, hoặc lặp lại mật khẩu không chính xác. Sẽ thông báo lỗi yêu cầu nhập lại.