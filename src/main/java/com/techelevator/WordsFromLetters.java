package com.techelevator;

import com.mongodb.BasicDBObject;
import com.techelevator.exceptions.BadLetter;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.*;

public class WordsFromLetters extends BasicDBObject implements Serializable {

	private String letters ="";
	private static final List<String> allWords = new ArrayList<>();
	private final static Path myPath = Path.of("usa.txt");
	private Map<Character, List<String>> wordsForRequiredLetter = new HashMap<>();

	public WordsFromLetters() {
		if(allWords.size() == 0) {
			setWordList();
		}
	}

	public WordsFromLetters(String letters) throws BadLetter{


		//if this is the first object it will populate the static word list
		if(allWords.size() == 0) {
			setWordList();
		}

		//words list is shared among all the objects
		//remove all words that contain more than seven letters
		removeWordsWithMoreThanSevenLetters();

		//remove words that are shorter than four letters
		removeWordsLessThanFourLettersLong();

		if(uniqueLetterCount(letters) != 7) {
			throw new BadLetter();
		}
		this.letters = letters;

		List<String> wordsWithLetters = wordsFromLetters(letters);

		if(containsAllLetters(letters).size() == 0)
		{
			throw new BadLetter();
		}

		for(char c : letters.toCharArray()) {
			wordsForRequiredLetter.put(c, getWordsWithLetter(c, wordsWithLetters));
		}
		System.out.println("Stop");
	}

	public String getLetters() {
		return letters;
	}

	public Map<Character, List<String>> getWordsForRequiredLetter() {
		return wordsForRequiredLetter;
	}

	public static List<String> getAllWords() {
		if(allWords.size() == 0) {
			setWordList();
		}
		return allWords;
	}

	private List<String> getWordsWithLetter(char letter, List<String> refinedList){

		List<String> results = new ArrayList<>();
		for(String word: refinedList) {
			if( word.indexOf(letter) != -1) {
				results.add(word);
			}
		}
		return results;
	}



	private static void removeWordsWithMoreThanSevenLetters() {
		for (int i = 0; i < allWords.size(); i++) {
			List<Character> letters = new ArrayList<>();
			for (int j = 0; j < allWords.get(i).length(); j++) {
				if (!letters.contains(allWords.get(i).charAt(j))) {
					letters.add(allWords.get(i).charAt(j));
				}
				if(letters.size()>7) {
					allWords.remove(i);
					i--;
					break;
				}
			}
		}
	}

	private static void removeWordsLessThanFourLettersLong() {
		for (int i = 0; i < allWords.size(); i++) {
			if (allWords.get(i).length() < 4) {
				allWords.remove(i);
				i--;
			}
		}
	}

	private static int uniqueLetterCount(String l) {
		Set<Character> letterCount = new HashSet<>();
		for(Character c : l.toCharArray()) {
			letterCount.add(c);
		}
		return letterCount.size();
	}

	private static void setWordList() {

		try (Scanner fileScanner = new Scanner(myPath)) {
			while (fileScanner.hasNextLine()) {
				allWords.add(fileScanner.nextLine());

			}
		} catch (IOException e) {
			System.out.println("Can't read from that file!");
		}
	}

	private static List<String> containsAllLetters(String l) {
		List<String> result = new ArrayList<>();
		for (String word : allWords) {
			boolean onlyValid = true;
			for (int j = 0; j < l.length(); j++) {
				if (word.indexOf(l.charAt(j)) == -1) {
					onlyValid = false;
					break;
				}
			}
			if (onlyValid) {
				result.add(word);
			}
		}
		return result;
	}

	private static List<String> wordsFromLetters(String l) {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < allWords.size(); i++) {
			boolean onlyValid = true;
			for (int j = 0; j < allWords.get(i).length(); j++) {
				if (l.indexOf(allWords.get(i).charAt(j)) == -1) {
					onlyValid = false;
					break;
				}
			}
			if (onlyValid) {
				result.add(allWords.get(i));
			}
		}
		return result;
	}
}
