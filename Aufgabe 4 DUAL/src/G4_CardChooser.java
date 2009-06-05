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

	public G4_GraphMap getMap(){
		return this.graphMap;
	}

	public Card[] chooseCards(){

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
			if (this.graphMap.checkpoint != null){
				G4_Position checkpoint = new G4_Position(this.graphMap.checkpoint.getX(),this.graphMap.checkpoint.getY(),Constants.DIRECTION_STAY);
				chooseMovingCards(myPosition, checkpoint, this.cards, new Vector<Card>());
			}
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

		System.out.println("Gew�hlte Karten: " );
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
						pathLength = graphMap.getLengthOfShortestPath(currentPosition, endPosition);
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

	private G4_Position applyCardEffect(Card c, G4_Position position){

		try {
			//Aktuellen Knoten aus dem Graph holen
			G4_Vertex vertexCurrent = this.graphMap.getVertex(position.x,position.y);
			Direction direction = position.getDirection();

			G4_Vertex vertexBackward1 = this.graphMap.getVertexInDirection(vertexCurrent, G4_DirectionUtils.turnU(direction));
			G4_Vertex vertexForward1 = this.graphMap.getVertexInDirection(vertexCurrent, direction);
			G4_Vertex vertexForward2 = this.graphMap.getVertexInDirection(vertexForward1, direction);
			G4_Vertex vertexForward3 = this.graphMap.getVertexInDirection(vertexForward2, direction);

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
				return null;
			}

		} catch (Exception e) {
			// Falls einer der verwendeten Vertices null ist.
			//e.printStackTrace();
			return null;
		}

	}

	private G4_Position applyVertexEffects(G4_Position position){

		//TODO Verarbeitungsreihenfolge der Effekte	
		
		Direction direction = position.getDirection();
		G4_Vertex vertexCurrent = this.graphMap.getVertex(position.x,position.y);
		
		G4_Vertex nextVertex = null;
		
		nextVertex = this.graphMap.applyTranslationEffects(vertexCurrent);
		direction = vertexCurrent.applyRotationEffects(direction);
		
		if (nextVertex == null){
			return null;
		}
		
		position.x = nextVertex.getX();
		position.y = nextVertex.getY();
		position.setDirection(direction);

		return position;	
	}

	private Card tryChoosingCard(CardType cardtype){
		
		Card returnCard = null;
		
		for (Card card: this.cards){
			if (cardtype == card.getCardType()){
				returnCard = this.cards.remove(this.cards.indexOf(card));
				break;				
			}
		}
		
		return returnCard;
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
	
	public Vector<Card> chooseMovingCards2(G4_Position start, G4_Position ziel){
		
		//Falls Zielposition erreicht oder 5 Karten gew�hlt sind, abbrechen!
		if (start.equals(ziel) || this.chosenCards.size() >= 5){
			return chosenCards;
		}
		
		//Kuerzester Weg
		List<DefaultWeightedEdge> path = this.graphMap.getEdgesOnShortestPath(start, ziel);
		
		//Bereits gewaehlte Karten
		int countChosenCards = chosenCards.size();
		
		//Wenn der richtige Knoten erreicht ist, nur noch drehen
		if (path.size() == 0){
			if (ziel.getDirection() == G4_DirectionUtils.turnCW(start.getDirection())){
				chosenCards.add(this.tryChoosingCard(Constants.CardType.Rotate_CW_Card));
			}
			else if (ziel.getDirection() == G4_DirectionUtils.turnCCW(start.getDirection())){
				chosenCards.add(this.tryChoosingCard(Constants.CardType.Rotate_CCW_Card));
			}
			else if (ziel.getDirection() == G4_DirectionUtils.turnU(start.getDirection())){
				chosenCards.add(this.tryChoosingCard(Constants.CardType.U_Turn_Card ));
			}
		}
		
		//An den Kanten des kuerzesten Weges langlaufen
		else if (path.size() >= 3 &&
			this.graphMap.getDirectionOfEdge(path.get(0)) == start.getDirection() &&
			this.graphMap.getDirectionOfEdge(path.get(1)) == start.getDirection() &&
			this.graphMap.getDirectionOfEdge(path.get(2)) == start.getDirection()){
					chosenCards.add(this.tryChoosingCard(Constants.CardType.Move_Three_Forward_Card));
		}
		else if (path.size() >= 2 &&
				 this.graphMap.getDirectionOfEdge(path.get(0)) == start.getDirection() &&
				 this.graphMap.getDirectionOfEdge(path.get(1)) == start.getDirection()){
				chosenCards.add(this.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card));
		}
		else if (this.graphMap.getDirectionOfEdge(path.get(0)) == start.getDirection()){
			chosenCards.add(this.tryChoosingCard(Constants.CardType.Move_Forward_Card));
		}		
		else if (this.graphMap.getDirectionOfEdge(path.get(0)) == G4_DirectionUtils.turnCW(start.getDirection())){
			chosenCards.add(this.tryChoosingCard(Constants.CardType.Rotate_CW_Card));
		}
		else if (this.graphMap.getDirectionOfEdge(path.get(0)) == G4_DirectionUtils.turnCCW(start.getDirection())){
			chosenCards.add(this.tryChoosingCard(Constants.CardType.Rotate_CCW_Card));
		}
		else if (this.graphMap.getDirectionOfEdge(path.get(0)) == G4_DirectionUtils.turnU(start.getDirection())){
			chosenCards.add(this.tryChoosingCard(Constants.CardType.U_Turn_Card ));
		}
					
		//Wenn eine Karte gewaehlt wurde
		if (countChosenCards < chosenCards.size()){
			//Karteneffekt und Knoteneffekt anwenden
			start = this.applyCardEffect(chosenCards.lastElement(), start);
			start = this.applyVertexEffects(start);
			
						
		}else{
			System.out.println("Keine passende Karte gefunden");
			return null;
		}
		
		//Rekursiver Aufruf 
		return this.chooseMovingCards2(start, ziel);
	}
	
