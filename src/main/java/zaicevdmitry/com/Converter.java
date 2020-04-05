package zaicevdmitry.com;

import java.util.ArrayList;

public class Converter {
    static String converseToMongo(String SQLQuery) throws SyntaxError {
        ArrayList<String> parsingSQLQuery = Parser.parseQuery(SQLQuery);
        return Constructor.createMongoQuery(parsingSQLQuery);
    }
}
