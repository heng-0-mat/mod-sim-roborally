import java.util.LinkedList;
import java.util.List;

import roborally.task.Card;


public class QZ_RoboMoveToBall {
	private int ballNodeX;
	private int ballNodeY;
	private int roboNodeX;
	private int roboNodeY;
	private int nextNodeX;
	private int nextNodeY;
	private int goalNodeX;
	private int goalNodeY;
	private String dir;
	private QZ_Wall[][] wallsInfo;
	private int width;
	private int height;
	private Card[] cards;
	Card[] myTurn =  { null, null, null, null, null };
	int numCards = 0;
	List<QZ_Node> getMiniPath=new LinkedList<QZ_Node>();
	
		
	public QZ_RoboMoveToBall(QZ_Wall[][] wallsInfo,int width,int height,Card[] cards,int ballNodeX,int ballNodeY,int roboNodeX,int roboNodeY,String dir,int nextNodeX,int nextNodeY)
	{
		this.cards=cards;
		this.ballNodeX=ballNodeX;
		this.ballNodeY=ballNodeY;
		this.roboNodeX=roboNodeX;
		this.roboNodeY=roboNodeY;
		this.dir=dir;
		this.nextNodeX=nextNodeX;
		this.nextNodeY=nextNodeY;
		this.wallsInfo=wallsInfo;
		this.width=width;
		this.height=height;
		CalLastNode();
		move();
	}
	
	//������һ��λ��
	private void CalLastNode()
	{
		if(nextNodeX<ballNodeX)
		{
			goalNodeX=ballNodeX+1;
			goalNodeY=ballNodeY;
			}
		else
		{
			if(nextNodeX>ballNodeX)
			{
				goalNodeX=ballNodeX-1;
				goalNodeY=ballNodeY;
			}
			else
			{
				if(nextNodeY<ballNodeY)
				{
					goalNodeX=ballNodeX;
					goalNodeY=ballNodeY+1;
				}
				else
				{
					if(nextNodeY>ballNodeY)
					{
						goalNodeX=ballNodeX;
						goalNodeY=ballNodeY-1;
					}
				}
			}
		}
		
		//Debug
		//System.out.println("roboNodeX="+roboNodeX+",roboNodeY="+roboNodeY);
		//System.out.println("nextNodeX="+nextNodeX+",nextNodeY="+nextNodeY);
		//System.out.println("ballNodeX="+ballNodeX+",ballNodeY="+ballNodeY);
		//System.out.println("goalNodeX="+goalNodeX+",goalNodeY="+goalNodeY);
	}
	
	
	//�������ƶ�
	public void move()
	{	
		//�Ƿ���һ��·��
		Boolean ExiAPathVar=true;
		try
		{
			//QZ_TestMap testmap=
			new QZ_TestMap(wallsInfo,roboNodeX,roboNodeY,goalNodeX,goalNodeY,height,width);
			
		}
		catch(Exception e)
		{
			ExiAPathVar=false;
		}
		
		if(ExiAPathVar)
		{
			ExiAPath();
		}
		else
		{
			NoneAPath();
		}
		
	}
	
	//�������ܵ�����ĺ���һ��λ��
	private Card[] ExiAPath()
	{
		//�Ѿ�������ĺ���һ��λ�ã�������Ҫ�����������ſ�
		if(roboNodeX==goalNodeX && roboNodeY==goalNodeY)
		{			
			QZ_Move_No_WallsInfo movestringnowalls=new QZ_Move_No_WallsInfo(roboNodeX,roboNodeY,ballNodeX,ballNodeY,dir);
			QZ_CardChange cardchange=new QZ_CardChange(movestringnowalls.getCards(),cards);	
			
			myTurn=cardchange.getMyTurn();
		}
		//��û�е���������λ��
		else
		{
			try
			{									
				QZ_Move5 testmove5string=new QZ_Move5(wallsInfo,width,height,roboNodeX,roboNodeY,dir,goalNodeX,goalNodeY);
				QZ_CardChange cardchange=new QZ_CardChange(testmove5string.getCards(),cards);
				
				myTurn=cardchange.getMyTurn();
				
				//Debug
				/*
				 * System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				for(int i=0;i<cardchange.getMyTurn().length;i++)
				{
					System.out.println("Card"+i+"----->"+cardchange.getMyTurn()[i]);
				}
				System.out.println("Richtung----->"+dir);
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n\n\n");	
				 */
			}
			catch(Exception e)
			{
				System.out.println("kknd");
			}
		}
		
		return myTurn;
	}
	
	
	//�������޷�����������λ��
	private Card[] NoneAPath()
	{
		//������ô����ǰ��һ��
		try
		{//����Ѿ����յ㣬�޷��������·���Ļ�
			//ǿ����ǰ��һ��			
			QZ_Move_No_WallsInfo movestringnowalls=new QZ_Move_No_WallsInfo(roboNodeX,roboNodeY,ballNodeX,ballNodeY,dir);
			QZ_CardChange cardchange=new QZ_CardChange(movestringnowalls.getCards(),cards);	
			
			myTurn=cardchange.getMyTurn();
		}
		catch(Exception e)
		{
			
		}
			
		
		return myTurn;
	}
	
	public Card[] getCards()
	{				
		return this.myTurn;
	}
}
