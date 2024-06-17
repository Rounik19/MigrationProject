package in.rounik.project;

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
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ApiConfigMappingConfigController {

    @Autowired
    private ApiConfigMappingConfigRepository ApiConfigMappingConfigRepository;

    @GetMapping("/homepage")
    public ResponseEntity<?> home() {
        return new ResponseEntity<>("Welcome to the API Config Mapping Config Homepage", HttpStatus.OK);
    }
    

    @GetMapping("/Configs")
    public ResponseEntity<?> getApiConfigMappingConfig() {
        if(ApiConfigMappingConfigRepository.count() == 0)
            return new ResponseEntity<>("No Configs found", HttpStatus.NOT_FOUND); // "No Configs found" is the response message
        return new ResponseEntity<>(ApiConfigMappingConfigRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/Configs/{id}")
    public ResponseEntity<?> getApiConfigMappingConfigById(@PathVariable String id) {
        ApiConfigMappingConfig existingApiConfigMappingConfig = ApiConfigMappingConfigRepository.findById(id).orElse(null);
        if (existingApiConfigMappingConfig != null)
            return new ResponseEntity<>(existingApiConfigMappingConfig, HttpStatus.OK);
        else
            return new ResponseEntity<>("Config with id : " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/Configs")
    public ResponseEntity<?> addApiConfigMappingConfig(@RequestBody ApiConfigMappingConfig Config) {  
        ApiConfigMappingConfigRepository.save(Config);
        return new ResponseEntity<>("Config added successfully with id : " + Config.getId(), HttpStatus.OK);
    }

    @PutMapping("/Configs/{id}")
    public ResponseEntity<?> updateApiConfigMappingConfig(@RequestBody ApiConfigMappingConfig Config, @PathVariable String id) {
        ApiConfigMappingConfig existingApiConfigMappingConfig = ApiConfigMappingConfigRepository.findById(id).orElse(null);
        if (existingApiConfigMappingConfig != null) {
            ApiConfigMappingConfigRepository.deleteById(id);
            Config.setId(id);
            ApiConfigMappingConfigRepository.save(Config);
            return new ResponseEntity<>("Config with id : " + id + " updated successfully", HttpStatus.OK);
        } 
        else
            return new ResponseEntity<>("Config with id : " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/Configs/{id}")
    public ResponseEntity<?> deleteApiConfigMappingConfig(@PathVariable String id) {
        ApiConfigMappingConfig existingApiConfigMappingConfig = ApiConfigMappingConfigRepository.findById(id).orElse(null); 
        if (existingApiConfigMappingConfig != null) {
            ApiConfigMappingConfigRepository.deleteById(id);
            return new ResponseEntity<>("Config with id : " + id + " deleted successfully", HttpStatus.OK);
        } 
        else
            return new ResponseEntity<>("Config with id : " + id + " not found", HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/Configs")
    public ResponseEntity<?> deleteAllApiConfigMappingConfig() {
        ApiConfigMappingConfigRepository.deleteAll();
        return new ResponseEntity<>("All Configs deleted successfully", HttpStatus.OK);
    }
}
