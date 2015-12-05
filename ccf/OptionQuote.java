package ccf;

public class OptionQuote 
{
   private double strike;
   private int daysToExpiration;
   private double bid;
   private double ask;
   private String type;
   private String tickerSymbol;
   private double last;
   private int volume;
   private String name;
   private int expirationMonth;
   private int expirationYear;
   private double underlyingLast;
   private String settlementType = "";
   private SecurityQuote underlyingQuote;

   public OptionQuote(SecurityQuote underlyingQuote)
   {
      this.tickerSymbol = underlyingQuote.getTickerSymbol();
      this.underlyingQuote = underlyingQuote;
   }
   
   public Double getPremium()
   {
      if(type.toLowerCase().equals("put"))
      {
         if(underlyingQuote.getLast() >= strike)
         {
            return bid;
         }
         else
         {
            return bid - (strike - underlyingQuote.getLast());
         }
      }
      else if(type.toLowerCase().equals("call"))
      {
         if(underlyingQuote.getLast() <= strike)
         {
            return bid;
         }
         else
         {
            return bid - (underlyingQuote.getLast() - strike);
         }
      }
      else
      {
         return null;
      }
   }
   
   public Double getBreakevenPercentage()
   {
      if(type.toLowerCase().equals("put"))
      {
         return -1.0 * (underlyingQuote.getLast() - strike + bid) / underlyingQuote.getLast();
      }
      else if(type.toLowerCase().equals("call"))
      {
         return -1.0 * bid / underlyingQuote.getLast();
      }
      else
      {
         return null;
      }
   }
   
   public Double getProtection()
   {
      if(type.toLowerCase().equals("put"))
      {
         return (underlyingQuote.getLast() - strike + bid) / underlyingQuote.getLast();
      }
      else if(type.toLowerCase().equals("call"))
      {
         return bid / underlyingQuote.getLast();
      }
      else
      {
         return null;
      }
   }
   
   public Double getProfit()
   {
      return getPremium() / underlyingQuote.getLast();
   }
   
   public Double getProfitAPR()
   {
      return Math.pow(1.0 + getPremium() / underlyingQuote.getLast(), 365.0 / daysToExpiration) - 1.0;
   }
   
   public double getDistance()
   {
      return Math.abs((strike - underlyingQuote.getLast())) / underlyingQuote.getLast();
   }
   
   public boolean isComplement(OptionQuote other)
   {
      return tickerSymbol.equals(other.getTickerSymbol()) && strike == other.getStrike() && daysToExpiration == other.getDaysToExpiration() && isStandardSettlement() == other.isStandardSettlement();
   }
   
   public SecurityQuote getUnderlyingQuote() { return underlyingQuote; }
   public void setUnderlyingQuote(SecurityQuote underlyingQuote) { this.underlyingQuote = underlyingQuote; }
   public boolean isStandardSettlement() { return settlementType.equals("S"); }
   public void setSettlementType(String settlementType) { this.settlementType = settlementType; }
   public int getExpirationMonth() { return expirationMonth; }
   public void setExpirationMonth(int expirationMonth) { this.expirationMonth = expirationMonth; }
   public int getExpirationYear() { return expirationYear; }
   public void setExpirationYear(int expirationYear) { this.expirationYear = expirationYear; }
   public double getStrike() { return strike; }
   public void setStrike(double strike) { this.strike = strike; }
   public int getDaysToExpiration() { return daysToExpiration; }
   public void setDaysToExpiration(int daysToExpiration) { this.daysToExpiration = daysToExpiration; }
   public double getBid() { return bid; }
   public void setBid(double bid) { this.bid = bid; }
   public double getAsk() { return ask; }
   public void setAsk(double ask) { this.ask = ask; }
   public String getType() { return type; }
   public void setType(String type) { this.type = type; }
   public String getTickerSymbol() { return tickerSymbol; }
   public void setTickerSymbol(String tickerSymbol) { this.tickerSymbol = tickerSymbol; }
   public double getLast() { return last; }
   public void setLast(double last) { this.last = last; }
   public int getVolume() { return volume; }
   public void setVolume(int volume) { this.volume = volume; }
   public String getName() { return name; }
   public void setName(String name) { this.name = name; }
   public double getUnderlyingLast() { return underlyingQuote.getLast(); }
   public void setUnderlyingLast(double underlyingLast) { this.underlyingLast = underlyingLast; }
   
   public void print()
   {
      System.out.println("\nQuote: " + tickerSymbol);
      System.out.println(type.toUpperCase() + " " + strike + " " + expirationMonth + "/" + expirationYear);
      System.out.println("Last: " + last);
      System.out.println("Bid: " + bid);
      System.out.println("Ask: " + ask);
      System.out.println("Volume: " + volume);
      System.out.println("Underlying Last: " + underlyingQuote.getLast());
      System.out.println("Days to Expiration: " + daysToExpiration);
      System.out.println("Premium: " + getPremium());
      System.out.println("Breakeven Percentage: " + getBreakevenPercentage());
      System.out.println("Profit: " + getProfit());
      System.out.println("Profit APR: " + getProfitAPR());
   }
}
