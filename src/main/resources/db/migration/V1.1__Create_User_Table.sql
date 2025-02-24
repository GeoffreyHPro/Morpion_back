CREATE TABLE users (
    id SERIAL primary key,
    username VARCHAR(255),
	email VARCHAR(255),
	password VARCHAR(255),
	roles VARCHAR(255)
);