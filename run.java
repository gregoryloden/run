import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.awt.MouseInfo;
//TO ADD:
public class run {
	public static File filefor(String... folders) {
		String filename = "";
		char slash = '\\';
		if (System.getProperty("os.name").toLowerCase().startsWith("mac"))
			slash = '/';
		for (int pos = 0; pos < folders.length - 1; pos += 1) {
			filename = filename + folders[pos] + slash;
		}
		if (folders.length > 0)
			filename = filename + folders[folders.length - 1];
		return new File(filename);
	}
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
	public static int xx;
	public static int yy;
	public static Autoer auto;
	public static int waittime = 0;
	public static String[] input = null;
	public static Varslist gbvars = null;
	public static VarsInt vars = null;
	public static int variance = 5;
	public static boolean keepgoing = true;
	public static long timer = 0;
	public static Varslist varcalled = null;
	public static int quitafter = -1;
	public static String endingcall = null;
	public static VarsInt jumpback = null;
	public static boolean mousequit = true;
	public static int scanx = 0;
	public static int scany = 0;
	public static String[] scripts = null;
	public static int[] ints = null;
	public static int pos = 0;
	public static VarsScript scriptlist = null;
	public static VarsScript mainscript = null;
	public static int action = 0;
	public static int[] scanorder = new int[] {3, 4};
	public static int[] rows = null;
	public static class Varslist {
		String name = "";
		Varslist next = null;
		public String type() {
			return "";
		}
		public String toString() {
			return "";
		}
	}
	public static class VarsInt extends Varslist {
		int val;
		VarsInt(String n, int v, Varslist vl) {
			name = n;
			val = v;
			next = vl;
		}
		public String type() {
			return "int";
		}
		public String toString() {
			return "" + val;
		}
	}
	public static class VarsBoolean extends Varslist {
		boolean val;
		VarsBoolean(String n, boolean v, Varslist vl) {
			name = n;
			val = v;
			next = vl;
		}
		public String type() {
			return "boolean";
		}
		public String toString() {
			return "" + val;
		}
	}
	public static class VarsColor extends Varslist {
		int[] val;
		VarsColor(String n, int[] v, Varslist vl) {
			name = n;
			val = v;
			next = vl;
		}
		public String type() {
			return "color";
		}
		public String toString() {
			return val[0] + "," + val[1] + "," + val[2];
		}
	}
	public static class VarsScript extends Varslist {
		String[][] val;
		int[] action;
		int[] row;
		VarsScript(String n, int lines, Varslist vl) {
			name = n;
			val = new String[lines][0];
			action = new int[lines];
			row = new int[lines];
			next = vl;
		}
		public String type() {
			return "color";
		}
		public String toString() {
			return name;
		}
	}
	public static void main(String[] args) {
		auto = new Autoer();
		if (args.length < 1)
			end("You need a script to run.");
		Filer filer = new Filer(filefor("scripts", args[0] + ".txt"));
		if (!filer.fileisthere())
			end("Script \"" + args[0] + "\" does not exist.");
		filer.readfile();
		String[] lines = filer.getlines();
		scriptlist = new VarsScript(args[0], lines.length, null);
//quit if there are errors
		if (!verify(lines))
			end();
//set the scriptlist so that the main script is on the top
		mainscript = scriptlist;
		if (scriptlist.next != null) {
			while (mainscript.next.next != null)
				mainscript = (VarsScript)(mainscript.next);
			mainscript.next.next = scriptlist;
			scriptlist = (VarsScript)(mainscript.next);
			mainscript.next = null;
			mainscript = scriptlist;
		}
		input = subarray(args, 1, args.length);
		rows = mainscript.row;
		timer = System.currentTimeMillis();
		try {
			runscript(mainscript.val, mainscript.action);
		} catch(Exception e) {
			end("Error: script \"" + mainscript.name + "\" at line " + rows[pos] + " caused this exception:\n" + e);
		}
		end();
	}
	public static boolean verify(String[] lines) {
		VarsScript putter = scriptlist;
		String[] bits;
		int argnum;
		int linecount = lines.length;
		boolean valid = true;
//clip all the lines
		for (int row = 0; row < lines.length; row += 1) {
			lines[row] = clip(lines[row]);
		}
//assign all the lines an action, check for errors
		for (int row = 0; row < lines.length; row += 1) {
			argnum = build(putter, lines, row);
			putter.row[row] = row + 1;
			bits = putter.val[row];
			if (argnum < 0)
				valid = false;
			else if ((argnum < 0 && -1 - argnum > bits.length) || argnum > bits.length) {
				System.out.println("Bad syntax in " + putter.name + " on line " + (row + 1) + ": only " + bits.length + " arguments instead of " + argnum + "");
				valid = false;
			} else if (putter.action[row] == 9 && argnum > 0 && varcalled(bits[0], scriptlist) == null) {
				Filer inner = new Filer(filefor("scripts", bits[0] + ".txt"));
				if (inner.fileisthere()) {
					inner.readfile();
					scriptlist = new VarsScript(bits[0], inner.lines(), scriptlist);
					valid = verify(inner.getlines()) && valid;
				} else {
					System.out.println("Missing content in " + putter.name + " on line " + (row + 1) + ": script \"" + bits[0] + "\" was not found");
					valid = false;
				}
			} else if (putter.action[row] == -1)
				linecount = linecount - 1;
		}
//remove all comments
		if (linecount < lines.length) {
			String[][] newval = new String[linecount][0];
			int[] newaction = new int[linecount];
			int[] newrow = new int[linecount];
			int index = 0;
			linecount = lines.length;
			for (int spot = 0; spot < linecount; spot += 1) {
				if (putter.action[spot] > -1) {
					newval[index] = putter.val[spot];
					newaction[index] = putter.action[spot];
					newrow[index] = putter.row[spot];
					index = index + 1;
				}
			}
			putter.val = newval;
			putter.action = newaction;
			putter.row = newrow;
		}
//assign the values to commands that need to know the line number
		linecount = putter.action.length;
		for (int row = 0; row < linecount; row += 1) {
			if (putter.action[row] == 7 || putter.action[row] == 8) {
				int jumpline = 0;
				while (jumpline < linecount && !putter.val[jumpline][0].equals(":" + putter.val[row][0]))
					jumpline = jumpline + 1;
				if (jumpline >= linecount) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": no label called \"" + putter.val[row][0] + "\"");
					valid = false;
				} else
					putter.val[row][0] = "" + jumpline;
			} else if (putter.action[row] == 20) {
				int jumpline = row + 1;
				int layer = 1;
				while (jumpline < linecount && layer > 0) {
					if (putter.action[jumpline] == 20)
						layer = layer + 1;
					else if (putter.action[jumpline] == 21)
						layer = layer - 1;
					jumpline = jumpline + 1;
				}
				if (jumpline >= linecount) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": for missing an end command");
					valid = false;
				} else
					putter.val[jumpline - 1] = new String[] {"" + row};
			} else if (putter.action[row] == 21 && putter.val[row][0].equals("")) {
				System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": end missing a for command");
				valid = false;
			} else if (putter.val[row][0].startsWith("[")) {
				int jumpline = row + 1;
				int layer = 1;
				while (jumpline < linecount && layer > 0) {
					if (putter.val[jumpline][0].equals("["))
						layer = layer + 1;
					else if (putter.val[jumpline][0].equals("]"))
						layer = layer - 1;
					jumpline = jumpline + 1;
				}
				if (jumpline >= linecount) {
					System.out.println("Missing content in " + putter.name + " on line " + putter.row[row] + ": if bracket \"[\" missing an opposite \"]\"");
					valid = false;
				} else
					putter.val[row] = new String[] {"[", "" + (jumpline - 1)};
			}
		}
		return valid;
	}
	public static String clip(String theline) {
		int spot = 0;
		int length = theline.length();
		while (spot < length && (theline.charAt(spot) == ' ' || theline.charAt(spot) == '\t'))
			spot = spot + 1;
		return theline.substring(spot, theline.length());
	}
	public static int build(VarsScript vs, String[] lines, int row) {
		String line = lines[row];
		if (line.startsWith("left")) {
			vs.action[row] = 1;
			vs.val[row] = split(line.substring(4, line.length()));
			return 2;
		} else if (line.startsWith("right")) {
			vs.action[row] = 2;
			vs.val[row] = split(line.substring(5, line.length()));
			return 2;
		} else if (line.startsWith("move")) {
			vs.action[row] = 3;
			vs.val[row] = split(line.substring(4, line.length()));
			return 2;
		} else if (line.startsWith("pausenot")) {
			vs.action[row] = 4;
			vs.val[row] = split(line.substring(8, line.length()));
			return 3;
		} else if (line.startsWith("pause")) {
			vs.action[row] = 5;
			vs.val[row] = split(line.substring(5, line.length()));
			return 1;
		} else if (line.startsWith("jumpback")) {
			vs.action[row] = 6;
			vs.val[row] = new String[] {""};
			return 0;
		} else if (line.startsWith("jump")) {
			vs.action[row] = 7;
			vs.val[row] = split(line.substring(4, line.length()));
			return 1;
		} else if (line.startsWith("call")) {
			vs.action[row] = 8;
			vs.val[row] = split(line.substring(4, line.length()));
			return 1;
		} else if (line.startsWith("run")) {
			vs.action[row] = 9;
			vs.val[row] = split(line.substring(3, line.length()));
			return 1;
		} else if (line.startsWith("create")) {
			vs.action[row] = 10;
			vs.val[row] = split(line.substring(6, line.length()));
			if (vs.val[row].length > 1 && vs.val[row][1].startsWith(".")) {
				System.out.println("Bad syntax in " + vs.name + " on line " + (row + 1) + ": variables cannot start with \".\"");
				return -4;
			}
			for (int spot = 0; spot < vs.val[row].length; spot += 1) {
				if (	vs.val[row][spot].equals("not") ||
					vs.val[row][spot].equals("input") ||
					vs.val[row][spot].equals("or") ||
					vs.val[row][spot].equals("and") ||
					vs.val[row][spot].equals("==") ||
					vs.val[row][spot].equals("!=") ||
					vs.val[row][spot].equals(">=") ||
					vs.val[row][spot].equals(">") ||
					vs.val[row][spot].equals("<=") ||
					vs.val[row][spot].equals("<") ||
					vs.val[row][spot].equals("colorat") ||
					vs.val[row][spot].equals("scan") ||
					vs.val[row][spot].equals("+") ||
					vs.val[row][spot].equals("-") ||
					vs.val[row][spot].equals("*") ||
					vs.val[row][spot].equals("/") ||
					vs.val[row][spot].equals("%") ||
					vs.val[row][spot].equals("random"))
					vs.val[row][spot] = " " + vs.val[row][spot];
			}
			return 3;
		} else if (line.startsWith("setargto")) {
			vs.action[row] = 11;
			vs.val[row] = split(line.substring(8, line.length()));
			return 2;
		} else if (line.startsWith("set")) {
			vs.action[row] = 12;
			vs.val[row] = split(line.substring(3, line.length()));
			for (int spot = 0; spot < vs.val[row].length; spot += 1) {
				if (	vs.val[row][spot].equals("not") ||
					vs.val[row][spot].equals("input") ||
					vs.val[row][spot].equals("or") ||
					vs.val[row][spot].equals("and") ||
					vs.val[row][spot].equals("==") ||
					vs.val[row][spot].equals("!=") ||
					vs.val[row][spot].equals(">=") ||
					vs.val[row][spot].equals(">") ||
					vs.val[row][spot].equals("<=") ||
					vs.val[row][spot].equals("<") ||
					vs.val[row][spot].equals("colorat") ||
					vs.val[row][spot].equals("scan") ||
					vs.val[row][spot].equals("+") ||
					vs.val[row][spot].equals("-") ||
					vs.val[row][spot].equals("*") ||
					vs.val[row][spot].equals("/") ||
					vs.val[row][spot].equals("%") ||
					vs.val[row][spot].equals("random"))
					vs.val[row][spot] = " " + vs.val[row][spot];
			}
			return 2;
		} else if (line.startsWith("+=")) {
			vs.action[row] = 13;
			vs.val[row] = split(line.substring(2, line.length()));
			return 2;
		} else if (line.startsWith("-=")) {
			vs.action[row] = 14;
			vs.val[row] = split(line.substring(2, line.length()));
			return 2;
		} else if (line.startsWith("*=")) {
			vs.action[row] = 15;
			vs.val[row] = split(line.substring(2, line.length()));
			return 2;
		} else if (line.startsWith("/=")) {
			vs.action[row] = 16;
			vs.val[row] = split(line.substring(2, line.length()));
			return 2;
		} else if (line.startsWith("%=")) {
			vs.action[row] = 17;
			vs.val[row] = split(line.substring(2, line.length()));
			return 2;
		} else if (line.startsWith("treset")) {
			vs.action[row] = 18;
			vs.val[row] = new String[] {""};
			return 0;
		} else if (line.startsWith("if")) {
			vs.action[row] = 19;
			vs.val[row] = split(line.substring(2, line.length()));
			for (int spot = 0; spot < vs.val[row].length; spot += 1) {
				if (	vs.val[row][spot].equals("not") ||
					vs.val[row][spot].equals("input") ||
					vs.val[row][spot].equals("or") ||
					vs.val[row][spot].equals("and") ||
					vs.val[row][spot].equals("==") ||
					vs.val[row][spot].equals("!=") ||
					vs.val[row][spot].equals(">=") ||
					vs.val[row][spot].equals(">") ||
					vs.val[row][spot].equals("<=") ||
					vs.val[row][spot].equals("<") ||
					vs.val[row][spot].equals("colorat") ||
					vs.val[row][spot].equals("scan"))
					vs.val[row][spot] = " " + vs.val[row][spot];
			}
			return 1;
		} else if (line.startsWith("for")) {
			vs.action[row] = 20;
			vs.val[row] = split(line.substring(3, line.length()));
			if (vs.val[row].length > 0 && vs.val[row][0].startsWith(".")) {
				System.out.println("Bad syntax in " + vs.name + " on line " + (row + 1) + ": variables cannot start with \".\"");
				return -5;
			}
			return 4;
		} else if (line.startsWith("end")) {
			vs.action[row] = 21;
			vs.val[row] = new String[] {""};
			return 0;
		} else if (line.startsWith("break")) {
			vs.action[row] = 22;
			vs.val[row] = new String[] {""};
			return 0;
		} else if (line.startsWith("return")) {
			vs.action[row] = 23;
			vs.val[row] = new String[] {""};
			return 0;
		} else if (line.startsWith("quit")) {
			vs.action[row] = 24;
			vs.val[row] = new String[] {""};
			return 0;
		} else if (line.startsWith("type")) {
			vs.action[row] = 25;
			if (line.length() < 6) {
				System.out.println("Bad syntax in " + vs.name + " on line " + (row + 1) + ": type must type at least one character");
				vs.val[row] = new String[] {""};
				return -1;
			}
			vs.val[row] = new String[] {line.substring(5, line.length())};
			return 0;
		} else if (line.startsWith("press")) {
			vs.action[row] = 26;
			vs.val[row] = split(line.substring(5, line.length()));
			return 1;
		} else if (line.startsWith("release")) {
			vs.action[row] = 27;
			vs.val[row] = split(line.substring(7, line.length()));
			return 1;
		} else if (line.startsWith("optionon")) {
			vs.action[row] = 28;
			vs.val[row] = split(line.substring(8, line.length()));
			return 1;
		} else if (line.startsWith("optionoff")) {
			vs.action[row] = 29;
			vs.val[row] = split(line.substring(9, line.length()));
			return 1;
		} else if (line.startsWith("printval")) {
			vs.action[row] = 30;
			vs.val[row] = split(line.substring(8, line.length()));
			return 1;
		} else if (line.startsWith("println")) {
			vs.action[row] = 31;
			String message = "";
			if (line.length() > 8)
				message = line.substring(8, line.length());
			vs.val[row] = new String[] {message};
			return 0;
		} else if (line.startsWith("print")) {
			vs.action[row] = 32;
			if (line.length() < 7) {
				System.out.println("Bad syntax in " + vs.name + " on line " + (row + 1) + ": print must print at least one character");
				vs.val[row] = new String[] {""};
				return -1;
			}
			vs.val[row] = new String[] {line.substring(6, line.length())};
			return 0;
		} else if (line.startsWith("[") || line.startsWith("(") || line.startsWith("{")) {
			vs.val[row] = new String[] {"["};
			return 0;
		} else if (line.startsWith("]") || line.startsWith(")") || line.startsWith("}")) {
			vs.val[row] = new String[] {"]"};
			return 0;
		} else if (line.startsWith(":")) {
			vs.val[row] = new String[] {line};
			return 0;
		}
		vs.action[row] = -1;
		vs.val[row] = new String[] {line};
		return 0;
	}
	public static String[] split(String theline) {
		Varslist vl = null;
		Varslist vl2 = null;
		int count = 0;
		int index = 0;
		int begin = 0;
		int length = theline.length();
		while (index < length) {
			while (index < length && (theline.charAt(index) == '\t' || theline.charAt(index) == ' '))
				index = index + 1;
			begin = index;
			while (index < length && theline.charAt(index) != '\t' && theline.charAt(index) != ' ')
				index = index + 1;
			vl2 = new Varslist();
			vl2.name = theline.substring(begin, index);
			vl2.next = vl;
			vl = vl2;
			count = count + 1;
		}
		if (count == 0) {
			count = 1;
			vl = new Varslist();
			vl.name = "";
		}
		String[] list = new String[count];
		for (int spot = count - 1; spot >= 0; spot -= 1) {
			list[spot] = vl.name;
			vl = vl.next;
		}
		return list;
	}
	public static Varslist varcalled(String s, Varslist vl) {
		while (vl != null) {
			if (vl.name.equals(s))
				return vl;
			vl = vl.next;
		}
		vl = gbvars;
		while (vl != null) {
			if (vl.name.equals(s))
				return vl;
			vl = vl.next;
		}
		return null;
	}
	public static String[] subarray(String[] lines, int start, int end) {
		String[] temp = new String[end - start];
		for (int spot = start; spot < end; spot += 1) {
			temp[spot - start] = lines[spot];
		}
		return temp;
	}
	public static void runscript(String[][] lines, int[] actions) {
		int linecount = lines.length;
		while (pos < linecount) {
			scripts = lines[pos];
			action = actions[pos];
//System.out.print(mainscript.name + " @ L" + rows[pos] + "- A" + action + ": ");
//for (int i = 0; i < scripts.length; i += 1) {
//System.out.print(scripts[i] + ", ");
//}
//System.out.println();
			if (action < 10) {
				//skip the line
				if (action == 0)
					;
				//left
				else if (action == 1) {
					ints = intsplit(scripts);
					auto.left(ints[0], ints[1]);
				//right
				} else if (action == 2) {
					ints = intsplit(scripts);
					auto.right(ints[0], ints[1]);
				//move
				} else if (action == 3) {
					ints = intsplit(scripts);
					auto.move(ints[0], ints[1]);
				//pausenot
				} else if (action == 4) {
					ints = intsplit(scripts);
					varcalled = varcalled(scripts[2], null);
					if (varcalled != null && varcalled.type().equals("color")) {
						VarsColor vc = (VarsColor)(varcalled);
						if (ints.length < 4)
							pausenot(ints[0], ints[1], vc.val[0], vc.val[1], vc.val[2], false);
						else {
							waittime = ints[3];
							pausenot(ints[0], ints[1], vc.val[0], vc.val[1], vc.val[2], true);
						}
					} else {
						if (ints.length < 6)
							pausenot(ints[0], ints[1], ints[2], ints[3], ints[4], false);
						else {
							waittime = ints[5];
							pausenot(ints[0], ints[1], ints[2], ints[3], ints[4], true);
						}
					}
				//pause
				} else if (action == 5) {
					ints = intsplit(scripts);
					if (ints.length < 3)
						pause(ints[0]);
					else {
						varcalled = varcalled(scripts[2], null);
						if (varcalled != null && varcalled.type().equals("color")) {
							VarsColor vc = (VarsColor)(varcalled);
							if (ints.length < 4)
								pause(ints[0], ints[1], vc.val[0], vc.val[1], vc.val[2], false);
							else {
								waittime = ints[3];
								pause(ints[0], ints[1], vc.val[0], vc.val[1], vc.val[2], true);
							}
						} else {
							if (ints.length < 6)
								pause(ints[0], ints[1], ints[2], ints[3], ints[4], false);
							else {
								waittime = ints[5];
								pause(ints[0], ints[1], ints[2], ints[3], ints[4], true);
							}
						}
					}
				//jumpback
				} else if (action == 6) {
					if (jumpback != null) {
						VarsInt vi = jumpback;
						jumpback = (VarsInt)(jumpback.next);
						pos = vi.val;
					}
				//jump
				} else if (action == 7)
					pos = inteval(scripts[0]);
				//call
				else if (action == 8) {
					jumpback = new VarsInt("", pos, jumpback);
					pos = inteval(scripts[0]);
				//run
				} else if (action == 9) {
					VarsScript oldmainscript = mainscript;
					String[] oldinput = input;
					Varslist oldgbvars = gbvars;
					VarsInt oldvars = vars;
					int oldpos = pos;
					int[] oldrows = rows;
					mainscript = (VarsScript)(varcalled(scripts[0], scriptlist));
					input = subarray(scripts, 1, scripts.length);
					gbvars = null;
					vars = null;
					pos = 0;
					rows = mainscript.row;
					runscript(mainscript.val, mainscript.action);
					mainscript = oldmainscript;
					input = oldinput;
					gbvars = oldgbvars;
					vars = oldvars;
					pos = oldpos;
					rows = oldrows;
				}
			} else if (action < 19) {
				//create
				if (action == 10) {
					if (scripts[0].equals("int")) {
						gbvars = new VarsInt(scripts[1], intsolve(scripts, 2), gbvars);
					} else if (scripts[0].equals("boolean"))
						gbvars = new VarsBoolean(scripts[1], evaluate(scripts, 2), gbvars);
					else if (scripts[0].equals("color")) {
						varcalled = varcalled(scripts[2], null);
						if (varcalled != null && varcalled.type().equals("color")) {
							VarsColor vc = (VarsColor)(varcalled);
							gbvars = new VarsColor(scripts[1], vc.val, gbvars);
						} else if (scripts[2].equals("at")) {
							ints = auto.colorsat(inteval(scripts[3]), inteval(scripts[4]));
							gbvars = new VarsColor(scripts[1], new int[] {ints[0], ints[1], ints[2]}, gbvars);
						} else {
							ints = intsplit(scripts);
							gbvars = new VarsColor(scripts[1], new int[] {ints[2], ints[3], ints[4]}, gbvars);
						}
					}
				//setargto, set
				} else if (action == 11 || action == 12) {
					if (action == 11) {
						for (int spot = 0; spot < input.length; spot += 1) {
							if (input[spot].startsWith(scripts[0])) {
								scripts = commasplit(scripts[1] + "," + input[spot].substring(scripts[0].length(), input[spot].length()));
								break;
							}
						}
					}
					varcalled = varcalled(scripts[0], vars);
					if (varcalled != null) {
						if (varcalled.type().equals("int")) {
							VarsInt vi = (VarsInt)(varcalled);
							vi.val = intsolve(scripts, 1);
						} else if (varcalled.type().equals("boolean")) {
							VarsBoolean vb = (VarsBoolean)(varcalled);
							vb.val = evaluate(scripts, 1);
						} else if (varcalled.type().equals("color")) {
							VarsColor vc = (VarsColor)(varcalled);
							varcalled = varcalled(scripts[1], null);
							if (varcalled != null && varcalled.type().equals("color")) {
								VarsColor vc2 = (VarsColor)(varcalled);
								vc.val = vc2.val;
							} else if (scripts[1].equals("at")) {
								ints = auto.colorsat(inteval(scripts[2]), inteval(scripts[3]));
								vc.val = new int[] {ints[0], ints[1], ints[2]};
							} else {
								ints = intsplit(scripts);
								vc.val = new int[] {ints[1], ints[2], ints[3]};
							}
						}
					}
				//+=, -=, *=, /=, %=
				} else if (action > 12 && action < 18) {
					varcalled = varcalled(scripts[0], vars);
					if (varcalled != null && varcalled.type().equals("int")) {
						VarsInt vi = (VarsInt)(varcalled);
						if (action == 13)
							vi.val += intsolve(scripts, 1);
						else if (action == 14)
							vi.val -= intsolve(scripts, 1);
						else if (action == 15)
							vi.val *= intsolve(scripts, 1);
						else if (action == 16)
							vi.val /= intsolve(scripts, 1);
						else if (action == 17)
							vi.val %= intsolve(scripts, 1);
					}
				//treset
				} else if (action == 18)
					timer = System.currentTimeMillis();
			} else if (action < 25) {
				//if
				if (action == 19) {
					if (lines[pos + 1][0].startsWith("[")) {
						if (!evaluate(scripts, 0))
							pos = inteval(lines[pos + 1][1]);
						else
							pos = pos + 1;
					} else if (!evaluate(scripts, 0))
						pos = pos + 1;
				//for
				} else if (action == 20) {
					int val = inteval(scripts[1]);
					vars = new VarsInt(scripts[0], val, vars);
				//end
				} else if (action == 21) {
					int jumpline = inteval(scripts[0]);
					vars.val = vars.val + inteval(lines[jumpline][3]);
					if (vars.val > inteval(lines[jumpline][2]))
						vars = (VarsInt)(vars.next);
					else
						pos = jumpline;
				//break
				} else if (action == 22) {
					int layer = 0;
					Varslist vl = vars;
					while (vl != null) {
						layer = layer + 1;
						vl = vl.next;
					}
					while (layer > 0 && pos < linecount) {
						pos = pos + 1;
						if (actions[pos] == 20)
							layer = layer + 1;
						if (actions[pos] == 21)
							layer = layer - 1;
					}
				//return
				} else if (action == 23)
					return;
				//quit
				else if (action == 24)
					end("Quitting script \"" + mainscript.name + "\" from line " + rows[pos] + ".");
			} else {
				//type
				if (action == 25)
					auto.type(scripts[0]);
				//press, release
				else if (action == 26 || action == 27)
					auto.button(scripts[0], action * 2 - 49);
				//optionon
				else if (action == 28) {
					if (scripts[0].equals("quitafter"))
						quitafter = inteval(scripts[1]);
					else if (scripts[0].equals("endingcall")) {
						if (mainscript == scriptlist)
							endingcall = ":" + scripts[1];
					} else if (scripts[0].equals("variance"))
						variance = inteval(scripts[1]);
					else if (scripts[0].equals("mousequit"))
						mousequit = true;
					else if (scripts[0].equals("scanorder")) {
						if (scripts[1].equals("left")) {
							scanorder[0] = 1;
							if (scripts[2].equals("up"))
								scanorder[1] = 2;
							else
								scanorder[1] = 4;
						} else if (scripts[1].equals("up")) {
							scanorder[0] = 2;
							if (scripts[2].equals("left"))
								scanorder[1] = 1;
							else
								scanorder[1] = 3;
						} else if (scripts[1].equals("right")) {
							scanorder[0] = 3;
							if (scripts[2].equals("up"))
								scanorder[1] = 2;
							else
								scanorder[1] = 4;
						} else if (scripts[1].equals("down")) {
							scanorder[0] = 4;
							if (scripts[2].equals("left"))
								scanorder[1] = 1;
							else
								scanorder[1] = 3;
						}
					}
				//optionoff
				} else if (action == 29) {
					if (scripts[0].equals("quitafter"))
						quitafter = -1;
					else if (scripts[0].equals("endingcall")) {
						if (mainscript == scriptlist)
							endingcall = null;
					} else if (scripts[0].equals("variance"))
						variance = 0;
					else if (scripts[0].equals("mousequit"))
						mousequit = false;
					else if (scripts[0].equals("scanorder"))
						scanorder = new int[] {3, 4};
				//printval
				} else if (action == 30) {
					if (scripts[0].startsWith(".")) {
						if (scripts[0].equals(".timestamp"))
							System.out.print(timestamp());
						else if (scripts[0].equals(".mousex"))
							System.out.print("" + mousex());
						else if (scripts[0].equals(".mousey"))
							System.out.print("" + mousey());
						else if (scripts[0].equals(".scanx"))
							System.out.print("" + scanx);
						else if (scripts[0].equals(".scany"))
							System.out.print("" + scany);
						else if (scripts[0].equals(".timer"))
							System.out.print("" + (int)(System.currentTimeMillis() - timer));
					} else {
						varcalled = varcalled(scripts[0], vars);
						if (varcalled != null)
							System.out.print("" + varcalled);
					}
				//println
				} else if (action == 31)
					System.out.println(scripts[0]);
				//print
				else if (action == 32)
					System.out.print(scripts[0]);
			}
			pos = pos + 1;
		}
	}
	public static int[] intsplit(String[] vals) {
		int[] list = new int[vals.length];
		for (int spot = 0; spot < vals.length; spot += 1) {
			list[spot] = inteval(vals[spot]);
		}
		return list;
	}
	public static int inteval(String s) {
		varcalled = varcalled(s, vars);
		if (varcalled != null && varcalled.type().equals("int")) {
			VarsInt vi = (VarsInt)(varcalled);
			return vi.val;
		} else if (s.startsWith(".")) {
			if (s.equals(".mousex"))
				return mousex();
			else if (s.equals(".mousey"))
				return mousey();
			else if (s.equals(".scanx"))
				return scanx;
			else if (s.equals(".scany"))
				return scany;
			else if (s.equals(".timer"))
				return (int)(System.currentTimeMillis() - timer);
		}
		return numbers.readint(s);
	}
	public static int intsolve(String[] vals, int off) {
		int length = vals.length;
		ints = new int[length];
		int add = 0;
		for (int spot = length - 1; spot >= off; spot -= 1) {
			if (vals[spot].startsWith(" ")) {
				if (vals[spot].equals(" +")) {
					ints[spot] = ints[spot + 1] + ints[spot + 2];
					add = 2;
				} else if (vals[spot].equals(" -")) {
					ints[spot] = ints[spot + 1] - ints[spot + 2];
					add = 2;
				} else if (vals[spot].equals(" *")) {
					ints[spot] = ints[spot + 1] * ints[spot + 2];
					add = 2;
				} else if (vals[spot].equals(" /")) {
					ints[spot] = ints[spot + 1] / ints[spot + 2];
					add = 2;
				} else if (vals[spot].equals(" %")) {
					ints[spot] = ints[spot + 1] % ints[spot + 2];
					add = 2;
				} else if (vals[spot].equals(" random")) {
					ints[spot] = (int)(Math.random() * ints[spot + 1]);
					add = 1;
				}
				length = length - add;
				for (int loc = spot + 1; loc < length; loc += 1) {
					vals[loc] = vals[loc + add];
					ints[loc] = ints[loc + add];
				}
			} else
				ints[spot] = inteval(vals[spot]);
		}
		return ints[off];
	}
	public static String[] commasplit(String theline) {
		Varslist vl = null;
		Varslist vl2 = null;
		int count = 0;
		int index = 0;
		int begin = 0;
		int length = theline.length();
		while (index < length) {
			while (index < length && theline.charAt(index) == ',')
				index = index + 1;
			begin = index;
			while (index < length && theline.charAt(index) != ',')
				index = index + 1;
			vl2 = new Varslist();
			vl2.name = theline.substring(begin, index);
			vl2.next = vl;
			vl = vl2;
			if (	vl.name.equals("not") ||
				vl.name.equals("input") ||
				vl.name.equals("or") ||
				vl.name.equals("and") ||
				vl.name.equals("==") ||
				vl.name.equals("!=") ||
				vl.name.equals(">=") ||
				vl.name.equals(">") ||
				vl.name.equals("<=") ||
				vl.name.equals("<") ||
				vl.name.equals("colorat") ||
				vl.name.equals("scan") ||
				vl.name.equals("+") ||
				vl.name.equals("-") ||
				vl.name.equals("*") ||
				vl.name.equals("/") ||
				vl.name.equals("%") ||
				vl.name.equals("random"))
				vl.name = " " + vl.name;
			count = count + 1;
		}
		count = Math.max(1, count);
		String[] list = new String[count];
		for (int spot = count - 1; spot >= 0; spot -= 1) {
			list[spot] = vl.name;
			vl = vl.next;
		}
		return list;
	}
	public static boolean evaluate(String[] vals, int off) {
		int length = vals.length;
		ints = new int[length];
		VarsBoolean vb = null;
		VarsInt vi = null;
		VarsColor vc = null;
		int add = 0;
		for (int spot = length - 1; spot >= off; spot -= 1) {
			if (vals[spot].startsWith(" ")) {
				if (vals[spot].equals(" not")) {
					ints[spot] = 1 - ints[spot + 1];
					add = 1;
				} else if (vals[spot].equals(" input")) {
					for (int loc = 0; loc < input.length; loc += 1) {
						if (input[loc].equals(vals[spot + 1])) {
							ints[spot] = 1;
							break;
						}
					}
					add = 1;
				} else if (vals[spot].equals(" or")) {
					ints[spot] = ints[spot + 1] | ints[spot + 2];
					add = 2;
				} else if (vals[spot].equals(" and")) {
					ints[spot] = ints[spot + 1] & ints[spot + 2];
					add = 2;
				} else if (vals[spot].equals(" ==")) {
					if (ints[spot + 1] == ints[spot + 2])
						ints[spot] = 1;
					add = 2;
				} else if (vals[spot].equals(" !=")) {
					if (ints[spot + 1] != ints[spot + 2])
						ints[spot] = 1;
					add = 2;
				} else if (vals[spot].equals(" >=")) {
					if (ints[spot + 1] >= ints[spot + 2])
						ints[spot] = 1;
					add = 2;
				} else if (vals[spot].equals(" >")) {
					if (ints[spot + 1] > ints[spot + 2])
						ints[spot] = 1;
					add = 2;
				} else if (vals[spot].equals(" <=")) {
					if (ints[spot + 1] <= ints[spot + 2])
						ints[spot] = 1;
					add = 2;
				} else if (vals[spot].equals(" <")) {
					if (ints[spot + 1] < ints[spot + 2])
						ints[spot] = 1;
					add = 2;
				} else if (vals[spot].equals(" colorat")) {
					varcalled = varcalled(vals[spot + 3], null);
					if (varcalled != null && varcalled.type().equals("color")) {
						vc = (VarsColor)(varcalled);
						if (colorat(ints[spot + 1], ints[spot + 2], vc.val[0], vc.val[1], vc.val[2]))
							ints[spot] = 1;
						add = 3;
					} else {
						if (colorat(ints[spot + 1], ints[spot + 2], ints[spot + 3], ints[spot + 4], ints[spot + 5]))
							ints[spot] = 1;
						add = 5;
					}
				} else if (vals[spot].equals(" scan")) {
					scanx = 0;
					scany = 0;
					BufferedImage image = auto.screenshot(ints[spot + 1], ints[spot + 2], ints[spot + 3], ints[spot + 4]);
					int pixel = 0;
					int r = 0;
					int g = 0;
					int b = 0;
					int ir = 0;
					int ig = 0;
					int ib = 0;
					varcalled = varcalled(vals[spot + 5], null);
					if (varcalled != null && varcalled.type().equals("color")) {
						vc = (VarsColor)(varcalled);
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
					int[] locs = new int[] {0, 0};
					int[] adds = new int[] {1, 1};
					int[] maxs = null;
					int[] gets = null;
//up or down is first
					if (scanorder[0] % 2 == 0) {
						maxs = new int[] {image.getHeight(), image.getWidth()};
						gets = new int[] {1, 0};
//left or right is first
					} else {
						maxs = new int[] {image.getWidth(), image.getHeight()};
						gets = new int[] {0, 1};
					}
					if (scanorder[0] < 3) {
						adds[0] = -1;
						locs[0] = maxs[0] - 1;
					}
					if (scanorder[1] < 3) {
						adds[1] = -1;
						locs[1] = maxs[1] - 1;
					}
					int[] coords = new int[] {locs[0], locs[1]};
					outer: for (int m = 0; m < maxs[1]; m += 1) {
						for (int n = 0; n < maxs[0]; n += 1) {
							pixel = image.getRGB(coords[gets[0]], coords[gets[1]]);
							r = (pixel >> 16) & 255;
							g = (pixel >> 8) & 255;
							b = pixel & 255;
							if (r <= ir + variance && r >= ir - variance && g <= ig + variance && g >= ig - variance && b <= ib + variance && b >= ib - variance) {
								scanx = ints[spot + 1] + coords[gets[0]];
								scany = ints[spot + 2] + coords[gets[1]];
								ints[spot] = 1;
								break outer;
							}
							coords[0] = coords[0] + adds[0];
						}
						coords[1] = coords[1] + adds[1];
						coords[0] = locs[0];
					}
				}
				length = length - add;
				for (int loc = spot + 1; loc < length; loc += 1) {
					vals[loc] = vals[loc + add];
					ints[loc] = ints[loc + add];
				}
			} else {
				varcalled = varcalled(vals[spot], vars);
				if (varcalled != null) {
					if (varcalled.type().equals("boolean")) {
						vb = (VarsBoolean)(varcalled);
						if (vb.val)
							ints[spot] = 1;
					} else if (varcalled.type().equals("int")) {
						vi = (VarsInt)(varcalled);
						ints[spot] = vi.val;
					} else if (varcalled.type().equals("color")) {
						vc = (VarsColor)(varcalled);
						ints[spot] = (vc.val[0] << 16) + (vc.val[1] << 8) + vc.val[2];
					}
				} else if (vals[spot].equals("true"))
					ints[spot] = 1;
				else if (vals[spot].equals("false"))
					ints[spot] = 0;
				else if (vals[spot].startsWith(".")) {
					if (vals[spot].equals(".mousex"))
						ints[spot] = mousex();
					else if (vals[spot].equals(".mousey"))
						ints[spot] = mousey();
					else if (vals[spot].equals(".scanx"))
						ints[spot] = scanx;
					else if (vals[spot].equals(".scany"))
						ints[spot] =  scany;
					else if (vals[spot].equals(".timer"))
						ints[spot] = (int)(System.currentTimeMillis() - timer);
				} else
					ints[spot] = numbers.readint(vals[spot]);
			}
		}
		return ints[off] != 0;
	}
	public static void pause(int time) {
		xx = mousex();
		yy = mousey();
		long now = System.currentTimeMillis();
		while (System.currentTimeMillis() - now < time) {
			if (mousequit && (xx != mousex() || yy != mousey()))
				end("You moved the mouse.");
		}
	}
	public static void pause(int x, int y, int r, int g, int b, boolean endearly) {
		xx = mousex();
		yy = mousey();
		long now = System.currentTimeMillis();
		while (!colorat(x, y, r, g, b) && (!endearly || System.currentTimeMillis() - now < waittime)) {
			if (mousequit && (xx != mousex() || yy != mousey()))
				end("You moved the mouse.");
			if (quitafter >= 0 && System.currentTimeMillis() - now >= quitafter)
				end("Quitting a pause from script \"" + mainscript.name + "\" at line " + rows[pos] + ".");
		}
	}
	public static void pausenot(int x, int y, int r, int g, int b, boolean endearly) {
		xx = mousex();
		yy = mousey();
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
		if (endingcall != null) {
			pos = 0;
			int linecount = scriptlist.val.length;
			while (pos < linecount && !scriptlist.val[pos][0].equals(endingcall))
				pos = pos + 1;
			pos = pos + 1;
			while (pos < linecount) {
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
						else if (scriptlist.val[pos][0].equals(".timer"))
							System.out.print("" + (int)(System.currentTimeMillis() - timer));
					} else {
						varcalled = varcalled(scriptlist.val[pos][0], vars);
						if (varcalled != null)
							System.out.print("" + varcalled);
					}
				//println
				} else if (scriptlist.action[pos] == 31)
					System.out.println(scriptlist.val[pos][0]);
				//print
				else if (scriptlist.action[pos] == 32)
					System.out.print(scriptlist.val[pos][0]);
				pos = pos + 1;
			}
		}
		System.exit(0);
	}
}