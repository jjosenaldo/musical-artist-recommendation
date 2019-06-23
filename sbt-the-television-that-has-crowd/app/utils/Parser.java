package utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Parser{
    public static Map<Integer, Double> mapFromString(String map){
        Map<Integer, Double> res = new ConcurrentHashMap<>();
        String[] keyValues = map.split(",");
        String[] keyAndValue;
        Integer key;
        Double value;

        for(String keyValue : keyValues){
            keyAndValue = keyValue.split(":");
            key = Integer.parseInt(keyAndValue[0].replaceAll(" ", ""));
            value = Double.parseDouble(keyAndValue[1].replaceAll(" ", ""));
            res.put(key, value);
        }

        return res;
    }
}