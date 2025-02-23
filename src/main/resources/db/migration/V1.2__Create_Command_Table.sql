CREATE TABLE command (
    id SERIAL primary key,
    productName VARCHAR(255),
	productRef VARCHAR(255),
	quantity integer,
	price numeric(10,2),
	paymentId INTEGER,            
    FOREIGN KEY (paymentId)         
        REFERENCES payment(id)      
        ON DELETE CASCADE 
);