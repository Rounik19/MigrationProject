package in.rounik.project;
import org.springframework.stereotype.Repository;

@Repository
public interface FetchedDataRepository extends org.springframework.data.mongodb.repository.MongoRepository<FetchedDataObject, String> {
    
}