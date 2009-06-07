import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Qi Zheng
 *
 *Diese Klasse erzeugt einen kurzsten Weg von Startposition bis Zielsposition .
 */
public class QZ_TestMap {
	
	final static public int[] HIT = { 1 };	
	private List list=new LinkedList();		
	private QZ_Node[] minPath=null;	
	private List<QZ_Node> templist=new LinkedList<QZ_Node>();	
	
	/**
	 * @param wallsInfo		//Die Mauern Information ueber dem Spielfeld
	 * @param currentNodeXX	//Die x_koordinate der Startposition
	 * @param currentNodeYY	//Die y_koordinate der Startposition
	 * @param flagNodeXX	//Die x_koordinate der Zielsposition
	 * @param flagNodeYY	//Die y_koordinate der Zielsposition
	 * @param heightt		//Die Hoehe des Spielfeldes
	 * @param widthh		//die Laenge des Spielfeldes
	 */
	public QZ_TestMap(QZ_Wall[][] wallsInfo,int currentNodeXX,int currentNodeYY,int flagNodeXX,int flagNodeYY,int heightt,int widthh) {
		
		myMiniPath(wallsInfo,currentNodeXX,currentNodeYY,flagNodeXX,flagNodeYY,heightt,widthh);
										
	}
		
	/**
	 * @param wallsInfo		//Die Mauern Information ueber dem Spielfeld
	 * @param currentNodeXX	//Die x_koordinate der Startposition
	 * @param currentNodeYY	//Die y_koordinate der Startposition
	 * @param flagNodeXX	//Die x_koordinate der Zielsposition
	 * @param flagNodeYY	//Die y_koordinate der Zielsposition
	 * @param heightt		//Die Hoehe des Spielfeldes
	 * @param widthh		//die Laenge des Spielfeldes
	 * 
	 * 
	 *Erzeugt eine Liste fuer einen kurzsten Weg von Startposition bis Zielsposition von Spielfeld. 
	 */
	public void myMiniPath(QZ_Wall[][] wallsInfo,int currentNodeXX,int currentNodeYY,int flagNodeXX,int flagNodeYY,int heightt,int widthh){
		
		int currentNodeX=currentNodeYY*2+1;
		int currentNodeY=currentNodeXX*2+1;
		int flagNodeX=flagNodeYY*2+1;
		int flagNodeY=flagNodeXX*2+1;
			
		int height=heightt;
		int width=widthh;
		
		//膨胀
		int[][] myWallsDouble=new int[height*2][width*2];
		for(int i=0;i<height*2;i++)
		{
			for(int j=0;j<width*2;j++)
			{
				myWallsDouble[i][j]=0;
			}
		}
		
		//Debug
		/*
		 * for(int i=0;i<height*2;i++)
		{
			for(int j=0;j<width*2;j++)
			{
				System.out.println("i ="+i+"j ="+j+"-------->"+myWallsDouble[i][j]);
			}
			
			System.out.println("\n\n\n");
		}
		 */
		
		//加入信息
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if (wallsInfo[i][j].getEast()==true) {myWallsDouble[2*i][2*j+1]=1;myWallsDouble[2*i+1][2*j+1]=1;}
				if (wallsInfo[i][j].getSouth()==true) {myWallsDouble[2*i+1][2*j]=1;myWallsDouble[2*i+1][2*j+1]=1;}
			}
		}
		
		//Debug
		/*
		 * for(int i=0;i<height*2;i++)
		{
			for(int j=0;j<width*2;j++)
			{
				System.out.println("i ="+i+"j ="+j+"-------->"+myWallsDouble[i][j]);
			}
			
			System.out.println("\n\n\n");
		}
		 * 
		 */
		
		//加框
		int[][] myWallsDoubleRahmen=new int[height*2+2][width*2+2];
		for(int i=0;i<height*2+2;i++)
		{
			myWallsDoubleRahmen[i][0]=1;
			myWallsDoubleRahmen[i][width*2+2-1]=1;
		}
		
		for(int i=0;i<width*2+2;i++)
		{
			myWallsDoubleRahmen[0][i]=1;
			myWallsDoubleRahmen[height*2+2-1][i]=1;
		}
		
		for(int i=0;i<height*2;i++)
		{
			for(int j=0;j<width*2;j++)
			{
				myWallsDoubleRahmen[i+1][j+1]=myWallsDouble[i][j];
			}
		}

		//Debug
		/*
		 * for(int i=0;i<height*2+2;i++)
		{
			for(int j=0;j<width*2+2;j++)
			{
				System.out.println("i ="+i+"j ="+j+"-------->"+myWallsDoubleRahmen[i][j]);
			}
			
			System.out.println("\n\n\n");
		}
		 */
		
		
		//行列交换
		int temp[][]=new int[height*2+2][width*2+2];
		for(int i=0;i<height*2+2;i++)
		{
			for(int j=0;j<width*2+2;j++)
			{
				temp[i][j]=myWallsDoubleRahmen[j][i];
			}
		
		}
		
		//Debug
		//int[][] pp={{1,1,1,1,1,1},{1,0,1,0,0,1},{1,0,1,0,0,1},{1,0,1,0,0,1},{1,0,0,0,0,1},{1,1,1,1,1,1}};
						
		QZ_PathFinder pathfinder=new QZ_PathFinder(temp,HIT);
		list=pathfinder.searchPath(new Point(currentNodeX,currentNodeY), new Point(flagNodeX,flagNodeY));
		minPath=new QZ_Node[list.size()];
		list.toArray(minPath);
		
		//Debug
		//Print des kurztstem Weg
		/*
		 * for(int i=0;i<minPath.length;i++)
		{
			System.out.println((minPath[i]._pos.x-1)+"++++++++"+(minPath[i]._pos.y-1));
		}
		 */
			
		
		//缩小，把膨胀的图形变成原始的
		int k=0;
		while(k<minPath.length){
			if((minPath[k]._pos.x-1)%2!=0)
			{
				minPath[k]._pos.x=minPath[k]._pos.x-2;
			}
			else
			{
				minPath[k]._pos.x=minPath[k]._pos.x-1;
			}
			
			if((minPath[k]._pos.y-1)%2!=0)
			{
				minPath[k]._pos.y=minPath[k]._pos.y-2;
			}
			else
			{
				minPath[k]._pos.y=minPath[k]._pos.y-1;
			}
			
			int tempp=0;
			
			tempp=minPath[k]._pos.x;
			minPath[k]._pos.x=minPath[k]._pos.y/2;
			minPath[k]._pos.y=tempp/2;
						
			k++;
		}
		
		//Debug
		//Print des kurztstem Weg
		/*
		 * for(int i=0;i<minPath.length;i++)
		{
			System.out.println((minPath[i]._pos.x)+"++++++++"+(minPath[i]._pos.y));
		}
		 */
			
				
		templist.add(minPath[0]);
		for(int i=1;i<minPath.length;i++)
		{	
			if(minPath[i-1]._pos.x!=minPath[i]._pos.x || minPath[i-1]._pos.y!=minPath[i]._pos.y)
			{
				templist.add(minPath[i]);
			}
		}
		 
				
		//Debug
		System.out.println("\n\n\nDer kuertzste Weg");
		for(int i=0;i<templist.size();i++)
		{
			System.out.println(i+"--->("+(templist.get(i)._pos.x)+","+(templist.get(i)._pos.y)+")");
		}
	}
	
		
	/**
	 * Liefert eine Liste des kurzsten Wegs
	 */
	public List<QZ_Node> getMiniPath()
	{
		return this.templist;
	}

}
