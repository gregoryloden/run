import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.awt.MouseInfo;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Stack;
import java.awt.Toolkit;
import java.io.File;
import javax.imageio.ImageIO;
//TO ADD:
public class run {
	public static final int CLEFT = 1;
	public static final int CRIGHT = 2;
	public static final int CMIDDLE = 3;
	public static final int CMOVE = 4;
	public static final int CSCROLL = 5;
	public static final int CPAUSENOT = 6;
	public static final int CPAUSE = 7;
	public static final int CJUMPBACK = 8;
	public static final int CJUMP = 9;
	public static final int CCALL = 10;
	public static final int CRUN = 11;
	public static final int CSET = 12;
	public static final int CPLUSEQUALS = 13;
	public static final int CMINUSEQUALS = 14;
	public static final int CTIMESEQUALS = 15;
	public static final int CDIVIDEEQUALS = 16;
	public static final int CMODEQUALS = 17;
	public static final int CCREATE = 18;
	public static final int CSETARGTO = 19;
	public static final int CIF = 20;
	public static final int CFOR = 21;
	public static final int CEND = 22;
	public static final int CBREAK = 23;
	public static final int CRETURN = 24;
	public static final int CQUIT = 25;
	public static final int CTYPE = 26;
	public static final int CPRESS = 27;
	public static final int CRELEASE = 28;
	public static final int COPTIONON = 29;
	public static final int COPTIONOFF = 30;
	public static final int CPRINTVAL = 31;
	public static final int CPRINT = 32;
	public static final int CSAVEIMAGE = 33;
	public static final int CLOADIMAGE = 34;
	public static final int OPLUS = -1;
	public static final int OMINUS = -2;
	public static final int OTIMES = -3;
	public static final int ODIVIDE = -4;
	public static final int OMOD = -5;
	public static final int ORANDOM = -6;
	public static final int OEQUAL = -7;
	public static final int ONOTEQUAL = -8;
	public static final int OGREATEREQUAL = -9;
	public static final int OGREATER = -10;
	public static final int OLESSEQUAL = -11;
	public static final int OLESS = -12;
	public static final int ONOT = -13;
	public static final int OOR = -14;
	public static final int OAND = -15;
	public static final int OIMAGECOLORAT = -16;
	public static final int OIMAGECOLOR = -17;
	public static final int OIMAGERED = -18;
	public static final int OIMAGEGREEN = -19;
	public static final int OIMAGEBLUE = -20;
	public static final int OREDPART = -21;
	public static final int OGREENPART = -22;
	public static final int OBLUEPART = -23;
	public static final int OINPUT = -24;
	public static final int OCOLORAT = -25;
	public static final int OSCAN = -26;
	public static String timestamp() {
		Calendar c = Calendar.getInstance();
		int second = c.get(Calendar.SECOND);
		int minute = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int day = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		return hour + (minute < 10 ? ":0" : ":") + minute + (second < 10 ? ":0" : ":") + second + " " + day + "/" + month + "/" + year;
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
		int length = line.length();
		for (int pos = 0; pos < length; pos += 1) {
			digit = (int)(line.charAt(pos));
			if (digit >= '0' && digit <= '9')
				number = number * 10 + digit - '0';
			else if (digit == '-' && number == 0)
				negative = true;
		}
		return negative ? -number : number;
	}
	public static int[] ints = new int[256];
	public static int stack = 0;
	public static Autoer auto = new Autoer();
	public static Script scriptlist = null;
	public static Varslist variable = null;
	public static run mainrun = null;
	public Script innerscript = null;
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
	public int pos = 0;
	public int[] scanorder = new int[] {3, 4};
	public int offx = 0;
	public int offy = 0;
	public boolean returnval = false;
	public boolean endmessages = true;
	public boolean skipped = false;
	public static class Varslist {
		public static final int VNONE = 0;
		public static final int VINT = 1;
		public static final int VBOOLEAN = 2;
		public static final int VCOLOR = 3;
		public static final int VTIMER = 4;
		public static final int VSCREENSHOT = 5;
		public static final int VSPECIAL = 100;
		public String name;
		public Varslist next;
		public Varslist(String n, Varslist vl) {
			name = n;
			next = vl;
		}
		public boolean istype(int t) {
			return t == VNONE;
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
			return t == VINT;
		}
		public int ival() {
			return val;
		}
		public void setto(String[] args, int[] cptrs) {
			mainrun.evaluate(args, cptrs, 1);
			val = ints[0];
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
			return t == VBOOLEAN;
		}
		public int ival() {
			return val ? 1 : 0;
		}
		public void setto(String[] args, int[] cptrs) {
			mainrun.evaluate(args, cptrs, 1);
			val = ints[0] != 0;
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
			return t == VCOLOR;
		}
		public int ival() {
			return -1;
		}
		public void setto(String[] args, int[] cptrs) {
			if (args[1].equals("at")) {
				mainrun.evaluate(args, cptrs, 2);
				val = auto.colorsat(ints[1] + mainrun.offx, ints[0] + mainrun.offy);
			} else {
				mainrun.evaluate(args, cptrs, 1);
				val = new int[] {ints[2], ints[1], ints[0]};
			}
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
			val = System.currentTimeMillis() - v;
		}
		public boolean istype(int t) {
			return t == VTIMER;
		}
		public int ival() {
			return (int)(System.currentTimeMillis() - val);
		}
		public void setto(String[] args, int[] cptrs) {
			mainrun.evaluate(args, cptrs, 1);
			val = System.currentTimeMillis() - ints[0];
		}
		public String toString() {
			return String.valueOf(System.currentTimeMillis() - val);
		}
		public VarsTimer clone() {
			return new VarsTimer(name, val, next);
		}
	}
	public static class VarsScreenshot extends Varslist {
		public BufferedImage val;
		public int top;
		public int left;
		public VarsScreenshot(String n, int x, int y, BufferedImage img, Varslist vl) {
			super(n, vl);
			left = x;
			top = y;
			val = img;
		}
		public boolean istype(int t) {
			return t == VSCREENSHOT;
		}
		public int ival() {
			return 0;
		}
		public void setto(String[] args, int[] cptrs) {
			int cptr1 = cptrs[1];
			if (cptr1 >= 0 && (variable = mainrun.vars[cptr1]).istype(Varslist.VSCREENSHOT)) {
				VarsScreenshot vs = (VarsScreenshot)(variable);
				left = vs.left;
				top = vs.top;
				val = vs.val;
			} else {
				mainrun.evaluate(args, cptrs, 1);
				left = ints[3];
				top = ints[2];
				val = auto.screenshot(ints[3] + mainrun.offx, ints[2] + mainrun.offy, ints[1], ints[0]);
			}
		}
		public String toString() {
			return "[screenshot]";
		}
		public VarsScreenshot clone() {
			return new VarsScreenshot(name, top, left, val, next);
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
			else if (n.equals(".skipped"))
				val = 6;
			else if (n.equals(".screenwidth"))
				val = 7;
			else if (n.equals(".screenheight"))
				val = 8;
		}
		public boolean istype(int t) {
			return t == VSPECIAL;
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
				return (int)(System.currentTimeMillis() / 1000);
			else if (val == 6)
				return mainrun.skipped ? 1 : 0;
			else if (val == 7)
				return Toolkit.getDefaultToolkit().getScreenSize().width;
			else if (val == 8)
				return Toolkit.getDefaultToolkit().getScreenSize().height;
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
			else if (val == 6)
				return String.valueOf(mainrun.skipped);
			else if (val == 7)
				return String.valueOf(Toolkit.getDefaultToolkit().getScreenSize().width);
			else if (val == 8)
				return String.valueOf(Toolkit.getDefaultToolkit().getScreenSize().height);
			return name;
		}
		public VarsSpecial clone() {
			return this;
		}
	}
	public static class Script {
		public String name;
		public String[][] val;
		public int[][] ival;
		public int[] action;
		public int[] rows;
		public Script next = null;
		public Varslist[] startvals = null;
		public Script(String n, int lines) {
			name = n;
			val = new String[lines][0];
			ival = new int[lines][0];
			action = new int[lines];
			rows = new int[lines];
		}
		//build the line, return the validity
		public boolean build(int row, String line) {
			int argnum = 0;
			boolean valid = true;
			if (line.startsWith("left")) {
				action[row] = CLEFT;
				val[row] = split(line.substring(4), true);
				argnum = 1;
			} else if (line.startsWith("right")) {
				action[row] = CRIGHT;
				val[row] = split(line.substring(5), true);
				argnum = 1;
			} else if (line.startsWith("middle")) {
				action[row] = CMIDDLE;
				val[row] = split(line.substring(6), true);
				argnum = 1;
			} else if (line.startsWith("move")) {
				action[row] = CMOVE;
				val[row] = split(line.substring(4), true);
				argnum = 2;
			} else if (line.startsWith("scroll")) {
				action[row] = CSCROLL;
				val[row] = split(line.substring(6), true);
				argnum = 1;
			} else if (line.startsWith("pausenot")) {
				action[row] = CPAUSENOT;
				val[row] = split(line.substring(8), true);
				argnum = 3;
			} else if (line.startsWith("pause")) {
				action[row] = CPAUSE;
				val[row] = split(line.substring(5), true);
			} else if (line.startsWith("jumpback")) {
				action[row] = CJUMPBACK;
				val[row] = new String[] {""};
			} else if (line.startsWith("jump")) {
				action[row] = CJUMP;
				val[row] = split(line.substring(4), true);
				argnum = 1;
			} else if (line.startsWith("call")) {
				action[row] = CCALL;
				val[row] = split(line.substring(4), true);
				argnum = 1;
			} else if (line.startsWith("run")) {
				action[row] = CRUN;
				val[row] = split(line.substring(3), true);
				argnum = 1;
			//setargto has to come before set, even though its num is higher
			} else if (line.startsWith("setargto")) {
				action[row] = CSETARGTO;
				val[row] = split(line.substring(8), true);
				argnum = 2;
			} else if (line.startsWith("set")) {
				action[row] = CSET;
				val[row] = split(line.substring(3), true);
				argnum = 2;
			} else if (line.startsWith("+=")) {
				action[row] = CPLUSEQUALS;
				val[row] = split(line.substring(2), true);
				argnum = 2;
			} else if (line.startsWith("-=")) {
				action[row] = CMINUSEQUALS;
				val[row] = split(line.substring(2), true);
				argnum = 2;
			} else if (line.startsWith("*=")) {
				action[row] = CTIMESEQUALS;
				val[row] = split(line.substring(2), true);
				argnum = 2;
			} else if (line.startsWith("/=")) {
				action[row] = CDIVIDEEQUALS;
				val[row] = split(line.substring(2), true);
				argnum = 2;
			} else if (line.startsWith("%=")) {
				action[row] = CMODEQUALS;
				val[row] = split(line.substring(2), true);
				argnum = 2;
			} else if (line.startsWith("create")) {
				action[row] = CCREATE;
				val[row] = split(line.substring(6), true);
				argnum = 3;
			} else if (line.startsWith("if")) {
				action[row] = CIF;
				val[row] = split(line, true);
				argnum = 2;
			} else if (line.startsWith("for")) {
				action[row] = CFOR;
				val[row] = split(line.substring(3), true);
				argnum = 4;
			} else if (line.startsWith("end")) {
				action[row] = CEND;
				val[row] = new String[] {""};
			} else if (line.startsWith("break")) {
				action[row] = CBREAK;
				val[row] = new String[] {"", ""};
			} else if (line.startsWith("return")) {
				action[row] = CRETURN;
				val[row] = new String[] {""};
			} else if (line.startsWith("quit")) {
				action[row] = CQUIT;
				val[row] = new String[] {""};
			} else if (line.startsWith("type")) {
				action[row] = CTYPE;
				int period = line.indexOf('.', 5);
				if (period < 0) {
					System.out.println("Bad syntax in " + name + " on line " + (row + 1) + ": missing period for type command");
					return false;
				}
				val[row] = new String[] {line.substring(period + 1)};
			} else if (line.startsWith("press")) {
				action[row] = CPRESS;
				val[row] = split(line.substring(5).toLowerCase(), true);
				argnum = 1;
			} else if (line.startsWith("release")) {
				action[row] = CRELEASE;
				val[row] = split(line.substring(7).toLowerCase(), true);
				argnum = 1;
			} else if (line.startsWith("optionon")) {
				action[row] = COPTIONON;
				val[row] = split(line.substring(8), true);
				argnum = 1;
			} else if (line.startsWith("optionoff")) {
				action[row] = COPTIONOFF;
				val[row] = split(line.substring(9), true);
				argnum = 1;
			} else if (line.startsWith("printval")) {
				action[row] = CPRINTVAL;
				val[row] = split(line.substring(8), true);
				argnum = 1;
			} else if (line.startsWith("println")) {
				action[row] = CPRINT;
				int begin = line.indexOf('.', 8);
				val[row] = new String[] {begin >= 0 ? line.substring(begin + 1) + "\n" : "\n"};
			} else if (line.startsWith("print")) {
				action[row] = CPRINT;
				int period = line.indexOf('.', 6);
				if (period < 0) {
					System.out.println("Bad syntax in " + name + " on line " + (row + 1) + ": missing period for print command");
					return false;
				}
				val[row] = new String[] {line.substring(period + 1)};
			} else if (line.startsWith("saveimage")) {
				action[row] = CSAVEIMAGE;
				val[row] = split(line.substring(9), true);
				argnum = 1;
			} else if (line.startsWith("loadimage")) {
				action[row] = CLOADIMAGE;
				val[row] = split(line.substring(9), true);
				argnum = 1;
			} else if (line.startsWith(":")) {
				action[row] = -1;
				val[row] = split(line, true);
			} else if (line.startsWith("[") || line.startsWith("(") || line.startsWith("{")) {
				action[row] = -1;
				val[row] = new String[] {"["};
			} else if (line.startsWith("]") || line.startsWith(")") || line.startsWith("}")) {
				action[row] = -1;
				val[row] = new String[] {"]"};
			}
			//give string values an int value
			ival[row] = ibuild(val[row]);
			String[] rval = val[row];
			int[] rival = ival[row];
			//find any redpart, greenpart, bluepart, input, or any of the image operators and swap it with the following variable
			for (int i = rval.length - 1; i >= 0; i -= 1) {
				if (rval[i].startsWith(" ") && rival[i] <= OIMAGECOLORAT && rival[i] >= OINPUT) {
					if (i == rval.length - 1) {
						System.out.print("Bad syntax in " + name + " on line " + (row + 1) + ":" + rval[i] + " is missing parameters");
						valid = false;
					} else {
						int ihold = rival[i];
						String shold = rval[i];
						rival[i] = rival[i + 1];
						rval[i] = rval[i + 1];
						rival[i + 1] = ihold;
						rval[i + 1] = shold;
					}
				}
			}
			rows[row] = row + 1;
			if (val[row].length < argnum) {
				if (action[row] == CIF)
					System.out.println("Bad syntax in " + name + " on line " + (row + 1) + ": only 0 parameters instead of 1");
				else
					System.out.println("Bad syntax in " + name + " on line " + (row + 1) + ": only " + val[row].length + " parameters instead of " + argnum);
				return false;
			} else if (action[row] == CFOR && val[row].length > argnum) {
				System.out.println("Bad syntax in " + name + " on line " + (row + 1) + ": too many parameters");
				return false;
			}
			return valid;
		}
		//get rid of all lines of a specific action
		public int purgelines(int purge) {
			int length = action.length;
			for (int spot = 0; spot < action.length; spot += 1) {
				if (action[spot] == purge)
					length -= 1;
			}
			String[][] newval = new String[length][0];
			int[][] newival = new int[length][0];
			int[] newaction = new int[length];
			int[] newrows = new int[length];
			int index = 0;
			for (int spot = 0; spot < action.length; spot += 1) {
				if (action[spot] != purge) {
					newval[index] = val[spot];
					newival[index] = ival[spot];
					newaction[index] = action[spot];
					newrows[index] = rows[spot];
					index += 1;
				}
			}
			val = newval;
			ival = newival;
			action = newaction;
			rows = newrows;
			return length;
		}
		//disinclude any labels in the line number calculation
		//returns 1 less than the row (if it's a label), but 1 gets added to it in the loop
		public int rowminuslabels(int oldrow) {
			int newrow = oldrow;
			for (int row = 0; row <= oldrow; row += 1) {
				//disinclude any labels in the line number calculation
				if (action[row] < 1)
					newrow -= 1;
			}
			return newrow;
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
				System.out.println("Error: script \"" + mainrun.innerscript.name + "\" at line " + mainrun.innerscript.rows[mainrun.pos] + " caused this exception:");
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
			valid = putter.build(row, lines[row]) && valid;
			//verify other scripts used
			if (putter.action[row] == CRUN && putter.val[row].length > 0 && scriptcalled(putter.val[row][0], scriptlist) == null) {
				Filer inner = new Filer("scripts/" + putter.val[row][0] + ".txt");
				if (inner.fileisthere()) {
					inner.readfile();
					Script lastscript = putter;
					while (lastscript.next != null)
						lastscript = lastscript.next;
					lastscript.next = new Script(putter.val[row][0], inner.lines());
					valid = verify(inner.getlines(), lastscript.next) && valid;
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
					jumpline += 1;
				if (jumpline >= linecount) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.rows[row] + ": no label called \"" + putter.val[row][index] + "\"");
					valid = false;
				} else
					putter.ival[row][index] = putter.rowminuslabels(jumpline);
			//find the end command for the for line, rearrange the values
			} else if (action == CFOR) {
				int jumpline = row;
				int layer = 1;
				while ((jumpline += 1) < linecount) {
					if (putter.action[jumpline] == CFOR)
						layer += 1;
					else if (putter.action[jumpline] == CEND) {
						if ((layer -= 1) == 0)
							break;
					}
				}
				if (layer > 0) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.rows[row] + ": for missing an end command");
					valid = false;
				} else {
					//reassign the data
					//end command: jump row, loop variable, destination value, increment
					putter.val[jumpline] = new String[] {"", putter.val[row][0], putter.val[row][2], putter.val[row][3]};
					putter.ival[jumpline] = new int[] {putter.rowminuslabels(row), putter.ival[row][0], putter.ival[row][2], putter.ival[row][3]};
					//for command: loop variable, initial value
					putter.val[row] = new String[] {putter.val[row][0], putter.val[row][1]};
					putter.ival[row] = new int[] {putter.ival[row][0], putter.ival[row][1]};
				}
			//make sure end commands have already been found
			} else if (action == CEND && putter.ival[row].length < 3) {
				System.out.println("Missing content in " + putter.name + " on line " + putter.rows[row] + ": end missing a for command");
				valid = false;
			//set the if command's next line
			} else if (action == CIF) {
				int jumpline = row + 1;
				int layer = 0;
				if (jumpline < linecount && putter.val[jumpline][0].equals("[")) {
					layer = 1;
					while ((jumpline += 1) < linecount) {
						if (putter.val[jumpline][0].equals("["))
							layer += 1;
						else if (putter.val[jumpline][0].equals("]")) {
							if ((layer -= 1) == 0)
								break;
						}
					}
					jumpline -= 1;
				}
				if (layer > 0) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.rows[row] + ": if bracket \"[\" missing an opposite \"]\"");
					valid = false;
				} else
					putter.ival[row][0] = putter.rowminuslabels(jumpline);
			//find the end command for the break
			} else if (action == CBREAK) {
				int jumpline = row;
				int layer = 1;
				while ((jumpline += 1) < linecount) {
					if (putter.action[jumpline] == CFOR)
						layer += 1;
					else if (putter.action[jumpline] == CEND) {
						if ((layer -= 1) == 0)
							break;
					}
				}
				if (layer > 0) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.rows[row] + ": break missing an end command");
					valid = false;
				} else
					putter.ival[row][0] = putter.rowminuslabels(jumpline);
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
			//find the index of the first value that could be a variable
			switch(action) {
				case CLEFT:
				case CRIGHT:
				case CMIDDLE:
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
				case CSAVEIMAGE:
				case CLOADIMAGE:
					i = 0;
					break;
				case CSETARGTO:
				case CIF:
				case CEND:
				case CCREATE:
					i = 1;
					break;
				case COPTIONON:
					name = putter.val[row][0];
					if (!name.equals("endingcall") && !name.equals("endmessages") && !name.equals("scanorder"))
						i = 1;
					break;
			}
			//check that variable names are usable
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
						System.out.println("Bad syntax in " + putter.name + " on line " + putter.rows[row] + ": that variable is reserved");
						valid = false;
					}
					break;
			}
			//new variable storage
			if (i != -1) {
				for (; i < putter.val[row].length; i += 1) {
					name = putter.val[row][i];
					//add any variable names to the list
					if (!name.startsWith(" ") && !tm.containsKey(name))
						tm.put(name, tm.size());
				}
			}
		}
		//stop if invalid
		if (!valid)
			return false;
		putter.startvals = new Varslist[tm.size()];
		Stack<Integer> forvars = new Stack<Integer>();
		//link all values with their variable name if they have one
		for (int row = 0; row < linecount; row += 1) {
			action = putter.action[row];
			int col = -1;
			//find the index of the first value that could be a variable
			switch(action) {
				case CLEFT:
				case CRIGHT:
				case CMIDDLE:
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
				case CSAVEIMAGE:
				case CLOADIMAGE:
					col = 0;
					break;
				case CSETARGTO:
				case CIF:
				case CEND:
				case CCREATE:
					col = 1;
					break;
				case COPTIONON:
					name = putter.val[row][0];
					if (!name.equals("endingcall") && !name.equals("endmessages") && !name.equals("scanorder"))
						col = 1;
					break;
			}
			//build the variable pointers list by assigning ids to the ival field and creating starting vals
			if (col != -1) {
				for (; col < putter.ival[row].length; col += 1) {
					name = putter.val[row][col];
					if (!name.startsWith(" ")) {
						int ptr = tm.get(name);
						if (name.startsWith("."))
							putter.startvals[ptr] = new VarsSpecial(name);
						else
							putter.startvals[ptr] = new VarsInt(name, putter.ival[row][col], null);
						putter.ival[row][col] = ptr;
					}
				}
			}
			if (action == CPRINTVAL && putter.ival[row][0] < 0) {
				System.out.println("Bad syntax in " + putter.name + " on line " + putter.rows[row] + ": cannot print the value of an operator");
				valid = false;
			}
			//push or pop the id of a for variable
			if (putter.action[row] == CFOR)
				forvars.push(putter.ival[row][0]);
			else if (putter.action[row] == CEND)
				forvars.pop();
			//put the variable id in break
			else if (putter.action[row] == CBREAK)
				putter.ival[row][1] = forvars.peek();
		}
		return valid;
	}
	//remove whitespace from the beginning of the line
	public static String clip(String theline) {
		int spot = 0;
		int length = theline.length();
		while (spot < length && (theline.charAt(spot) == ' ' || theline.charAt(spot) == '\t'))
			spot += 1;
		return theline.substring(spot);
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
				index += 1;
			while (index < length) {
				begin = index;
				while (index < length && theline.charAt(index) != '\t' && theline.charAt(index) != ' ')
					index += 1;
				vl = new Varslist(theline.substring(begin, index), vl);
				count += 1;
				while (index < length && (theline.charAt(index) == '\t' || theline.charAt(index) == ' '))
					index += 1;
			}
		} else {
			while (index < length && theline.charAt(index) == ',')
				index += 1;
			while (index < length) {
				begin = index;
				while (index < length && theline.charAt(index) != ',')
					index += 1;
				vl = new Varslist(theline.substring(begin, index), vl);
				count += 1;
				while (index < length && theline.charAt(index) == ',')
					index += 1;
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
				op = OPLUS;
			else if (val[spot].equals("-"))
				op = OMINUS;
			else if (val[spot].equals("*"))
				op = OTIMES;
			else if (val[spot].equals("/"))
				op = ODIVIDE;
			else if (val[spot].equals("%"))
				op = OMOD;
			else if (val[spot].equals("random"))
				op = ORANDOM;
			else if (val[spot].equals("=="))
				op = OEQUAL;
			else if (val[spot].equals("!="))
				op = ONOTEQUAL;
			else if (val[spot].equals(">="))
				op = OGREATEREQUAL;
			else if (val[spot].equals(">"))
				op = OGREATER;
			else if (val[spot].equals("<="))
				op = OLESSEQUAL;
			else if (val[spot].equals("<"))
				op = OLESS;
			else if (val[spot].equals("not"))
				op = ONOT;
			else if (val[spot].equals("or"))
				op = OOR;
			else if (val[spot].equals("and"))
				op = OAND;
			else if (val[spot].equals("imagecolorat"))
				op = OIMAGECOLORAT;
			else if (val[spot].equals("imagecolor"))
				op = OIMAGECOLOR;
			else if (val[spot].equals("imagered"))
				op = OIMAGERED;
			else if (val[spot].equals("imagegreen"))
				op = OIMAGEGREEN;
			else if (val[spot].equals("imageblue"))
				op = OIMAGEBLUE;
			else if (val[spot].equals("redpart"))
				op = OREDPART;
			else if (val[spot].equals("greenpart"))
				op = OGREENPART;
			else if (val[spot].equals("bluepart"))
				op = OBLUEPART;
			else if (val[spot].equals("input"))
				op = OINPUT;
			else if (val[spot].equals("colorat"))
				op = OCOLORAT;
			else if (val[spot].equals("scan"))
				op = OSCAN;
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
		innerscript = s;
		//extract all the data
		String[][] lines = s.val;
		int[][] cplist = s.ival;
		int[] actions = s.action;
		//build the vars
		int varcount = s.startvals.length;
		vars = new Varslist[varcount];
		for (int i = 0; i < varcount; i += 1) {
			vars[i] = s.startvals[i].clone();
		}
/*
System.out.println("Vars:");
for (int i = 0; i < vars.length; i += 1) {
System.out.println(vars[i].name + ": " + vars[i].ival());
}
/**/
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
System.out.print(s.name + " @ L" + s.rows[pos] + "- A" + action + ": ");
for (int i = 0; i < args.length; i += 1) {
System.out.print(args[i] + "(" + cptrs[i] + "), ");
}
System.out.println();
/**/
			if (action <= CPAUSE) {
				//left
				if (action == CLEFT) {
					evaluate(args, cptrs, 0);
					if (stack < 1)
						auto.left(ints[0]);
					else
						auto.leftEnsure(ints[1] + offx, ints[0] + offy);
				//right
				} else if (action == CRIGHT) {
					evaluate(args, cptrs, 0);
					if (stack < 1)
						auto.right(ints[0]);
					else
						auto.rightEnsure(ints[1] + offx, ints[0] + offy);
				//middle
				} else if (action == CMIDDLE) {
					evaluate(args, cptrs, 0);
					if (stack < 1)
						auto.middle(ints[0]);
					else
						auto.middleEnsure(ints[1] + offx, ints[0] + offy);
				//move
				} else if (action == CMOVE) {
					evaluate(args, cptrs, 0);
					auto.moveEnsure(ints[1] + offx, ints[0] + offy);
				//scroll
				} else if (action == CSCROLL) {
					if (args.length < 2)
						auto.scroll(args[0]);
					else {
						evaluate(args, cptrs, 1);
						auto.scroll(args[0], ints[0]);
					}
				//pausenot
				} else if (action == CPAUSENOT) {
					evaluate(args, cptrs, 0);
					if (stack < 5)
						pausenot(ints[4] + offx, ints[3] + offy, ints[2], ints[1], ints[0], -1);
					else
						pausenot(ints[5] + offx, ints[4] + offy, ints[3], ints[2], ints[1], ints[0]);
				//pause
				} else {
					evaluate(args, cptrs, 0);
					if (stack < 0)
						pause();
					else if (stack < 4)
						pause(ints[0]);
					else if (stack < 5)
						pause(ints[4] + offx, ints[3] + offy, ints[2], ints[1], ints[0], -1);
					else
						pause(ints[5] + offx, ints[4] + offy, ints[3], ints[2], ints[1], ints[0]);
				}
			} else if (action <= CRUN) {
				//jumpback
				if (action == CJUMPBACK) {
					pos = jumpback.val;
					jumpback = (VarsInt)(jumpback.next);
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
						end("Called from script \"" + s.name + "\" at line " + s.rows[pos] + ".");
					mainrun = this;
				}
			} else if (action <= CSETARGTO) {
				//set
				if (action == CSET)
					vars[cptrs[0]].setto(args, cptrs);
				//+=, -=, *=, /=, %=
				else if (action <= CMODEQUALS) {
					VarsInt vi = (VarsInt)(vars[cptrs[0]]);
					evaluate(args, cptrs, 1);
					if (action == CPLUSEQUALS)
						vi.val += ints[0];
					else if (action == CMINUSEQUALS)
						vi.val -= ints[0];
					else if (action == CTIMESEQUALS)
						vi.val *= ints[0];
					else if (action == CDIVIDEEQUALS)
						vi.val /= ints[0];
					else
						vi.val %= ints[0];
				//create
				} else if (action == CCREATE) {
					int cptr = cptrs[1];
					if (args[0].equals("int")) {
						evaluate(args, cptrs, 2);
						vars[cptr] = new VarsInt(args[1], ints[0], vars[cptr]);
					} else if (args[0].equals("boolean")) {
						evaluate(args, cptrs, 2);
						vars[cptr] = new VarsBoolean(args[1], ints[0] != 0, vars[cptr]);
					} else if (args[0].equals("color")) {
						if (args[2].equals("at")) {
							evaluate(args, cptrs, 3);
							vars[cptr] = new VarsColor(args[1], auto.colorsat(ints[1] + offx, ints[0] + offy), vars[cptr]);
						} else {
							evaluate(args, cptrs, 2);
							vars[cptr] = new VarsColor(args[1], new int[] {ints[2], ints[1], ints[0]}, vars[cptr]);
						}
					} else if (args[0].equals("timer")) {
						evaluate(args, cptrs, 2);
						vars[cptr] = new VarsTimer(args[1], ints[0], vars[cptr]);
					} else if (args[0].equals("screenshot")) {
						int cptr2 = cptrs[2];
						if (cptr2 > 0 && (variable = vars[cptr2]).istype(Varslist.VSCREENSHOT)) {
							VarsScreenshot vs = (VarsScreenshot)(variable);
							vars[cptr] = new VarsScreenshot(args[1], vs.left, vs.top, vs.val, vars[cptr]);
						} else {
							evaluate(args, cptrs, 2);
							vars[cptr] = new VarsScreenshot(args[1], ints[3], ints[2], auto.screenshot(ints[3] + offx, ints[2] + offy, ints[1], ints[0]), vars[cptr]);
						}
					}
				//setargto
				} else {
					variable = vars[cptrs[1]];
					for (int spot = 1; spot < input.length; spot += 1) {
						if (input[spot].startsWith(args[0])) {
							args = split(" ," + input[spot].substring(args[0].length()), false);
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
					evaluate(args, cptrs, 1);
					if (ints[0] == 0)
						pos = cptrs[0];
				//for
				} else if (action == CFOR)
					vars[cptrs[0]] = new VarsInt(args[0], vars[cptrs[1]].ival(), vars[cptrs[0]]);
				//end
				else if (action == CEND) {
					VarsInt vi = (VarsInt)(vars[cptrs[1]]);
					int add = vars[cptrs[3]].ival();
					vi.val += add;
					if (add >= 0 ? vi.val > vars[cptrs[2]].ival() : vi.val < vars[cptrs[2]].ival())
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
					end("Quitting script \"" + s.name + "\" from line " + s.rows[pos] + ".");
			} else if (action <= CPRINT) {
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
					if (args[0].equals("quitafter")) {
						evaluate(args, cptrs, 1);
						quitafter = ints[0];
					} else if (args[0].equals("endingcall"))
						endingcall = cptrs[1];
					else if (args[0].equals("variance")) {
						evaluate(args, cptrs, 1);
						if (stack < 2) {
							rvariance = ints[0];
							gvariance = ints[0];
							bvariance = ints[0];
						} else {
							rvariance = ints[2];
							gvariance = ints[1];
							bvariance = ints[0];
						}
					} else if (args[0].equals("mousetolerance")) {
						evaluate(args, cptrs, 1);
						if (stack < 1) {
							mousetolx = ints[0];
							mousetoly = ints[0];
						} else {
							mousetolx = ints[1];
							mousetoly = ints[0];
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
						evaluate(args, cptrs, 1);
						offx = ints[1];
						offy = ints[0];
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
			} else {
				//saveimage
				if (action == CSAVEIMAGE) {
					VarsScreenshot vs = (VarsScreenshot)(vars[cptrs[0]]);
					try {
						ImageIO.write(vs.val, "png", new File(vs.name + ".png"));
					} catch(Exception e) {
						System.out.println("Error: could not save file \"" + args[0] + ".png\".");
					}
				//loadimage
				} else {
					evaluate(args, cptrs, 1);
					int left, top;
					if (stack < 1) {
						left = 0;
						top = 0;
					} else {
						left = ints[1];
						top = ints[0];
					}
					try {
						vars[cptrs[0]] = new VarsScreenshot(args[0], left, top, ImageIO.read(new File(args[0] + ".png")), vars[cptrs[0]]);
					} catch(Exception e) {
						System.out.println("Error: could not load file \"" + args[0] + ".png\".");
						vars[cptrs[0]] = new VarsScreenshot(args[0], left, top, null, vars[cptrs[0]]);
					}
				}
			}
			pos = pos + 1;
		}
		return returnval;
	}
	//evaluate the arithmetic/boolean equation into a stack
	public void evaluate(String[] vals, int[] cptrs, int off) {
		stack = -1;
		for (int spot = vals.length - 1; spot >= off; spot -= 1) {
			int cptr = cptrs[spot];
			if (cptr < 0) {
				if (cptr >= ORANDOM) {
					// +
					if (cptr == OPLUS) {
						stack -= 1;
						ints[stack] = ints[stack + 1] + ints[stack];
					// -
					} else if (cptr == OMINUS) {
						stack -= 1;
						ints[stack] = ints[stack + 1] - ints[stack];
					// *
					} else if (cptr == OTIMES) {
						stack -= 1;
						ints[stack] = ints[stack + 1] * ints[stack];
					// /
					} else if (cptr == ODIVIDE) {
						stack -= 1;
						ints[stack] = ints[stack + 1] / ints[stack];
					// %
					} else if (cptr == OMOD) {
						stack -= 1;
						ints[stack] = ints[stack + 1] % ints[stack];
					// random
					} else
						ints[stack] = (int)(Math.random() * ints[stack]);
				} else if (cptr >= OLESS) {
					// ==
					if (cptr == OEQUAL) {
						stack -= 1;
						ints[stack] = ints[stack + 1] == ints[stack] ? 1 : 0;
					// !=
					} else if (cptr == ONOTEQUAL) {
						stack -= 1;
						ints[stack] = ints[stack + 1] != ints[stack] ? 1 : 0;
					// >=
					} else if (cptr == OGREATEREQUAL) {
						stack -= 1;
						ints[stack] = ints[stack + 1] >= ints[stack] ? 1 : 0;
					// >
					} else if (cptr == OGREATER) {
						stack -= 1;
						ints[stack] = ints[stack + 1] > ints[stack] ? 1 : 0;
					// <=
					} else if (cptr == OLESSEQUAL) {
						stack -= 1;
						ints[stack] = ints[stack + 1] <= ints[stack] ? 1 : 0;
					// <
					} else {
						stack -= 1;
						ints[stack] = ints[stack + 1] < ints[stack] ? 1 : 0;
					}
				} else if (cptr >= OAND) {
					// not
					if (cptr == ONOT)
						ints[stack] = ints[stack] == 0 ? 1 : 0;
					// or
					else if (cptrs[spot] == OOR) {
						stack -= 1;
						ints[stack] = (ints[stack + 1] != 0 || ints[stack] != 0) ? 1 : 0;
					// and
					} else {
						stack -= 1;
						ints[stack] = (ints[stack + 1] != 0 && ints[stack] != 0) ? 1 : 0;
					}
				} else if (cptr >= OIMAGEBLUE) {
					// imagecolorat
					if (cptr == OIMAGECOLORAT) {
						VarsScreenshot vs = (VarsScreenshot)(vars[cptrs[spot -= 1]]);
						int pixel = vs.val.getRGB(ints[stack] - vs.left, ints[stack - 1] - vs.top);
						stack -= 4;
						int r = (pixel >> 16) & 255;
						int g = (pixel >> 8) & 255;
						int b = pixel & 255;
						int ir = ints[stack + 2];
						int ig = ints[stack + 1];
						int ib = ints[stack];
						ints[stack] = (ir <= r + rvariance && ir >= r - rvariance && ig <= g + gvariance && ig >= g - gvariance && ib <= b + bvariance && ib >= b - bvariance) ? 1 : 0;
					// imagecolor
					} else if (cptr == OIMAGECOLOR) {
						VarsScreenshot vs = (VarsScreenshot)(vars[cptrs[spot -= 1]]);
						int pixel = vs.val.getRGB(ints[stack] - vs.left, ints[stack - 1] - vs.top);
						ints[stack - 1] = pixel & 255;
						ints[stack] = (pixel >> 8) & 255;
						ints[stack += 1] = (pixel >> 16) & 255;
					// imagered
					} else if (cptr == OIMAGERED) {
						VarsScreenshot vs = (VarsScreenshot)(vars[cptrs[spot -= 1]]);
						stack -= 1;
						ints[stack] = (vs.val.getRGB(ints[stack + 1] - vs.left, ints[stack] - vs.top) >> 16) & 255;
					// imagegreen
					} else if (cptr == OIMAGEGREEN) {
						VarsScreenshot vs = (VarsScreenshot)(vars[cptrs[spot -= 1]]);
						stack -= 1;
						ints[stack] = (vs.val.getRGB(ints[stack + 1] - vs.left, ints[stack] - vs.top) >> 8) & 255;
					// imageblue
					} else {
						VarsScreenshot vs = (VarsScreenshot)(vars[cptrs[spot -= 1]]);
						stack -= 1;
						ints[stack] = vs.val.getRGB(ints[stack + 1] - vs.left, ints[stack] - vs.top) & 255;
					}
				} else {
					// redpart
					if (cptr == OREDPART)
						ints[stack += 1] = ((VarsColor)(vars[cptrs[spot -= 1]])).val[0];
					// greenpart
					else if (cptr == OGREENPART)
						ints[stack += 1] = ((VarsColor)(vars[cptrs[spot -= 1]])).val[1];
					// bluepart
					else if (cptr == OBLUEPART)
						ints[stack += 1] = ((VarsColor)(vars[cptrs[spot -= 1]])).val[2];
					// input
					else if (cptr == OINPUT) {
						spot -= 1;
						ints[stack += 1] = 0;
						int length = input.length;
						for (int loc = 1; loc < length; loc += 1) {
							if (input[loc].equals(vals[spot])) {
								ints[stack] = 1;
								break;
							}
						}
					// colorat
					} else if (cptr == OCOLORAT) {
						stack -= 4;
						ints[stack] = colorat(ints[stack + 4] + offx, ints[stack + 3] + offy, ints[stack + 2], ints[stack + 1], ints[stack]) ? 1 : 0;
					// scan
					} else {
						scanx = -1;
						scany = -1;
						BufferedImage image;
						int sleft, stop;
						cptr = cptrs[spot + 1];
						if (cptr >= 0 && vars[cptr].istype(Varslist.VSCREENSHOT)) {
							VarsScreenshot vs = (VarsScreenshot)(vars[cptr]);
							image = vs.val;
							sleft = vs.left;
							stop = vs.top;
							stack -= 3;
						} else {
							image = auto.screenshot(ints[stack] + offx, ints[stack - 1] + offy, ints[stack - 2], ints[stack - 3]);
							sleft = ints[stack];
							stop = ints[stack - 1];
							stack -= 6;
						}
						int pixel = 0;
						int r = 0;
						int g = 0;
						int b = 0;
						int ir = ints[stack + 2];
						int ig = ints[stack + 1];
						int ib = ints[stack];
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
									scanx = sleft + coords[getx];
									scany = stop + coords[gety];
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
				variable = vars[cptr];
				if (variable.istype(Varslist.VCOLOR)) {
					int[] vcval = ((VarsColor)(variable)).val;
					ints[stack += 1] = vcval[2];
					ints[stack += 1] = vcval[1];
					ints[stack += 1] = vcval[0];
				} else
					ints[stack += 1] = variable.ival();
			}
		}
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
			//wait a millisecond at a time
			auto.wait(1);
			if (!mousewithin(xx, yy)) {
				end("You moved the mouse at a pause on line " + innerscript.rows[pos] + " in script \"" + innerscript.name + "\"");
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
		skipped = false;
		long now = System.currentTimeMillis();
		while (!colorat(x, y, r, g, b)) {
			if (!mousewithin(xx, yy)) {
				end("You moved the mouse at a pause on line " + innerscript.rows[pos] + " in script \"" + innerscript.name + "\"");
				return;
			}
			if (quitafter >= 0 && System.currentTimeMillis() - now >= quitafter) {
				end("Quitting a pause from script \"" + innerscript.name + "\" at line " + innerscript.rows[pos]);
				return;
			}
			if (waittime >= 0 && System.currentTimeMillis() - now >= waittime) {
				skipped = true;
				return;
			}
		}
	}
	public void pausenot(int x, int y, int r, int g, int b, int waittime) {
		int xx = mousex();
		int yy = mousey();
		skipped = false;
		long now = System.currentTimeMillis();
		while (colorat(x, y, r, g, b)) {
			if (!mousewithin(xx, yy)) {
				end("You moved the mouse at a pausenot on line " + innerscript.rows[pos] + " in script \"" + innerscript.name + "\"");
				return;
			}
			if (quitafter >= 0 && System.currentTimeMillis() - now >= quitafter) {
				end("Quitting a pausenot from script \"" + innerscript.name + "\" at line " + innerscript.rows[pos]);
				return;
			}
			if (waittime >= 0 && System.currentTimeMillis() - now >= waittime) {
				skipped = true;
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
			pos = innerscript.val.length;
		returnval = true;
	}
}
