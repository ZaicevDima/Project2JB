package zaicevdmitry.com;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConverterTest {
    @Test
    public void converterTest1() throws SyntaxError {
        String SQLQuery = "SELECT * FROM sales LIMIT 10";
        String expected = "db.sales.find({}).limit(10)";
        assertEquals(expected, Converter.converseToMongo(SQLQuery));
    }

    @Test
    public void converterTest2() throws SyntaxError {
        String SQLQuery = "SELECT name, surname FROM collection";
        String expected = "db.collection.find({}, {name: 1, surname: 1})";
        assertEquals(expected, Converter.converseToMongo(SQLQuery));
    }

    @Test
    public void converterTest3() throws SyntaxError {
        String SQLQuery = "SELECT * FROM collection OFFSET 5 LIMIT 10";
        String expected = "db.collection.find({}).skip(5).limit(10)";
        assertEquals(expected, Converter.converseToMongo(SQLQuery));
    }

    @Test
    public void converterTest4() throws SyntaxError {
        String SQLQuery = "SELECT * FROM customers WHERE age > 22 AND name = 'Vasya'";
        String expected = "db.customers.find({age: {$gt: 22}, name: 'Vasya'})";
        assertEquals(expected, Converter.converseToMongo(SQLQuery));
    }

    @Test
    public void converterTest5() throws SyntaxError {
        String SQLQuery = "SELECT id, name, surname FROM customers WHERE age <> 22 AND name = 'Vasya' AND id > 2 LIMIT 10 OFFSET 5";
        String expected = "db.customers.find({age: {$ne: 22}, name: 'Vasya', id: {$gt: 2}}, {id: 1, name: 1, surname: 1}).skip(5).limit(10)";
        assertEquals(expected, Converter.converseToMongo(SQLQuery));
    }

    @Test
    public void converterTest6() throws SyntaxError {
        String SQLQuery = "SELECT title FROM book WHERE price > 10";
        String expected = "db.book.find({price: {$gt: 10}}, {title: 1})";
        assertEquals(expected, Converter.converseToMongo(SQLQuery));
    }

    @Test(expected = SyntaxError.class)
    public void converterErrorTest1() throws SyntaxError {
        String SQLQuery = "SELECT FROM book WHERE price > 10";
        Converter.converseToMongo(SQLQuery);
    }

    @Test(expected = SyntaxError.class)
    public void converterErrorTest2() throws SyntaxError {
        String SQLQuery = "SELECT * FROM WHERE price > 10";
        Converter.converseToMongo(SQLQuery);
    }

    @Test(expected = SyntaxError.class)
    public void converterErrorTest3() throws SyntaxError {
        String SQLQuery = "SELECT * FROM 'table name' WHERE price # 10";
        Converter.converseToMongo(SQLQuery);
    }

    @Test(expected = SyntaxError.class)
    public void converterErrorTest4() throws SyntaxError {
        String SQLQuery = "SELECT * FROM table WHERE price > 10 ORDER BY LIMIT(5)";
        Converter.converseToMongo(SQLQuery);
    }
}
