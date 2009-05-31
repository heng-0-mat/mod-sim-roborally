import roborally.task.Card;
import roborally.task.Constants.CardType;

public class QZ_CardChange {

	private String[] myTurnString;
	private Card[] myTurn={null,null,null,null,null};
	private Card[] cards;
	public QZ_CardChange(String[] myTurnString,Card[] cards) 
	{
		this.myTurnString=myTurnString;
		this.cards=cards;
		change();
	}
	
	private void change()
	{
				
		for (Card c : cards)
		{
			if ( c.getCardType() == CardType.Move_Forward_Card)
			{
				for(int i=0;i<myTurnString.length;i++)
				{
					if(myTurnString[i]=="move")
					{
						myTurn[i]=c;
						myTurnString[i]="kknd";
						break;
					}
				}
			}
				
			if ( c.getCardType() == CardType.Rotate_CW_Card)
			{
				for(int i=0;i<myTurnString.length;i++)
				{
					if(myTurnString[i]=="cw")
					{
						myTurn[i]=c;
						myTurnString[i]="kknd";
						break;
					}
				}
			}
				
			if ( c.getCardType() == CardType.Rotate_CCW_Card)
			{
				for(int i=0;i<myTurnString.length;i++)
				{
					if(myTurnString[i]=="ccw")
					{
						myTurn[i]=c;
						myTurnString[i]="kknd";
						break;
					}
				}
			}	
			
			if ( c.getCardType() == CardType.U_Turn_Card)
			{
				for(int i=0;i<myTurnString.length;i++)
				{
					if(myTurnString[i]=="turn")
					{
						myTurn[i]=c;
						myTurnString[i]="kknd";
						break;
					}
				}
			}
			
			if ( c.getCardType() == CardType.Stationary_Card)
			{
				for(int i=0;i<myTurnString.length;i++)
				{
					if(myTurnString[i]=="stay")
					{
						myTurn[i]=c;
						myTurnString[i]="kknd";
						break;
					}
				}
			}
			
			
			if ( myTurnString[0]=="kknd" && myTurnString[1]=="kknd" && myTurnString[2]=="kknd" && myTurnString[3]=="kknd" && myTurnString[4]=="kknd" )
				break;
		}
			
	}
	
		
	public Card[] getMyTurn()
	{
		return myTurn;
	}
	
}
