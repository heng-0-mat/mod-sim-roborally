import java.util.HashSet;
import java.util.Vector;

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
	public String nodeString;

	private HashSet<Direction> wallDirections = new HashSet<Direction>();
	
	public boolean pusher = false;
	public Direction pusherDirection = Direction.NONE;
	
	public boolean conveyor = false;
	public boolean conveyorAndRotator = false;
	public Direction conveyorDirection = Direction.NONE;
	
	public boolean rotator = false;
	public String rotatorDirection = G4_DirectionUtils.NoRotation;
	
	private boolean hole = false;
	private boolean compactor = false;

	public boolean laser = false;
	public Direction laserDirection = Direction.NONE;
	
	public Vector<Direction> shotAtFromDirections = new Vector<Direction>();
	public HashSet<Direction> shootDirections = new HashSet<Direction>(); 
	public HashSet<Direction> ffDirections = new HashSet<Direction>(); 
	
	public boolean betterGetOffThatDamnThing = false;
	public Direction deathLiesInDirection = Direction.NONE;
	
	public boolean grenzknoten=false; //von Qi
	
	public boolean checkpoint = false;
	public int checkpointNr = -1;


	public G4_Vertex(int x, int y, String nodeString) {
		this.x = x;
		this.y = y;
		this.effect = new G4_Effect(nodeString);
		this.nodeString = nodeString;
		
		//System.out.println(nodeString);
		if (this.nodeString!= null){
		
		// ---------- WALLS ---------------------------------------
		if (nodeString.contains("north(wall()")){
			this.wallDirections.add(Direction.NORTH);
		}
		if (nodeString.contains("east(wall()")){
			this.wallDirections.add(Direction.EAST);
		}
		if (nodeString.contains("south(wall()")){
			this.wallDirections.add(Direction.SOUTH);
		}
		if (nodeString.contains("west(wall()")){
			this.wallDirections.add(Direction.WEST);
		}
		
		// ---------- HOLE ---------------------------------------
		if (nodeString.contains("OutOfGame")){
			this.hole = true;
		}
		
		// ---------- HOLE ---------------------------------------
		if (nodeString.contains("Compactor")){
			this.compactor = true;
		}
		
		// ---------- LASER ---------------------------------------
		// TODO Mehrere Laser auf einem Knoten
		if (nodeString.contains("LaserGun(north")){
			this.laser = true;
			this.laserDirection = Direction.NORTH;
			this.shotAtFromDirections.add(Direction.NORTH);
		}
		if (nodeString.contains("LaserGun(east")){
			this.laser = true;
			this.laserDirection = Direction.EAST;
			this.shotAtFromDirections.add(Direction.EAST);
		}
		if (nodeString.contains("LaserGun(south")){
			this.laser = true;
			this.laserDirection = Direction.SOUTH;
			this.shotAtFromDirections.add(Direction.SOUTH);
		}
		if (nodeString.contains("LaserGun(west")){
			this.laser = true;
			this.laserDirection = Direction.WEST;
			this.shotAtFromDirections.add(Direction.WEST);
		}
		
		// ---------- PUSHER ---------------------------------------
		if (nodeString.contains("PusherDevice(north")){
			this.pusher = true;
			this.pusherDirection = Direction.NORTH;
		}
		if (nodeString.contains("PusherDevice(east")){
			this.pusher = true;
			this.pusherDirection = Direction.EAST;
		}
		if (nodeString.contains("PusherDevice(south")){
			this.pusher = true;
			this.pusherDirection = Direction.SOUTH;
		}
		if (nodeString.contains("PusherDevice(west")){
			this.pusher = true;
			this.pusherDirection = Direction.WEST;		
		}
		
		// ---------- COGWHEEL ---------------------------------------
		if (nodeString.contains("CogRotate")){
			this.rotator = true;
			if (nodeString.contains("counterclockwise")){
				this.rotatorDirection = G4_DirectionUtils.RotateCCW;
			}
			else if (nodeString.contains("clockwise")){
				this.rotatorDirection = G4_DirectionUtils.RotateCW;
			}
		}
		
		// ---------- CONVEYER ---------------------------------------
		if (nodeString.contains("Conveyer")){
			this.conveyor = true;
			if (nodeString.contains("Conveyer(north")){
				this.conveyorDirection = Constants.DIRECTION_NORTH;
			}
			if (nodeString.contains("Conveyer(east")){
				this.conveyorDirection = Constants.DIRECTION_EAST;
			}
			if (nodeString.contains("Conveyer(south")){
				this.conveyorDirection = Constants.DIRECTION_SOUTH;
			}
			if (nodeString.contains("Conveyer(west")){
				this.conveyorDirection = Constants.DIRECTION_WEST;
			}
			if (nodeString.contains("counterclockwise")){
				this.conveyorAndRotator = true;
			}
			else if (nodeString.contains("clockwise")){
				this.conveyorAndRotator = true;				
			}
			
		}
		
		// ---------- CHECKPOINTS ---------------------------------------
		if (nodeString.contains("Checkpoint")){
			this.checkpoint = true;
			if (nodeString.contains("Checkpoint(0)")){
				this.checkpointNr = 0;
			}
			if (nodeString.contains("Checkpoint(1)")){
				this.checkpointNr = 1;
			}
			if (nodeString.contains("Checkpoint(2)")){
				this.checkpointNr = 2;
			}
			if (nodeString.contains("Checkpoint(3)")){
				this.checkpointNr = 3;
			}
			if (nodeString.contains("Checkpoint(4)")){
				this.checkpointNr = 4;
			}
		}	
		}
		
	}
	
	public G4_Vertex(G4_Vertex v) {
		super();
		this.x = v.x;
		this.y = v.y;
		this.effect = v.effect;
		this.nodeString = v.nodeString;
		this.wallDirections = v.wallDirections;
		this.pusher = v.pusher;
		this.pusherDirection = v.pusherDirection;
		this.conveyor = v.conveyor;
		this.conveyorAndRotator = v.conveyorAndRotator;
		this.conveyorDirection = v.conveyorDirection;
		this.rotator = v.rotator;
		this.rotatorDirection = v.rotatorDirection;
		this.hole = v.hole;
		this.compactor = v.compactor;
		this.laser = v.laser;
		this.laserDirection = v.laserDirection;
		this.shotAtFromDirections = v.shotAtFromDirections;
		this.shootDirections = v.shootDirections;
		this.ffDirections = v.ffDirections;
		this.betterGetOffThatDamnThing = v.betterGetOffThatDamnThing;
		this.deathLiesInDirection = v.deathLiesInDirection;
		this.grenzknoten = v.grenzknoten;
		this.checkpoint = v.checkpoint;
		this.checkpointNr = v.checkpointNr;
	}

	
	/**
	 * Determines if there is a wall or border in the given direction 
	 */
	public boolean isWallinDirection(Direction direction) {
		return this.wallDirections.contains(direction);
	}
	
	/**
	 * Sets if there is a wall or border in the given direction 
	 */
	public void setWallinDirection(Direction direction, Boolean value) {
		if (value)
			this.wallDirections.add(direction);
		else
			this.wallDirections.remove(direction);
	}
	
	/**
	 * Determines if shooting in the given direction could hit an enemy 
	 */
	public boolean isShootingDirection(Direction direction) {
		return this.shootDirections.contains(direction);
	}

	
	/**
	 * Sets if shooting in the given direction could hit an enemy 
	 */
	public void setShootingDirection(Direction direction, boolean value) {
		if (value){
			this.shootDirections.add(direction);
		}
		else{
			this.shootDirections.remove(direction);
		}
	}
	
	/**
	 * Determines if shooting in the given direction could hit a team mate
	 */
	public boolean isffDirection(Direction direction) {
		return this.shootDirections.contains(direction);
	}

	
	/**
	 * Sets if shooting in the given direction could hit a team mate
	 */
	public void setffDirection(Direction direction, boolean value) {
		if (value){
			this.shootDirections.add(direction);
		}
		else{
			this.shootDirections.remove(direction);
		}
	}
	
	/**
	 * Applies the the rotation effects on this vertex to the given direction
	 * @param direction
	 * @return the new direction
	 */
	public Direction applyRotationEffects(Direction direction){
		return (G4_DirectionUtils.rotate(direction, this.effect.getRotationDirection()));
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
			return new G4_Position(this.x,this.y,Constants.DIRECTION_NONE);
		} catch (Exception e) {
			return null;
		}
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
	
	
	public boolean isCompactor() {
		return compactor;
	}

	public void setCompactor(boolean compactor) {
		this.compactor = compactor;
	}	
	
	 
	//von Qi
	public boolean isGrenzknoten()
	{
		return this.grenzknoten;
	}
	
	//von Qi
	public void setGrenzknoten(boolean isGrenzknoten)
	{
		this.grenzknoten=isGrenzknoten;
	}
	
	public boolean isHole() {
		return hole;
	}

	public void setHole(boolean isHole) {
		this.hole = isHole;
	}
}
