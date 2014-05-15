package it.unisa.gestioneAutenticazione;

import java.io.Serializable;

public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String squadra;
	
	public Account (){
		
	}
	
	public Account(String user, String pass, String squad){
		
		username=user;
		password=pass;
		squadra=squad;
		
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setUsername(String user){
		this.username=user;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void setPassword(String pass){
		this.password=pass;
	}
	
	public String getSquadra(){
		return this.squadra;
	}
	
	public void setSquadra(String squadra){
		this.squadra=squadra;
	}
	
	public String toString(){
		return getUsername()+"."+getPassword()+"."+getSquadra();
	}
}
