import roborally.task.Constants;
import roborally.task.Direction;

/**
 * Helper class with only static methods to make turning more comfortable
 * @author henning
 *
 */
public class G4_DirectionUtils{
	
	public final static String RotateCW = "clockwise";
	public final static String RotateCCW = "counterclockwise";
	public final static String NoRotation = "noRotation";
	
	public G4_DirectionUtils(Direction direction){
	
	}
	
	/**
	 * Rotates a given direction 90 degrees in the given rotation's direction
	 * @param direction
	 * @param rotation
	 * @return The new direction
	 */
	public static Direction rotate(Direction direction, String rotation){
		if (rotation == G4_DirectionUtils.RotateCCW){
			return G4_DirectionUtils.turnCCW(direction);
		}
		else if (rotation == G4_DirectionUtils.RotateCW){
			return G4_DirectionUtils.turnCW(direction);
		}
			
		return direction;
	}
	
	/**
	 * Turns a given direction 90 degrees clockwise
	 * @param direction
	 * @return
	 */
	public static Direction turnCW(Direction direction){
		if (direction.equals(Constants.DIRECTION_NORTH)){
			direction = Constants.DIRECTION_EAST;
		}
		else if (direction.equals(Constants.DIRECTION_EAST)){
			direction = Constants.DIRECTION_SOUTH;
		}
		else if (direction.equals(Constants.DIRECTION_SOUTH)){
			direction = Constants.DIRECTION_WEST;
		}
		else if (direction.equals(Constants.DIRECTION_WEST)){
			direction = Constants.DIRECTION_NORTH;
		}
		
		return direction;
	}
	
	
	/**
	 * Turns a given direction 90 degrees counterclockwise
	 * @param direction
	 * @return
	 */
	public static Direction turnCCW(Direction direction){
		if (direction.equals(Constants.DIRECTION_NORTH)){
			direction = Constants.DIRECTION_WEST;
		}
		else if (direction.equals(Constants.DIRECTION_EAST)){
			direction = Constants.DIRECTION_NORTH;
		}
		else if (direction.equals(Constants.DIRECTION_SOUTH)){
			direction = Constants.DIRECTION_EAST;
		}
		else if (direction.equals(Constants.DIRECTION_WEST)){
			direction = Constants.DIRECTION_SOUTH;
		}
				
		return direction;
	}

	/**
	 * Turns a given direction 180 degrees 
	 * @param direction
	 * @return
	 */
	public static Direction turnU(Direction direction){
		if (direction.equals(Constants.DIRECTION_NORTH)){
			direction = Constants.DIRECTION_SOUTH;
		}
		else if (direction.equals(Constants.DIRECTION_EAST)){
			direction = Constants.DIRECTION_WEST;
		}
		else if (direction.equals(Constants.DIRECTION_SOUTH)){
			direction = Constants.DIRECTION_NORTH;
		}
		else if (direction.equals(Constants.DIRECTION_WEST)){
			direction = Constants.DIRECTION_EAST;
		}

		return direction;
	}
	
	

}
