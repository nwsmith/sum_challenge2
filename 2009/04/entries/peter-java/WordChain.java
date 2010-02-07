//package wordChain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordChain {
    private final String _firstWord;
    private final String _lastWord;
    private final String _dictionaryName;
    final static String SOLUTION_FILE_NAME = "PeterSolution";
    final static String SOLUTION_SHORT_FILE_NAME = "PeterSolutionShort";
    final static String SOLUTION_LONG_FILE_NAME = "PeterSolutionLong";
    final static String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static String _dictionaryLastModified;

    private enum SEARCH_TYPE {
	SHORTEST, LONGEST
    }


    public static void main(String[] args) {
	if (args == null || args.length != 4) {
	    System.out
		    .println("you must supply 4 parameters:  1st word, 2nd word, dictionary file name, one of [SHORT,LONG]");
	    System.exit(0);
	}
	new WordChain(args[0], args[1], args[2], args[3]);
    }

    public WordChain(String firstWord, String lastWord, String dictionaryName, String searchType) {
	this._firstWord = firstWord;
	this._lastWord = lastWord;
	this._dictionaryName = dictionaryName;
	if (performValidation(searchType)) {
	    System.exit(0);
	}
	SEARCH_TYPE shortOrLong = SEARCH_TYPE.SHORTEST;
	if ("long".equalsIgnoreCase(searchType)) {
	    shortOrLong = SEARCH_TYPE.LONGEST;
	}
	Solution previousSolution = getPreviousSolution(shortOrLong);
	if (previousSolution.getSolutionCount() > 0 && (previousSolution.getSolutionCount() % 10 == 0 ||
							shortOrLong == SEARCH_TYPE.SHORTEST)) {
	    outputResults(previousSolution.getChain());
	    previousSolution.setSolutionCount(previousSolution.getSolutionCount() + 1);
	    writeSolution(shortOrLong, previousSolution, null);
	} else {
	    Map<String, ArrayList<String>> chains = getPreviousChains();
	    if (chains == null) {
		Set<String> dictionary = loadDictionary();
		if (dictionary.size() == 0) {
		    System.out
			    .println("There are no words in the dictionary with the same number of characters as the words given");
		    System.exit(0);
		}
		if (!dictionary.contains(firstWord) || !dictionary.contains(lastWord)) {
		    System.out
			    .println("There words given are both not found in the dictionary");
		    System.exit(0);
		}
		if (firstWord.equals(lastWord)) {
		    System.out.println("There shortest and longest chain from "
				       + firstWord + " to " + lastWord + " is 1");
		    System.exit(0);
		}
		chains = createChains(dictionary);
		if (chains.get(firstWord) == null || chains.get(lastWord) == null) {
		    System.out.println("There is no solution from "
				       + firstWord + " to " + lastWord);
		    System.exit(0);
		}
	    }
	    int charsDiff = 1;  //count first word
	    for (int i = 0; i < firstWord.length(); i++) {
		if (firstWord.charAt(i) != lastWord.charAt(i)) {
		    charsDiff++;
		}
	    }
	    ArrayList<String> solutionChain;
	    if (shortOrLong == SEARCH_TYPE.SHORTEST) {
		solutionChain = findChains(chains, SEARCH_TYPE.SHORTEST, charsDiff, 50000, 0);
	    } else {
		solutionChain = new ArrayList<String>();
		boolean finished = false;
		for (int attempt = 0; attempt < 3 && !finished; attempt++) {
		    if (previousSolution.getSolutionCount() == 0 || attempt > 0) {
			for (ArrayList<String> chain : chains.values()) {
			    Collections.shuffle(chain);
			}
		    }
		    for (int firstIndex = 0; firstIndex < chains.get(firstWord).size() && !finished; firstIndex++) {
			ArrayList<String> newLongest = findChains(chains, SEARCH_TYPE.LONGEST, chains.size(), 50, firstIndex);
			if (newLongest != null && newLongest.size() > solutionChain.size()) {
			    solutionChain = new ArrayList<String>(newLongest);
			}
			if (solutionChain.size() >= chains.size()) {
			    finished = true;
			}
		    }
		}
	    }
	    outputResults(solutionChain);
	    previousSolution.setSolutionCount(previousSolution.getSolutionCount() + 1);
	    writeSolution(shortOrLong, previousSolution, solutionChain);
	}
    }

    private Solution getPreviousSolution(SEARCH_TYPE shortOrLong) {
	Solution previousSolution = new Solution(0, new ArrayList<String>());
	File file;
	if (shortOrLong == SEARCH_TYPE.SHORTEST) {
	    file = new File(SOLUTION_SHORT_FILE_NAME);
	} else {
	    file = new File(SOLUTION_LONG_FILE_NAME);
	}
	if (file.exists()) {
	    FileInputStream inputStream = null;
	    DataInputStream dataInputStream = null;
	    BufferedReader bufferedReader = null;
	    try {
		inputStream = new FileInputStream(file.getName());
		dataInputStream = new DataInputStream(inputStream);
		bufferedReader = new BufferedReader(new InputStreamReader(
			dataInputStream));
		String line;
		ArrayList<String> parms = new ArrayList<String>();
		for (int i = 0; i < 5 && (line = bufferedReader.readLine()) != null; i++) {
		    parms.add(line);
		}
		if (parms.size() == 5) {
		    if (_firstWord.equals(parms.get(0)) &&
			_lastWord.equals(parms.get(1)) &&
			_dictionaryName.equals(parms.get(2)) &&
			_dictionaryLastModified.equals(parms.get(3))) {
			ArrayList<String> chain = new ArrayList<String>();
			while ((line = bufferedReader.readLine()) != null) {
			    chain.add(line);
			}
			previousSolution = new Solution(Integer.valueOf(parms.get(4)), chain);
		    }
		}
	    } catch (FileNotFoundException e) {
		e.printStackTrace(); // To change body of catch statement use File |
	    } catch (IOException e) {
		e.printStackTrace(); // To change body of catch statement use File |
	    } finally {
		try {
		    if (bufferedReader != null) {
			bufferedReader.close();
		    }
		    if (dataInputStream != null) {
			dataInputStream.close();
		    }
		    if (inputStream != null) {
			inputStream.close();
		    }
		} catch (IOException e1) {
		}
	    }
	}
	return previousSolution;
    }

    private Map<String, ArrayList<String>> getPreviousChains() {
	Map<String, ArrayList<String>> solution = null;
	File file = new File(SOLUTION_FILE_NAME);
	if (file.exists()) {
	    FileInputStream inputStream = null;
	    DataInputStream dataInputStream = null;
	    BufferedReader bufferedReader = null;
	    try {
		inputStream = new FileInputStream(SOLUTION_FILE_NAME);
		dataInputStream = new DataInputStream(inputStream);
		bufferedReader = new BufferedReader(new InputStreamReader(
			dataInputStream));
		String line;
		ArrayList<String> parms = new ArrayList<String>();
		for (int i = 0; i < 4 && (line = bufferedReader.readLine()) != null; i++) {
		    parms.add(line);
		}
		if (parms.size() == 4) {
		    if (_firstWord.equals(parms.get(0)) &&
			_lastWord.equals(parms.get(1)) &&
			_dictionaryName.equals(parms.get(2)) &&
			_dictionaryLastModified.equals(parms.get(3))) {
			solution = new HashMap<String, ArrayList<String>>();
			while ((line = bufferedReader.readLine()) != null) {
			    ArrayList<String> chain = new ArrayList<String>();
			    String key = line;
			    while ((line = bufferedReader.readLine()) != null) {
				if (line != null) {
				    if ("~".equals(line)) {
					break;
				    } else {
					chain.add(line);
				    }
				}
			    }
			    solution.put(key, chain);
			}
		    }
		}
	    } catch (FileNotFoundException e) {
		e.printStackTrace(); // To change body of catch statement use File |
	    } catch (IOException e) {
		e.printStackTrace(); // To change body of catch statement use File |
	    } finally {
		try {
		    if (bufferedReader != null) {
			bufferedReader.close();
		    }
		    if (dataInputStream != null) {
			dataInputStream.close();
		    }
		    if (inputStream != null) {
			inputStream.close();
		    }
		} catch (IOException e1) {
		}
	    }
	}
	return solution;
    }

    private void writeSolution(SEARCH_TYPE shortOrLong, Solution previousSolution, ArrayList<String> solutionChain) {
	File file;
	if (shortOrLong == SEARCH_TYPE.SHORTEST) {
	    file = new File(SOLUTION_SHORT_FILE_NAME);
	} else {
	    file = new File(SOLUTION_LONG_FILE_NAME);
	}
	if (file.exists()) {
	    file.delete();
	}
	ArrayList<String> solution = new ArrayList<String>(previousSolution.getChain());
	if (shortOrLong == SEARCH_TYPE.LONGEST) {
	    if (solutionChain != null && solutionChain.size() > previousSolution.getChain().size()) {
		solution = new ArrayList<String>(solutionChain);
	    }
	} else {
	    if (previousSolution.getChain().size() == 0 ||
		(solutionChain != null && solutionChain.size() > 0 && previousSolution.getChain().size() > solutionChain.size())) {
		solution = solutionChain == null ? new ArrayList<String>() : new ArrayList<String>(solutionChain);
	    }
	}
	Writer output = null;
	StringBuilder sb = new StringBuilder();
	sb.append(_firstWord + "\n");
	sb.append(_lastWord + "\n");
	sb.append(_dictionaryName + "\n");
	sb.append(_dictionaryLastModified + "\n");
	sb.append(String.valueOf(previousSolution.getSolutionCount()) + "\n");
	for (String value : solution) {
	    sb.append(value + "\n");
	}
	try {
	    file.createNewFile();
	    output = new BufferedWriter(new FileWriter(file));
	    output.write(sb.toString());
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (output != null) {
		try {
		    output.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private Map<String, ArrayList<String>> createChains(Set<String> dictionary) {
	Map<String, ArrayList<String>> chains = new HashMap<String, ArrayList<String>>();
	ArrayList<String> dictList = new ArrayList<String>(dictionary);
	int i = 0;
	for (String word1 : dictList) {
	    for (int j = i + 1; j < dictionary.size(); j++) {
		String word2 = dictList.get(j);
		if (countCharactersDifference(word1, word2) == 1) {
		    ArrayList<String> chain = chains.get(word1);
		    if (chain == null) {
			chain = new ArrayList<String>();
			chain.add(word2);
			chains.put(word1, chain);
		    } else {
			chain.add(word2);
		    }
		    chain = chains.get(word2);
		    if (chain == null) {
			chain = new ArrayList<String>();
			chain.add(word1);
			chains.put(word2, chain);
		    } else {
			chain.add(word1);
		    }
		}
	    }
	    i++;
	}
	writeSolutionChains(chains);
	return chains;
    }

    private void writeSolutionChains(Map<String, ArrayList<String>> chains) {
	File file = new File(SOLUTION_FILE_NAME);
	if (file.exists()) {
	    file.delete();
	}
	Writer output = null;
	StringBuilder sb = new StringBuilder();
	sb.append(_firstWord + "\n");
	sb.append(_lastWord + "\n");
	sb.append(_dictionaryName + "\n");
	sb.append(_dictionaryLastModified + "\n");
	for (String key : chains.keySet()) {
	    sb.append(key + "\n");
	    for (String value : chains.get(key)) {
		sb.append(value + "\n");
	    }
	    sb.append("~\n");
	}
	try {
	    file.createNewFile();
	    output = new BufferedWriter(new FileWriter(file));
	    output.write(sb.toString());
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (output != null) {
		try {
		    output.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private int countCharactersDifference(String word1, String word2) {
	int numberOfDifferences = 0;
	for (int i = 0; i < word1.length() && numberOfDifferences <= 1; i++) {
	    if (word1.charAt(i) != word2.charAt(i)) {
		numberOfDifferences++;
	    }
	}
	return numberOfDifferences;
    }

    private ArrayList<String> findChains(
	    Map<String, ArrayList<String>> chains,
	    SEARCH_TYPE searchType,
	    int targetElementsLength,
	    long milliSecondsToRun,
	    int startingIndex) {
	long startMilliSecond = System.currentTimeMillis();
	ArrayList<String> firstChain = chains.get(_firstWord);
	ArrayList<Element> currentElements = new ArrayList<Element>();
	currentElements.add(new Element(_firstWord, startingIndex, firstChain.size(), firstChain));
	int currentElementsSize = 0;
	long count = 0;
	HashSet<String> currentElementSet = new HashSet<String>();
	currentElementSet.add(_firstWord);
	ArrayList<String> solution = null;
	while (currentElements.size() > 0) {
	    if (count++ % 1000 == 0) {
		if (System.currentTimeMillis() - startMilliSecond > milliSecondsToRun) {
		    break; //timed out
		}
	    }
	    Element element = currentElements.get(currentElementsSize); //get the last element of currentElements
	    String nextWord = element.getWordList().get(element.getIndex());
	    if (nextWord.equals(_lastWord)) {
		if (solution == null ||
		    (SEARCH_TYPE.LONGEST == searchType && solution.size() <= currentElements.size() + 1) ||
		    (SEARCH_TYPE.SHORTEST == searchType && solution.size() >= currentElements.size() + 1)) {
		    solution = createSolution(nextWord, currentElements);
		    if ((SEARCH_TYPE.SHORTEST == searchType && solution.size() <= targetElementsLength) ||
			(SEARCH_TYPE.LONGEST == searchType && solution.size() >= targetElementsLength)) {
			break;	//we found possible final solution
		    }
		}
		currentElementsSize = nextElement(element, currentElements, currentElementsSize, currentElementSet);
		continue;
	    }
	    if (SEARCH_TYPE.SHORTEST == searchType && currentElements.size() >= targetElementsLength) {
		currentElementsSize = nextElement(element, currentElements, currentElementsSize, currentElementSet);
	    } else {
		ArrayList<String> newChain = new ArrayList<String>(chains.get(nextWord));
		newChain.removeAll(currentElementSet);
		if (newChain.size() == 0 || currentElements.size() >= targetElementsLength) {
		    currentElementsSize = nextElement(element, currentElements, currentElementsSize, currentElementSet);
		} else {
		    currentElements.add(new Element(nextWord, 0, newChain.size(), newChain));
		    currentElementSet.add(nextWord);
		    currentElementsSize++;
		    if (newChain.contains(_lastWord)) {
			solution = createSolution(_lastWord, currentElements);
			if ((solution.size() <= targetElementsLength && SEARCH_TYPE.SHORTEST == searchType) ||
			    (solution.size() >= targetElementsLength && SEARCH_TYPE.LONGEST == searchType)) { //we found a possible final solution
			    break;
			}
		    }
		}
	    }
	    if (currentElements.size() == 0 && SEARCH_TYPE.SHORTEST == searchType) { //restart again
		if (targetElementsLength == chains.size()) {
		    break;
		}
		currentElements.add(new Element(_firstWord, startingIndex, firstChain.size(), firstChain));
		currentElementsSize = 0;
		currentElementSet.add(_firstWord);
		targetElementsLength++;
	    }
	}
	return solution;
    }

    private ArrayList<String> createSolution(String nextWord, ArrayList<Element> currentElements) {
	ArrayList<String> solution = new ArrayList<String>();
	for (Element currElement : currentElements) {
	    solution.add(currElement.getWord());
	}
	solution.add(nextWord);
	return solution;
    }

    private Set<String> loadDictionary() {
	FileInputStream inputStream = null;
	DataInputStream dataInputStream = null;
	BufferedReader bufferedReader = null;
	Set<String> wordList = new HashSet<String>();
	try {
	    inputStream = new FileInputStream(_dictionaryName);
	    dataInputStream = new DataInputStream(inputStream);
	    bufferedReader = new BufferedReader(new InputStreamReader(
		    dataInputStream));
	    String line;
	    int length = _firstWord.length();
	    while ((line = bufferedReader.readLine()) != null) {
		if (line != null && line.trim().length() == length) {
		    wordList.add(line);
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace(); // To change body of catch statement use File |
	} catch (IOException e) {
	    e.printStackTrace(); // To change body of catch statement use File |
	} finally {
	    try {
		if (bufferedReader != null) {
		    bufferedReader.close();
		}
		if (dataInputStream != null) {
		    dataInputStream.close();
		}
		if (inputStream != null) {
		    inputStream.close();
		}
	    } catch (IOException e1) {
	    }
	}
	return wordList;
    }

    private int nextElement(Element element,
			    ArrayList<Element> currentElements,
			    int currentSize,
			    HashSet<String> currentElementSet) {
//	_timingProfile.start("nextElement");
	while (currentElements.size() > 0) {
	    element.setIndex(element.getIndex() + 1);
	    if (element.getIndex() >= element.getSize()) {
		Element removed = currentElements.remove(currentSize);
		currentElementSet.remove(removed.getWord());
		currentSize--;
		if (currentSize >= 0) {
		    element = currentElements.get(currentSize);
		}
	    } else {
		break;
	    }
	}
//	_timingProfile.stop("nextElement");
	return currentSize;
    }

    private void outputResults(ArrayList<String> chain) {
	if (chain != null) {
	    for (String word : chain) {
		System.out.println(word);
	    }
	}
    }

    private boolean performValidation(String searchType) {
	boolean error = false;
	if (_firstWord == null || _lastWord == null
	    || _firstWord.trim().length() != _lastWord.trim().length()) {
	    System.out
		    .println("The starting word and ending word are either blank or different lengths");
	    error = true;
	}
	File file = new File(_dictionaryName);
	if (!file.exists()) {
	    System.out.println("The dictionary file name does not exist");
	    error = true;
	} else {
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    _dictionaryLastModified = sdf.format(new Date(file.lastModified()));
	}
	if (!"short".equalsIgnoreCase(searchType) && !"long".equalsIgnoreCase(searchType)) {
	    System.out.println("The search type must be either 'short' or 'long' without the quotes.");
	    error = true;
	}
	return error;
    }

    private class Element {
	private String _word;
	private int _index;
	private int _size;
	private ArrayList<String> _wordList;

	public Element(String word, int index, int size, ArrayList<String> wordList) {
	    _word = word;
	    _index = index;
	    _size = size;
	    _wordList = wordList;
	}

	public int getIndex() {
	    return _index;
	}

	public int getSize() {
	    return _size;
	}

	public String getWord() {
	    return _word;
	}

	public void setIndex(int index) {
	    _index = index;
	}

	public ArrayList<String> getWordList() {
	    return _wordList;
	}

	public void setWordList(ArrayList<String> wordList) {
	    _wordList = wordList;
	}

	@Override
	public String toString() {
	    return "word=" + _word + ", index=" + _index + ", size=" + _size;
	}
    }

    private class Solution {
	private int solutionCount;
	private ArrayList<String> chain;

	private Solution(int solutionCount, ArrayList<String> chain) {
	    this.solutionCount = solutionCount;
	    this.chain = chain;
	}

	public int getSolutionCount() {
	    return solutionCount;
	}

	public ArrayList<String> getChain() {
	    return chain;
	}

	public void setSolutionCount(int solutionCount) {
	    this.solutionCount = solutionCount;
	}

	public void setChain(ArrayList<String> chain) {
	    this.chain = chain;
	}
    }
}
