import roborally.task.Constants;
import roborally.task.Direction;

/**
 * @author henning
 *
 */
public class G4_Position {
	
	private Direction direction;
	public int x;
	public int y;
				
	public G4_Position(int x, int y, Direction direction){
		this.direction = direction;
		this.x = x;
		this.y = y;
	}
	
	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public String toString(){		
		return "Position (" + this.x + "," + this.y + ") -> " + this.direction;
	}
	
	public G4_Vertex toG4_Vertex(){
		try {
			return new G4_Vertex(this.x,this.y,"");
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		//Flacher Vergleich
		if(this == obj)
			return true;
		//Ist obj vom Typ G4_Position oder null
		if((obj == null) || (obj.getClass() != this.getClass()))
				return false;
		//obj muss vom Typ G4_Position sein.
		G4_Position position = (G4_Position)obj;
		return (this.x == position.x && this.y == position.y && 
				(this.getDirection() == position.getDirection() || 
						position.getDirection() == Constants.DIRECTION_STAY || 
						position.getDirection() == Constants.DIRECTION_NONE));
	}
	
	public G4_Position getPositionInDirection(Direction direction){
		if (direction == Constants.DIRECTION_NORTH){
			return new G4_Position(this.x, this.y-1, this.direction);
		}
		if (direction == Constants.DIRECTION_EAST){
			return new G4_Position(this.x+1, this.y, this.direction);
		}
		if (direction == Constants.DIRECTION_SOUTH){
			return new G4_Position(this.x, this.y+1, this.direction);
		}
		if (direction == Constants.DIRECTION_WEST){
			return new G4_Position(this.x-1, this.y, this.direction);
		}
		
		return this;
	}
}
