import java.util.List;
import java.util.Vector;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import roborally.task.Card;
import roborally.task.Constants;
import roborally.task.Direction;
import roborally.task.Constants.CardType;

/**
 * Versucht rekursiv die Karten so zu waehlen, dass nach fuenf Karten der Abstand zum Ziel minimal ist.
 * 
 *
 */
public class G4_CardChooser {
	
	public boolean debugOutput;

	public G4_GraphMap graphMap;
	public G4_GraphMapBall graphMapBall;
	
	private Vector<Card> cards = new Vector<Card>();
	private Vector<Card> chosenCards = new Vector<Card>();
	
	private G4_Position myPosition;

	private boolean checkpointReached = false;
	public int moeglichkeitenCount = 0;
	private int cardsToCheckpoint = 5;

	private int attackCardNr;
	private int moveCardNr;

	private double shortestLength = 1000; 
	private Vector<Card> movingCards = new Vector<Card>(); 

	private int mostAttackPositions = 0;
	private Vector<Card> attackingCards = new Vector<Card>(); 
	
	public G4_CardChooser(G4_GraphMap map, Card[] useableCards, G4_Position position, int AttackCardNr, int moveCardNr) {
		this.graphMap = map;
		this.myPosition = position;
		this.moveCardNr = moveCardNr;
		this.attackCardNr = AttackCardNr;

		for (Card c : useableCards){
			this.cards.add(c);	
		}		
	}
	
