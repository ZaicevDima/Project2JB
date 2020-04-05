package zaicevdmitry.com;

import java.util.ArrayList;

public class Parser {
    static String getSelectExpression(String query) throws SyntaxError {
        int start = 0;
        int end = 0;
        if (query.length() <= "SELECT".length()) {
            throw new SyntaxError("Syntax error! Expected SELECT");
        }
        for (int i = 0; i < query.length() - "FROM".length(); i++) {
            if ((query.length() -  i > "SELECT".length()) && query.substring(0, i).equals("SELECT")) {
                start = i + 1;
            }
            if ((query.length() -  i > "FROM".length()) && query.substring(i, i + "FROM".length()).equals("FROM")) {
                end = i - 1;
                break;
            }
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

    static String getFromExpression(String query) throws SyntaxError {
        int start = 0;
        int end = 0;
        if (query.length() <= "SELECT".length()) {
            throw new SyntaxError("Syntax error! Expected SELECT");
        }
        for (int i = 0; i < query.length(); i++) {
            if (query.length() - i > "FROM".length() && query.substring(i, i + "FROM".length()).equals("FROM")) {
                start = i + "FROM".length() + 1;
            }

            if (start > 0 && getEnd(query, "WHERE", i, end) > 0) {
                end = getEnd(query, "WHERE", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "GROUP BY", i, end) > 0) {
                end = getEnd(query, "GROUP BY", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "ORDER BY", i, end) > 0) {
                end = getEnd(query, "ORDER BY", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "LIMIT", i, end) > 0) {
                end = getEnd(query, "LIMIT", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "OFFSET", i, end) > 0) {
                end = getEnd(query, "OFFSET", i, end);
                break;
            }
        }
        if ((start > 0) && (start < end) && (query.substring(start, end).charAt(0) != ' ')) {
            return query.substring(start, end);
        } else if (start > 0 && end == 0 && (query.substring(start).charAt(0) != ' ')) {
            return query.substring(start);
        } else {
            throw new SyntaxError("Syntax error! Expected table name in FROM");
        }
    }

    static String getWhereExpression(String query) throws SyntaxError {
        int start = 0;
        int end = 0;

        if (query.length() <= "SELECT".length()) {
            throw new SyntaxError("Syntax error! Expected SELECT");
        }
        for (int i = 0; i < query.length(); i++) {
            if (query.length() - i > "WHERE".length()  && query.substring(i, i + "WHERE".length()).equals("WHERE")) {
                start = i + "WHERE".length() + 1;
            }

            if (start > 0 && getEnd(query, "GROUP BY", i, end) > 0) {
                end = getEnd(query, "GROUP BY", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "ORDER BY", i, end) > 0) {
                end = getEnd(query, "ORDER BY", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "LIMIT", i, end) > 0) {
                end = getEnd(query, "LIMIT", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "OFFSET", i, end) > 0) {
                end = getEnd(query, "OFFSET", i, end);
                break;
            }
        }

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
        int start = 0;
        int end = 0;

        if (query.length() <= "SELECT".length()) {
            throw new SyntaxError("Syntax error! Expected SELECT");
        }
        for (int i = 0; i < query.length(); i++) {
            if (query.length() - i > "GROUP BY".length() && query.substring(i, i + "GROUP BY".length()).equals("GROUP BY")) {
                start = i + "GROUP BY".length() + 1;
            }

            if (start > 0 && getEnd(query, "ORDER BY", i, end) > 0) {
                end = getEnd(query, "ORDER BY", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "LIMIT", i, end) > 0) {
                end = getEnd(query, "LIMIT", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "OFFSET", i, end) > 0) {
                end = getEnd(query, "OFFSET", i, end);
                break;
            }
        }

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
        int start = 0;
        int end = 0;

        if (query.length() <= "SELECT".length()) {
            throw new SyntaxError("Syntax error! Expected SELECT");
        }
        for (int i = 0; i < query.length(); i++) {
            if (query.length() - i > "ORDER BY".length() && query.substring(i, i + "ORDER BY".length()).equals("ORDER BY")) {
                start = i + "ORDER BY".length() + 1;
            }

            if (start > 0 && getEnd(query, "LIMIT", i, end) > 0) {
                end = getEnd(query, "LIMIT", i, end);
                break;
            }
            if (start > 0 && getEnd(query, "OFFSET", i, end) > 0) {
                end = getEnd(query, "OFFSET", i, end);
                break;
            }
        }

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
        int start = 0;
        int end = 0;

        for (int i = 0; i < query.length(); i++) {
            if (query.length() - i > "LIMIT".length() && query.substring(i).startsWith("LIMIT")) {
                start = i + "LIMIT".length() + 1;
            }

            if (start > 0 && getEnd(query, "OFFSET", i, end) > 0) {
                end = getEnd(query, "OFFSET", i, end);
                break;
            }
        }

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
        int start = 0;
        int end = 0;

        if (query.length() <= "SELECT".length()) {
            throw new SyntaxError("Syntax error! Expected SELECT");
        }
        for (int i = 0; i < query.length(); i++) {
            if (query.length() - i > "OFFSET".length() && query.substring(i, i + "OFFSET".length()).equals("OFFSET")) {
                start = i + "OFFSET".length() + 1;
            }

            if (start > 0 && getEnd(query, "LIMIT", i, end) > 0) {
                end = getEnd(query, "LIMIT", i, end);
                break;
            }
        }

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
