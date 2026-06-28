## Use-case name: Đăng ký tài khoản

- Actor: Khách hàng
- Description: Khách hàng đăng ký tài khoản.
- Trigger: Khách hàng muốn đăng ký tài khoản.
- Preconditions: Khách hàng chưa có tài khoản, email đăng ký tài khoản chưa được dùng để đăng ký.  
- Postconditions: Khách hàng đăng ký tài khoản thành công
- Normal folow
  1. Khách hàng bấm vào nút “Đăng ký” 
  2. Chuyển đến trang đăng ký tài khoản.
  3. Khách hàng dùng điền đầy đủ thông tin: tên đầy đủ ,email, số điện thoại, mật khẩu, lặp lại mật khẩu.
  4. Khách hàng bấm nút “đăng ký”.
  5. Chuyển đến trang đăng nhập, và thông báo thành công.
- Exception flow:
  - Case 1: 
      3a. Khách hàng bỏ sót các trường thông tin hoặc lặp lại mật khẩu không chính xác.
      4. Khách hàng bấm nút “đăng ký”.
      5a. Hệ thống thông báo lỗi và yêu cầu khách hàng nhập lại.
  - Case 2:
      5b. Nếu địa chỉ email hoặc số điện thoại đã được dùng để đăng ký tài khoản khác. Hệ thống sẽ thông báo “Email hoặc số điện thoại đã được sử dụng”. Yêu cầu khách hàng thay đổi.


