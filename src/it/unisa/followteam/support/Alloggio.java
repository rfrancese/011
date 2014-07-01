package it.unisa.followteam.support;

import java.io.Serializable;

public class Alloggio implements Serializable {

	private static final long serialVersionUID = 1L;

	public Alloggio(double lat, double lon, String nome, String indir, String rating) {

		this(lat,lon,nome,indir);
		this.rating = rating;

	}
	
	public Alloggio(double lat, double lon, String nome, String indir){
		latitudine = lat;
		longitudine = lon;
		nomeHotel = nome;
		indirizzo = indir;
	}
	
	

	public String getRating() {

		return rating;
	}

	public double getLatitudine() {

		return latitudine;
	}

	public double getLongitudine() {

		return longitudine;
	}

	public String getNomeHotel() {

		return nomeHotel;
	}

	public String getIndrizzo() {

		return indirizzo;
	}

	@Override
	public String toString() {

		return "latitudine: " + latitudine + " longitudine: " + longitudine
				+ " nome hotel: " + nomeHotel + " indirizzo hotel: "
				+ indirizzo;
	}
	

	private double latitudine;
	private double longitudine;
	private String nomeHotel, indirizzo, rating;
}
