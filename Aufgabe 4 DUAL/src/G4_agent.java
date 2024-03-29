import java.awt.Robot;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import roborally.task.*;

//import org.jgraph.JGraph;
//import org.jgrapht.ext.JGraphModelAdapter;
//import org.jgrapht.graph.*;



/**
 * This has to be implemented for the assignments.
 * An <code>AITask</code> represents the inteface between the assigned tasks and the roborally system.
 * To use an <code>AITask</code>, it has to be contained in a jar archive including all the other classes it depends on.
 * It is instanciated and started by putting the jar into roborally's deploy directory.
 * <strong>Any class implementing <code>AITask</code> must not be abstract and must be public and have a public default
 * constructor (no arguments)</strong>
 */
public class G4_agent extends AITask
{
	public G4_GraphMap graphMap;
	public G4_Position position;
	public G4_Position startposition;
	
	//OMAR 
	private HashSet<String> friendNames = new HashSet<String>();
	private HashSet<String> enemyNames = new HashSet<String>();
	
	private Vector<RobotInformation> friends = new Vector<RobotInformation>();
	private Vector<RobotInformation> enemies = new Vector<RobotInformation>();
		
	private boolean imattacking = false;
	
	
	public String[][] nodeStrings;
	private int nextCheckpoint = 1;
	
	public boolean debugOutput;
		
	
	/**
	 * This method is called before doTrade() in every Round. An AITask can examine its cards and make computations
	 * concerning trading operations.
	 * 
	 * @param useableCards  is an array of all <code>Card</code>s useable for the future program. <strong>Important</strong>:
	 *                      only cards that are reusable may be used more than once!
	 */
	public void preTrade(Card[] useableCards)	
	{
		
		// TODO: Implement if you want to use trading
	}
	
	/**
	 * This method is called before executeTurn and after preTrade in every Round. 
	 * An AITask can create and accept trade offers, using the global TradeManager class.
	 */
	public void doTrade()
	{
		// TODO: Implement if you want to use trading
	}		
	
	/**
	 * Returns the name of the associated robot.
	 * To provide unique names, the returned name has to contain the following elements:
	 * <ul><li>groupname</li>
	 * <li>assignment number</li></ul>
	 * For a group name "xyz" and assignment 1, the name will be:
	 * <strong>xyz-01</strong>
	 * @return name of the associated robot
	 */
	public String getRobotName()
	{
		// TODO: Add your group name here
		return "GGGG";
	}

	/**
	 * is called by the game every time a move may be made.
	 * This method is the "main method" of an <code>AITask</code>, it should contain the simulation of the next move
	 * @param useableCards  is an array of all <code>GameCard</code>s useable for the future program. <strong>Important</strong>:
	 *                      only cards that are reusable may be used more than once!
	 * @return a subset of the received useableCards as an array. If an array with more than 5 Cards are returned
	 * 			only the first 5 cards will be executed and all other cards will be ignored.        
	 */	
	public Card[] executeTurn(Card[] useableCards)
	{
		//Ausgaben auf die Konsole (de-)aktivieren
		this.debugOutput = true;
		
		// Eigene Position bestimmen
		this.position = new G4_Position(getCurrentNode().getX(),getCurrentNode().getY(),Game.Me.getOrientation());

		//Map Strings intitaliesieren
		if (this.Game.Round.getRound() == 1){
			this.nodeStrings = new String[this.Game.Map.getWidth()][this.Game.Map.getHeight()];
			this.startposition = position;
		}
		
		//Map Strings aktualisieren	
		for (int i = 0; i < this.Game.Map.getHeight(); i++){
			for (int j = 0; j < this.Game.Map.getWidth(); j++){
				if (this.Game.Map.getNode(i, j) != null){
					this.nodeStrings[i][j] = this.Game.Map.getNode(i, j).toString();
				}
			}
		}
		
		//Spielfeld als Graph erzeugen
		this.graphMap = new G4_GraphMap(nodeStrings, this.position );
		this.graphMap.startPosition = this.startposition;
		
		//FREUND UND FEIND BESTIMMEN -- ERSTE RUNDE
		if (this.Game.Round.getRound() == 1){
			for (RobotInformation robot: this.Game.Robots.getAllRobots()){
				if (robot.getRobotName() != this.getRobotName()){
					G4_Position robotPosition = new G4_Position(robot.getNode().getX(),
																robot.getNode().getY(),
																robot.getOrientation());
					if (this.graphMap.getLengthOfShortestPath(this.position, 
								robotPosition, false, false) <= 4){
						this.friendNames.add(robot.getRobotName());
						this.friends.add(robot);
					}else{
						this.enemyNames.add(robot.getRobotName());
						this.enemies.add(robot);
					}
				}
			}
			
			if (this.friends.size() == 1)
				this.imattacking = true;
		}
		//RUNDEN DANACH -- PSOITIONEN AKTUALISIEREN omar
		else{
			
			this.enemies.clear();
			this.friends.clear();
			
			for (RobotInformation robot: this.Game.Robots.getAllRobots()){
				if (robot.getRobotName() != this.getRobotName()){
					if (this.friendNames.contains(robot.getRobotName())){
						this.friends.add(robot);
					}else{
						this.enemies.add(robot);
					}
				}
			}
		}
		
		//ROBOTERINFO AN GRAPHEN GEBEN omar
		this.graphMap.setEnemies(this.enemies, this.getRobotName());
		this.graphMap.setMates(this.friends, this.getRobotName());
		
//		
//	    if (Game.Round.getRound() == 1){
//	    	JGraph jgraph = new JGraph( new JGraphModelAdapter( myMapGraph ) );
//	    	JFrame myFrame;
//
//	    	myFrame = new JFrame("Meine Map");
//	    	myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//	    	myFrame.getContentPane().add(jgraph);
//	    	myFrame.setSize(800, 800);
//	    	myFrame.show();
//	    	ToolTipManager.sharedInstance().registerComponent(jgraph);
//
//	    }
	   
	    if (this.Settings.getGameMode().equals(Constants.GameMode.REGULAR_GAME)){
	    	return this.playDomination(useableCards);//this.playRegularGame(useableCards);
	    }
	    else if (this.Settings.getGameMode().equals(Constants.GameMode.LAST_MAN_STANDING)){
	    	return this.playLastManStanding(useableCards);
	    }
	    else if (this.Settings.getGameMode().equals(Constants.GameMode.SOKUBAN)){
	    	try {
	    		//Sind mehr Roboter als wir und der Dummy auf dem Spielfeld???
				if (this.Game.Robots.getAllRobots()[2] != null)
					return this.playSokubanWithOpponent(useableCards);
			} catch (Exception e) {
				//e.printStackTrace();
				return this.playSokuban(useableCards);
			}
	     }
	    
	    
	    return null;

	}
	
