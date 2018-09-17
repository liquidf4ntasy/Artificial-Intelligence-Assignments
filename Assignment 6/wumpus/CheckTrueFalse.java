import java.io.*;
import java.util.*;

public class CheckTrueFalse {

    public static final int TRUE = 1;
    public static final int FALSE = 0;
    public static final int UNKNOWN = -1;
    public static final int NO_OF_ROW_COLS = 5;
    public static final String UNDERSCORE = "_";
    public static final String RESULT_FILE = "result.txt";

    public static int MONSTER[][] = new int[5][5];
    public static int PIT[][] = new int[5][5];
    public static int STENCH[][] = new int[5][5];
    public static int BREEZE[][] = new int[5][5];
    public static int counter = 0;

    public static boolean isComputingNegationStatement = false;

    public static ArrayList<String> symbols = new ArrayList<String>();
    public static ArrayList<String> symbols2;
    public static HashMap<String, Boolean> model = new HashMap<String, Boolean>();

    /**
     * @param args
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Usage: " + args[0] + " [wumpus-rules-file] [additional-knowledge-file] [input_file]\n");
            exit_function(0);
        }

        assign_default_symbol_values();

        String buffer;
        BufferedReader inputStream;
        BufferedWriter outputStream;

        // create the knowledge base and the statement
        LogicalExpression knowledge_base = new LogicalExpression();
        LogicalExpression statement = new LogicalExpression();
        // LogicalExpression negation_statement = new LogicalExpression();

        // open the wumpus_rules.txt
        try {
            inputStream = new BufferedReader(new FileReader(args[0]));

            // load the wumpus rules
            System.out.println("loading the wumpus rules...");
            knowledge_base.setConnective("and");

            while ((buffer = inputStream.readLine()) != null) {
                if (!(buffer.startsWith("#") || (buffer.equals("")))) {
                    // the line is not a comment
                    checkAndInitializeSymbolValue(buffer); // B_2_3 or (not M_2_3)
                    LogicalExpression subExpression = readExpression(buffer);
                    knowledge_base.setSubexpression(subExpression);
                }
            }

            // close the input file
            inputStream.close();

        } catch (Exception e) {
            System.out.println("failed to open " + args[0]);
            e.printStackTrace();
            exit_function(0);
        }
        try {
            inputStream = new BufferedReader(new FileReader(args[1]));

            System.out.println("loading the additional knowledge...");
            while ((buffer = inputStream.readLine()) != null) {
                if (!(buffer.startsWith("#") || (buffer.equals("")))) {
                    checkAndInitializeSymbolValue(buffer); // B_2_3 or (not M_2_3)
                    LogicalExpression subExpression = readExpression(buffer);
                    knowledge_base.setSubexpression(subExpression);
                }
            }
            inputStream.close();

        } catch (Exception e) {
            System.out.println("failed to open " + args[1]);
            e.printStackTrace();
            exit_function(0);
        }
        if (!valid_expression(knowledge_base)) {
            System.out.println("invalid knowledge base");
            exit_function(0);
        }
        knowledge_base.print_expression("\n");

        try {
            inputStream = new BufferedReader(new FileReader(args[2]));

            System.out.println("\n\nLoading the statement file...");
            while ((buffer = inputStream.readLine()) != null) {
                if (!buffer.startsWith("#")) {
                    statement = readExpression(buffer);
                    break;
                }
            }

            // close the input file
            inputStream.close();

        } catch (Exception e) {
            System.out.println("failed to open " + args[2]);
            e.printStackTrace();
            exit_function(0);
        }
        if (!valid_expression(statement)) {
            System.out.println("invalid statement");
            exit_function(0);
        }

        // print the statement
        statement.print_expression("");
        // print a new line
        System.out.println("\n");

        createUnknownSysmbolList();
        symbols2 = new ArrayList<String>(symbols);
        System.out.println("\nProcessing....");
        boolean tt_entails_alpha = TT_Entails(knowledge_base, statement, symbols, model);
        counter = 0;
        isComputingNegationStatement = true;
        boolean tt_entails_negation_alpha = TT_Entails(knowledge_base, statement, symbols2, model);
        isComputingNegationStatement = false;
        evaluateFinalResult(tt_entails_alpha, tt_entails_negation_alpha);
    } // end of main


    private static boolean TT_Entails(LogicalExpression knowledge_base, LogicalExpression alpha,
        ArrayList<String> symbols_list, HashMap<String, Boolean> model) {
        // TODO Auto-generated method stub
        return TT_Check_All(knowledge_base, alpha, symbols_list, model);
    }
    private static boolean TT_Check_All(LogicalExpression knowledge_base, LogicalExpression alpha, ArrayList<String> symbols,
        HashMap<String, Boolean> model) {
        // TODO Auto-generated method stub

        if (symbols.isEmpty()) {
            if (PL_TRUE(knowledge_base, model, false)) {
                return PL_TRUE(alpha, model, isComputingNegationStatement);
            } else {
                return true;
            }
        } else {
            String P = symbols.remove(0);
            ArrayList<String> rest = new ArrayList<String>(symbols);

            return TT_Check_All(knowledge_base, alpha, rest, EXTENDS(P, true, model))
                && TT_Check_All(knowledge_base, alpha, rest, EXTENDS(P, false, model));
        }
    }

    /**
     * This function implements ETENDS() method of 'Entailment' algorithm. it assigns true or false value to given symbol
     * */
    private static HashMap<String, Boolean> EXTENDS(String P, boolean value, HashMap<String, Boolean> model) {
        model.put(P, value);
        return model;
    }

