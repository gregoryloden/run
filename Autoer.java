import java.awt.Robot;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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
		left(2);
	}
	public void right(int x, int y) {
		auto.mouseMove(x, y);
		right(2);
	}
	public void left(int type) {
		if (type < 3)
			auto.mousePress(InputEvent.BUTTON1_MASK);
		if (type > 1)
			auto.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	public void right(int type) {
		if (type < 3)
			auto.mousePress(InputEvent.BUTTON3_MASK);
		if (type > 1)
			auto.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	public void move(int x, int y) {
		auto.mouseMove(x, y);
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
	public void type(String message) {
		char letter = ' ';
		for (int pos = 0; pos < message.length(); pos += 1) {
			letter = message.charAt(pos);
			if ((int)(letter) >= 65 && (int)(letter) <= 90)
				auto.keyPress(KeyEvent.VK_SHIFT);
			switch(letter) {
				case ' ':
					auto.keyPress(KeyEvent.VK_SPACE);
					auto.keyRelease(KeyEvent.VK_SPACE);
					break;
				case 'A':
				case 'a':
					auto.keyPress(KeyEvent.VK_A);
					auto.keyRelease(KeyEvent.VK_A);
					break;
				case 'B':
				case 'b':
					auto.keyPress(KeyEvent.VK_B);
					auto.keyRelease(KeyEvent.VK_B);
					break;
				case 'C':
				case 'c':
					auto.keyPress(KeyEvent.VK_C);
					auto.keyRelease(KeyEvent.VK_C);
					break;
				case 'D':
				case 'd':
					auto.keyPress(KeyEvent.VK_D);
					auto.keyRelease(KeyEvent.VK_D);
					break;
				case 'E':
				case 'e':
					auto.keyPress(KeyEvent.VK_E);
					auto.keyRelease(KeyEvent.VK_E);
					break;
				case 'F':
				case 'f':
					auto.keyPress(KeyEvent.VK_F);
					auto.keyRelease(KeyEvent.VK_F);
					break;
				case 'G':
				case 'g':
					auto.keyPress(KeyEvent.VK_G);
					auto.keyRelease(KeyEvent.VK_G);
					break;
				case 'H':
				case 'h':
					auto.keyPress(KeyEvent.VK_H);
					auto.keyRelease(KeyEvent.VK_H);
					break;
				case 'I':
				case 'i':
					auto.keyPress(KeyEvent.VK_I);
					auto.keyRelease(KeyEvent.VK_I);
					break;
				case 'J':
				case 'j':
					auto.keyPress(KeyEvent.VK_J);
					auto.keyRelease(KeyEvent.VK_J);
					break;
				case 'K':
				case 'k':
					auto.keyPress(KeyEvent.VK_K);
					auto.keyRelease(KeyEvent.VK_K);
					break;
				case 'L':
				case 'l':
					auto.keyPress(KeyEvent.VK_L);
					auto.keyRelease(KeyEvent.VK_L);
					break;
				case 'M':
				case 'm':
					auto.keyPress(KeyEvent.VK_M);
					auto.keyRelease(KeyEvent.VK_M);
					break;
				case 'N':
				case 'n':
					auto.keyPress(KeyEvent.VK_N);
					auto.keyRelease(KeyEvent.VK_N);
					break;
				case 'O':
				case 'o':
					auto.keyPress(KeyEvent.VK_O);
					auto.keyRelease(KeyEvent.VK_O);
					break;
				case 'P':
				case 'p':
					auto.keyPress(KeyEvent.VK_P);
					auto.keyRelease(KeyEvent.VK_P);
					break;
				case 'Q':
				case 'q':
					auto.keyPress(KeyEvent.VK_Q);
					auto.keyRelease(KeyEvent.VK_Q);
					break;
				case 'R':
				case 'r':
					auto.keyPress(KeyEvent.VK_R);
					auto.keyRelease(KeyEvent.VK_R);
					break;
				case 'S':
				case 's':
					auto.keyPress(KeyEvent.VK_S);
					auto.keyRelease(KeyEvent.VK_S);
					break;
				case 'T':
				case 't':
					auto.keyPress(KeyEvent.VK_T);
					auto.keyRelease(KeyEvent.VK_T);
					break;
				case 'U':
				case 'u':
					auto.keyPress(KeyEvent.VK_U);
					auto.keyRelease(KeyEvent.VK_U);
					break;
				case 'V':
				case 'v':
					auto.keyPress(KeyEvent.VK_V);
					auto.keyRelease(KeyEvent.VK_V);
					break;
				case 'W':
				case 'w':
					auto.keyPress(KeyEvent.VK_W);
					auto.keyRelease(KeyEvent.VK_W);
					break;
				case 'X':
				case 'x':
					auto.keyPress(KeyEvent.VK_X);
					auto.keyRelease(KeyEvent.VK_X);
					break;
				case 'Y':
				case 'y':
					auto.keyPress(KeyEvent.VK_Y);
					auto.keyRelease(KeyEvent.VK_Y);
					break;
				case 'Z':
				case 'z':
					auto.keyPress(KeyEvent.VK_Z);
					auto.keyRelease(KeyEvent.VK_Z);
					break;
				case '0':
					auto.keyPress(KeyEvent.VK_0);
					auto.keyRelease(KeyEvent.VK_0);
					break;
				case '1':
					auto.keyPress(KeyEvent.VK_1);
					auto.keyRelease(KeyEvent.VK_1);
					break;
				case '2':
					auto.keyPress(KeyEvent.VK_2);
					auto.keyRelease(KeyEvent.VK_2);
					break;
				case '3':
					auto.keyPress(KeyEvent.VK_3);
					auto.keyRelease(KeyEvent.VK_3);
					break;
				case '4':
					auto.keyPress(KeyEvent.VK_4);
					auto.keyRelease(KeyEvent.VK_4);
					break;
				case '5':
					auto.keyPress(KeyEvent.VK_5);
					auto.keyRelease(KeyEvent.VK_5);
					break;
				case '6':
					auto.keyPress(KeyEvent.VK_6);
					auto.keyRelease(KeyEvent.VK_6);
					break;
				case '7':
					auto.keyPress(KeyEvent.VK_7);
					auto.keyRelease(KeyEvent.VK_7);
					break;
				case '8':
					auto.keyPress(KeyEvent.VK_8);
					auto.keyRelease(KeyEvent.VK_8);
					break;
				case '9':
					auto.keyPress(KeyEvent.VK_9);
					auto.keyRelease(KeyEvent.VK_9);
					break;
				case ')':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_0);
					auto.keyRelease(KeyEvent.VK_0);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '!':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_1);
					auto.keyRelease(KeyEvent.VK_1);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '@':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_2);
					auto.keyRelease(KeyEvent.VK_2);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '#':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_3);
					auto.keyRelease(KeyEvent.VK_3);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '$':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_4);
					auto.keyRelease(KeyEvent.VK_4);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '%':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_5);
					auto.keyRelease(KeyEvent.VK_5);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '^':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_6);
					auto.keyRelease(KeyEvent.VK_6);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '&':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_7);
					auto.keyRelease(KeyEvent.VK_7);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '*':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_8);
					auto.keyRelease(KeyEvent.VK_8);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '(':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_9);
					auto.keyRelease(KeyEvent.VK_9);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '`':
					auto.keyPress(KeyEvent.VK_BACK_QUOTE);
					auto.keyRelease(KeyEvent.VK_BACK_QUOTE);
					break;
				case '~':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_BACK_QUOTE);
					auto.keyRelease(KeyEvent.VK_BACK_QUOTE);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '|':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_BACK_SLASH);
					auto.keyRelease(KeyEvent.VK_BACK_SLASH);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '-':
					auto.keyPress(KeyEvent.VK_MINUS);
					auto.keyRelease(KeyEvent.VK_MINUS);
					break;
				case '_':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_MINUS);
					auto.keyRelease(KeyEvent.VK_MINUS);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '=':
					auto.keyPress(KeyEvent.VK_EQUALS);
					auto.keyRelease(KeyEvent.VK_EQUALS);
					break;
				case '+':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_EQUALS);
					auto.keyRelease(KeyEvent.VK_EQUALS);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '[':
					auto.keyPress(KeyEvent.VK_BRACELEFT);
					auto.keyRelease(KeyEvent.VK_BRACELEFT);
					break;
				case '{':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_BRACELEFT);
					auto.keyRelease(KeyEvent.VK_BRACELEFT);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case ']':
					auto.keyPress(KeyEvent.VK_BRACERIGHT);
					auto.keyRelease(KeyEvent.VK_BRACERIGHT);
					break;
				case '}':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_BRACERIGHT);
					auto.keyRelease(KeyEvent.VK_BRACERIGHT);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case ';':
					auto.keyPress(KeyEvent.VK_SEMICOLON);
					auto.keyRelease(KeyEvent.VK_SEMICOLON);
					break;
				case ':':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_SEMICOLON);
					auto.keyRelease(KeyEvent.VK_SEMICOLON);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case ',':
					auto.keyPress(KeyEvent.VK_COMMA);
					auto.keyRelease(KeyEvent.VK_COMMA);
					break;
				case '<':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_COMMA);
					auto.keyRelease(KeyEvent.VK_COMMA);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '.':
					auto.keyPress(KeyEvent.VK_PERIOD);
					auto.keyRelease(KeyEvent.VK_PERIOD);
					break;
				case '>':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_PERIOD);
					auto.keyRelease(KeyEvent.VK_PERIOD);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '/':
					auto.keyPress(KeyEvent.VK_SLASH);
					auto.keyRelease(KeyEvent.VK_SLASH);
					break;
				case '?':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_SLASH);
					auto.keyRelease(KeyEvent.VK_SLASH);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '\\':
					auto.keyPress(KeyEvent.VK_BACK_SLASH);
					auto.keyRelease(KeyEvent.VK_BACK_SLASH);
					break;
				case '\n':
					auto.keyPress(KeyEvent.VK_ENTER);
					auto.keyRelease(KeyEvent.VK_ENTER);
					break;
				case '\t':
					auto.keyPress(KeyEvent.VK_TAB);
					auto.keyRelease(KeyEvent.VK_TAB);
					break;
				case '\"':
					auto.keyPress(KeyEvent.VK_SHIFT);
					auto.keyPress(KeyEvent.VK_QUOTE);
					auto.keyRelease(KeyEvent.VK_QUOTE);
					auto.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case '\'':
					auto.keyPress(KeyEvent.VK_QUOTE);
					auto.keyRelease(KeyEvent.VK_QUOTE);
					break;
			}
			if ((int)(letter) >= 65 && (int)(letter) <= 90)
				auto.keyRelease(KeyEvent.VK_SHIFT);
		}
	}
	public void button(String button, int type) {
		if (button.equals("enter") && type < 3)
			auto.keyPress(KeyEvent.VK_ENTER);
		else if (button.equals("backspace") && type < 3)
			auto.keyPress(KeyEvent.VK_BACK_SPACE);
		else if (button.equals("delete") && type < 3)
			auto.keyPress(KeyEvent.VK_DELETE);
		else if (button.equals("control") && type < 3)
			auto.keyPress(KeyEvent.VK_CONTROL);
		else if (button.equals("up") && type < 3)
			auto.keyPress(KeyEvent.VK_UP);
		else if (button.equals("down") && type < 3)
			auto.keyPress(KeyEvent.VK_DOWN);
		else if (button.equals("left") && type < 3)
			auto.keyPress(KeyEvent.VK_LEFT);
		else if (button.equals("right") && type < 3)
			auto.keyPress(KeyEvent.VK_RIGHT);
		else if (button.equals("shift") && type < 3)
			auto.keyPress(KeyEvent.VK_SHIFT);
		else if (button.equals("alt") && type < 3)
			auto.keyPress(KeyEvent.VK_ALT);
		else if (button.equals("capslock") && type < 3)
			auto.keyPress(KeyEvent.VK_CAPS_LOCK);
		else if (button.equals("escape") && type < 3)
			auto.keyPress(KeyEvent.VK_ESCAPE);
		if (button.equals("enter") && type > 1)
			auto.keyRelease(KeyEvent.VK_ENTER);
		else if (button.equals("backspace") && type > 1)
			auto.keyRelease(KeyEvent.VK_BACK_SPACE);
		else if (button.equals("delete") && type > 1)
			auto.keyRelease(KeyEvent.VK_DELETE);
		else if (button.equals("control") && type > 1)
			auto.keyRelease(KeyEvent.VK_CONTROL);
		else if (button.equals("up") && type > 1)
			auto.keyRelease(KeyEvent.VK_UP);
		else if (button.equals("down") && type > 1)
			auto.keyRelease(KeyEvent.VK_DOWN);
		else if (button.equals("left") && type > 1)
			auto.keyRelease(KeyEvent.VK_LEFT);
		else if (button.equals("right") && type > 1)
			auto.keyRelease(KeyEvent.VK_RIGHT);
		else if (button.equals("shift") && type > 1)
			auto.keyRelease(KeyEvent.VK_SHIFT);
		else if (button.equals("alt") && type > 1)
			auto.keyRelease(KeyEvent.VK_ALT);
		else if (button.equals("capslock") && type > 1)
			auto.keyRelease(KeyEvent.VK_CAPS_LOCK);
		else if (button.equals("escape") && type > 1)
			auto.keyRelease(KeyEvent.VK_ESCAPE);
	}
	public BufferedImage screenshot() {
		return auto.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}
	public BufferedImage screenshot(int x, int y, int w, int h) {
		return auto.createScreenCapture(new Rectangle(x, y, w, h));
	}
}