package entite;

import java.io.Serializable;

import view.PointsCoordinate;

public class City implements Serializable{

	private String name;
	private PointsCoordinate latlang;

	

	public PointsCoordinate getLatlang() {
		return latlang;
	}

	public void setLatlang(PointsCoordinate latlang) {
		this.latlang = latlang;
	}

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "City [name=" + name + ", latlang=" + latlang + "]";
	}

	public City(String name, PointsCoordinate latlang) {
		super();
		this.name = name;
		this.latlang = latlang;
	}

	
}
