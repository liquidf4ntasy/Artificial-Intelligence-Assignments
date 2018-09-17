import java.util.*;

public class LogicalExpression {

    private String uniqueSymbol = null; 
    private String connective = null; 
    private Vector<LogicalExpression> subexpressions = null; 
                                                             
    private final String TRUE = "T";
    private final String FALSE = "F";

    public static Stack<String> symbol_stack = new Stack<String>();
    private static boolean final_result;

    // constructor
    public LogicalExpression() {
        this.subexpressions = new Vector<LogicalExpression>();
    }

    public LogicalExpression(LogicalExpression oldExpression) {

        if (oldExpression.getUniqueSymbol() == null) {
            this.uniqueSymbol = oldExpression.getUniqueSymbol();
        } else {
            // create a new logical expression from the one passed to it
            this.connective = oldExpression.getConnective();

            // hint, enumerate over the subexpression vector of newExpression
            for (Enumeration e = oldExpression.getSubexpressions().elements(); e.hasMoreElements();) {
                LogicalExpression nextExpression = (LogicalExpression) e.nextElement();

                this.subexpressions.add(nextExpression);
            }
        }

    }

    public void setUniqueSymbol(String newSymbol) {
        boolean valid = true;

        // remove the leading whitespace
        newSymbol.trim();

        if (this.uniqueSymbol != null) {
            System.out.println("setUniqueSymbol(): - this LE already has a unique symbol!!!" + "\nswapping :->"
                + this.uniqueSymbol + "<- for ->" + newSymbol + "<-\n");
        } else if (valid) {
            this.uniqueSymbol = newSymbol;
        }
    }

    public String setConnective(String inputString) {

        String connect;

        inputString.trim();
        if (inputString.startsWith("(")) {
            inputString = inputString.substring(inputString.indexOf('('), inputString.length());

            // trim the whitespace
            inputString.trim();
        }

        if (inputString.contains(" ")) {
            connect = inputString.substring(0, inputString.indexOf(" "));
            inputString = inputString.substring((connect.length() + 1), inputString.length());

        } else {
            connect = inputString;
            inputString = "";
        }

        if (connect.equalsIgnoreCase("if") || connect.equalsIgnoreCase("iff") || connect.equalsIgnoreCase("and")
            || connect.equalsIgnoreCase("or") || connect.equalsIgnoreCase("xor") || connect.equalsIgnoreCase("not")) {
            // ok, first word in the string is a valid connective

            // set the connective
            this.connective = connect;

            return inputString;

        } else {
            System.out.println("unexpected character!!! : invalid connective!! - setConnective():-" + inputString);
            this.exit_function(0);
        }

        System.out.println(" invalid connective! : setConnective:-" + inputString);
        return inputString;
    }

    public void setSubexpression(LogicalExpression newSub) {
        this.subexpressions.add(newSub);
    }

    public void setSubexpressions(Vector<LogicalExpression> symbols) {
        this.subexpressions = symbols;

    }

    public String getUniqueSymbol() {
        return this.uniqueSymbol;
    }

    public String getConnective() {
        return this.connective;
    }

    public LogicalExpression getNextSubexpression() {
        return this.subexpressions.lastElement();
    }

    public Vector getSubexpressions() {
        return this.subexpressions;
    }


    public void print_expression(String separator) {

        if (this.uniqueSymbol != null) {
            System.out.print(this.uniqueSymbol.toUpperCase());
        } else {
            LogicalExpression nextExpression;

            // print the connective
            System.out.print("(" + this.connective.toUpperCase());

            // enumerate over the 'symbols' ( LogicalExpression objects ) and print them
            for (Enumeration e = this.subexpressions.elements(); e.hasMoreElements();) {
                nextExpression = (LogicalExpression) e.nextElement();

                System.out.print(" ");
                nextExpression.print_expression("");
                System.out.print(separator);
            }

            System.out.print(")");
        }
    }
    public boolean solve_expressions(HashMap<String, Boolean> model) {

        if (this.getUniqueSymbol() != null) {
            symbol_stack.push(this.getUniqueSymbol());
        } else {
            LogicalExpression nextExpression;

            symbol_stack.push(this.getConnective());

            // enumerate over the 'symbols' ( LogicalExpression objects ) and print them
            for (Enumeration e = this.subexpressions.elements(); e.hasMoreElements();) {
                nextExpression = (LogicalExpression) e.nextElement();

                nextExpression.solve_expressions(model);
            }

            final_result = popUniqueSymbolsAndEvaluateResult(model);
        }
        return final_result;
    }

