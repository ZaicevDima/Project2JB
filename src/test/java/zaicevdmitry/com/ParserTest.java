package zaicevdmitry.com;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

public class ParserTest {
    @Test
    public void getSelectExpression() throws SyntaxError {
        String query = "SELECT * FROM table";
        assertEquals("*", Parser.getSelectExpression(query));
    }

    @Test(expected = SyntaxError.class)
    public void getSelectExpressionErrorSelectExpression() throws SyntaxError {
        Parser.getSelectExpression("SELECT FROM");
    }

    @Test(expected = SyntaxError.class)
    public void getSelectExpressionErrorSelect() throws SyntaxError {
        Parser.getSelectExpression("SELE");
    }

    @Test(expected = SyntaxError.class)
    public void getFromExpressionErrorFrom() throws SyntaxError {
        Parser.getFromExpression("SELE");
    }

    @Test
    public void getFromExpression() throws SyntaxError {
        String query = "SELECT * FROM table";
        assertEquals("table", Parser.getFromExpression(query));
    }

    @Test
    public void getFromExpressionWithWHERE() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100";
        assertEquals("table", Parser.getFromExpression(query));
    }

    @Test
    public void getFromExpressionWithGROUP() throws SyntaxError {
        String query = "SELECT * FROM table GROUP BY id ORDER BY id LIMIT 10 OFFSET 2";
        assertEquals("table", Parser.getFromExpression(query));
    }

    @Test
    public void getFromExpressionWithORDER() throws SyntaxError {
        String query = "SELECT * FROM table ORDER BY id LIMIT 10 OFFSET 2";
        assertEquals("table", Parser.getFromExpression(query));
    }

    @Test
    public void getFromExpressionWithLIMIT() throws SyntaxError {
        String query = "SELECT * FROM table LIMIT 10";
        assertEquals("table", Parser.getFromExpression(query));
    }

    @Test
    public void getFromExpressionWithOFFSET() throws SyntaxError {
        String query = "SELECT * FROM table OFFSET 2";
        assertEquals("table", Parser.getFromExpression(query));
    }

    @Test(expected = SyntaxError.class)
    public void getFromExpressionWithEmptyTable() throws SyntaxError {
        String query = "SELECT * FROM GROUP BY id ORDER BY id LIMIT 10 OFFSET 2";
        Parser.getFromExpression(query);
    }

    @Test(expected = SyntaxError.class)
    public void getFromExpressionWithSpaceTable() throws SyntaxError {
        String query = "SELECT * FROM    GROUP BY id ORDER BY id LIMIT 10 OFFSET 2";
        Parser.getFromExpression(query);
    }

    @Test
    public void getWhereExpressionEmpty() throws SyntaxError {
        String query = "SELECT * FROM table GROUP BY id ORDER BY id LIMIT 10 OFFSET 2";
        assertEquals("", Parser.getWhereExpression(query));
    }

    @Test
    public void getWhereExpression() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100";
        assertEquals("id < 100", Parser.getWhereExpression(query));
    }

    @Test
    public void getWhereExpressionWithGroup() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 GROUP BY id";
        assertEquals("id < 100", Parser.getWhereExpression(query));
    }

    @Test
    public void getWhereExpressionWithORDER() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 ORDER BY id";
        assertEquals("id < 100", Parser.getWhereExpression(query));
    }

    @Test
    public void getWhereExpressionWithOffset() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 OFFSET 5";
        assertEquals("id < 100", Parser.getWhereExpression(query));
    }

    @Test
    public void getWhereExpressionWithLimit() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 LIMIT 10";
        assertEquals("id < 100", Parser.getWhereExpression(query));
    }

    @Test(expected = SyntaxError.class)
    public void getWhereExpressionWithSpaceWhere() throws SyntaxError {
        String query = "SELECT * FROM  table WHERE    GROUP BY id ORDER BY id LIMIT 10 OFFSET 2";
        Parser.getWhereExpression(query);
    }

    @Test(expected = SyntaxError.class)
    public void getWhereExpressionSyntaxError() throws SyntaxError {
        String query = "SE ";
        Parser.getWhereExpression(query);
    }

    @Test
    public void getGroupExpressionEmpty() throws SyntaxError {
        String query = "SELECT * FROM table ORDER BY id LIMIT 10 OFFSET 2";
        assertEquals("", Parser.getGroupExpression(query));
    }

    @Test
    public void getGroupExpression() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 GROUP BY id";
        assertEquals("id", Parser.getGroupExpression(query));
    }

    @Test
    public void getGroupExpressionWithORDER() throws SyntaxError {
        String query = "SELECT * FROM table GROUP BY id HAVING COUNT(*) > 1 ORDER BY id";
        assertEquals("id HAVING COUNT(*) > 1", Parser.getGroupExpression(query));
    }

    @Test
    public void getGroupExpressionWithOffset() throws SyntaxError {
        String query = "SELECT * FROM table GROUP BY id HAVING COUNT(*) > 1 OFFSET 5";
        assertEquals("id HAVING COUNT(*) > 1", Parser.getGroupExpression(query));
    }

    @Test
    public void getGroupExpressionWithLimit() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 GROUP BY id HAVING COUNT(*) > 1 LIMIT 10";
        assertEquals("id HAVING COUNT(*) > 1", Parser.getGroupExpression(query));
    }

    @Test(expected = SyntaxError.class)
    public void getGroupExpressionWithSpaceWhere() throws SyntaxError {
        String query = "SELECT * FROM  table GROUP BY     ORDER BY id LIMIT 10 OFFSET 2";
        Parser.getGroupExpression(query);
    }

    @Test(expected = SyntaxError.class)
    public void getGroupExpressionSyntaxError() throws SyntaxError {
        String query = "SE ";
        Parser.getGroupExpression(query);
    }

    @Test
    public void getOrderExpressionEmpty() throws SyntaxError {
        String query = "SELECT * FROM table LIMIT 10 OFFSET 2";
        assertEquals("", Parser.getOrderExpression(query));
    }

    @Test
    public void getOrderExpression() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 ORDER BY id";
        assertEquals("id", Parser.getOrderExpression(query));
    }

    @Test
    public void getOrderExpressionWithOffset() throws SyntaxError {
        String query = "SELECT * FROM table GROUP BY id HAVING COUNT(*) > 1 ORDER BY name OFFSET 5";
        assertEquals("name", Parser.getOrderExpression(query));
    }

    @Test
    public void getOrderExpressionWithLimit() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 GROUP BY id HAVING COUNT(*) > 1 ORDER BY name LIMIT 10";
        assertEquals("name", Parser.getOrderExpression(query));
    }

    @Test(expected = SyntaxError.class)
    public void getOrderExpressionWithSpaceWhere() throws SyntaxError {
        String query = "SELECT * FROM  table ORDER BY     LIMIT 10 OFFSET 2";
        Parser.getOrderExpression(query);
    }

    @Test(expected = SyntaxError.class)
    public void getOrderExpressionSyntaxError() throws SyntaxError {
        String query = "SE ";
        Parser.getOrderExpression(query);
    }

    @Test
    public void getLimitExpressionEmpty() throws SyntaxError {
        String query = "SELECT * FROM table OFFSET 2";
        assertEquals("", Parser.getLimitExpression(query));
    }

    @Test
    public void getLimitExpression() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 ORDER BY id LIMIT 10";
        assertEquals("10", Parser.getLimitExpression(query));
    }

    @Test
    public void getLimitExpressionWithOffset() throws SyntaxError {
        String query = "SELECT * FROM table GROUP BY id HAVING COUNT(*) > 1 ORDER BY name LIMIT 7 OFFSET 5";
        assertEquals("7", Parser.getLimitExpression(query));
    }

    @Test(expected = SyntaxError.class)
    public void getLimitExpressionWithSpaceWhere() throws SyntaxError {
        String query = "SELECT * FROM  table LIMIT      OFFSET 2";
        Parser.getLimitExpression(query);
    }

    @Test(expected = SyntaxError.class)
    public void getLimitExpressionSyntaxError() throws SyntaxError {
        String query = "SE ";
        Parser.getLimitExpression(query);
    }


    @Test
    public void getOffsetExpressionEmpty() throws SyntaxError {
        String query = "SELECT * FROM table";
        assertEquals("", Parser.getOffsetExpression(query));
    }

    @Test
    public void getOffsetExpression() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 ORDER BY id OFFSET 2 LIMIT 10";
        assertEquals("2", Parser.getOffsetExpression(query));
    }

    @Test
    public void getOffsetExpressionWithLimit() throws SyntaxError {
        String query = "SELECT * FROM table LIMIT 7 OFFSET 5";
        assertEquals("5", Parser.getOffsetExpression(query));
    }

    @Test(expected = SyntaxError.class)
    public void getOffsetExpressionWithSpaceWhere() throws SyntaxError {
        String query = "SELECT * FROM  table OFFSET    ";
        Parser.getOffsetExpression(query);
    }

    @Test(expected = SyntaxError.class)
    public void getOffsetExpressionWithSpaceOffset() throws SyntaxError {
        String query = "SELECT * FROM  table OFFSET ";
        Parser.getOffsetExpression(query);
    }

    @Test
    public void parseQuery() throws SyntaxError {
        String query = "SELECT * FROM table WHERE id < 100 GROUP BY id HAVING COUNT(*) > 10 OFFSET 2 LIMIT 10";
        ArrayList<String> expected = new ArrayList<>();
        expected.add("*");
        expected.add("table");
        expected.add("id < 100");
        expected.add("id HAVING COUNT(*) > 10");
        expected.add("");
        expected.add("10");
        expected.add("2");
        assertEquals(expected, Parser.parseQuery(query));
    }
}
