package ccf;

import java.util.List;

public class TableData 
{
   public static final int PROTECTION = 0;
   public static final int PROFIT = 1;
   
   private List<OptionQuote> trades;
   
   public List<OptionQuote> sortTrades(int sortBy)
   {
      if(sortBy == PROTECTION)
      {
      	trades.sort((quote1, quote2) -> quote1.getProtection().compareTo(quote2.getProtection()));
      }
      else if(sortBy == PROFIT)
      {
      	trades.sort((quote1, quote2) -> quote1.getProfitAPR().compareTo(quote2.getProfitAPR()));
      }
      
      return trades;
   }
   
   public List<OptionQuote> getTrades() 
   {
      return trades;
   }
   public void setTrades(List<OptionQuote> trades) 
   {
      this.trades = trades;
   }
}
