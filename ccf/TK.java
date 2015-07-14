package ccf;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TK 
{
   private static final String PROTECTED_RESOURCE_URL = "https://api.tradeking.com/v1/member/profile.json";
   
   private static final double MAX_DISTANCE = 0.10;
   private static final double MIN_PROFIT = 0.10;
   private static final double MIN_OPEN_INTEREST = 10;
   private static final double MIN_DAYS_REMAINING = 20;
   private static final double MAX_DAYS_REMAINING = 60;
   private static final double MIN_PROTECTION = 0.05;
   
   public static ArrayList<OptionQuote> findTrades(String[] tickers, double maxDistance, int minDays, int maxDays, double minProfit, double minProtection) throws Exception
   {      
      SecurityQuote underlyingQuote;
      ArrayList<SecurityQuote> underlyingQuotes = new ArrayList<SecurityQuote>();
      ArrayList<OptionQuote> quotes = new ArrayList<OptionQuote>();
      
      for(int i = 0; i < tickers.length; i++)
      {
         underlyingQuote = getSecurityQuote(tickers[i]);
         underlyingQuote.print();
         underlyingQuotes.add(underlyingQuote);
         quotes.addAll(getOptionQuotes(underlyingQuote, "put"));
         quotes.addAll(getOptionQuotes(underlyingQuote, "call"));
      }
      
      ArrayList<OptionQuote> filtered = new ArrayList<OptionQuote>();
      
      for(OptionQuote option : quotes)
      {
         if(      option.getDistance() < maxDistance 
               && option.getDaysToExpiration() >= minDays && option.getDaysToExpiration() <= maxDays
               && option.getProfitAPR() > minProfit
               && option.getProtection() > minProtection
           )
         {
            filtered.add(option);
         }
      }
      
      for(OptionQuote option : filtered)
      {
         option.print();
      }
      
      return filtered;
   }
   
   public static SecurityQuote getSecurityQuote(String tickerSymbol) throws Exception
   {
      SecurityQuote quote = new SecurityQuote(tickerSymbol);
      
      String response = queryTK("https://api.tradeking.com/v1/market/ext/quotes.xml?symbols=" + tickerSymbol);
      
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(new ByteArrayInputStream(response.getBytes()));

      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName("quote");
    
      for (int temp = 0; temp < nList.getLength(); temp++) 
      {
         Node nNode = nList.item(temp);
    
         if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    
            Element eElement = (Element) nNode;
            
            quote.setLast(Double.parseDouble(eElement.getElementsByTagName("last").item(0).getTextContent()));
            quote.setVolume(Integer.parseInt(eElement.getElementsByTagName("pvol").item(0).getTextContent()));
            quote.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
            
            if(eElement.getElementsByTagName("yield") != null && eElement.getElementsByTagName("yield").item(0) != null)
            {
               quote.setYield(Double.parseDouble(eElement.getElementsByTagName("yield").item(0).getTextContent()));
            }
         }
      }
      
      return quote;
   }
   
   public static String queryTK(String url)
   {
      OAuthService service = new ServiceBuilder()
        .provider(TradeKingApi.class)
        .apiKey(Auth.CONSUMER_KEY)
        .apiSecret(Auth.CONSUMER_SECRET)
        .build();
      Token accessToken = new Token(Auth.OAUTH_TOKEN, Auth.OAUTH_TOKEN_SECRET);

      OAuthRequest request = new OAuthRequest(Verb.GET, url);
      service.signRequest(accessToken, request);
      Response response = request.send();
      
      return response.getBody();
   }
   
   public static ArrayList<OptionQuote> getOptionQuotes(SecurityQuote underlyingQuote, String type) throws Exception
   {
      ArrayList<OptionQuote> quotes = new ArrayList<OptionQuote>();
      OptionQuote quote;
       
      String query = "https://api.tradeking.com/v1/market/options/search.xml?symbol=" + underlyingQuote.getTickerSymbol() + "&query=put_call-eq%3A" + type;
      String response = queryTK(query);
       
       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
       Document doc = dBuilder.parse(new ByteArrayInputStream(response.getBytes()));

       doc.getDocumentElement().normalize();
       NodeList nList = doc.getElementsByTagName("quote");
     
       for (int temp = 0; temp < nList.getLength(); temp++) 
       {
          quote = new OptionQuote(underlyingQuote);
          Node nNode = nList.item(temp);
     
          if (nNode.getNodeType() == Node.ELEMENT_NODE) 
          {
             Element eElement = (Element) nNode;
             
             try
             {
                quote.setLast(Double.parseDouble(eElement.getElementsByTagName("last").item(0).getTextContent()));
                quote.setVolume(Integer.parseInt(eElement.getElementsByTagName("pvol").item(0).getTextContent()));
                quote.setType(eElement.getElementsByTagName("put_call").item(0).getTextContent());
                quote.setStrike(Double.parseDouble(eElement.getElementsByTagName("strikeprice").item(0).getTextContent()));
                quote.setExpirationMonth(Integer.parseInt(eElement.getElementsByTagName("xmonth").item(0).getTextContent()));
                quote.setExpirationYear(Integer.parseInt(eElement.getElementsByTagName("xyear").item(0).getTextContent()));
                quote.setBid(Double.parseDouble(eElement.getElementsByTagName("bid").item(0).getTextContent()));
                quote.setAsk(Double.parseDouble(eElement.getElementsByTagName("ask").item(0).getTextContent()));
                quote.setDaysToExpiration(Integer.parseInt(eElement.getElementsByTagName("days_to_expiration").item(0).getTextContent()));
                quote.setSettlementType(eElement.getElementsByTagName("op_delivery").item(0).getTextContent());
                
                if(quote.isStandardSettlement())
                {
                   quotes.add(quote);
                }
             }
             catch(Exception e)
             {
                //swallowing these because TradeKing includes a bunch of non-standard options, that we don't care about
            	 //TODO: should probably log these, so we can review them to make sure we aren't missing standard options
             }
          }
       }
       
       return quotes;
    }
}
