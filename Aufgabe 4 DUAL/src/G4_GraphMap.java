import java.util.List;
import java.util.Vector;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.*;

import roborally.task.*;
import roborally.task.AITask.GameApi.MapObject;

/**
 * Die Klasse G4_GraphMap erbt von der Klasse DefaultDirectedWeightedGraph aus der JGraphT Bibliothek.
 * Der Graph besteht aus Knoten vom Typ G4_Vertex, und aus Kanten vom Typ DefaultWeightedEdge.
 * Diese Klasse stammt auch aus der JGraphT Bibliothek und stellt ein gewichtete Kante in diesem Graphen dar.
 * Desweiteren werden die Methoden addVertex, addEdge, setEdgeWeight aus JGraphT zum Manipulieren des Graphen benutzt.
 * Zum Suchen des kuerzesten Weges, wird eine Implementation des Dijkstra-Algorithmus aus JGraphT benutzt. 
 *
 */
public class G4_GraphMap extends DefaultDirectedWeightedGraph<G4_Vertex, DefaultWeightedEdge> {
	
	private static final long serialVersionUID = 1L;
		
	private MapObject rrMap;
	private Vector<RobotInformation>  enemies = new Vector<RobotInformation>();
	
	public G4_Vertex checkpoint = null;
	public G4_Vertex startpoint = null;
	public Direction startDirection = null;
	
	final int defaultConnWeight = 2;
	final int rotatorConnWeight = 3;
	final int reducedConnWeight = 1;
	final int increasedConnWeight = 3;
	
	public G4_GraphMap(MapObject map) {
		super(DefaultWeightedEdge.class);
	
		this.rrMap = map;
		this.loadMap(this.rrMap);

		
	}
	
	/**
	 * Returns the position of the nearest enemy to the given position 
	 * @param startPosition
	 * @return position of nearest enemy
	 */
	public G4_Position getNextEnemy(G4_Position startPosition){
		double pathToEnemyLength = 1000;
		G4_Position enemyPosition = null;
		
		for (RobotInformation robot: this.enemies){
			
			G4_Position robotPosition = new G4_Position(robot.getNode().getX(), robot.getNode().getY(), robot.getOrientation());
			double pathLength = this.getLengthOfShortestPath(startPosition, robotPosition);
			
			if (pathLength < pathToEnemyLength){
				pathToEnemyLength = pathLength;
				enemyPosition = robotPosition;
			}
		}
		
		if (enemyPosition == null){
			return null;
		}
		
		return enemyPosition;
		
	}
	
	/**
	 * Loads the enemies' positions into the G4_GraphMap.
	 * Marks direction as shooting direction on vertices if shooting in that direction from
	 * the vertex would hit an enemy.
	 * @param robots
	 */
	public void loadEnemies(RobotInformation[] robots, String myRobotName){
		
		for (RobotInformation robot: robots){
			if (robot.getRobotName() != myRobotName)
			 this.enemies.add(robot);				
		}
	
		for (RobotInformation robot: this.enemies){
		
			G4_Position robotPosition = new G4_Position(robot.getNode().getX(), 
														robot.getNode().getY(), 
														robot.getOrientation());
			
			G4_Vertex startVertex = this.getVertex(robotPosition.x, robotPosition.y);
			
			//Positionen markieren von denen geschossen werden könnte
			this.setShootingPositions(startVertex, Constants.DIRECTION_NORTH);
			this.setShootingPositions(startVertex, Constants.DIRECTION_EAST);
			this.setShootingPositions(startVertex, Constants.DIRECTION_SOUTH);
			this.setShootingPositions(startVertex, Constants.DIRECTION_WEST);
		}
	}
	
	/**
	 * "Walks" from a position into the given direction until a wall is hit
	 * and marks every vertex on this path as a shooting position for the opposite direction
	 * @param startVertex
	 * @param direction
	 */
	private void setShootingPositions(G4_Vertex startVertex, Direction direction){
		
		G4_Vertex currentVertex = startVertex;
		
		try {
			
			do{
				
				currentVertex = this.getVertexInDirection(currentVertex, direction);
				currentVertex.setShootingDirection(G4_DirectionUtils.turnU(direction),true);
								
			}while (!currentVertex.isWallinDirection(direction));
			
		} catch (Exception e) {
			// Falls Position ausserhalb
			//e.printStackTrace();
		}
		
	}
	