	public Card[] playDomination(Card[] useableCards){
		
	    int attackCards = 0;
	    int moveCards = 5;

		//Kartenauswaehler initialisieren
		G4_CardChooser chooser = new G4_CardChooser(this.graphMap, useableCards, this.position, attackCards, moveCards);
		chooser.debugOutput = this.debugOutput;
		
		G4_Position zielPosition = null;
		G4_Vertex dom1 = (G4_Vertex) this.graphMap.dominationPoints.toArray()[0];
		G4_Vertex dom2 = (G4_Vertex) this.graphMap.dominationPoints.toArray()[1];
		
		if (this.imattacking){
			
			//QI ANGRIFF
			G4_Position freund = (G4_Position) this.graphMap.matesPositions.toArray()[0];
			chooser.chooseMovingCards2(position, angriefZielPosition(freund));
			chooser.roundNround();
			
		}
		else{
			
				
			boolean imHunting = false;
			
			//Ist ein Domination Punkt belaqert?
			if (this.graphMap.isVertexUnderHeavyAttack(dom1) ||
					this.graphMap.isVertexUnderHeavyAttack(dom2)){
				//JAGEN
				zielPosition = this.graphMap.getNextEnemy(this.position);
				imHunting = true;
			}
			else{	
				
				//Sind beide DOMs bewacht?
				boolean dom1attack = false;
				boolean dom2attack = false;
				
				double myDist2dom1 = this.graphMap.getLengthOfShortestPath(position,dom1.toG4_Position(), false, false);
				double myDist2dom2 = this.graphMap.getLengthOfShortestPath(position,dom2.toG4_Position(), false, false);
				
				boolean imClosest2Dom1 = true;
				boolean imClosest2Dom2 = true;
						
				//DOM1
				for (G4_Vertex vertex: this.graphMap.enemiesVertices){
						if (this.graphMap.isVertexInProximity(vertex, dom1))
							dom1attack = true;
				}
				
				//DOM1
				for (G4_Position pos: this.graphMap.matesPositions){
					if (this.graphMap.getLengthOfShortestPath(pos, dom1.toG4_Position(), false, false) < myDist2dom1)
						imClosest2Dom1 = false;
				}
				
				//DOM2
				for (G4_Vertex vertex: this.graphMap.enemiesVertices){
					if (this.graphMap.isVertexInProximity(vertex, dom2))
						dom2attack = true;
				}
				
				//DOM1
				for (G4_Position pos: this.graphMap.matesPositions){
					if (this.graphMap.getLengthOfShortestPath(pos, dom2.toG4_Position(), false, false) < myDist2dom2)
						imClosest2Dom2 = false;
				}
				
				if (dom1attack){
					zielPosition = dom1.toG4_Position();
				}
				else if (dom2attack){
					zielPosition = dom2.toG4_Position();
				}
				else if (!imClosest2Dom1 && !imClosest2Dom2){
					//JAGEN
					if (this.debugOutput)
						System.out.println("JAGEN: " + this.graphMap.getNextEnemy(this.position));
					zielPosition = this.graphMap.getNextEnemy(this.position);
					imHunting = true;
				}
				else{
					//if (!dom1guarded){
					if (!imClosest2Dom2){
						//FAHR ZU DOM1
						zielPosition = dom1.toG4_Position();
					}
					else{
						//FAHR ZU DOM2
						zielPosition = dom2.toG4_Position();
					}
				}
							
			}
		
			chooser.chooseMovingCards2(position, zielPosition);
			if (!imHunting){		
				//Position zum Schiessen in der Naehe?
				G4_Position next = this.graphMap.getNextShootingPosition(position);
				//if (this.graphMap.isVertexInProximity(position.toG4_Vertex(),next.toG4_Vertex()))
						chooser.chooseMovingCards2(position, next);
			}
			chooser.FREEZE();
		}
		
		if (this.debugOutput)
			System.out.println("DOMINATION -- RUNDE " +  this.Game.Round.getRound());
			    //chooser.chooseCards(zielPosition);
	    return chooser.getChosenCardsArray();
	
	   
	}

