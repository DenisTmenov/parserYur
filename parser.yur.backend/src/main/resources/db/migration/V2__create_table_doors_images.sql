CREATE TABLE Doors_Images (
  id int AUTO_INCREMENT, 
  name VARCHAR(255) UNIQUE, 
  type VARCHAR(255) ,
  image LONGBLOB ,
  door_id int NOT NULL,
  
  CONSTRAINT pk_Door_Image_id PRIMARY KEY (id),
  CONSTRAINT fk_Door_id_frome_Doors  FOREIGN KEY (door_id) REFERENCES Doors (id)
) ;

