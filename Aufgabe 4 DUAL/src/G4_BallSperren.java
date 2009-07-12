
/**
 * @author Qi Zheng
 *
 *Wenn der Ball hat den Bewegungsweg von Robot blockiert,waehlt der Robot nach Strategie eine Position aus,die nicht auf dem kuerzstem Weg liegt.
 *
 */
//Init
public class G4_BallSperren {
	private int ballNodeX;
	private int ballNodeY;
	private int roboNodeX;
	private int roboNodeY;
	private G4_Wall[][] wallsInfo;
	private G4_Wall[][] wallsInfoE;
	private G4_Wall[][] wallsInfoS;
	private G4_Wall[][] wallsInfoW;
	private G4_Wall[][] wallsInfoN;
	private G4_Wall[][] wallsInfoNoBall;
	private G4_Wall[][] wallsInfoNew;
	private G4_Wall[][] wallsInfoENew;
	private G4_Wall[][] wallsInfoSNew;
	private G4_Wall[][] wallsInfoWNew;
	private G4_Wall[][] wallsInfoNNew;
	private G4_Wall[][] wallsInfoNoBallNew;
	private int width;
	private int height;
	private int[] positionTag={0,0,0,0};
		
	//Init
	public G4_BallSperren(G4_Wall[][] wallsInfo,G4_Wall[][] wallsInfoE,G4_Wall[][] wallsInfoS,G4_Wall[][] wallsInfoW,G4_Wall[][] wallsInfoN,G4_Wall[][] wallsInfoNoBall,int width,int height,int ballNodeX,int ballNodeY,int roboNodeX,int roboNodeY)
	{
		this.ballNodeX=ballNodeX;
		this.ballNodeY=ballNodeY;
		this.roboNodeX=roboNodeX;
		this.roboNodeY=roboNodeY;
		this.width=width;
		this.height=height;
		this.wallsInfo=wallsInfo;
		this.wallsInfoE=wallsInfoE;
		this.wallsInfoS=wallsInfoS;
		this.wallsInfoW=wallsInfoW;
		this.wallsInfoN=wallsInfoN;
		this.wallsInfoNoBall=wallsInfoNoBall;
		this.wallsInfoNew=change(wallsInfo);
		this.wallsInfoENew=change(wallsInfoE);
		this.wallsInfoSNew=change(wallsInfoS);
		this.wallsInfoWNew=change(wallsInfoW);
		this.wallsInfoNNew=change(wallsInfoN);
		this.wallsInfoNoBallNew=change(wallsInfoNoBall);
		positionAussuchen();
	}
	
