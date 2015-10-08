import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebScraper
{
   // testing this class
   public static void main(String[] args) throws IOException
   {
      // url to yelp page or at least info from yelp on the project
      String url = "url goes here";
      
      ArrayList<String> reviews = retrieveReviews(url);
      
      boolean[][] times = TextAnalyzer.analyze(reviews);
      
      for (boolean[] ba : times)
      {
         for (boolean b : ba)
         {
            System.out.print(b + " ");
         }
         System.out.println();
      }
      
   }
   
   
   private static ArrayList<String> retrieveReviews(String url) throws IOException
   {
      Document doc = Jsoup.connect(url).ignoreContentType(true).get();
      
      String[] reviews = doc.select("p[itemprop]").toString().split("\n");
      ArrayList<String> formattedReviews = new ArrayList<>();
      for (String s : reviews)
      {
         s = s.substring(36,s.length()-4);
         s = s.replace("<br />", "");
         s = s.toLowerCase();
         formattedReviews.add(s);
      }
      
      return formattedReviews;
   }
}
