
/**
 * @author Qi Zheng
 *
 *Stellt alle moegliche Kartenkombination
 */
public class QZ_OneCardChooser {

	private String[] userCards;
	private String myCard;
	private boolean tagT[];
	private boolean tag[];
	
	public QZ_OneCardChooser(String[] userCards,String myCard,boolean[] tagT) 
	{
		this.userCards=userCards;
		this.myCard=myCard;
		this.tagT=tagT;
		tag=new boolean[tagT.length];
		initTag();
		cardChooser();
	}
		
	private void cardChooser()
	{
		
		if(myCard=="CW"){cwCard();}
		if(myCard=="CCW"){ccwCard();}
		if(myCard=="U"){uCard();}
		if(myCard=="ONEFW"){oneFWCard();}
		if(myCard=="TWOFW"){twoFWCard();}
		if(myCard=="THREEFW"){threeFWCard();}
		if(myCard=="BW"){bwCard();}
		
		if(myCard=="ZCW"){zcwCard();}
		if(myCard=="ZCCW"){zccwCard();}
		if(myCard=="ZU"){zuCard();}
		if(myCard=="ZONEFW"){zonefwCard();}
		if(myCard=="ZTWOFW"){ztwofwCard();}
		if(myCard=="ZTHREEFW"){zthreefwCard();}
		if(myCard=="ZBW"){zbwCard();}
	}
	