	/**
	 * Creates a graph representation of a Roborally.MapObject
	 * @param map
	 */
	public void loadMap(MapObject map){
		
		//Knoten erstellen fuer alle Felder auf der Karte
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
					G4_Vertex vertex = new G4_Vertex(x, y, map.getNode(x,y).toString());
										
					//Ist dies der Zielknoten?
					if (map.getNode(x,y).toString().contains("Checkpoint")){
						this.checkpoint = vertex;
					}
					if (map.getNode(x,y).toString().contains("Startpoint")){
						this.startpoint = vertex;
						
						if (map.getNode(x,y).toString().contains("Startpoint(north")){
							this.startDirection = Constants.DIRECTION_NORTH;
						}
						if (map.getNode(x,y).toString().contains("Startpoint(east")){
							this.startDirection = Constants.DIRECTION_EAST;
						}
						if (map.getNode(x,y).toString().contains("Startpoint(south")){
							this.startDirection = Constants.DIRECTION_SOUTH;
						}
						if (map.getNode(x,y).toString().contains("Startpoint(west")){
							this.startDirection = Constants.DIRECTION_WEST;
						}
						
					}
										
					this.addVertex(vertex);
			}
		}
		
		//Kanten fuer verbundene Tiles einfuegen
		for(G4_Vertex currentVertex: this.vertexSet()){
							
				G4_Vertex vertexNorth = this.getVertex(currentVertex.getX(),currentVertex.getY()-1);
				G4_Vertex vertexEast =  this.getVertex(currentVertex.getX()+1,currentVertex.getY());
				G4_Vertex vertexSouth = this.getVertex(currentVertex.getX(),currentVertex.getY()+1);
				G4_Vertex vertexWest =  this.getVertex(currentVertex.getX()-1,currentVertex.getY());
				
				//ALLE KNOTEN MITEINANDER VERKNUEPFEN
				if (this.vertexSet().contains(vertexNorth) && !currentVertex.isWallNorth()){
					this.addEdge(currentVertex,vertexNorth);
					this.setEdgeWeight(this.getEdge(currentVertex, vertexNorth), defaultConnWeight);
				}
				if (this.vertexSet().contains(vertexEast) && !currentVertex.isWallEast()){
					this.addEdge(currentVertex,vertexEast);
					this.setEdgeWeight(this.getEdge(currentVertex, vertexEast), defaultConnWeight);
				}
				if (this.vertexSet().contains(vertexSouth) && !currentVertex.isWallSouth()){
					this.addEdge(currentVertex,vertexSouth);
					this.setEdgeWeight(this.getEdge(currentVertex, vertexSouth), defaultConnWeight);
				}
				if (this.vertexSet().contains(vertexWest) && !currentVertex.isWallWest()){
					this.addEdge(currentVertex,vertexWest);
					this.setEdgeWeight(this.getEdge(currentVertex, vertexWest), defaultConnWeight);
				}
				
				//PUSHER- UND CONVEYER-KANTENGEWICHTE VERRINGERN
				Direction effectDirection = currentVertex.getEffect().getTranslationDirection();
				if (effectDirection != Constants.DIRECTION_STAY){
					G4_Vertex vertexInEffectDirection = this.getVertexInDirection(currentVertex, effectDirection);
					if (this.vertexSet().contains(vertexInEffectDirection)){
						this.setEdgeWeight(this.getEdge(currentVertex, vertexInEffectDirection), reducedConnWeight);
						//this.setEdgeWeight(this.getEdge(vertexInEffectDirection, currentVertex), increasedConnWeight);
					}
				}
										
			}
		
		//Kanten fuer Loecher entfernen
		for(G4_Vertex currentVertex: this.vertexSet()){
			
			G4_Vertex vertexNorth = this.getVertex(currentVertex.getX(),currentVertex.getY()-1);
			G4_Vertex vertexEast =  this.getVertex(currentVertex.getX()+1,currentVertex.getY());
			G4_Vertex vertexSouth = this.getVertex(currentVertex.getX(),currentVertex.getY()+1);
			G4_Vertex vertexWest =  this.getVertex(currentVertex.getX()-1,currentVertex.getY());
			
			Node currentNode = map.getNode(currentVertex.getX(),currentVertex.getY());				

			//AUSGEHENDE KANTEN VON LOECHERN LOESCHEN
			if (currentNode.toString().contains("OutOfGame")){
				
				//this.removeAllEdges(this.outgoingEdgesOf(currentVertex));
				
				if (this.vertexSet().contains(vertexNorth)){
					try {
						this.removeEdge(currentVertex,vertexNorth);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}						
				}
				if (this.vertexSet().contains(vertexEast)){
					try {
						this.removeEdge(currentVertex,vertexEast);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}						
				}
				if (this.vertexSet().contains(vertexSouth)){
					try {
						this.removeEdge(currentVertex,vertexSouth);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}						
				}
				if (this.vertexSet().contains(vertexWest)){
					try {
						this.removeEdge(currentVertex,vertexWest);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}						
				}
			}	
			
//			//DIAGONALKANTEN FUER ZAHNRAEDER EINFUEGEN
//			}else if (currentNode.toString().contains("counterclockwise")){
//				
//				try {
//					if (!vertexNorth.isHole() && !vertexEast.isHole() && currentVertex.isWallEast()){
//						this.addEdge(vertexNorth,vertexEast);
//						this.setEdgeWeight(this.getEdge(vertexNorth, vertexEast), rotatorConnWeight);
//					}
//				} catch (Exception e) {
//					//e.printStackTrace();
//				}
//				try {
//					if (!vertexEast.isHole() && !vertexSouth.isHole() && currentVertex.isWallSouth()){
//						this.addEdge(vertexEast,vertexSouth);
//						this.setEdgeWeight(this.getEdge(vertexEast, vertexSouth), rotatorConnWeight);
//					}
//				} catch (Exception e) {
//					//e.printStackTrace();
//				}
//				try {
//					if (!vertexSouth.isHole() && !vertexWest.isHole() && currentVertex.isWallWest()){
//						this.addEdge(vertexSouth,vertexWest);
//						this.setEdgeWeight(this.getEdge(vertexSouth, vertexWest), rotatorConnWeight);
//					}
//				} catch (Exception e) {
//					//e.printStackTrace();
//				}
//				try {
//					if (!vertexWest.isHole() && !vertexNorth.isHole() && currentVertex.isWallNorth()){
//						this.addEdge(vertexWest,vertexNorth);
//						this.setEdgeWeight(this.getEdge(vertexWest, vertexNorth), rotatorConnWeight);
//					}
//				} catch (Exception e) {
//					//e.printStackTrace();
//				}
//			}else if (currentNode.toString().contains("clockwise")){
//				try {
//					if (!vertexNorth.isHole() && !vertexWest.isHole() && currentVertex.isWallWest()){
//						this.addEdge(vertexNorth,vertexWest);
//						this.setEdgeWeight(this.getEdge(vertexNorth, vertexWest), rotatorConnWeight);
//					}
//				} catch (Exception e) {
//					//e.printStackTrace();
//				}
//				try {
//					if (!vertexWest.isHole() && !vertexSouth.isHole() && currentVertex.isWallSouth()){
//						this.addEdge(vertexWest,vertexSouth);
//						this.setEdgeWeight(this.getEdge(vertexWest, vertexSouth), rotatorConnWeight);
//					}
//				} catch (Exception e) {
//					//e.printStackTrace();
//				}
//				try {
//					if (!vertexSouth.isHole() && !vertexEast.isHole() && currentVertex.isWallEast()){
//						this.addEdge(vertexSouth,vertexEast);
//						this.setEdgeWeight(this.getEdge(vertexSouth, vertexEast), rotatorConnWeight);
//					}
//				} catch (Exception e) {
//					//e.printStackTrace();
//				}
//				try {
//					if (!vertexEast.isHole() && !vertexNorth.isHole() && currentVertex.isWallNorth()){
//						this.addEdge(vertexEast,vertexNorth);
//						this.setEdgeWeight(this.getEdge(vertexEast, vertexNorth), rotatorConnWeight);
//					}
//				} catch (Exception e) {
//					//e.printStackTrace();		
//			}
//				
			
		}

	}
	
	/**
	 * Adjusts the edge weights around the given position so that moving in the given direction
	 * (at most 3 vertices) is less expensive than moving into the other directions
	 * @param position 
	 * @param direction
	 * @return A G4_GraphMap with adjusted weights around the given position
	 */
	private G4_GraphMap getAdaptedGraph(G4_Vertex position, Direction direction){

		G4_GraphMap returnGraph = (G4_GraphMap) this.clone();
				
		int x = position.getX();
		int y = position.getY();
		
		G4_Vertex coordinatesNorth = this.getVertex(x, y-1);	
		G4_Vertex coordinatesNorth2 = this.getVertex(x, y-2);	
		G4_Vertex coordinatesNorth3 = this.getVertex(x, y-3);	
		G4_Vertex coordinatesEast =  this.getVertex(x+1, y);	
		G4_Vertex coordinatesEast2 =  this.getVertex(x+2, y);	
		G4_Vertex coordinatesEast3 =  this.getVertex(x+3, y);	
		G4_Vertex coordinatesSouth = this.getVertex(x, y+1);	
		G4_Vertex coordinatesSouth2 = this.getVertex(x, y+2);	
		G4_Vertex coordinatesSouth3 = this.getVertex(x, y+3);	
		G4_Vertex coordinatesWest =  this.getVertex(x-1, y);	
		G4_Vertex coordinatesWest2 =  this.getVertex(x-2, y);	
		G4_Vertex coordinatesWest3 =  this.getVertex(x-3, y);	
		
		//TODO Kantengewichte relativ anpassen

		try{
		
		if (direction == Constants.DIRECTION_NORTH){
			DefaultWeightedEdge edge = returnGraph.getEdge(position, coordinatesNorth);
			returnGraph.setEdgeWeight(edge, reducedConnWeight);
		}
		if (direction == Constants.DIRECTION_EAST){
			DefaultWeightedEdge edge = returnGraph.getEdge(position, coordinatesEast);
			returnGraph.setEdgeWeight(edge, reducedConnWeight);
		}
		if (direction == Constants.DIRECTION_SOUTH){
			DefaultWeightedEdge edge = returnGraph.getEdge(position, coordinatesSouth);
			returnGraph.setEdgeWeight(edge, reducedConnWeight);
		}
		if (direction == Constants.DIRECTION_WEST){
			DefaultWeightedEdge edge = returnGraph.getEdge(position, coordinatesWest);
			returnGraph.setEdgeWeight(edge, reducedConnWeight);
		}

		}catch (Exception e){}

		try{

			if (direction == Constants.DIRECTION_NORTH){
				DefaultWeightedEdge edge = returnGraph.getEdge(coordinatesNorth, coordinatesNorth2);
				returnGraph.setEdgeWeight(edge, reducedConnWeight);
			}
			if (direction == Constants.DIRECTION_EAST){
				DefaultWeightedEdge edge = returnGraph.getEdge(coordinatesEast, coordinatesEast2);
				returnGraph.setEdgeWeight(edge, reducedConnWeight);
			}
			if (direction == Constants.DIRECTION_SOUTH){
				DefaultWeightedEdge edge = returnGraph.getEdge(coordinatesSouth, coordinatesSouth2);
				returnGraph.setEdgeWeight(edge, reducedConnWeight);
			}
			if (direction == Constants.DIRECTION_WEST){
				DefaultWeightedEdge edge = returnGraph.getEdge(coordinatesWest, coordinatesWest2);
				returnGraph.setEdgeWeight(edge, reducedConnWeight);
			}

		}catch (Exception e){}

		try{

			if (direction == Constants.DIRECTION_NORTH){
				DefaultWeightedEdge edge = returnGraph.getEdge(coordinatesNorth2, coordinatesNorth3);
				returnGraph.setEdgeWeight(edge, reducedConnWeight);
			}
			if (direction == Constants.DIRECTION_EAST){
				DefaultWeightedEdge edge = returnGraph.getEdge(coordinatesEast2, coordinatesEast3);
				returnGraph.setEdgeWeight(edge, reducedConnWeight);
			}
			if (direction == Constants.DIRECTION_SOUTH){
				DefaultWeightedEdge edge = returnGraph.getEdge(coordinatesSouth2, coordinatesSouth3);
				returnGraph.setEdgeWeight(edge, reducedConnWeight);
			}
			if (direction == Constants.DIRECTION_WEST){
				DefaultWeightedEdge edge = returnGraph.getEdge(coordinatesWest2, coordinatesWest3);
				returnGraph.setEdgeWeight(edge, reducedConnWeight);
			}

		}catch (Exception e){}
	
		return returnGraph; 
	}
	
	/**
	 * Calculates Dijkstras shortest Path from the given Start-Position to the given End-Position
	 * @param startPosition
	 * @return The edges on the shortest path
	 */
	public List<DefaultWeightedEdge> getEdgesOnShortestPath(G4_Position startPosition, G4_Position endPosition){
		
		G4_Vertex startVertex = new G4_Vertex(startPosition.x,startPosition.y,"");
		G4_Vertex endVertex = new G4_Vertex(endPosition.x, endPosition.y, "");
		
		try {
			//Kuerzesten Weg von momentaner Position zum Checkpoint bestimmen
			DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge> shortestPath = 
				new DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge>(
						this.getAdaptedGraph(startVertex,startPosition.getDirection()), 
						startVertex, 
						endVertex);
//				new DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge>(
//						this, 
//						startVertex, 
//						endVertex);
			return shortestPath.getPathEdgeList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Calculates Dijkstras shortest Path from the given Start-Position to the given End-Position
	 * @param startPosition
	 * @return The length of the shortest path
	 */
	public double getLengthOfShortestPath(G4_Position startPosition, G4_Position endPosition){
		
		G4_Vertex startVertex = new G4_Vertex(startPosition.x, startPosition.y, "");
		G4_Vertex endVertex = new G4_Vertex(endPosition.x, endPosition.y, "");
		
		 //Kuerzesten Weg von momentaner Position zum Checkpoint bestimmen
		 DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge> shortestPath = 
			 new DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge>(
					 this.getAdaptedGraph(startVertex,startPosition.getDirection()), 
					 startVertex, 
					 endVertex);
		 return shortestPath.getPathLength();
	}

	/**
	 * Returns the vertex representing the tile in the given direction.
	 * @param vertex The vertex to start from
	 * @param direction The direction to "walk" into
	 * @return The vertex lying in the given direction or null if there is none (out of bounds).
	 */
	public G4_Vertex getVertexInDirection(G4_Vertex vertex, Direction direction){
		
		//HIER LAG EIN PROBLEM!!!
		if (vertex == null)
			return null;
		
		if (direction.equals(Constants.DIRECTION_STAY)){
			return vertex;
		}
		
		G4_Vertex nextVertex = new G4_Vertex(vertex);
		
		if (direction.equals(Constants.DIRECTION_NORTH)){
			nextVertex.setY(vertex.getY() - 1);
		}
		else if (direction.equals(Constants.DIRECTION_EAST)){
			nextVertex.setX(vertex.getX() + 1);
		}
		else if (direction.equals(Constants.DIRECTION_SOUTH)){
			nextVertex.setY(vertex.getY() + 1);
		}
		else if (direction.equals(Constants.DIRECTION_WEST)){
			nextVertex.setX(vertex.getX() - 1);
		}
		
		if (this.getEdge(vertex, nextVertex) != null){
			return this.getVertex(nextVertex.getX(),nextVertex.getY());
		}
		else{
			return null;
		}
	}
	
	/**
	 * Returns the vertex that corresponds to the coordinates.
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @return The corresponding vertex or null if there is none corresponding
	 */
	public G4_Vertex getVertex(int x, int y){
		
		G4_Vertex vertex = new G4_Vertex(x,y,"");
		
		for (G4_Vertex v : this.vertexSet()){
			if (v.equals(vertex))
				return v;
		}
		
		return null;		
	}

	/**
	 * Returns the vertex in the translation direction of the given vertex' effects
	 * @param vertex 
	 * @return The vertex a robot would be translated to if standing on given vertex or null 
	 * 			if vertex is not part of the graph (out of bounds).
	 */
	public G4_Vertex applyTranslationEffects(G4_Vertex vertex){
		return this.getVertexInDirection(vertex,vertex.getEffect().getTranslationDirection());
	}

	/**
	 * Returns the direction of the given edge
	 * @param edge
	 * @return the direction of the edge
	 */
	public Direction getDirectionOfEdge(DefaultWeightedEdge edge){
		Direction direction = null;
		
		G4_Vertex source = this.getEdgeSource(edge);
		G4_Vertex target = this.getEdgeTarget(edge);
		
		 //--------- RICHTUNG DES NAECHSTEN KNOTEN BESTIMMEN -----------------------------------------
		
		 // x-Position bleibt gleich
		 if (source.getX() == target.getX()){
			 // y-Position des naechsten Knoten ist kleiner => NORDEN 
			 if (target.getY() < source.getY()){
				 direction = Constants.DIRECTION_NORTH;
			 }
			 // y-Position des naechsten Knoten ist groesser => SUEDEN 
			 else if (target.getY() > source.getY()){
				 direction = Constants.DIRECTION_SOUTH;
			 }
		 }
		 // y-Position bleibt gleich
		 else if (source.getY() == target.getY()){
			// x-Position des naechsten Knoten ist kleiner => WESTEN 
			 if (target.getX() < source.getX()){
				 direction = Constants.DIRECTION_WEST;
			 }
			 // x-Position des naechsten Knoten ist groesser => OSTEN 
			 else if (target.getX() > source.getX()){
				 direction = Constants.DIRECTION_EAST;
				 }
		 }
				
		return direction;
	} 
	
	
}


