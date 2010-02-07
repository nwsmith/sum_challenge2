import java.util.Arrays;
import java.util.List;


public class SevenSegment {

	private static Integer[] segment1 = new Integer[]{0,2,3,5,6,7,8,9};
	private static Integer[] segment2= new Integer[]{0,4,5,6,8,9};
	private static Integer[] segment3 = new Integer[]{0,1,2,3,4,7,8,9};
	private static Integer[] segment4 = new Integer[]{2,3,4,5,6,8,9};
	private static Integer[] segment5 = new Integer[]{0,2,6,8};
	private static Integer[] segment6 = new Integer[]{0,1,3,4,5,6,7,8,9};
	private static Integer[] segment7 = new Integer[]{0,2,3,5,6,8};
	private static List<Integer> segment1List = Arrays.asList(segment1);
	private static List<Integer> segment2List = Arrays.asList(segment2);
	private static List<Integer> segment3List = Arrays.asList(segment3);
	private static List<Integer> segment4List = Arrays.asList(segment4);
	private static List<Integer> segment5List = Arrays.asList(segment5);
	private static List<Integer> segment6List = Arrays.asList(segment6);
	private static List<Integer> segment7List = Arrays.asList(segment7);

	private static Integer[] numbersToRender;
	private static Integer size;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		parseInput(args);
		render();
	}

	private static void parseInput(String[] args) {
		if (args.length != 2) {
			throw new RuntimeException("Invalid arguments.");
		} else {
			try {
				numbersToRender = new Integer[args[0].length()];
				for (int i = 0; i < args[0].length(); i++) {
					numbersToRender[i] = Integer.parseInt(args[0].substring(i, i+1));
				}
				size = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Invalid arguments.");
			}
		}
	}


	private static void render() {
		StringBuilder result = new StringBuilder();
		for (Integer number : numbersToRender) {
			result.append(renderSegment1(number));
		}
		result.append("\n");
		for (int j = 0; j < size; j++) {
			for (Integer number : numbersToRender) {
				result.append(renderSegment2(number));
				result.append(renderSegment3(number));
			}
			result.append("\n");
		}
		for (Integer number : numbersToRender) {
			result.append(renderSegment4(number));
		}
		result.append("\n");

		for (int j = 0; j < size; j++) {
			for (Integer number : numbersToRender) {
				result.append(renderSegment5(number));
				result.append(renderSegment6(number));
			}
			result.append("\n");
		}
		for (Integer number : numbersToRender) {
			result.append(renderSegment7(number));
		}
		result.append("\n");
		System.out.println(result.toString());
	}

	private static String renderSegment1(Integer number) {
		return renderSegments147(segment1List.contains(number));
	}

	private static String renderSegment2(Integer number) {
		return renderSegments25(segment2List.contains(number));

	}

	private static String renderSegment3(Integer number) {
		return renderSegments36(segment3List.contains(number));

	}

	private static String renderSegment4(Integer number) {
		return renderSegments147(segment4List.contains(number));		
	}

	private static String renderSegment5(Integer number) {
		return renderSegments25(segment5List.contains(number));

	}

	private static String renderSegment6(Integer number) {
		return renderSegments36(segment6List.contains(number));

	}

	private static String renderSegment7(Integer number) {
		return renderSegments147(segment7List.contains(number));		
	}

	private static String renderSegments147(Boolean draw) {
		StringBuilder ret = new StringBuilder();
		ret.append(" ");
		for (int i = 0; i < size; i++) {
			if (draw) {
				ret.append("-");
			} else {
				ret.append(" ");	
			}
		}
		ret.append("  ");
		return ret.toString();
	}

	private static String renderSegments25(Boolean draw) {
		StringBuilder ret = new StringBuilder();
		if (draw) {
			ret.append("|");
		} else {
			ret.append(" ");
		}
		for (int i = 0; i < size; i++) {
			ret.append(" ");
		}
		return ret.toString();

	}

	private static String renderSegments36(boolean draw) {
		StringBuilder ret = new StringBuilder();
		if (draw) {
			ret.append("| ");
		} else {
			ret.append("  ");
		}
		return ret.toString();
	}

}