	//waehlt CWCard
	private void cwCard()
	{
		//CW		
		for(int i=0;i<userCards.length;i++)
		{
			//Die Karte an diesr Position ist noch nicht ausgewaehlt
			if(tagT[i]==false)
			{
				if(userCards[i]=="CW")
				{
					tag[i]=true;
					return;
				}
			}
		}
		
		//U+CCW
		initTag();
		{
			boolean utemp=false;
			boolean ccwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="U" && utemp==false)
					{
						tag[i]=true;
						utemp=true;
						//if(utemp==true && ccwtemp==true){return;}
						return;
					}
					
					if(userCards[i]=="CCW" && ccwtemp==false)
					{
						tag[i]=true;
						ccwtemp=true;
						//if(utemp==true && ccwtemp==true){return;}
						return;
					}
				}
			}
		}
				
		//3CCW
		initTag();
		{
			int ccwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="CCW")
					{
						tag[i]=true;
						ccwtemp++;
						//if(ccwtemp==3){return;}
						return;
					}
				}
			}
		}
				
	}
	
	
	//Waehlt CCWCard
	private void ccwCard()
	{
		//CCW		
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="CCW")
				{
					tag[i]=true;
					return;
				}
			}
		}
		
		//U+CW
		initTag();
		{
			boolean utemp=false;
			boolean cwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="U" && utemp==false)
					{
						tag[i]=true;
						utemp=true;
						//if(utemp==true && cwtemp==true){return;}
						return;
					}
					
					if(userCards[i]=="CW" && cwtemp==false)
					{
						tag[i]=true;
						cwtemp=true;
						//if(utemp==true && cwtemp==true){return;}
						return;
					}
				}
			}
		}
				
		//3CW
		initTag();
		{
			int cwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="CW")
					{
						tag[i]=true;
						cwtemp++;
						//if(cwtemp==3){return;}
						return;
					}
				}
			}
		}
	
	}
	
	//Waehlt UCard
	private void uCard()
	{
		//U		
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="U")
				{
					tag[i]=true;
					return;
				}
			}
		}
		
		//2CW
		initTag();
		{
			int cwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="CW")
					{
						tag[i]=true;
						cwtemp++;
						//if(cwtemp==2){return;}
						return;
					}
				}
			}
		}
				
		//2CCW
		initTag();
		{
			int ccwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="CCW")
					{
						tag[i]=true;
						ccwtemp++;
						//if(ccwtemp==2){return;}
						return;
					}
				}
			}
		}
	}

	
	//Waehl OneFWCard
	private void oneFWCard()
	{
		//1FW		
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="ONEFW")
				{
					tag[i]=true;
					return;
				}
			}
		}
		
		//U+BW
		initTag();
		{
			boolean utemp=false;
			boolean bwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="U" && utemp==false)
					{
						tag[i]=true;
						utemp=true;
						//if(utemp==true && bwtemp==true){return;}
						return;
					}
					
					if(userCards[i]=="BW" && bwtemp==false && utemp==true)
					{
						tag[i]=true;
						bwtemp=true;
						//if(utemp==true && bwtemp==true){return;}
						return;
					}
				}
			}
		}
		
		//Das Block kann Momental nicht eingesetzt werden
		/*
		 * //2FW+BW		
		{
			boolean twofwtemp=false;
			boolean bwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="TWOFW")
				{
					tag[i]=true;
					if(twofwtemp==true && bwtemp==true){return;}
				}
				if(userCards[i]=="BW")
				{
					tag[i]=true;
					if(twofwtemp==true && bwtemp==true){return;}
				}
			}
		}
		 */
		
		
		//Das Block kann Momental nicht eingesetzt werden
		/*
		 * //3FW+2*BW		
		{
			boolean threefwtemp=false;
			int bwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(userCards[i]=="BW")
				{
					tag[i]=true;
					bwtemp++;
					if(threefwtemp==true && bwtemp==2){return;}
				}
				
				if(userCards[i]=="THREEFW")
				{
					tag[i]=true;
					threefwtemp=true;
					if(threefwtemp==true && bwtemp==2){return;}
				}
				
			}
		}
		 */
				
	}
		
	
	private void twoFWCard()
	{
		//2FW		
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="TWOFW")
				{
					tag[i]=true;
					return;
				}
			}
		}
		
		//1FW*2
		initTag();
		{
			int onefwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="ONEFW")
					{
						tag[i]=true;
						onefwtemp++;
						//if(onefwtemp==2){return;}
						return;
					}
				}
			}
		}
		
		//Das Block kann Momental nicht eingesetzt werden
		/*
		 * //3FW+BW		
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
		 */
		
		

		//U+2*BW		
		initTag();
		{
			boolean utemp=false;
			int bwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="U" && utemp==false)
					{
						tag[i]=true;
						utemp=true;
						//if(utemp==true && bwtemp==2){return;}
						return;
					}
					if(userCards[i]=="BW" && bwtemp!=2 && utemp==true)
					{
						tag[i]=true;
						bwtemp++;
						//if(utemp==true && bwtemp==2){return;}
						return;
					}
				}
			}
		}
	}
	
	
	//Waehlt 3FW
	private void threeFWCard()
	{
		//3FW		
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="THREEFW")
				{
					tag[i]=true;
					return;
				}
			}
		}
		
		//2FW+1FW		
		initTag();
		{
			boolean twofwtemp=false;
			boolean onefwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="TWOFW" && twofwtemp==false)
					{
						tag[i]=true;
						twofwtemp=true;
						//if(onefwtemp==true && twofwtemp==true){return;}
						return;
					}
					
					if(userCards[i]=="ONEFW" && onefwtemp==false && twofwtemp==true)
					{
						tag[i]=true;
						onefwtemp=true;
						//if(onefwtemp==true && twofwtemp==true){return;}
						return;
					}
				}
			}
		}
		
		
		//1FW*3		
		initTag();
		{
			int onefwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="ONEFW")
					{
						tag[i]=true;
						onefwtemp++;
						//if(onefwtemp==3){return;}
						return;
					}
				}
			}
		}
		
		
		//U+3*BW
		initTag();
		{
			boolean utemp=false;
			int bwtemp=0;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="U" && utemp==false)
					{
						tag[i]=true;
						utemp=true;
						//if(utemp==true && bwtemp==3){return;}
						return;
					}
					
					if(userCards[i]=="BW" && bwtemp!=3 && utemp==true)
					{
						tag[i]=true;
						bwtemp++;
						//if(utemp==true && bwtemp==3){return;}
						return;
					}
				}
			}
		}
			
	}
	
	
	//Waehlt CWCard
	private void bwCard()
	{
		//BW		
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="BW")
				{
					tag[i]=true;
					return;
				}
			}
		}
		
		//U+ONEFW
		initTag();
		{
			boolean utemp=false;
			boolean onefwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="U" && utemp==false)
					{
						tag[i]=true;
						utemp=true;
						//if(onefwtemp==true && utemp==true){return;}
						return;
					}
				
					if(userCards[i]=="ONEFW" && onefwtemp==false && utemp==true)
					{
						tag[i]=true;
						onefwtemp=true;
						//if(onefwtemp==true && utemp==true){return;}
						return;
					}
				}
			}
		}
		
		
		//2*CW+ONEFW
		initTag();
		{
			int cwtemp=0;
			boolean onefwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="CW" && cwtemp!=2)
					{
						tag[i]=true;
						cwtemp++;
						//if(onefwtemp==true && cwtemp==2){return;}
						return;
					}
			
					if(userCards[i]=="ONEFW" && onefwtemp==false && cwtemp==2)
					{
						tag[i]=true;
						onefwtemp=true;
						//if(onefwtemp==true && cwtemp==2){return;}
						return;
					}
				}				
			}
		}
		
		//2*CCW+ONEFW
		initTag();
		{
			int ccwtemp=0;
			boolean onefwtemp=false;
			
			for(int i=0;i<userCards.length;i++)
			{
				if(tagT[i]==false)
				{
					if(userCards[i]=="CCW" && ccwtemp!=2)
					{
						tag[i]=true;
						ccwtemp++;
						//if(onefwtemp==true && ccwtemp==2){return;}
						return;
					}
				
					if(userCards[i]=="ONEFW" && onefwtemp==false && ccwtemp==2)
					{
						tag[i]=true;
						onefwtemp=true;
						//if(onefwtemp==true && ccwtemp==2){return;}
						return;
					}
				}			
			}
		}
	}
	
	private void zcwCard()
	{
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="CW")
				{
					tag[i]=true;
				}
			}
		}
	}
	
	private void zccwCard()
	{
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="CCW")
				{
					tag[i]=true;
				}
			}
		}
	}
	
	private void zuCard()
	{
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="U")
				{
					tag[i]=true;
				}
			}
		}
	}
	
	private void zonefwCard()
	{
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="ONEFW")
				{
					tag[i]=true;
				}
			}
		}
	}
	
	private void ztwofwCard()
	{
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="TWOFW")
				{
					tag[i]=true;
				}
			}
		}
	}
	
	private void zthreefwCard()
	{
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="THREEFW")
				{
					tag[i]=true;
				}
			}
		}
	}
	
	private void zbwCard()
	{
		for(int i=0;i<userCards.length;i++)
		{
			if(tagT[i]==false)
			{
				if(userCards[i]=="BW")
				{
					tag[i]=true;
				}
			}
		}
	}
	
	
	//一次没有成功的取到卡，就要刷干净重新来
	private void initTag()
	{
		for(int i=0;i<tag.length;i++)
		{
			tag[i]=false;
		}
	}
	
	public boolean[] getTag()
	{
		//Debug
		for(int i=0;i<tag.length;i++)
		{
			//System.out.println("Card"+i+"--->"+tag[i]);
		}
		
		return this.tag;
	}
	
	public boolean[] getTagT()
	{
		for(int i=0;i<tagT.length;i++)
		{
			if(tag[i]==true)
			{
				tagT[i]=true;
			}
		}
		
		return this.tagT;
	}
	
	//Ob eine Kombination von Karten gefunden
	public boolean getE()
	{
		int temp=0;
		for(int i=0;i<tag.length;i++)
		{
			if(tag[i]==true)
			{
				temp++;
			}
		}
		
		if(temp==0)
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}
}
