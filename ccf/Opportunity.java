package ccf;

public class Opportunity implements Comparable<Opportunity>
{
   private OptionQuote putQuote;
   private OptionQuote callQuote;
   private int numOfContracts;
   private double commission;
   private String sortType = "Reversal";
   
   public Opportunity(OptionQuote callQuote, OptionQuote putQuote, int numOfContracts, double commission)
   {
      this.callQuote = callQuote;
      this.putQuote = putQuote;
      this.numOfContracts = numOfContracts;
      this.commission = commission;
   }
   
   public int compareTo(Opportunity other)
   {
      if(sortType.equals("Reversal") && getReversalDailyExpectation() > other.getReversalDailyExpectation())
      {
         return 1;
      }
      
      if(sortType.equals("Conversion") && getConversionAPRExpectation() > other.getConversionAPRExpectation())
      {
         return 1;
      }
      
      return 0;
   }
   
   public void setSortType(String sortType)
   {
      this.sortType = sortType;
   }
   
   public double getConversionExpectation()
   {
      return (double)numOfContracts * 100.0 * (callQuote.getBid() + callQuote.getStrike() - putQuote.getAsk() - callQuote.getUnderlyingQuote().getLast()) - commission;
   }
   
   public double getReversalExpectation()
   {
      return (double)numOfContracts * 100.0 * (putQuote.getBid() + callQuote.getUnderlyingQuote().getLast() - callQuote.getAsk() - callQuote.getStrike()) - commission;
   }
   
   public double getConversionDailyExpectation()
   {
      return getConversionExpectation() / (double)callQuote.getDaysToExpiration();
   }
   
   public double getReversalDailyExpectation()
   {
      return getReversalExpectation() / (double)callQuote.getDaysToExpiration();
   }
   
   public double getConversionAnnualExpectation()
   {
      return 365.0 * getConversionExpectation() / (double)callQuote.getDaysToExpiration();
   }
   
   public double getReversalAnnualExpectation()
   {
      return 365.0 * getReversalExpectation() / (double)callQuote.getDaysToExpiration();
   }
   
   public double getConversionMonthlyExpectation()
   {
      return 30.0 * getConversionExpectation() / (double)callQuote.getDaysToExpiration();
   }
   
   public double getReversalMonthlyExpectation()
   {
      return 30.0 * getReversalExpectation() / (double)callQuote.getDaysToExpiration();
   }
   
   public double getConversionAPRExpectation()
   {
      return 365.0 * getConversionExpectation() / (double)callQuote.getDaysToExpiration() / ((double)numOfContracts * callQuote.getStrike());
   }
   
   public void print()
   {
      System.out.println(callQuote.getTickerSymbol() + " \t" + Math.floor(getConversionMonthlyExpectation()) + " \t" + Math.floor(getReversalMonthlyExpectation()) + " \t" + callQuote.getStrike() + " \t" + callQuote.getExpirationMonth() + "/" + callQuote.getExpirationYear() + " \t" + callQuote.getDaysToExpiration());
      //callQuote.print();
      //putQuote.print();
      
      //System.out.println("Conversion: " + getConversionMonthlyExpectation() + " $/month");
      //System.out.println("Reversal: " + getReversalMonthlyExpectation() + " $/month");
   }
}
