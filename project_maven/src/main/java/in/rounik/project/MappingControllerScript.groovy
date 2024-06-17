import groovy.json.JsonSlurper
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.domain.Sort
import in.rounik.project.FetchedDataObject
import in.rounik.project.MappingConfiguration
import in.rounik.project.MappingConfigurationRepository


def processSingleData(data, mappingConfiguration) {
    // implement the logic to process the data
    // for now, just print the data
    return data
}

// implement a function that takes fetchedData(list of datas) and then for all for all the datas in the list, processes it
def processFetchedData(fetchedData, mappingConfiguration) {
    // make a list to store all the processe data
    def processedData = []
    for (data in fetchedData) {
        processedData.add(processSingleData(data, mappingConfiguration))
    }
    return processedData
}