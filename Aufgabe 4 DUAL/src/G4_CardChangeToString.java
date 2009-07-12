import roborally.task.Card;
import roborally.task.Constants.CardType;

/**
 * @author Qi Zheng
 *
 */
public class G4_CardChangeToString {

	private Card[] cards;
	private String[] stringCards;
	
	public G4_CardChangeToString(Card[] cards) 
	{
		this.cards=cards;
		stringCards=new String[cards.length];
		change();
	}
	
	private void change()
	{
		for(int i=0;i<cards.length;i++)
		{
			if ( cards[i].getCardType() == CardType.Move_Forward_Card)
			{
				stringCards[i]="ONEFW";
			}
			
			if ( cards[i].getCardType() == CardType.Move_Two_Forward_Card)
			{
				stringCards[i]="TWOFW";
			}
			
			if ( cards[i].getCardType() == CardType.Move_Three_Forward_Card)
			{
				stringCards[i]="THREEFW";
			}
			
			if ( cards[i].getCardType() == CardType.Move_Backward_Card)
			{
				stringCards[i]="BW";
			}
			
			if ( cards[i].getCardType() == CardType.Rotate_CW_Card)
			{
				stringCards[i]="CW";
			}
			
			if ( cards[i].getCardType() == CardType.Rotate_CCW_Card)
			{
				stringCards[i]="CCW";
			}
			
			if ( cards[i].getCardType() == CardType.U_Turn_Card)
			{
				stringCards[i]="U";
			}
		}
		
		
	}
	
	public String[] getStringCards()
	{
		return stringCards;
	}
	
}
