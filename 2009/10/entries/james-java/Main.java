import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {
        validateInput(args[0]);
        decode(new ArrayList<MorseCodeCharacter>(), args[0]);
    }

    private static void decode(List<MorseCodeCharacter> prev, String code) {
        for (MorseCodeCharacter curr : MorseCodeCharacter.allCharacters) {
            if (code.matches(curr.getRegex())) {
                String newCode = code.substring(curr.getLength());
                List<MorseCodeCharacter> next = new ArrayList<MorseCodeCharacter>(prev);
                next.add(curr);
                if (newCode.length() == 0) {
                    displayWord(next);
                } else {
                    decode(next, newCode); 
                }
            }
        }
    }

    private static void displayWord(List<MorseCodeCharacter> characters) {
        for (MorseCodeCharacter ch : characters) {
            System.out.print(ch.getName());
        }
        System.out.println();
    }

    private static void validateInput(String morseCode) {
        for (int i = 0; i < morseCode.length(); i++) {
            if ((morseCode.charAt(i) != '-') && (morseCode.charAt(i) != '.')) {
                throw new RuntimeException("Bad Input!");
            }
        }
    }
}
