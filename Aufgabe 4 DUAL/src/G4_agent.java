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
		

		
//		//von Qi
//		int currentNodeX=Game.Me.getCurrentNode().getX();
//		int currentNodeY=Game.Me.getCurrentNode().getY();
//		int flagNodeX=Game.Me.getCheckpoints()[0].getX();
//		int flagNodeY=Game.Me.getCheckpoints()[0].getY();
//		int height=Game.Map.getHeight();
//		int width=Game.Map.getWidth();
////		int ballNodeX=Game.Robots.getRobotByID("Dummy").getNode().getX();
////		int ballNodeY=Game.Robots.getRobotByID("Dummy").getNode().getY();
//		int nextNodeX;
//		int nextNodeY;
//		
//		G4_Position start = new G4_Position(ballNodeX,ballNodeY, Constants.DIRECTION_STAY);

		
//		List<DefaultEdge> edges = BallsGraph.getEdgesOnShortestPath(start,ziel); 
//		G4_Vertex vertex  = BallsGraph.getEdgeTarget(edges.get(0));
//		nextNodeX=vertex.getX();
//		nextNodeY=vertex.getY();
				
//		
//		//Von Qi
//		String orientation=Game.Me.getOrientation().toString();
//		QZ_RoboMoveToBall robomoveball=new QZ_RoboMoveToBall(getWallsInfo(ballNodeX,ballNodeY,currentNodeX,currentNodeY,flagNodeX,flagNodeY),width,height,useableCards,ballNodeX,ballNodeY,currentNodeX,currentNodeY,orientation,nextNodeX,nextNodeY);
//				
//		
//		for (DefaultEdge edge: edges){
//			System.out.println(edge);
//		}
		
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
		
			
				
		//return robomoveball.getCards();
	}

	//Erzeugt wallsInformation von Qi
	public QZ_Wall[][] getWallsInfo(int ballNodeX,int ballNodeY,int currentNodeX,int currentNodeY,int flagNodeX,int flagNodeY){
		
		int height=Game.Map.getHeight();
		int width=Game.Map.getWidth();
		QZ_Wall[][] wallsInfo=new QZ_Wall[height][width];
		
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				wallsInfo[i][j]=new QZ_Wall();
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
			
			//��¼������Ϣ
				try
					{						
							String[] temp=this.Game.Map.getEffects(j,i);
							if(temp.length>0)
							{
								
								if(currentNodeY==i && currentNodeX==j){break;} //�ѻ������Լ���λ���޳���
								
								wallsInfo[i][j].setEast(true);
								wallsInfo[i][j].setSouth(true);
								wallsInfo[i][j].setWest(true);
								wallsInfo[i][j].setNorth(true);
								
								//Debug
								//System.out.println("currentWallX ="+j+",currentWallY ="+i);
								//��Ҫ���±곬�������ķ�Χ
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
						System.out.println("None Effect");
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
			
					
		//�������λ��Ϊǽ,Robot����ͨ����
		wallsInfo[ballNodeY][ballNodeX].setEast(true);
		wallsInfo[ballNodeY][ballNodeX].setSouth(true);
		wallsInfo[ballNodeY][ballNodeX].setWest(true);
		wallsInfo[ballNodeY][ballNodeX].setNorth(true);
		try{wallsInfo[ballNodeY][ballNodeX-1].setEast(true);}catch(Exception e){}
		try{wallsInfo[ballNodeY-1][ballNodeX].setSouth(true);}catch(Exception e){}
		try{wallsInfo[ballNodeY][ballNodeX+1].setWest(true);}catch(Exception e){}
		try{wallsInfo[ballNodeY+1][ballNodeX].setNorth(true);}catch(Exception e){}
				
		//�����ĵ�λ����Ϊ����ͨ������Ȼ�������յ�
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
			
			//Falls wir noch nicht auf der n�chsten Push-Position sind
			if (!position.equals(nextPushPosition)){
				// TODO "Ball im Weg"-Problem verlaesslich loesen
				
				//Nicht den Ball ueberfahren, Knoten des Balls aus Graph entfernen
				G4_Vertex vertexOfBall = positionOfBall.toG4_Vertex();
				G4_Vertex nextVertexOfBall = chooser.graphMap.getVertexInDirection(vertexOfBall, position.getDirection());
				chooser.graphMap.removeVertex(vertexOfBall);
				
				if (chooser.graphMap.getLengthOfShortestPath(position, nextPushPosition) == Double.POSITIVE_INFINITY){
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
				//chooser.choosePushingCards	
				System.out.println("JETZT MUESSTE ICH SCHIEBEN!");
				chooser.choosePushingCards(position, ballZielposition);
				
			}
		}
		
		System.out.println("Sokuban -- RUNDE FERTIG");

		return chooser.getChosenCardsArray();
	}
}