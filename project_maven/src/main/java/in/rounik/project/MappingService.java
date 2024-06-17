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
        this.file = new File("src/main/java/in/rounik/project/MappingControllerScript.groovy");
        this.groovyShell = new GroovyShell();
        this.script = null;
        try{
            this.script = this.groovyShell.parse(file);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
