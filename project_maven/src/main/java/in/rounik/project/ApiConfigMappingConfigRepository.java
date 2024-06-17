package in.rounik.project;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiConfigMappingConfigRepository extends org.springframework.data.mongodb.repository.MongoRepository<ApiConfigMappingConfig, String> {
    
}