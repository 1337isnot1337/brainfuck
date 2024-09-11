import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome! this will 100% work ");
        try (Scanner inputScanner = new Scanner(System.in)) {
            boolean isRunning = true;
            while (isRunning) {
                System.out.print("> ");
                String userInput = inputScanner.nextLine();
                if (userInput.startsWith("!exit")) {
                    isRunning = false;
                    System.out.println("baiii >.<");

                } else {
                    String code = BrainfuckReader.getCode(userInput);
                    Interpreter.parse(code);
                    System.out.println("");

                }
            }
        }
    }
}

class Interpreter {
    public static void parse(String code) throws IOException {
        ArrayList<Integer> FullBuffer = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            FullBuffer.add(0);
        }
        int Pointer = 0;
        for (int i = 0; i < code.length(); i++) {
            char CurChar = code.charAt(i);

            switch (CurChar) {
                case '>': {
                    Pointer++;
                }
                    break;
                case '<': {
                    Pointer--;
                }
                    break;
                case '+': {
                    int CurVal = FullBuffer.get(Pointer);
                    CurVal = (CurVal + 1) % 256;
                    FullBuffer.set(Pointer, CurVal);
                }
                    break;
                case '-': {
                    int CurVal = FullBuffer.get(Pointer);
                    CurVal = (CurVal - 1 + 256) % 256;
                    FullBuffer.set(Pointer, CurVal);
                }
                    break;
                case '.': {
                    int CurVal = FullBuffer.get(Pointer);
                    char asciiChar = (char) (CurVal & 0xFF);
                    String asciiString = Character.toString(asciiChar);
                    System.out.print(asciiString);
                }
                    break;
                case '[': {
                    int CurVal = FullBuffer.get(Pointer);
                    if (CurVal == 0) {
                        int openBrackets = 1;
                        while (openBrackets > 0) {
                            i++;
                            if (code.charAt(i) == '[')
                                openBrackets++;
                            else if (code.charAt(i) == ']')
                                openBrackets--;
                        }
                    }
                }
                    break;
                case ']': {
                    int CurVal = FullBuffer.get(Pointer);
                    if (CurVal != 0) {
                        int closeBrackets = 1;
                        while (closeBrackets > 0) {
                            i--;
                            if (code.charAt(i) == ']')
                                closeBrackets++;
                            else if (code.charAt(i) == '[')
                                closeBrackets--;
                        }
                    }
                }
                    break;
                case ',': {
                    System.out.print("\n(input:) ");
                    int input = System.in.read();
                    FullBuffer.set(Pointer, input);

                }
                    break;
            }

        }

    }

}

class BrainfuckReader {
    public static String getCode(String fileName) {
        try {
            return Files.readString(new File(fileName).toPath());
        } catch (Exception err) {
            System.err.println(err);
        }
        return "";
    }
}
