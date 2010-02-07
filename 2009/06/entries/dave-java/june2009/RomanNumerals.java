import java.io.PrintStream;

public class RomanNumerals
{
  public static void main(String[] args)
  {
    int[] decimal = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
    String[] roman = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
    int number = Integer.parseInt(args[0]);

    int stage = 0;

    while (number > 0)
      if (number >= decimal[stage]) {
        System.out.print(roman[stage]);
        number -= decimal[stage];
      } else {
        ++stage;
      }
  }
}
