import java.awt.Robot;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.MouseInfo;
import java.awt.Point;
public class Autoer {
	public Robot auto;
	public Autoer() {
		try {
			auto = new Robot();
		} catch(Exception e) {
			System.out.println("The robot could not be made.\n" + e);
			System.exit(0);
		}
	}
	public void left(int x, int y) {
		auto.mouseMove(x, y);
		auto.mousePress(InputEvent.BUTTON1_MASK);
		auto.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	public void leftEnsure(int x, int y) {
		moveEnsure(x, y);
		auto.mousePress(InputEvent.BUTTON1_MASK);
		auto.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	public void left(int type) {
		if (type < 3)
			auto.mousePress(InputEvent.BUTTON1_MASK);
		if (type > 1)
			auto.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	public void right(int x, int y) {
		auto.mouseMove(x, y);
		auto.mousePress(InputEvent.BUTTON3_MASK);
		auto.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	public void rightEnsure(int x, int y) {
		moveEnsure(x, y);
		auto.mousePress(InputEvent.BUTTON3_MASK);
		auto.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	public void right(int type) {
		if (type < 3)
			auto.mousePress(InputEvent.BUTTON3_MASK);
		if (type > 1)
			auto.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	public void middle(int x, int y) {
		auto.mouseMove(x, y);
		auto.mousePress(InputEvent.BUTTON2_MASK);
		auto.mouseRelease(InputEvent.BUTTON2_MASK);
	}
	public void middleEnsure(int x, int y) {
		moveEnsure(x, y);
		auto.mousePress(InputEvent.BUTTON2_MASK);
		auto.mouseRelease(InputEvent.BUTTON2_MASK);
	}
	public void middle(int type) {
		if (type < 3)
			auto.mousePress(InputEvent.BUTTON2_MASK);
		if (type > 1)
			auto.mouseRelease(InputEvent.BUTTON2_MASK);
	}
	public void move(int x, int y) {
		auto.mouseMove(x, y);
	}
	public void moveEnsure(int x, int y) {
		auto.mouseMove(x, y);
		long now = System.currentTimeMillis();
		Point loc = MouseInfo.getPointerInfo().getLocation();
		//wait until the mouse is in the right spot or 5 milliseconds have passed
		while ((loc.x != x || loc.y != y) && System.currentTimeMillis() - now < 5)
			loc = MouseInfo.getPointerInfo().getLocation();
	}
	public void wait(int wait) {
		auto.delay(wait);
	}
	public Color colorat(int x, int y) {
		return auto.getPixelColor(x, y);
	}
	public int redat(int x, int y) {
		return auto.getPixelColor(x, y).getRed();
	}
	public int greenat(int x, int y) {
		return auto.getPixelColor(x, y).getGreen();
	}
	public int blueat(int x, int y) {
		return auto.getPixelColor(x, y).getBlue();
	}
	public int[] colorsat(int x, int y) {
		Color c = auto.getPixelColor(x, y);
		return new int[] {c.getRed(), c.getGreen(), c.getBlue()};
	}
	public void scroll(String direction) {
		if (direction.equalsIgnoreCase("up"))
			auto.mouseWheel(-1);
		else if (direction.equalsIgnoreCase("down"))
			auto.mouseWheel(1);
	}
	public void scroll(String direction, int amount) {
		if (direction.equalsIgnoreCase("up"))
			auto.mouseWheel(-amount);
		else if (direction.equalsIgnoreCase("down"))
			auto.mouseWheel(amount);
	}
	public void scroll(int amount) {
		auto.mouseWheel(amount);
	}
	public void type(String message) {
		int length = message.length();
		for (int pos = 0; pos < length; pos += 1) {
			button(message.charAt(pos), 2);
		}
	}
	public void button(char button, int type) {
		boolean shift = false;
		int buttonnum = 0;
		if (button == ' ')
			buttonnum = KeyEvent.VK_SPACE;
		else if (button == 'a')
			buttonnum = KeyEvent.VK_A;
		else if (button == 'b')
			buttonnum = KeyEvent.VK_B;
		else if (button == 'c')
			buttonnum = KeyEvent.VK_C;
		else if (button == 'd')
			buttonnum = KeyEvent.VK_D;
		else if (button == 'e')
			buttonnum = KeyEvent.VK_E;
		else if (button == 'f')
			buttonnum = KeyEvent.VK_F;
		else if (button == 'g')
			buttonnum = KeyEvent.VK_G;
		else if (button == 'h')
			buttonnum = KeyEvent.VK_H;
		else if (button == 'i')
			buttonnum = KeyEvent.VK_I;
		else if (button == 'j')
			buttonnum = KeyEvent.VK_J;
		else if (button == 'k')
			buttonnum = KeyEvent.VK_K;
		else if (button == 'l')
			buttonnum = KeyEvent.VK_L;
		else if (button == 'm')
			buttonnum = KeyEvent.VK_M;
		else if (button == 'n')
			buttonnum = KeyEvent.VK_N;
		else if (button == 'o')
			buttonnum = KeyEvent.VK_O;
		else if (button == 'p')
			buttonnum = KeyEvent.VK_P;
		else if (button == 'q')
			buttonnum = KeyEvent.VK_Q;
		else if (button == 'r')
			buttonnum = KeyEvent.VK_R;
		else if (button == 's')
			buttonnum = KeyEvent.VK_S;
		else if (button == 't')
			buttonnum = KeyEvent.VK_T;
		else if (button == 'u')
			buttonnum = KeyEvent.VK_U;
		else if (button == 'v')
			buttonnum = KeyEvent.VK_V;
		else if (button == 'w')
			buttonnum = KeyEvent.VK_W;
		else if (button == 'x')
			buttonnum = KeyEvent.VK_X;
		else if (button == 'y')
			buttonnum = KeyEvent.VK_Y;
		else if (button == 'z')
			buttonnum = KeyEvent.VK_Z;
		else if (button == '1')
			buttonnum = KeyEvent.VK_1;
		else if (button == '2')
			buttonnum = KeyEvent.VK_2;
		else if (button == '3')
			buttonnum = KeyEvent.VK_3;
		else if (button == '4')
			buttonnum = KeyEvent.VK_4;
		else if (button == '5')
			buttonnum = KeyEvent.VK_5;
		else if (button == '6')
			buttonnum = KeyEvent.VK_6;
		else if (button == '7')
			buttonnum = KeyEvent.VK_7;
		else if (button == '8')
			buttonnum = KeyEvent.VK_8;
		else if (button == '9')
			buttonnum = KeyEvent.VK_9;
		else if (button == '0')
			buttonnum = KeyEvent.VK_0;
		else if (button == '`')
			buttonnum = KeyEvent.VK_BACK_QUOTE;
		else if (button == '-')
			buttonnum = KeyEvent.VK_MINUS;
		else if (button == '=')
			buttonnum = KeyEvent.VK_EQUALS;
		else if (button == '[')
			buttonnum = KeyEvent.VK_BRACELEFT;
		else if (button == ']')
			buttonnum = KeyEvent.VK_BRACERIGHT;
		else if (button == '\\')
			buttonnum = KeyEvent.VK_BACK_SLASH;
		else if (button == ';')
			buttonnum = KeyEvent.VK_SEMICOLON;
		else if (button == '\'')
			buttonnum = KeyEvent.VK_QUOTE;
		else if (button == ',')
			buttonnum = KeyEvent.VK_COMMA;
		else if (button == '.')
			buttonnum = KeyEvent.VK_PERIOD;
		else if (button == '/')
			buttonnum = KeyEvent.VK_SLASH;
		else if (button == 'A') {
			shift = true;
			buttonnum = KeyEvent.VK_A;
		} else if (button == 'B') {
			shift = true;
			buttonnum = KeyEvent.VK_B;
		} else if (button == 'C') {
			shift = true;
			buttonnum = KeyEvent.VK_C;
		} else if (button == 'D') {
			shift = true;
			buttonnum = KeyEvent.VK_D;
		} else if (button == 'E') {
			shift = true;
			buttonnum = KeyEvent.VK_E;
		} else if (button == 'F') {
			shift = true;
			buttonnum = KeyEvent.VK_F;
		} else if (button == 'G') {
			shift = true;
			buttonnum = KeyEvent.VK_G;
		} else if (button == 'H') {
			shift = true;
			buttonnum = KeyEvent.VK_H;
		} else if (button == 'I') {
			shift = true;
			buttonnum = KeyEvent.VK_I;
		} else if (button == 'J') {
			shift = true;
			buttonnum = KeyEvent.VK_J;
		} else if (button == 'K') {
			shift = true;
			buttonnum = KeyEvent.VK_K;
		} else if (button == 'L') {
			shift = true;
			buttonnum = KeyEvent.VK_L;
		} else if (button == 'M') {
			shift = true;
			buttonnum = KeyEvent.VK_M;
		} else if (button == 'N') {
			shift = true;
			buttonnum = KeyEvent.VK_N;
		} else if (button == 'O') {
			shift = true;
			buttonnum = KeyEvent.VK_O;
		} else if (button == 'P') {
			shift = true;
			buttonnum = KeyEvent.VK_P;
		} else if (button == 'Q') {
			shift = true;
			buttonnum = KeyEvent.VK_Q;
		} else if (button == 'R') {
			shift = true;
			buttonnum = KeyEvent.VK_R;
		} else if (button == 'S') {
			shift = true;
			buttonnum = KeyEvent.VK_S;
		} else if (button == 'T') {
			shift = true;
			buttonnum = KeyEvent.VK_T;
		} else if (button == 'U') {
			shift = true;
			buttonnum = KeyEvent.VK_U;
		} else if (button == 'V') {
			shift = true;
			buttonnum = KeyEvent.VK_V;
		} else if (button == 'W') {
			shift = true;
			buttonnum = KeyEvent.VK_W;
		} else if (button == 'X') {
			shift = true;
			buttonnum = KeyEvent.VK_X;
		} else if (button == 'Y') {
			shift = true;
			buttonnum = KeyEvent.VK_Y;
		} else if (button == 'Z') {
			shift = true;
			buttonnum = KeyEvent.VK_Z;
		} else if (button == '!') {
			shift = true;
			buttonnum = KeyEvent.VK_1;
		} else if (button == '@') {
			shift = true;
			buttonnum = KeyEvent.VK_2;
		} else if (button == '#') {
			shift = true;
			buttonnum = KeyEvent.VK_3;
		} else if (button == '$') {
			shift = true;
			buttonnum = KeyEvent.VK_4;
		} else if (button == '%') {
			shift = true;
			buttonnum = KeyEvent.VK_5;
		} else if (button == '^') {
			shift = true;
			buttonnum = KeyEvent.VK_6;
		} else if (button == '&') {
			shift = true;
			buttonnum = KeyEvent.VK_7;
		} else if (button == '*') {
			shift = true;
			buttonnum = KeyEvent.VK_8;
		} else if (button == '(') {
			shift = true;
			buttonnum = KeyEvent.VK_9;
		} else if (button == ')') {
			shift = true;
			buttonnum = KeyEvent.VK_0;
		} else if (button == '~') {
			shift = true;
			buttonnum = KeyEvent.VK_BACK_QUOTE;
		} else if (button == '_') {
			shift = true;
			buttonnum = KeyEvent.VK_MINUS;
		} else if (button == '+') {
			shift = true;
			buttonnum = KeyEvent.VK_EQUALS;
		} else if (button == '{') {
			shift = true;
			buttonnum = KeyEvent.VK_BRACELEFT;
		} else if (button == '}') {
			shift = true;
			buttonnum = KeyEvent.VK_BRACERIGHT;
		} else if (button == '|') {
			shift = true;
			buttonnum = KeyEvent.VK_BACK_SLASH;
		} else if (button == ':') {
			shift = true;
			buttonnum = KeyEvent.VK_SEMICOLON;
		} else if (button == '\"') {
			shift = true;
			buttonnum = KeyEvent.VK_QUOTE;
		} else if (button == '<') {
			shift = true;
			buttonnum = KeyEvent.VK_COMMA;
		} else if (button == '>') {
			shift = true;
			buttonnum = KeyEvent.VK_PERIOD;
		} else if (button == '?') {
			shift = true;
			buttonnum = KeyEvent.VK_SLASH;
		} else if (button == '\n')
			buttonnum = KeyEvent.VK_ENTER;
		else if (button == '\t')
			buttonnum = KeyEvent.VK_TAB;
		else if (button == '\b')
			buttonnum = KeyEvent.VK_BACK_SPACE;
		if (shift)
			auto.keyPress(KeyEvent.VK_SHIFT);
		if (type < 3)
			auto.keyPress(buttonnum);
		if (type > 1)
			auto.keyRelease(buttonnum);
		if (shift)
			auto.keyRelease(KeyEvent.VK_SHIFT);
	}
	public void button(String button, int type) {
		int buttonnum = 0;
		if (button.equals("enter"))
			buttonnum = KeyEvent.VK_ENTER;
		else if (button.equals("space"))
			buttonnum = KeyEvent.VK_SPACE;
		else if (button.equals("up"))
			buttonnum = KeyEvent.VK_UP;
		else if (button.equals("down"))
			buttonnum = KeyEvent.VK_DOWN;
		else if (button.equals("left"))
			buttonnum = KeyEvent.VK_LEFT;
		else if (button.equals("right"))
			buttonnum = KeyEvent.VK_RIGHT;
		else if (button.equals("shift"))
			buttonnum = KeyEvent.VK_SHIFT;
		else if (button.equals("backspace"))
			buttonnum = KeyEvent.VK_BACK_SPACE;
		else if (button.equals("tab"))
			buttonnum = KeyEvent.VK_TAB;
		else if (button.equals("delete"))
			buttonnum = KeyEvent.VK_DELETE;
		else if (button.equals("control"))
			buttonnum = KeyEvent.VK_CONTROL;
		else if (button.equals("alt"))
			buttonnum = KeyEvent.VK_ALT;
		else if (button.equals("capslock"))
			buttonnum = KeyEvent.VK_CAPS_LOCK;
		else if (button.equals("escape"))
			buttonnum = KeyEvent.VK_ESCAPE;
		else if (button.equals("home"))
			buttonnum = KeyEvent.VK_HOME;
		else if (button.equals("end"))
			buttonnum = KeyEvent.VK_END;
		else if (button.equals("pageup"))
			buttonnum = KeyEvent.VK_PAGE_UP;
		else if (button.equals("pagedown"))
			buttonnum = KeyEvent.VK_PAGE_DOWN;
		if (type < 3)
			auto.keyPress(buttonnum);
		if (type > 1)
			auto.keyRelease(buttonnum);
	}
	public BufferedImage screenshot() {
		return auto.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}
	public BufferedImage screenshot(int x, int y, int w, int h) {
		return auto.createScreenCapture(new Rectangle(x, y, w, h));
	}
}