//Definiert eine Mauer
public class QZ_Wall {
	private boolean east;
	private boolean south;
	private boolean west;
	private boolean north;
	
	public QZ_Wall(){
		east=false;
		south=false;
		west=false;
		north=false;
	}
	
	public void setEast(boolean east){
		this.east=east;
	}
	
	public void setSouth(boolean south){
		this.south=south;
	}
	
	public void setWest(boolean west){
		this.west=west;
	}
	
	public void setNorth(boolean north){
		this.north=north;
	}
	
	public boolean getEast(){
		return this.east;
	}
	
	public boolean getSouth(){
		return this.south;
	}
	
	public boolean getWest(){
		return this.west;
	}
	
	public boolean getNorth(){
		return this.north;
	}
	
	
}
