import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URI;
//lines start at 1
public class Filer {
	public class Stringlink {
		String line = "";
		Stringlink next;
		Stringlink last;
	}
	private Stringlink thefile = null;
	private File file = new File("null");
	private Stringlink currentlink = thefile;
	private File[] filelist = null;
	private int currentline = 1;
	public Filer() {
		file = file.getAbsoluteFile();
	}
	public Filer(String name) {
		setfile(name);
	}
	public Filer(File newfile) {
		setfile(newfile);
	}
	public void setfile(String name) {
		file = new File(name);
		file = file.getAbsoluteFile();
	}
	public void setfile(File newfile) {
		file = newfile;
		file = file.getAbsoluteFile();
	}
	public boolean delete() {
		return file.delete();
	}
//file information stuff
	public int lines() {
		if (thefile == null)
			return 0;
		int lines = 1;
		Stringlink templink = thefile;
		while (templink.next != null) {
			lines = lines + 1;
			templink = templink.next;
		}
		return lines;
	}
	public int files() {
		if (filelist != null)
			return filelist.length;
		else
			return 0;
	}
	public File getfile() {
		return file;
	}
//folder changing stuff
	public void goup() {
		if (file.getParentFile() != null)
			setfile(file.getParentFile());
	}
	public void goupfull() {
		while (file.getParentFile() != null)
			setfile(file.getParentFile());
	}
	public void godown(String folder) {
		setfile(new File(file, folder));
	}
//reader stuff
	public boolean fileisthere() {
		return file.exists();
	}
	public void readfile() {
		int next;
		thefile = new Stringlink();
		currentlink = thefile;
		currentline = 1;
		Stringlink templink = thefile;
		try {
			FileInputStream input = new FileInputStream(file);
			next = input.read();
			while (next >= 0) {
				if (next != 13 && next != 10)
					templink.line = templink.line + (char)(next);
				else if (next == 10) {
					templink.next = new Stringlink();
					templink.next.last = templink;
					templink = templink.next;
				}
				next = input.read();
			}
			input.close();
		} catch(Exception e) {
			System.out.println("Sorry, an error occured:\n" + e + "\nYour file could not be read.");
		}
	}
	public String getline(int line) {
		if (thefile != null && line != 0) {
			while (line > currentline) {
				if (currentlink.next != null)
					currentlink = currentlink.next;
				else
					return null;
				currentline = currentline + 1;
			}
			while (line < currentline) {
				currentlink = currentlink.last;
				currentline = currentline - 1;
			}
			return currentlink.line;
		} else
			return null;
	}
	public String[] getlines() {
		String[] thelines = new String[lines()];
		for (int pos = 1; pos <= thelines.length; pos += 1) {
			thelines[pos - 1] = getline(pos);
		}
		return thelines;
	}
//writer stuff
	public void write(String text, int line) {
		setline(line);
		currentlink.line = currentlink.line + text;
	}
	public void writeover(String text, int line) {
		setline(line);
		currentlink.line = text;
	}
	public void writeline(String text, int line) {
		write(text, line);
		setline(line + 1);
	}
	public void setline(int line) {
		if (line <= 0)
			return;
		if (thefile == null) {
			thefile = new Stringlink();
			currentlink = thefile;
			currentline = 1;
		}
		while (line > currentline) {
			if (currentlink.next == null) {
				currentlink.next = new Stringlink();
				currentlink.next.last = currentlink;
			}
			currentlink = currentlink.next;
			currentline = currentline + 1;
		}
		while (line < currentline) {
			currentlink = currentlink.last;
			currentline = currentline - 1;
		}
	}
	public void addline(String text) {
		if (thefile == null) {
			thefile = new Stringlink();
			currentlink = thefile;
			currentline = 1;
		}
		while (currentlink.next != null) {
			currentlink = currentlink.next;
			currentline = currentline + 1;
		}
		currentlink.next = new Stringlink();
		currentlink.next.last = currentlink;
		currentlink = currentlink.next;
		currentline = currentline + 1;
		currentlink.line = text;
	}
	public void removeline(int line) {
		if (line <= 0)
			return;
		if (thefile == null)
			return;
		while (line > currentline) {
			if (currentlink.next == null)
				return;
			currentlink = currentlink.next;
			currentline = currentline + 1;
		}
		while (line < currentline) {
			currentlink = currentlink.last;
			currentline = currentline - 1;
		}
		if (currentlink.next != null && currentlink.last != null) {
			currentlink.next.last = currentlink.last;
			currentlink.last.next = currentlink.next;
			currentlink = currentlink.next;
		} else if (currentlink.last != null) {
			currentlink = currentlink.last;
			currentlink.next = null;
			currentline = currentline - 1;
		} else if (currentlink.next != null) {
			currentlink = currentlink.next;
			thefile = thefile.next;
			currentlink.next.last = null;
		} else
			thefile = null;
		return;
	}
	public void insertline(String text, int line) {
		insertline(line);
		currentlink.line = text;
	}
	public void insertline(int line) {
		if (line <= 0)
			return;
		if (thefile == null) {
			thefile = new Stringlink();
			currentlink = thefile;
			currentline = 1;
		}
		while (line > currentline) {
			if (currentlink.next == null) {
				currentlink.next = new Stringlink();
				currentlink.next.last = currentlink;
				currentlink = currentlink.next;
				currentline = currentline + 1;
				if (currentline == line)
					return;
			} else {
				currentlink = currentlink.next;
				currentline = currentline + 1;
			}
		}
		while (line < currentline) {
			currentlink = currentlink.last;
			currentline = currentline - 1;
		}
		if (currentlink.last == null) {
			currentlink.last = new Stringlink();
			currentlink.last.next = currentlink;
			currentlink = currentlink.last;
			thefile = currentlink;
		} else {
			Stringlink templink = new Stringlink();
			currentlink.last.next = templink;
			templink.last = currentlink.last;
			templink.next = currentlink;
			currentlink.last = templink;
			currentlink = templink;
		}
	}
	public boolean savefile() {
		if (thefile == null) {
			thefile = new Stringlink();
			currentlink = thefile;
			currentline = 1;
		}
		try {
			FileWriter writer = new FileWriter(file);
			currentlink = thefile;
			if (thefile != null) {
				while (currentlink.next != null) {
					writer.write(currentlink.line + "\r\n");
					currentlink = currentlink.next;
				}
					writer.write(currentlink.line);
			}
			writer.close();
			currentlink = thefile;
			currentline = 1;
		} catch(Exception e) {
			System.out.println("Sorry, an error occured:\n" + e + "\nYour file could not be written.");
			return false;
		}
		return true;
	}
//file stuff
	public void storefilelist() {
		filelist = file.getParentFile().listFiles();
	}
	public File getlistfile(int pos) {
		return filelist[pos];
	}
	public File getlistfile(String filename) {
		String thefile;
		for (int pos = 0; pos < filelist.length; pos += 1) {
			thefile = filelist[pos].getName();
			if (filename.equalsIgnoreCase(thefile))
				return filelist[pos];
		}
		return null;
	}
}