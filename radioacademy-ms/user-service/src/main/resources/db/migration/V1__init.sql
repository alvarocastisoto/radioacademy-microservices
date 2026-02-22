CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    profile_picture_url VARCHAR(255) NOT NULL,
    sur_name VARCHAR(255) NOT NULL
);