    /**
     * This function implements PL_TRUE() method of 'Entailment' algorithm. It checks weather given logical statement is true
     * for given model
     * @return true if statement evaluates to 'true' for given model, false otherwise
     * */
    private static boolean PL_TRUE(LogicalExpression logical_statement, HashMap<String, Boolean> model,
        boolean isComputingNegationStatement) {
        // TODO Auto-generated method stub

        boolean result = logical_statement.solve_expressions(model);

        LogicalExpression.clearStack();

        if (isComputingNegationStatement) {
            return !result;
        } else {
            return result;
        }
    }

    private static void createUnknownSysmbolList() {
        // TODO Auto-generated method stub

        for (int i = 1; i < NO_OF_ROW_COLS; i++) {
            for (int j = 1; j < NO_OF_ROW_COLS; j++) {
                if (MONSTER[i][j] == UNKNOWN) {
                    symbols.add("M_" + i + "_" + j);
                    model.put("M_" + i + "_" + j, false);
                }
            }
        }
        for (int i = 1; i < NO_OF_ROW_COLS; i++) {
            for (int j = 1; j < NO_OF_ROW_COLS; j++) {
                if (PIT[i][j] == UNKNOWN) {
                    symbols.add("P_" + i + "_" + j);
                    model.put("P_" + i + "_" + j, false);
                }
            }
        }
        for (int i = 1; i < NO_OF_ROW_COLS; i++) {
            for (int j = 1; j < NO_OF_ROW_COLS; j++) {
                if (STENCH[i][j] == UNKNOWN) {
                    symbols.add("S_" + i + "_" + j);
                    model.put("S_" + i + "_" + j, false);
                }
            }
        }
        for (int i = 1; i < NO_OF_ROW_COLS; i++) {
            for (int j = 1; j < NO_OF_ROW_COLS; j++) {
                if (BREEZE[i][j] == UNKNOWN) {
                    symbols.add("B_" + i + "_" + j);
                    model.put("B_" + i + "_" + j, false);
                }
            }
        }
    }

