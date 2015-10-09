import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class TextAnalyzer
{
   public static boolean[][] analyze(ArrayList<String> reviews) throws FileNotFoundException
   {
      
      reviews = removeStopwords(reviews);
      
      ArrayList<String> data = new ArrayList<>();
      for (String s : reviews)
      {
         String[] sentences = s.split("[.!?]");
         
         for (String sn : sentences)
         {
            if (sn.contains("happy hour"))
            {
               String text = getRelevantText(sn);
               if (!text.equals(""))
                  data.add(text);
            }
         }
      }
      
      System.out.println(data);
      
      // take text and return a 7x24 (or less) array of booleans tells when hh are on
      // could probably be from 2pm - 2am
      boolean[][] hhTimes = classifyText(data);
      
      return hhTimes;
   }
   
   
   public static ArrayList<String> removeStopwords(ArrayList<String> reviews) throws FileNotFoundException
   {
      // should probably make an object which does this to keep words in memory
      Scanner file = new Scanner(new File("stopwords/stopwords-basic"));
      HashSet<String> wordlist = new HashSet<>();
      while (file.hasNext())
      {
         wordlist.add(file.nextLine());
      }
      file.close();
      
      for (int i = 0; i < reviews.size(); i++)
      {
         String[] words = reviews.get(i).split(" ");
         String newSentence = "";
         for (String s : words)
         {
            if (!wordlist.contains(s))
               newSentence += s + " ";
         }
         
         reviews.set(i, newSentence);
      }
      
      return reviews;
   }
   
   
   public static String getRelevantText(String sentence)
   {
      
      if (sentence.matches("(?s).*\\d.*"))
      {
         int idx = sentence.indexOf("happy hour");
         sentence = sentence.substring(idx+10,sentence.length());
         
         return sentence;
      }
      return "";
   }
   
   
   private static boolean[][] classifyText(ArrayList<String> data)
   {
      boolean[][] hhTimes = new boolean[12][7];
      for (String s : data)
      {
         if (!s.matches("(?s).*\\d.*"))
            continue;
      }
      return hhTimes;
   }
}
