package in.rounik.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingExecutor {

    @Autowired
    private MappingService mappingService;

    @GetMapping("/executeMapping/{id}")
    public ResponseEntity<?> executeTransformation(@PathVariable String id){
        Object result = mappingService.executeDataTransformationScript(id);
        return ResponseEntity.ok().body(result);
    }
    
}
