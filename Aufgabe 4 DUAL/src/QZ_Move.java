

public class QZ_Move{
	public String[] myTurn;
	public String dir;
	
	public QZ_Move(int currentNodeX,int currentNodeY,int nextX,int nextY,String orientation)
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
				myTurn=new String[1];
				myTurn[0]="move";
				
			}
			
			if(orientation=="east")
			{
				myTurn=new String[2];
				myTurn[0]="ccw";
				myTurn[1]="move";
							
			}
			
			if(orientation=="west")
			{
				myTurn=new String[2];
				myTurn[0]="cw";
				myTurn[1]="move";
				
			}
			
			if(orientation=="south")
			{
				myTurn=new String[2];
				myTurn[0]="turn";
				myTurn[1]="move";
									
			}
			
			dir="north";
		}
		
		
		
		//下一步在下面
		if(nextY>currentNodeY)
		{
			if(orientation=="north")
			{
				myTurn=new String[2];
				myTurn[0]="turn";
				myTurn[1]="move";
						
			}
			
			if(orientation=="east")
			{
				myTurn=new String[2];
				myTurn[0]="cw";
				myTurn[1]="move";
				
			}
			
			if(orientation=="west")
			{
				myTurn=new String[2];
				myTurn[0]="ccw";
				myTurn[1]="move";
						
			}
			
			if(orientation=="south")
			{				
				myTurn=new String[1];
				myTurn[0]="move";
				
			}
		
			dir="south";
		}
		
		
		
		//下一步往右边
		if(nextX>currentNodeX)
		{
			if(orientation=="north")
			{
				myTurn=new String[2];
				myTurn[0]="cw";
				myTurn[1]="move";
							
			}
			
			if(orientation=="east")
			{				
				myTurn=new String[1];
				myTurn[0]="move";
				
			}
			
			if(orientation=="west")
			{
				myTurn=new String[2];
				myTurn[0]="turn";
				myTurn[1]="move";
								
			}
			
			if(orientation=="south")
			{
				myTurn=new String[2];
				myTurn[0]="ccw";
				myTurn[1]="move";
									
			}
			
			dir="east";
		}
		 
		
		//下一步往左边
		if(nextX<currentNodeX)
		{
			if(orientation=="north")
			{
				myTurn=new String[2];
				myTurn[0]="ccw";
				myTurn[1]="move";
							
			}
			
			if(orientation=="east")
			{
				myTurn=new String[2];
				myTurn[0]="turn";
				myTurn[1]="move";
							
			}
			
			if(orientation=="west")
			{						
				myTurn=new String[1];
				myTurn[0]="move";
				
			}
			
			if(orientation=="south")
			{
				myTurn=new String[2];
				myTurn[0]="cw";
				myTurn[1]="move";
								
			}
			
			dir="west";
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
