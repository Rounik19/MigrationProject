package in.rounik.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingExecutor {

    @Autowired
    private MappingService mappingService;

    @GetMapping("/executeMapping")
    public ResponseEntity<String> executeTransformation() {
        mappingService.executeDataTransformationScript();
        return ResponseEntity.ok("Mapping script executed successfully.");
    }
    
}