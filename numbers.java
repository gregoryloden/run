public class numbers {
	public static String binarystyle(long number) {
		return binarystyle(number, 1);
	}
	public static String binarystyle(long number, int digits) {
		long test = 1;
		int power = 0;
		while (test < number) {
			power = power + 1;
			test = test << 1;
		}
		if (power < digits)
			power = digits;
		String binary = "";
		for (int count = 1; count <= power; count += 1) {
			if (number % 2 == 1 || number % 2 == -1)
				binary = "1" + binary;
			else
				binary = "0" + binary;
			number = number >> 1;
		}
		return binary;
	}
	public static int readint(String line) {
		char letter = ' ';
		byte digit = 0;
		int number = 0;
		boolean negative = false;
		while (line.length() > 0) {
			letter = line.charAt(0);
			switch(letter) {
				case '0':
					digit = 0;
					break;
				case '1':
					digit = 1;
					break;
				case '2':
					digit = 2;
					break;
				case '3':
					digit = 3;
					break;
				case '4':
					digit = 4;
					break;
				case '5':
					digit = 5;
					break;
				case '6':
					digit = 6;
					break;
				case '7':
					digit = 7;
					break;
				case '8':
					digit = 8;
					break;
				case '9':
					digit = 9;
					break;
				case '-':
					if (number == 0)
						negative = true;
					break;
				default:
					digit = -1;
					break;
			}
			if (digit >= 0) {
				if (number < 214748364 || (number == 214748364 && digit <= 7))
					number = number * 10 + digit;
				else
					return number;
			}
			line = line.substring(1, line.length());
		}
		if (negative)
			return -number;
		return number;
	}
	public static double readdouble(String line) {
		char letter = ' ';
		byte digit = 0;
		byte digits = 0;
		double number = 0;
		boolean ee = false;
		byte point = 0;
		byte power = 0;
		boolean negative = false;
		while (line.length() > 0) {
			letter = line.charAt(0);
			switch(letter) {
				case '0':
					digit = 0;
					digits = (byte)(digits + 1);
					break;
				case '1':
					digit = 1;
					digits = (byte)(digits + 1);
					break;
				case '2':
					digit = 2;
					digits = (byte)(digits + 1);
					break;
				case '3':
					digit = 3;
					digits = (byte)(digits + 1);
					break;
				case '4':
					digit = 4;
					digits = (byte)(digits + 1);
					break;
				case '5':
					digit = 5;
					digits = (byte)(digits + 1);
					break;
				case '6':
					digit = 6;
					digits = (byte)(digits + 1);
					break;
				case '7':
					digit = 7;
					digits = (byte)(digits + 1);
					break;
				case '8':
					digit = 8;
					digits = (byte)(digits + 1);
					break;
				case '9':
					digit = 9;
					digits = (byte)(digits + 1);
					break;
				case '-':
					if (number == 0)
						negative = true;
					break;
				case '.':
					if (point == 0)
						point = 1;
					digit = -1;
					break;
				case 'e':
					ee = true;
					digit = -1;
					break;
				default:
					digit = -1;
					break;
			}
			if (digit != -1) {
				if (!ee) {
					if (point < 1) {
						if (digits < 10)
							number = number * 10 + digit;
						else
							return number;
					} else {
						if (digits < 10) {
							number = number + digit * Math.pow(10, -point);
							point = (byte)(point + 1);
						} else
							return number;
					}
				} else {
					if (power * 10 + digit < 308)
						power = (byte)(power * 10 + digit);
				}
			}
			line = line.substring(1, line.length());
		}
		if (power > 0)
			number = number * Math.pow(10, power);
		if (negative)
			return -number;
		return number;
	}
}