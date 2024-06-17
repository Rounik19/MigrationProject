package in.rounik.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fetchedData")
public class FetchedDataController {

    @Autowired
    private FetchedDataRepository fetchedDataRepository;

    private JsonNode ConvertStringToJsonNode(String fetchedData) {
        // Deserialize fetchedData into JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(fetchedData);
        } catch (Exception e) {
            return null;
        }
        return rootNode;
    }

    @GetMapping
    public ResponseEntity<?> getFetchedData() {
        List<FetchedDataObject> fetchedDataObjects = fetchedDataRepository.findAll();
        if (fetchedDataObjects.isEmpty()) {
            return ResponseEntity.ok().body("No fetched data found");
        }

        String fetchedData = fetchedDataObjects.get(0).getFetchedData();

        return ResponseEntity.ok().body(fetchedDataObjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFetchedDataById(@PathVariable String id) {
        Optional<FetchedDataObject> fetchedDataObject = fetchedDataRepository.findById(id);
        if(fetchedDataObject.isPresent()){
            String fetchedData = fetchedDataObject.get().getFetchedData();
            return ResponseEntity.ok().body(ConvertStringToJsonNode(fetchedData));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createFetchedData(@RequestBody FetchedDataObject fetchedDataObject) {
        return ResponseEntity.ok().body("fetched Data created with ID: " + fetchedDataRepository.save(fetchedDataObject).getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFetchedData(@PathVariable String id, @RequestBody FetchedDataObject updatedFetchedDataObject) {
        updatedFetchedDataObject.setId(id); // Ensure the ID is set for update
        deleteFetchedData(id); // Delete the existing fetched data object
        fetchedDataRepository.save(updatedFetchedDataObject);
        return ResponseEntity.ok().body("Fetched Data updated with ID: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFetchedData(@PathVariable String id) {
        fetchedDataRepository.deleteById(id);
        return ResponseEntity.ok().body("Fetched Data deleted with ID: " + id);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllFetchedData() {
        fetchedDataRepository.deleteAll();
        return ResponseEntity.ok().body("All fetched data deleted");
    }
}