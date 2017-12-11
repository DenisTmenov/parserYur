CREATE TABLE Doors (
  id int AUTO_INCREMENT, 
  name VARCHAR(255) , 
  brand VARCHAR(255) NOT NULL,
  collection VARCHAR(255) NOT NULL,
  material VARCHAR(255) ,
  coating VARCHAR(255) ,
  construction VARCHAR(255) ,
  color VARCHAR(255) ,
  tipe VARCHAR(255) ,
  size VARCHAR(255) ,
  url VARCHAR(255) UNIQUE,
  
  CONSTRAINT pk_Door_id PRIMARY KEY (id)
) ;

