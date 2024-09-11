import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Welcome! this will 100% work ");
        System.out.println("Use '!help' to get some info ");
        try (Scanner inputScanner = new Scanner(System.in)) {
            boolean isRunning = true;
            while (isRunning) {
                System.out.print("(enter file name)> ");
                String userInput = inputScanner.nextLine();
                if (userInput.startsWith("!exit")) {
                    isRunning = false;
                    System.out.println("baiii >.<");

                } else if (userInput.startsWith("!help")) {
                    System.out.println(
                            "Welcome! This is a brainfuck interpreter written in java. To get started, at the prompt, just enter the filename of the code you want to run. Or, you can run some commands. \nCommands:\n!exit - exits the program\n!help - displays this help message\n!ex [command] - will run [command] in your native shell");

                } else if (userInput.startsWith("!ex")) {
                    String Command = userInput.replaceFirst("!ex", "");
                    System.out.println("<-- BEGINNING OF SHELL OUTPUT -->\n");
                    ShellExecutor.runCommand(Command);
                    System.out.println("\n<-- END OF SHELL OUTPUT -->");
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

class ShellExecutor {
    public static void runCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        } else {
            processBuilder = new ProcessBuilder("bash", "-c", command);
        }

        Process process = processBuilder.start();
        process.waitFor();

        try (BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = processReader.readLine()) != null) {
                System.out.println(line);
            }
        }
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
        }
    }

}
