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

    @Autowired
    private FetchedDataRepository fetchedDataRepository;
    @Autowired
    private MappingConfigurationRepository mappingConfigurationRepository;

    private GroovyShell groovyShell;
    private File file;
    private Script script;

    public Object executeDataTransformationScript(String id){
        this.file = new File("src/main/java/in/rounik/project/MappingControllerScript.groovy");
        this.groovyShell = new GroovyShell();
        this.script = null;
        try{
            this.script = this.groovyShell.parse(file);
        }catch(IOException e){
            e.printStackTrace();
        }

        List<FetchedDataObject> fetchedData = fetchedDataRepository.findAll();
        // MappingConfiguration mappingConfiguration = mappingConfigurationRepository.findById(id).get();
        
        Object result = script.invokeMethod("processFetchedData", new Object[]{fetchedData, mappingConfigurationRepository});
        return result;
    }
}
