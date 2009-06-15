import roborally.task.Card;
import roborally.task.Constants.CardType;

/**
 * @author Qi Zheng
 *
 */
public class QZ_OneCardChooser {

	private String[] userCards;
	private String myCard;
	private boolean tag[];
	
	public QZ_OneCardChooser(String[] userCards,String myCard) 
	{
		this.userCards=userCards;
		this.myCard=myCard;
		tag=new boolean[userCards.length];
		cardChooser();
	}
		
	void cardChooser()
	{
		
		if(myCard=="CW"){cwCard();}
		if(myCard=="CCW"){ccwCard();}
		if(myCard=="U"){uCard();}
		if(myCard=="ONEFW"){oneFWCard();}
		if(myCard=="TWOFW"){twoFWCard();}
		if(myCard=="THREEFW"){threeFWCard();}
		if(myCard=="BW"){bwCard();}
		
		
	}
	
	//waehl CWCard
	private void cwCard()
	{
		//CW		
		for(int i=0;i<userCards.length;i++)
		{
			if(userCards[i]=="CW")
			{
				tag[i]=true;
				return;
			}
		}
		
		//U+CCW
		{
			boolean utemp=false;
			boolean ccwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="U")
				{
					tag[i]=true;
					utemp=true;
					if(utemp==true && ccwtemp==true){return;}
				}
				
				if(userCards[i]=="CCW")
				{
					tag[i]=true;
					ccwtemp=true;
					if(utemp==true && ccwtemp==true){return;}
				}
			}
		}
				
		//3CCW
		{
			int ccwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="CCW")
				{
					tag[i]=true;
					ccwtemp++;
					if(ccwtemp==3){return;}
				}
			}
		}
				
	}
	
	
	//Waehl CCWCard
	private void ccwCard()
	{
		
	
	}
	
	//Waehl UCard
	void uCard()
	{
		//U		
		for(int i=0;i<userCards.length;i++)
		{
			if(userCards[i]=="U")
			{
				tag[i]=true;
				return;
			}
		}
		
		//2CW
		{
			int cwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="CW")
				{
					tag[i]=true;
					cwtemp++;
					if(cwtemp==2){return;}
				}
			}
		}
				
		//2CCW
		{
			int ccwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="CCW")
				{
					tag[i]=true;
					ccwtemp++;
					if(ccwtemp==2){return;}
				}
			}
		}
	}

	
	//Waehl OneFWCard
	void oneFWCard()
	{
		//1FW		
		for(int i=0;i<userCards.length;i++)
		{
			if(userCards[i]=="ONEFW")
			{
				tag[i]=true;
				return;
			}
		}
		
		//U+BW
		{
			boolean uwtemp=false;
			boolean bwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="U")
				{
					tag[i]=true;
					uwtemp=true;
					if(uwtemp==true && bwtemp==true){return;}
				}
				
				if(userCards[i]=="BW")
				{
					tag[i]=true;
					bwtemp=true;
					if(uwtemp==true && bwtemp==true){return;}
				}
			}
		}
		
		//2FW+BW		
		{
			boolean twofwtemp=false;
			boolean bw=true;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="TWOFW")
				{
					tag[i]=true;
					if(twofwtemp==true && bw==true){return;}
				}
				if(userCards[i]=="BW")
				{
					tag[i]=true;
					if(twofwtemp==true && bw==true){return;}
				}
			}
		}
		

		//3FW+2*BW		
		{
			boolean twofwtemp=false;
			int bw=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="THREEFW")
				{
					tag[i]=true;
					twofwtemp=true;
					if(twofwtemp==true && bw==2){return;}
				}
				if(userCards[i]=="BW")
				{
					tag[i]=true;
					bw++;
					if(twofwtemp==true && bw==2){return;}
				}
			}
		}
			
	}
		
	
	void twoFWCard()
	{
		//2FW		
		for(int i=0;i<userCards.length;i++)
		{
			if(userCards[i]=="TWOFW")
			{
				tag[i]=true;
				return;
			}
		}
		
		//1FW*2
		{
			int onefwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="ONEFW")
				{
					tag[i]=true;
					onefwtemp++;
					if(onefwtemp==2){return;}
				}
			}
		}
		
		//3FW+BW		
		{
			boolean threefwtemp=false;
			boolean bw=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="THREEFW")
				{
					tag[i]=true;
					threefwtemp=true;
					if(threefwtemp==true && bw==true){return;}
				}
				if(userCards[i]=="BW")
				{
					tag[i]=true;
					bw=true;
					if(threefwtemp==true && bw==true){return;}
				}
			}
		}
		

		//U+2*BW		
		{
			boolean utemp=false;
			int bw=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="U")
				{
					tag[i]=true;
					utemp=true;
					if(utemp==true && bw==2){return;}
				}
				if(userCards[i]=="BW")
				{
					tag[i]=true;
					bw++;
					if(utemp==true && bw==2){return;}
				}
			}
		}
	}
	
	//3FW
	void threeFWCard()
	{
		//3FW		
		for(int i=0;i<userCards.length;i++)
		{
			if(userCards[i]=="THREEFW")
			{
				tag[i]=true;
				return;
			}
		}
		
		//2FW+1FW		
		{
			boolean twofwtemp=false;
			boolean onefwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="TWOFW")
				{
					tag[i]=true;
					twofwtemp=true;
					if(onefwtemp==true && twofwtemp==true){return;}
				}
				
				if(userCards[i]=="ONEFW")
				{
					tag[i]=true;
					onefwtemp=true;
					if(onefwtemp==true && twofwtemp==true){return;}
				}
			}
		}
		
		
		//1FW*3		
		{
			int onefwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="ONEFW")
				{
					tag[i]=true;
					onefwtemp++;
					if(onefwtemp==3){return;}
				}
			}
		}
		
		//U+
			
	}
	
	
	void bwCard(){}
	
}
