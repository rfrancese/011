package gestioneAutenticazione;

public class Account {

	private String username;
	private String password;
	private String squadra;
	
	public Account(String user, String pass, String squad){
		
		username=user;
		password=pass;
		squadra=squad;
		
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String user){
		username=user;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String pass){
		password=pass;
	}
	
	public String getSquadra(){
		return squadra;
	}
	
	public void setSquadra(String squadra){
		this.squadra=squadra;
	}
}
