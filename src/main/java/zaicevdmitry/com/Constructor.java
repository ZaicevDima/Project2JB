package zaicevdmitry.com;

import java.util.ArrayList;

/**
 * Class for create part of mongo query
 */
public class Constructor {

    /**
     * Transform SQL SELECT part to mongo style
     * @param select SELECT expression of SQL query
     * @return expression in mongo style
     * @throws SyntaxError if select expression is incorrect
     */
    static String getSelect(String select) throws SyntaxError {

        if (select.equals("*")) {
            return ")";
        } else {
            StringBuilder resultSelect = new StringBuilder();
            int start = 0;
            for (int i = 0; i < select.length(); i++) {
                if (select.charAt(i) == ',') {
                    if ((i - start > ", ".length()) && select.substring(start).startsWith(",  ")) {
                        throw new SyntaxError("Incorrect SELECT expression");
                    }
                    resultSelect.append(select, start, i).append(": 1");
                    start = i;
                }
            }
            if ((select.length() - start > ", ".length()) && select.substring(start).startsWith(",  ") ||
                    (select.substring(start).length() <= 2) && select.substring(start).startsWith(",")) {
                throw new SyntaxError("Incorrect SELECT expression");
            }
            resultSelect.append(select.substring(start)).append(": 1");
            return ", {" + resultSelect + "})";
        }
    }

    /**
     * Transform SQL FROM part to mongo style
     * @param from from expression of SQL query
     * @return expression in mongo style
     * @throws SyntaxError if from expression is incorrect
     */
    static String getFrom(String from) throws SyntaxError {
        if ((from.charAt(0) != '"' || !from.endsWith("\"")) && from.contains(" ")) {
            throw new SyntaxError("Incorrect FROM");
        }
        return "db." + from;
    }

    /**
     * Checks that predicate is correct WHERE predicate
     * @param predicate predicate of WHERE argument
     * @return true if contains = or <> or > or <
     */
    static boolean isCorrectWhere(String predicate) {
        return predicate.contains("=") ^ predicate.contains("<>") ^ (predicate.contains(">") ^ predicate.contains("<"));
    }

    /**
     * Transform SQL WHERE part to mongo style
     * @param predicate WHERE expression of SQL query
     * @return expression in mongo style
     * @throws SyntaxError if WHERE expression is incorrect
     */
    static String transformWherePredicate(String predicate) throws SyntaxError {
        if (predicate.contains("=") && (predicate.charAt(predicate.indexOf("=") - 1) == ' ') &&
                predicate.charAt(predicate.indexOf("=") + 1) == ' ') {
            try {
                double rightPart = Double.parseDouble(predicate.substring(predicate.indexOf('=') + 1));
                return predicate.substring(0, predicate.indexOf("=") - 1) + ": {$eq:" + rightPart;
            } catch (NumberFormatException e) {
                return predicate.substring(0, predicate.indexOf("=") - 1) + ":" +
                        predicate.substring(predicate.indexOf('=') + 1);
            }
        } else if (predicate.contains("<>") && (predicate.charAt(predicate.indexOf("<") - 1) == ' ') &&
                (predicate.charAt(predicate.indexOf('>') + 1) == ' ')) {
            return predicate.substring(0, predicate.indexOf("<>") - 1) + ": {$ne:" +
                    predicate.substring(predicate.indexOf("<>") + 2) + "}";
        } else if (predicate.contains("<") && (predicate.charAt(predicate.indexOf("<") - 1) == ' ') &&
                predicate.charAt(predicate.indexOf("<") + 1) == ' ') {
            return predicate.substring(0, predicate.indexOf("<") - 1) + ": {$lt:" +
                    predicate.substring(predicate.indexOf("<") + 1) + "}";
        } else if (predicate.contains(">") && (predicate.charAt(predicate.indexOf(">") - 1) == ' ') &&
                predicate.charAt(predicate.indexOf(">") + 1) == ' ') {
            return predicate.substring(0, predicate.indexOf(">") - 1) + ": {$gt:" +
                    predicate.substring(predicate.indexOf(">") + 1) + "}";
        }
        throw new SyntaxError("Incorrect WHERE predicate");
    }