	public Card[] playRegularGame(Card[] useableCards){
		
	    int attackCards = 0;
	    int moveCards = 5;

		//Kartenauswaehler initialisieren
		G4_CardChooser chooser = new G4_CardChooser(this.graphMap, useableCards, this.position, attackCards, moveCards);
		chooser.debugOutput = this.debugOutput;
		
			
		while (chooser.getChosenCards().size() < 5 && this.nextCheckpoint < 5){
			G4_Position zielPosition;
			
			//Kennen wir einen Checkpoint ohne Reihenfolge
			if (this.graphMap.getCheckpoint(0) != null){
				zielPosition = this.graphMap.getCheckpoint(0).toG4_Position();
			}
			//Kennen wir den n鋍hsten Checkpoint in der Reihenfolge
			else if (this.graphMap.getCheckpoint(this.nextCheckpoint) != null){
				zielPosition = this.graphMap.getCheckpoint(nextCheckpoint).toG4_Position();
			}
			//Explorer-Modus
			else{
			
				zielPosition = this.graphMap.getNextGrenzknoten(position);	
				if (position.equals(zielPosition))
					break;
			}
									
			chooser.chooseMovingCards2(position, zielPosition);
		   
			//Wenn wir (vermutlich) einen Checkpoint erreicht haben
			try {
				if (position.equals(this.graphMap.getCheckpoint(nextCheckpoint).toG4_Position()))
					nextCheckpoint++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}		
			
		}
				
		
		
		if (this.debugOutput)
			System.out.println("Regular Game -- RUNDE " +  this.Game.Round.getRound());
			    //chooser.chooseCards(zielPosition);
	    return chooser.getChosenCardsArray();
	
	   
	}
	
	public Card[] playLastManStanding(Card[] useableCards){
		
		
		//Spielfeld als Graph erzeugen
		G4_GraphMap myMapGraph = new G4_GraphMap(Game.Map, position );

		//Gegner laden
		myMapGraph.loadEnemies(Game.Robots.getAllRobots(), this.getRobotName());

	
		int attackCards = 3;
		int moveCards = 2;
		
		  G4_Position zielPosition = new G4_Position(this.Game.Me.getNextCheckpoints()[0].getX(),
				   this.Game.Me.getNextCheckpoints()[0].getY(),
				   Constants.DIRECTION_STAY);

		//Kartenauswaehler initialisieren
		G4_CardChooser chooser = new G4_CardChooser(myMapGraph, useableCards, position, attackCards, moveCards);
		chooser.debugOutput = this.debugOutput;
		
		if (this.debugOutput)
			System.out.println("Last Man Standing -- RUNDE FERTIG");

		return chooser.chooseCards(zielPosition);
	}

