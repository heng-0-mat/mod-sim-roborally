

public class QZ_Move_No_WallsInfo{
	public String[] myTurn=new String[5];
	public String dir;
	private int numCards=0;
	
	public QZ_Move_No_WallsInfo(int currentNodeX,int currentNodeY,int nextX,int nextY,String orientation)
	{
		
		this.myTurn=move(currentNodeX,currentNodeY,nextX,nextY,orientation);
		
	}
	
	//move
	private String[] move(int currentNodeX,int currentNodeY,int nextX,int nextY,String orientation)
	{	
						
		//下一步在上面
		if(nextY<currentNodeY)
		{
			if(orientation=="north")
			{
				
				myTurn[0]="move";
				numCards=1;
			}
			
			if(orientation=="east")
			{
				
				myTurn[0]="ccw";
				myTurn[1]="move";
				numCards=2;			
			}
			
			if(orientation=="west")
			{
				
				myTurn[0]="cw";
				myTurn[1]="move";
				numCards=2;
			}
			
			if(orientation=="south")
			{
				
				myTurn[0]="turn";
				myTurn[1]="move";
				numCards=2;					
			}
			
			dir="north";
		}
		
		
		
		//下一步在下面
		if(nextY>currentNodeY)
		{
			if(orientation=="north")
			{
				
				myTurn[0]="turn";
				myTurn[1]="move";
				numCards=2;		
			}
			
			if(orientation=="east")
			{
				
				myTurn[0]="cw";
				myTurn[1]="move";
				numCards=2;	
			}
			
			if(orientation=="west")
			{
				
				myTurn[0]="ccw";
				myTurn[1]="move";
				numCards=2;			
			}
			
			if(orientation=="south")
			{				
				
				myTurn[0]="move";
				numCards=1;	
			}
		
			dir="south";
		}
		
		
		
		//下一步往右边
		if(nextX>currentNodeX)
		{
			if(orientation=="north")
			{
				
				myTurn[0]="cw";
				myTurn[1]="move";
				numCards=2;				
			}
			
			if(orientation=="east")
			{				
				
				myTurn[0]="move";
				numCards=1;	
			}
			
			if(orientation=="west")
			{
				
				myTurn[0]="turn";
				myTurn[1]="move";
				numCards=2;					
			}
			
			if(orientation=="south")
			{
				
				myTurn[0]="ccw";
				myTurn[1]="move";
				numCards=2;						
			}
			
			dir="east";
		}
		 
		
		//下一步往左边
		if(nextX<currentNodeX)
		{
			if(orientation=="north")
			{
				
				myTurn[0]="ccw";
				myTurn[1]="move";
				numCards=2;				
			}
			
			if(orientation=="east")
			{
				
				myTurn[0]="turn";
				myTurn[1]="move";
				numCards=2;				
			}
			
			if(orientation=="west")
			{						
				
				myTurn[0]="move";
				numCards=1;	
			}
			
			if(orientation=="south")
			{
				
				myTurn[0]="cw";
				myTurn[1]="move";
				numCards=2;					
			}
			
			dir="west";
		}
							
		
		while(numCards<5)
		{
			myTurn[numCards]="turn";
			numCards++;
		}
		return myTurn;
	}
	
	//gibt einen Kartensatz zurueck
	public String[] getCards()
	{
		
		return this.myTurn;
	}
	
	public String getDir()
	{
		//Debug
		//System.out.println("Richtung----->"+dir);
		
		return this.dir;
	}
		
}
