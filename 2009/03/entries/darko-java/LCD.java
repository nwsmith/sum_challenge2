import java.util.Arrays;

public class LCD {

    private static final char[][] DIGITS = {
	    "||- -||".toCharArray(),
	    "     ||".toCharArray(),
	    "| --- |".toCharArray(),
	    "  ---||".toCharArray(),
	    " | - ||".toCharArray(),
	    " |---| ".toCharArray(),
	    "||---| ".toCharArray(),
	    "    -||".toCharArray(),
	    "||---||".toCharArray(),
	    " |---||".toCharArray()
    };

    private void display(int size, String number) {
	int width = (3 + size) * number.length() - 1;
	int height = 3 + 2 * size;
	char[] blank = getBlankString(height);
	char[][] grid = new char[width][];
	int kk = 0;
	for (int i = 0; i < number.length(); i++) {
	    char[][] digit = expand(number.charAt(i) - '0', size);
	    for (int j = 0; j < digit.length; j++) {
		grid[kk++] = digit[j];
	    }
	    if (i < number.length() - 1) {
		grid[kk++] = blank;
	    }
	}
	for (int i = height - 1; i >= 0; i--) {
	    for (int j = 0; j < width; j++) {
		System.out.write(grid[j][i]);
	    }
	    System.out.println();
	}
    }

    private char[][] expand(int digit, int size) {
	char[][] ret = new char[2 + size][];
	ret[0] = getRow(" " + DIGITS[digit][0] + " " + DIGITS[digit][1] + " ", size);
	char[] midRow = getRow(DIGITS[digit][2] + " " + DIGITS[digit][3] + " " + DIGITS[digit][4], size);
	for (int i = 1; i < ret.length - 1; i++) {
	    ret[i] = midRow;
	}
	ret[ret.length - 1] = getRow(" " + DIGITS[digit][5] + " " + DIGITS[digit][6] + " ", size);
	return ret;
    }

    private char[] getRow(String s, int size) {
	StringBuilder sb = new StringBuilder();
	sb.append(s.charAt(0));
	for (int i = 0; i < size; i++) {
	    sb.append(s.charAt(1));
	}
	sb.append(s.charAt(2));
	for (int i = 0; i < size; i++) {
	    sb.append(s.charAt(3));
	}
	sb.append(s.charAt(4));
	return sb.toString().toCharArray();
    }

    private char[] getBlankString(int len) {
	char[] ret = new char[len];
	Arrays.fill(ret, ' ');
	return ret;
    }

    public static void main(String[] args) {
	int size = 2;
	String number = null;
	if (args.length == 1) {
	    if (isValidNumber(args[0])) {
		number = args[0];
	    }
	} else if (args.length == 3) {
	    if ("-size".equals(args[0])) {
		try {
		    size = Integer.parseInt(args[1]);
		    if (size > 0 && size < 1000) {
			if (isValidNumber(args[2])) {
			    number = args[2];
			}
		    }
		}
		catch (NumberFormatException nfe) {
		    // ignore
		}
	    }
	}
	if (number == null) {
	    System.err.println("Usage: java LCD [-size <xx>] <number>");
	    System.err.println("      xx - size of the display (default: 2, 0 < size < 1000)");
	    System.err.println("  number - String of decimal digits to be displayed");
	    System.exit(-1);
	}
	LCD lcd = new LCD();
	lcd.display(size, number);
    }

    private static boolean isValidNumber(String s) {
	boolean ok = true;
	for (int i = 0; i < s.length(); i++) {
	    if (!Character.isDigit(s.charAt(i))) {
		ok = false;
		break;
	    }
	}
	return ok;
    }
}