    /**
     * This function initialize all symbols to default unknown value -1.
     * */
    private static void assign_default_symbol_values() {

        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                MONSTER[i][j] = UNKNOWN;
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                PIT[i][j] = UNKNOWN;
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                STENCH[i][j] = UNKNOWN;
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                BREEZE[i][j] = UNKNOWN;
            }
        }
    }

    /**
     * This function prints values for all symbols. Use this only for testing
     * */
    private static void print_symbol_values() {

        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                System.out.println("M_" + i + "_" + j + " = " + MONSTER[i][j]);
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                System.out.println("P_" + i + "_" + j + " = " + PIT[i][j]);
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                System.out.println("S_" + i + "_" + j + " = " + STENCH[i][j]);
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                System.out.println("B_" + i + "_" + j + " = " + BREEZE[i][j]);
            }
        }
    }

    /**
     * This method checks for KB lines like 'B_2_3' or '(not M_2_3)' in order to initialize true or false values for symbols
     * @param line read line from knowledge file
     */
    private static void checkAndInitializeSymbolValue(String line) {
        int values_to_assign = TRUE;
        String symbol = line;
        String symbol_initials = null;
        String[] symbol_literals = new String[3];

        if (!line.startsWith("(")) { // checks if line contains only unique symbol i.e 'B_2_3'
            values_to_assign = TRUE;
        } else if ((line.startsWith("(not") || line.startsWith("(NOT"))
            && !(line.startsWith("(not (") || line.startsWith("(NOT ("))) { // checks if line contains only negation of symbol
                                                                            // i.e '(not M_2_3)'
            values_to_assign = FALSE;
            symbol = line.substring(line.indexOf(" ") + 1, line.indexOf(")"));
        } else {
            return;
        }

        symbol_literals = symbol.split(UNDERSCORE);
        symbol_initials = symbol_literals[0];
        int pos_x = Integer.parseInt(symbol_literals[1]);
        int pos_y = Integer.parseInt(symbol_literals[2]);

        if (symbol_initials.equals("M")) {
            MONSTER[pos_x][pos_y] = values_to_assign;
        } else if (symbol_initials.equals("P")) {
            PIT[pos_x][pos_y] = values_to_assign;
        } else if (symbol_initials.equals("S")) {
            STENCH[pos_x][pos_y] = values_to_assign;
        } else if (symbol_initials.equals("B")) {
            BREEZE[pos_x][pos_y] = values_to_assign;
        } else {
            System.out.println("Oops...Incorrect knowlwdge base format!!");
        }

    }

    public static boolean getValueFromArray(String symbol) {

        String symbol_initials = null;
        String[] symbol_literals = new String[3];

        symbol_literals = symbol.split(UNDERSCORE);
        symbol_initials = symbol_literals[0];
        int pos_x = Integer.parseInt(symbol_literals[1]);
        int pos_y = Integer.parseInt(symbol_literals[2]);

        if (symbol_initials.equals("M")) {
            if (MONSTER[pos_x][pos_y] == TRUE) {
                return true;
            } else {
                return false;
            }
        } else if (symbol_initials.equals("P")) {
            if (PIT[pos_x][pos_y] == TRUE) {
                return true;
            } else {
                return false;
            }
        } else if (symbol_initials.equals("S")) {
            if (STENCH[pos_x][pos_y] == TRUE) {
                return true;
            } else {
                return false;
            }
        } else if (symbol_initials.equals("B")) {
            if (BREEZE[pos_x][pos_y] == TRUE) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Oops...Incorrect Symbol format!!");
        }
        return false;
    }

    public static LogicalExpression readExpression(String input_string) {
        LogicalExpression result = new LogicalExpression();

        input_string = input_string.trim();

        if (input_string.startsWith("(")) {
            String symbolString = "";
            symbolString = input_string.substring(1);

            if (!symbolString.endsWith(")")) {
                System.out.println("missing ')' !!! - invalid expression! - readExpression():-" + symbolString);
                exit_function(0);
            } else {
                symbolString = symbolString.substring(0, (symbolString.length() - 1));
                symbolString.trim();
                symbolString = result.setConnective(symbolString);
            }
            result.setSubexpressions(read_subexpressions(symbolString));
        } else {
            result.setUniqueSymbol(input_string);
        }

        return result;
    }
    public static Vector<LogicalExpression> read_subexpressions(String input_string) {

        Vector<LogicalExpression> symbolList = new Vector<LogicalExpression>();
        LogicalExpression newExpression;// = new LogicalExpression();
        String newSymbol = new String();

        input_string.trim();
        while (input_string.length() > 0) {
            newExpression = new LogicalExpression();
            if (input_string.startsWith("(")) {
                // its a subexpression.
                // have readExpression parse it into a LogicalExpression object
                // find the matching ')'
                int parenCounter = 1;
                int matchingIndex = 1;
                while ((parenCounter > 0) && (matchingIndex < input_string.length())) {
                    if (input_string.charAt(matchingIndex) == '(') {
                        parenCounter++;
                    } else if (input_string.charAt(matchingIndex) == ')') {
                        parenCounter--;
                    }
                    matchingIndex++;
                }

                newSymbol = input_string.substring(0, matchingIndex);
                newExpression = readExpression(newSymbol);
                symbolList.add(newExpression);
                input_string = input_string.substring(newSymbol.length(), input_string.length());
            } else {

                if (input_string.contains(" ")) {
                    // remove the first string from the string
                    newSymbol = input_string.substring(0, input_string.indexOf(" "));
                    input_string = input_string.substring((newSymbol.length() + 1), input_string.length());
                } else {
                    newSymbol = input_string;
                    input_string = "";
                }
                newExpression.setUniqueSymbol(newSymbol);
                symbolList.add(newExpression);
            }

            input_string.trim();

            if (input_string.startsWith(" ")) {
                input_string = input_string.substring(1);
            }
        }
        return symbolList;
    }

    public static boolean valid_expression(LogicalExpression expression) {
        if (!(expression.getUniqueSymbol() == null) && (expression.getConnective() == null)) {
            return valid_symbol(expression.getUniqueSymbol());
        }
        if ((expression.getConnective().equalsIgnoreCase("if")) || (expression.getConnective().equalsIgnoreCase("iff"))) {

            if (expression.getSubexpressions().size() != 2) {
                System.out.println("error: connective \"" + expression.getConnective() + "\" with "
                    + expression.getSubexpressions().size() + " arguments\n");
                return false;
            }
        }

        else if (expression.getConnective().equalsIgnoreCase("not")) {
            if (expression.getSubexpressions().size() != 1) {
                System.out.println("error: connective \"" + expression.getConnective() + "\" with "
                    + expression.getSubexpressions().size() + " arguments\n");
                return false;
            }
        }

        else if ((!expression.getConnective().equalsIgnoreCase("and")) && (!expression.getConnective().equalsIgnoreCase("or"))
            && (!expression.getConnective().equalsIgnoreCase("xor"))) {
            System.out.println("error: unknown connective " + expression.getConnective() + "\n");
            return false;
        }
        for (Enumeration e = expression.getSubexpressions().elements(); e.hasMoreElements();) {
            LogicalExpression testExpression = (LogicalExpression) e.nextElement();
            if (!valid_expression(testExpression)) {
                return false;
            }
        }
        // if the method made it here, the expression must be valid
        return true;
    }

    /** this function checks to see if a unique symbol is valid */
    public static boolean valid_symbol(String symbol) {
        if (symbol == null || (symbol.length() == 0)) {
            return false;
        }

        for (int counter = 0; counter < symbol.length(); counter++) {
            if ((symbol.charAt(counter) != '_') && (!Character.isLetterOrDigit(symbol.charAt(counter)))) {

                System.out.println("String: " + symbol + " is invalid! Offending character:---" + symbol.charAt(counter)
                    + "---\n");

                return false;
            }
        }
        return true;
    }
	
    private static void evaluateFinalResult(boolean tt_entails_alpha, boolean tt_entails_negation_alpha) {
        // TODO Auto-generated method stub
        String decision = "I don't know if the statement is definitely true or definitely false.";
        if (tt_entails_alpha && !tt_entails_negation_alpha) {
            decision = "definitely true";
        } else if (!tt_entails_alpha && tt_entails_negation_alpha) {
            decision = "definitely false";
        } else if (!tt_entails_alpha && !tt_entails_negation_alpha) {
            decision = "possibly true, possibly false";
        } else if (tt_entails_alpha && tt_entails_negation_alpha) {
            decision = "both true and false";
        }

        System.out.println("Final Answer = " + decision);
        printDecisionToFile(decision, RESULT_FILE);

    }

    /**
     * This function writes entailment decision to text file.
     * */
    public static void printDecisionToFile(String decision, String resultFile) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(resultFile));

            // write the current turn
            output.write(decision + "\r\n");
            output.close();

        } catch (IOException e) {
            System.out.println("\nProblem writing to the result file!\n" + "Try again.");
            e.printStackTrace();
        }
    }

    private static void exit_function(int value) {
        System.out.println("exiting from checkTrueFalse");
        System.exit(value);
    }
}
