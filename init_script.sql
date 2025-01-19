CREATE TABLE genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

INSERT INTO genres (id, name) VALUES
    (1, 'Fantasy'),
    (4, 'Adventure'),
    (5, 'Mystery'),
    (6, 'Horror'),
    (7, 'Thriller & Suspense'),
    (8, 'Historical'),
    (9, 'Romance'),
    (10, 'Biography'),
    (11, 'History'),
    (12, 'Essays'),
    (13, 'Guide/How-to'),
    (14, 'Other'),
    (3, 'Science');

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    update_at TIMESTAMPTZ DEFAULT NOW(),
    avatar VARCHAR(94),
    email VARCHAR(40) NOT NULL,
    password VARCHAR(60) NOT NULL,
    role VARCHAR(255) NOT NULL,
    username VARCHAR(40) UNIQUE NOT NULL
);

CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    update_at TIMESTAMPTZ DEFAULT NOW(),
    author VARCHAR(45) NOT NULL,
    cover VARCHAR(94),
    date_of_publishing DATE,
    description VARCHAR(300),
    language VARCHAR(2) NOT NULL,
    numbers_of_pages INTEGER,
    price REAL NOT NULL,
    title VARCHAR(30) NOT NULL,
    created_by INTEGER REFERENCES users(id),
    quantity INTEGER NOT NULL,
    genre_id INTEGER REFERENCES genres(id)
);

CREATE TABLE cart (
    book_id BIGINT REFERENCES books(id),
    user_id INTEGER REFERENCES users(id),
    quantity INTEGER NOT NULL,
    PRIMARY KEY (book_id, user_id)
);
