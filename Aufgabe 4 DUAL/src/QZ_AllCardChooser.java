import roborally.task.Card;

/**
 * Erzeugt die Karten,die der Roboter braucht
 */

/**
 * @author Qi Zheng
 *
 */
public class QZ_AllCardChooser {

	private Card[] userCards;
	//private Card[] myCards;
	private String[] userCardsString;
	private String[] myCardsString;
	private boolean[] tagT;
	private boolean[] tag;
	private int[] tagIndex;
	private Card[] myTurn={null,null,null,null,null};
	private String stringtemp;
	
	public QZ_AllCardChooser(Card[] userCards,String stringtemp) {
		this.userCards=userCards;
		//this.myCards=myCards;
		this.userCardsString=new QZ_CardChangeToString(this.userCards).getStringCards();
		//this.myCardsString=new QZ_CardChangeToString(this.myCards).getStringCards();
		this.stringtemp=stringtemp;
		this.myCardsString=new String[1];
		this.myCardsString[0]=this.stringtemp;
		tagT=new boolean[userCards.length];
		tag=new boolean[userCards.length];
		tagIndex=new int[userCards.length];
		initTagT();
		initTagIndex();
		cardsErzeuger();
	
	}
	
	private void cardsErzeuger()
	{
		//Ob eine Kommbination der ausgewaehlt Karte zu Verfuegung steht	
		for(int i=0;i<myCardsString.length;i++)
		{
			QZ_OneCardChooser onecardchoosertemp=new QZ_OneCardChooser(userCardsString,myCardsString[i],tagT);
			
			if(!onecardchoosertemp.getE())
			{
				break;
			}			
			else
			{
				this.tag=onecardchoosertemp.getTag();
				getTagIndex();
			}
			
			tagT=onecardchoosertemp.getTagT();
		}
	
		//findet keine Kommbination mehr
		if(indexTagTotal(tagIndex)==true)
		{
			//取5张，然后生成card类型
			CardChangeToCard();
		}
		else
		{
			if(indexTagTotal(tagIndex)==false)
			{
				QZ_OneCardChooser onecardchoosertemp=new QZ_OneCardChooser(userCardsString,"ZCW",tagT);
				this.tag=onecardchoosertemp.getTag();
				getTagIndex();
				tagT=onecardchoosertemp.getTagT();
			}
			
			if(indexTagTotal(tagIndex)==false)
			{
				QZ_OneCardChooser onecardchoosertemp=new QZ_OneCardChooser(userCardsString,"ZCCW",tagT);
				this.tag=onecardchoosertemp.getTag();
				getTagIndex();
				tagT=onecardchoosertemp.getTagT();
			}
			
			if(indexTagTotal(tagIndex)==false)
			{
				QZ_OneCardChooser onecardchoosertemp=new QZ_OneCardChooser(userCardsString,"ZU",tagT);
				this.tag=onecardchoosertemp.getTag();
				getTagIndex();
				tagT=onecardchoosertemp.getTagT();
			}
			
			if(indexTagTotal(tagIndex)==false)
			{
				QZ_OneCardChooser onecardchoosertemp=new QZ_OneCardChooser(userCardsString,"ZONEFW",tagT);
				this.tag=onecardchoosertemp.getTag();
				getTagIndex();
				tagT=onecardchoosertemp.getTagT();
			}
			
			if(indexTagTotal(tagIndex)==false)
			{
				QZ_OneCardChooser onecardchoosertemp=new QZ_OneCardChooser(userCardsString,"ZTWOFW",tagT);
				this.tag=onecardchoosertemp.getTag();
				getTagIndex();
				tagT=onecardchoosertemp.getTagT();
			}
			
			if(indexTagTotal(tagIndex)==false)
			{
				QZ_OneCardChooser onecardchoosertemp=new QZ_OneCardChooser(userCardsString,"ZTHREEFW",tagT);
				this.tag=onecardchoosertemp.getTag();
				getTagIndex();
				tagT=onecardchoosertemp.getTagT();
			}
			
			if(indexTagTotal(tagIndex)==false)
			{
				QZ_OneCardChooser onecardchoosertemp=new QZ_OneCardChooser(userCardsString,"ZBW",tagT);
				this.tag=onecardchoosertemp.getTag();
				getTagIndex();
				tagT=onecardchoosertemp.getTagT();
			}
			
			//取5张，然后生成card类型
			CardChangeToCard();
		}
		
	}
	
	private void initTagT()
	{
		for(int i=0;i<tagT.length;i++)
		{
			tagT[i]=false;
		}
	}
	
	private void initTagIndex()
	{
		for(int i=0;i<tagIndex.length;i++)
		{
			tagIndex[i]=-1;
		}
	}

	private void getTagIndex()
	{
		//CW,CCW,U
		for(int i=0;i<tag.length;i++)
		{
			for(int j=0;j<tagIndex.length;j++)
			{
				if(tagIndex[j]==-1 && tag[i]==true)
				{
					if(userCardsString[i]=="CW" || userCardsString[i]=="CCW" || userCardsString[i]=="U")
					{
						tagIndex[j]=i;
						tag[i]=false;	//hier ist sehr wichtig
						tagT[i]=true;	//
						break;
					}
					
				}
			}
		}
		
		//1FW,2FW,3FW,BW
		for(int i=0;i<tag.length;i++)
		{
			for(int j=0;j<tagIndex.length;j++)
			{
				if(tagIndex[j]==-1 && tag[i]==true)
				{
					tagIndex[j]=i;
					break;
				}
			}
		}
	}
	
	//Ob wir fuenf Karten haben
	private boolean indexTagTotal(int[] tagIndex)
	{
		int temp=0;
		
		for(int i=0;i<tagIndex.length;i++)
		{
			if(tagIndex[i]!=-1)
			{
				temp++;
			}
		}
		if(temp>4)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Erzeugt die Karten mit DatenTyp Card
	private void CardChangeToCard()
	{
		for(int i=0;i<myTurn.length;i++)
		{
			myTurn[i]=userCards[tagIndex[i]];
		}
	}
	
	
	//Gibt fuenf Karten zurueck
	public Card[] getCards()
	{
		//Debug
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		for(int i=0;i<myTurn.length;i++)
		{
			System.out.println("Card"+i+"----->"+myTurn[i]);
		}
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n\n\n");	
		
		return myTurn;
	}
	
}
