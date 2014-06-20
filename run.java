import java.awt.MouseInfo;
//TO ADD:
//expanded booleans using the same format as ifs
//perhaps expressions, eventually
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
	public static long timer = System.currentTimeMillis();
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
			return "boolean";
		}
		public String toString() {
			return val[0] + "," + val[1] + "," + val[2];
		}
	}
	public static void main(String[] args) {
		auto = new Autoer();
		if (args.length < 1)
			end("You need a script to run.");
		Filer filer = new Filer("scripts/" + args[0] + ".txt");
		if (!filer.fileisthere())
			end("Script \"" + args[0] + "\" does not exist.");
		filer.readfile();
		String[] lines = filer.getlines();
		if (scriptname.equals(""))
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
		for (int row = 1; row <= lines.length; row += 1) {
			bits = split(lines[row - 1]);
			if (bits.length >= 2) {
				if (bits[1].startsWith(".") && (bits[0].equals("create") || bits[0].equals("for")))
					end("Bad syntax in " + scriptname + " on line " + row + ": variables cannot start with \".\"");
			}
			if (bits.length > 0) {
				if (bits[0].equals("print") && clip(lines[row - 1]).length() < 7)
					end("Bad syntax in " + scriptname + " on line " + row + ": print must print at least one character.");
				else if (bits[0].equals("type") && clip(lines[row - 1]).length() < 6)
					end("Bad syntax in " + scriptname + " on line " + row + ": type must type at least one character.");
				else if (argnum(bits[0]) > bits.length - 1)
					end("Bad syntax in " + scriptname + " on line " + row + ": only " + (bits.length - 1) + " arguments instead of " + argnum(bits[0]) + ".");
				else if (bits[0].equals("run")) {
					Filer inner = new Filer("scripts/" + bits[1] + ".txt");
					inner.readfile();
					verify(inner.getlines());
				}
			}
		}
	}
	public static int argnum(String line) {
		if (	line.equals("pausenot"))
			return 5;
		if (	line.equals("for"))
			return 4;
		if (	line.equals("create"))
			return 3;
		if (	line.equals("left") ||
			line.equals("right") ||
			line.equals("move") ||
			line.equals("+=") ||
			line.equals("-=") ||
			line.equals("*=") ||
			line.equals("/=") ||
			line.equals("set") ||
			line.equals("setargto"))
			return 2;
		if (	line.equals("run") ||
			line.equals("jump") ||
			line.equals("if") ||
			line.equals("printval") ||
			line.equals("pause"))
			return 1;
		return 0;
	}
	public static void runscript(String[] lines, VarsInt vars) {
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
				line = "";
				while (jumpline < linecount) {
					line = clip(lines[jumpline]);
					if (line.startsWith("for"))
						layer = layer + 1;
					else if (line.startsWith("end"))
						layer = layer - 1;
					if (layer == 0)
						break;
					jumpline = jumpline + 1;
				}
				String[] subarray = subarray(lines, pos + 1, jumpline - 1);
				VarsInt newvars = new VarsInt(scripts[0], ints[1], vars);
				while (newvars.val <= ints[2] && runloop) {
					runscript(subarray, newvars);
					newvars.val = newvars.val + ints[3];
				}
				runloop = true;
				pos = jumpline;
			} else if (line.startsWith("if")) {
				if (!evaluate(split(line.substring(2, line.length())), vars))
					pos = pos + 1;
			} else if (line.startsWith("create")) {
				scripts = split(line.substring(6, line.length()));
				if (scripts[0].equals("int")) {
					if (scripts[2].equals("random")) {
						int rand = (int)(Math.random() * inteval(scripts[3], vars));
						gbvars = new VarsInt(scripts[1], rand, gbvars);
					} else
						gbvars = new VarsInt(scripts[1], inteval(scripts[2], vars), gbvars);
				} else if (scripts[0].equals("boolean"))
					gbvars = new VarsBoolean(scripts[1], evaluate(new String[] {scripts[2]}, vars), gbvars);
				else if (scripts[0].equals("color")) {
					Varslist temp = varcalled(scripts[2], vars);
					if (temp != null && temp.type().equals("color")) {
						VarsColor vc = (VarsColor)(temp);
						gbvars = new VarsColor(scripts[1], vc.val, gbvars);
					} else if (scripts[2].equals("at")) {
						ints = auto.colorsat(inteval(scripts[3], vars), inteval(scripts[4], vars));
						gbvars = new VarsColor(scripts[1], new int[] {ints[0], ints[1], ints[2]}, gbvars);
					} else {
						ints = intsplit(scripts, vars);
						gbvars = new VarsColor(scripts[1], new int[] {ints[2], ints[3], ints[4]}, gbvars);
					}
				}
			} else if (line.startsWith("setargto")) {
				scripts = split(line.substring(8, line.length()));
				String arg = "";
				for (int spot = 0; spot < input.length; spot += 1) {
					if (input[spot].startsWith(scripts[0])) {
						arg = input[spot].substring(scripts[0].length(), input[spot].length());
						break;
					}
				}
				if (!arg.equals("")) {
					Varslist temp = varcalled(scripts[1], vars);
					scripts = commasplit(arg);
					if (temp != null) {
						if (temp.type().equals("int")) {
							VarsInt vi = (VarsInt)(temp);
							if (scripts[0].equals("random"))
								vi.val = (int)(Math.random() * inteval(scripts[1], vars));
							else
								vi.val = inteval(arg.substring(7, arg.length()), vars);
						} else if (temp.type().equals("boolean")) {
							VarsBoolean vb = (VarsBoolean)(temp);
							vb.val = evaluate(scripts, vars);
						} else if (temp.type().equals("color")) {
							VarsColor vc = (VarsColor)(temp);
							temp = varcalled(arg, vars);
							if (temp != null && temp.type().equals("color")) {
								VarsColor vc2 = (VarsColor)(temp);
								vc.val = vc2.val;
							} else if (scripts[0].equals("at")) {
								ints = auto.colorsat(inteval(scripts[1], vars), inteval(scripts[2], vars));
								vc.val = new int[] {ints[0], ints[1], ints[2]};
							} else {
								ints = intsplit(scripts, vars);
								vc.val = new int[] {ints[0], ints[1], ints[2]};
							}
						}
					}
				}
			} else if (line.startsWith("+=") || line.startsWith("-=") || line.startsWith("*=") || line.startsWith("/=")) {
				scripts = split(line);
				Varslist temp = varcalled(scripts[1], vars);
				if (temp != null && temp.type().equals("int")) {
					VarsInt vi = (VarsInt)(temp);
					if (scripts[0].equals("-="))
						vi.val -= inteval(scripts[2], vars);
					else if (scripts[0].equals("*="))
						vi.val *= inteval(scripts[2], vars);
					else if (scripts[0].equals("/="))
						vi.val /= inteval(scripts[2], vars);
					else
						vi.val += inteval(scripts[2], vars);
				}
			} else if (line.startsWith("set")) {
				scripts = split(line.substring(3, line.length()));
				Varslist temp = varcalled(scripts[0], vars);
				if (temp != null) {
					if (temp.type().equals("int")) {
						VarsInt vi = (VarsInt)(temp);
						if (scripts[1].equals("random"))
							vi.val = (int)(Math.random() * inteval(scripts[2], vars));
						else
							vi.val = inteval(scripts[1], vars);
					} else if (temp.type().equals("boolean")) {
						VarsBoolean vb = (VarsBoolean)(temp);
						vb.val = evaluate(new String[] {scripts[1]}, vars);
					} else if (temp.type().equals("color")) {
						VarsColor vc = (VarsColor)(temp);
						temp = varcalled(scripts[1], vars);
						if (temp != null && temp.type().equals("color")) {
							VarsColor vc2 = (VarsColor)(temp);
							vc.val = vc2.val;
						} else if (scripts[1].equals("at")) {
							ints = auto.colorsat(inteval(scripts[2], vars), inteval(scripts[3], vars));
							vc.val = new int[] {ints[0], ints[1], ints[2]};
						} else {
							ints = intsplit(scripts, vars);
							vc.val = new int[] {ints[1], ints[2], ints[3]};
						}
					}
				}
			} else if (line.startsWith("treset"))
				timer = System.currentTimeMillis();
			else if (line.startsWith("println"))
				System.out.println();
			else if (line.startsWith("printval")) {
				scripts = split(line.substring(8, line.length()));
				Varslist temp = varcalled(scripts[0], vars);
				if (temp != null)
					System.out.println("" + temp);
			} else if (line.startsWith("print"))
				System.out.print(line.substring(6, line.length()));
			else if (line.startsWith("type"))
				auto.type(line.substring(5,line.length()));
			else if (line.startsWith("press")) {
				scripts = split(line.substring(5, line.length()));
				auto.button(scripts[0], 1);
			} else if (line.startsWith("release")) {
				scripts = split(line.substring(7, line.length()));
				auto.button(scripts[0], 3);
			} else if (line.startsWith("break")) {
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
	public static Varslist varcalled(String s, Varslist vars) {
		while (vars != null) {
			if (vars.name.equals(s))
				return vars;
			vars = vars.next;
		}
		vars = gbvars;
		while (vars != null) {
			if (vars.name.equals(s))
				return vars;
			vars = vars.next;
		}
		return null;
	}
	public static boolean evaluate(String[] vals, VarsInt vars) {
		if (vals[0].equals("not"))
			return !evaluate(subarray(vals, 1, vals.length - 1), vars);
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
			return colorat(ints[1], ints[2], ints[3], ints[4], ints[5]);
		}
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
		return theline.substring(pos, theline.length());
	}
	public static int[] intsplit(String[] vals, VarsInt vars) {
		int[] list = new int[vals.length];
		for (int spot = 0; spot < vals.length; spot += 1) {
			list[spot] = inteval(vals[spot], vars);
		}
		return list;
	}
	public static int inteval(String s, Varslist vars) {
		String valstring = "";
		vars = varcalled(s, vars);
		if (vars != null && vars.type().equals("int")) {
			VarsInt vi = (VarsInt)(vars);
			return vi.val;
		} else if (s.equals(".mousex"))
			return mousex();
		else if (s.equals(".mousey"))
			return mousey();
		else if (s.equals(".timer"))
			return (int)(System.currentTimeMillis() - timer);
		return numbers.readint(s);
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
	public static void pause(int time) {
		xx = mousex();
		yy = mousey();
		long now = System.currentTimeMillis();
		while (System.currentTimeMillis() - now < time) {
			if (xx != mousex() || yy != mousey())
				end("You moved the mouse.");
		}
	}
	public static void pause(int x, int y, int r, int g, int b, boolean endearly) {
		xx = mousex();
		yy = mousey();
		long now = System.currentTimeMillis();
		while (!colorat(x, y, r, g, b) && (!endearly || System.currentTimeMillis() - now < waittime)) {
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
		while (colorat(x, y, r, g, b) && (!endearly || System.currentTimeMillis() - now < waittime)) {
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