	//Waehlt eine Position aus,wenn der Ball den Weg blokiert ist
	public void positionAussuchen()
	{	
		//System.out.println("Schieben starten:");
		//System.out.println("Ballposition("+ballNodeX+","+ballNodeY+")");
		//System.out.println("Roboposition("+roboNodeX+","+roboNodeY+")");
		try
		{				
			//schiebt den Ball vom East zum West
			new G4_TestMap(wallsInfo,roboNodeX,roboNodeY,ballNodeX+1,ballNodeY,height,width);
			//System.out.println("einmal schieben Vom East zum West");
			new G4_TestMap(wallsInfoE,ballNodeX,ballNodeY,ballNodeX-2,ballNodeY,height,width);
			//System.out.println("zweimal schieben vom East zum West");

			//Debug
			//System.out.println(wallsInfoNoBallNew[ballNodeX+1][ballNodeY].getWest());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY].getEast());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY].getWest());
			//System.out.println(wallsInfoNoBallNew[ballNodeX-1][ballNodeY].getEast());
			//System.out.println(wallsInfoNoBallNew[ballNodeX-1][ballNodeY].getWest());
			//System.out.println(wallsInfoNoBallNew[ballNodeX-2][ballNodeY].getEast());
						
			if(!wallsInfoNoBallNew[ballNodeX+1][ballNodeY].getWest())
			{
				if(!wallsInfoNoBallNew[ballNodeX][ballNodeY].getEast())
				{
					if(!wallsInfoNoBallNew[ballNodeX][ballNodeY].getWest())
					{
						if(!wallsInfoNoBallNew[ballNodeX-1][ballNodeY].getEast())
						{
							if(!wallsInfoNoBallNew[ballNodeX-1][ballNodeY].getWest())
							{
								if(!wallsInfoNoBallNew[ballNodeX-2][ballNodeY].getEast())
								{
									positionTag[0]=1;
								}
							}
						}
					}
				}
			}
			
			/*
			 * boolean temp=!wallsInfoNoBall[ballNodeX+1][ballNodeY].getWest() &&
			 !wallsInfoNoBall[ballNodeX][ballNodeY].getEast() &&
			 !wallsInfoNoBall[ballNodeX][ballNodeY].getWest() &&
			 !wallsInfoNoBall[ballNodeX-1][ballNodeY].getEast() &&
			 !wallsInfoNoBall[ballNodeX-1][ballNodeY].getWest() &&
			 !wallsInfoNoBall[ballNodeX-2][ballNodeY].getEast();
						
			if( temp==false )
			{
				positionTag[0]=1;
			}
			 */
			
			
		}
		catch(Exception e)
		{
			//System.out.println("Der Robot kann die Position von East des Robots nicht erreichen");
		}
		
		
		try
		{	
			//schiebt den Ball vom West zum East
			new G4_TestMap(wallsInfo,roboNodeX,roboNodeY,ballNodeX-1,ballNodeY,height,width);
			//System.out.println("einmal schieben vom West zum East");
			new G4_TestMap(wallsInfoW,ballNodeX,ballNodeY,ballNodeX+2,ballNodeY,height,width);
			//System.out.println("zweimal schieben vom West zum East");
			
			//Debug
			//System.out.println(wallsInfoNoBallNew[ballNodeX-1][ballNodeY].getEast());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY].getWest());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY].getEast());
			//System.out.println(wallsInfoNoBallNew[ballNodeX+1][ballNodeY].getWest());
			//System.out.println(wallsInfoNoBallNew[ballNodeX+1][ballNodeY].getEast());
			//System.out.println(wallsInfoNoBallNew[ballNodeX+2][ballNodeY].getWest());
			
			if(!wallsInfoNoBallNew[ballNodeX-1][ballNodeY].getEast())
			{
				if(!wallsInfoNoBallNew[ballNodeX][ballNodeY].getWest())
				{
					if(!wallsInfoNoBallNew[ballNodeX][ballNodeY].getEast())
					{
						if(!wallsInfoNoBallNew[ballNodeX+1][ballNodeY].getWest())
						{
							if(!wallsInfoNoBallNew[ballNodeX+1][ballNodeY].getEast())
							{
								if(!wallsInfoNoBallNew[ballNodeX+2][ballNodeY].getWest())
								{
									positionTag[2]=1;
								}
							}
						}
					}
				}
			}
			
			/*
			 * boolean temp=!wallsInfoNoBall[ballNodeX-1][ballNodeY].getEast() &&
			 			 !wallsInfoNoBall[ballNodeX][ballNodeY].getWest() &&
			 			 !wallsInfoNoBall[ballNodeX][ballNodeY].getEast() &&
			 			 !wallsInfoNoBall[ballNodeX+1][ballNodeY].getWest() &&
			 			 !wallsInfoNoBall[ballNodeX+1][ballNodeY].getEast() &&
			 			 !wallsInfoNoBall[ballNodeX+2][ballNodeY].getWest();
			
			if(temp==false)
			{
				positionTag[2]=1;
			}
			 */
					
		}
		catch(Exception e)
		{
			//System.out.println("Der Robot kann die Position von West des Robots nicht erreichen");
		}
		
		
		try
		{	
			//schiebt den Ball vom North zum South
			new G4_TestMap(wallsInfo,roboNodeX,roboNodeY,ballNodeX,ballNodeY-1,height,width);
			//System.out.println("einmal schieben vom North zum South");
			new G4_TestMap(wallsInfoN,ballNodeX,ballNodeY,ballNodeX,ballNodeY+2,height,width);
			//System.out.println("zweimal schieben vom North zum South");
			
			//Debug
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY-1].getSouth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY].getNorth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY].getSouth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY+1].getNorth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY+1].getSouth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY+2].getNorth());
			
			if(!wallsInfoNoBallNew[ballNodeX][ballNodeY-1].getSouth())
			{
				if(!wallsInfoNoBallNew[ballNodeX][ballNodeY].getNorth())
				{
					if(!wallsInfoNoBallNew[ballNodeX][ballNodeY].getSouth())
					{
						if(!wallsInfoNoBallNew[ballNodeX][ballNodeY+1].getNorth())
						{
							if(!wallsInfoNoBallNew[ballNodeX][ballNodeY+1].getSouth())
							{
								if(!wallsInfoNoBallNew[ballNodeX][ballNodeY+2].getNorth())
								{
									positionTag[3]=1;
								}
							}
						}
					}
				}
			}
			
			/*
			 * boolean temp=!wallsInfoNoBall[ballNodeX][ballNodeY-1].getSouth() &&
			 			 !wallsInfoNoBall[ballNodeX][ballNodeY].getNorth() &&
			 			 !wallsInfoNoBall[ballNodeX][ballNodeY].getSouth() &&
			 			 !wallsInfoNoBall[ballNodeX][ballNodeY+1].getNorth() &&
			 			 !wallsInfoNoBall[ballNodeX][ballNodeY+1].getSouth() &&
			 			 !wallsInfoNoBall[ballNodeX][ballNodeY+2].getNorth();
			
			if(temp==false)
			{
				positionTag[3]=1;
			}
			 */
			
				
		}
		catch(Exception e)
		{
			//System.out.println("Der Robot kann die Position von North des Robots nicht erreichen");
		}
		
		
		try
		{	
			//schiebt den vom South zum North
			new G4_TestMap(wallsInfo,roboNodeX,roboNodeY,ballNodeX,ballNodeY+1,height,width);
			//System.out.println("einmal schieben vom South zum North");
			new G4_TestMap(wallsInfoS,ballNodeX,ballNodeY,ballNodeX,ballNodeY-2,height,width);
			//System.out.println("zweimal schieben vom South zum North");
				
			//Debug
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY+1].getNorth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY].getSouth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY].getNorth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY-1].getSouth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY-1].getNorth());
			//System.out.println(wallsInfoNoBallNew[ballNodeX][ballNodeY-2].getSouth());
			
			if(!wallsInfoNoBallNew[ballNodeX][ballNodeY+1].getNorth())
			{
				if(!wallsInfoNoBallNew[ballNodeX][ballNodeY].getSouth())
				{
					if(!wallsInfoNoBallNew[ballNodeX][ballNodeY].getNorth())
					{
						if(!wallsInfoNoBallNew[ballNodeX][ballNodeY-1].getSouth())
						{
							if(!wallsInfoNoBallNew[ballNodeX][ballNodeY-1].getNorth())
							{
								if(!wallsInfoNoBallNew[ballNodeX][ballNodeY-2].getSouth())
								{
									positionTag[1]=1;
								}
							}
						}
					}
				}
			}
			
			
			/*
			 * boolean temp=!wallsInfoNoBall[ballNodeX][ballNodeY+1].getNorth() &&
			 !wallsInfoNoBall[ballNodeX][ballNodeY].getSouth() &&
			 !wallsInfoNoBall[ballNodeX][ballNodeY].getNorth() &&
			 !wallsInfoNoBall[ballNodeX][ballNodeY-1].getSouth() &&
			 !wallsInfoNoBall[ballNodeX][ballNodeY-1].getNorth() &&
			 !wallsInfoNoBall[ballNodeX][ballNodeY-2].getSouth();

			if(temp==false)
			{
				positionTag[1]=1;
			}
			 */
					
		}
		catch(Exception e)
		{
			//System.out.println("Der Robot kann die Position von South des Robots nicht erreichen");
		}

	}
	
	//umtauscht Zeile und Spalte von Wallsinformation
	public G4_Wall[][] change(G4_Wall[][] input)
	{
		G4_Wall[][] output=new G4_Wall[width][height];
		
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				output[i][j]=new G4_Wall();
				output[i][j].setEast(input[j][i].getEast());
				output[i][j].setSouth(input[j][i].getSouth());
				output[i][j].setWest(input[j][i].getWest());
				output[i][j].setNorth(input[j][i].getNorth());
			}
		}
				
		
		return output;
	}
	
	//gebe Positioninformation zurueck
	public int[] getPositionTag()
	{
		return positionTag;
	}
}
