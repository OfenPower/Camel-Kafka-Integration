package loanbroker;

import java.util.HashMap;
import java.util.Map;

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