	public Card[] playSokuban(Card[] useableCards){

		//Spielfeld als Graph erzeugen
		G4_GraphMap myMapGraph = new G4_GraphMap(Game.Map, this.position );

		myMapGraph.loadEnemies(Game.Robots.getAllRobots(), this.getRobotName());

		//Graph fuer die Bewegungen des Balls erzeugen
		G4_GraphMapBall BallsGraph = new G4_GraphMapBall(myMapGraph.vertexSet());

		//Position des Balls bestimmen
		RobotInformation ball = this.Game.Robots.getRobotByID("Dummy");
		Node nodeOfBall = ball.getNode();
		G4_Position positionOfBall = new G4_Position(nodeOfBall.getX(), 
				nodeOfBall.getY(), 
				ball.getOrientation());

		//Position des Balls speichern
		BallsGraph.setPositionOfBall(positionOfBall);

		//Zielposition des Balls
		G4_Position ballZielposition = new G4_Position(ball.getNextCheckpoints()[0].getX(),
				ball.getNextCheckpoints()[0].getY(),
				Constants.DIRECTION_STAY);

		// Eigene Position bestimmen
		G4_Position position = new G4_Position(getCurrentNode().getX(),getCurrentNode().getY(),Game.Me.getOrientation());


		int attackCards = 0;
		int moveCards = 0;

		//Kartenauswaehler initialisieren
		G4_CardChooser chooser = new G4_CardChooser(myMapGraph, useableCards, position, attackCards, moveCards);
		chooser.debugOutput = this.debugOutput;
		chooser.setGraphMapBall(BallsGraph);

		//Schiebn oder zur naechsten PushPosition fahren		
		while (chooser.getChosenCards().size() < 5){

			G4_Position nextPushPosition = BallsGraph.getNextPushingPosition(positionOfBall, ballZielposition );

			//Falls der Ball schon im Ziel sein muesste
			if (nextPushPosition == null)
				break;

			//Falls wir noch nicht auf der n鋍hsten Push-Position sind
			if (!position.equals(nextPushPosition)){

				//Nicht den Ball ueberfahren, Knoten des Balls aus Graph entfernen
				G4_Vertex vertexOfBall = positionOfBall.toG4_Vertex();
				G4_Vertex nextVertexOfBall = chooser.graphMap.getVertexInDirection(vertexOfBall, position.getDirection());
				chooser.graphMap.removeVertex(vertexOfBall);

				if (chooser.graphMap.getLengthOfShortestPath_OLD(position, nextPushPosition) == Double.POSITIVE_INFINITY){

					if (this.debugOutput)
						System.out.println("JETZT IST DER BALL MIR IM WEG!");

					//Karte neu laden um den Knoten des Balls wieder hinzuzufuegen
					chooser.graphMap.loadMap(this.Game.Map);
					//Karten waehlen um den Ball zu verschieben

					//von Qi
					G4_Position nextVertexOfBallVonQi=BallSperren(position,positionOfBall,ballZielposition,nextPushPosition);
					if(nextVertexOfBallVonQi.x!=-1)
					{
						chooser.choosePushingCards(position,nextVertexOfBall.toG4_Position() );
						//System.out.println("Test:hier wird die Position von Qi genommen");
						continue;
					}
					else
					{
						chooser.choosePushingCards(position,nextVertexOfBallVonQi);
						//System.out.println("Test:hier wird die Position von Henning genommen");
						continue;
					}

					//von Henning
					//chooser.choosePushingCards(position,nextVertexOfBall.toG4_Position() );
					//continue;
				}				

				chooser.chooseMovingCards2(position, nextPushPosition);

				//Vielleicht koennen wir den Ball schon schieben
				//wenn die letzte Karte ein "vorwaertskarte" war
				if (position.equals(nextPushPosition)){
					G4_Position nextBallPosition1 = positionOfBall.getPositionInDirection(nextPushPosition.getDirection());
					G4_Position nextBallPosition2 = nextBallPosition1.getPositionInDirection(nextPushPosition.getDirection());

					if (chooser.getChosenCards().lastElement().getCardType() == Constants.CardType.Move_Forward_Card){
						if (BallsGraph.isMovingPossible(nextBallPosition2, positionOfBall))
							chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Three_Forward_Card);
						else if (BallsGraph.isMovingPossible(nextBallPosition1, positionOfBall))
							chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Two_Forward_Card);
					}
					else if (chooser.getChosenCards().lastElement().getCardType() == Constants.CardType.Move_Two_Forward_Card){
						if (BallsGraph.isMovingPossible(nextBallPosition1, positionOfBall))
							chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Three_Forward_Card);
					}					
				}

