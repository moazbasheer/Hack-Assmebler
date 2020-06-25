package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Application {
	HashMap<String, Integer> symbolTable;
	HashMap<String, String> destToCode;
	HashMap<String, String> compToCode;
	HashMap<String, String> jumpToCode;
	Integer memoryCursor;

	Application() throws FileNotFoundException {
		symbolTable = new HashMap<>();
		memoryCursor = new Integer(Constants.INIT_MEM_CURSOR);
		destToCode = new HashMap<>();
		compToCode = new HashMap<>();
		jumpToCode = new HashMap<>();
	}

	boolean isNum(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) < '0' || s.charAt(i) > '9') {
				return false;
			}
		}
		return true;
	}

	void loadPredefinedSymbols() throws FileNotFoundException {
		File file = new File(Constants.PRE_SYMBOL_SRC);
		Scanner scan = new Scanner(file);

		while (scan.hasNext()) {
			String lineCode = scan.next().trim();
			int code = scan.nextInt();
			symbolTable.put(lineCode, code);
		}
		scan.close();
	}

	void startFirstPass(String filename) {
		try {
			int currentLine = 0;
			File file = new File(filename);
			Scanner scan;
			scan = new Scanner(file);
			while (scan.hasNext()) {
				String lineCode = scan.next().trim();

				if (lineCode.equals("")) {
					continue;
				}
				if (lineCode.charAt(0) == '('
						&& lineCode.charAt(lineCode.length() - 1) == ')') {
					String s = lineCode.substring(1, lineCode.length() - 1);
					symbolTable.put(s, currentLine);
					System.out.println(s + " " + currentLine);
					continue;
				}
				currentLine++;
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	String toBinary(String s) {
		return "";
	}

	void startSecondPass(String filename) {
		try {
			Scanner scan = new Scanner(new File(filename + ".asm")); // Getting
																		// lines
			// from user
			FileWriter fileWriter;
			fileWriter = new FileWriter(new File(filename + ".hack"));
			System.out.println(filename);

			while (scan.hasNext()) {
				String line = scan.nextLine().trim();
				System.out.println(line);
				if (line.equals("")) {
					continue;
				}
				if (line.charAt(0) == '@') { // if A-instruction
					String A = line.substring(1);
					Integer AValue;
					if (isNum(A)) {
						AValue = new Integer(A);
					} else {
						if (symbolTable.containsKey(A)) { // if in the table.
							AValue = symbolTable.get(A);
						} else { // if not,
							AValue = memoryCursor;
							symbolTable.put(A, memoryCursor);
							memoryCursor++;
						}
					}
					String binary = Integer.toBinaryString(AValue);
					while (binary.length() < 15) {
						binary = "0" + binary;
					}
					fileWriter.write("0" + binary + "\n");
				} else { // if C-instruction
					if (line.charAt(0) == '(')
						continue;
					Parser parser = new Parser(line);
					String dest = parser.getDest();
					String comp = parser.getComp();
					String jump = parser.getJump();
					fileWriter.write("111");
					System.out.println(comp);
					fileWriter.write(compToCode.get(comp));
					fileWriter.write(destToCode.get(dest));
					System.out.println(jump);
					fileWriter.write(jumpToCode.get(jump));
					fileWriter.write("\n");
				}
			}
			scan.close();
			fileWriter.close();
		} catch (FileNotFoundException e) {
			System.out
					.println("This file is not found the program will terminate");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void loadCinstr() {
		try {
			Scanner scan = new Scanner(new File("destToCode.txt"));
			while (scan.hasNext()) {
				String instruction = scan.next();
				String code = scan.next();
				destToCode.put(instruction, code);
			}
			scan.close();
			scan = new Scanner(new File("compToCode.txt"));
			while (scan.hasNext()) {
				String instruction = scan.next();
				String code = scan.next();
				compToCode.put(instruction, code);
			}
			scan.close();
			scan = new Scanner(new File("jumpToCode.txt"));
			while (scan.hasNext()) {
				String instruction = scan.next();
				String code = scan.next();
				jumpToCode.put(instruction, code);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	void run() throws FileNotFoundException {
		System.out.println("Enter the file name"); // get filename from user
		Scanner scan = new Scanner(System.in);
		String temp = scan.nextLine();
		loadPredefinedSymbols();
		loadCinstr();
		startFirstPass(temp + ".asm");
		startSecondPass(temp);
		scan.close();
	}

	public static void main(String[] args) throws FileNotFoundException {
		Application app = new Application();
		app.run();
	}
}
