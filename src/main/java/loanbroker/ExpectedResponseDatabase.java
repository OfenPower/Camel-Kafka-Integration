package loanbroker;

import java.util.HashMap;
import java.util.Map;

/*
 * Mockup einer Datenbank, welche in einer Map speichert, wieviele Messages 
 * vom Aggregator erwartet werden und wieviele bisher ankamen.
 * 
 * Die Klasse verwendet das Singleton-Pattern
 */
public class ExpectedResponseDatabase {

    private static ExpectedResponseDatabase instance = new ExpectedResponseDatabase();

    public Map<Integer,Integer> map;

    private ExpectedResponseDatabase()
    {
        map = new HashMap<>();
    }

    public static ExpectedResponseDatabase getInstance()
    {
        return instance;
    }
}
