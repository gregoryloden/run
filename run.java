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
	public static int offsetx = 0;
	public static int offsety = 0;
	public static int temp = 0;
	public static int variance = 10;
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
		public static boolean addto(Varslist vl, String n, int v) {
			if (vl == null)
				return false;
			if (vl.name.equals(n) && vl.type.equals("int")) {
				vl.val = "" + (v + numbers.readint(vl.val));
				return true;
			}
			return addto(vl.next, n, v);
		}
		public static boolean set(Varslist vl, String n, int v) {
			if (vl == null)
				return false;
			if (vl.name.equals(n) && vl.type.equals("int")) {
				vl.val = v + "";
				return true;
			}
			return set(vl.next, n, v);
		}
	}
	public static void main(String[] args) {
		auto = new Autoer();
		if (args.length < 1) {
			System.out.println("You need a script to run.");
			end();
		}
		Filer filer = new Filer("scripts\\" + args[0] + ".txt");
		if (!filer.fileisthere()) {
			System.out.println("Script \"" + args[0] + "\" does not exist.");
			end();
		}
		filer.readfile();
		String[] oldinput = input;
		Varslist oldgbvars = gbvars;
		int oldoffsetx = offsetx;
		int oldoffsety = offsety;
		input = subarray(args, 1, args.length - 1);
		gbvars = null;
		offsetx = 0;
		offsety = 0;
		runscript(filer.getlines(), null);
		input = oldinput;
		gbvars = oldgbvars;
		offsetx = oldoffsetx;
		offsety = oldoffsety;
	}
	public static void runscript(String[] lines, Varslist vars) {
		String line = "";
		int pos = 0;
		int[] ints;
		String[] scripts;
		int linecount = lines.length;
		while (pos < linecount) {
			line = clip(lines[pos]);
			if (line.startsWith("left")) {
				scripts = split(line.substring(4, line.length()));
				ints = intsplit(scripts, vars);
				auto.left(ints[0] + offsetx, ints[1] + offsety);
			} else if (line.startsWith("right")) {
				scripts = split(line.substring(5, line.length()));
				ints = intsplit(scripts, vars);
				auto.right(ints[0] + offsetx, ints[1] + offsety);
			} else if (line.startsWith("move")) {
				scripts = split(line.substring(4, line.length()));
				ints = intsplit(scripts, vars);
				auto.move(ints[0] + offsetx, ints[1] + offsety);
			} else if (line.startsWith("pausenot")) {
				scripts = split(line.substring(5, line.length()));
				ints = intsplit(scripts, vars);
				pausenot(ints[0], ints[1], ints[2], ints[3], ints[4], false);
			} else if (line.startsWith("pause")) {
				scripts = split(line.substring(5, line.length()));
				ints = intsplit(scripts, vars);
				if (ints.length < 5)
					pause(ints[0]);
				else
					pause(ints[0], ints[1], ints[2], ints[3], ints[4], false);
			} else if (line.startsWith("run")) {
				scripts = split(line.substring(3, line.length()));
				main(scripts);
			} else if (line.startsWith("jump")) {
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
				for (int count = ints[1]; count <= ints[2]; count += ints[3])
					runscript(subarray, new Varslist("int", scripts[0], "" + count, vars));
				pos = jumpline;
			} else if (line.startsWith("if")) {
				if (!evaluate(split(line.substring(2, line.length())), vars))
					pos = pos + 1;
			} else if (line.startsWith("offsetx")) {
				scripts = split(line.substring(7, line.length()));
				ints = intsplit(scripts, vars);
				offsetx = offsetx + ints[0];
			} else if (line.startsWith("offsety")) {
				scripts = split(line.substring(7, line.length()));
				ints = intsplit(scripts, vars);
				offsety = offsety + ints[0];
			} else if (line.startsWith("setoffsetx")) {
				scripts = split(line.substring(10, line.length()));
				ints = intsplit(scripts, vars);
				offsetx = ints[0];
			} else if (line.startsWith("setoffsety")) {
				scripts = split(line.substring(10, line.length()));
				ints = intsplit(scripts, vars);
				offsety = ints[0];
			} else if (line.startsWith("create")) {
				scripts = split(line.substring(6, line.length()));
				gbvars = new Varslist(scripts[0], scripts[1], scripts[2], gbvars);
			} else if (line.startsWith("setintarg")) {
				scripts = split(line.substring(9, line.length()));
				for (int spot = 0; spot < input.length; spot += 1) {
					if (input[spot].startsWith(scripts[0]))
						gbvars = new Varslist("int", scripts[1], input[spot].substring(scripts[0].length(), input[spot].length()), gbvars);
				}
			} else if (line.startsWith("addto")) {
				scripts = split(line.substring(5, line.length()));
				temp = numbers.readint(scripts[1]);
				if (!Varslist.addto(vars, scripts[0], temp))
					Varslist.addto(gbvars, scripts[0], temp);
			} else if (line.startsWith("set")) {
				scripts = split(line.substring(3, line.length()));
				temp = numbers.readint(scripts[1]);
				if (!Varslist.set(vars, scripts[0], temp))
					Varslist.set(gbvars, scripts[0], temp);
			} else if (line.startsWith("quit")) {
				System.out.println("Quitting script.");
				end();
			}
			pos = pos + 1;
		}
	}
//evaluate: used to check the value of the "if" clauses
//commonly used to check if something was input through the args
	public static boolean evaluate(String[] vals, Varslist vars) {
		if (vals[0].equals("input")) {
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
			valstring = Varslist.varval(vars, vals[spot]);
			if (valstring.equals(""))
				valstring = Varslist.varval(gbvars, vals[spot]);
			if (!valstring.equals(""))
				list[spot] = numbers.readint(valstring);
			else if (vals[spot].equals("mousex"))
				list[spot] = mousex();
			else if (vals[spot].equals("mousey"))
				list[spot] = mousey();
			else
				list[spot] = numbers.readint(vals[spot]);
		}
		return list;
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
		if (xx != mousex() || yy != mousey()) {
			System.out.println("You moved the mouse.");
			end();
		}
	}
	public static void pause(int x, int y, int r, int g, int b, boolean endearly) {
		xx = mousex();
		yy = mousey();
		long now = System.currentTimeMillis();
		while((auto.redat(x, y) > r + variance || auto.redat(x, y) < r - variance || auto.greenat(x, y) > g + variance || auto.greenat(x, y) < g - variance || auto.blueat(x, y) > b + variance || auto.blueat(x, y) < b - variance) && (!endearly || System.currentTimeMillis() - now < waittime)) {
			if (xx != mousex() || yy != mousey()) {
				System.out.println("You moved the mouse.");
				end();
			}
		}
		if (System.currentTimeMillis() - now >= waittime && endearly) {
			System.out.println("Took too long.");
			end();
		}
	}
	public static void pausenot(int x, int y, int r, int g, int b, boolean endearly) {
		xx = mousex();
		yy = mousey();
		long now = System.currentTimeMillis();
		while(auto.redat(x, y) <= r + variance && auto.redat(x, y) >= r - variance && auto.greenat(x, y) <= g + variance && auto.greenat(x, y) >= g - variance && auto.blueat(x, y) <= b + variance && auto.blueat(x, y) >= b - variance && (!endearly || System.currentTimeMillis() - now < waittime)) {
			if (xx != mousex() || yy != mousey()) {
				System.out.println("You moved the mouse.");
				end();
			}
		}
		if (System.currentTimeMillis() - now >= waittime && endearly) {
			System.out.println("Took too long.");
			end();
		}
	}
	public static void end() {
		System.exit(0);
	}
}