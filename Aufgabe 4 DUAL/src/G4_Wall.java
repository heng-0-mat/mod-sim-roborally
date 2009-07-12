/**
 * @author Qi Zheng
 *
 *Mit dieser Klasse koennen die Information ueber alle vier Richtungen einer Mauern gespeicht werden.
 */
public class G4_Wall {
	private boolean east;	//Definiere die Richtung East
	private boolean south;	//Definiere die Richtung South
	private boolean west;	//Definiere die Richtung West
	private boolean north;	//Definiere die Richtung North
	
	/**
	 * Jede Knoten besitzt die Infomation ueber Mauer fuer alle vier Richtung um ihr 
	 */
	public G4_Wall(){
		east=false;
		south=false;
		west=false;
		north=false;
	}
	
	//Kriegt die MauernInformation ueber die Richung East, wenn hier "true" ist,heisst es, in der Richtung gibt es eine Mauern
	public void setEast(boolean east){
		this.east=east;
	}
	
	//Kriegt die Information ueber die Richtung South
	public void setSouth(boolean south){
		this.south=south;
	}
	
	//Ueber die Richtung West
	public void setWest(boolean west){
		this.west=west;
	}
	
	//Ueber die Richtung North
	public void setNorth(boolean north){
		this.north=north;
	}
	
	//Lieft die MauernInformation ueber die Richung East, wenn hier "true" ist,heisst es, in der Richtung gibt es eine Mauern
	public boolean getEast(){
		return this.east;
	}
	
	//Genau wie oben,aber liefert die Infomation ueber die Richtung South
	public boolean getSouth(){
		return this.south;
	}
	
	//Ueber die Richtung West
	public boolean getWest(){
		return this.west;
	}
	
	//Ueber die Richtung North
	public boolean getNorth(){
		return this.north;
	}
	
	
}
