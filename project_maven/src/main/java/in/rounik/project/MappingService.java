package in.rounik.project;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class MappingService {

    private GroovyShell groovyShell;
    private File file;
    private Script script;
    
    public MappingService(){
        this.file = new File("src/main/resources/NestedField.groovy");
        this.groovyShell = new GroovyShell();
        this.script = null;
        try{
            this.script = this.groovyShell.parse(file);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public JsonNode getNestedField(JsonNode json, String Path){
        Object object = script.invokeMethod("getNestedField", new Object[] {json,Path});
        return (JsonNode)object;
    }

    public JsonNode swapFields(JsonNode json, Map<String,Object> map){
        // return (JsonNode) script.invokeMethod("swapFields", new Object[] {json.toString(),map});
        return null;
    }
    public JsonNode mapData(JsonNode node, JsonNode dataMap){
        return (JsonNode) script.invokeMethod("mapData", new Object[] {node, dataMap});
    }
    public JsonNode mapFields(JsonNode json, Map<String,String> fieldMap){
        return (JsonNode) script.invokeMethod("mapFields", new Object[] {json, fieldMap});
    }
}
