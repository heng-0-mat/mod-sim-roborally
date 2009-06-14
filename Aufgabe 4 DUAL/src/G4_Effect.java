import roborally.task.Constants;
import roborally.task.Direction;

/**
 * Represents Roborally Effects on Tiles (Vertices)
 * 
 *
 */
public class G4_Effect {
	
	private String rotationDirection;
	private Direction translationDirection;
	public int effectOnHealth = 0;
	//private ArrayList<Integer> activePhase;
	//OMARS KOMMENTAR
	
	public String getRotationDirection() {
		return rotationDirection;
	}

	public Direction getTranslationDirection() {
		return translationDirection;
	}
	
	/**
	 * Constructor:
	 * Reads a Roborally.Node to find out what effects are on the Node
	 */
	public G4_Effect(String nodeString) {
		//this.activePhase = new ArrayList();
		this.translationDirection = Constants.DIRECTION_STAY;
		this.rotationDirection = "";
		
		// ---------- PUSHER ---------------------------------------
		if (nodeString.contains("PusherDevice")){
			if (nodeString.contains("PusherDevice(north")){
				this.translationDirection = Constants.DIRECTION_NORTH;
			}
			else if (nodeString.contains("PusherDevice(east")){
				this.translationDirection = Constants.DIRECTION_EAST;
			}
			else if (nodeString.contains("PusherDevice(south")){
				this.translationDirection = Constants.DIRECTION_SOUTH;
			}
			else if (nodeString.contains("PusherDevice(west")){
				this.translationDirection = Constants.DIRECTION_WEST;
			}
		}
		
		// ---------- COGWHEEL ---------------------------------------
		else if (nodeString.contains("CogRotate")){
			if (nodeString.contains("counterclockwise")){
				this.rotationDirection = G4_DirectionUtils.RotateCCW;
			}
			else if (nodeString.contains("clockwise")){
				this.rotationDirection = G4_DirectionUtils.RotateCW;				
			}
		}
		
		// ---------- CONVEYER ---------------------------------------
		else if (nodeString.contains("Conveyer")){
			if (nodeString.contains("Conveyer(north")){
				this.translationDirection = Constants.DIRECTION_NORTH;
			}
			if (nodeString.contains("Conveyer(east")){
				this.translationDirection = Constants.DIRECTION_EAST;
			}
			if (nodeString.contains("Conveyer(south")){
				this.translationDirection = Constants.DIRECTION_SOUTH;
			}
			if (nodeString.contains("Conveyer(west")){
				this.translationDirection = Constants.DIRECTION_WEST;
			}
			if (nodeString.contains("counterclockwise")){
				this.rotationDirection = G4_DirectionUtils.RotateCCW;
			}
			else if (nodeString.contains("clockwise")){
				this.rotationDirection = G4_DirectionUtils.RotateCW;				
			}
			
		}
		//MEIN KOMMENTAR
		
	}

}