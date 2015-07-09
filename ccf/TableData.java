package ccf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class TableData 
{
   public static final int PROTECTION = 0;
   public static final int PROFIT = 1;
   
   private ArrayList<OptionQuote> trades;
   
   public ArrayList<OptionQuote> sortTrades(int sortBy)
   {
      if(sortBy == PROTECTION)
      {
         Collections.sort(trades, new Comparator<OptionQuote>() {
            @Override
            public int compare(OptionQuote quote1, OptionQuote quote2)
            {
               try
               {
                  
                   if(quote1.getProtection() > quote2.getProtection())
                   {
                      return -1;
                   }
                   else
                   {
                      return 1;
                   }
               }
               catch(Exception e)
               {
                  e.printStackTrace();
               }
               
               return 0;
            }
        });
      }
      else if(sortBy == PROFIT)
      {
         Collections.sort(trades, new Comparator<OptionQuote>() {
            @Override
            public int compare(OptionQuote quote1, OptionQuote quote2)
            {
               try
               {
                   if(quote1.getProfitAPR() > quote2.getProfitAPR())
                   {
                      return -1;
                   }
                   else
                   {
                      return 1;
                   }
               }
               catch(Exception e)
               {
                  e.printStackTrace();
               }
               
               return 0;
            }
        });
      }
      
      return trades;
   }
   
   public ArrayList<OptionQuote> getTrades() 
   {
      return trades;
   }
   public void setTrades(ArrayList<OptionQuote> trades) 
   {
      this.trades = trades;
   }
}
