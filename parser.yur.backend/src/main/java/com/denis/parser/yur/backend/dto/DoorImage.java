package com.denis.parser.yur.backend.dto;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Doors_Images")
public class DoorImage implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String tipe;
	private byte[] image;
	@Column(name = "door_id")
	private int doorId;

	public DoorImage() {
		super(); // this form used by Hibernate
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTipe() {
		return tipe;
	}

	public void setTipe(String tipe) {
		this.tipe = tipe;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int getDoorId() {
		return doorId;
	}

	public void setDoorId(int doorId) {
		this.doorId = doorId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + doorId;
		result = prime * result + id;
		result = prime * result + Arrays.hashCode(image);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tipe == null) ? 0 : tipe.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DoorImage other = (DoorImage) obj;
		if (doorId != other.doorId)
			return false;
		if (id != other.id)
			return false;
		if (!Arrays.equals(image, other.image))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tipe == null) {
			if (other.tipe != null)
				return false;
		} else if (!tipe.equals(other.tipe))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DoorImage [id=" + id + ", name=" + name + ", tipe=" + tipe + ", image=" + Arrays.toString(image)
				+ ", doorId=" + doorId + "]";
	}

}