    static String getWhere(String where) throws SyntaxError {

        if (where.equals("")) {
            return ".find({}";
        } else {
            String[] predicates = where.split(" AND ");
            StringBuilder resultWhere = new StringBuilder(".find({");
            for (int i = 0; i < predicates.length; i++) {
                String predicate = predicates[i];
                if (isCorrectWhere(predicate)) {
                    resultWhere.append(
                            transformWherePredicate(predicate));

                    if (i != predicates.length - 1) {
                        resultWhere.append(", ");
                    }
                } else {
                    throw new SyntaxError("Incorrect WHERE");
                }
            }
            return resultWhere + "}";
        }
    }

    static String getLimit(String limit) {
        if (limit.equals("")) {
            return "";
        } else {
            return ".limit(" + limit + ")";
        }
    }

    static String getOffset(String offset) {
        if (offset.equals("")) {
            return "";
        } else {
            return ".skip(" + offset + ")";
        }
    }

    private static int getAmountSpaces(String s) {
        int counterSpace = 0;
        boolean isFirstQuote = true;
        for (int j = 0; j < s.length(); j++) {
            if (s.charAt(j) == '\'' && isFirstQuote) {
                isFirstQuote = false;
            } else if (s.charAt(j) == '\'' && !isFirstQuote) {
                isFirstQuote = true;
            }
            if (s.charAt(j) == ' ' && isFirstQuote) {
                counterSpace++;
            }
        }
        return counterSpace;
    }

    private static boolean isASC(String[] predicates) {
        return !predicates[predicates.length - 1].equals("DESC");
    }

    private static StringBuilder getResultOrder(boolean isASC, String s, StringBuilder resultOrder, String[] predicates) {
        if (isASC && !predicates[predicates.length - 1].equals("ASC")) {
            resultOrder.append("{").append(s, 0, s.length()).append(": 1}, ");
        } else if (predicates[predicates.length - 1].equals("ASC")) {
            resultOrder.append("{").append(s, 0, s.length() - "ASC".length() - 1).append(": 1}, ");
        } else {
            resultOrder.append("{").append(s, 0, s.length() - "DESC".length() - 1).append(": -1}, ");
        }
        return resultOrder;
    }

    static String getOrder(String order) throws SyntaxError {
        if (order.equals("")) {
            return "";
        }
        String[] parsingOrder = order.split(",");
        StringBuilder resultOrder = new StringBuilder();
        for (String s : parsingOrder) {
            if (s.charAt(0) == ' ') {
                s = s.substring(1);
            }
            int counterSpace = getAmountSpaces(s);

            if (counterSpace > 1) {
                throw new SyntaxError("Incorrect ORDER BY");
            }

            String[] predicates = s.split(" ");
            boolean isASC = isASC(predicates);
            getResultOrder(isASC, s, resultOrder, predicates);
        }
        resultOrder.delete(resultOrder.length() - 2, resultOrder.length());
        return "sort(" + resultOrder.toString() + ")";
    }

    static String getGroup(String group) throws SyntaxError {
        if (group.equals("")) {
            return "";
        } else {
            throw new SyntaxError("Sorry, don't fount operator GROUP BY");
        }
    }

    static String createMongoQuery(ArrayList<String> parsingSQLQuery) throws SyntaxError {
        String select = getSelect(parsingSQLQuery.get(0));
        String from = getFrom(parsingSQLQuery.get(1));
        String where = getWhere(parsingSQLQuery.get(2));
        String group = getGroup(parsingSQLQuery.get(3));
        String order = getOrder(parsingSQLQuery.get(4));
        String limit = getLimit(parsingSQLQuery.get(5));
        String offset = getOffset(parsingSQLQuery.get(6));

        return from + where + select + group + order + offset + limit;
    }
}
