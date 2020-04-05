package zaicevdmitry.com;


import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class ConstructorTest {
    @Test
    public void getSelectStar() throws SyntaxError {
        assertEquals(")", Constructor.getSelect("*"));
    }

    @Test
    public void getSelectOneNames() throws SyntaxError {
        assertEquals(", {name: 1})", Constructor.getSelect("name"));
    }

    @Test
    public void getSelect() throws SyntaxError {
        assertEquals(", {name: 1, surname: 1})", Constructor.getSelect("name, surname"));
    }

    @Test(expected = SyntaxError.class)
    public void getSelectWithLastComma() throws SyntaxError {
        Constructor.getSelect("name, surname,");
    }

    @Test(expected = SyntaxError.class)
    public void getSelectWithNotLastComma() throws SyntaxError {
        Constructor.getSelect("name,    surname");
    }

    @Test(expected = SyntaxError.class)
    public void getSelectWithLastCommaAndSpace() throws SyntaxError {
        Constructor.getSelect("name,  ,surname");
    }

    @Test
    public void getFrom() throws SyntaxError {
        assertEquals("db.name", Constructor.getFrom("name"));
    }

    @Test(expected = SyntaxError.class)
    public void getIncorrectFrom() throws SyntaxError {
        Constructor.getFrom("na   me");
    }


    @Test
    public void getWhereWithAND() throws SyntaxError {
        assertEquals(".find({age: {$ne: 22}, id: {$lt: 10}, name: 'Vasya'}",
                Constructor.getWhere("age <> 22 AND id < 10 AND name = 'Vasya'"));
    }

    @Test
    public void getWhere() throws SyntaxError {
        assertEquals(".find({age: {$gt: 22}}", Constructor.getWhere("age > 22"));
    }

    @Test(expected = SyntaxError.class)
    public void getIncorrectWhereIncorrectOperation() throws SyntaxError {
        Constructor.getWhere("id = 2 AND age != 22 AND name = 'Vasya'");
    }

    @Test(expected = SyntaxError.class)
    public void getIncorrectWhere() throws SyntaxError {
        Constructor.getWhere("age > = 22 AND name = 'Vasya'");
    }

    @Test
    public void getLimit() {
        assertEquals(".limit(22)", Constructor.getLimit("22"));
    }

    @Test
    public void getLimitEmpty() {
        assertEquals("", Constructor.getLimit(""));
    }

    @Test
    public void getOffset() {
        assertEquals(".skip(1)", Constructor.getOffset("1"));
    }

    @Test
    public void getOffsetEmpty() {
        assertEquals("", Constructor.getOffset(""));
    }

    @Test
    public void getOrderEmpty() throws SyntaxError {
        assertEquals("", Constructor.getOrder(""));
    }

    @Test
    public void getOrderWithoutDESK() throws SyntaxError {
        System.out.println(Constructor.getOrder("name"));
        assertEquals("sort({name: 1})", Constructor.getOrder("name"));
    }

    @Test
    public void getOrderWithDESK() throws SyntaxError {
        System.out.println(Constructor.getOrder("name"));
        assertEquals("sort({name: -1}, {id: 1})", Constructor.getOrder("name DESC, id ASC"));
    }

    @Test
    public void getOrderWithNameWithSpace() throws SyntaxError {
        assertEquals("sort({name 'DE  SC': 1}, {id: 1})",
                Constructor.getOrder("name 'DE  SC', id ASC"));
    }

    @Test(expected = SyntaxError.class)
    public void getOrderSpaceError() throws SyntaxError {
        Constructor.getOrder("name DE  SC, id ASC");
    }

    @Test
    public void getGroupEmpty() throws SyntaxError {
        assertEquals("", Constructor.getGroup(""));
    }

    @Test(expected = SyntaxError.class)
    public void getGroupNotEmpty() throws SyntaxError {
        Constructor.getGroup("name");
    }


    @Test
    public void createTransform() throws SyntaxError {
        ArrayList<String> parsingQuery = new ArrayList<>();
        parsingQuery.add("*");
        parsingQuery.add("table");
        parsingQuery.add("id < 100");
        parsingQuery.add("");
        parsingQuery.add("");
        parsingQuery.add("10");
        parsingQuery.add("2");


        System.out.println(Constructor.createMongoQuery(parsingQuery));
        assertEquals("db.table.find({id: {$lt: 100}}).skip(2).limit(10)",
                Constructor.createMongoQuery(parsingQuery));
    }
}