				//Karte neu laden um den Knoten des Balls wieder hinzuzufuegen
				chooser.graphMap.loadMap(this.Game.Map);
			}
			else{
				if (this.debugOutput)
					System.out.println("JETZT MUESSTE ICH SCHIEBEN!");

				chooser.choosePushingCards(position, ballZielposition);

			}
		}

		if (this.debugOutput)
			System.out.println("Sokuban -- RUNDE FERTIG");

		return chooser.getChosenCardsArray();
	}

	public Card[] playSokubanWithOpponent(Card[] useableCards){

		//Spielfeld als Graph erzeugen
		G4_GraphMap myMapGraph = new G4_GraphMap(Game.Map, this.position );

		myMapGraph.loadEnemies(Game.Robots.getAllRobots(), this.getRobotName());

		//Graph fuer die Bewegungen des Balls erzeugen
		G4_GraphMapBall BallsGraph = new G4_GraphMapBall(myMapGraph.vertexSet());

		//Position des Balls bestimmen
		RobotInformation ball = this.Game.Robots.getRobotByID("Dummy");
		Node nodeOfBall = ball.getNode();
		G4_Position positionOfBall = new G4_Position(nodeOfBall.getX(), 
				nodeOfBall.getY(), 
				ball.getOrientation());

		//Position des Balls speichern
		BallsGraph.setPositionOfBall(positionOfBall);

		//Zielposition des Balls
		G4_Position ballZielposition = new G4_Position(ball.getNextCheckpoints()[0].getX(),
				ball.getNextCheckpoints()[0].getY(),
				Constants.DIRECTION_STAY);

		// Eigene Position bestimmen
		G4_Position position = new G4_Position(getCurrentNode().getX(),getCurrentNode().getY(),Game.Me.getOrientation());


		int attackCards = 0;
		int moveCards = 0;

		//Kartenauswaehler initialisieren
		G4_CardChooser chooser = new G4_CardChooser(myMapGraph, useableCards, position, attackCards, moveCards);
		chooser.debugOutput = this.debugOutput;
		chooser.setGraphMapBall(BallsGraph);

		//Schiebn oder zur naechsten PushPosition fahren		
		while (chooser.getChosenCards().size() < 5){

			G4_Position nextPushPosition = BallsGraph.getNextPushingPosition(positionOfBall, ballZielposition );
			//G4_Position nextPushPosition = BallsGraph.getNearestPushPosition(position, myMapGraph);
			
			//Falls der Ball schon im Ziel sein muesste
			if (nextPushPosition == null)
				break;

			//Falls wir noch nicht auf der n鋍hsten Push-Position sind
			if (!position.equals(nextPushPosition)){

				//Nicht den Ball ueberfahren, Knoten des Balls aus Graph entfernen
				G4_Vertex vertexOfBall = positionOfBall.toG4_Vertex();
				G4_Vertex nextVertexOfBall = chooser.graphMap.getVertexInDirection(vertexOfBall, position.getDirection());
				chooser.graphMap.removeVertex(vertexOfBall);

				//Erstmal den Ball abschirmen, also nicht in Zielrichtung verschieben
				if (chooser.getChosenCards().size() == 0){
					G4_Position nearestPushPosition = BallsGraph.getNearestPushPosition(position, myMapGraph);
					Direction pushDirection = nearestPushPosition.getDirection();
					chooser.chooseMovingCards2(position, nearestPushPosition);

					boolean schonVerschoben = false;
					
					//Vielleicht koennen wir den Ball schon schieben
					//wenn die letzte Karte ein "vorwaertskarte" war
					if (position.equals(nearestPushPosition) && this.Game.Round.getRound() > 2){
						G4_Position nextBallPosition1 = positionOfBall.getPositionInDirection(nearestPushPosition.getDirection());
						G4_Position nextBallPosition2 = nextBallPosition1.getPositionInDirection(nearestPushPosition.getDirection());

						if (chooser.getChosenCards().lastElement().getCardType() == Constants.CardType.Move_Forward_Card){
							if (BallsGraph.isMovingPossible(nextBallPosition2, positionOfBall))
								chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Three_Forward_Card);
							else if (BallsGraph.isMovingPossible(nextBallPosition1, positionOfBall))
								chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Two_Forward_Card);
							
							//Karte neu laden um den Knoten des Balls wieder hinzuzufuegen
							chooser.graphMap.loadMap(this.Game.Map);

							//Karteneffekt und Knoteneffekt auf Roboter anwenden
							position = chooser.applyCardEffect(chooser.getChosenCards().lastElement(), position);
							position = chooser.applyVertexEffects(position);

							//Richtung aendern damit Karten/Knoteneffekt richtig wirkt
							Direction testBallDirection = BallsGraph.getPositionOfBall().getDirection();
							BallsGraph.getPositionOfBall().setDirection(pushDirection);
							//Position des Balls aktualisieren
							BallsGraph.setPositionOfBall(chooser.applyCardEffect(chooser.getChosenCards().lastElement(),
									BallsGraph.getPositionOfBall()));
							BallsGraph.setPositionOfBall(chooser.applyVertexEffects(BallsGraph.getPositionOfBall()));
							BallsGraph.getPositionOfBall().setDirection(testBallDirection);
							schonVerschoben = true;
						}
						else if (chooser.getChosenCards().lastElement().getCardType() == Constants.CardType.Move_Two_Forward_Card){
							if (BallsGraph.isMovingPossible(nextBallPosition1, positionOfBall))
								chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Three_Forward_Card);
							
							//Karte neu laden um den Knoten des Balls wieder hinzuzufuegen
							chooser.graphMap.loadMap(this.Game.Map);

							//Karteneffekt und Knoteneffekt auf Roboter anwenden
							position = chooser.applyCardEffect(chooser.getChosenCards().lastElement(), position);
							position = chooser.applyVertexEffects(position);

							//Richtung aendern damit Karten/Knoteneffekt richtig wirkt
							Direction testBallDirection = BallsGraph.getPositionOfBall().getDirection();
							BallsGraph.getPositionOfBall().setDirection(pushDirection);
							//Position des Balls aktualisieren
							BallsGraph.setPositionOfBall(chooser.applyCardEffect(chooser.getChosenCards().lastElement(),
									BallsGraph.getPositionOfBall()));
							BallsGraph.setPositionOfBall(chooser.applyVertexEffects(BallsGraph.getPositionOfBall()));
							BallsGraph.getPositionOfBall().setDirection(testBallDirection);
							schonVerschoben = true;
						}					
					}
					
					if (chooser.getChosenCards().size() < 5 && !schonVerschoben){
						switch (BallsGraph.getMaximalPushStrength(nearestPushPosition)){

						case 3:
							if (this.Game.Round.getRound() < 3)
								chooser.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card, false);
							else
								chooser.tryChoosingCard(Constants.CardType.Move_Three_Forward_Card, false);
							break;
						case 2:
							chooser.tryChoosingCard(Constants.CardType.Move_Two_Forward_Card, false);
							break;
						case 1:
							chooser.tryChoosingCard(Constants.CardType.Move_Forward_Card, false);
							break;
						case 0:
							continue;
						}
						
						//Karte neu laden um den Knoten des Balls wieder hinzuzufuegen
						chooser.graphMap.loadMap(this.Game.Map);
						
						//Karteneffekt und Knoteneffekt auf Roboter anwenden
						position = chooser.applyCardEffect(chooser.getChosenCards().lastElement(), position);
						position = chooser.applyVertexEffects(position);

						//Richtung aendern damit Karten/Knoteneffekt richtig wirkt
						Direction testBallDirection = BallsGraph.getPositionOfBall().getDirection();
						BallsGraph.getPositionOfBall().setDirection(pushDirection);
						//Position des Balls aktualisieren
						BallsGraph.setPositionOfBall(chooser.applyCardEffect(chooser.getChosenCards().lastElement(),
								BallsGraph.getPositionOfBall()));
						BallsGraph.setPositionOfBall(chooser.applyVertexEffects(BallsGraph.getPositionOfBall()));
						BallsGraph.getPositionOfBall().setDirection(testBallDirection);
						
					}

					continue;
				}
				// TODO "Ball im Weg"-Problem verlaesslich loesen

				if (chooser.graphMap.getLengthOfShortestPath_OLD(position, nextPushPosition) == Double.POSITIVE_INFINITY){

					if (this.debugOutput)
						System.out.println("JETZT IST DER BALL MIR IM WEG!");

					//Karte neu laden um den Knoten des Balls wieder hinzuzufuegen
					chooser.graphMap.loadMap(this.Game.Map);
					//Karten waehlen um den Ball zu verschieben

					//von Qi
					G4_Position nextVertexOfBallVonQi=BallSperren(position,positionOfBall,ballZielposition,nextPushPosition);
					if(nextVertexOfBallVonQi.x!=-1)
					{
						chooser.choosePushingCards(position,nextVertexOfBall.toG4_Position() );
						//System.out.println("Test:hier wird die Position von Qi genommen");
						continue;
					}
					else
					{
						chooser.choosePushingCards(position,nextVertexOfBallVonQi);
						//System.out.println("Test:hier wird die Position von Henning genommen");
						continue;
					}

					//von Henning
					//chooser.choosePushingCards(position,nextVertexOfBall.toG4_Position() );
					//continue;
				}						

				chooser.chooseMovingCards2(position, nextPushPosition);

				//Vielleicht koennen wir den Ball schon schieben
				//wenn die letzte Karte ein "vorwaertskarte" war
				if (position.equals(nextPushPosition)){
					G4_Position nextBallPosition1 = positionOfBall.getPositionInDirection(nextPushPosition.getDirection());
					G4_Position nextBallPosition2 = nextBallPosition1.getPositionInDirection(nextPushPosition.getDirection());

					if (chooser.getChosenCards().lastElement().getCardType() == Constants.CardType.Move_Forward_Card){
						if (BallsGraph.isMovingPossible(nextBallPosition2, positionOfBall))
							chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Three_Forward_Card);
						else if (BallsGraph.isMovingPossible(nextBallPosition1, positionOfBall))
							chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Two_Forward_Card);
					}
					else if (chooser.getChosenCards().lastElement().getCardType() == Constants.CardType.Move_Two_Forward_Card){
						if (BallsGraph.isMovingPossible(nextBallPosition1, positionOfBall))
							chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Three_Forward_Card);
					}					
				}

				//Karte neu laden um den Knoten des Balls wieder hinzuzufuegen
				chooser.graphMap.loadMap(this.Game.Map);
			}
			else{
				if (this.debugOutput)
					System.out.println("JETZT MUESSTE ICH SCHIEBEN!");

				chooser.choosePushingCards(position, ballZielposition);

			}
		}

		if (this.debugOutput)
			System.out.println("Sokuban -- RUNDE FERTIG");

		return chooser.getChosenCardsArray();
	}

	//Erzeugt wallsInformation von Qi
	public G4_Wall[][] getWallsInfo(int ballNodeX,int ballNodeY,int currentNodeX,int currentNodeY,int flagNodeX,int flagNodeY){

		int height=Game.Map.getHeight();
		int width=Game.Map.getWidth();
		G4_Wall[][] wallsInfo=new G4_Wall[height][width];

		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				wallsInfo[i][j]=new G4_Wall();
			}
		}


		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(Game.Map.getNode(j, i).getConnection(Direction.EAST).toString().contains("wall")==true){wallsInfo[i][j].setEast(true);}
				if(Game.Map.getNode(j, i).getConnection(Direction.SOUTH).toString().contains("wall")==true){wallsInfo[i][j].setSouth(true);}
				if(Game.Map.getNode(j, i).getConnection(Direction.WEST).toString().contains("wall")==true){wallsInfo[i][j].setWest(true);}
				if(Game.Map.getNode(j, i).getConnection(Direction.NORTH).toString().contains("wall")==true){wallsInfo[i][j].setNorth(true);}

				//记录洞的信息
				try
				{						
					String[] temp=this.Game.Map.getEffects(j,i);
					if(temp.length>0)
					{

						if(currentNodeY==i && currentNodeX==j){break;} //把机器人自己的位置剔出来

						wallsInfo[i][j].setEast(true);
						wallsInfo[i][j].setSouth(true);
						wallsInfo[i][j].setWest(true);
						wallsInfo[i][j].setNorth(true);

						//Debug
						//System.out.println("currentWallX ="+j+",currentWallY ="+i);
						//不要让下标超过允许的范围
						try{wallsInfo[i-1][j].setSouth(true);}catch(Exception e){//System.out.println("East out of bound");
						}
						try{wallsInfo[i][j-1].setEast(true);}catch(Exception e){//System.out.println("South out of bound");
						}
						try{wallsInfo[i+1][j].setNorth(true);}catch(Exception e){//System.out.println("West out of bound");
						}
						try{wallsInfo[i][j+1].setWest(true);}catch(Exception e){//System.out.println("North out of bound");
						}
					}
				}
				catch(Exception e)
				{
					//System.out.println("None Effect");
				}


			}
		}


		//Debug
		/*
		 * System.out.println("Hier wird alle Informationen Ueber Walls gezeigt.");
	for(int i=0;i<height;i++)
	{
		for(int j=0;j<width;j++)
		{
			System.out.println("i ="+i+",j ="+j+"East ="+Boolean.toString(wallsInfo[i][j].getEast()));
			System.out.println("i ="+i+",j ="+j+"South ="+Boolean.toString(wallsInfo[i][j].getSouth()));
			System.out.println("i ="+i+",j ="+j+"West ="+Boolean.toString(wallsInfo[i][j].getWest()));
			System.out.println("i ="+i+",j ="+j+"North ="+Boolean.toString(wallsInfo[i][j].getNorth()));
			System.out.println("--------------------------");
		}

		System.out.println("\n\n\n");
	}
		 */

		if(ballNodeX!=-1)
		{
			//设置球的位置为墙,Robot不能通过球
			wallsInfo[ballNodeY][ballNodeX].setEast(true);
			wallsInfo[ballNodeY][ballNodeX].setSouth(true);
			wallsInfo[ballNodeY][ballNodeX].setWest(true);
			wallsInfo[ballNodeY][ballNodeX].setNorth(true);
			try{wallsInfo[ballNodeY][ballNodeX-1].setEast(true);}catch(Exception e){}
			try{wallsInfo[ballNodeY-1][ballNodeX].setSouth(true);}catch(Exception e){}
			try{wallsInfo[ballNodeY][ballNodeX+1].setWest(true);}catch(Exception e){}
			try{wallsInfo[ballNodeY+1][ballNodeX].setNorth(true);}catch(Exception e){}
		}			


		//把旗帜的位置设为可以通过，不然到不了终点
		wallsInfo[flagNodeY][flagNodeX].setEast(true);
		wallsInfo[flagNodeY][flagNodeX].setSouth(true);
		wallsInfo[flagNodeY][flagNodeX].setWest(true);
		wallsInfo[flagNodeY][flagNodeX].setNorth(true);
		try{wallsInfo[flagNodeY][flagNodeX-1].setEast(true);}catch(Exception e){}
		try{wallsInfo[flagNodeY-1][flagNodeX].setSouth(true);}catch(Exception e){}
		try{wallsInfo[flagNodeY][flagNodeX+1].setWest(true);}catch(Exception e){}
		try{wallsInfo[flagNodeY+1][flagNodeX].setNorth(true);}catch(Exception e){}

		return wallsInfo;
	}

	//waehlt eine Postion aus,wenn der Ball hat den Weg von Robot blockiert ist
	public G4_Position BallSperren(G4_Position position,G4_Position positionOfBall,G4_Position ballZielposition,G4_Position nextPushPosition)
	{
		//von Qi
		//kiegt alle Information ueber Spielfeld
		//int currentNodeX=Game.Me.getCurrentNode().getX();
		//int currentNodeY=Game.Me.getCurrentNode().getY();
		int currentNodeX=position.x;
		int currentNodeY=position.y;
		int flagNodeX=Game.Me.getCheckpoints()[0].getX();
		int flagNodeY=Game.Me.getCheckpoints()[0].getY();
		int height=Game.Map.getHeight();
		int width=Game.Map.getWidth();
		//int ballNodeX=Game.Robots.getRobotByID("Dummy").getNode().getX();
		//int ballNodeY=Game.Robots.getRobotByID("Dummy").getNode().getY();
		int ballNodeX=positionOfBall.x;
		int ballNodeY=positionOfBall.y;
		int nextNodeX=-1;
		int nextNodeY=-1;
		G4_Wall[][] wallsInfo=getWallsInfo(ballNodeX,ballNodeY,currentNodeX,currentNodeY,flagNodeX,flagNodeY);
		G4_Wall[][] wallsInfoNoBall=getWallsInfo(-1,-1,currentNodeX,currentNodeY,flagNodeX,flagNodeY);
		G4_Wall[][] wallsInfoE=getWallsInfo(ballNodeX-1,ballNodeY,currentNodeX,currentNodeY,flagNodeX,flagNodeY);
		G4_Wall[][] wallsInfoS=getWallsInfo(ballNodeX,ballNodeY-1,currentNodeX,currentNodeY,flagNodeX,flagNodeY);
		G4_Wall[][] wallsInfoW=getWallsInfo(ballNodeX+1,ballNodeY,currentNodeX,currentNodeY,flagNodeX,flagNodeY);
		G4_Wall[][] wallsInfoN=getWallsInfo(ballNodeX,ballNodeY+1,currentNodeX,currentNodeY,flagNodeX,flagNodeY);

		//waehlt eine Position aus
		G4_BallSperren ballsperren=new G4_BallSperren(wallsInfo,wallsInfoE,wallsInfoS,wallsInfoW,wallsInfoN,wallsInfoNoBall,width,height,ballNodeX,ballNodeY,currentNodeX,currentNodeY);

		//Debug
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		System.out.println("East-->"+ballsperren.getPositionTag()[0]);
//		System.out.println("South-->"+ballsperren.getPositionTag()[1]);
//		System.out.println("West-->"+ballsperren.getPositionTag()[2]);
//		System.out.println("North-->"+ballsperren.getPositionTag()[3]);
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

		if(ballsperren.getPositionTag()[0]+ballsperren.getPositionTag()[1]+ballsperren.getPositionTag()[2]+ballsperren.getPositionTag()[3]!=0)
		{
			//anpasst die Codes von Qi
			//if(ballsperren.getPositionTag()[0]==1){nextNodeX=positionOfBall.x+1;nextNodeY=positionOfBall.y;}
			//if(ballsperren.getPositionTag()[1]==1){nextNodeX=positionOfBall.x;nextNodeY=positionOfBall.y+1;}
			//if(ballsperren.getPositionTag()[2]==1){nextNodeX=positionOfBall.x-1;nextNodeY=positionOfBall.y;}
			//if(ballsperren.getPositionTag()[3]==1){nextNodeX=positionOfBall.x;nextNodeY=positionOfBall.y-1;}

			//anpasst die Codes von henning
			if(ballsperren.getPositionTag()[0]==1){nextNodeX=positionOfBall.x-1;nextNodeY=positionOfBall.y;}
			if(ballsperren.getPositionTag()[1]==1){nextNodeX=positionOfBall.x;nextNodeY=positionOfBall.y-1;}
			if(ballsperren.getPositionTag()[2]==1){nextNodeX=positionOfBall.x+1;nextNodeY=positionOfBall.y;}
			if(ballsperren.getPositionTag()[3]==1){nextNodeX=positionOfBall.x;nextNodeY=positionOfBall.y+1;}

			return new G4_Position(nextNodeX,nextNodeY,Game.Me.getOrientation());
		}
		else
		{
			return new G4_Position(nextNodeX,nextNodeY,Game.Me.getOrientation());
		}
	}
	
	//Von Qi
	//Angrief_Strategie
	public G4_Position angriefZielPosition(G4_Position partnerposition)
	{
		G4_Position myposition=new G4_Position(this.Game.Me.getCurrentNode().getX(),this.Game.Me.getCurrentNode().getY(),this.Game.Me.getOrientation());
		G4_Position checkpoint0=new G4_Position(this.Game.Me.getCheckpoints()[0].getX(),this.Game.Me.getCheckpoints()[0].getY(),this.Game.Me.getOrientation());
		G4_Position checkpoint1=new G4_Position(this.Game.Me.getCheckpoints()[1].getX(),this.Game.Me.getCheckpoints()[1].getY(),this.Game.Me.getOrientation());
				
		return this.graphMap.getKnotenMyCheckpoint(myposition, partnerposition, checkpoint0, checkpoint1);
	}
}
