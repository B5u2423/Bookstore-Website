INSERT INTO users (first_name, last_name, email, password, user_type, create_ts, update_ts) VALUES
    ('admin1', 'admin1', 'admin1@company.com', '$2a$10$hf8t9CoXS5lALVDBB/.44.gZTIOFFdFiqoDduW2.8jQJpSwB8LsYS', 'ADMIN', now(), now()),
    ('admin2', 'admin2', 'admin2@company.com', '$2a$10$KrGRL5FYyNV7GKhRR25hc.8g1jEvmD/U2JurEK677q5Q3QAs/7RiO', 'ADMIN', now(), now()),
    ('admin3', 'admin3', 'admin3@company.com', '$2a$10$YQLzmNr3vzHRn/3D2S0D9Or.0yvUTbGIIMDcmzDzZu418hsYBbwUS', 'ADMIN', now(), now());

INSERT INTO books (
    create_ts,
    update_ts,
    isbn,
    title,
    description,
    author,
    price,
    in_stock,
    url_slug,
    publisher,
    publish_year,
    page_count,
    image_url
) VALUES
      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01001-1', 'Truyện Kiều', 'Tác phẩm kinh điển của văn học Việt Nam, kể về câu chuyện của Thúy Kiều, một người phụ nữ tài sắc vẹn toàn đã phải hy sinh bản thân để cứu gia đình', 'Nguyễn Du', 150000, 100, 'truyen-kieu', 'NXB Văn Học', 1820, 248, 'https://cdn1.fahasa.com/media/catalog/product/8/9/8935230001195.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01002-2', 'Dế Mèn Phiêu Lưu Ký', 'Câu chuyện về hành trình phiêu lưu của chú Dế Mèn, một tác phẩm văn học thiếu nhi nổi tiếng dạy về tình bạn và các bài học cuộc sống', 'Tô Hoài', 120000, 150, 'de-men-phieu-luu-ky', 'NXB Kim Đồng', 1941, 200, 'https://cdn1.fahasa.com/media/catalog/product/d/e/de-men-50k_1.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01003-3', 'Số Đỏ', 'Tiểu thuyết châm biếm sâu sắc về xã hội Việt Nam thời Pháp thuộc, với nhân vật chính là Xuân tóc đỏ', 'Vũ Trọng Phụng', 130000, 80, 'so-do', 'NXB Văn Học', 1936, 280, 'https://cdn1.fahasa.com/media/catalog/product/8/9/8935235244597.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01004-4', 'Chí Phèo', 'Tác phẩm xuất sắc về một người nông dân nghèo khổ bị tha hóa và bi kịch của anh trong xã hội phong kiến', 'Nam Cao', 100000, 120, 'chi-pheo', 'NXB Văn Học', 1941, 156, 'https://cdn1.fahasa.com/media/catalog/product/i/m/image_193731.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01005-5', 'Tắt Đèn', 'Câu chuyện cảm động về cuộc sống khổ cực của người nông dân Việt Nam dưới ách thống trị của thực dân Pháp', 'Ngô Tất Tố', 110000, 90, 'tat-den', 'NXB Văn Học', 1939, 192, 'https://cdn1.fahasa.com/media/flashmagazine/images/page_images/tat_den/2024_06_10_11_08_25_1-390x510.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01006-6', 'Những Ngã Tư Và Những Cột Đèn', 'Tập thơ nổi tiếng viết về cuộc sống đời thường và những khoảnh khắc đẹp của tuổi thơ', 'Trần Đăng Khoa', 95000, 70, 'nhung-nga-tu-va-nhung-cot-den', 'NXB Kim Đồng', 1968, 168, 'https://cdn1.fahasa.com/media/catalog/product/n/h/nhung-nga-tu-va-nhung-cot-den-01.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01007-7', 'Lão Hạc', 'Truyện ngắn cảm động về tình phụ tử và sự hy sinh cao cả của một người cha nghèo khổ', 'Nam Cao', 85000, 100, 'lao-hac', 'NXB Văn Học', 1943, 124, 'https://cdn1.fahasa.com/media/catalog/product/8/9/8935236427838.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01008-8', 'Vang Bóng Một Thời', 'Tập truyện ngắn đặc sắc về những nét đẹp văn hóa truyền thống Việt Nam', 'Nguyễn Tuân', 140000, 85, 'vang-bong-mot-thoi', 'NXB Văn Học', 1940, 224, 'https://cdn1.fahasa.com/media/catalog/product/i/m/image_231237.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01009-9', 'Nhật Ký Trong Tù', 'Tập thơ chữ Hán được sáng tác trong thời gian bị giam cầm, thể hiện tinh thần lạc quan cách mạng', 'Hồ Chí Minh', 125000, 200, 'nhat-ky-trong-tu', 'NXB Chính Trị Quốc Gia', 1943, 132, 'https://cdn1.fahasa.com/media/catalog/product/8/9/8936067606294.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01010-0', 'Chiến Thắng Điện Biên Phủ', 'Hồi ký lịch sử về chiến thắng Điện Biên Phủ lừng lẫy của quân đội nhân dân Việt Nam', 'Võ Nguyên Giáp', 180000, 150, 'chien-thang-dien-bien-phu', 'NXB Quân Đội Nhân Dân', 1964, 312, 'https://cdn1.fahasa.com/media/catalog/product/k/i/ki-niem-70-nam-chien-thang-dbp_70-cau-hoi-dap-ve-chien-thang-dien-bien-phu_bia.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01011-1', 'Sống Mãi Với Thủ Đô', 'Tiểu thuyết lịch sử về Hà Nội trong thời kỳ kháng chiến chống Pháp', 'Nguyễn Huy Tưởng', 135000, 75, 'song-mai-voi-thu-do', 'NXB Văn Học', 1960, 264, 'https://cdn1.fahasa.com/media/catalog/product/s/o/song-mai-voi-thu-do_bia_tb-2024.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01012-2', 'Tuổi Thơ Dữ Dội', 'Câu chuyện về tuổi thơ trong những năm tháng chiến tranh, đầy ắp những kỷ niệm và tình bạn đẹp', 'Phùng Quán', 120000, 110, 'tuoi-tho-du-doi', 'NXB Kim Đồng', 1988, 248, 'https://cdn1.fahasa.com/media/catalog/product/i/m/image_187162.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01013-3', 'Giọt Nắng Bên Thềm', 'Câu chuyện trong sáng về tình yêu học trò và những kỷ niệm đẹp của tuổi học trò', 'Nguyễn Nhật Ánh', 95000, 200, 'giot-nang-ben-them', 'NXB Trẻ', 2000, 208, 'https://cdn1.fahasa.com/media/catalog/product/c/h/chu-meo-tam-nang-ben-hien_bia.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01014-4', 'Mắt Biếc', 'Câu chuyện tình yêu đầy cảm động về Ngạn và người bạn thanh mai trúc mã Hà Lan', 'Nguyễn Nhật Ánh', 105000, 250, 'mat-biec', 'NXB Trẻ', 1990, 236, 'https://cdn1.fahasa.com/media/catalog/product/m/a/mat-biec_bia-mem_in-lan-thu-44.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01015-5', 'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 'Câu chuyện đẹp về tuổi thơ ở một miền quê Việt Nam, với những kỷ niệm hồn nhiên, trong sáng', 'Nguyễn Nhật Ánh', 110000, 300, 'toi-thay-hoa-vang-tren-co-xanh', 'NXB Trẻ', 2010, 244, 'https://cdn1.fahasa.com/media/catalog/product/8/9/8934974211006.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01016-6', 'Bên Kia Sông Là Nước Mỹ', 'Tiểu thuyết kể về cuộc sống của những người Việt Nam tị nạn và hành trình đến Mỹ', 'Nguyễn Ngọc Ngạn', 160000, 80, 'ben-kia-song-la-nuoc-my', 'NXB Văn Nghệ', 1999, 328, 'https://cdn1.fahasa.com/media/catalog/product/e/u/eureka-america-o-kia-nuoc-my.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01017-7', 'Thép Đã Tôi Thế Đấy', 'Tiểu thuyết được dịch về tinh thần cách mạng và sự kiên cường của con người trước nghịch cảnh', 'Nikolai Ostrovsky', 145000, 95, 'thep-da-toi-the-day', 'NXB Văn Học', 1934, 384, 'https://cdn1.fahasa.com/media/flashmagazine/images/page_images/thep_da_toi_the_day/2024_11_12_14_21_41_1-390x510.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01018-8', 'Không Gia Đình', 'Câu chuyện cảm động về cậu bé Rémi mồ côi và hành trình đi tìm hạnh phúc của mình', 'Hector Malot', 170000, 120, 'khong-gia-dinh', 'NXB Kim Đồng', 1878, 456, 'https://cdn1.fahasa.com/media/catalog/product/8/9/8935230009887.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01019-9', 'Đất Rừng Phương Nam', 'Câu chuyện phiêu lưu của cậu bé An trong những năm kháng chiến ở miền Nam Việt Nam', 'Đoàn Giỏi', 125000, 160, 'dat-rung-phuong-nam', 'NXB Kim Đồng', 1957, 288, 'https://cdn1.fahasa.com/media/catalog/product/d/a/dat-rung-phuong-nam.jpg'),

      (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '978-604-1-01020-0', 'Hồn Trương Ba, Da Hàng Thịt', 'Vở kịch nổi tiếng về số phận con người và những day dứt về bản thể, thân phận con người', 'Lưu Quang Vũ', 90000, 70, 'hon-truong-ba-da-hang-thit', 'NXB Sân Khấu', 1981, 148, 'https://cdn1.fahasa.com/media/catalog/product/i/m/image_232595.jpg');

INSERT INTO categories (category_name, create_ts, update_ts) VALUES
    ('Văn học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Kinh tế', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Tâm lý - kỹ năng sống', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Nuôi dạy con', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Tiểu sử - hồi ký', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Sách thiếu nhi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Sách học ngoại ngữ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);