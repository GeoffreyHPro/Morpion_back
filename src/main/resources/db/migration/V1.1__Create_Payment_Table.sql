CREATE TABLE payment (
    id SERIAL primary key,
    amount numeric(10,2),
	currency VARCHAR(255),
	paymentMeans VARCHAR(255),
	paymentStatus VARCHAR(255)
);