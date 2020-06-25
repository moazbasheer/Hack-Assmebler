package assembler;

public class Parser {
	private String dest, comp, jump;

	Parser(String s) {
		dest = "";
		int i = 0;
		if (isEqualFound(s)) {
			for (; i < s.length(); i++) {
				if (s.charAt(i) == '=')
					break;
				dest += s.charAt(i);
			}
			i++;
		}
		if (dest.equals(""))
			dest = "null";
		comp = "";
		for (; i < s.length(); i++) {
			if (s.charAt(i) == ';')
				break;
			comp += s.charAt(i);
		}
		i++;
		jump = "";
		for (; i < s.length(); i++) {
			jump += s.charAt(i);
		}
		if (jump.equals(""))
			jump = "null";

	}

	boolean isEqualFound(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '=') {
				return true;
			}
		}
		return false;
	}

	String getDest() {
		return dest;
	}

	String getComp() {
		return comp;
	}

	String getJump() {
		return jump;
	}
}
