CREATE TABLE Doors_Prices (
  id int AUTO_INCREMENT, 
  leaf double NOT NULL,
  frame double NOT NULL,
  clypeus double NOT NULL,
  total_price double NOT NULL,
  door_id int NOT NULL,
  
  CONSTRAINT pk_Door_Price_id PRIMARY KEY (id),
  CONSTRAINT fk_Door_id_for_Doors_Prices  FOREIGN KEY (door_id) REFERENCES Doors (id)
) ;