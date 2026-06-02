COPY users (name, email, password, user_type)
    FROM '/docker-entrypoint-initdb.d/seeds/users.csv'
    DELIMITER ','
    CSV HEADER;
COPY categories (category_name, category_slug, parent_id)
    FROM '/docker-entrypoint-initdb.d/seeds/categories.csv'
    DELIMITER ','
    CSV HEADER;

-- books
COPY books (isbn, title, description, author, price, in_stock, url_slug, publisher, publish_year, page_count, image_url, category_id)
    FROM '/docker-entrypoint-initdb.d/seeds/books.csv'
    DELIMITER ','
    CSV HEADER;

-- book: sach ngoai ngu
COPY books (image_url, title, price, category_id, isbn, description, author, in_stock, url_slug, publisher, publish_year, page_count)
    FROM '/docker-entrypoint-initdb.d/seeds/ngoaingu.csv'
    DELIMITER ','
    CSV HEADER;

-- book: sach hoi ky
COPY books (image_url, title, price, category_id, isbn, description, author, in_stock, url_slug, publisher, publish_year, page_count)
    FROM '/docker-entrypoint-initdb.d/seeds/hoiky.csv'
    DELIMITER ','
    CSV HEADER;

-- book: truyen thieu nhi
COPY books (image_url, title, price, category_id, isbn, description, author, in_stock, url_slug, publisher, publish_year, page_count)
    FROM '/docker-entrypoint-initdb.d/seeds/thieunhi.csv'
    DELIMITER ','
    CSV HEADER;

-- book: sach cha me
COPY books (image_url, title, price, category_id, isbn, description, author, in_stock, url_slug, publisher, publish_year, page_count)
    FROM '/docker-entrypoint-initdb.d/seeds/chame.csv'
    DELIMITER ','
    CSV HEADER;

-- book: sach tam ly
COPY books (image_url, title, price, category_id, isbn, description, author, in_stock, url_slug, publisher, publish_year, page_count)
    FROM '/docker-entrypoint-initdb.d/seeds/tamly.csv'
    DELIMITER ','
    CSV HEADER;

-- book: sach kinh te
COPY books (image_url, title, price, category_id, isbn, description, author, in_stock, url_slug, publisher, publish_year, page_count)
    FROM '/docker-entrypoint-initdb.d/seeds/kinhte.csv'
    DELIMITER ','
    CSV HEADER;

-- book: sach van hoc
COPY books (image_url, title, price, category_id, isbn, description, author, in_stock, url_slug, publisher, publish_year, page_count)
    FROM '/docker-entrypoint-initdb.d/seeds/vanhoc.csv'
    DELIMITER ','
    CSV HEADER;