public Vector<Card> choosePushingCards(G4_Position robotPosition, G4_Position ballEndPosition){
		
		//G4_Position start = this.graphMapBall.getPositionOfBall(); 
	
		//Falls Zielposition erreicht oder 5 Karten gew�hlt sind, abbrechen!
		if (this.graphMapBall.getPositionOfBall().equals(ballEndPosition) || this.chosenCards.size() >= 5){
			return chosenCards;
		}
		
		//Kuerzester Weg
		List<DefaultEdge> path = this.graphMapBall.getEdgesOnShortestPath(this.graphMapBall.getPositionOfBall(), ballEndPosition);
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
						chosenCards.add(this.tryChoosingCard(Constants.CardType.Move_Three_Forward_Card));
			}
			else if (path.size() >= 2 &&
					 this.graphMapBall.getDirectionOfEdge(path.get(0)) == robotPosition.getDirection() &&
					 this.graphMapBall.getDirectionOfEdge(path.get(1)) == robotPosition.getDirection()){
					chosenCards.add(this.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card));
			}
			else if (this.graphMapBall.getDirectionOfEdge(path.get(0)) == robotPosition.getDirection()){
				chosenCards.add(this.tryChoosingCard(Constants.CardType.Move_Forward_Card));
			}	
			
			//Wenn eine Karte gewaehlt wurde
			if (countChosenCards < chosenCards.size()){
//				
//				//Karteneffekt und Knoteneffekt anwenden
//				start = this.applyCardEffect(chosenCards.lastElement(), start);
//				start = this.applyVertexEffects(start);
				
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


	public Card[] getChosenCardsArray(){

		Card[] myTurn = {null,null,null,null,null};
		int numCards = 0;	
		
		//Alle gewaehlten Karten zurueckgeben
		for (Card c : this.chosenCards ){
			myTurn[numCards] = c;
			numCards++;
			System.out.println(c.getCardTypeString() + "  ID = " + c.hashCode());
		}
		
		return myTurn;
	}

	public Vector<Card> getChosenCards() {
		return this.chosenCards;
	}

	public G4_GraphMapBall getGraphMapBall() {
		return graphMapBall;
	}

	public void setGraphMapBall(G4_GraphMapBall graphMapBall) {
		this.graphMapBall = graphMapBall;
	}
	
	public void tryReplacingCard(int index, CardType cardType){
		try {
			this.chosenCards.set(index, this.tryChoosingCard(cardType));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}

