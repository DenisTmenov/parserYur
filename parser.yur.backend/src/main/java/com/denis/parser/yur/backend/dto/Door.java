package com.denis.parser.yur.backend.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Doors")
public class Door implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String brand;
	private String collection;
	private String material;
	private String coating;
	private String construction;
	private String color;
	private String tipe;
	private String size;
	private String url;
	@Transient // ignore
	private List<DoorImage> doorImages;

	public Door() {
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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getCoating() {
		return coating;
	}

	public void setCoating(String coating) {
		this.coating = coating;
	}

	public String getConstruction() {
		return construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTipe() {
		return tipe;
	}

	public void setTipe(String tipe) {
		this.tipe = tipe;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<DoorImage> getDoorImages() {
		return doorImages;
	}

	public void setDoorImages(List<DoorImage> doorImages) {
		this.doorImages = doorImages;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((coating == null) ? 0 : coating.hashCode());
		result = prime * result + ((collection == null) ? 0 : collection.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((construction == null) ? 0 : construction.hashCode());
		result = prime * result + ((doorImages == null) ? 0 : doorImages.hashCode());
		result = prime * result + id;
		result = prime * result + ((material == null) ? 0 : material.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((tipe == null) ? 0 : tipe.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Door other = (Door) obj;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (coating == null) {
			if (other.coating != null)
				return false;
		} else if (!coating.equals(other.coating))
			return false;
		if (collection == null) {
			if (other.collection != null)
				return false;
		} else if (!collection.equals(other.collection))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (construction == null) {
			if (other.construction != null)
				return false;
		} else if (!construction.equals(other.construction))
			return false;
		if (doorImages == null) {
			if (other.doorImages != null)
				return false;
		} else if (!doorImages.equals(other.doorImages))
			return false;
		if (id != other.id)
			return false;
		if (material == null) {
			if (other.material != null)
				return false;
		} else if (!material.equals(other.material))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (tipe == null) {
			if (other.tipe != null)
				return false;
		} else if (!tipe.equals(other.tipe))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Door [id=" + id + ", name=" + name + ", brand=" + brand + ", collection=" + collection + ", material="
				+ material + ", coating=" + coating + ", construction=" + construction + ", color=" + color + ", tipe="
				+ tipe + ", size=" + size + ", url=" + url + ", doorImages=" + doorImages + "]";
	}

}
