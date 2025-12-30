COPY users (name, email, password, user_type)
    FROM '/docker-entrypoint-initdb.d/seeds/users.csv'
    DELIMITER ','
    CSV HEADER;
COPY books (isbn, title, description, author, price, in_stock, url_slug, publisher, publish_year, page_count, image_url)
    FROM '/docker-entrypoint-initdb.d/seeds/books.csv'
    DELIMITER ','
    CSV HEADER;

COPY categories (category_name, category_slug, parent_id)
    FROM '/docker-entrypoint-initdb.d/seeds/categories.csv'
    DELIMITER ','
    CSV HEADER;
