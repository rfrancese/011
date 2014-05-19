package it.unisa.followteam.eccezioni;

public class TableNotFoundException extends Exception {

	public TableNotFoundException(){
		super();
	}
	public TableNotFoundException(String message){
		super(message);
	}
}
