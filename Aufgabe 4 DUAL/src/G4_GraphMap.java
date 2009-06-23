import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.jgrapht.alg.BellmanFordShortestPath;
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
	private String[][] nodeStrings;
	private Vector<RobotInformation>  enemies = new Vector<RobotInformation>();
	
	public G4_Position startPosition = null; 
	
	public HashSet<G4_Vertex> pushers = new HashSet<G4_Vertex>();
	public HashSet<G4_Vertex> compactors = new HashSet<G4_Vertex>();
	public HashSet<G4_Vertex> conveyors = new HashSet<G4_Vertex>();
	public HashSet<G4_Vertex> holes = new HashSet<G4_Vertex>();
	public HashSet<G4_Vertex> lasers = new HashSet<G4_Vertex>();
	public HashSet<G4_Vertex> mostDangerous = new HashSet<G4_Vertex>();
	public HashSet<G4_Vertex> grenzKnoten = new HashSet<G4_Vertex>();
	
	final int defaultConnWeight = 2;
	final int default2ConnWeight = 4;
	final int default3ConnWeight = 6;
	
	final int inHoleWeight = 50;
	final int inCompactorWeight = 50;
	
	final int inLaserWeight = 10;
	final int outLaserWeight = 2;
	final int stayInLaserWeight = 50;
	
	final int withConveyorWeight = 1;
	final int againstConveyorWeight = 8;
	
	
	public G4_GraphMap(MapObject map, G4_Position startPosition) {
		super(DefaultWeightedEdge.class);
	
		this.rrMap = map;
		this.startPosition = startPosition;
		
		this.loadMap(this.rrMap);

		
	}
	
	public G4_GraphMap(String[][] nodeStrings, G4_Position startPosition) {
		super(DefaultWeightedEdge.class);
	
		this.rrMap = null;
		this.nodeStrings = nodeStrings;
		this.startPosition = startPosition;
		
		this.loadMap(nodeStrings);

		
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
				//System.out.println(map.getNode(x,y).toString());
				this.addVertex(vertex);
				
				if (vertex.isHole())
					holes.add(vertex);
				
				if (vertex.pusher)
					pushers.add(vertex);
					
				if (vertex.isCompactor())
					compactors.add(vertex);
				
				if (vertex.conveyor)
					conveyors.add(vertex);
				
				if (vertex.laser)
					lasers.add(vertex);
				
			}
		}
		
		//Alle Felder, die nicht durch Wände getrennt sind, verbinden
		connectVertices();
			
		//Loecher verarbeiten
		loadHoles();
		
		//Compactors verarbeiten
		loadCompactors();
		
		//Zahnraeder verarbeiten
		loadCogwheels();
		
		//Pusher/Conveyor-Kantengewichte anpassen
		loadConveyors();
		
		//Pusher verarbeiten
		loadPushers();
								
		//Laser verarbeiten
		loadLasers();
		
	}	
	
	
	/**
	 * Creates a graph representation of a Roborally.MapObject
	 * @param map
	 */
	public void loadMap(String[][] nodeStrings){
		
		//Knoten erstellen fuer alle Felder auf der Karte
		for (int x = 0; x < nodeStrings.length; x++) {
			for (int y = 0; y < nodeStrings[x].length; y++) {
					
				G4_Vertex vertex = new G4_Vertex(x, y, nodeStrings[x][y]);
			
				this.addVertex(vertex);
				
				if (vertex.isHole())
					holes.add(vertex);
				
				if (vertex.pusher)
					pushers.add(vertex);
					
				if (vertex.isCompactor())
					compactors.add(vertex);
				
				if (vertex.conveyor)
					conveyors.add(vertex);
				
				if (vertex.laser)
					lasers.add(vertex);
				
			}
		}
		
		//Alle Felder, die nicht durch Wände getrennt sind, verbinden
		connectVertices();
			
		//Loecher verarbeiten
		loadHoles();
		
		//Compactors verarbeiten
		loadCompactors();
		
		//Zahnraeder verarbeiten
		loadCogwheels();
		
		//Pusher/Conveyor-Kantengewichte anpassen
		loadConveyors();
		
		//Pusher verarbeiten
		loadPushers();
								
		//Laser verarbeiten
		loadLasers();
		
	}	
	
	private void connectVertices(){
		
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
				
			}
		
	}

	private void loadConveyors(){

		//Alle ausgehenden Kanten bis auf die Kante in Pusher-Richtung entfernen
		for(G4_Vertex vertex: this.conveyors){

			//Kante zum "Drueberfahren" einrichten
			G4_Vertex vertexCW1 = this.getVertexInDirection(vertex, G4_DirectionUtils.turnCW(vertex.conveyorDirection)); 
			G4_Vertex vertexCCW1 = this.getVertexInDirection(vertex, G4_DirectionUtils.turnCCW(vertex.conveyorDirection));
			
			if (vertexCW1 != null && vertexCCW1 != null &&
					!vertexCW1.isHole() && !vertexCCW1.isHole() && 
					!vertexCW1.isCompactor() && !vertexCCW1.isCompactor() &&
					!vertexCW1.conveyor && !vertexCCW1.conveyor){
				//Lange Kanten einfuegen
				DefaultWeightedEdge hinEdge = this.addEdge(vertexCW1, vertexCCW1);
				this.setEdgeWeight(hinEdge, default2ConnWeight);
				DefaultWeightedEdge zuEdge = this.addEdge(vertexCCW1, vertexCW1);
				this.setEdgeWeight(zuEdge, default2ConnWeight);
				
			}			
			
			HashSet<DefaultWeightedEdge> outEdges = new HashSet<DefaultWeightedEdge>(this.outgoingEdgesOf(vertex));
			HashSet<DefaultWeightedEdge> inEdges = new HashSet<DefaultWeightedEdge>(this.incomingEdgesOf(vertex));
			
			//Falls in ein Loch, ins Aus oder auf ein Compactor geschoben wird
			// alle ausgehenden und eingehenden Kanten Gewichte erhoehen
			if (this.getVertexInDirection(vertex, vertex.conveyorDirection) == null ||
					this.getVertexInDirection(vertex, vertex.conveyorDirection).isHole() ||
					this.getVertexInDirection(vertex, vertex.conveyorDirection).isCompactor()){
				
				vertex.betterGetOffThatDamnThing = true;
				vertex.deathLiesInDirection = vertex.conveyorDirection;
				this.mostDangerous.add(vertex);
				
				
				for(DefaultWeightedEdge edge: outEdges){
					this.setEdgeWeight(edge, inHoleWeight);
				}
				
				for(DefaultWeightedEdge edge: inEdges){
					this.setEdgeWeight(edge, inHoleWeight);
				}
				
//				this.removeAllEdges(outEdges);
//				this.removeAllEdges(inEdges);
			}				
			//Im Normalfall
			else{
				for(DefaultWeightedEdge edge: outEdges){
					//Kanten in Translationsrichtung
					if (this.getDirectionOfEdge(edge) == vertex.conveyorDirection)
						this.setEdgeWeight(edge, withConveyorWeight);
					//Kanten entgegen Translationsrichtung
					else if (this.getDirectionOfEdge(edge) == G4_DirectionUtils.turnU(vertex.conveyorDirection))
						this.setEdgeWeight(edge, againstConveyorWeight);
				}
				
				for(DefaultWeightedEdge edge: inEdges){
					//Kanten in Translationsrichtung
					if (this.getDirectionOfEdge(edge) == vertex.conveyorDirection)
						this.setEdgeWeight(edge, withConveyorWeight);
					//Kanten entgegen Translationsrichtung
					else if (this.getDirectionOfEdge(edge) == G4_DirectionUtils.turnU(vertex.conveyorDirection))
						this.setEdgeWeight(edge, againstConveyorWeight);
				}
			}
			
			if (vertex.conveyorAndRotator){
				G4_Vertex vertex1 = this.getVertexInDirection(vertex, vertex.conveyorDirection);
				G4_Vertex vertex2 = this.getVertexInDirection(vertex1, vertex.conveyorDirection);
				if (vertex2 != null){
					DefaultWeightedEdge longEdge = this.addEdge(vertex, vertex2);
					this.setEdgeWeight(longEdge, withConveyorWeight);
				}
			}
					
		}
	}
	
	private void loadCogwheels(){
//		//DIAGONALKANTEN FUER ZAHNRAEDER EINFUEGEN
//		}else if (currentNode.toString().contains("counterclockwise")){
//			
//			try {
//				if (!vertexNorth.isHole() && !vertexEast.isHole() && currentVertex.isWallEast()){
//					this.addEdge(vertexNorth,vertexEast);
//					this.setEdgeWeight(this.getEdge(vertexNorth, vertexEast), rotatorConnWeight);
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//			}
//			try {
//				if (!vertexEast.isHole() && !vertexSouth.isHole() && currentVertex.isWallSouth()){
//					this.addEdge(vertexEast,vertexSouth);
//					this.setEdgeWeight(this.getEdge(vertexEast, vertexSouth), rotatorConnWeight);
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//			}
//			try {
//				if (!vertexSouth.isHole() && !vertexWest.isHole() && currentVertex.isWallWest()){
//					this.addEdge(vertexSouth,vertexWest);
//					this.setEdgeWeight(this.getEdge(vertexSouth, vertexWest), rotatorConnWeight);
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//			}
//			try {
//				if (!vertexWest.isHole() && !vertexNorth.isHole() && currentVertex.isWallNorth()){
//					this.addEdge(vertexWest,vertexNorth);
//					this.setEdgeWeight(this.getEdge(vertexWest, vertexNorth), rotatorConnWeight);
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//			}
//		}else if (currentNode.toString().contains("clockwise")){
//			try {
//				if (!vertexNorth.isHole() && !vertexWest.isHole() && currentVertex.isWallWest()){
//					this.addEdge(vertexNorth,vertexWest);
//					this.setEdgeWeight(this.getEdge(vertexNorth, vertexWest), rotatorConnWeight);
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//			}
//			try {
//				if (!vertexWest.isHole() && !vertexSouth.isHole() && currentVertex.isWallSouth()){
//					this.addEdge(vertexWest,vertexSouth);
//					this.setEdgeWeight(this.getEdge(vertexWest, vertexSouth), rotatorConnWeight);
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//			}
//			try {
//				if (!vertexSouth.isHole() && !vertexEast.isHole() && currentVertex.isWallEast()){
//					this.addEdge(vertexSouth,vertexEast);
//					this.setEdgeWeight(this.getEdge(vertexSouth, vertexEast), rotatorConnWeight);
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//			}
//			try {
//				if (!vertexEast.isHole() && !vertexNorth.isHole() && currentVertex.isWallNorth()){
//					this.addEdge(vertexEast,vertexNorth);
//					this.setEdgeWeight(this.getEdge(vertexEast, vertexNorth), rotatorConnWeight);
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();		
//		}
//		
	}
		
	/**
	 * Laedt Loecher auf der Karte.
	 * Entfernt alle ausgehenden Kanten.
	 * Setzt das Gewicht eingehender Kanten auf inHoleWeight
	 * Setzt das Gewicht aller eingehenden Kanten auf benachbarte Felder mit Translationseffekt
	 * in Richtung des Lochs auf inHoleWeight (und entfernt die ausgehenden Kanten, die nicht
	 * in Richtung des Lochs zeigen.)
	 *  
	 */
	private void loadHoles(){
		//Alle ausgehenden Kanten von Loechern entfernen
		for(G4_Vertex vertex: this.holes){
			HashSet<DefaultWeightedEdge> outEdges = new HashSet<DefaultWeightedEdge>(this.outgoingEdgesOf(vertex));
			this.removeAllEdges(outEdges);
			
			//Eingehende Kanten mit hohem Kantengewicht versehen
			HashSet<DefaultWeightedEdge> inEdges = new HashSet<DefaultWeightedEdge>(this.incomingEdgesOf(vertex));
			for(DefaultWeightedEdge inEdge: inEdges){
				this.setEdgeWeight(inEdge, inHoleWeight);
			}

			//Eingangskanten von Conveyors und Pushers die ins Loch schieben erhöhen
			for (Direction dir: Direction.getDirectionsNESW()){
				G4_Vertex dirVertex = this.getVertexInDirection(vertex, dir);
				if (this.conveyors.contains(dirVertex)){
					if (dirVertex.conveyorDirection == G4_DirectionUtils.turnU(dir)){
						for(DefaultWeightedEdge inEdge: this.incomingEdgesOf(dirVertex)){
							this.setEdgeWeight(inEdge, inHoleWeight);
						}
					}
				}	
				if (this.pushers.contains(dirVertex)){
					if (dirVertex.pusherDirection == G4_DirectionUtils.turnU(dir)){
						for(DefaultWeightedEdge inEdge: this.incomingEdgesOf(dirVertex)){
							this.setEdgeWeight(inEdge, inHoleWeight);
						}
					}
				}	
				
			}
			
			
			
		}		
	}
		
	private void loadLasers(){
		//Beschossene Positionen markieren
		for(G4_Vertex currentVertex: this.lasers){
			//Laser verarbeiten
			for(Direction dir: currentVertex.shotAtFromDirections){
				this.setLaserPositions(currentVertex, dir);
			}
		}
		
		//Bei Beschossenen Positionen eingehende-Kanten-Gewichte erhöhen
		for(G4_Vertex currentVertex: this.vertexSet()){
			if (currentVertex.getEffect().effectOnHealth < 0){
				//Alle eingehende Kanten
				for (DefaultWeightedEdge inEdge: this.incomingEdgesOf(currentVertex)){
					this.setEdgeWeight(inEdge, inLaserWeight);
				}
			}
		}
		//Bei Beschossenen Positionen ausgehenden-Kanten-Gewichte erhöhen
		for(G4_Vertex currentVertex: this.vertexSet()){
			if (currentVertex.getEffect().effectOnHealth < 0){
				for (DefaultWeightedEdge outEdge: this.outgoingEdgesOf(currentVertex)){
					//Kanten die in der Schusslinie laufen extrem hohe Gewichte geben
					if (currentVertex.shotAtFromDirections.contains(this.getDirectionOfEdge(outEdge)) ||
							currentVertex.shotAtFromDirections.contains(G4_DirectionUtils.turnU(this.getDirectionOfEdge(outEdge))))
						this.setEdgeWeight(outEdge, stayInLaserWeight);
					else
						this.setEdgeWeight(outEdge, outLaserWeight);
				}
				
			}
		}
	}
	
	/**
	 * Erzeugt Pusher im Grpahen
	 */
	private void loadPushers(){
		
		//############  PUSHER REPRAESENTIEREN ########################################
		//Lange Kanten einrichten		
		for(G4_Vertex vertex: this.pushers){
			
			G4_Vertex vertexCW1 = this.getVertexInDirection(vertex, G4_DirectionUtils.turnCW(vertex.pusherDirection));
			G4_Vertex vertexCW2 = this.getVertexInDirection(vertexCW1, G4_DirectionUtils.turnCW(vertex.pusherDirection));
			G4_Vertex vertexCCW1 = this.getVertexInDirection(vertex, G4_DirectionUtils.turnCCW(vertex.pusherDirection));
			G4_Vertex vertexCCW2 = this.getVertexInDirection(vertexCCW1, G4_DirectionUtils.turnCCW(vertex.pusherDirection));
			
			//Lange Kanten einrichten			
			try {
				if (!vertexCW1.pusher){
					//Feld davor ist kein Pusher
					//Lange "HIN" - Kante über das Pusher-Feld einfuegen
					try {
						DefaultWeightedEdge longEdge = this.addEdge(vertexCW1, vertexCCW1);
						this.setEdgeWeight(longEdge, default2ConnWeight);

						if (vertexCCW1.pusher){
							//Feld danach ist auch Pusher
							//Lange "HIN" - Kante über beide Pusher-Felder einfuegen
							DefaultWeightedEdge longerEdge = this.addEdge(vertexCW1, vertexCCW2);
							this.setEdgeWeight(longerEdge, default3ConnWeight);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			try {
				if (!vertexCCW1.pusher){
					//Feld danach ist kein Pusher
					//Lange "ZURUECK" - Kante über das Pusher-Feld einfuegen
					try {
						DefaultWeightedEdge longEdge = this.addEdge(vertexCCW1, vertexCW1);
						this.setEdgeWeight(longEdge, default2ConnWeight);

						if (vertexCW1.pusher){
							//Feld davor ist auch Pusher
							//Lange "ZURUECK" - Kante über beide Pusher-Felder einfuegen
							DefaultWeightedEdge longerEdge = this.addEdge(vertexCCW1, vertexCW2);
							this.setEdgeWeight(longerEdge, default3ConnWeight);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
		//Alle ausgehenden Kanten bis auf die Kante in Pusher-Richtung entfernen
		for(G4_Vertex vertex: this.pushers){

			HashSet<DefaultWeightedEdge> outEdges = new HashSet<DefaultWeightedEdge>(this.outgoingEdgesOf(vertex));
			HashSet<DefaultWeightedEdge> inEdges = new HashSet<DefaultWeightedEdge>(this.incomingEdgesOf(vertex));

			//Falls in ein Loch, ins Aus oder auf ein Compactor geschoben wird
			// ALLE ausgehenden und eingehenden Kanten entfernen
			if (this.getVertexInDirection(vertex, vertex.pusherDirection) == null ||
					this.getVertexInDirection(vertex, vertex.pusherDirection).isHole() ||
					this.getVertexInDirection(vertex, vertex.pusherDirection).isCompactor()){
				
				vertex.betterGetOffThatDamnThing = true;
				vertex.deathLiesInDirection = vertex.pusherDirection;
				this.mostDangerous.add(vertex);
				
				for(DefaultWeightedEdge edge: outEdges){
					this.setEdgeWeight(edge, inHoleWeight);
				}
				
				for(DefaultWeightedEdge edge: inEdges){
					this.setEdgeWeight(edge, inHoleWeight);
				}
			//Im Normalfall
			}else{
				for(DefaultWeightedEdge edge: outEdges){
					if (this.getDirectionOfEdge(edge) != vertex.pusherDirection)
						this.removeEdge(edge);
					else
						this.setEdgeWeight(edge,0);
				}
			}
		}
		//############ ENDE PUSHER REPRAESENTIEREN ########################################
		
			
	}
		
	private void loadCompactors(){
		//LANGE Kanten einfuegen
		for(G4_Vertex vertex: this.compactors){
			G4_Vertex vertexNorth = this.getVertexInDirection(vertex, Direction.NORTH);
			G4_Vertex vertexEast = this.getVertexInDirection(vertex, Direction.EAST);
			G4_Vertex vertexSouth = this.getVertexInDirection(vertex, Direction.SOUTH);
			G4_Vertex vertexWest = this.getVertexInDirection(vertex, Direction.WEST);
			
			try {
				DefaultWeightedEdge edgeNS = this.addEdge(vertexNorth, vertexSouth);
				this.setEdgeWeight(edgeNS, default2ConnWeight);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				DefaultWeightedEdge edgeSN = this.addEdge(vertexSouth, vertexNorth);
				this.setEdgeWeight(edgeSN, default2ConnWeight);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				DefaultWeightedEdge edgeEW = this.addEdge(vertexEast, vertexWest);
				this.setEdgeWeight(edgeEW, default2ConnWeight);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				DefaultWeightedEdge edgeWE = this.addEdge(vertexWest, vertexEast);
				this.setEdgeWeight(edgeWE, default2ConnWeight);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
		
		
		//Alle ausgehenden Kanten entfernen
		for(G4_Vertex vertex: this.compactors){
			HashSet<DefaultWeightedEdge> outEdges = new HashSet<DefaultWeightedEdge>(this.outgoingEdgesOf(vertex));
			this.removeAllEdges(outEdges);
			//Eingehende Kantengewichte erhoehen
			HashSet<DefaultWeightedEdge> inEdges = new HashSet<DefaultWeightedEdge>(this.incomingEdgesOf(vertex));
			for(DefaultWeightedEdge inEdge: inEdges){
				this.setEdgeWeight(inEdge, inCompactorWeight);
			}
			
		}		
	} 
	
	
	//von Qi
	//Fuegt Grenzknoten in Graphen hin 
	private void loadGrenzknoten(){
		for (int x = 0; x < this.nodeStrings.length; x++)
		{
			for (int y=0;y<this.nodeStrings[x].length;y++)
			{
				if(nodeStrings[x+1][y]==null)
				{
					this.grenzKnoten.add(this.getVertex(x,y));
					continue;
				}
				
				if(nodeStrings[x-1][y]==null)
				{
					this.grenzKnoten.add(this.getVertex(x,y));
					continue;
				}
				
				if(nodeStrings[x][y+1]==null)
				{
					this.grenzKnoten.add(this.getVertex(x,y));
					continue;
				}
				
				if(nodeStrings[x][y-1]==null)
				{
					this.grenzKnoten.add(this.getVertex(x,y));
					continue;
				}
			}
		}
	}
	
	/**
	 * Adjusts the edge weights around the given position so that moving in the given direction
	 * (at most 3 vertices) is less expensive than moving into the other directions
	 * @param vertex 
	 * @param direction
	 * @return A G4_GraphMap with adjusted weights around the given position
	 */
	private G4_GraphMap getAdaptedGraph(G4_Vertex vertex, Direction direction, boolean remove2Edges, boolean remove3Edges){

		G4_GraphMap returnGraph = new G4_GraphMap(this.rrMap,this.startPosition);
		
		//Bei Bedarf 2er und 3er Kanten entfernen
		for(DefaultWeightedEdge edge: this.edgeSet()){
			if (this.getLengthOfEdge(edge) == 2 && remove2Edges){
				returnGraph.removeEdge(edge);
				//returnGraph.setEdgeWeight(edge, this.stayInLaserWeight);
			}
			if (this.getLengthOfEdge(edge) == 3 && remove3Edges){
				returnGraph.removeEdge(edge);
				//returnGraph.setEdgeWeight(edge, this.stayInLaserWeight);
			}
		}
		
		for(G4_Vertex dangerVertex: this.mostDangerous){
			HashSet<DefaultWeightedEdge> dangerEdges = new HashSet<DefaultWeightedEdge>(this.incomingEdgesOf(dangerVertex));
			returnGraph.removeAllEdges(dangerEdges);
		}
				
		HashSet<DefaultWeightedEdge> edges = new HashSet<DefaultWeightedEdge>();
		HashSet<DefaultWeightedEdge> edges2 =  new HashSet<DefaultWeightedEdge>();
		HashSet<DefaultWeightedEdge> edges3 =  new HashSet<DefaultWeightedEdge>();
		
		edges = returnGraph.getOutgoingEdgesInDirection(vertex, direction);
		
		for( DefaultWeightedEdge edge: edges){
			returnGraph.setEdgeWeight(edge, returnGraph.getEdgeWeight(edge) - 1);
			edges2.addAll(returnGraph.getOutgoingEdgesInDirection(returnGraph.getEdgeTarget(edge), direction));
		}
		
		for( DefaultWeightedEdge edge: edges2){
			returnGraph.setEdgeWeight(edge, returnGraph.getEdgeWeight(edge) - 1);
			edges3.addAll(returnGraph.getOutgoingEdgesInDirection(returnGraph.getEdgeTarget(edge), direction));
		}
		
		for( DefaultWeightedEdge edge: edges3){
			returnGraph.setEdgeWeight(edge, returnGraph.getEdgeWeight(edge) - 1);
		}
		
		return returnGraph; 
	}
	
	
		
	public HashSet<DefaultWeightedEdge> getOutgoingEdgesInDirection(G4_Vertex vertex, Direction direction){
		HashSet<DefaultWeightedEdge> returnEdges =  new HashSet<DefaultWeightedEdge>(this.outgoingEdgesOf(vertex));
		HashSet<DefaultWeightedEdge> outEdges =  new HashSet<DefaultWeightedEdge>(this.outgoingEdgesOf(vertex));
		
		for (DefaultWeightedEdge edge: outEdges){
			if (this.getDirectionOfEdge(edge) != direction)
				returnEdges.remove(edge);
		}
		
		return returnEdges;
	}
	
	/**
	 * Calculates Dijkstras shortest Path from the given Start-Position to the given End-Position
	 * @param startPosition
	 * @return The edges on the shortest path
	 */
	public List<DefaultWeightedEdge> getEdgesOnShortestPath(G4_Position startPosition, G4_Position endPosition, 
			boolean remove2edges, boolean remove3edges){
		
		G4_Vertex startVertex = new G4_Vertex(startPosition.x,startPosition.y,"");
		G4_Vertex endVertex = new G4_Vertex(endPosition.x, endPosition.y, "");
		
		try {
			//Kuerzesten Weg von momentaner Position zum Checkpoint bestimmen
			return DijkstraShortestPath.findPathBetween(
					this.getAdaptedGraph(startVertex, startPosition.getDirection(), remove2edges, remove3edges)
					//this
					, startVertex, endVertex);
			
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
	public double getLengthOfShortestPath_OLD(G4_Position startPosition, G4_Position endPosition){
		
		G4_Vertex startVertex = new G4_Vertex(startPosition.x, startPosition.y, "");
		G4_Vertex endVertex = new G4_Vertex(endPosition.x, endPosition.y, "");
		
		 //Kuerzesten Weg von momentaner Position zum Checkpoint bestimmen
		 DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge> shortestPath = 
			 new DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge>(
					 this.getAdaptedGraph(startVertex,startPosition.getDirection(),true,true), 
					 //this, 
					 startVertex, 
					 endVertex);
		 return shortestPath.getPathLength();
	}
	
	/**
	 * Calculates Dijkstras shortest Path from the given Start-Position to the given End-Position
	 * @param startPosition
	 * @return The length of the shortest path
	 */
	public double getLengthOfShortestPath(G4_Position startPosition, G4_Position endPosition, 
			boolean remove2edges, boolean remove3edges){
		
		G4_Vertex startVertex = new G4_Vertex(startPosition.x, startPosition.y, "");
		G4_Vertex endVertex = new G4_Vertex(endPosition.x, endPosition.y, "");
		
		 //Kuerzesten Weg von momentaner Position zum Checkpoint bestimmen
		 DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge> shortestPath = 
			 new DijkstraShortestPath<G4_Vertex, DefaultWeightedEdge>(
					 this.getAdaptedGraph(startVertex,startPosition.getDirection(), remove2edges, remove3edges), 
					 //this, 
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
		
		if (direction.equals(Constants.DIRECTION_STAY) || direction.equals(Constants.DIRECTION_NONE)){
			return vertex;
		}
		
		G4_Vertex nextVertex = new G4_Vertex(vertex);
		
		if (direction.equals(Constants.DIRECTION_NORTH)){
			if (!vertex.isWallNorth())
				nextVertex.setY(vertex.getY() - 1);
		}
		else if (direction.equals(Constants.DIRECTION_EAST)){
			if (!vertex.isWallEast())
			nextVertex.setX(vertex.getX() + 1);
		}
		else if (direction.equals(Constants.DIRECTION_SOUTH)){
			if (!vertex.isWallSouth())
			nextVertex.setY(vertex.getY() + 1);
		}
		else if (direction.equals(Constants.DIRECTION_WEST)){
			if (!vertex.isWallWest())
			nextVertex.setX(vertex.getX() - 1);
		}
		
		if (this.getEdge(vertex, nextVertex) != null  || vertex.equals(nextVertex)){
			return this.getVertex(nextVertex.getX(),nextVertex.getY());
		}
		else{
			return null;
		}
	}
	
	public G4_Vertex getVertexInDirectionIgnoringEdges(G4_Vertex vertex, Direction direction){
		
		//HIER LAG EIN PROBLEM!!!
		if (vertex == null)
			return null;
		
		if (direction.equals(Constants.DIRECTION_STAY) || direction.equals(Constants.DIRECTION_NONE)){
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
		
		if (this.vertexSet().contains(nextVertex)){
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
	
	/**
	 * Returns the length of the given edge
	 * @param edge
	 * @return the length of the edge as an Integer
	 */
	public int getLengthOfEdge(DefaultWeightedEdge edge){
		
		G4_Vertex source = this.getEdgeSource(edge);
		G4_Vertex target = this.getEdgeTarget(edge);
		int length = this.defaultConnWeight;
		
		 //--------- ENTFERNUNG DES NAECHSTEN KNOTEN BESTIMMEN -----------------------------------------
		
		 // x-Position bleibt gleich
		 if (source.getX() == target.getX()){
			 return Math.abs(source.getY() - target.getY());
		 }
		 // y-Position bleibt gleich
		 else if (source.getY() == target.getY()){
			return Math.abs(source.getX() - target.getX());
		 }
				
		return length;
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
			double pathLength = this.getLengthOfShortestPath_OLD(startPosition, robotPosition);
			
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
	 * "Walks" from a position into the given direction until a wall is hit
	 * and sets the effectOnHealth 
	 * @param startVertex
	 * @param direction
	 */
	private void setLaserPositions(G4_Vertex startVertex, Direction direction){
		
		G4_Vertex currentVertex = startVertex;
		currentVertex.getEffect().effectOnHealth--;

		while (currentVertex != null && !currentVertex.isWallinDirection(direction)){
			
			try {	
				currentVertex = this.getVertexInDirectionIgnoringEdges(currentVertex, direction);
				currentVertex.getEffect().effectOnHealth--;
				currentVertex.shotAtFromDirections.add(direction);
			
			} catch (Exception e) {
				//e.printStackTrace();
			}					
		}


		
	}
	
}