    private boolean popUniqueSymbolsAndEvaluateResult(HashMap<String, Boolean> model) {
        ArrayList<String> uniqueSymbole = new ArrayList<String>();
        String symbol, connective;
        boolean result = false;

        do {
            symbol = symbol_stack.pop();
            uniqueSymbole.add(symbol);
        } while (!isConnective(symbol));

        uniqueSymbole.remove(symbol);
        connective = symbol;

        if (connective.equalsIgnoreCase("or")) { // can have more than two unique symbols
            result = false;
            while (!uniqueSymbole.isEmpty() && !result) {
                result = result || getValue(uniqueSymbole.remove(0), model);
            }
        } else if (connective.equalsIgnoreCase("and")) { // can have more than two unique symbols
            result = true;
            while (!uniqueSymbole.isEmpty() && result) {
                result = result && getValue(uniqueSymbole.remove(0), model);
            }
        } else if (connective.equalsIgnoreCase("not")) {
            result = true;
            result = !getValue(uniqueSymbole.remove(0), model);
        } else if (connective.equalsIgnoreCase("xor")) { // result = a'b + ab'
                                                         
            result = false;
            int no_of_true_symbol = 0;
            while (!uniqueSymbole.isEmpty()) {
                if (getValue(uniqueSymbole.remove(0), model)) {
                    no_of_true_symbol++;
                }
            }
            if (no_of_true_symbol == 1) {
                result = true;
            }
        } else if (connective.equalsIgnoreCase("if")) { // required exactly two symbols
            result = true;
            if (uniqueSymbole.size() == 2) {
                if (getValue(uniqueSymbole.get(1), model) && !getValue(uniqueSymbole.get(0), model)) {
                    result = false;
                }
            }
        } else if (connective.equalsIgnoreCase("iff")) { // result = a'b' + ab
                                                        
            result = false;
            if (uniqueSymbole.size() == 2) {
                boolean symbol1 = getValue(uniqueSymbole.get(1), model);
                boolean symbol2 = getValue(uniqueSymbole.get(0), model);
                if ((!symbol1 && !symbol2) || (symbol1 && symbol2)) {
                    result = true;
                }
            }
        } else {
            System.out.println("Oops..incorrect connective!!");
        }

        if (result) { 
            symbol_stack.push(TRUE);
        } else {
            symbol_stack.push(FALSE);
        }

        return result;
    }

    private boolean isConnective(String symbol) {
        return (symbol.equalsIgnoreCase("if") || symbol.equalsIgnoreCase("iff") || symbol.equalsIgnoreCase("and")
            || symbol.equalsIgnoreCase("or") || symbol.equalsIgnoreCase("xor") || symbol.equalsIgnoreCase("not"));
    }

    private boolean getValue(String symbol, HashMap<String, Boolean> model) {
        if (symbol.equalsIgnoreCase(TRUE)) {
            return true;
        } else if (symbol.equalsIgnoreCase(FALSE)) {
            return false;
        } else if (model.get(symbol) == null) {
            return CheckTrueFalse.getValueFromArray(symbol);
        } else {
            return model.get(symbol);
        }
    }

    public static void clearStack() {
        if (symbol_stack != null) {
            symbol_stack.clear();
        }
    }

    private static void exit_function(int value) {
        System.out.println("exiting from LogicalExpression");
        System.exit(value);
    }
}
