import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.awt.MouseInfo;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Stack;
//TO ADD:
//?backwards compatibility?
public class run {
	public static final int CLEFT = 1;
	public static final int CRIGHT = 2;
	public static final int CMOVE = 3;
	public static final int CSCROLL = 4;
	public static final int CPAUSENOT = 5;
	public static final int CPAUSE = 6;
	public static final int CJUMPBACK = 7;
	public static final int CJUMP = 8;
	public static final int CCALL = 9;
	public static final int CRUN = 10;
	public static final int CSET = 11;
	public static final int CPLUSEQUALS = 12;
	public static final int CMINUSEQUALS = 13;
	public static final int CTIMESEQUALS = 14;
	public static final int CDIVIDEEQUALS = 15;
	public static final int CMODEQUALS = 16;
	public static final int CCREATE = 17;
	public static final int CSETARGTO = 18;
	public static final int CIF = 19;
	public static final int CFOR = 20;
	public static final int CEND = 21;
	public static final int CBREAK = 22;
	public static final int CRETURN = 23;
	public static final int CQUIT = 24;
	public static final int CTYPE = 25;
	public static final int CPRESS = 26;
	public static final int CRELEASE = 27;
	public static final int COPTIONON = 28;
	public static final int COPTIONOFF = 29;
	public static final int CPRINTVAL = 30;
	public static final int CPRINTLN = 31;
	public static final int CPRINT = 31;
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
			if (digit >= 0 && digit < 10)
				number = number * 10 + digit;
			pos = pos + 1;
		}
		if (negative)
			return -number;
		return number;
	}
	public static Autoer auto = new Autoer();
	public static Script scriptlist = null;
	public static Varslist variable = null;
	public static run mainrun = null;
	public Script mainscript = null;
	public String[] input = null;
	public Varslist[] vars = null;
	public int rvariance = 5;
	public int gvariance = 5;
	public int bvariance = 5;
	public int quitafter = -1;
	public int endingcall = -1;
	public VarsInt jumpback = null;
	public int mousetoly = 0;
	public int mousetolx = 0;
	public int scanx = 0;
	public int scany = 0;
	public int[] rows = null;
	public int pos = 0;
	public int[] scanorder = new int[] {3, 4};
	public int offx = 0;
	public int offy = 0;
	public boolean returnval = false;
	public boolean endmessages = true;
	public static class Varslist {
		public static final int INT = 1;
		public static final int BOOLEAN = 2;
		public static final int COLOR = 3;
		public static final int TIMER = 4;
		public static final int SPECIAL = 100;
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
		public void setto(String[] args, int[] cptrs) {
		}
		public String toString() {
			return name;
		}
		public Varslist clone() {
			return new Varslist(name, next);
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
		public void setto(String[] args, int[] cptrs) {
			val = mainrun.evaluate(args, cptrs, 1);
		}
		public String toString() {
			return String.valueOf(val);
		}
		public VarsInt clone() {
			return new VarsInt(name, val, next);
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
		public void setto(String[] args, int[] cptrs) {
			val = mainrun.evaluate(args, cptrs, 1) != 0;
		}
		public String toString() {
			return String.valueOf(val);
		}
		public VarsBoolean clone() {
			return new VarsBoolean(name, val, next);
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
		public void setto(String[] args, int[] cptrs) {
			Varslist[] vars = mainrun.vars;
			variable = vars[cptrs[1]];
			if (variable.istype(Varslist.COLOR)) {
				VarsColor vc2 = (VarsColor)(variable);
				val = vc2.val;
			} else if (args[1].equals("at"))
				val = auto.colorsat(vars[cptrs[2]].ival() + mainrun.offx, vars[cptrs[3]].ival() + mainrun.offy);
			else
				val = new int[] {vars[cptrs[1]].ival(), vars[cptrs[2]].ival(), vars[cptrs[3]].ival()};
		}
		public String toString() {
			return val[0] + ", " + val[1] + ", " + val[2];
		}
		public VarsColor clone() {
			return new VarsColor(name, val, next);
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
		public void setto(String[] args, int[] cptrs) {
			val = mainrun.vars[cptrs[1]].ival() + System.currentTimeMillis();
		}
		public String toString() {
			return String.valueOf(System.currentTimeMillis() - val);
		}
		public VarsTimer clone() {
			return new VarsTimer(name, val, next);
		}
	}
	public static class VarsSpecial extends Varslist {
		public int val;
		public VarsSpecial(String n) {
			super(n, null);
			if (n.equals(".mousex"))
				val = 1;
			else if (n.equals(".mousey"))
				val = 2;
			else if (n.equals(".scanx"))
				val = 3;
			else if (n.equals(".scany"))
				val = 4;
			else if (n.equals(".timestamp"))
				val = 5;
		}
		public boolean istype(int t) {
			return t == SPECIAL;
		}
		public int ival() {
			if (val == 1)
				return mousex() - mainrun.offx;
			else if (val == 2)
				return mousey() - mainrun.offy;
			else if (val == 3)
				return mainrun.scanx;
			else if (val == 4)
				return mainrun.scany;
			else if (val == 5)
				return (int)(System.currentTimeMillis());
			return 0;
		}
		public String toString() {
			if (val == 1)
				return String.valueOf(mousex() - mainrun.offx);
			else if (val == 2)
				return String.valueOf(mousey() - mainrun.offy);
			else if (val == 3)
				return String.valueOf(mainrun.scanx);
			else if (val == 4)
				return String.valueOf(mainrun.scany);
			else if (val == 5)
				return timestamp();
			return name;
		}
		public VarsSpecial clone() {
			return new VarsSpecial(name);
		}
	}
	public static class Script {
		public String name;
		public String[][] val;
		public int[][] pval;
		public int[] action;
		public int[] row;
		public int varcount = 0;
		public Script next = null;
		public Varslist[] startvals = null;
		public Script(String n, int lines) {
			name = n;
			val = new String[lines][0];
			pval = new int[lines][0];
			action = new int[lines];
			row = new int[lines];
		}
		//get rid of all lines of a specific action
		public int purgelines(int purge) {
			int length = action.length;
			for (int spot = 0; spot < action.length; spot += 1) {
				if (action[spot] == purge)
					length -= 1;
			}
			String[][] newval = new String[length][0];
			int[][] newpval = new int[length][0];
			int[] newaction = new int[length];
			int[] newrow = new int[length];
			int index = 0;
			for (int spot = 0; spot < action.length; spot += 1) {
				if (action[spot] != purge) {
					newval[index] = val[spot];
					newpval[index] = pval[spot];
					newaction[index] = action[spot];
					newrow[index] = row[spot];
					index += 1;
				}
			}
			val = newval;
			pval = newpval;
			action = newaction;
			row = newrow;
			return length;
		}
	}
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("You need a script to run.");
			return;
		}
		Filer filer = new Filer("scripts/" + args[0] + ".txt");
		if (!filer.fileisthere()) {
			System.out.println("Script \"" + args[0] + "\" does not exist.");
			return;
		}
		filer.readfile();
		String[] lines = filer.getlines();
		scriptlist = new Script(args[0], lines.length);
		//quit if there are errors
		if (verify(lines, scriptlist)) {
			try {
				run runner = new run();
				runner.runscript(scriptlist, args);
			} catch(Exception e) {
				System.out.println("Error: script \"" + mainrun.mainscript.name + "\" at line " + mainrun.rows[mainrun.pos] + " caused this exception:");
				e.printStackTrace();
			}
		}
	}
	//format the script and make sure it's valid
	public static boolean verify(String[] lines, Script putter) {
		boolean valid = true;
		int linecount = lines.length;
		//clip all the lines
		for (int row = 0; row < linecount; row += 1) {
			lines[row] = clip(lines[row]);
		}
		//assign all the lines an action, check for errors
		for (int row = 0; row < linecount; row += 1) {
			//make sure the line was validly built
			valid = build(putter, lines, row) && valid;
			//verify other scripts used
			if (putter.action[row] == CRUN && putter.val[row].length > 0 && scriptcalled(putter.val[row][0], scriptlist) == null) {
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
		linecount = putter.purgelines(0);
		int action = 0;
		//assign the values to commands that need to know the line number
		for (int row = 0; row < linecount; row += 1) {
			action = putter.action[row];
			//set the jumpline for jump & call, and for endingcall labels
			if (action == CJUMP || action == CCALL || (action == COPTIONON && putter.val[row][0].equals("endingcall"))) {
				int index = 0;
				if (action == COPTIONON)
					index = 1;
				String label = ":" + putter.val[row][index];
				int jumpline = 0;
				while (jumpline < linecount && (putter.val[jumpline].length < 1 || !putter.val[jumpline][0].equals(label)))
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
					putter.pval[row][index] = newjumpline;
				}
			//find the end command for the for line, rearrange the values
			} else if (action == CFOR) {
				int jumpline = row;
				int layer = 1;
				while (jumpline + 1 < linecount && layer > 0) {
					jumpline += 1;
					if (putter.action[jumpline] == CFOR)
						layer = layer + 1;
					else if (putter.action[jumpline] == CEND)
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
					putter.pval[jumpline] = new int[] {newrow, putter.pval[row][0], putter.pval[row][2], putter.pval[row][3]};
					putter.val[row] = new String[] {putter.val[row][0], putter.val[row][1]};
					putter.pval[row] = new int[] {putter.pval[row][0], putter.pval[row][1]};
				}
			//make sure end commands have already been found
			} else if (action == CEND && putter.pval[row].length < 3) {
				System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": end missing a for command");
				valid = false;
			//set the if command's next line
			} else if (action == CIF) {
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
					putter.pval[row][0] = newjumpline;
				}
			//find the end command for the break
			} else if (action == CBREAK) {
				int jumpline = row;
				int layer = 1;
				while (jumpline + 1 < linecount && layer > 0) {
					jumpline += 1;
					if (putter.action[jumpline] == CFOR)
						layer = layer + 1;
					else if (putter.action[jumpline] == CEND)
						layer = layer - 1;
				}
				if (layer > 0) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": break missing an end command");
					valid = false;
				} else {
					int newjumpline = jumpline;
					for (int line = 0; line < jumpline; line += 1) {
						if (putter.action[line] < 1)
							newjumpline -= 1;
					}
					putter.pval[row][0] = newjumpline;
				}
			}
		}
		//get rid of labels and brackets
		linecount = putter.purgelines(-1);
		//find all possible variable names
		String name = "";
		TreeMap<String, Integer> tm = new TreeMap<String, Integer>();
		for (int row = 0; row < linecount; row += 1) {
			action = putter.action[row];
			int i = -1;
			switch(action) {
				case CLEFT:
				case CRIGHT:
				case CMOVE:
				case CSCROLL:
				case CPAUSENOT:
				case CPAUSE:
				case CSET:
				case CPLUSEQUALS:
				case CMINUSEQUALS:
				case CTIMESEQUALS:
				case CDIVIDEEQUALS:
				case CMODEQUALS:
				case CPRINTVAL:
				case CFOR:
					i = 0;
					break;
				case CSETARGTO:
				case CIF:
				case CEND:
				case COPTIONON:
				case CCREATE:
					i = 1;
					break;
			}
			switch(action) {
				case CCREATE:
				case CFOR:
				case CSET:
				case CSETARGTO:
				case CPLUSEQUALS:
				case CMINUSEQUALS:
				case CTIMESEQUALS:
				case CDIVIDEEQUALS:
				case CMODEQUALS:
					name = putter.val[row][i];
					if (name.startsWith(" ") || name.equals("true") || name.equals("false") || name.startsWith(".")) {
						System.out.println("Bad syntax in " + putter.name + " on line " + putter.row[row] + ": that variable is reserved");
						valid = false;
					}
					break;
			}
			//new variable storage
			if (i != -1) {
				for (; i < putter.val[row].length; i += 1) {
					name = putter.val[row][i];
					if (!name.startsWith(" ") && !tm.containsKey(name)) {
						tm.put(name, putter.varcount);
						putter.varcount += 1;
					}
				}
			}
		}
		//stop if invalid
		if (!valid)
			return false;
		putter.startvals = new Varslist[putter.varcount];
		Stack<Integer> forvars = new Stack<Integer>();
		//link all values with their variable name if they have one
		for (int row = 0; row < linecount; row += 1) {
			action = putter.action[row];
			//push or pop a for variable
			if (putter.action[row] == CFOR)
				forvars.push(putter.pval[row][0]);
			else if (putter.action[row] == CEND)
				forvars.pop();
			//put the variable in break
			else if (putter.action[row] == CBREAK)
				putter.pval[row][1] = forvars.peek();
			int col = -1;
			switch(action) {
				case CLEFT:
				case CRIGHT:
				case CMOVE:
				case CSCROLL:
				case CPAUSENOT:
				case CPAUSE:
				case CSET:
				case CPLUSEQUALS:
				case CMINUSEQUALS:
				case CTIMESEQUALS:
				case CDIVIDEEQUALS:
				case CMODEQUALS:
				case CPRINTVAL:
				case CFOR:
					col = 0;
					break;
				case CSETARGTO:
				case CIF:
				case CEND:
				case CCREATE:
					col = 1;
					break;
				case COPTIONON:
					if (!putter.val[row][0].equals("endingcall"))
						col = 1;
					break;
			}
			if (col != -1) {
				for (; col < putter.pval[row].length; col += 1) {
					name = putter.val[row][col];
					if (!name.startsWith(" ")) {
						int ptr = tm.get(name);
						if (name.startsWith("."))
							putter.startvals[ptr] = new VarsSpecial(name);
						else
							putter.startvals[ptr] = new VarsInt(name, putter.pval[row][col], null);
						putter.pval[row][col] = ptr;
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
			s.action[row] = CLEFT;
			s.val[row] = split(line.substring(4, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("right")) {
			s.action[row] = CRIGHT;
			s.val[row] = split(line.substring(5, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("move")) {
			s.action[row] = CMOVE;
			s.val[row] = split(line.substring(4, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("scroll")) {
			s.action[row] = CSCROLL;
			s.val[row] = split(line.substring(6, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("pausenot")) {
			s.action[row] = CPAUSENOT;
			s.val[row] = split(line.substring(8, line.length()), true);
			argnum = 3;
		} else if (line.startsWith("pause")) {
			s.action[row] = CPAUSE;
			s.val[row] = split(line.substring(5, line.length()), true);
		} else if (line.startsWith("jumpback")) {
			s.action[row] = CJUMPBACK;
			s.val[row] = new String[] {""};
		} else if (line.startsWith("jump")) {
			s.action[row] = CJUMP;
			s.val[row] = split(line.substring(4, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("call")) {
			s.action[row] = CCALL;
			s.val[row] = split(line.substring(4, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("run")) {
			s.action[row] = CRUN;
			s.val[row] = split(line.substring(3, line.length()), true);
			argnum = 1;
		//setargto has to come before set, even though its num is higher
		} else if (line.startsWith("setargto")) {
			s.action[row] = CSETARGTO;
			s.val[row] = split(line.substring(8, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("set")) {
			s.action[row] = CSET;
			s.val[row] = split(line.substring(3, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("+=")) {
			s.action[row] = CPLUSEQUALS;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("-=")) {
			s.action[row] = CMINUSEQUALS;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("*=")) {
			s.action[row] = CTIMESEQUALS;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("/=")) {
			s.action[row] = CDIVIDEEQUALS;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("%=")) {
			s.action[row] = CMODEQUALS;
			s.val[row] = split(line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("create")) {
			s.action[row] = CCREATE;
			s.val[row] = split(line.substring(6, line.length()), true);
			argnum = 3;
		} else if (line.startsWith("if")) {
			s.action[row] = CIF;
			s.val[row] = split(". " + line.substring(2, line.length()), true);
			argnum = 2;
		} else if (line.startsWith("for")) {
			s.action[row] = CFOR;
			s.val[row] = split(line.substring(3, line.length()), true);
			argnum = 4;
		} else if (line.startsWith("end")) {
			s.action[row] = CEND;
			s.val[row] = new String[] {""};
		} else if (line.startsWith("break")) {
			s.action[row] = CBREAK;
			s.val[row] = new String[] {"", ""};
		} else if (line.startsWith("return")) {
			s.action[row] = CRETURN;
			s.val[row] = new String[] {""};
		} else if (line.startsWith("quit")) {
			s.action[row] = CQUIT;
			s.val[row] = new String[] {""};
		} else if (line.startsWith("type")) {
			s.action[row] = CTYPE;
			int begin = 4;
			while (begin < line.length() && line.charAt(begin) != '.')
				begin += 1;
			s.val[row] = new String[] {line.substring(begin, line.length())};
		} else if (line.startsWith("press")) {
			s.action[row] = CPRESS;
			s.val[row] = split(line.substring(5, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("release")) {
			s.action[row] = CRELEASE;
			s.val[row] = split(line.substring(7, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("optionon")) {
			s.action[row] = COPTIONON;
			s.val[row] = split(line.substring(8, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("optionoff")) {
			s.action[row] = COPTIONOFF;
			s.val[row] = split(line.substring(9, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("printval")) {
			s.action[row] = CPRINTVAL;
			s.val[row] = split(line.substring(8, line.length()), true);
			argnum = 1;
		} else if (line.startsWith("println")) {
			s.action[row] = CPRINTLN;
			int begin = 7;
			while (begin < line.length() && line.charAt(begin - 1) != '.')
				begin += 1;
			s.val[row] = new String[] {line.substring(begin, line.length()) + "\n"};
		} else if (line.startsWith("print")) {
			s.action[row] = CPRINT;
			int begin = 5;
			while (begin < line.length() && line.charAt(begin - 1) != '.')
				begin += 1;
			s.val[row] = new String[] {line.substring(begin, line.length())};
		} else if (line.startsWith(":")) {
			s.action[row] = -1;
			s.val[row] = split(line, true);
		} else if (line.startsWith("[") || line.startsWith("(") || line.startsWith("{")) {
			s.action[row] = -1;
			s.val[row] = new String[] {"["};
		} else if (line.startsWith("]") || line.startsWith(")") || line.startsWith("}")) {
			s.action[row] = -1;
			s.val[row] = new String[] {"]"};
		}
		//give string values an int value
		s.pval[row] = ibuild(s.val[row]);
		s.row[row] = row + 1;
		if (argnum > s.val[row].length) {
			if (s.action[row] == CIF)
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
			while (index < length && (theline.charAt(index) == '\t' || theline.charAt(index) == ' '))
				index = index + 1;
			while (index < length) {
				begin = index;
				while (index < length && theline.charAt(index) != '\t' && theline.charAt(index) != ' ')
					index = index + 1;
				vl = new Varslist(theline.substring(begin, index), vl);
				count = count + 1;
				while (index < length && (theline.charAt(index) == '\t' || theline.charAt(index) == ' '))
					index = index + 1;
			}
		} else {
			while (index < length && theline.charAt(index) == ',')
				index = index + 1;
			while (index < length) {
				begin = index;
				while (index < length && theline.charAt(index) != ',')
					index = index + 1;
				vl = new Varslist(theline.substring(begin, index), vl);
				count = count + 1;
				while (index < length && theline.charAt(index) == ',')
					index = index + 1;
			}
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
		int op;
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
			else if (val[spot].equals("redpart"))
				op = 16;
			else if (val[spot].equals("greenpart"))
				op = 17;
			else if (val[spot].equals("bluepart"))
				op = 18;
			else if (val[spot].equals("input"))
				op = 19;
			else if (val[spot].equals("colorat"))
				op = 20;
			else if (val[spot].equals("scan"))
				op = 21;
			else
				op = -1;
			if (op != -1) {
				ival[spot] = op;
				val[spot] = " " + val[spot];
			} else if (val[spot].equals("true"))
				ival[spot] = 1;
			else
				ival[spot] = readint(val[spot]);
		}
		return ival;
	}
	//find the script with the right name
	public static Script scriptcalled(String s, Script sc) {
		while (sc != null) {
			if (sc.name.equals(s))
				return sc;
			sc = sc.next;
		}
		return null;
	}
	//run the script
	public boolean runscript(Script s, String[] theinput) {
		input = theinput;
		mainrun = this;
		mainscript = s;
		//extract all the data
		String[][] lines = s.val;
		int[][] cplist = s.pval;
		int[] actions = s.action;
		rows = s.row;
		//build the vars
		vars = new Varslist[s.varcount];
		for (int i = 0; i < s.startvals.length; i += 1) {
			vars[i] = s.startvals[i].clone();
		}
/*
System.out.println("Vars:");
for (int i = 0; i < vars.length; i += 1) {
System.out.println(vars[i].name + ": " + vars[i].ival());
}
*/
		//prepare to extract the data even further
		String[] args = null;
		int[] cptrs = null;
		int action = 0;
		//parse the script
		int linecount = lines.length;
		pos = 0;
		while (pos < linecount) {
			args = lines[pos];
			cptrs = cplist[pos];
			action = actions[pos];
/*
System.out.print(s.name + " @ L" + rows[pos] + "- A" + action + ": ");
for (int i = 0; i < args.length; i += 1) {
System.out.print(args[i] + "(" + cptrs[i] + "), ");
//System.out.print(args[i] + ", ");
}
System.out.println();
*/
			if (action <= CPAUSE) {
				//left
				if (action == CLEFT) {
					if (args.length > 1)
						auto.leftEnsure(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy);
					else
						auto.left(vars[cptrs[0]].ival());
				//right
				} else if (action == CRIGHT) {
					if (args.length > 1)
						auto.rightEnsure(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy);
					else
						auto.right(vars[cptrs[0]].ival());
				//move
				} else if (action == CMOVE)
					auto.moveEnsure(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy);
				//scroll
				else if (action == CSCROLL) {
					if (args.length < 2)
						auto.scroll(args[0]);
					else
						auto.scroll(args[0], vars[cptrs[1]].ival());
				//pausenot
				} else if (action == CPAUSENOT) {
					variable = vars[cptrs[2]];
					if (variable.istype(Varslist.COLOR)) {
						VarsColor vc = (VarsColor)(variable);
						if (cptrs.length < 4)
							pausenot(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy, vc.val[0], vc.val[1], vc.val[2], -1);
						else
							pausenot(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy, vc.val[0], vc.val[1], vc.val[2], vars[cptrs[3]].ival());
					} else {
						if (cptrs.length < 6)
							pausenot(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy, vars[cptrs[2]].ival(), vars[cptrs[3]].ival(), vars[cptrs[4]].ival(), -1);
						else
							pausenot(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy, vars[cptrs[2]].ival(), vars[cptrs[3]].ival(), vars[cptrs[4]].ival(), vars[cptrs[5]].ival());
					}
				//pause
				} else {
					if (cptrs.length < 1)
						pause();
					else if (cptrs.length < 3)
						pause(vars[cptrs[0]].ival());
					else {
						variable = vars[cptrs[2]];
						if (variable.istype(Varslist.COLOR)) {
							VarsColor vc = (VarsColor)(variable);
							if (cptrs.length < 4)
								pause(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy, vc.val[0], vc.val[1], vc.val[2], -1);
							else
								pause(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy, vc.val[0], vc.val[1], vc.val[2], vars[cptrs[3]].ival());
						} else {
							if (cptrs.length < 6)
								pause(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy, vars[cptrs[2]].ival(), vars[cptrs[3]].ival(), vars[cptrs[4]].ival(), -1);
							else
								pause(vars[cptrs[0]].ival() + offx, vars[cptrs[1]].ival() + offy, vars[cptrs[2]].ival(), vars[cptrs[3]].ival(), vars[cptrs[4]].ival(), vars[cptrs[5]].ival());
						}
					}
				}
			} else if (action <= CRUN) {
				//jumpback
				if (action == CJUMPBACK) {
					if (jumpback != null) {
						pos = jumpback.val;
						jumpback = (VarsInt)(jumpback.next);
					}
				//jump
				} else if (action == CJUMP)
					pos = cptrs[0];
				//call
				else if (action == CCALL) {
					jumpback = new VarsInt("", pos, jumpback);
					pos = cptrs[0];
				//run
				} else {
					run runner = new run();
					if (runner.runscript(scriptcalled(args[0], scriptlist), args))
						end("Called from script \"" + mainscript.name + "\" at line " + rows[pos] + ".");
					mainrun = this;
				}
			} else if (action <= CSETARGTO) {
				//set
				if (action == CSET)
					vars[cptrs[0]].setto(args, cptrs);
				//+=, -=, *=, /=, %=
				else if (action <= CMODEQUALS) {
					variable = vars[cptrs[0]];
					VarsInt vi = (VarsInt)(variable);
					if (action == CPLUSEQUALS)
						vi.val += vars[cptrs[1]].ival();
					else if (action == CMINUSEQUALS)
						vi.val -= vars[cptrs[1]].ival();
					else if (action == CTIMESEQUALS)
						vi.val *= vars[cptrs[1]].ival();
					else if (action == CDIVIDEEQUALS)
						vi.val /= vars[cptrs[1]].ival();
					else
						vi.val %= vars[cptrs[1]].ival();
				//create
				} else if (action == CCREATE) {
					if (args[0].equals("int"))
						vars[cptrs[1]] = new VarsInt(args[1], evaluate(args, cptrs, 2), vars[cptrs[1]]);
					else if (args[0].equals("boolean"))
						vars[cptrs[1]] = new VarsBoolean(args[1], evaluate(args, cptrs, 2) != 0, vars[cptrs[1]]);
					else if (args[0].equals("color")) {
						variable = vars[cptrs[2]];
						if (variable.istype(Varslist.COLOR)) {
							VarsColor vc = (VarsColor)(variable);
							vars[cptrs[1]] = new VarsColor(args[1], vc.val, vars[cptrs[1]]);
						} else if (args[2].equals("at"))
							vars[cptrs[1]] = new VarsColor(args[1], auto.colorsat(vars[cptrs[3]].ival() + offx, vars[cptrs[4]].ival() + offy), vars[cptrs[1]]);
						else
							vars[cptrs[1]] = new VarsColor(args[1], new int[] {vars[cptrs[2]].ival(), vars[cptrs[3]].ival(), vars[cptrs[4]].ival()}, vars[cptrs[1]]);
					} else if (args[0].equals("timer"))
						vars[cptrs[1]] = new VarsTimer(args[1], vars[cptrs[2]].ival(), vars[cptrs[1]]);
				//setargto
				} else {
					variable = vars[cptrs[1]];
					for (int spot = 1; spot < input.length; spot += 1) {
						if (input[spot].startsWith(args[0])) {
							args = split(" ," + input[spot].substring(args[0].length(), input[spot].length()), false);
							Varslist[] oldvars = vars;
							int[] vals = ibuild(args);
							vars = new Varslist[args.length];
							for (int i = 0; i < vars.length; i += 1) {
								vars[i] = new VarsInt(args[i], vals[i], null);
								if (!args[i].startsWith(" "))
									vals[i] = i;
							}
							variable.setto(args, vals);
							vars = oldvars;
							break;
						}
					}
				}
			} else if (action <= CQUIT) {
				//if
				if (action == CIF) {
					if (evaluate(args, cptrs, 1) == 0)
						pos = cptrs[0];
				//for
				} else if (action == CFOR)
					vars[cptrs[0]] = new VarsInt(args[0], vars[cptrs[1]].ival(), vars[cptrs[0]]);
				//end
				else if (action == CEND) {
					VarsInt vi = (VarsInt)(vars[cptrs[1]]);
					int add = vars[cptrs[3]].ival();
					vi.val += add;
					if ((add >= 0 && vi.val > vars[cptrs[2]].ival()) || (add < 0 && vi.val < vars[cptrs[2]].ival()))
						vars[cptrs[1]] = vi.next;
					else
						pos = cptrs[0];
				//break
				} else if (action == CBREAK) {
					pos = cptrs[0];
					vars[cptrs[1]] = vars[cptrs[1]].next;
				//return
				} else if (action == CRETURN)
					return returnval;
				//quit
				else
					end("Quitting script \"" + mainscript.name + "\" from line " + rows[pos] + ".");
			} else {
				//type
				if (action == CTYPE)
					auto.type(args[0]);
				//press
				else if (action == CPRESS) {
					if (args[0].length() > 1)
						auto.button(args[0], 1);
					else
						auto.button(args[0].charAt(0), 1);
				//release
				} else if (action == CRELEASE) {
					if (args[0].length() > 1)
						auto.button(args[0], 3);
					else
						auto.button(args[0].charAt(0), 3);
				//optionon
				} else if (action == COPTIONON) {
					if (args[0].equals("quitafter"))
						quitafter = vars[cptrs[1]].ival();
					else if (args[0].equals("endingcall"))
						endingcall = cptrs[1];
					else if (args[0].equals("variance")) {
						if (cptrs.length < 4) {
							rvariance = vars[cptrs[1]].ival();
							gvariance = vars[cptrs[1]].ival();
							bvariance = vars[cptrs[1]].ival();
						} else {
							rvariance = vars[cptrs[1]].ival();
							gvariance = vars[cptrs[2]].ival();
							bvariance = vars[cptrs[3]].ival();
						}
					} else if (args[0].equals("mousetolerance")) {
						if (cptrs.length < 3) {
							mousetolx = vars[cptrs[1]].ival();
							mousetoly = vars[cptrs[1]].ival();
						} else {
							mousetolx = vars[cptrs[1]].ival();
							mousetoly = vars[cptrs[2]].ival();
						}
					} else if (args[0].equals("scanorder")) {
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
						offx = vars[cptrs[1]].ival();
						offy = vars[cptrs[2]].ival();
					} else if (args[0].equals("endmessages"))
						endmessages = true;
				//optionoff
				} else if (action == COPTIONOFF) {
					if (args[0].equals("quitafter"))
						quitafter = -1;
					else if (args[0].equals("endingcall"))
						endingcall = -1;
					else if (args[0].equals("variance")) {
						rvariance = 0;
						gvariance = 0;
						bvariance = 0;
					} else if (args[0].equals("mousetolerance")) {
						mousetolx = 0;
						mousetoly = 0;
					} else if (args[0].equals("scanorder")) {
						scanorder[0] = 3;
						scanorder[1] = 4;
					} else if (args[0].equals("offset")) {
						offx = 0;
						offy = 0;
					} else if (args[0].equals("endmessages"))
						endmessages = false;
				//printval
				} else if (action == CPRINTVAL)
					System.out.print(vars[cptrs[0]].toString());
				//println, print
				else
					System.out.print(args[0]);
			}
			pos = pos + 1;
		}
		return returnval;
	}
	//evaluate the arithmetic/boolean equation
	public int evaluate(String[] vals, int[] cptrs, int off) {
		int length = vals.length;
		int[] ints = new int[length];
		int stack = length;
		for (int spot = length - 1; spot >= off; spot -= 1) {
			if (vals[spot].startsWith(" ")) {
				if (cptrs[spot] < 7) {
					// +
					if (cptrs[spot] == 1) {
						stack += 1;
						ints[stack] = ints[stack - 1] + ints[stack];
					// -
					} else if (cptrs[spot] == 2) {
						stack += 1;
						ints[stack] = ints[stack - 1] - ints[stack];
					// *
					} else if (cptrs[spot] == 3) {
						stack += 1;
						ints[stack] = ints[stack - 1] * ints[stack];
					// /
					} else if (cptrs[spot] == 4) {
						stack += 1;
						ints[stack] = ints[stack - 1] / ints[stack];
					// %
					} else if (cptrs[spot] == 5) {
						stack += 1;
						ints[stack] = ints[stack - 1] % ints[stack];
					// random
					} else if (cptrs[spot] == 6)
						ints[stack] = (int)(Math.random() * ints[stack]);
				} else if (cptrs[spot] < 13) {
					// ==
					if (cptrs[spot] == 7) {
						stack += 1;
						if (ints[stack - 1] == ints[stack])
							ints[stack] = 1;
						else
							ints[stack] = 0;
					// !=
					} else if (cptrs[spot] == 8) {
						stack += 1;
						if (ints[stack - 1] != ints[stack])
							ints[stack] = 1;
						else
							ints[stack] = 0;
					// >=
					} else if (cptrs[spot] == 9) {
						stack += 1;
						if (ints[stack - 1] >= ints[stack])
							ints[stack] = 1;
						else
							ints[stack] = 0;
					// >
					} else if (cptrs[spot] == 10) {
						stack += 1;
						if (ints[stack - 1] > ints[stack])
							ints[stack] = 1;
						else
							ints[stack] = 0;
					// <=
					} else if (cptrs[spot] == 11) {
						stack += 1;
						if (ints[stack - 1] <= ints[stack])
							ints[stack] = 1;
						else
							ints[stack] = 0;
					// <
					} else if (cptrs[spot] == 12) {
						stack += 1;
						if (ints[stack - 1] < ints[stack])
							ints[stack] = 1;
						else
							ints[stack] = 0;
					}
				} else if (cptrs[spot] < 16) {
					// not
					if (cptrs[spot] == 13) {
						if (ints[stack] == 0)
							ints[stack] = 1;
						else
							ints[stack] = 0;
					// or
					} else if (cptrs[spot] == 14) {
						stack += 1;
						if ((ints[stack - 1] | ints[stack]) != 0)
							ints[stack] = 1;
						else
							ints[stack] = 0;
					// and
					} else if (cptrs[spot] == 15) {
						stack += 1;
						if ((ints[stack - 1] & ints[stack]) != 0)
							ints[stack] = 1;
					}
				} else {
					// redpart
					if (cptrs[spot] == 16) {
						VarsColor vc = (VarsColor)(vars[cptrs[spot + 1]]);
						ints[stack] = vc.val[0];
					// greenpart
					} else if (cptrs[spot] == 17) {
						VarsColor vc = (VarsColor)(vars[cptrs[spot + 1]]);
						ints[stack] = vc.val[1];
					// bluepart
					} else if (cptrs[spot] == 18) {
						VarsColor vc = (VarsColor)(vars[cptrs[spot + 1]]);
						ints[stack] = vc.val[2];
					// input
					} else if (cptrs[spot] == 19) {
						ints[stack] = 0;
						for (int loc = 1; loc < input.length; loc += 1) {
							if (input[loc].equals(vals[spot + 1])) {
								ints[stack] = 1;
								break;
							}
						}
					// colorat
					} else if (cptrs[spot] == 20) {
						variable = vars[cptrs[spot + 3]];
						if (variable.istype(Varslist.COLOR)) {
							stack += 2;
							VarsColor vc = (VarsColor)(variable);
							if (colorat(ints[stack - 2] + offx, ints[stack - 1] + offy, vc.val[0], vc.val[1], vc.val[2]))
								ints[stack] = 1;
							else
								ints[stack] = 0;
						} else {
							stack += 4;
							if (colorat(ints[stack - 4] + offx, ints[stack - 3] + offy, ints[stack - 2], ints[stack - 1], ints[stack]))
								ints[stack] = 1;
							else
								ints[stack] = 0;
						}
					// scan
					} else if (cptrs[spot] == 21) {
						scanx = -1;
						scany = -1;
						BufferedImage image = auto.screenshot(ints[stack] + offx, ints[stack + 1] + offy, ints[stack + 2], ints[stack + 3]);
						int pixel = 0;
						int r = 0;
						int g = 0;
						int b = 0;
						int ir = 0;
						int ig = 0;
						int ib = 0;
						int oldstack = stack;
						variable = vars[cptrs[stack + 4]];
						if (variable.istype(Varslist.COLOR)) {
							stack += 4;
							VarsColor vc = (VarsColor)(variable);
							ir = vc.val[0];
							ig = vc.val[1];
							ib = vc.val[2];
						} else {
							stack += 6;
							ir = ints[stack - 2];
							ig = ints[stack - 1];
							ib = ints[stack];
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
						ints[stack] = 0;
						outer: for (int m = 0; m < max1; m += 1) {
							for (int n = 0; n < max0; n += 1) {
								pixel = image.getRGB(coords[getx], coords[gety]);
								r = (pixel >> 16) & 255;
								g = (pixel >> 8) & 255;
								b = pixel & 255;
								if (r <= ir + rvariance && r >= ir - rvariance && g <= ig + gvariance && g >= ig - gvariance && b <= ib + bvariance && b >= ib - bvariance) {
									scanx = ints[oldstack] + coords[getx];
									scany = ints[oldstack + 1] + coords[gety];
									ints[stack] = 1;
									break outer;
								}
								coords[0] += add0;
							}
							coords[1] += add1;
							coords[0] = loc0;
						}
					}
				}
			} else {
				stack -= 1;
				ints[stack] = vars[cptrs[spot]].ival();
			}
		}
		return ints[stack];
	}
	public void pause() {
		Scanner scan = new Scanner(System.in);
		String temp = scan.nextLine();
	}
	public void pause(int time) {
		int xx = mousex();
		int yy = mousey();
		long now = System.currentTimeMillis();
		while (System.currentTimeMillis() - now < time) {
			if (!mousewithin(xx, yy)) {
				end("You moved the mouse at a pause on line " + rows[pos] + " in script \"" + mainscript.name + "\"");
				return;
			}
		}
	}
	public boolean mousewithin(int xx, int yy) {
		int mx = mousex();
		int my = mousey();
		return mx >= xx - mousetolx && mx <= xx + mousetolx && my >= yy - mousetoly && my <= yy + mousetoly;
	}
	public void pause(int x, int y, int r, int g, int b, int waittime) {
		int xx = mousex();
		int yy = mousey();
		long now = System.currentTimeMillis();
		while (!colorat(x, y, r, g, b) && (waittime < 0 || System.currentTimeMillis() - now < waittime)) {
			if (!mousewithin(xx, yy)) {
				end("You moved the mouse at a pause on line " + rows[pos] + " in script \"" + mainscript.name + "\"");
				return;
			}
			if (quitafter >= 0 && System.currentTimeMillis() - now >= quitafter) {
				end("Quitting a pause from script \"" + mainscript.name + "\" at line " + rows[pos]);
				return;
			}
		}
	}
	public void pausenot(int x, int y, int r, int g, int b, int waittime) {
		int xx = mousex();
		int yy = mousey();
		long now = System.currentTimeMillis();
		while (colorat(x, y, r, g, b) && (waittime < 0 || System.currentTimeMillis() - now < waittime)) {
			if (!mousewithin(xx, yy)) {
				end("You moved the mouse at a pausenot on line " + rows[pos] + " in script \"" + mainscript.name + "\"");
				return;
			}
			if (quitafter >= 0 && System.currentTimeMillis() - now >= quitafter) {
				end("Quitting a pausenot from script \"" + mainscript.name + "\" at line " + rows[pos]);
				return;
			}
		}
	}
	public boolean colorat(int x, int y, int r, int g, int b) {
		int[] cs = auto.colorsat(x, y);
		return cs[0] <= r + rvariance && cs[0] >= r - rvariance && cs[1] <= g + gvariance && cs[1] >= g - gvariance && cs[2] <= b + bvariance && cs[2] >= b - bvariance;
	}
	public void end(String s) {
		if (endmessages)
			System.out.println(s);
		if (endingcall != -1) {
			pos = endingcall;
			endingcall = -1;
		} else
			pos = mainscript.val.length;
		returnval = true;
	}
}