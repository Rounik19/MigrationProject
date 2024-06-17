package in.rounik.project;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingConfigurationController {
    @Autowired
    private MappingConfigurationRepository mappingConfigurationRepository;

    @GetMapping("/Mappings")
    public List<MappingConfiguration> getMappingConfiguration() {
        if(mappingConfigurationRepository.count() == 0)
            return List.of();
        return mappingConfigurationRepository.findAll();
    }

    @GetMapping("/Mappings/{id}")
    public ResponseEntity<?> getMappingConfigurationById(@PathVariable String id) {
        MappingConfiguration existingMappingConfiguration = mappingConfigurationRepository.findById(id).orElse(null);
        if (existingMappingConfiguration != null)
            return new ResponseEntity<>(existingMappingConfiguration, HttpStatus.OK);
        else
            return new ResponseEntity<>("Mapping with id : " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/Mappings")
    public ResponseEntity<?> addMappingConfiguration(@RequestBody MappingConfiguration Config) {  
        mappingConfigurationRepository.save(Config);
        return new ResponseEntity<>("Mapping added successfully with id : " + Config.getId(), HttpStatus.OK);
    }

    @PutMapping("/Mappings/{id}")
    public ResponseEntity<?> updateMappingConfiguration(@RequestBody MappingConfiguration Config, @PathVariable String id) {
        MappingConfiguration existingMappingConfiguration = mappingConfigurationRepository.findById(id).orElse(null);
        if (existingMappingConfiguration != null) {
            mappingConfigurationRepository.deleteById(id);
            Config.setId(id);
            mappingConfigurationRepository.save(Config);
            return new ResponseEntity<>("Mapping with id : " + id + " updated successfully", HttpStatus.OK);
        } 
        else
            return new ResponseEntity<>("Mapping with id : " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/Mappings/{id}")
    public ResponseEntity<?> deleteMappingConfiguration(@PathVariable String id) {
        MappingConfiguration existingMappingConfiguration = mappingConfigurationRepository.findById(id).orElse(null); 
        if (existingMappingConfiguration != null) {
            mappingConfigurationRepository.deleteById(id);
            return new ResponseEntity<>("Mapping with id : " + id + " deleted successfully", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Mapping with id : " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