	public Vector<Card> chooseMovingCards2(G4_Position start, G4_Position ziel){
		
		//Falls Zielposition erreicht oder 5 Karten gewählt sind, abbrechen!
		if (start.equals(ziel) || this.chosenCards.size() >= 5){
			return chosenCards;
		}
		if (this.debugOutput)
			System.out.println("CHOOSE CARDS: " + start + " -> " + ziel);
		
		//Koennen wir 2 Vorwaerts und 3 Vorwaerts Kanten gehen???
		boolean remove2edges = false;
		boolean remove3edges = false;
		if (!this.handContainsCardType(Constants.CardType.Move_Three_Forward_Card))
			remove3edges = true;
		if (!this.handContainsCardType(Constants.CardType.Move_Two_Forward_Card))
			remove2edges = true;
		
		//Wenn wir in ein Loch oder aehnliches geraten sind, zurueck auf "die" Startposition
		if (this.graphMap.getLengthOfShortestPath(start, ziel, remove2edges, remove3edges) == Double.POSITIVE_INFINITY){
			if (this.debugOutput)
				System.out.println("Wir sind gerade in ein Loch oder sowas gefahren!");
			start = this.graphMap.startPosition;
		}
		
		//Kuerzester Weg
		List<DefaultWeightedEdge> path = this.graphMap.getEdgesOnShortestPath(start, ziel, remove2edges, remove3edges);
		if (this.debugOutput)
			System.out.println("NAECHSTE KANTE: " + path.get(0).toString());		
		
		//Anzahl bereits gewaehlter Karten
		int countChosenCards = chosenCards.size();
		
		//Wenn wir auf einem "Todesknoten" stehen
		G4_Vertex startVertex = this.graphMap.getVertex(start.x, start.y);
	
//		if (startVertex.betterGetOffThatDamnThing){
//			//AAAAAH wir gucken genau in den Abgrund!!!! ZURUECKRUDERN
//			if (start.getDirection() == startVertex.deathLiesInDirection){
//				this.tryChoosingCard(Constants.CardType.Move_Backward_Card, false);
//			}
//			//Vielleicht kommen wir ja noch weg, hoffentlich fahren wir nicht woanders rein
//			else if (start.getDirection() ==  G4_DirectionUtils.turnU(startVertex.deathLiesInDirection)){
//				this.tryChoosingCard(Constants.CardType.Move_Three_Forward_Card, false);
//			}
//			else{
//				//Irgendwie runter davon...
//				if (this.graphMap.getOutgoingEdgesInDirection(startVertex, start.getDirection()).size() > 0)
//					this.tryChoosingCard(Constants.CardType.Move_Forward_Card, false);
//				else
//					this.tryChoosingCard(Constants.CardType.Move_Backward_Card, false);
//					
//			}
//	else	}
		
		//Wenn der richtige Knoten erreicht ist, nur noch drehen
		 if (path.size() == 0){
			if (ziel.getDirection() == G4_DirectionUtils.turnCW(start.getDirection())){
				this.tryChoosingCard(Constants.CardType.Rotate_CW_Card,  false);
			}
			else if (ziel.getDirection() == G4_DirectionUtils.turnCCW(start.getDirection())){
				this.tryChoosingCard(Constants.CardType.Rotate_CCW_Card, false);
			}
			else if (ziel.getDirection() == G4_DirectionUtils.turnU(start.getDirection())){
				this.tryChoosingCard(Constants.CardType.U_Turn_Card, false);
			}			
		}
		
		//Der richtige Knoten ist noch NICHT erreicht
		else{
			// Wir schauen nicht in die richtige Richtung
			if (this.graphMap.getDirectionOfEdge(path.get(0)) != start.getDirection()){
				if (this.graphMap.getDirectionOfEdge(path.get(0)) == G4_DirectionUtils.turnCW(start.getDirection())){
					if (!this.graphMap.getEdgeSource(path.get(0)).cogwheelCCW && !this.graphMap.getEdgeSource(path.get(0)).cogwheelCW)
						this.tryChoosingCard(Constants.CardType.Rotate_CW_Card, false);
				}
				else if (this.graphMap.getDirectionOfEdge(path.get(0)) == G4_DirectionUtils.turnCCW(start.getDirection())){
					if (!this.graphMap.getEdgeSource(path.get(0)).cogwheelCCW && !this.graphMap.getEdgeSource(path.get(0)).cogwheelCW)
						this.tryChoosingCard(Constants.CardType.Rotate_CCW_Card , false);
				}
				else if (this.graphMap.getDirectionOfEdge(path.get(0)) == G4_DirectionUtils.turnU(start.getDirection())){
					if (this.graphMap.getLengthOfEdge(path.get(0)) == 2){
						//Wenns nur ein Feld weit in U-Turn Richtung gefahren werden muss 
						if (path.size() == 1 || ( path.size() > 1 && this.graphMap.getDirectionOfEdge(path.get(1)) != G4_DirectionUtils.turnU(start.getDirection()))){
							this.tryChoosingCard(Constants.CardType.Move_Backward_Card, false);
						}
						//oder wir auf einem Zahnrad sind
						else if (this.graphMap.getEdgeSource(path.get(0)).cogwheelCCW || this.graphMap.getEdgeSource(path.get(0)).cogwheelCW){
							this.tryChoosingCard(Constants.CardType.Move_Backward_Card, true);
						}	
					}
					else
						this.tryChoosingCard(Constants.CardType.U_Turn_Card, false );
				}
			}
			//Wir schauen in die richtige RIchtung
			else{
				//Naechste Kante geht ueber 3 Knoten
				if (this.graphMap.getLengthOfEdge(path.get(0)) == 3){
					this.tryChoosingCard(Constants.CardType.Move_Three_Forward_Card, true);
				}
				//Naechste Kante geht uber 2 Knoten
				else if(this.graphMap.getLengthOfEdge(path.get(0)) == 2){
					//Ueber-Naechste Kante geht NICHT in die gleiche Richtung
					if (path.size() > 1 && this.graphMap.getDirectionOfEdge(path.get(1)) != start.getDirection()){
						this.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card , true);
					}
					//Ueber-Naechste Kante geht in die gleiche Richtung
					else{
						//Ueber-Naechste Kante geht ueber 1 Knoten
						if (path.size() > 1 &&  this.graphMap.getLengthOfEdge(path.get(1)) == 1){
							this.tryChoosingCard(Constants.CardType.Move_Three_Forward_Card, false);
						}
						//Ueber-Naechste Kante geht ueber mehr als 1 Knoten
						else{
							this.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card, true);
						}
					}
				}
				//Naechste Kante geht ueber 1 Knoten
				else{
					//Ueber-Naechste Kante existiert NICHT
					if (path.size() < 2){
						this.tryChoosingCard(Constants.CardType.Move_Forward_Card, false);
					}
					//Ueber-Naechste Kante EXISTIERT
					else{
						//Ueber-Naechste Kante geht NICHT in die gleiche Richtung
						if (this.graphMap.getDirectionOfEdge(path.get(1)) != start.getDirection()){
							this.tryChoosingCard(Constants.CardType.Move_Forward_Card, false);
						}
						//Ueber-Naechste Kante geht in die gleiche Richtung
						else{
							//Ueber-Naechste Kante geht ueber 3 Knoten
							if (this.graphMap.getLengthOfEdge(path.get(1)) == 3){
								this.tryChoosingCard(Constants.CardType.Move_Forward_Card, true);
							}
							//Ueber-Naechste Kante geht ueber 2 Knoten
							else if (this.graphMap.getLengthOfEdge(path.get(1)) == 2){
								this.tryChoosingCard(Constants.CardType.Move_Three_Forward_Card, true);
							}
							//Ueber-Naechste Kante geht ueber 1 Knoten
							else if (this.graphMap.getLengthOfEdge(path.get(1)) == 1){

								//Ueber-Ueber-Naechste Kante existiert NICHT
								if (path.size() < 3){
									this.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card, false);	
								}
								else{
									//Ueber-Ueber-Naechste Kante geht NICHT in die gleiche Richtung
									if (this.graphMap.getDirectionOfEdge(path.get(2)) != start.getDirection()){
										this.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card, false);	
									}
									//Ueber-Ueber-Naechste Kante geht in die gleiche Richtung							
									else{
										//Ueber-Ueber-Naechste Kante geht ueber 1 Knoten
										if (this.graphMap.getLengthOfEdge(path.get(2)) == 1){
											this.tryChoosingCard(Constants.CardType.Move_Three_Forward_Card, false);	
										}
										//Ueber-Ueber-Naechste Kante geht ueber MEHR als 1 Knoten
										else{
											this.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card, false);	
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		if (this.debugOutput) System.out.println("Vor Karten-Effekt: " + start.toString());
		
		//Wenn eine Karte gewaehlt wurde
		if (countChosenCards < chosenCards.size()){
			//Karteneffekt und Knoteneffekt anwenden
			start = this.applyCardEffect(chosenCards.lastElement(), start);
		}else{
			if (this.graphMap.getEdgeSource(path.get(0)).cogwheelCCW || this.graphMap.getEdgeSource(path.get(0)).cogwheelCW){
				this.tryChoosingCard(Constants.CardType.U_Turn_Card, false );
				start = this.applyCardEffect(chosenCards.lastElement(), start);
			}else{
				System.out.println("Keine passende Karte gefunden");
				return null;
			}
		}
		if (this.debugOutput) System.out.println("Nach Karten-Effekt: " + start.toString());		
		if (this.debugOutput) System.out.println("Vor Vertex-Effekt: " + start.toString());		
		
		start = this.applyVertexEffects(start);
		
		if (this.debugOutput) System.out.println("Nach Vertex-Effekt: " + start.toString());		
		
		//Rekursiver Aufruf 
		return this.chooseMovingCards2(start, ziel);
	}

	public Vector<Card> choosePushingCards(G4_Position robotPosition, G4_Position ballEndPosition){
		
		//G4_Position start = this.graphMapBall.getPositionOfBall(); 
	
		//Falls Zielposition erreicht oder 5 Karten gewählt sind, abbrechen!
		if (this.graphMapBall.getPositionOfBall().equals(ballEndPosition) || this.chosenCards.size() >= 5){
			return chosenCards;
		}
		
		//Kuerzester Weg
		List<DefaultWeightedEdge> path = this.graphMapBall.getEdgesOnShortestPath(this.graphMapBall.getPositionOfBall(), ballEndPosition);
		Direction pushDirection = this.graphMapBall.getDirectionOfEdge(path.get(0));
	
		while (pushDirection == this.graphMapBall.getDirectionOfEdge(path.get(0))){
			
			//Bereits gewaehlte Karten
			int countChosenCards = chosenCards.size();
			
			//Wenn der richtige Knoten erreicht ist oder genug Karten gewaehlt
			if (path.size() == 0 || this.chosenCards.size() >= 5){
				break;
			}			
			//Ansonsten so schnell wie moeglich geradeaus laufen
			else if (path.size() >= 3 &&
				this.graphMapBall.getDirectionOfEdge(path.get(0)) == robotPosition.getDirection() &&
				this.graphMapBall.getDirectionOfEdge(path.get(1)) == robotPosition.getDirection() &&
				this.graphMapBall.getDirectionOfEdge(path.get(2)) == robotPosition.getDirection()){
						this.tryChoosingCard(Constants.CardType.Move_Three_Forward_Card, false);
			}
			else if (path.size() >= 2 &&
					 this.graphMapBall.getDirectionOfEdge(path.get(0)) == robotPosition.getDirection() &&
					 this.graphMapBall.getDirectionOfEdge(path.get(1)) == robotPosition.getDirection()){
					this.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card, false);
					}
			else if (this.graphMapBall.getDirectionOfEdge(path.get(0)) == robotPosition.getDirection()){
				this.tryChoosingCard(Constants.CardType.Move_Forward_Card, false);
			}	
			
			//Wenn eine Karte gewaehlt wurde
			if (countChosenCards < chosenCards.size()){
				
				//Karteneffekt und Knoteneffekt auf Roboter anwenden
				robotPosition = this.applyCardEffect(chosenCards.lastElement(), robotPosition);
				robotPosition = this.applyVertexEffects(robotPosition);
				
				//Richtung aendern damit Karten/Knoteneffekt richtig wirkt
				this.graphMapBall.getPositionOfBall().setDirection(pushDirection);
				//Position des Balls aktualisieren
				this.graphMapBall.setPositionOfBall(this.applyCardEffect(chosenCards.lastElement(),
																		this.graphMapBall.getPositionOfBall()));
				this.graphMapBall.setPositionOfBall(this.applyVertexEffects(this.graphMapBall.getPositionOfBall()));
							
			}else{
				System.out.println("Keine passende Karte gefunden");
				break;
			}

			//PFad neu berechnen
			path = this.graphMapBall.getEdgesOnShortestPath(this.graphMapBall.getPositionOfBall(), ballEndPosition);		
			//Wenn der richtige Knoten erreicht ist oder genug Karten gewaehlt
			if (path.size() == 0 || this.chosenCards.size() >= 5){
				break;
			}	
		}
		
		return chosenCards;
	}
	
	public Card[] chooseCards(G4_Position Ziel){

		Card[] myTurn = {null,null,null,null,null};
		int numCards = 0;	

		System.out.println(this.myPosition.toString());

		//Falls angegriffen werden soll, versuche anzugreifen!!!
		if (this.attackCardNr > 0){
			
			chooseAttackCards(this.myPosition, this.cards, new Vector<Card>(), 0);
			System.out.println("ATTACKE! :" + this.mostAttackPositions);

			//Alle gewaehlten Angriffskarten zurueckgeben
			for (Card c : this.attackingCards ){
				//Bereits gewaehlte Karten aus der Hand entfernen
				this.cards.remove(c);
				myTurn[numCards] = c;
				numCards++;
				
				//Position anpassen
				this.myPosition = this.applyCardEffect(c, myPosition);
				this.myPosition = this.applyVertexEffects(myPosition);

				System.out.println(c.getCardTypeString() + "  ID = " + c.hashCode());

			}	
		}

		//Die restlichen Karten fuer Bewegung zum Feind oder Ziel
		//this.moveCardNr = 5-numCards;

		//wenn angegriffen werden soll muss man zum gegner fahren			
		G4_Position nextEnemy = this.graphMap.getNextEnemy(myPosition);
		if (nextEnemy != null){
			//Falls Feinde auf der Karte
			chooseMovingCards(myPosition, nextEnemy, this.cards, new Vector<Card>());
		}
		else{
			//Ansonsten versuch zum Checkpoint zu fahren
			//if (this.graphMap.checkpoint != null){
			//	G4_Position checkpoint = new G4_Position(this.graphMap.checkpoint.getX(),this.graphMap.checkpoint.getY(),Constants.DIRECTION_STAY);
				chooseMovingCards(myPosition, Ziel, this.cards, new Vector<Card>());
			//}
		}

		//Alle gewaehlten Bewegungskarten zurueckgeben
		for (Card c : this.movingCards ){
			//Bereits gewaehlte Karten aus der Hand entfernen
			this.cards.remove(c);
			myTurn[numCards] = c;
			numCards++;
			
			//Position anpassen
			this.myPosition = this.applyCardEffect(c, myPosition);
			this.myPosition = this.applyVertexEffects(myPosition);
			
		}

		//Noch Karten uebrig? Rotationen spielen
		while (numCards<5){
			for (Card c: this.cards){
				if (c.getCardType() == Constants.CardType.Rotate_CW_Card){
//					||
//						c.getCardType() == Constants.CardType.Rotate_CCW_Card ||
//						c.getCardType() == Constants.CardType.U_Turn_Card){
					myTurn[numCards] = c;
					numCards++;
					this.cards.remove(c);
					break;
				}
			}
		}

		System.out.println("Gewählte Karten: " );
		for (Card crd: myTurn){
			System.out.println(crd.getCardTypeString() + "  ID = " + crd.hashCode());

		}
		System.out.println(this.myPosition.toString());

		if (myTurn[0] == null)
			System.out.println("NULLWARNUNG-----------------------------------");

		return myTurn;

	} 

	private void chooseAttackCards(G4_Position position, Vector<Card> useableCards, Vector<Card> chosenCards, int attackPositions){

		//Nur verschiedene Kartentype betrachten
		for (Card c: getDistinctUseableCards(useableCards)){

			G4_Position currentPosition = new G4_Position(position.x,position.y,position.getDirection());

			//#### Effekt der ausgespielten Karte anwenden #################
			currentPosition = this.applyCardEffect(c, currentPosition);

			//Falls Position nicht existent, Karte verwerfen
			if (currentPosition == null) 
				continue;
			
			//#### Effekt des Spielfelds auf Position berechnen ############
			currentPosition = this.applyVertexEffects(currentPosition);
		
			//Falls Position nicht existent, Karte verwerfen
			if (currentPosition == null) 
				continue;

			//Karte aus dem Vector der noch verfuegbaren Karten entfernen
			Vector<Card> stillUseableCards = new Vector<Card>(useableCards);
			stillUseableCards.remove(c);

			//Karte der Auswahl hinzufuegen		
			chosenCards.add(c);

			G4_Vertex vertexCurrent = this.graphMap.getVertex(currentPosition.x,currentPosition.y);
			Direction direction = currentPosition.getDirection();
			
			int newAttackPositions = attackPositions;
			if (vertexCurrent.isShootingDirection(direction)){
				newAttackPositions++; 
				//this.chooseAttackCards(newPosition, stillUseableCards, chosenCards, newAttackPositions);
				//chosenCards.remove(chosenCards.lastIndexOf(c));
			}

			//Wenn Kartenlimit errreicht, auswerten wie gut die Wahl war
			if ((chosenCards.size() == this.attackCardNr)){

				if (newAttackPositions > this.mostAttackPositions){

					this.mostAttackPositions = newAttackPositions;
					this.attackingCards = new Vector<Card>(chosenCards);


				}			
				//Letzte gewaehlte Karte zuruecknehmen
				chosenCards.remove(chosenCards.lastIndexOf(c));
			}
			else {
				if(chosenCards.size() < this.attackCardNr) {
					//REKURSION
					this.chooseAttackCards(currentPosition, stillUseableCards, chosenCards, newAttackPositions);
					//Letzte gewaehlte Karte zuruecknehmen
					chosenCards.remove(chosenCards.lastIndexOf(c));
				}
			}

		}

	}

	private void chooseMovingCards(G4_Position startPosition,G4_Position endPosition, Vector<Card> useableCards, Vector<Card> chosenCards){
		
		//if (!this.checkpointReached){
			//Falls CHeckpoint erreicht... nach mir die Sintflut
			if (startPosition.x == endPosition.x && startPosition.y == endPosition.y){
				System.out.println("++++++++++checkpoint++++++++++++++++++");

				if (chosenCards.size() < this.cardsToCheckpoint){
					this.cardsToCheckpoint = chosenCards.size();
					this.movingCards = new Vector<Card>(chosenCards);				
				}
				this.shortestLength = 0;
				this.checkpointReached = true;
			}
			else{

				//Nur verschiedene Kartentype betrachten
				for (Card c: getDistinctUseableCards(useableCards)){

					G4_Position currentPosition = new G4_Position(startPosition.x,startPosition.y,startPosition.getDirection());

					//#### Effekt der ausgespielten Karte anwenden #################
					currentPosition = this.applyCardEffect(c, currentPosition);

					//Falls Position nicht existent, Karte verwerfen
					if (currentPosition == null) 
						continue;
					
					//#### Effekt des Spielfelds auf Position berechnen ############
					currentPosition = this.applyVertexEffects(currentPosition);
				
					//Falls Position nicht existent, Karte verwerfen
					if (currentPosition == null) 
						continue;

					//Karte aus dem Vector der noch verfuegbaren Karten entfernen
					Vector<Card> stillUseableCards = new Vector<Card>(useableCards);
					stillUseableCards.remove(c);

					//Karte der Auswahl hinzufuegen		
					chosenCards.add(c);	
					
					double pathLength = 1000;
					if (!this.checkpointReached){
						pathLength = graphMap.getLengthOfShortestPath_OLD(currentPosition, endPosition);
					}
					
					//The secret formula..... Rekursionsbaum etwas stuzten
					if (pathLength != 1000 && pathLength >1 + this.shortestLength + (this.moveCardNr - chosenCards.size()) *3){
						chosenCards.remove(chosenCards.lastIndexOf(c));
						continue;
					}

					//Wenn 5 Karten gewaehlt sind, den verbleibenden Weg zum Ziel abschaetzen
					if ((chosenCards.size() == this.moveCardNr)){

						if (pathLength < shortestLength){
							this.shortestLength = pathLength;
							this.movingCards = new Vector<Card>(chosenCards);
						}			
						this.moeglichkeitenCount++;
						System.out.println(this.moeglichkeitenCount);
						//Letzte gewaehlte Karte zuruecknehmen
						chosenCards.remove(chosenCards.lastIndexOf(c));
					}
					else {
						if(chosenCards.size() < this.moveCardNr) {
							//REKURSION
							chooseMovingCards(currentPosition, endPosition, stillUseableCards, chosenCards);
							//Letzte gewaehlte Karte zuruecknehmen
							chosenCards.remove(chosenCards.lastIndexOf(c));
						}
					}
				

				}
		}
		//}

	}

	public G4_Position applyCardEffect(Card c, G4_Position position){

		try {
			//Aktuellen Knoten aus dem Graph holen
			G4_Vertex vertexCurrent = this.graphMap.getVertex(position.x,position.y);
			Direction direction = position.getDirection();

			G4_Vertex vertexBackward1 = this.graphMap.getVertexInDirectionIgnoringEdges(vertexCurrent, G4_DirectionUtils.turnU(direction));
			G4_Vertex vertexForward1 = this.graphMap.getVertexInDirectionIgnoringEdges(vertexCurrent, direction);
			G4_Vertex vertexForward2 = this.graphMap.getVertexInDirectionIgnoringEdges(vertexForward1, direction);
			G4_Vertex vertexForward3 = this.graphMap.getVertexInDirectionIgnoringEdges(vertexForward2, direction);

			//Auswirkung der naechsten Karte berechnen
			if (c.getCardType().equals(Constants.CardType.Move_Forward_Card)){
				if (!vertexCurrent.isWallinDirection(direction)){
					vertexCurrent = vertexForward1;
				}
			}
			if (c.getCardType().equals(Constants.CardType.Move_Two_Forward_Card)){
				if (!vertexCurrent.isWallinDirection(direction)){
					vertexCurrent = vertexForward1;
					if (!vertexCurrent.isWallinDirection(direction)){
						vertexCurrent = vertexForward2;
					}
				}
			}
			if (c.getCardType().equals(Constants.CardType.Move_Three_Forward_Card)){
				if (!vertexCurrent.isWallinDirection(direction)){
					vertexCurrent = vertexForward1;
					if (!vertexCurrent.isWallinDirection(direction)){
						vertexCurrent = vertexForward2;
						if (!vertexCurrent.isWallinDirection(direction)){
							vertexCurrent = vertexForward3;
						}
					}
				}
			}
			if (c.getCardType().equals(Constants.CardType.Move_Backward_Card)){
				if (!vertexCurrent.isWallinDirection(G4_DirectionUtils.turnU(direction))){
					vertexCurrent = vertexBackward1;
				}
			}
			if (c.getCardType().equals(Constants.CardType.Rotate_CW_Card)){
				direction = (G4_DirectionUtils.turnCW(position.getDirection()));
			}
			if (c.getCardType().equals(Constants.CardType.Rotate_CCW_Card)){
				direction = (G4_DirectionUtils.turnCCW(position.getDirection()));
			}
			if (c.getCardType().equals(Constants.CardType.U_Turn_Card)){
				direction = (G4_DirectionUtils.turnU(position.getDirection()));
			}


			if (vertexCurrent != null){
				position.setDirection(direction);
				position.x = vertexCurrent.getX();
				position.y = vertexCurrent.getY();
				return position;
			}else{
				return this.graphMap.startPosition;
			}

		} catch (Exception e) {
			// Falls einer der verwendeten Vertices null ist.
			//e.printStackTrace();
			return null;
		}

	}

	public G4_Position applyVertexEffects(G4_Position position){

		//TODO Verarbeitungsreihenfolge der Effekte	
		
		Direction direction = position.getDirection();
		G4_Vertex vertex = this.graphMap.getVertex(position.x,position.y);
				
		if (vertex.conveyor){
			if (vertex.conveyorAndRotator)
				direction = vertex.applyRotationEffects(direction);
			
			vertex = this.graphMap.applyTranslationEffects(vertex);
		}
		if (vertex.rotator){
			direction = vertex.applyRotationEffects(direction);
		}
		if (vertex.pusher){
			vertex = this.graphMap.applyTranslationEffects(vertex);
		}
						
		if (vertex == null){
			return this.graphMap.startPosition;
		}
		
		position.x = vertex.getX();
		position.y = vertex.getY();
		position.setDirection(direction);

		return position;	
	}

	public void tryChoosingCard(CardType cardtype, boolean strict){
		if (this.debugOutput) System.out.println("NEEDING: " + cardtype.toString());
		
		for (Card card: this.cards){
			if (this.debugOutput) System.out.println("HAVING: --- " + card.getCardTypeString().toString());			
		}
		Card returnCard = null;
		
		for (Card card: this.cards){
			if (cardtype == card.getCardType()){
				returnCard = this.cards.remove(this.cards.indexOf(card));
				break;				
			}
		}
		
		//Falls keine passende Karte gefunden wurde
		if (returnCard == null){
			//Wir brauchen unbedingt diese Karte... Drehungen spielen
			if (strict){
				String cardString = this.cardTypeToQiString(Constants.CardType.U_Turn_Card);
				
				Card[] cards =new Card[this.cards.size()];
				for(int i = 0; i < this.cards.size(); i++){
					cards[i] = this.cards.get(i);
				}
			
				QZ_AllCardChooser kkndkknd=new QZ_AllCardChooser(cards, cardString);
		    	returnCard = kkndkknd.getCards()[0];
		    	returnCard = this.cards.remove(this.cards.indexOf(returnCard));				
			}
			else{	
			 //Von Qi
				String cardString = this.cardTypeToQiString(cardtype);
			
				Card[] cards =new Card[this.cards.size()];
				for(int i = 0; i < this.cards.size(); i++){
					cards[i] = this.cards.get(i);
				}
			
				QZ_AllCardChooser kkndkknd=new QZ_AllCardChooser(cards, cardString);
		    	returnCard = kkndkknd.getCards()[0];
		    	returnCard = this.cards.remove(this.cards.indexOf(returnCard));
			}
		}
				
		this.chosenCards.add(returnCard);
		
		if (this.debugOutput) System.out.println("CHOSEN: " + returnCard.getCardTypeString() + "  ID = " + returnCard.hashCode());
	}
	
	public Card tryPickingCard(CardType cardtype){
		
		Card returnCard = null;
		
		for (Card card: this.cards){
			if (cardtype == card.getCardType()){
				returnCard = this.cards.remove(this.cards.indexOf(card));
				break;				
			}
		}
		
		return returnCard;
	}
	
	public boolean handContainsCardType(CardType cardtype){
		
		boolean contains = false;
		
		for (Card card: this.cards){
			if (cardtype == card.getCardType()){
				contains = true;		
			}
		}
		
		return contains;
	}
		
	public void tryReplacingCard(int index, CardType cardType){
		try {
			this.chosenCards.set(index, this.tryPickingCard(cardType));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private Vector<Card> getDistinctUseableCards(Vector<Card> useableCards){

		Vector<CardType> useableCardTypes = new Vector<CardType>();
		Vector<Card> distinctUseableCards = new Vector<Card>();

		for (Card c: useableCards){
			if (!useableCardTypes.contains(c.getCardType())){
				useableCardTypes.add(c.getCardType());
				distinctUseableCards.add(c);
			}
		}

		return distinctUseableCards;
	}
		
	public Card[] getChosenCardsArray(){

		Card[] myTurn = {null,null,null,null,null};
		
		//Falls die Zielposition schon erreicht sein muesste ist einfach mal weiter nach vorne schieben
		//vielleicht hat uns ja jemand zurueckgeschoben
		//this.chosenCards.addAll(this.cards);
		while (this.chosenCards.size() < 5){
			this.tryChoosingCard(Constants.CardType.U_Turn_Card, false);	
		}
			
		//Die ersten 5 gewaehlten Karten zurueckgeben
		for (int i = 0; i < 5; i++){
			myTurn[i] = chosenCards.get(i);
			if (this.debugOutput)
				System.out.println(myTurn[i].getCardTypeString() + "  ID = " + myTurn[i].hashCode());
		}
		if (this.debugOutput) System.out.println("----------------------------- ENDE --------------------------------------------");
		if (this.debugOutput) System.out.println("");
		if (this.debugOutput) System.out.println("");
		
		return myTurn;
	}

	public Vector<Card> getChosenCards() {
		return this.chosenCards;
	}

	public void setGraphMapBall(G4_GraphMapBall graphMapBall) {
		this.graphMapBall = graphMapBall;
	}

	public boolean containsTooLongEdges(List<DefaultWeightedEdge> path, int steps){
		
		for (int i = 0; i <= steps; i++){
			DefaultWeightedEdge edge = path.get(i);
			if (this.graphMap.getLengthOfEdge(edge) == 2)
				return true;
		}
		
		return false;
	}
	
	public boolean containsThreeLongEdges(List<DefaultWeightedEdge> path, int steps){
		
		for (int i = 0; i <= steps; i++){
			DefaultWeightedEdge edge = path.get(i);
			if (this.graphMap.getLengthOfEdge(edge) == 3)
				return true;
		}
		
		return false;
	}
	
	private String cardTypeToQiString(CardType cardType)
	{
		if(Constants.CardType.Rotate_CW_Card == cardType)
		{
			return "CW";
		}
		if(Constants.CardType.Rotate_CCW_Card == cardType)
		{
			return "CCW";
		}
		if(Constants.CardType.U_Turn_Card == cardType)
		{
			return "U";
		}
		if(Constants.CardType.Move_Forward_Card == cardType)
		{
			return "ONEFW";
		}
		if(Constants.CardType.Move_Two_Forward_Card == cardType)
		{
			return "TWOFW";
		}
		if(Constants.CardType.Move_Three_Forward_Card == cardType)
		{
			return "THREEFW";
		}
		if(Constants.CardType.Move_Backward_Card == cardType)
		{
			return "BW";
		}
		
		return null;
	}

}


