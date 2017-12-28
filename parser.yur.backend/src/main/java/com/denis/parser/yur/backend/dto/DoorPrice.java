package com.denis.parser.yur.backend.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Doors_Prices")
public class DoorPrice implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "door_id")
	private int doorId;
	private double leaf; // полотно
	private double frame; // коробка
	private double clypeus; // наличники
	@Column(name = "total_price")
	private double totalPrice;

	public DoorPrice() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDoorId() {
		return doorId;
	}

	public void setDoorId(int doorId) {
		this.doorId = doorId;
	}

	public double getLeaf() {
		return leaf;
	}

	public void setLeaf(double leaf) {
		this.leaf = leaf;
	}

	public double getFrame() {
		return frame;
	}

	public void setFrame(double frame) {
		this.frame = frame;
	}

	public double getClypeus() {
		return clypeus;
	}

	public void setClypeus(double clypeus) {
		this.clypeus = clypeus;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(clypeus);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + doorId;
		temp = Double.doubleToLongBits(frame);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + id;
		temp = Double.doubleToLongBits(leaf);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		DoorPrice other = (DoorPrice) obj;
		if (Double.doubleToLongBits(clypeus) != Double.doubleToLongBits(other.clypeus))
			return false;
		if (doorId != other.doorId)
			return false;
		if (Double.doubleToLongBits(frame) != Double.doubleToLongBits(other.frame))
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(leaf) != Double.doubleToLongBits(other.leaf))
			return false;
		if (Double.doubleToLongBits(totalPrice) != Double.doubleToLongBits(other.totalPrice))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DoorPrice [id=" + id + ", doorId=" + doorId + ", leaf=" + leaf + ", frame=" + frame + ", clypeus="
				+ clypeus + ", totalPrice=" + totalPrice + "]";
	}

}
