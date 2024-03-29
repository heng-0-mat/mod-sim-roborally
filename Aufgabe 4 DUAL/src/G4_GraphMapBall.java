import java.util.List;
import java.util.Set;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import roborally.task.Constants;
import roborally.task.Direction;

/**
 * @author omar el goss
 *
 */
public class G4_GraphMapBall extends DefaultDirectedWeightedGraph<G4_Vertex, DefaultWeightedEdge> {

	private G4_Position positionOfBall;
	
	final int defaultConnWeight = 2;
	final int rotatorConnWeight = 3;
	final int reducedConnWeight = 1;
	final int increasedConnWeight = 3;
		
	public G4_Position getPositionOfBall() {
		return positionOfBall;
	}

	public void setPositionOfBall(G4_Position positionOfBall) {
		this.positionOfBall = positionOfBall;
	}

	public G4_GraphMapBall(Set<G4_Vertex> vertexSet) {
		super(DefaultWeightedEdge.class);
		
		for(G4_Vertex vertex: vertexSet){
			this.addVertex(vertex);	
		}
		
		//Kanten einfuegen falls das Feld in in Gegenrichtung benutzbar ist
		for(G4_Vertex currentVertex: this.vertexSet()){

			G4_Vertex vertexNorth = this.getVertex(currentVertex.getX(),currentVertex.getY()-1);
			G4_Vertex vertexEast =  this.getVertex(currentVertex.getX()+1,currentVertex.getY());
			G4_Vertex vertexSouth = this.getVertex(currentVertex.getX(),currentVertex.getY()+1);
			G4_Vertex vertexWest =  this.getVertex(currentVertex.getX()-1,currentVertex.getY());

			if (!currentVertex.isHole()){

				//KNOTEN MITEINANDER VERKNUEPFEN
				if (this.vertexSet().contains(vertexNorth) 
						&& this.vertexSet().contains(vertexSouth) 
						&& !currentVertex.isWallinDirection(Direction.NORTH)
						&& !currentVertex.isWallinDirection(Direction.SOUTH)
						&& !vertexSouth.isHole()){
					this.addEdge(currentVertex,vertexNorth);
				}

				if (this.vertexSet().contains(vertexEast) 
						&& this.vertexSet().contains(vertexWest) 
						&& !currentVertex.isWallinDirection(Direction.EAST)
						&& !currentVertex.isWallinDirection(Direction.WEST )
						&& !vertexWest.isHole()){
					this.addEdge(currentVertex,vertexEast);
				}

				if (this.vertexSet().contains(vertexSouth) 
						&& this.vertexSet().contains(vertexNorth) 
						&& !currentVertex.isWallinDirection(Direction.SOUTH)
						&& !currentVertex.isWallinDirection(Direction.NORTH)
						&& !vertexNorth.isHole()){
					this.addEdge(currentVertex,vertexSouth);
				}

				if (this.vertexSet().contains(vertexWest) 
						&& this.vertexSet().contains(vertexEast) 
						&& !currentVertex.isWallinDirection(Direction.WEST )
						&& !currentVertex.isWallinDirection(Direction.EAST)
						&& !vertexEast.isHole()){
					this.addEdge(currentVertex,vertexWest);
				}
			}
		}
		
		for (DefaultWeightedEdge edge : this.edgeSet()){
			this.setEdgeWeight(edge, defaultConnWeight);
		}
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
	 * Calculates Dijkstras shortest Path from the given Position to the this Graph's checkpoint
	 * @param startPosition
	 * @return The edges on the shortest path
	 */
	public List<DefaultWeightedEdge> getEdgesOnShortestPath(G4_Position startPosition, G4_Position endPosition){
		
		G4_Vertex startVertex = new G4_Vertex(startPosition.x,startPosition.y,"");
		G4_Vertex endVertex = new G4_Vertex(endPosition.x, endPosition.y, "");
		
		//Kuerzesten Weg von momentaner Position zum Checkpoint bestimmen
		DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge> shortestPath = 
			new DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge>(
					 this.getAdaptedGraph(startVertex, startPosition.getDirection()),
					// this,
					startVertex, 
					endVertex);
		return shortestPath.getPathEdgeList();
	}
	
	/**
	 * Calculates Dijkstras shortest Path from the given Position to the this Graph's checkpoint
	 * @param startPosition
	 * @return The length of the shortest path
	 */
	public double getLengthOfShortestPath(G4_Position startPosition, G4_Position endPosition){
		
		G4_Vertex startVertex = new G4_Vertex(startPosition.x, startPosition.y, "");
		G4_Vertex endVertex = new G4_Vertex(endPosition.x, endPosition.y, "");
		
		 //Kuerzesten Weg von momentaner Position zum Checkpoint bestimmen
		 DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge> shortestPath = 
			 new DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge>(
					 this.getAdaptedGraph(startVertex, startPosition.getDirection()),
					// this,
					 startVertex, 
					 endVertex);
		 return shortestPath.getPathLength();
	}

	/**
	 * Returns the next position to push from when moving the ball from given StartPostion
	 * to the given EndPosition
	 * 
	 * @param startBallPosition
	 * @param endBallPosition
	 * @return next position to push from or null
	 */
	public G4_Position getNextPushingPosition(G4_Position startBallPosition, G4_Position endBallPosition){
						
		List<DefaultWeightedEdge> path = this.getEdgesOnShortestPath(startBallPosition, endBallPosition);
		
		if (path.size() == 0)
			return null;
		
		G4_Vertex vertexOfBall = this.getVertex(startBallPosition.x, startBallPosition.y);
		Direction nextEdgesDirection = this.getDirectionOfEdge(path.get(0));
		 
		G4_Vertex nextPushingVertex = this.getVertexInDirection(vertexOfBall, G4_DirectionUtils.turnU(nextEdgesDirection));
		G4_Position nextPushingPosition = new G4_Position(nextPushingVertex.getX(), nextPushingVertex.getY(), nextEdgesDirection);
		
		return nextPushingPosition;
					
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
	
	/**
	 * Determines whether moving from the given start position to the given end position is possible
	 * @param start
	 * @param ziel
	 * @return
	 */
	public boolean isMovingPossible(G4_Position start, G4_Position ziel){
		
		if (this.getLengthOfShortestPath(start, ziel) != Double.POSITIVE_INFINITY)
			return true;
		
		return false;
	}
	
	/**
	 * Returns the nearest Position to the given Position from which pushing the Ball is possible
	 * @param position
	 * @return
	 */
	public G4_Position getNearestPushPosition(G4_Position position, G4_GraphMap MovingMap){
		
		double shortestLength = 1000;
		G4_Position nearestPosition = null;
		
		G4_Position positionNorth;
		try {
			positionNorth = this.getVertexInDirection(this.positionOfBall.toG4_Vertex(), Constants.DIRECTION_NORTH).toG4_Position();
			positionNorth.setDirection(Constants.DIRECTION_SOUTH);
			double lengthNorth = MovingMap.getLengthOfShortestPath_OLD(position, positionNorth);
			if (shortestLength > lengthNorth){
				shortestLength = lengthNorth;
				nearestPosition = positionNorth;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		G4_Position positionEast;
		try {
			positionEast = this.getVertexInDirection(this.positionOfBall.toG4_Vertex(), Constants.DIRECTION_EAST).toG4_Position();
			positionEast.setDirection(Constants.DIRECTION_WEST);
			double lengthEast = MovingMap.getLengthOfShortestPath_OLD(position, positionEast);
			if (shortestLength > lengthEast){
				shortestLength = lengthEast;
				nearestPosition = positionEast;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		G4_Position positionSouth;
		try {
			positionSouth = this.getVertexInDirection(this.positionOfBall.toG4_Vertex(), Constants.DIRECTION_SOUTH).toG4_Position();
			positionSouth.setDirection(Constants.DIRECTION_NORTH);
			double lengthSouth = MovingMap.getLengthOfShortestPath_OLD(position, positionSouth);
			if (shortestLength > lengthSouth){
				shortestLength = lengthSouth;
				nearestPosition = positionSouth;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		G4_Position positionWest;
		try {
			positionWest = this.getVertexInDirection(this.positionOfBall.toG4_Vertex(), Constants.DIRECTION_WEST).toG4_Position();
			positionWest.setDirection(Constants.DIRECTION_EAST);
			double lengthWest = MovingMap.getLengthOfShortestPath_OLD(position, positionWest);
			if (shortestLength > lengthWest){
				shortestLength = lengthWest;
				nearestPosition = positionWest;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nearestPosition;
			
	}

	/**
	 * Returns how far the ball can be pushed from the given position in the given positions direction
	 * Assumes that the given position is a pushing position.
	 * @param position
	 * @return
	 */
	public int getMaximalPushStrength(G4_Position position){
		
		//Position 0 ist die Position des Balls
		G4_Position position0 = position.getPositionInDirection(position.getDirection());
		G4_Position position1 = position0.getPositionInDirection(position.getDirection());
		G4_Position position2 = position1.getPositionInDirection(position.getDirection());
		G4_Position position3 = position2.getPositionInDirection(position.getDirection());
		
		if (this.isMovingPossible(position3, position0))
			return 3;
		if (this.isMovingPossible(position2, position0))
			return 2;
		if (this.isMovingPossible(position1, position0))
			return 1;

		return 0;
	}
	
	/**
	 * Adjusts the edge weights around the given position so that moving in the given direction
	 * (at most 3 vertices) is less expensive than moving into the other directions
	 * @param position 
	 * @param direction
	 * @return A D_GraphMap with adjusted weights around the given position
	 */
	private G4_GraphMapBall getAdaptedGraph(G4_Vertex position, Direction direction){

		G4_GraphMapBall returnGraph = (G4_GraphMapBall) this.clone();
		
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
	
}
