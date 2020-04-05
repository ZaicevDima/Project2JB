package zaicevdmitry.com;

import java.util.ArrayList;

public class Constructor {

    static String getSelect(String select) throws SyntaxError {

        if (select.equals("*")) {
            return ")";
        } else {
            StringBuilder resultSelect = new StringBuilder();
            int start = 0;
            for (int i = 0; i < select.length(); i++) {
                if (select.charAt(i) == ',') {
                    if ((i - start > ", ".length()) && select.substring(start, start + 3).equals(",  ")) {
                        throw new SyntaxError("Incorrect SELECT expression");
                    }
                    resultSelect.append(select, start, i).append(": 1");
                    start = i;
                }
            }
            if ((select.length() - start > ", ".length()) && select.substring(start, start + 3).equals(",  ") ||
                    (select.substring(start).length() <= 2) && select.substring(start).charAt(0) == ',') {
                throw new SyntaxError("Incorrect SELECT expression");
            }
            resultSelect.append(select.substring(start)).append(": 1");
            return ", {" + resultSelect + "})";
        }
    }

    static String getFrom(String from) throws SyntaxError {
        if ((from.charAt(0) != '"' || from.charAt(from.length() - 1) != '"') && from.contains(" ")) {
            throw new SyntaxError("Incorrect FROM");
        }
        return "db." + from;
    }

    static boolean isCorrectWhere(String predicate) {
        return predicate.contains("=") ^ predicate.contains("<>") ^ (predicate.contains(">") ^ predicate.contains("<"));
    }

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
            boolean isFirstQuote = true;
            int counterSpace = 0;

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

            if (counterSpace > 1) {
                throw new SyntaxError("Incorrect ORDER BY");
            }

            boolean isASC = true;
            String[] predicates = s.split(" ");
            if (predicates[predicates.length - 1].equals("DESC")) {
                isASC = false;
            }

            if (isASC && !predicates[predicates.length - 1].equals("ASC")) {
                resultOrder.append("{").append(s, 0, s.length()).append(": 1}, ");
            } else if (predicates[predicates.length - 1].equals("ASC")) {
                resultOrder.append("{").append(s, 0, s.length() - "ASC".length() - 1).append(": 1}, ");
            } else {
                resultOrder.append("{").append(s, 0, s.length() - "DESC".length() - 1).append(": -1}, ");
            }
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