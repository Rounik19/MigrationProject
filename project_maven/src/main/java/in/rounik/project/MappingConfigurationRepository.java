package in.rounik.project;
import org.springframework.stereotype.Repository;

@Repository
public interface MappingConfigurationRepository extends org.springframework.data.mongodb.repository.MongoRepository<MappingConfiguration, String> {
    
}