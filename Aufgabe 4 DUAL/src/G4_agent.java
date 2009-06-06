import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import roborally.task.*;

import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.*;



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
	
	boolean debugOutput;
	
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
		return "Gruppe4";
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
		   
	    if (this.Settings.getGameMode().equals(Constants.GameMode.REGULAR_GAME)){
	    	return this.playRegularGame(useableCards);
	    }
	    else if (this.Settings.getGameMode().equals(Constants.GameMode.LAST_MAN_STANDING)){
	    	return this.playLastManStanding(useableCards);
	    }
	    else if (this.Settings.getGameMode().equals(Constants.GameMode.SOKUBAN)){
	    	return this.playSokuban(useableCards);
	    }
	    
	    
	    return null;

	    
//		if (Game.Round.getRound() == 1){
//			JGraph jgraph = new JGraph( new JGraphModelAdapter( BallsGraph ) );
//		    JFrame myFrame;
//
//		    myFrame = new JFrame("Meine Map");
//		    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	      
//	        myFrame.getContentPane().add(jgraph);
//		    myFrame.setSize(800, 800);
//		    myFrame.show();
//		    ToolTipManager.sharedInstance().registerComponent(jgraph);
//	
//		}
		
	}

	public Card[] playRegularGame(Card[] useableCards){
		
		//Spielfeld als Graph erzeugen
		G4_GraphMap myMapGraph = new G4_GraphMap(Game.Map );
		
		// Eigene Position bestimmen
	    G4_Position position = new G4_Position(getCurrentNode().getX(),getCurrentNode().getY(),Game.Me.getOrientation());
	    
	    // Eigene ZielPosition bestimmen
	    G4_Position zielPosition = new G4_Position(this.Game.Me.getNextCheckpoints()[0].getX(),
	    										   this.Game.Me.getNextCheckpoints()[0].getY(),
	    										   Constants.DIRECTION_STAY);
	
	    int attackCards = 0;
	    int moveCards = 0;

		//Kartenauswaehler initialisieren
		G4_CardChooser chooser = new G4_CardChooser(myMapGraph, useableCards, position, attackCards, moveCards);
		
		
		chooser.chooseMovingCards2(position, zielPosition);
		
		if (this.debugOutput)
			System.out.println("Regular Game -- RUNDE FERTIG");
			    
	    return chooser.getChosenCardsArray();
	}
	
	public Card[] playLastManStanding(Card[] useableCards){

		//Spielfeld als Graph erzeugen
		G4_GraphMap myMapGraph = new G4_GraphMap(Game.Map );

		//Gegner laden
		myMapGraph.loadEnemies(Game.Robots.getAllRobots(), this.getRobotName());

		// Eigene Position bestimmen
		G4_Position position = new G4_Position(getCurrentNode().getX(),getCurrentNode().getY(),Game.Me.getOrientation());

		int attackCards = 3;
		int moveCards = 2;

		//Kartenauswaehler initialisieren
		G4_CardChooser chooser = new G4_CardChooser(myMapGraph, useableCards, position, attackCards, moveCards);

		if (this.debugOutput)
			System.out.println("Last Man Standing -- RUNDE FERTIG");

		return chooser.chooseCards();
	}

	public Card[] playSokuban(Card[] useableCards){
		
		//Spielfeld als Graph erzeugen
		G4_GraphMap myMapGraph = new G4_GraphMap(Game.Map );
		
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
		chooser.setGraphMapBall(BallsGraph);
		
		//Schiebn oder zur naechsten PushPosition fahren		
		while (chooser.getChosenCards().size() < 5){

			G4_Position nextPushPosition = BallsGraph.getNextPushingPosition(positionOfBall, ballZielposition );
			
			//Falls der Ball schon im Ziel sein muesste
			if (nextPushPosition == null)
				break;
			
			//Falls wir noch nicht auf der nächsten Push-Position sind
			if (!position.equals(nextPushPosition)){
				// TODO "Ball im Weg"-Problem verlaesslich loesen
				
				//Nicht den Ball ueberfahren, Knoten des Balls aus Graph entfernen
				G4_Vertex vertexOfBall = positionOfBall.toG4_Vertex();
				G4_Vertex nextVertexOfBall = chooser.graphMap.getVertexInDirection(vertexOfBall, position.getDirection());
				chooser.graphMap.removeVertex(vertexOfBall);
				
				if (chooser.graphMap.getLengthOfShortestPath(position, nextPushPosition) == Double.POSITIVE_INFINITY){
					
					if (this.debugOutput)
						System.out.println("JETZT IST DER BALL MIR IM WEG!");
					
					//Karte neu laden um den Knoten des Balls wieder hinzuzufuegen
					chooser.graphMap.loadMap(this.Game.Map);
					//Karten waehlen um den Ball zu verschieben
					chooser.choosePushingCards(position,nextVertexOfBall.toG4_Position() );
					continue;
				}				

				chooser.chooseMovingCards2(position, nextPushPosition);
				
				//Vielleicht koennen wir den Ball schon schieben
				//wenn die letzte Karte ein "vorwaertskarte" war
				if (chooser.getChosenCards().size() < 5){
					if (chooser.getChosenCards().lastElement().getCardType() == Constants.CardType.Move_Forward_Card){
						chooser.tryReplacingCard(chooser.getChosenCards().size() - 1, Constants.CardType.Move_Two_Forward_Card);
					}
					else if (chooser.getChosenCards().lastElement().getCardType() == Constants.CardType.Move_Two_Forward_Card){
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
}
