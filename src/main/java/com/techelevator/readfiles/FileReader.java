package com.techelevator.readfiles;

import com.mongodb.*;
import com.techelevator.WordsFromLetters;
import java.util.*;

public class FileReader {

    public static void main(String[] args) {
        List<String> words = WordsFromLetters.getAllWords();
        List<WordsFromLetters> letterCombos = new ArrayList<>();

        List<String> uniqueLetterCombinations = new ArrayList<>();

        for(String word : words) {
            if(uniqueLetterCount(word)==7) {
                char[] tempArray = word.toCharArray();
                Arrays.sort(tempArray);
                uniqueLetterCombinations.add(uniqueCharacters(new String(tempArray)));
            }
        }

        Collections.sort(uniqueLetterCombinations);

        for(String s : uniqueLetterCombinations) {
            try {
                letterCombos.add(new WordsFromLetters(s));
            }
            catch (Exception e) {
                System.out.println("Error");
            }
        }

        MongoClient mongoClient;

        try {
            mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            DB database = mongoClient.getDB("letterCombinations");
            DBCollection collection = database.getCollection("LettersToWords");
           for(WordsFromLetters word: letterCombos) {
               DBObject letters = new BasicDBObject("letters", word.getLetters());
               letters.putAll(word.getWordsForRequiredLetter());
               collection.insert(letters);
           }
        }
        catch (Exception e) {
            System.out.println("Data base error: " + e);
        }
    }



    private static String uniqueCharacters(String word) {
        String letters = "";
        for(int i = 0; i < word.length(); i++) {
            if(letters.indexOf(word.charAt(i)) == -1 ) {
                letters+=word.charAt(i);
            }
        }
        return letters;
    }

    private static int uniqueLetterCount(String word) {
        Set<Character>  letters = new HashSet<>();
        for(int i = 0; i < word.length(); i++) {
            letters.add(word.charAt(i));
            if(letters.size()>7) {
                return 8;
            }
        }
        return letters.size();
    }

}
