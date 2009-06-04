import roborally.task.Constants;
import roborally.task.Direction;

/**
 * A vertex in the graph representation of a Roborally.MapObject
 * 
 * @author henning
 *
 */
public class G4_Vertex {
	
	private int x;
	private int y;
	private G4_Effect effect;
	
	private boolean wallNorth = false;
	private boolean wallEast = false;
	private boolean wallSouth = false ;
	private boolean wallWest = false;
	
	private boolean shootingNorth = false;
	private boolean shootingEast = false;
	private boolean shootingWest = false;
	private boolean shootingSouth = false;
	
	private boolean isHole = false;
	
	public boolean isHole() {
		return isHole;
	}

	public void setHole(boolean isHole) {
		this.isHole = isHole;
	}

	public G4_Vertex(int x, int y, String nodeString) {
		this.x = x;
		this.y = y;
		this.effect = new G4_Effect(nodeString);
		
		if (nodeString.contains("north(wall()")){
			this.wallNorth = true;
		}
		if (nodeString.contains("east(wall()")){
			this.wallEast = true;
		}
		if (nodeString.contains("south(wall()")){
			this.wallSouth = true;
		}
		if (nodeString.contains("west(wall()")){
			this.wallWest = true;
		}
		if (nodeString.contains("OutOfGame")){
			this.isHole = true;
		}
	}
	
	public G4_Vertex(G4_Vertex v) {
		this.x = v.getX();
		this.y = v.getY();
		this.effect = v.getEffect();
		this.wallEast = v.isWallEast();
		this.wallNorth = v.isWallNorth();
		this.wallSouth = v.isWallSouth();
		this.wallWest =v.isWallWest();
		this.shootingEast = v.isShootingEast();
		this.shootingNorth = v.isShootingNorth();
		this.shootingSouth = v.isShootingSouth();
		this.shootingWest = v.isShootingWest();
	
	}
	
	@Override
	public boolean equals(Object obj) {
		//Flacher Vergleich
		if(this == obj)
			return true;
		//Ist obj vom Typ G4_Vertex oder null
		if((obj == null) || (obj.getClass() != this.getClass()))
				return false;
		//obj muss vom Typ G4_Vertex sein.
		G4_Vertex position = (G4_Vertex)obj;
		return (this.x == position.x && this.y == position.y);
	}
	
	@Override
	public int hashCode(){
		int hash = 7;
		hash = 31 * hash + this.x;
		hash = 31 * hash + this.y;
		return hash;
	}
	
	@Override
	public String toString(){
		return this.x + "," +this.y;
	}
	
	public G4_Position toG4_Position(){
		try {
			return new G4_Position(this.x,this.y,Constants.DIRECTION_STAY);
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean isWallNorth() {
		return wallNorth;
	}

	public boolean isWallEast() {
		return wallEast;
	}

	public boolean isWallSouth() {
		return wallSouth;
	}

	public boolean isWallWest() {
		return wallWest;
	}
	
	public void setWallNorth(boolean wallNorth) {
		this.wallNorth = wallNorth;
	}

	public void setWallEast(boolean wallEast) {
		this.wallEast = wallEast;
	}

	public void setWallSouth(boolean wallSouth) {
		this.wallSouth = wallSouth;
	}

	public void setWallWest(boolean wallWest) {
		this.wallWest = wallWest;
	}

	public int getX(){
		return this.x;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public G4_Effect getEffect() {
		return effect;
	}

	public void setEffect(G4_Effect effect) {
		this.effect = effect;
	}
	
	public boolean isShootingNorth() {
		return shootingNorth;
	}

	public void setShootingNorth(boolean shootingNorth) {
		this.shootingNorth = shootingNorth;
	}

	public boolean isShootingEast() {
		return shootingEast;
	}

	public void setShootingEast(boolean shootingEast) {
		this.shootingEast = shootingEast;
	}

	public boolean isShootingWest() {
		return shootingWest;
	}

	public void setShootingWest(boolean shootingWest) {
		this.shootingWest = shootingWest;
	}

	public boolean isShootingSouth() {
		return shootingSouth;
	}

	public void setShootingSouth(boolean shootingSouth) {
		this.shootingSouth = shootingSouth;
	}

	
	/**
	 * Determines if shooting in the given direction could hit an enemy 
	 */
	public boolean isShootingDirection(Direction direction) {
		if (direction.equals(Constants.DIRECTION_NORTH)){
			return this.isShootingNorth();
		}
		else if (direction.equals(Constants.DIRECTION_EAST)){
			return this.isShootingEast();
		}
		else if (direction.equals(Constants.DIRECTION_SOUTH)){
			return this.isShootingSouth();
		}
		else if (direction.equals(Constants.DIRECTION_WEST)){
			return this.isShootingWest();
		}
		
		//Optimismus...
		return true;
	}
	
	/**
	 * Sets if shooting in the given direction could hit an enemy 
	 */
	public void setShootingDirection(Direction direction, boolean value) {
		if (direction.equals(Constants.DIRECTION_NORTH)){
			this.setShootingNorth(value);
		}
		else if (direction.equals(Constants.DIRECTION_EAST)){
			this.setShootingEast(value);
		}
		else if (direction.equals(Constants.DIRECTION_SOUTH)){
			this.setShootingSouth(value);
		}
		else if (direction.equals(Constants.DIRECTION_WEST)){
			this.setShootingWest(value);
		}
	
	}
	
	
	/**
	 * Determines if there is a wall or border in the given direction 
	 */
	public boolean isWallinDirection(Direction direction) {
		if (direction.equals(Constants.DIRECTION_NORTH)){
			return this.isWallNorth();
		}
		else if (direction.equals(Constants.DIRECTION_EAST)){
			return this.isWallEast();
		}
		else if (direction.equals(Constants.DIRECTION_SOUTH)){
			return this.isWallSouth();
		}
		else if (direction.equals(Constants.DIRECTION_WEST)){
			return this.isWallWest();
		}
		
		//Optimismus...
		return true;
	}
	
	/**
	 * Applies the the rotation effects on this vertex to the given direction
	 * @param direction
	 * @return the new direction
	 */
	public Direction applyRotationEffects(Direction direction){
		return (G4_DirectionUtils.rotate(direction, this.effect.getRotationDirection()));
	}	
}
