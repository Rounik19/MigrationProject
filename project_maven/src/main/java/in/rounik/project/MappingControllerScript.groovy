import groovy.json.JsonSlurper
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.domain.Sort
import in.rounik.project.FetchedDataObject
import in.rounik.project.MappingConfiguration
import in.rounik.project.MappingConfigurationRepository
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.ArrayNode

def ConvertStringToJsonNode(String fetchedData) {
    // Deserialize fetchedData into JsonNode using ObjectMapper
    def objectMapper = new ObjectMapper()
    def rootNode
    try {
        rootNode = objectMapper.readTree(fetchedData)
    } catch (Exception e) {
        return null
    }
    return rootNode
}

def processDataNode(JsonNode dataNode, Map<String, Object> mappingConfiguration) {
    def objectMapper = new ObjectMapper()
    def transformedData = objectMapper.createObjectNode()

    mappingConfiguration.each { key, value ->
        if (value instanceof String) {
            transformedData.put(value, dataNode.get(key)?.asText() ?: "")
        } else if (value instanceof Map) {
            if (value.target && value.transform) {
                def itemsArray = objectMapper.createArrayNode()
                dataNode.get(key)?.each { subfolder ->
                    subfolder.get(value.transform[0].source)?.each { file ->
                        def item = objectMapper.createObjectNode()
                        value.transform[0].fields.each { fieldKey, fieldValue ->
                            // now get the value of the fieldKey from the file
                            def fieldKeyValue = file.get(fieldKey)?.asText() ?: ""
                            if (fieldValue.startsWith("{") && fieldValue.endsWith("}")) {
                                String subfolderField = fieldValue.substring(1, fieldValue.length() - 1)
                                item.put(fieldKey, subfolder.get(subfolderField)?.asText() ?: "")
                            } else {
                                item.put(fieldValue, fieldKeyValue)
                            }
                        }
                        itemsArray.add(item)
                    }
                }
                transformedData.set(value.target, itemsArray)
            }
        }
    }
    return transformedData
}


def processFetchedData(fetchedData, mappingConfiguration) {

    // Test code
    def fetchedData1 = [
        [
            getFetchedData: { ->
                '''[
                    {
                        "folder_id": "folder_1",
                        "folder_name": "Folder 1",
                        "subfolders": [
                            {
                                "subfolder_id": "subfolder_1",
                                "subfolder_name": "Subfolder 1",
                                "files": [
                                    {
                                        "file_id": "file_3",
                                        "file_name": "File 3",
                                        "file_size": "4096"
                                    },
                                    {
                                        "file_id": "file_2",
                                        "file_name": "File 2",
                                        "file_size": "2048"
                                    }
                                ]
                            },
                            {
                                "subfolder_id": "subfolder_2",
                                "subfolder_name": "Subfolder 2",
                                "files": [
                                    {
                                        "file_id": "file_4",
                                        "file_name": "File 4",
                                        "file_size": "8192"
                                    }
                                ]
                            }
                        ]
                    }
                ]'''
            }
        ]
    ]

    def mappingConfiguration1 = [
        "folder_id": "id",
        "folder_name": "name",
        "subfolders": [
            "target": "items",
            "transform": [
                [
                    "source": "files",
                    "fields": [
                        "file_id": "id",
                        "file_name": "name",
                        "file_size": "size",
                        "parentSubfolderId": "{subfolder_id}"
                    ]
                ]
            ]
        ]
    ]

    def fetchedData2 = [
        [
            getFetchedData: { ->
                '''[
                    {
                        "folder_id": "folder_1",
                        "folder_name": "Folder 1",
                        "subfolders": [
                            {
                                "subfolder_id": "subfolder_1",
                                "subfolder_name": "Subfolder 1",
                                "files": [
                                    {
                                        "file_id": "file_3",
                                        "file_name": "File 3",
                                        "file_size": "4096"
                                    },
                                    {
                                        "file_id": "file_2",
                                        "file_name": "File 2",
                                        "file_size": "2048"
                                    }
                                ],
                                "metadata": {
                                    "owner": "Alice",
                                    "created_at": "2023-01-15"
                                }
                            },
                            {
                                "subfolder_id": "subfolder_2",
                                "subfolder_name": "Subfolder 2",
                                "files": [
                                    {
                                        "file_id": "file_4",
                                        "file_name": "File 4",
                                        "file_size": "8192"
                                    }
                                ],
                                "metadata": {
                                    "owner": "Bob",
                                    "created_at": "2023-02-20"
                                }
                            }
                        ],
                        "metadata": {
                            "created_by": "Admin",
                            "created_at": "2023-01-01"
                        }
                    }
                ]'''
            }
        ]
    ]

    def mappingConfiguration2 = [
        "folder_id": "id",
        "folder_name": "name",
        // "metadata": [
        //     "created_by": "createdBy",
        //     "created_at": "createdAt"
        // ],
        "subfolders": [
            "target": "items",
            "transform": [
                [
                    "source": "subfolders",
                    "fields": [
                        "subfolder_id": "id",
                        "subfolder_name": "name",
                        "metadata": [
                            "owner": "owner",
                            "created_at": "createdAt"
                        ],
                        // "files": [
                        //     "target": "files",
                        //     "transform": [
                        //         [
                        //             "source": "files",
                        //             "fields": [
                        //                 "file_id": "id",
                        //                 "file_name": "name",
                        //                 "file_size": "size",
                        //                 "parentSubfolderId": "{subfolder_id}"
                        //             ]
                        //         ]
                        //     ]
                        // ]
                    ]
                ]
            ]
        ]
    ]



    def processedData = []
    for (data in fetchedData2) {
        String fetcheddata = data.getFetchedData()
        def dataNodes = ConvertStringToJsonNode(fetcheddata)
        for (dataNode in dataNodes) {
            def processedDataNode = processDataNode(dataNode, mappingConfiguration2)
            processedData.add(dataNode)
            processedData.add(processedDataNode)
        }
    }
    return processedData
}
