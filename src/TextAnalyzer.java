import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
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
   
   
   private static ArrayList<String> removeStopwords(ArrayList<String> reviews) throws FileNotFoundException
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
   
   
   private static String getRelevantText(String sentence)
   {
      
      if (sentence.matches("(?s).*\\d.*"))
      {
         int idx = sentence.indexOf("happy hour");
         sentence = sentence.substring(idx+10,sentence.length());
         
         return sentence;
      }
      return "";
   }
   
   
   private static boolean[][] classifyText(ArrayList<String> data) throws FileNotFoundException
   {
      Scanner file = new Scanner(new File("abbreviations/abbreviations-weekdays"));
      HashMap<String,Integer> wordlist = new HashMap<>();
      while (file.hasNext())
      {
         String[] keyValue = file.nextLine().split(",");
         wordlist.put(keyValue[0],Integer.parseInt(keyValue[1]));
      }
      file.close();
      
      boolean[][] hhTimes = new boolean[13][7];
      for (String s : data)
      {
         if (!s.matches("(?s).*\\d.*"))
            continue;
         
         // the following algorithm checks for times that fall after the dates
         String[] words = s.split("[ -]");
         for (int i = 0; i < words.length; i++)
         {
            int startDay = 13, endDay = 13, endTime = 13, startTime = 13;
            
            
            if (wordlist.containsKey(words[i]))
            {
               startDay = wordlist.get(words[i]);
               endDay = startDay;
               if ((i+1) < words.length && wordlist.containsKey(words[i+1]))
               {
                  endDay = wordlist.get(words[i+1]);
               }
               for (int j = i; j < words.length; j++)
               {
                  startTime = 13;
                  endTime = 13;
                  if (words[j].matches("[0-9]"))
                  {
                     startTime = Integer.parseInt(words[j]) - 2;
                     if (words[j+1].matches("[0-9]"))
                     {
                        endTime = Integer.parseInt(words[j+1]) - 2;
                        break;
                     }
                     else
                     {
                        endTime = 12;
                        break;
                     }
                  }
               }
            }
            
            for (int j = startTime; j < 12; j++)
            {
               for (int k = startDay; k < 7; k++)
               {
                  if (j <= endTime && k <= endDay)
                  {
                     hhTimes[j][k] = true;
                  }
               }
            }
            
         }
      }
      return hhTimes;
   }
}
