package ccf;

public class SecurityQuote 
{
   private String tickerSymbol;
   private double last;
   private int volume;
   private double yield;
   private String name;

   public SecurityQuote(String tickerSymbol)
   {
      this.tickerSymbol = tickerSymbol;
   }
   
   public String getTickerSymbol() { return tickerSymbol; }
   public void setTickerSymbol(String tickerSymbol) { this.tickerSymbol = tickerSymbol; }
   public double getLast() { return last; }
   public void setLast(double last) { this.last = last; }
   public int getVolume() { return volume; }
   public void setVolume(int volume) { this.volume = volume; }
   public double getYield() { return yield; }
   public void setYield(double yield) { this.yield = yield; }
   public String getName() { return name; }
   public void setName(String name) { this.name = name; }
   
   public void print()
   {
      System.out.println("Quote: " + tickerSymbol);
      System.out.println("Name: " + name);
      System.out.println("Last: " + last);
      System.out.println("Volume: " + volume);
      System.out.println("Yield: " + yield);
   }
}
