package net.disy.sonarplugins.cssreport.cssstats;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import java.util.HashMap;
import java.util.List;

public class Mapper {
    //TODO look for all usages of mapper and replace them
    public final static ObjectMapper mapper = new ObjectMapper();
    public final static CollectionType stringList = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
    public final static CollectionType intList = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
    public final static JavaType stringType = mapper.getTypeFactory().constructType(String.class);
    public final static MapType propertiesType = mapper.getTypeFactory().constructMapType(HashMap.class, stringType, stringList);
    public final static MapType stringIntMap = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class);
}
