package it.unisa.followteam.support;


public class Alloggio {

	public Alloggio(double lat, double lon, String nome, String indir){
		
		latitudine=lat;
		longitudine=lon;
		nomeHotel=nome;
		indirizzo=indir;
		
	}
	
	public double getLatitudine(){
		
		return latitudine;
	}
	
	public double getLongitudine(){
		
		return longitudine;
	}
	
	public String getNomeHotel(){
	
		return nomeHotel;
	}
	
	public String getIndrizzo(){
		
		return indirizzo;
	}
	
	public String toString(){
		
		return "latitudine: "+latitudine+" longitudine: "+longitudine+" nome hotel: "+nomeHotel+" indirizzo hotel: "+indirizzo;
	}
	
	private double latitudine;
	private double longitudine;
	private String nomeHotel;
	private String indirizzo;
}
