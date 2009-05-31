import java.util.LinkedList;
import java.util.List;

public class QZ_Move5 {
	
	private QZ_Wall[][] wallsInfo;
	private int width;
	private int height;
	
	private int roboNodeX;
	private int roboNodeY;
	private String dir;
	private int goalNodeX;
	private int goalNodeY;
	
	private String[] myTurn;
	private String[] myTurn5={null,null,null,null,null};
	private List<String> myTurnList =new LinkedList<String>();
	List<QZ_Node> getMiniPath=new LinkedList<QZ_Node>();
	int numCards = 0;
	int numCards5=0;
	
	
	public QZ_Move5(QZ_Wall[][] wallsInfo,int width,int height,int roboNodeX,int roboNodeY,String dir,int goalNodeX,int goalNodeY)
	{
		
		this.wallsInfo=wallsInfo;
		this.width=width;
		this.height=height;
					
		this.roboNodeX=roboNodeX;
		this.roboNodeY=roboNodeY;
		this.dir=dir;
		this.goalNodeX=goalNodeX;
		this.goalNodeY=goalNodeY;
		
		move();	
	}
	
	
	
	//机器人移动
	public void move()
	{				
			QZ_TestMap testmap=new QZ_TestMap(wallsInfo,roboNodeX,roboNodeY,goalNodeX,goalNodeY,height,width);
			getMiniPath=testmap.getMiniPath();
								
			while(numCards<getMiniPath.size())
			{
				//Debug
				/*
				 * System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("Richtung(vor Bewegung)----->"+this.dir);
				System.out.println("startNodeX ="+getMiniPath.get(numCards)._pos.x+",startNodeY ="+getMiniPath.get(numCards)._pos.y);
				System.out.println("zielNodeX ="+getMiniPath.get(numCards+1)._pos.x+",zielNodeY ="+getMiniPath.get(numCards+1)._pos.y);
								
				 */
				try
				{
					QZ_Move testmovestring=new QZ_Move(getMiniPath.get(numCards)._pos.x,getMiniPath.get(numCards)._pos.y,getMiniPath.get(numCards+1)._pos.x,getMiniPath.get(numCards+1)._pos.y,dir);
					this.myTurn=new String[testmovestring.getCards().length];
					this.myTurn=testmovestring.getCards();
					this.dir=testmovestring.getDir();
					
					//Debug
					/*
					 * for(int i=0;i<myTurn.length;i++)
					{
						System.out.println("Card"+i+"----->"+myTurn[i]);
					}
					System.out.println("Richtung(nach Bewegung)----->"+this.dir);
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx\n\n\n");		
					 */
																							
					for (String c : myTurn)
					{
						myTurnList.add(c);
					}
					
					//Debug
					/*
					 * System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
					for(int i=0;i<myTurnList.size();i++)
					{
						System.out.println("Card"+i+"----->"+myTurnList.get(i).getCardTypeString());
					}
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n\n\n");
					 */
											
				}
				catch(Exception e)
				{
					//已经到最短路径的末尾，不能再取了
					System.out.println("Der Letzte Node des kurzste Weg ist schoen erreicht.");
				}
							
								
				numCards++;				
			}
			
			//Debug
			/*
			 * System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			for(int i=0;i<myTurnList.size();i++)
			{
				System.out.println("Card"+i+"----->"+myTurnList.get(i));
			}
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n\n\n"); 
			 */
												
			//现在要在所有卡中取前5张卡，如果不够5张的话补上其他的卡
			while(true)
			{
				if(numCards5==5 || numCards5>myTurnList.size()-1)
				{
					break;
				}
				
				myTurn5[numCards5]=myTurnList.get(numCards5);
				numCards5++;
							
			}
			
						
			while( numCards5 < 5 )
			{					
				myTurn5[numCards5] = "turn";
				numCards5++;
								
			}			
			
			//Debug
			/*
			 * System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			for(int i=0;i<myTurn5.length;i++)
			{
				System.out.println("Card"+i+"----->"+myTurn5[i]);
			}
			System.out.println("Richtung----->"+dir);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n\n\n");	
					
			 */
	}	
		
	public String[] getCards()
	{
		
		return this.myTurn5;
	}
}
