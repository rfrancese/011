package it.unisa.eccezioni;

public class TableNotFoundException extends Exception {

	public TableNotFoundException(){
		super();
	}
	public TableNotFoundException(String message){
		super(message);
	}
}
