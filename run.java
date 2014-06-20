import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.awt.MouseInfo;
//TO ADD:
//?backwards compatibility?
public class run {
	public static String timestamp() {
		Calendar c = Calendar.getInstance();
		String s = ":";
		int second = c.get(Calendar.SECOND);
		if (second < 10)
			s = ":0";
		int minute = c.get(Calendar.MINUTE);
		String m = ":";
		if (minute < 10)
			m = ":0";
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int day = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		return (hour + m + minute + s + second + " " + day + "/" + month + "/" + year);
	}
	public static int mousex() {
		return MouseInfo.getPointerInfo().getLocation().x;
	}
	public static int mousey() {
		return MouseInfo.getPointerInfo().getLocation().y;
	}
	public static int readint(String line) {
		int digit = -1;
		int number = 0;
		boolean negative = false;
		int pos = 0;
		int length = line.length();
		while (pos < length) {
			digit = (int)(line.charAt(pos));
			if (digit > 47 && digit < 58)
				digit = digit - 48;
			else if (digit == 45 && number == 0)
				negative = true;
			if (digit >= 0 && digit < 10) {
				if (number < 214748364 || (number == 214748364 && digit <= 7))
					number = number * 10 + digit;
				else
					return number;
			}
			pos = pos + 1;
		}
		if (negative)
			return -number;
		return number;
	}
	public static Autoer auto = new Autoer();
	public static int waittime = 0;
	public static Varslist[] mainvars = null;
	public static Varslist[] vars = null;
	public static Varslist variable = null;
	public static int variance = 5;
	public static int quitafter = -1;
	public static int endingcall = -1;
	public static VarsInt jumpback = null;
	public static boolean mousequit = true;
	public static int scanx = 0;
	public static int scany = 0;
	public static Script scriptlist = null;
	public static Script mainscript = null;
	public static int[] rows = null;
	public static int pos = 0;
	public static int[] scanorder = new int[] {3, 4};
	public static int offx = 0;
	public static int offy = 0;
	public static String[] input = null;
	public static class Varslist {
		public static final int INT = 1;
		public static final int BOOLEAN = 2;
		public static final int COLOR = 3;
		public static final int TIMER = 4;
		public String name;
		public Varslist next;
		public Varslist(String n, Varslist vl) {
			name = n;
			next = vl;
		}
		public boolean istype(int t) {
			return t == 0;
		}
		public int ival() {
			return 0;
		}
		public void setto(String[] args, int[] cvars, int[] cints) {
		}
		public String toString() {
			return "";
		}
	}
	public static class VarsInt extends Varslist {
		public int val;
		public VarsInt(String n, int v, Varslist vl) {
			super(n, vl);
			val = v;
		}
		public boolean istype(int t) {
			return t == INT;
		}
		public int ival() {
			return val;
		}
		public void setto(String[] args, int[] cvars, int[] cints) {
			val = evaluate(args, cvars, cints, 1);
		}
		public String toString() {
			return "" + val;
		}
	}
	public static class VarsBoolean extends Varslist {
		public boolean val;
		public VarsBoolean(String n, boolean v, Varslist vl) {
			super(n, vl);
			val = v;
		}
		public boolean istype(int t) {
			return t == BOOLEAN;
		}
		public int ival() {
			if (val)
				return 1;
			return 0;
		}
		public void setto(String[] args, int[] cvars, int[] cints) {
			val = evaluate(args, cvars, cints, 1) != 0;
		}
		public String toString() {
			return "" + val;
		}
	}
	public static class VarsColor extends Varslist {
		public int[] val;
		public VarsColor(String n, int[] v, Varslist vl) {
			super(n, vl);
			val = v;
		}
		public boolean istype(int t) {
			return t == COLOR;
		}
		public int ival() {
			return (val[0] << 16) + (val[1] << 8) + val[2];
		}
		public void setto(String[] args, int[] cvars, int[] cints) {
			variable = vars[cvars[1]];
			if (variable != null && variable.istype(Varslist.COLOR)) {
				VarsColor vc2 = (VarsColor)(variable);
				val = vc2.val;
			} else if (args[1].equals("at"))
				val = auto.colorsat(inteval(args[2], vars[cvars[2]], cints[2]), inteval(args[3], vars[cvars[3]], cints[3]));
			else {
				int[] ints = intsplit(args, cvars, cints);
				val = new int[] {ints[1], ints[2], ints[3]};
			}
		}
		public String toString() {
			return val[0] + ", " + val[1] + ", " + val[2];
		}
	}
	public static class VarsTimer extends Varslist {
		public long val;
		public VarsTimer(String n, long v, Varslist vl) {
			super(n, vl);
			val = v + System.currentTimeMillis();
		}
		public boolean istype(int t) {
			return t == TIMER;
		}
		public int ival() {
			return (int)(System.currentTimeMillis() - val);
		}
		public void setto(String[] args, int[] cvars, int[] cints) {
			val = inteval(args[1], vars[cvars[1]], cints[1]) + System.currentTimeMillis();
		}
		public String toString() {
			return (System.currentTimeMillis() - val) + "";
		}
	}
	public static class Script {
		public String name;
		public String[][] val;
		public int[][] ival;
		public int[][] vval;
		public int[] action;
		public int[] row;
		public int varcount = 0;
		public Script next = null;
		public Script(String n, int lines) {
			name = n;
			val = new String[lines][0];
			ival = new int[lines][0];
			vval = new int[lines][0];
			action = new int[lines];
			row = new int[lines];
		}
		//get rid of all lines of a specific action
		public void purgelines(int purge) {
			int length = action.length;
			for (int spot = 0; spot < action.length; spot += 1) {
				if (action[spot] == purge)
					length = length - 1;
			}
			String[][] newval = new String[length][0];
			int[][] newival = new int[length][0];
			int[][] newvval = new int[length][0];
			int[] newaction = new int[length];
			int[] newrow = new int[length];
			int index = 0;
			for (int spot = 0; spot < action.length; spot += 1) {
				if (action[spot] != purge) {
					newval[index] = val[spot];
					newival[index] = ival[spot];
					newvval[index] = vval[spot];
					newaction[index] = action[spot];
					newrow[index] = row[spot];
					index += 1;
				}
			}
			val = newval;
			ival = newival;
			vval = newvval;
			action = newaction;
			row = newrow;
		}
	}
	public static void main(String[] args) {
		if (args.length < 1)
			end("You need a script to run.");
		Filer filer = new Filer("scripts/" + args[0] + ".txt");
		if (!filer.fileisthere())
			end("Script \"" + args[0] + "\" does not exist.");
		filer.readfile();
		String[] lines = filer.getlines();
		scriptlist = new Script(args[0], lines.length);
		//quit if there are errors
		if (!verify(lines, scriptlist))
			System.exit(0);
		input = args;
		try {
			runscript(scriptlist, true);
		} catch(Exception e) {
			System.out.println("Error: script \"" + mainscript.name + "\" at line " + rows[pos] + " caused this exception:");
			e.printStackTrace();
		}
	}
	//format the script and make sure it's valid
	public static boolean verify(String[] lines, Script putter) {
		int linecount = lines.length;
		boolean valid = true;
		//clip all the lines
		for (int row = 0; row < linecount; row += 1) {
			lines[row] = clip(lines[row]);
		}
		//assign all the lines an action, check for errors
		for (int row = 0; row < linecount; row += 1) {
			//make sure the line was validly built
			valid = build(putter, lines, row) && valid;
			//verify other scripts used
			if (putter.action[row] == 10 && putter.val[row].length > 0 && scriptcalled(putter.val[row][0], scriptlist) == null) {
				Filer inner = new Filer("scripts/" + putter.val[row][0] + ".txt");
				if (inner.fileisthere()) {
					inner.readfile();
					putter.next = new Script(putter.val[row][0], inner.lines());
					valid = verify(inner.getlines(), putter.next) && valid;
				} else {
					System.out.println("Missing content in " + putter.name + " on line " + (row + 1) + ": script \"" + putter.val[row][0] + "\" was not found");
					valid = false;
				}
			}
		}
		//stop if invalid
		if (!valid)
			return false;
		//get rid of comments
		putter.purgelines(0);
		linecount = putter.action.length;
		//assign the values to commands that need to know the line number
		for (int row = 0; row < linecount; row += 1) {
			//set the jumpline for jump & call, and for endingcall labels
			if (putter.action[row] == 8 || putter.action[row] == 9 || (putter.action[row] == 28 && putter.val[row][0].equals("endingcall"))) {
				int index = 0;
				if (putter.action[row] == 28)
					index = 1;
				String label = ":" + putter.val[row][index];
				int jumpline = 0;
				while (jumpline < linecount && !putter.val[jumpline][0].equals(label))
					jumpline = jumpline + 1;
				if (jumpline >= linecount) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": no label called \"" + putter.val[row][index] + "\"");
					valid = false;
				} else {
					int newjumpline = jumpline;
					for (int line = 0; line <= jumpline; line += 1) {
						if (putter.action[line] < 1)
							newjumpline -= 1;
					}
					putter.ival[row][index] = newjumpline;
				}
			//find the end command for the for line, rearrange the values
			} else if (putter.action[row] == 20) {
				int jumpline = row;
				int layer = 1;
				while (jumpline + 1 < linecount && layer > 0) {
					jumpline += 1;
					if (putter.action[jumpline] == 20)
						layer = layer + 1;
					else if (putter.action[jumpline] == 21)
						layer = layer - 1;
				}
				if (layer > 0) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": for missing an end command");
					valid = false;
				} else {
					int newrow = row;
					for (int line = 0; line < row; line += 1) {
						if (putter.action[line] < 1)
							newrow -= 1;
					}
					//reassign the data
					putter.val[jumpline] = new String[] {"", putter.val[row][0], putter.val[row][2], putter.val[row][3]};
					putter.ival[jumpline] = new int[] {newrow, putter.ival[row][0], putter.ival[row][2], putter.ival[row][3]};
					putter.vval[jumpline] = new int[] {0, putter.vval[row][0], putter.vval[row][2], putter.vval[row][3]};
					putter.val[row] = new String[] {putter.val[row][0], putter.val[row][1]};
					putter.ival[row] = new int[] {putter.ival[row][0], putter.ival[row][1]};
					putter.vval[row] = new int[] {putter.vval[row][0], putter.vval[row][1]};
				}
			//make sure end commands have already been found
			} else if (putter.action[row] == 21 && putter.ival[row].length < 3) {
				System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": end missing a for command");
				valid = false;
			//set the if command's next line
			} else if (putter.action[row] == 19) {
				int jumpline = row + 1;
				int layer = 0;
				if (jumpline < linecount && putter.val[jumpline][0].equals("[")) {
					layer = 1;
					while (jumpline + 1 < linecount && layer > 0) {
						jumpline += 1;
						if (putter.val[jumpline][0].equals("["))
							layer = layer + 1;
						else if (putter.val[jumpline][0].equals("]"))
							layer = layer - 1;
					}
				}
				if (layer > 0) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": if bracket \"[\" missing an opposite \"]\"");
					valid = false;
				} else {
					int newjumpline = jumpline;
					for (int line = 0; line <= jumpline; line += 1) {
						if (putter.action[line] < 1)
							newjumpline -= 1;
					}
					putter.ival[row][0] = newjumpline;
				}
			}
		}
		//get rid of labels and brackets
		putter.purgelines(-1);
		//find all possible variable names
		VarsInt names = null;
		Varslist head;
		linecount = putter.action.length;
		String name = "";
		for (int row = 0; row < linecount; row += 1) {
			//new variable storage
			if (putter.action[row] == 11 || putter.action[row] == 20) {
				if (putter.action[row] == 11)
					name = putter.val[row][1];
				else
					name = putter.val[row][0];
				if (name.startsWith(" ") || name.equals("true") || name.equals("false")) {
					System.out.println("Bad syntax in " + putter.name + " on line " + putter.row[row] + ": that name is reserved");
					valid = false;
				} else {
					for (head = names; head != null; head = head.next) {
						if (name.equals(head.name))
							break;
					}
					if (head == null) {
						putter.varcount += 1;
						names = new VarsInt(name, putter.varcount, names);
					}
				}
			}
		}
		//link all values with their variable name if they have one
		VarsInt vi;
		for (int row = 0; row < linecount; row += 1) {
			for (int col = 0; col < putter.vval[row].length; col += 1) {
				name = putter.val[row][col];
				for (head = names; head != null; head = head.next) {
					if (name.equals(head.name)) {
						vi = (VarsInt)(head);
						putter.vval[row][col] = vi.val;
						break;
					}
				}
			}
		}
		return valid;
	}
	//remove whitespace from the beginning of the line
	public static String clip(String theline) {
		int spot = 0;
		int length = theline.length();
		while (spot < length && (theline.charAt(spot) == ' ' || theline.charAt(spot) == '\t'))
			spot = spot + 1;
		return theline.substring(spot, length);
	}
	//build the line, return the necessary arguments
	public static boolean build(Script s, String[] lines, int row) {
		String line = lines[row];
		int argnum = 0;
		boolean valid = true;
		if (line.startsWith("left")) {
			s.action[row] = 1;
			s.val[row] = split(line.substring(4, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("right")) {
			s.action[row] = 2;
			s.val[row] = split(line.substring(5, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("move")) {
			s.action[row] = 3;
			s.val[row] = split(line.substring(4, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("scroll")) {
			s.action[row] = 4;
			s.val[row] = split(line.substring(5, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("pausenot")) {
			s.action[row] = 5;
			s.val[row] = split(line.substring(8, line.length()), true);
			argnum = 3;
		} else if (line.startsWith("pause")) {
			s.action[row] = 6;
			s.val[row] = split(line.substring(5, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("jumpback")) {
			s.action[row] = 7;
			s.val[row] = new String[] {""};
			argnum = 0;
		} else if (line.startsWith("jump")) {
			s.action[row] = 8;
			s.val[row] = split(line.substring(4, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("call")) {
			s.action[row] = 9;
			s.val[row] = split(line.substring(4, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("run")) {
			s.action[row] = 10;
			s.val[row] = split(line.substring(3, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("create")) {
			s.action[row] = 11;
			s.val[row] = split(line.substring(6, line.length()), true);
			if (s.val[row].length > 1 && s.val[row][1].startsWith(".")) {
				System.out.println("Bad syntax in " + s.name + " on line " + (row + 1) + ": variables cannot start with \".\"");
				valid = false;
			}
			argnum = 3;
		} else if (line.startsWith("setargto")) {
			s.action[row] = 12;
			s.val[row] = split(line.substring(8, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("set")) {
			s.action[row] = 13;
			s.val[row] = split(line.substring(3, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("+=")) {
			s.action[row] = 14;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("-=")) {
			s.action[row] = 15;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("*=")) {
			s.action[row] = 16;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("/=")) {
			s.action[row] = 17;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("%=")) {
			s.action[row] = 18;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("if")) {
			s.action[row] = 19;
			s.val[row] = split(". " + line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("for")) {
			s.action[row] = 20;
			s.val[row] = split(line.substring(3, line.length()), true);
			if (s.val[row].length > 0 && s.val[row][0].startsWith(".")) {
				System.out.println("Bad syntax in " + s.name + " on line " + (row + 1) + ": variables cannot start with \".\"");
				valid = false;
			}
			argnum = 4;
		} else if (line.startsWith("end")) {
			s.action[row] = 21;
			s.val[row] = new String[] {""};
			argnum = 0;
		} else if (line.startsWith("break")) {
			s.action[row] = 22;
			s.val[row] = new String[] {""};
			argnum = 0;
		} else if (line.startsWith("return")) {
			s.action[row] = 23;
			s.val[row] = new String[] {""};
			argnum = 0;
		} else if (line.startsWith("quit")) {
			s.action[row] = 24;
			s.val[row] = new String[] {""};
			argnum = 0;
		} else if (line.startsWith("type")) {
			s.action[row] = 25;
			int begin = 4;
			while (begin < line.length() && line.charAt(begin) != '.')
				begin += 1;
			s.val[row] = new String[] {line.substring(begin, line.length())};
			argnum = 0;
		} else if (line.startsWith("press")) {
			s.action[row] = 26;
			s.val[row] = split(line.substring(5, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("release")) {
			s.action[row] = 27;
			s.val[row] = split(line.substring(7, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("optionon")) {
			s.action[row] = 28;
			s.val[row] = split(line.substring(8, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("optionoff")) {
			s.action[row] = 29;
			s.val[row] = split(line.substring(9, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("printval")) {
			s.action[row] = 30;
			s.val[row] = split(line.substring(8, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("println")) {
			s.action[row] = 31;
			int begin = 6;
			while (begin + 1 < line.length() && line.charAt(begin) != '.')
				begin += 1;
			s.val[row] = new String[] {line.substring(begin + 1, line.length()) + "\n"};
			argnum = 0;
		} else if (line.startsWith("print")) {
			s.action[row] = 31;
			int begin = 4;
			while (begin + 1 < line.length() && line.charAt(begin) != '.')
				begin += 1;
			s.val[row] = new String[] {line.substring(begin + 1, line.length())};
			argnum = 0;
		} else if (line.startsWith(":")) {
			s.action[row] = -1;
			s.val[row] = split(line, true);
			argnum = 0;
		} else if (line.startsWith("[") || line.startsWith("(") || line.startsWith("{")) {
			s.action[row] = -1;
			s.val[row] = new String[] {"["};
			argnum = 0;
		} else if (line.startsWith("]") || line.startsWith(")") || line.startsWith("}")) {
			s.action[row] = -1;
			s.val[row] = new String[] {"]"};
			argnum = 0;
		} else {
			s.val[row] = new String[] {line};
			argnum = 0;
		}
		//give string values an int value
		s.ival[row] = ibuild(s.val[row]);
		s.vval[row] = new int[s.val[row].length];
		s.row[row] = row + 1;
		if (argnum > s.val[row].length) {
			if (s.action[row] == 19)
				System.out.println("Bad syntax in " + s.name + " on line " + (row + 1) + ": only 0 arguments instead of 1");
			else
				System.out.println("Bad syntax in " + s.name + " on line " + (row + 1) + ": only " + s.val[row].length + " arguments instead of " + argnum + "");
			valid = false;
		}
		return valid;
	}
	//separate lines by whitespace or by commas
	public static String[] split(String theline, boolean whitespace) {
		Varslist vl = null;
		int count = 0;
		int index = 0;
		int begin = 0;
		int length = theline.length();
		if (whitespace) {
			while (index < length) {
				while (index < length && (theline.charAt(index) == '\t' || theline.charAt(index) == ' '))
					index = index + 1;
				begin = index;
				while (index < length && theline.charAt(index) != '\t' && theline.charAt(index) != ' ')
					index = index + 1;
				vl = new Varslist(theline.substring(begin, index), vl);
				count = count + 1;
			}
		} else {
			while (index < length) {
				while (index < length && theline.charAt(index) == ',')
					index = index + 1;
				begin = index;
				while (index < length && theline.charAt(index) != ',')
					index = index + 1;
				vl = new Varslist(theline.substring(begin, index), vl);
				count = count + 1;
			}
		}
		if (count == 0) {
			count = 1;
			vl = new Varslist("", null);
		}
		String[] list = new String[count];
		for (int spot = count - 1; spot >= 0; spot -= 1) {
			list[spot] = vl.name;
			vl = vl.next;
		}
		return list;
	}
	//get the int values of the strings
	public static int[] ibuild(String[] val) {
		int[] ival = new int[val.length];
		int op = 0;
		for (int spot = 0; spot < val.length; spot += 1) {
			if (val[spot].equals("+"))
				op = 1;
			else if (val[spot].equals("-"))
				op = 2;
			else if (val[spot].equals("*"))
				op = 3;
			else if (val[spot].equals("/"))
				op = 4;
			else if (val[spot].equals("%"))
				op = 5;
			else if (val[spot].equals("random"))
				op = 6;
			else if (val[spot].equals("=="))
				op = 7;
			else if (val[spot].equals("!="))
				op = 8;
			else if (val[spot].equals(">="))
				op = 9;
			else if (val[spot].equals(">"))
				op = 10;
			else if (val[spot].equals("<="))
				op = 11;
			else if (val[spot].equals("<"))
				op = 12;
			else if (val[spot].equals("not"))
				op = 13;
			else if (val[spot].equals("or"))
				op = 14;
			else if (val[spot].equals("and"))
				op = 15;
			else if (val[spot].equals("input"))
				op = 16;
			else if (val[spot].equals("colorat"))
				op = 17;
			else if (val[spot].equals("scan"))
				op = 18;
			else
				op = 0;
			if (op != 0) {
				ival[spot] = op;
				val[spot] = " " + val[spot];
			} else if (val[spot].equals("true"))
				ival[spot] = 1;
			else
				ival[spot] = readint(val[spot]);
		}
		return ival;
	}
	//find the variable with the right name
	public static Script scriptcalled(String s, Script sc) {
		while (sc != null) {
			if (sc.name.equals(s))
				return sc;
			sc = sc.next;
		}
		return null;
	}
	//run the script
	public static void runscript(Script s, boolean ismainvars) {
		mainscript = s;
		//extract all the data
		String[][] lines = s.val;
		int[][] cilist = s.ival;
		int[][] cvlist = s.vval;
		int[] actions = s.action;
		rows = s.row;
		vars = new Varslist[s.varcount + 1];
		if (ismainvars)
			mainvars = vars;
		//prepare to extract the data even further
		String[] args = null;
		int[] cvars = null;
		int[] cints = null;
		int action = 0;
		//parse the script
		int linecount = lines.length;
		pos = 0;
		int[] ints = null;
		while (pos < linecount) {
			args = lines[pos];
			cvars = cvlist[pos];
			cints = cilist[pos];
			action = actions[pos];
/*
System.out.print(s.name + " @ L" + rows[pos] + "- A" + action + ": ");
for (int i = 0; i < args.length; i += 1) {
System.out.print(args[i] + ", ");
}
System.out.println();
*/
			if (action < 7) {
				//left
				if (action == 1) {
					ints = intsplit(args, cvars, cints);
					if (args.length > 1)
						auto.left(ints[0] + offx, ints[1] + offy);
					else
						auto.left(ints[0]);
				//right
				} else if (action == 2) {
					ints = intsplit(args, cvars, cints);
					if (args.length > 1)
						auto.right(ints[0] + offx, ints[1] + offy);
					else
						auto.right(ints[0]);
				//move
				} else if (action == 3) {
					ints = intsplit(args, cvars, cints);
					auto.move(ints[0] + offx, ints[1] + offy);
				//scroll
				} else if (action == 4) {
					if (args.length < 2)
						auto.scroll(args[0]);
					else
						auto.scroll(args[0], inteval(args[1], vars[cvars[1]], cints[1]));
				//pausenot
				} else if (action == 5) {
					ints = intsplit(args, cvars, cints);
					variable = vars[cvars[2]];
					if (variable != null && variable.istype(Varslist.COLOR)) {
						VarsColor vc = (VarsColor)(variable);
						if (ints.length < 4)
							pausenot(ints[0] + offx, ints[1] + offy, vc.val[0], vc.val[1], vc.val[2], false);
						else {
							waittime = ints[3];
							pausenot(ints[0] + offx, ints[1] + offy, vc.val[0], vc.val[1], vc.val[2], true);
						}
					} else {
						if (ints.length < 6)
							pausenot(ints[0] + offx, ints[1] + offy, ints[2], ints[3], ints[4], false);
						else {
							waittime = ints[5];
							pausenot(ints[0] + offx, ints[1] + offy, ints[2], ints[3], ints[4], true);
						}
					}
				//pause
				} else if (action == 6) {
					ints = intsplit(args, cvars, cints);
					if (ints.length < 3)
						pause(ints[0]);
					else {
						variable = vars[cvars[2]];
						if (variable != null && variable.istype(Varslist.COLOR)) {
							VarsColor vc = (VarsColor)(variable);
							if (ints.length < 4)
								pause(ints[0] + offx, ints[1] + offy, vc.val[0], vc.val[1], vc.val[2], false);
							else {
								waittime = ints[3];
								pause(ints[0] + offx, ints[1] + offy, vc.val[0], vc.val[1], vc.val[2], true);
							}
						} else {
							if (ints.length < 6)
								pause(ints[0] + offx, ints[1] + offy, ints[2], ints[3], ints[4], false);
							else {
								waittime = ints[5];
								pause(ints[0] + offx, ints[1] + offy, ints[2], ints[3], ints[4], true);
							}
						}
					}
				}
			} else if (action < 11) {
				//jumpback
				if (action == 7) {
					if (jumpback != null) {
						pos = jumpback.val;
						jumpback = (VarsInt)(jumpback.next);
					}
				//jump
				} else if (action == 8)
					pos = cints[0];
				//call
				else if (action == 9) {
					jumpback = new VarsInt("", pos, jumpback);
					pos = cints[0];
				//run
				} else if (action == 10) {
					Varslist[] oldvars = vars;
					Script oldmainscript = mainscript;
					int[] oldrows = rows;
					int oldpos = pos;
					String[] oldinput = input;
					input = args;
					runscript(scriptcalled(args[0], scriptlist), false);
					vars = oldvars;
					mainscript = oldmainscript;
					rows = oldrows;
					pos = oldpos;
					input = oldinput;
				}
			} else if (action < 19) {
				//create
				if (action == 11) {
					if (args[0].equals("int"))
						vars[cvars[1]] = new VarsInt(args[1], evaluate(args, cvars, cints, 2), vars[cvars[1]]);
					else if (args[0].equals("boolean"))
						vars[cvars[1]] = new VarsBoolean(args[1], evaluate(args, cvars, cints, 2) != 0, vars[cvars[1]]);
					else if (args[0].equals("color")) {
						variable = vars[cvars[2]];
						if (variable != null && variable.istype(Varslist.COLOR)) {
							VarsColor vc = (VarsColor)(variable);
							vars[cvars[1]] = new VarsColor(args[1], vc.val, vars[cvars[1]]);
						} else if (args[2].equals("at"))
							vars[cvars[1]] = new VarsColor(args[1], auto.colorsat(inteval(args[3], vars[cvars[3]], cints[3]), inteval(args[4], vars[cvars[4]], cints[4])), vars[cvars[1]]);
						else {
							ints = intsplit(args, cvars, cints);
							vars[cvars[1]] = new VarsColor(args[1], new int[] {ints[2], ints[3], ints[4]}, vars[cvars[1]]);
						}
					} else if (args[0].equals("timer"))
						vars[cvars[1]] = new VarsTimer(args[1], inteval(args[2], vars[cvars[2]], cints[2]), vars[cvars[1]]);
				//setargto
				} else if (action == 12) {
					variable = vars[cvars[1]];
					for (int spot = 1; spot < input.length; spot += 1) {
						if (input[spot].startsWith(args[0]) && variable != null) {
							args = split(" ," + input[spot].substring(args[0].length(), input[spot].length()), false);
							variable.setto(args, new int[args.length], ibuild(args));
							break;
						}
					}
				//set
				} else if (action == 13) {
					variable = vars[cvars[0]];
					if (variable != null)
						variable.setto(args, cvars, cints);
				//+=, -=, *=, /=, %=
				} else if (action < 19) {
					variable = vars[cvars[0]];
					if (variable != null && variable.istype(Varslist.INT)) {
						VarsInt vi = (VarsInt)(variable);
						if (action == 14)
							vi.val += inteval(args[1], vars[cvars[1]], cints[1]);
						else if (action == 15)
							vi.val -= inteval(args[1], vars[cvars[1]], cints[1]);
						else if (action == 16)
							vi.val *= inteval(args[1], vars[cvars[1]], cints[1]);
						else if (action == 17)
							vi.val /= inteval(args[1], vars[cvars[1]], cints[1]);
						else
							vi.val %= inteval(args[1], vars[cvars[1]], cints[1]);
					}
				}
			} else if (action < 25) {
				//if
				if (action == 19) {
					if (evaluate(args, cvars, cints, 1) == 0)
						pos = cilist[pos][0];
				//for
				} else if (action == 20)
					vars[cvars[0]] = new VarsInt(args[0], inteval(args[1], vars[cvars[1]], cints[1]), vars[cvars[0]]);
				//end
				else if (action == 21) {
					VarsInt vi = (VarsInt)(vars[cvars[1]]);
					vi.val += inteval(args[3], vars[cvars[3]], cints[3]);
					if (vi.val > inteval(args[2], vars[cvars[2]], cints[2]))
						vars[cvars[1]] = vi.next;
					else
						pos = cints[0];
				//break
				} else if (action == 22) {
					pos += 1;
					while (pos < linecount && actions[pos] != 21)
						pos += 1;
					if (pos < linecount)
						vars[cvlist[pos][1]] = vars[cvlist[pos][1]].next;
				//return
				} else if (action == 23)
					return;
				//quit
				else if (action == 24)
					end("Quitting script \"" + s.name + "\" from line " + s.row[pos] + ".");
			} else {
				//type
				if (action == 25)
					auto.type(args[0]);
				//press
				else if (action == 26) {
					if (args[0].length() > 1)
						auto.button(args[0], 1);
					else
						auto.button(args[0].charAt(0), 1);
				//release
				} else if (action == 27) {
					if (args[0].length() > 1)
						auto.button(args[0], 3);
					else
						auto.button(args[0].charAt(0), 3);
				//optionon
				} else if (action == 28) {
					if (args[0].equals("quitafter"))
						quitafter = inteval(args[1], vars[cvars[1]], cints[1]);
					else if (args[0].equals("endingcall")) {
						if (s == scriptlist)
							endingcall = cints[1];
					} else if (args[0].equals("variance"))
						variance = inteval(args[1], vars[cvars[1]], cints[1]);
					else if (args[0].equals("mousequit"))
						mousequit = true;
					else if (args[0].equals("scanorder")) {
						if (args[1].equals("left")) {
							scanorder[0] = 1;
							if (args[2].equals("up"))
								scanorder[1] = 2;
							else
								scanorder[1] = 4;
						} else if (args[1].equals("up")) {
							scanorder[0] = 2;
							if (args[2].equals("left"))
								scanorder[1] = 1;
							else
								scanorder[1] = 3;
						} else if (args[1].equals("right")) {
							scanorder[0] = 3;
							if (args[2].equals("up"))
								scanorder[1] = 2;
							else
								scanorder[1] = 4;
						} else if (args[1].equals("down")) {
							scanorder[0] = 4;
							if (args[2].equals("left"))
								scanorder[1] = 1;
							else
								scanorder[1] = 3;
						}
					} else if (args[0].equals("offset")) {
						offx = inteval(args[1], vars[cvars[1]], cints[1]);
						offy = inteval(args[2], vars[cvars[2]], cints[2]);
					}
				//optionoff
				} else if (action == 29) {
					if (args[0].equals("quitafter"))
						quitafter = -1;
					else if (args[0].equals("endingcall")) {
						if (s == scriptlist)
							endingcall = -1;
					} else if (args[0].equals("variance"))
						variance = 0;
					else if (args[0].equals("mousequit"))
						mousequit = false;
					else if (args[0].equals("scanorder"))
						scanorder = new int[] {3, 4};
					else if (args[0].equals("offset")) {
						offx = 0;
						offy = 0;
					}
				//printval
				} else if (action == 30) {
					if (args[0].startsWith(".")) {
						if (args[0].equals(".timestamp"))
							System.out.print(timestamp());
						else if (args[0].equals(".mousex"))
							System.out.print("" + mousex());
						else if (args[0].equals(".mousey"))
							System.out.print("" + mousey());
						else if (args[0].equals(".scanx"))
							System.out.print("" + scanx);
						else if (args[0].equals(".scany"))
							System.out.print("" + scany);
					} else {
						variable = vars[cvars[0]];
						if (variable != null)
							System.out.print("" + variable);
					}
				//println, print
				} else if (action == 31)
					System.out.print(args[0]);
			}
			pos = pos + 1;
		}
	}
	//get all the right int values
	public static int[] intsplit(String[] vals, int[] cvars, int[] cints) {
		int[] list = new int[vals.length];
		for (int spot = 0; spot < vals.length; spot += 1) {
			list[spot] = inteval(vals[spot], vars[cvars[spot]], cints[spot]);
		}
		return list;
	}
	//return the right int value
	public static int inteval(String s, Varslist cvar, int cint) {
		if (cvar != null)
			return cvar.ival();
		else if (s.startsWith(".")) {
			if (s.equals(".mousex"))
				return mousex();
			else if (s.equals(".mousey"))
				return mousey();
			else if (s.equals(".scanx"))
				return scanx;
			else if (s.equals(".scany"))
				return scany;
		}
		return cint;
	}
	//evaluate the arithmetic/boolean equation
	public static int evaluate(String[] vals, int[] cvars, int[] cints, int off) {
		int length = vals.length;
		int[] ints = new int[length];
		int add = 0;
		for (int spot = length - 1; spot >= off; spot -= 1) {
			if (vals[spot].startsWith(" ")) {
				if (cints[spot] < 7) {
					// +
					if (cints[spot] == 1) {
						ints[spot] = ints[spot + 1] + ints[spot + 2];
						add = 2;
					// -
					} else if (cints[spot] == 2) {
						ints[spot] = ints[spot + 1] - ints[spot + 2];
						add = 2;
					// *
					} else if (cints[spot] == 3) {
						ints[spot] = ints[spot + 1] * ints[spot + 2];
						add = 2;
					// /
					} else if (cints[spot] == 4) {
						ints[spot] = ints[spot + 1] / ints[spot + 2];
						add = 2;
					// %
					} else if (cints[spot] == 5) {
						ints[spot] = ints[spot + 1] % ints[spot + 2];
						add = 2;
					// random
					} else if (cints[spot] == 6) {
						ints[spot] = (int)(Math.random() * ints[spot + 1]);
						add = 1;
					}
				} else if (cints[spot] < 13) {
					// ==
					if (cints[spot] == 7) {
						if (ints[spot + 1] == ints[spot + 2])
							ints[spot] = 1;
						add = 2;
					// !=
					} else if (cints[spot] == 8) {
						if (ints[spot + 1] != ints[spot + 2])
							ints[spot] = 1;
						add = 2;
					// >=
					} else if (cints[spot] == 9) {
						if (ints[spot + 1] >= ints[spot + 2])
							ints[spot] = 1;
						add = 2;
					// >
					} else if (cints[spot] == 10) {
						if (ints[spot + 1] > ints[spot + 2])
							ints[spot] = 1;
						add = 2;
					// <=
					} else if (cints[spot] == 11) {
						if (ints[spot + 1] <= ints[spot + 2])
							ints[spot] = 1;
						add = 2;
					// <
					} else if (cints[spot] == 12) {
						if (ints[spot + 1] < ints[spot + 2])
							ints[spot] = 1;
						add = 2;
					}
				} else if (cints[spot] < 16) {
					// not
					if (cints[spot] == 13) {
						if (ints[spot + 1] == 0)
							ints[spot] = 1;
						add = 1;
					// or
					} else if (cints[spot] == 14) {
						if ((ints[spot + 1] | ints[spot + 2]) != 0)
							ints[spot] = 1;
						add = 2;
					// and
					} else if (cints[spot] == 15) {
						if ((ints[spot + 1] & ints[spot + 2]) != 0)
							ints[spot] = 1;
						add = 2;
					}
				} else {
					// input
					if (cints[spot] == 16) {
						for (int loc = 1; loc < input.length; loc += 1) {
							if (input[loc].equals(vals[spot + 1])) {
								ints[spot] = 1;
								break;
							}
						}
						add = 1;
					// colorat
					} else if (cints[spot] == 17) {
						variable = vars[cvars[spot + 1]];
						if (variable != null && variable.istype(Varslist.COLOR)) {
							VarsColor vc = (VarsColor)(variable);
							if (colorat(ints[spot + 1] + offx, ints[spot + 2] + offy, vc.val[0], vc.val[1], vc.val[2]))
								ints[spot] = 1;
							add = 3;
						} else {
							if (colorat(ints[spot + 1] + offx, ints[spot + 2] + offy, ints[spot + 3], ints[spot + 4], ints[spot + 5]))
								ints[spot] = 1;
							add = 5;
						}
					// scan
					} else if (cints[spot] == 18) {
						scanx = -1;
						scany = -1;
						BufferedImage image = auto.screenshot(ints[spot + 1] + offx, ints[spot + 2] + offy, ints[spot + 3], ints[spot + 4]);
						int pixel = 0;
						int r = 0;
						int g = 0;
						int b = 0;
						int ir = 0;
						int ig = 0;
						int ib = 0;
						variable = vars[cvars[spot + 5]];
						if (variable != null && variable.istype(Varslist.COLOR)) {
							VarsColor vc = (VarsColor)(variable);
							ir = vc.val[0];
							ig = vc.val[1];
							ib = vc.val[2];
							add = 5;
						} else {
							ir = ints[spot + 5];
							ig = ints[spot + 6];
							ib = ints[spot + 7];
							add = 7;
						}
						int loc0 = 0;
						int loc1 = 0;
						int add0 = 1;
						int add1 = 1;
						int max0 = 0;
						int max1 = 0;
						int getx = 0;
						int gety = 0;
						//up or down is first
						if (scanorder[0] % 2 == 0) {
							max0 = image.getHeight();
							max1 = image.getWidth();
							getx = 1;
						//left or right is first
						} else {
							max0 = image.getWidth();
							max1 = image.getHeight();
							gety = 1;
						}
						if (scanorder[0] < 3) {
							add0 = -1;
							loc0 = max0 - 1;
						}
						if (scanorder[1] < 3) {
							add1 = -1;
							loc1 = max1 - 1;
						}
						int[] coords = new int[] {loc0, loc1};
						outer: for (int m = 0; m < max1; m += 1) {
							for (int n = 0; n < max0; n += 1) {
								pixel = image.getRGB(coords[getx], coords[gety]);
								r = (pixel >> 16) & 255;
								g = (pixel >> 8) & 255;
								b = pixel & 255;
								if (r <= ir + variance && r >= ir - variance && g <= ig + variance && g >= ig - variance && b <= ib + variance && b >= ib - variance) {
									scanx = ints[spot + 1] + coords[getx];
									scany = ints[spot + 2] + coords[gety];
									ints[spot] = 1;
									break outer;
								}
								coords[0] = coords[0] + add0;
							}
							coords[1] = coords[1] + add1;
							coords[0] = loc0;
						}
					}
				}
				length = length - add;
				for (int loc = spot + 1; loc < length; loc += 1) {
					ints[loc] = ints[loc + add];
				}
			} else
				ints[spot] = inteval(vals[spot], vars[cvars[spot]], cints[spot]);
		}
		return ints[off];
	}
	public static void pause(int time) {
		int xx = mousex();
		int yy = mousey();
		long now = System.currentTimeMillis();
		while (System.currentTimeMillis() - now < time) {
			if (mousequit && (xx != mousex() || yy != mousey()))
				end("You moved the mouse.");
		}
	}
	public static void pause(int x, int y, int r, int g, int b, boolean endearly) {
		int xx = mousex();
		int yy = mousey();
		long now = System.currentTimeMillis();
		while (!colorat(x, y, r, g, b) && (!endearly || System.currentTimeMillis() - now < waittime)) {
			if (mousequit && (xx != mousex() || yy != mousey()))
				end("You moved the mouse.");
			if (quitafter >= 0 && System.currentTimeMillis() - now >= quitafter)
				end("Quitting a pause from script \"" + mainscript.name + "\" at line " + rows[pos] + ".");
		}
	}
	public static void pausenot(int x, int y, int r, int g, int b, boolean endearly) {
		int xx = mousex();
		int yy = mousey();
		long now = System.currentTimeMillis();
		while (colorat(x, y, r, g, b) && (!endearly || System.currentTimeMillis() - now < waittime)) {
			if (mousequit && (xx != mousex() || yy != mousey()))
				end("You moved the mouse.");
			if (quitafter >= 0 && System.currentTimeMillis() - now >= quitafter)
				end("Quitting a pausenot from script \"" + mainscript.name + "\" at line " + rows[pos] + ".");
		}
	}
	public static boolean colorat(int x, int y, int r, int g, int b) {
		int[] cs = auto.colorsat(x, y);
		return cs[0] <= r + variance && cs[0] >= r - variance && cs[1] <= g + variance && cs[1] >= g - variance && cs[2] <= b + variance && cs[2] >= b - variance;
	}
	public static void end(String s) {
		System.out.println(s);
		end();
	}
	public static void end() {
		if (endingcall != -1) {
			pos = endingcall;
			while (pos < scriptlist.action.length) {
				//printval
				if (scriptlist.action[pos] == 30) {
					if (scriptlist.val[pos][0].startsWith(".")) {
						if (scriptlist.val[pos][0].equals(".timestamp"))
							System.out.print(timestamp());
						else if (scriptlist.val[pos][0].equals(".mousex"))
							System.out.print("" + mousex());
						else if (scriptlist.val[pos][0].equals(".mousey"))
							System.out.print("" + mousey());
						else if (scriptlist.val[pos][0].equals(".scanx"))
							System.out.print("" + scanx);
						else if (scriptlist.val[pos][0].equals(".scany"))
							System.out.print("" + scany);
					} else {
						variable = mainvars[scriptlist.vval[pos][0]];
						if (variable != null)
							System.out.print("" + variable);
					}
				//println, print
				} else if (scriptlist.action[pos] == 31)
					System.out.print(scriptlist.val[pos][0]);
				pos = pos + 1;
			}
		}
		System.exit(0);
	}
}