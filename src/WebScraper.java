import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebScraper
{
   // testing this class
   public static void main(String[] args) throws IOException
   {
//       Pattern pattern = Pattern.compile(("(\\d+)(\\s?-\\s?|\\s?through\\s?|\\s?to\\s?)(\\d+)"));
////        Matcher matcher = pattern.matcher(sentence);
//       String url = "Happy hour is daily from 3-7pm and all day Monday.";
//       Matcher matcher = pattern.matcher(url);
//
//       if (matcher.find())
//       {
//           System.out.println(url.substring(matcher.start(), matcher.end()));
//       }
      YelpAPI yelp = new YelpAPI();
      Business[] b = yelp.returnBusinesses(yelp, "Happy Hour", "91210", 10);

      for (Business q : b)
      {
         String url = q.getUrl();
         ArrayList<String> reviews = retrieveReviews(url);
         System.out.println(q.getName());
         TextAnalyzer.analyze(reviews);
      }

//      boolean[][] times = TextAnalyzer.analyze(reviews);
//
//      System.out.printf("   %9s %9s %9s %9s %9s %9s %9s%n","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday");
//
//      for (int i = 0; i < 12; i++)
//      {
//         boolean[] ba = times[i];
//         System.out.printf("%3d",i+2);
//         for (boolean b : ba)
//         {
//            System.out.printf("%9s ",b);
//         }
//         System.out.println();
//      }

   }


   public static ArrayList<String> retrieveReviews(String url) throws IOException
   {
      ArrayList<String> formattedReviews = new ArrayList<>();

      for (int i = 0; i < 100; i+=20)
      {
         // Gets up to 100 reviews.
         Document doc = Jsoup.connect(url + "?start=" + i + "&q=happy+hour" ).timeout(60000).maxBodySize(10*1024*1024).get();
         String[] reviews = doc.select("p[itemprop]").toString().split("\n");
         if (reviews.length <= 1)
            break;

         for (String s : reviews)
         {
            s = s.substring(36,s.length()-4);
            s = s.replace("<br />", "");
            s = s.toLowerCase();
            formattedReviews.add(s);
         }
      }
      return formattedReviews;
   }
}
