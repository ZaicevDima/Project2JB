package zaicevdmitry.com;

import javafx.util.Pair;

import java.util.ArrayList;

public class Parser {
    static String getSelectExpression(String query) throws SyntaxError {
        int start = "SELECT ".length();
        int end = 0;
        if (!query.startsWith("SELECT ")) {
            throw new SyntaxError("Syntax error! Expected SELECT");
        }
        for (int i = 0; i < query.length(); i++) {
            if ((query.length() -  i > "SELECT ".length()) && query.substring(i).startsWith("SELECT ")) {
                start = i + "SELECT ".length();
            }
            if ((query.length() -  i > "FROM".length()) && query.substring(i).startsWith("FROM")) {
                end = i - 1;
                break;
            }
        }
        if (start > "SELECT ".length()) {
            throw new SyntaxError("SELECT must be one");
        }
        if (start < end) {
            return query.substring(start, end);
        } else {
            throw new SyntaxError("Syntax error! Expected expression in SELECT");
        }
    }

    private static int getEnd(String query, String typeExpression, int index, int end) {
        if ((index < query.length() - typeExpression.length()) &&
                query.substring(index, index + typeExpression.length()).equals(typeExpression)) {
            return index - 1;
        }
        return end;
    }

    private static int[] getStartEnd2(String query, String countOperator, ArrayList<String> operators) {
        int start = 0;
        int end = 0;
        boolean flag = false;
        for (int i = 0; i < query.length(); i++) {
            if (query.length() - i > countOperator.length() && query.substring(i, i + countOperator.length()).equals(countOperator)) {
                start = i + countOperator.length() + 1;
            }
            for (String operator : operators) {
                if (start > 0 && getEnd(query, operator, i, end) > 0) {
                    end = getEnd(query, operator, i, end);
                    flag = true;
                    break;
                }
            }
            if (flag) {
                break;
            }
        }
        return new int[]{start, end};
    }


    static String getFromExpression(String query) throws SyntaxError {
        ArrayList<String> operators = new ArrayList<String>();
        operators.add("WHERE");
        operators.add("GROUP BY");
        operators.add("ORDER BY");
        operators.add("LIMIT");
        operators.add("OFFSET");
        int start = getStartEnd2(query, "FROM", operators)[0];
        int end = getStartEnd2(query, "FROM", operators)[1];
        if ((start > 0) && (start < end) && (query.substring(start, end).charAt(0) != ' ')) {
            return query.substring(start, end);
        } else if (start > 0 && end == 0 && (query.substring(start).charAt(0) != ' ')) {
            return query.substring(start);
        } else {
            throw new SyntaxError("Syntax error! Expected table name in FROM");
        }
    }

    static String getWhereExpression(String query) throws SyntaxError {
        ArrayList<String> operators = new ArrayList<String>();
        operators.add("GROUP BY");
        operators.add("ORDER BY");
        operators.add("LIMIT");
        operators.add("OFFSET");
        int start = getStartEnd2(query, "WHERE", operators)[0];
        int end = getStartEnd2(query, "WHERE", operators)[1];

        if ((start > 0) && (start < end) && (query.substring(start, end).charAt(0) != ' ')) {
            return query.substring(start, end);
        } else if (start > 0 && end == 0 && (query.substring(start).charAt(0) != ' ')) {
            return query.substring(start);
        } else if (start == 0) {
            return "";
        } else {
            throw new SyntaxError("Syntax error! Expected expression in WHERE");
        }
    }

    static String getGroupExpression(String query) throws SyntaxError {
        ArrayList<String> operators = new ArrayList<String>();
        operators.add("ORDER BY");
        operators.add("LIMIT");
        operators.add("OFFSET");
        int start = getStartEnd2(query, "GROUP BY", operators)[0];
        int end = getStartEnd2(query, "GROUP BY", operators)[1];

        if ((start > 0) && (start < end) && (query.substring(start, end).charAt(0) != ' ')) {
            return query.substring(start, end);
        } else if (start > 0 && end == 0 && (query.substring(start).charAt(0) != ' ')) {
            return query.substring(start);
        } else if (start == 0) {
            return "";
        } else {
            throw new SyntaxError("Syntax error! Expected expression in GROUP BY");
        }
    }

    static String getOrderExpression(String query) throws SyntaxError {
        ArrayList<String> operators = new ArrayList<String>();
        operators.add("LIMIT");
        operators.add("OFFSET");
        int start = getStartEnd2(query, "ORDER BY", operators)[0];
        int end = getStartEnd2(query, "ORDER BY", operators)[1];

        if ((start > 0) && (start < end) && (query.substring(start, end).charAt(0) != ' ')) {
            return query.substring(start, end);
        } else if (start > 0 && end == 0 && (query.substring(start).charAt(0) != ' ')) {
            return query.substring(start);
        } else if (start == 0) {
            return "";
        } else {
            throw new SyntaxError("Syntax error! Expected arguments in ORDER BY");
        }
    }

    static String getLimitExpression(String query) throws SyntaxError {
        ArrayList<String> operators = new ArrayList<String>();
        operators.add("OFFSET");
        int start = getStartEnd2(query, "LIMIT", operators)[0];
        int end = getStartEnd2(query, "LIMIT", operators)[1];

        if ((start > 0) && (start < end) && (query.substring(start, end).charAt(0) != ' ')) {
            return query.substring(start, end);
        } else if (start > 0 && end == 0 && (query.substring(start).charAt(0) != ' ')) {
            return query.substring(start);
        } else if (start == 0) {
            return "";
        } else {
            throw new SyntaxError("Syntax error! Expected arguments in LIMIT");
        }
    }

    static String getOffsetExpression(String query) throws SyntaxError {
        ArrayList<String> operators = new ArrayList<String>();
        operators.add("LIMIT");
        int start = getStartEnd2(query, "OFFSET", operators)[0];
        int end = getStartEnd2(query, "OFFSET", operators)[1];

        if ((start > 0) && (start < end) && (query.substring(start, end).charAt(0) != ' ')) {
            return query.substring(start, end);
        } else if (start > 0 && end == 0  && query.substring (start).length() > 0 && query.substring(start).charAt(0) != ' ') {
            return query.substring(start) ;
        } else if (start == 0) {
            return "";
        } else {
            throw new SyntaxError("Syntax error! Expected arguments in OFFSET");
        }
    }

    static ArrayList<String> parseQuery(String query) throws SyntaxError {
        ArrayList<String> resultParse = new ArrayList<>();
        resultParse.add(getSelectExpression(query));
        resultParse.add(getFromExpression(query));
        resultParse.add(getWhereExpression(query));
        resultParse.add(getGroupExpression(query));
        resultParse.add(getOrderExpression(query));
        resultParse.add(getLimitExpression(query));
        resultParse.add(getOffsetExpression(query));
        return resultParse;
    }
}
