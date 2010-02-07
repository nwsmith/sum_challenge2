import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/* boring and basic recursive breadth first search */
public class WordChainLongest {
    static ArrayList<String> _dictionary;
    static String _endWord;
    static ArrayList<String> _longestSolution = null;

    public static void main(String[] args) throws IOException {
	_dictionary = loadDictionary(args[2], args[0].length());
	_dictionary.remove(args[0]);

	_endWord = args[1];

	ArrayList<String> startChain = new ArrayList<String>();
	startChain.add(args[0]);

	findChain(startChain, 0);

	if (_longestSolution != null) {
	    _longestSolution.add(_endWord);
      for (String s: _longestSolution) {
          System.out.println(s);
      }
	    //System.out.println("length: " + _longestSolution.size());
	    //System.out.println(_longestSolution);
	} else {
	    System.out.println("No solution found");
	}


    }

    private static ArrayList<String> loadDictionary(String fileName, int wordLength) throws IOException {
	ArrayList<String> dictionary = new ArrayList<String>(10000);
	BufferedReader input = new BufferedReader(new FileReader(fileName));
	String word = null;
	while ((word = input.readLine()) != null) {
	    if (word.length() == wordLength) {
		dictionary.add(word);
	    }
	}
	input.close();
	return dictionary;
    }


    static boolean differentByOneCharacter(String x, String y, String z) {
	int diffFound = 0;
	for (int k = 0; k < x.length(); k++) {
	    if (x.charAt(k) != y.charAt(k)) {
		if (diffFound > 0) {
		    return false;
		} else {
		    ++diffFound;
		    // don't consider letters that allready match the target word
		    if (x.charAt(k) == z.charAt(k)) {
			return false;
		    }
		}
	    }
	}
	// if we're here we only found one difference, all good
	return true;
    }


    static ArrayList<String> findChain(ArrayList<String> currChain, int dictionaryIndex) {
	while (!differentByOneCharacter(currChain.get(currChain.size() - 1), _endWord, _endWord)) {

	    if (!currChain.contains(_dictionary.get(dictionaryIndex))
		&& differentByOneCharacter(currChain.get(currChain.size() - 1), _dictionary.get(dictionaryIndex), _endWord)) {

		// continue down this chain
		ArrayList<String> tmp = (ArrayList<String>) currChain.clone();
		tmp.add(_dictionary.get(dictionaryIndex));
		tmp = findChain(tmp, 0);
		if (tmp != null) {
		    if (_longestSolution == null) {
			_longestSolution = tmp;
		    } else if (tmp.size() > _longestSolution.size()) {
			_longestSolution = tmp;
//			System.out.println(_longestSolution);
		    }
		}
	    }

	    // don't continue if this is a longer chain than the current shortest solution
	    if (_longestSolution != null &&
		_longestSolution.size() >= currChain.size() + 1) {
		currChain = null;
		break;
	    }

	    // loop through the whole dictionary
	    if (++dictionaryIndex >= _dictionary.size()) {
		currChain = null;
		break;
	    }
	}

	return currChain;
    }
}


