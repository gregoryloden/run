import java.awt.MouseInfo;
public class run {
	public static int mousex() {
		return MouseInfo.getPointerInfo().getLocation().x;
	}
	public static int mousey() {
		return MouseInfo.getPointerInfo().getLocation().y;
	}
	public static int xx;
	public static int yy;
	public static Autoer auto;
	public static int waittime = 5000;
	public static String[] input = null;
	public static Varslist gbvars = null;
	public static int variance = 10;
	public static boolean keepgoing = true;
	public static boolean runloop = true;
	public static String scriptname = "";
	public static class Varslist {
		String type;
		String name;
		String val;
		Varslist next;
		Varslist(String t, String n, String v, Varslist vl) {
			type = t;
			name = n;
			val = v;
			next = vl;
		}
		public static String varval(Varslist vl, String n) {
			if (vl == null)
				return "";
			if (vl.name.equals(n))
				return vl.val;
			return varval(vl.next, n);
		}
		public static boolean change(Varslist vl, String n, String o, int v) {
			if (vl == null)
				return false;
			if (vl.name.equals(n) && vl.type.startsWith("int")) {
				int i = numbers.readint(vl.val);
				if (o.startsWith("-="))
					i -= v;
				else if (o.startsWith("*="))
					i *= v;
				else if (o.startsWith("/="))
					i /= v;
				else
					i += v;
				vl.val = "" + i;
				return true;
			}
			return change(vl.next, n, o, v);
		}
		public static boolean change(Varslist vl, String n, String o, String v) {
			if (vl == null)
				return false;
			if (vl.name.equals(n)) {
				//To be filled in later
				return false;
			}
			return change(vl.next, n, o, v);
		}
		public static boolean set(Varslist vl, String n, int v) {
			if (vl == null)
				return false;
			if (vl.name.equals(n) && vl.type.startsWith("int")) {
				vl.val = "" + v;
				return true;
			}
			return set(vl.next, n, v);
		}
		public static boolean set(Varslist vl, String n, String v) {
			if (vl == null)
				return false;
			if (vl.name.equals(n)) {
				vl.val = v;
				return true;
			}
			return set(vl.next, n, v);
		}
	}
	public static void main(String[] args) {
		auto = new Autoer();
		if (args.length < 1)
			end("You need a script to run.");
		Filer filer = new Filer("scripts\\" + args[0] + ".txt");
		if (!filer.fileisthere())
			end("Script \"" + args[0] + "\" does not exist.");
		filer.readfile();
		String[] lines = filer.getlines();
		verify(lines);
		String[] oldinput = input;
		Varslist oldgbvars = gbvars;
		String oldscriptname = scriptname;
		input = subarray(args, 1, args.length - 1);
		gbvars = null;
		scriptname = args[0];
		runscript(lines, null);
		keepgoing = true;
		input = oldinput;
		gbvars = oldgbvars;
		scriptname = oldscriptname;
	}
	public static void verify(String[] lines) {
		String[] bits;
		for (int row = 0; row < lines.length; row += 1) {
			bits = split(lines[row]);
			if (bits.length >= 2) {
				if (bits[1].startsWith(".") && (bits[0].startsWith("create") || bits[0].startsWith("for")))
					end("Invalid script: line " + row + ", variables cannot start with \".\"");
			}
			if (bits.length >= 3) {
				if (bits[2].startsWith(".") && bits[0].startsWith("setargto")) {
					end("Invalid script: line " + row + ", variables cannot start with \".\"");
				}
			}
			if (bits.length > 0) {
				if (argnum(bits[0]) > bits.length - 1)
					end("Bad syntax on line " + row + ": only " + (bits.length - 1) + " arguments instead of " + argnum(bits[0]) + ".");
				if (bits[0].startsWith("run")) {
					Filer inner = new Filer("scripts\\" + bits[1] + ".txt");
					inner.readfile();
					verify(inner.getlines());
				}
			}
		}
	}
	public static int argnum(String line) {
		if (	line.startsWith("pausenot"))
			return 5;
		if (	line.startsWith("for"))
			return 4;
		if (	line.startsWith("create") ||
			line.startsWith("setargto"))
			return 3;
		if (	line.startsWith("left") ||
			line.startsWith("right") ||
			line.startsWith("move") ||
			line.startsWith("+=") ||
			line.startsWith("-=") ||
			line.startsWith("*=") ||
			line.startsWith("/=") ||
			line.startsWith("set"))
			return 2;
		if (	line.startsWith("run") ||
			line.startsWith("jump") ||
			line.startsWith("if") ||
			line.startsWith("print") ||
			line.startsWith("pause"))
			return 1;
		return 0;
	}
	public static void runscript(String[] lines, Varslist vars) {
		String line = "";
		int pos = 0;
		int[] ints;
		String[] scripts;
		int linecount = lines.length;
		while (pos < linecount && keepgoing) {
			line = clip(lines[pos]);
			if (line.startsWith("left")) {
				ints = intsplit(split(line.substring(4, line.length())), vars);
				auto.left(ints[0], ints[1]);
			} else if (line.startsWith("right")) {
				ints = intsplit(split(line.substring(5, line.length())), vars);
				auto.right(ints[0], ints[1]);
			} else if (line.startsWith("move")) {
				ints = intsplit(split(line.substring(4, line.length())), vars);
				auto.move(ints[0], ints[1]);
			} else if (line.startsWith("pausenot")) {
				ints = intsplit(split(line.substring(8, line.length())), vars);
				pausenot(ints[0], ints[1], ints[2], ints[3], ints[4], false);
			} else if (line.startsWith("pause")) {
				ints = intsplit(split(line.substring(5, line.length())), vars);
				if (ints.length < 5)
					pause(ints[0]);
				else
					pause(ints[0], ints[1], ints[2], ints[3], ints[4], false);
			} else if (line.startsWith("run"))
				main(split(line.substring(3, line.length())));
			else if (line.startsWith("jump")) {
				scripts = split(line.substring(4, line.length()));
				int jumpline = 0;
				while (jumpline < linecount) {
					if (clip(lines[jumpline]).startsWith(":" + scripts[0])) {
						pos = jumpline - 1;
						break;
					}
					jumpline = jumpline + 1;
				}
			} else if (line.startsWith("for")) {
				scripts = split(line.substring(3, line.length()));
				ints = intsplit(scripts, vars);
				int jumpline = pos + 1;
				int layer = 1;
				String clipline = "";
				while (jumpline < linecount) {
					clipline = clip(lines[jumpline]);
					if (clipline.startsWith("for"))
						layer = layer + 1;
					else if (clipline.startsWith("end"))
						layer = layer - 1;
					if (layer == 0)
						break;
					jumpline = jumpline + 1;
				}
				String[] subarray = subarray(lines, pos + 1, jumpline - 1);
				Varslist newvars = new Varslist("int", scripts[0], "" + ints[1], vars);
				while (numbers.readint(Varslist.varval(newvars, scripts[0])) <= ints[2] && runloop) {
					runscript(subarray, newvars);
					Varslist.change(newvars, scripts[0], "+=", ints[3]);
				}
				runloop = true;
				pos = jumpline;
			} else if (line.startsWith("if")) {
				if (!evaluate(split(line.substring(2, line.length())), vars))
					pos = pos + 1;
			} else if (line.startsWith("create")) {
				scripts = split(line.substring(6, line.length()));
				if (scripts[0].startsWith("int"))
					gbvars = new Varslist("int", scripts[1], "" + inteval(scripts[2], vars), gbvars);
				else
					gbvars = new Varslist(scripts[0], scripts[1], scripts[2], gbvars);
			} else if (line.startsWith("setargto")) {
				scripts = split(line.substring(8, line.length()));
				for (int spot = 0; spot < input.length; spot += 1) {
					if (input[spot].startsWith(scripts[0])) {
						gbvars = new Varslist(scripts[1], scripts[2], input[spot].substring(scripts[0].length(), input[spot].length()), gbvars);
						break;
					}
				}
			} else if (line.startsWith("+=") || line.startsWith("-=") || line.startsWith("*=") || line.startsWith("/=")) {
				scripts = split(line);
				ints = intsplit(scripts, vars);
				if (!Varslist.change(vars, scripts[1], scripts[0], ints[2]))
					Varslist.change(gbvars, scripts[1], scripts[0], ints[2]);
			} else if (line.startsWith("set")) {
				scripts = split(line.substring(3, line.length()));
				ints = intsplit(scripts, vars);
				if (Varslist.set(vars, scripts[0], ints[1]))
					;
				else if (Varslist.set(gbvars, scripts[0], ints[1]))
					;
				else if (Varslist.set(vars, scripts[0], scripts[1]))
					;
				else if (Varslist.set(gbvars, scripts[0], scripts[1]))
					;
			} else if (line.startsWith("println")) {
				if (line.length() > 8)
					System.out.println(line.substring(8, line.length()));
				else
					System.out.println();
			} else if (line.startsWith("print"))
				System.out.print(line.substring(6, line.length()));
			else if (line.startsWith("break")) {
				runloop = false;
				break;
			} else if (line.startsWith("return")) {
				keepgoing = false;
				return;
			} else if (line.startsWith("quit"))
				end("Quitting script \"" + scriptname + "\" from line " + (pos + 1) + ".");
			pos = pos + 1;
		}
	}
//evaluate: used to check the value of the "if" clauses
//commonly used to check if something was input through the args
	public static boolean evaluate(String[] vals, Varslist vars) {
		if (vals[0].equals("true"))
			return true;
		else if (vals[0].equals("input")) {
			for (int pos = 0; pos < input.length; pos += 1) {
				if (input[pos].equals(vals[1]))
					return true;
			}
		} else if (vals[0].equals("==")) {
			int[] ints = intsplit(vals, vars);
			return ints[1] == ints[2];
		} else if (vals[0].equals(">=")) {
			int[] ints = intsplit(vals, vars);
			return ints[1] >= ints[2];
		} else if (vals[0].equals(">")) {
			int[] ints = intsplit(vals, vars);
			return ints[1] > ints[2];
		} else if (vals[0].equals("colorat")) {
			int[] ints = intsplit(vals, vars);
			return colorat(ints[0], ints[1], ints[2], ints[3], ints[4]);
		}
		String valstring = Varslist.varval(vars, vals[0]);
		if (valstring.equals(""))
			valstring = Varslist.varval(gbvars, vals[0]);
		if (valstring.equals("true"))
			return true;
		return false;
	}
	public static String[] subarray(String[] lines, int start, int end) {
		String[] temp = new String[end - start + 1];
		for (int pos = start; pos <= end; pos += 1) {
			temp[pos - start] = lines[pos];
		}
		return temp;
	}
	public static String clip(String theline) {
		int pos = 0;
		int length = theline.length();
		while (pos < length && (theline.charAt(pos) == ' ' || theline.charAt(pos) == '\t'))
			pos = pos + 1;
		if (pos >= length)
			return "";
		return theline.substring(pos, theline.length());
	}
	public static int[] intsplit(String[] vals, Varslist vars) {
		int[] list = new int[vals.length];
		String valstring = "";
		for (int spot = 0; spot < vals.length; spot += 1) {
			list[spot] = inteval(vals[spot], vars);
		}
		return list;
	}
	public static int inteval(String s, Varslist vars) {
		String valstring = "";
		valstring = Varslist.varval(vars, s);
		if (!valstring.equals(""))
			return numbers.readint(valstring);
		valstring = Varslist.varval(gbvars, s);
		if (!valstring.equals(""))
			return numbers.readint(valstring);
		else if (s.equals(".mousex"))
			return mousex();
		else if (s.equals(".mousey"))
			return mousey();
		return numbers.readint(s);
	}
	public static String[] split(String theline) {
		theline = clip(theline);
		int count = 0;
		for (int spot = 0; spot < theline.length(); spot += 1) {
			if (theline.charAt(spot) == '\t' || theline.charAt(spot) == ' ')
				count = count + 1;
		}
		String[] list = new String[count + 1];
		int index = 0;
		for (int spot = 0; spot < count; spot += 1) {
			while (theline.charAt(index) != '\t' && theline.charAt(index) != ' ')
				index = index + 1;
			list[spot] = theline.substring(0, index);
			theline = clip(theline.substring(index, theline.length()));
			index = 0;
		}
		list[count] = theline;
		return list;
	}
	public static void pause(int time) {
		xx = mousex();
		yy = mousey();
		auto.wait(time);
		if (xx != mousex() || yy != mousey())
			end("You moved the mouse.");
	}
	public static void pause(int x, int y, int r, int g, int b, boolean endearly) {
		xx = mousex();
		yy = mousey();
		long now = System.currentTimeMillis();
		while(!colorat(x, y, r, g, b) && (!endearly || System.currentTimeMillis() - now < waittime)) {
			if (xx != mousex() || yy != mousey())
				end("You moved the mouse.");
		}
		if (System.currentTimeMillis() - now >= waittime && endearly)
			end("Took too long.");
	}
	public static void pausenot(int x, int y, int r, int g, int b, boolean endearly) {
		xx = mousex();
		yy = mousey();
		long now = System.currentTimeMillis();
		while(colorat(x, y, r, g, b) && (!endearly || System.currentTimeMillis() - now < waittime)) {
			if (xx != mousex() || yy != mousey())
				end("You moved the mouse.");
		}
		if (System.currentTimeMillis() - now >= waittime && endearly)
			end("Took too long.");
	}
	public static boolean colorat(int x, int y, int r, int g, int b) {
		int[] cs = auto.colorsat(x, y);
		return cs[0] <= r + variance && cs[0] >= r - variance && cs[1] <= g + variance && cs[1] >= g - variance && cs[2] <= b + variance && cs[2] >= b - variance;
	}
	public static void end(String s) {
		System.out.println(s);
		System.exit(0);
	}
	public static void end() {
		System.exit(0);
	}
}