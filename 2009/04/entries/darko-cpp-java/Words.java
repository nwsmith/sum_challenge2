import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Words {

	private static int len;
	private static char[][] words;
	private static final long TIME_LIMIT = 540000;
	private static final int NO_IMPROVEMENT_LIMIT = 1 << 18;
	private static final boolean LONGEST_PATH = true;

	private void work(String fileName, String s, String t) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		words = new char[1 << 16][];
		int n = 0;
		int six = -1;
		int tix = -1;
		String word;
		while ((word = br.readLine()) != null) {
			if (word.length() == len) {
				if (s.equals(word)) {
					six = n;
				}
				if (t.equals(word)) {
					tix = n;
				}
				words[n++] = word.toCharArray();
			}
		}
		if (six < 0) {
			if (tix < 0) {
				System.err.println("Neither [" + s + "] nor [" + t
						+ "] found in the dictionary.");
			} else {
				System.err.println("Word [" + s
						+ "] not found in the dictionary.");
			}
			System.exit(0);
		} else if (tix < 0) {
			System.err.println("Word [" + t + "] not found in the dictionary.");
			System.exit(0);
		}
		Map<Integer, Set<Integer>> graph = new HashMap<Integer, Set<Integer>>();
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				if (oneCharAway(words[i], words[j])) {
					// System.err.println(words[i] + " " + words[j]);
					if (!graph.containsKey(i)) {
						graph.put(i, new HashSet<Integer>());
					}
					if (!graph.containsKey(j)) {
						graph.put(j, new HashSet<Integer>());
					}
					graph.get(i).add(j);
					graph.get(j).add(i);
				}
			}
		}

		State[] q = new State[1 << 16];
		int qs = 0;
		int qe = 0;
		boolean[] seen = new boolean[n];
		q[qe++] = new State(six, null);
		seen[six] = true;
		List<Integer> list = null;
		while (qs < qe) {
			State cur = q[qs++];
			if (cur.k == tix) {
				list = new ArrayList<Integer>();
				populate(cur, list);
				break;
			}
			int u = cur.k;
			if (graph.containsKey(u)) {
				for (int v : graph.get(u)) {
					if (!seen[v]) {
						q[qe++] = new State(v, cur);
						seen[v] = true;
					}
				}
			}
		}
		if (list == null) {
			System.err.println("Chain not found linking [" + s + "] and [" + t
					+ "]");
			System.exit(0);
		} else {
			List<Integer> bestList = new ArrayList<Integer>(list);
			if (LONGEST_PATH) {
				// insert something in there, so we can find a longer path
				if (list.size() == 2) {
					int aa = list.get(0);
					int bb = list.get(1);
					int cc = -1;
					for (int xx : graph.get(aa)) {
						if (xx == bb) {
							continue;
						}
						for (int yy : graph.get(bb)) {
							if (yy == aa) {
								continue;
							}
							if (xx == yy) {
								cc = xx;
								break;
							}
						}
						if (cc >= 0) {
							break;
						}
					}
					if (cc >= 0) {
						list.add(1, cc);
					}
				}
				if (list.size() > 2) {
					List<Integer> originalList = new ArrayList<Integer>(list);
					long startTime = System.currentTimeMillis();
					Random rnd = new Random();
					List<Integer> tmpList = new ArrayList<Integer>();
					LinkedList<State> qq = new LinkedList<State>();
					while (System.currentTimeMillis() - startTime < TIME_LIMIT) {
						list = new ArrayList<Integer>(originalList);
						int curCount = NO_IMPROVEMENT_LIMIT;
						while (curCount-- > 0) {
							{
								int k1 = rnd.nextInt(list.size() - 1);
								int k2 = k1 + 1;
								Arrays.fill(seen, false);
								for (int i = 0; i <= k1; i++) {
									seen[list.get(i)] = true;
								}
								for (int i = k2; i < list.size(); i++) {
									seen[list.get(i)] = true;
								}

								int aa = list.get(k1);
								int bb = list.get(k2);
								int cc = -1;
								for (int xx : graph.get(aa)) {
									if (xx == bb || seen[xx]) {
										continue;
									}
									for (int yy : graph.get(bb)) {
										if (yy == aa || seen[yy]) {
											continue;
										}
										if (xx == yy) {
											cc = xx;
											break;
										}
									}
									if (cc >= 0) {
										break;
									}
								}
								if (cc >= 0) {
									list.add(k2, cc);
									k2++;
								}
								if (k2 - k1 > 1) {
									six = list.get(k1);
									tix = list.get(k2);
									seen[tix] = false;
									qq.clear();
									qq.add(new State(six, null));
									while (!qq.isEmpty()) {
										State cur = qq.poll();
										if (cur.k == tix) {
											tmpList.clear();
											populate(cur, tmpList);
											if (tmpList.size() > k2 - k1 + 1) {
												for (int i = 0; i < k2 - k1 + 1; i++) {
													list.remove(k1);
												}
												for (int i = tmpList.size() - 1; i >= 0; i--) {
													list
															.add(k1, tmpList
																	.get(i));
												}
												curCount = NO_IMPROVEMENT_LIMIT;
											}
											break;
										}
										int u = cur.k;
										if (graph.containsKey(u)) {
											for (int v : graph.get(u)) {
												if (!seen[v]) {
													qq
															.add(0, new State(
																	v, cur));
													seen[v] = true;
												}
											}
										}
									}
								}
							}
							{
								int k1 = rnd.nextInt(list.size());
								int k2 = rnd.nextInt(list.size());
								while (k2 == k1) {
									k2 = rnd.nextInt(list.size());
								}
								if (k1 > k2) {
									int tt = k1;
									k1 = k2;
									k2 = tt;
								}
								Arrays.fill(seen, false);
								for (int i = 0; i <= k1; i++) {
									seen[list.get(i)] = true;
								}
								for (int i = k2; i < list.size(); i++) {
									seen[list.get(i)] = true;
								}

								if (k2 - k1 == 1) {
									int aa = list.get(k1);
									int bb = list.get(k2);
									int cc = -1;
									for (int xx : graph.get(aa)) {
										if (xx == bb || seen[xx]) {
											continue;
										}
										for (int yy : graph.get(bb)) {
											if (yy == aa || seen[yy]) {
												continue;
											}
											if (xx == yy) {
												cc = xx;
												break;
											}
										}
										if (cc >= 0) {
											break;
										}
									}
									if (cc >= 0) {
										list.add(k2, cc);
										k2++;
									}
								}
								if (k2 - k1 > 1) {
									six = list.get(k1);
									tix = list.get(k2);
									seen[tix] = false;
									qq.clear();
									qq.add(new State(six, null));
									while (!qq.isEmpty()) {
										State cur = qq.poll();
										if (cur.k == tix) {
											tmpList.clear();
											populate(cur, tmpList);
											if (tmpList.size() > k2 - k1 + 1) {
												for (int i = 0; i < k2 - k1 + 1; i++) {
													list.remove(k1);
												}
												for (int i = tmpList.size() - 1; i >= 0; i--) {
													list
															.add(k1, tmpList
																	.get(i));
												}
												curCount = NO_IMPROVEMENT_LIMIT;
											}
											break;
										}
										int u = cur.k;
										if (graph.containsKey(u)) {
											for (int v : graph.get(u)) {
												if (!seen[v]) {
													qq
															.add(0, new State(
																	v, cur));
													seen[v] = true;
												}
											}
										}
									}
								}
							}
						}
						if (list.size() > bestList.size()) {
							bestList = new ArrayList<Integer>(list);
						}
					}
				}
			}
			for (int i : bestList) {
				System.out.println(words[i]);
			}
			System.err.printf("list size: %d\n", list.size());
			System.err.flush();
		}
	}

	private void populate(State s, List<Integer> list) {
		if (s == null) {
			return;
		}
		populate(s.prev, list);
		list.add(s.k);
	}

	private void print(State s) {
		if (s == null) {
			return;
		}
		print(s.prev);
		System.out.println(words[s.k]);
	}

	private boolean oneCharAway(char[] s1, char[] s2) {
		int cnt = 0;
		for (int i = 0; i < s1.length; i++) {
			if (s1[i] != s2[i]) {
				if (++cnt > 1) {
					break;
				}
			}
		}
		return cnt == 1;
	}

	public static void main(String[] args) {
		if (args.length != 3 || args[0].length() != args[1].length()) {
			System.err
					.println("Usage: java -cp . Words <word1> <word2> <file_name>");
			System.exit(0);
		}
		len = args[1].length();
		Words w = new Words();
		try {
			w.work(args[2], args[0], args[1]);
		} catch (IOException ioe) {
			System.err
					.println("Please make sure the dictionary file is unzipped and in the same directory as Words.class.");
			System.err
					.println("Usage: java -cp . Words <word1> <word2> <file_name>\n\n");
			ioe.printStackTrace();
		}
	}

	class State {
		public int k;
		public State prev;

		public State(int k, State prev) {
			this.k = k;
			this.prev = prev;
		}
	}

}
