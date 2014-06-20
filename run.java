import java.util.Calendar;
import java.awt.image.BufferedImage;
import java.awt.MouseInfo;
import java.io.File;
//TO ADD:
//perhaps expressions, eventually
public class run {
	public static String timestamp() {
		Calendar c = Calendar.getInstance();
		int second = c.get(Calendar.SECOND);
		int minute = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int day = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		return (hour + ":" + minute + ":" + second + " " + day + "/" + month + "/" + year);
	}
	public static int mousex() {
		return MouseInfo.getPointerInfo().getLocation().x;
	}
	public static int mousey() {
		return MouseInfo.getPointerInfo().getLocation().y;
	}
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
	public static int xx;
	public static int yy;
	public static Autoer auto;
	public static int waittime = 0;
	public static String[] input = null;
	public static Varslist gbvars = null;
	public static VarsInt vars = null;
	public static int variance = 5;
	public static boolean keepgoing = true;
	public static boolean runloop = true;
	public static String scriptname = "";
	public static long timer = 0;
	public static Varslist varcalled = null;
	public static int quitafter = -1;
	public static boolean endingtimestamp = false;
	public static VarsInt jumpback = null;
	public static boolean mousequit = true;
	public static int scanx = 0;
	public static int scany = 0;
	public static String[] scripts = null;
	public static int[] ints = null;
	public static int pos = 0;
	public static VarsScript scriptlist = null;
	public static int action = 0;
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
			return ("" + val);
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
			return ("" + val);
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
			return (val[0] + "," + val[1] + "," + val[2]);
		}
	}
	public static class VarsScript extends Varslist {
		String[][] val;
		int[] action;
		VarsScript(String n, int lines, Varslist vl) {
			name = n;
			val = new String[lines][0];
			action = new int[lines];
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
		scriptname = args[0];
		VarsScript mainscript = new VarsScript(scriptname, lines.length, null);
		scriptlist = mainscript;
		verify(lines);
		input = subarray(args, 1, args.length);
		try {
			timer = System.currentTimeMillis();
			runscript(mainscript.val, mainscript.action);
		} catch(Exception e) {
			end("Error: script \"" + scriptname + "\" at line " + (pos + 1) + " caused this exception:\n" + e);
		}
		if (endingtimestamp && scriptname.equals(""))
			System.out.println("Ended at " + timestamp() + ".");
	}
	public static void verify(String[] lines) {
		VarsScript putter = scriptlist;
		String[] bits;
		int argnum;
		int linecount = lines.length;
		for (int row = 0; row < lines.length; row += 1) {
			argnum = build(putter, clip(lines[row]), row);
			bits = putter.val[row];
			if (argnum > bits.length)
				end("Bad syntax in " + scriptname + " on line " + (row + 1) + ": only " + bits.length + " arguments instead of " + argnum + ".");
			if (putter.action[row] == 9 && varcalled(bits[0], scriptlist) == null) {
				Filer inner = new Filer(filefor("scripts", bits[0] + ".txt"));
				inner.readfile();
				scriptlist = new VarsScript(bits[0], inner.lines(), scriptlist);
				verify(inner.getlines());
			} else if (putter.action[row] == -1)
				linecount = linecount - 1;
		}
		if (linecount < lines.length) {
			String[][] newval = new String[linecount][0];
			int[] newaction = new int[linecount];
			int index = 0;
			linecount = lines.length;
			for (int spot = 0; spot < linecount; spot += 1) {
				if (putter.action[spot] != -1) {
					newval[index] = putter.val[spot];
					newaction[index] = putter.action[spot];
					index = index + 1;
				}
			}
			putter.val = newval;
			putter.action = newaction;
		}
	}
	public static int build(VarsScript vs, String line, int row) {
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
			if (vs.val[row].length > 1 && vs.val[row][1].startsWith("."))
				end("Bad syntax in " + scriptname + " on line " + (row + 1) + ": variables cannot start with \".\"");
			return 3;
		} else if (line.startsWith("setargto")) {
			vs.action[row] = 11;
			vs.val[row] = split(line.substring(8, line.length()));
			return 2;
		} else if (line.startsWith("set")) {
			vs.action[row] = 12;
			vs.val[row] = split(line.substring(3, line.length()));
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
			return 0;
		} else if (line.startsWith("if")) {
			vs.action[row] = 19;
			vs.val[row] = split(line.substring(2, line.length()));
			return 1;
		} else if (line.startsWith("for")) {
			vs.action[row] = 20;
			vs.val[row] = split(line.substring(3, line.length()));
			if (vs.val[row].length > 1 && vs.val[row][1].startsWith("."))
				end("Bad syntax in " + scriptname + " on line " + (row + 1) + ": variables cannot start with \".\"");
			return 4;
		} else if (line.startsWith("break")) {
			vs.action[row] = 21;
			return 0;
		} else if (line.startsWith("return")) {
			vs.action[row] = 22;
			return 0;
		} else if (line.startsWith("quit")) {
			vs.action[row] = 23;
			return 0;
		} else if (line.startsWith("type")) {
			vs.action[row] = 24;
			if (line.length() < 6)
				end("Bad syntax in " + scriptname + " on line " + (row + 1) + ": type must type at least one character.");
			vs.val[row] = new String[] {line.substring(5, line.length())};
			return 0;
		} else if (line.startsWith("press")) {
			vs.action[row] = 25;
			vs.val[row] = split(line.substring(5, line.length()));
			return 1;
		} else if (line.startsWith("release")) {
			vs.action[row] = 26;
			vs.val[row] = split(line.substring(7, line.length()));
			return 1;
		} else if (line.startsWith("optionon")) {
			vs.action[row] = 27;
			vs.val[row] = split(line.substring(8, line.length()));
			return 1;
		} else if (line.startsWith("optionoff")) {
			vs.action[row] = 28;
			vs.val[row] = split(line.substring(9, line.length()));
			return 1;
		} else if (line.startsWith("printval")) {
			vs.action[row] = 29;
			vs.val[row] = split(line.substring(8, line.length()));
			return 1;
		} else if (line.startsWith("println")) {
			vs.action[row] = 30;
			return 0;
		} else if (line.startsWith("print")) {
			vs.action[row] = 31;
			if (line.length() < 7)
				end("Bad syntax in " + scriptname + " on line " + (row + 1) + ": print must print at least one character.");
			vs.val[row] = new String[] {line.substring(6, line.length())};
			return 0;
		} else if (line.startsWith("end") || line.startsWith(":")) {
			vs.val[row] = new String[] {line};
			return 0;
		}
		vs.action[row] = -1;
		return 0;
	}
	public static String clip(String theline) {
		int spot = 0;
		int length = theline.length();
		while (spot < length && (theline.charAt(spot) == ' ' || theline.charAt(spot) == '\t'))
			spot = spot + 1;
		return theline.substring(spot, theline.length());
	}
	public static String[] split(String theline) {
		theline = clip(theline);
		int count = 0;
		int index = 0;
		int length = theline.length();
		while (index < length) {
			while (index < length && (theline.charAt(index) != '\t' && theline.charAt(index) != ' '))
				index = index + 1;
			count = count + 1;
			while (index < length && (theline.charAt(index) == '\t' || theline.charAt(index) == ' '))
				index = index + 1;
		}
		String[] list = new String[count];
		index = 0;
		for (int spot = 0; spot < count; spot += 1) {
			while (index < length && (theline.charAt(index) != '\t' && theline.charAt(index) != ' '))
				index = index + 1;
			list[spot] = theline.substring(0, index);
			theline = clip(theline.substring(index, length));
			index = 0;
			length = theline.length();
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
		while (pos < linecount && keepgoing && runloop) {
			scripts = lines[pos];
			action = actions[pos];
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
				//jump, call
				} else if (action == 7 || action == 8) {
					int jumpline = 0;
					while (jumpline < linecount) {
						if (lines[jumpline][0].equals(":" + scripts[0])) {
							if (action == 8)
								jumpback = new VarsInt("", pos, jumpback);
							pos = jumpline;
							break;
						}
						jumpline = jumpline + 1;
					}
				//run
				} else if (action == 9) {
					VarsScript vs = (VarsScript)(varcalled(scripts[0], scriptlist));
					String oldscriptname = scriptname;
					String[] oldinput = input;
					Varslist oldgbvars = gbvars;
					VarsInt oldvars = vars;
					int oldpos = pos;
					scriptname = scripts[0];
					input = subarray(scripts, 1, scripts.length);
					gbvars = null;
					vars = null;
					pos = 0;
					runscript(vs.val, vs.action);
					keepgoing = true;
					input = oldinput;
					gbvars = oldgbvars;
					scriptname = oldscriptname;
					pos = oldpos;
					vars = oldvars;
				}
			} else if (action < 19) {
				//create
				if (action == 10) {
					if (scripts[0].equals("int")) {
						if (scripts[2].equals("random")) {
							int rand = (int)(Math.random() * inteval(scripts[3]));
							gbvars = new VarsInt(scripts[1], rand, gbvars);
						} else
							gbvars = new VarsInt(scripts[1], inteval(scripts[2]), gbvars);
					} else if (scripts[0].equals("boolean"))
						gbvars = new VarsBoolean(scripts[1], evaluate(subarray(scripts, 2, scripts.length)), gbvars);
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
							if (scripts[1].equals("random"))
								vi.val = (int)(Math.random() * inteval(scripts[2]));
							else
								vi.val = inteval(scripts[1]);
						} else if (varcalled.type().equals("boolean")) {
							VarsBoolean vb = (VarsBoolean)(varcalled);
							vb.val = evaluate(subarray(scripts, 1, scripts.length));
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
							vi.val += inteval(scripts[1]);
						else if (action == 14)
							vi.val -= inteval(scripts[1]);
						else if (action == 15)
							vi.val *= inteval(scripts[1]);
						else if (action == 16)
							vi.val /= inteval(scripts[1]);
						else if (action == 17)
							vi.val %= inteval(scripts[1]);
					}
				//treset
				} else if (action == 18)
					timer = System.currentTimeMillis();
			} else if (action < 24) {
				//if
				if (action == 19) {
					if (!evaluate(scripts))
						pos = pos + 1;
				//for
				} else if (action == 20) {
					ints = intsplit(scripts);
					int jumpline = pos + 1;
					int layer = 1;
					while (jumpline < linecount) {
						if (actions[jumpline] == 20)
							layer = layer + 1;
						else if (lines[jumpline][0].startsWith("end"))
							layer = layer - 1;
						if (layer == 0)
							break;
						jumpline = jumpline + 1;
					}
					String[][] sublines = subarrayscript(lines, pos + 1, jumpline);
					int[] subactions = subarrayint(actions, pos + 1, jumpline);
					vars = new VarsInt(scripts[0], ints[1], vars);
					int[] oldints = ints;
					while (vars.val <= oldints[2] && runloop && keepgoing) {
						runscript(sublines, subactions);
						vars.val = vars.val + oldints[3];
					}
					vars = (VarsInt)(vars.next);
					runloop = true;
					pos = jumpline;
				//break
				} else if (action == 21)
					runloop = false;
				//return
				else if (action == 22)
					keepgoing = false;
				//quit
				else if (action == 23)
					end("Quitting script \"" + scriptname + "\" from line " + (pos + 1) + ".");
			} else {
				//type
				if (action == 24)
					auto.type(scripts[0]);
				//press, release
				else if (action == 25 || action == 26)
					auto.button(scripts[0], action * 2 - 49);
				//optionon
				else if (action == 27) {
					if (scripts[0].equals("quitafter"))
						quitafter = inteval(scripts[1]);
					else if (scripts[0].equals("endingtimestamp"))
						endingtimestamp = true;
					else if (scripts[0].equals("variance"))
						variance = inteval(scripts[1]);
					else if (scripts[0].equals("mousequit"))
						mousequit = true;
				//optionoff
				} else if (action == 28) {
					if (scripts[0].equals("quitafter"))
						quitafter = -1;
					else if (scripts[0].equals("endingtimestamp"))
						endingtimestamp = false;
					else if (scripts[0].equals("variance"))
						variance = 0;
					else if (scripts[0].equals("mousequit"))
						mousequit = false;
				//printval
				} else if (action == 29) {
					if (scripts[0].equals(".timestamp"))
						System.out.print(timestamp());
					else {
						varcalled = varcalled(scripts[0], vars);
						if (varcalled != null)
							System.out.print("" + varcalled);
					}
				//println
				} else if (action == 30)
					System.out.println();
				//print
				else if (action == 31)
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
				return mousex();
			else if (s.equals(".scany"))
				return mousey();
			else if (s.equals(".timer"))
				return (int)(System.currentTimeMillis() - timer);
		}
		return numbers.readint(s);
	}
	public static String[] commasplit(String theline) {
		int count = 0;
		int index = 0;
		int length = theline.length();
		while (index < length) {
			while (index < length && theline.charAt(index) != ',')
				index = index + 1;
			count = count + 1;
			while (index < length && theline.charAt(index) == ',')
				index = index + 1;
		}
		String[] list = new String[count];
		index = 0;
		for (int spot = 0; spot < count; spot += 1) {
			while (index < length && theline.charAt(index) != ',')
				index = index + 1;
			list[spot] = theline.substring(0, index);
			theline = theline.substring(index, length);
			index = 0;
			length = theline.length();
		}
		return list;
	}
	public static boolean evaluate(String[] vals) {
		if (vals[0].equals("not"))
			return !evaluate(subarray(vals, 1, vals.length));
		else if (vals[0].equals("true"))
			return true;
		else if (vals[0].equals("input")) {
			for (int spot = 0; spot < input.length; spot += 1) {
				if (input[spot].equals(vals[1]))
					return true;
			}
		} else if (vals[0].equals("==")) {
			ints = intsplit(vals);
			return ints[1] == ints[2];
		} else if (vals[0].equals(">=")) {
			ints = intsplit(vals);
			return ints[1] >= ints[2];
		} else if (vals[0].equals(">")) {
			ints = intsplit(vals);
			return ints[1] > ints[2];
		} else if (vals[0].equals("<=")) {
			ints = intsplit(vals);
			return ints[1] <= ints[2];
		} else if (vals[0].equals("<")) {
			ints = intsplit(vals);
			return ints[1] < ints[2];
		} else if (vals[0].equals("colorat")) {
			ints = intsplit(vals);
			varcalled = varcalled(vals[3], null);
			if (varcalled != null && varcalled.type().equals("color")) {
				VarsColor vc = (VarsColor)(varcalled);
				ints = new int[] {0, ints[1], ints[2], vc.val[0], vc.val[1], vc.val[2]};
			}
			return colorat(ints[1], ints[2], ints[3], ints[4], ints[5]);
		} else if (vals[0].equals("scan")) {
			scanx = 0;
			scany = 0;
			ints = intsplit(vals);
			BufferedImage image = auto.screenshot(ints[1], ints[2], ints[3], ints[4]);
			int width = image.getWidth();
			int height = image.getHeight();
			int pixel = 0;
			int r = 0;
			int g = 0;
			int b = 0;
			varcalled = varcalled(vals[5], null);
			if (varcalled != null && varcalled.type().equals("color")) {
				VarsColor vc = (VarsColor)(varcalled);
				ints = new int[] {0, ints[1], ints[2], ints[3], ints[4], vc.val[0], vc.val[1], vc.val[2]};
			}
			int ir = ints[5];
			int ig = ints[6];
			int ib = ints[7];
			for (int y = 0; y < height; y += 1) {
				for (int x = 0; x < width; x += 1) {
					pixel = image.getRGB(x, y);
					r = (pixel >> 16) & 255;
					g = (pixel >> 8) & 255;
					b = pixel & 255;
					if (r <= ir + variance && r >= ir - variance && g <= ig + variance && g >= ig - variance && b <= ib + variance && b >= ib - variance) {
						scanx = x;
						scany = y;
						return true;
					}
				}
			}
		}
		varcalled = varcalled(vals[0], null);
		if (varcalled != null && varcalled.type().equals("boolean")) {
			VarsBoolean vb = (VarsBoolean)(varcalled);
			return vb.val;
		}
		return false;
	}
	public static String[][] subarrayscript(String[][] lines, int start, int end) {
		String[][] temp = new String[end - start][0];
		for (int spot = start; spot < end; spot += 1) {
			temp[spot - start] = lines[spot];
		}
		return temp;
	}
	public static int[] subarrayint(int[] lines, int start, int end) {
		int[] temp = new int[end - start];
		for (int spot = start; spot < end; spot += 1) {
			temp[spot - start] = lines[spot];
		}
		return temp;
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
				end("Quitting a pause from script \"" + scriptname + "\" at line " + (pos + 1) + ".");
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
				end("Quitting a pausenot from script \"" + scriptname + "\" at line " + (pos + 1) + ".");
		}
	}
	public static boolean colorat(int x, int y, int r, int g, int b) {
		int[] cs = auto.colorsat(x, y);
		return (cs[0] <= r + variance && cs[0] >= r - variance && cs[1] <= g + variance && cs[1] >= g - variance && cs[2] <= b + variance && cs[2] >= b - variance);
	}
	public static void end(String s) {
		System.out.println(s);
		end();
	}
	public static void end() {
		if (endingtimestamp)
			System.out.println("Ended at " + timestamp() + ".");
		System.exit(0);
	}
}