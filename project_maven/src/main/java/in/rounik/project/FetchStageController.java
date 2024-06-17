package in.rounik.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class FetchStageController {

    @Autowired
    private ApiConfigMappingConfigRepository apiConfigMappingConfigRepository;
    
    @Autowired
    private FetchedDataRepository fetchedDataRepository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/fetchData/{configId}")
    public ResponseEntity<?> fetchData(@PathVariable String configId) {
        ApiConfigMappingConfig config = apiConfigMappingConfigRepository.findById(configId).orElse(null);
        if (config == null) {
            return ResponseEntity.status(404).body("Config not found");
        }

        List<Object> finalData = new ArrayList<>();
        // List<JsonNode> finalData = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        PaginationInfo paginationInfo = new PaginationInfo(1, 0, true, 0);

        while (paginationInfo.isHasMoreFolders()) {
            // first fetch folders
            try {
                fetchFolders(config, paginationInfo, finalData, objectMapper);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error fetching folders: " + e.getMessage());
            }

            // now fetch articles
            try {
                fetchArticles(config, finalData);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error fetching articles: " + e.getMessage());
            }

            // now fetch translations
            try {
                fetchTranslations(config, finalData);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error fetching translations: " + e.getMessage());
            }
        }

        FetchedDataObject fetchedDataObject = new FetchedDataObject();
        fetchedDataObject.setFetchTime(new Date());
        fetchedDataObject.setLastModifiedTime(new Date());
        
        // Convert finalData to JSON
        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(finalData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting data to JSON");
        }

        fetchedDataObject.setFetchedData(jsonResponse);
        fetchedDataRepository.save(fetchedDataObject);

        return ResponseEntity.ok(jsonResponse);
    }

    private List<JsonNode> processJsonNodes(JsonNode jsonResponse, ApiConfigMappingConfig config, String path) {
        List<JsonNode> folderList = new ArrayList<>();
        JsonNode foldersNode = jsonResponse.at("/" + path.replace(config.getArticleFetchConfig().getNestedPathSeparator(), "/"));
        // folderList.add(foldersNode);
        if (!foldersNode.isMissingNode()) {
            if (foldersNode.isArray()) {
                foldersNode.forEach(folderList::add);
            } else {
                folderList.add(foldersNode);
            }
        } else {
            folderList.add(jsonResponse);
        }
        return folderList;
    }

    private void fetchFolders(ApiConfigMappingConfig config, PaginationInfo paginationInfo, List<Object> finalData, ObjectMapper objectMapper) throws Exception {
        // ResponseEntity<String> response = fetchFoldersResponse(config, page, offset);

        // to test the code, we will use a hardcoded response
        ResponseEntity<String> response1 = ResponseEntity.ok("{\"folders\": [{\"folder_id\": \"folder_1\",\"folder_name\": \"xyz\",\"subfolders\": [{\"subfolder_id\": \"subfolder_1\",\"subfolder_name\": \"abc\",\"articles\": [{\"article_id\": \"article_11\",\"title\": \"hello\"},{\"article_id\": \"article_12\",\"title\": \"hi\"}]},{\"subfolder_id\": \"subfolder_2\",\"subfolder_name\": \"bcd\",\"articles\": [{\"article_id\": \"article_21\",\"title\": \"bye\"}]}]},{\"folder_id\": \"folder_2\",\"folder_name\": \"pqr\",\"subfolders\": [{\"subfolder_id\": \"subfolder_3\",\"subfolder_name\": \"def\",\"articles\": [{\"article_id\": \"article_31\",\"title\": \"good morning\"}]}]},{\"folder_id\": \"folder_3\",\"folder_name\": \"lmn\",\"subfolders\": [{\"subfolder_id\": \"subfolder_4\",\"subfolder_name\": \"ghi\",\"articles\": [{\"article_id\": \"article_41\",\"title\": \"good night\"}]}]}]}");
        ResponseEntity<String> response2 = ResponseEntity.ok("{\"folder_id\": \"folder_1\",\"folder_name\": \"xyz\",\"subfolders\": [{\"subfolder_id\": \"subfolder_1\",\"subfolder_name\": \"abc\",\"articles\": [{\"article_id\": \"article_11\",\"title\": \"hello\"},{\"article_id\": \"article_12\",\"title\": \"hi\"}]},{\"subfolder_id\": \"subfolder_2\",\"subfolder_name\": \"bcd\",\"articles\": [{\"article_id\": \"article_21\",\"title\": \"bye\"}]}]}");
        ResponseEntity<String> response3 = ResponseEntity.ok("{\"folder_id\": \"folder_1\",\"folder_name\": \"xyz\",\"subfolders\": [{\"subfolder_id\": \"subfolder_1\",\"subfolder_name\": \"abc\",\"article_ids\": [\"ar_11\", \"ar_12\", \"ar_13\"]}]}");
        JsonNode rootNode = objectMapper.readTree(response1.getBody());
        String path = config.getFolderFetchConfig().getFoldersPath();
        List<JsonNode> folderNodes = processJsonNodes(rootNode, config, path);
        if(!folderNodes.isEmpty()){
            processFolders(folderNodes, config, finalData);
            if(config.getFolderFetchConfig().getPaginationConfig().getPageParam() != null){
                paginationInfo.setPage(paginationInfo.getPage() + 1);
            }
            else if (config.getFolderFetchConfig().getPaginationConfig().getOffsetParam() != null){
                paginationInfo.setOffset(paginationInfo.getOffset() + config.getFolderFetchConfig().getPaginationConfig().getPageSize());
            }
        }
        else{
            paginationInfo.setHasMoreFolders(false);
        }
        paginationInfo.setCnt(paginationInfo.getCnt() + folderNodes.size());
        if (paginationInfo.getCnt() > 0){
            paginationInfo.setHasMoreFolders(false);
        }
    }

    private void processFolders(List<JsonNode> folderNodes, ApiConfigMappingConfig config, List<Object> finalData) {
        for (JsonNode folderNode : folderNodes) {
            processSingleFolder(folderNode, config, finalData);
        }
    }

    private void processSingleFolder(JsonNode folderNode, ApiConfigMappingConfig config, List<Object> finalData) {
        List<String> requiredFieldValues = extractFieldIds(config, folderNode);
        Map<String, Object> orderedMap = new LinkedHashMap<>();
        orderedMap.put("fetchedFolder", folderNode);
        orderedMap.put("requiredFieldValues", requiredFieldValues);
        finalData.add(orderedMap);
    }

    private List<String> extractFieldIds(ApiConfigMappingConfig config, JsonNode folderNode) {
        List<String> fieldIds = new ArrayList<>();
        String[] nestedPaths = (config.getArticleFetchConfig().getNestedFieldPath().replace(config.getArticleFetchConfig().getNestedPathSeparator(), "/")).split("/");
        performDFS(folderNode, nestedPaths, 0, config, fieldIds);
        return fieldIds;
    }
    
    private void performDFS(JsonNode currentNode, String[] nestedPaths, int currentIndex, ApiConfigMappingConfig config, List<String> fieldIds) {
        if (currentIndex == nestedPaths.length) {
            JsonNode fieldNode = currentNode;
            String fieldId = extractFieldId(fieldNode, config);
            if(!fieldId.isEmpty()){
                fieldIds.add(fieldId);
            }
            return;
        }
        List<JsonNode> nextNodes = processJsonNodes(currentNode, config, nestedPaths[currentIndex]);
        for (JsonNode nxtNode : nextNodes) {
            performDFS(nxtNode, nestedPaths, currentIndex + 1, config, fieldIds);
        }
    }

    private String extractFieldId(JsonNode fieldNode, ApiConfigMappingConfig config) {
        for (String idPath : config.getArticleFetchConfig().getFieldParamPaths()) {
            JsonNode idNode = fieldNode.at("/" + idPath.replace(config.getArticleFetchConfig().getNestedPathSeparator(), "/"));
            if (!idNode.isMissingNode()) {
                String idValue = idNode.asText();

                if (config.getArticleFetchConfig().getFieldParamRegex() != null && !config.getArticleFetchConfig().getFieldParamRegex().isEmpty()) {
                    Matcher matcher = Pattern.compile(config.getArticleFetchConfig().getFieldParamRegex()).matcher(idValue);
                    if (matcher.find()) {
                        return matcher.group(1);
                    }
                }
                return idValue;
            }
            else {
                return "No id found";
            }
        }
        return fieldNode.asText();
    }

    private ResponseEntity<String> fetchFoldersResponse(ApiConfigMappingConfig config, int page, int offset) {
        String url = constructFolderFetchUrl(config, page, offset);
        HttpHeaders headers = constructHeaders(config);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    private String constructFolderFetchUrl(ApiConfigMappingConfig config, int page, int offset) {
        StringBuilder url = new StringBuilder(config.getFolderFetchConfig().getApiFetchConfig().getHostname() + config.getFolderFetchConfig().getApiFetchConfig().getPath() + "?");
        if (config.getFolderFetchConfig().getPaginationConfig().getPageParam() != null) {
            url.append(config.getFolderFetchConfig().getPaginationConfig().getPageParam()).append("=").append(page).append("&");
        }

        if (config.getFolderFetchConfig().getPaginationConfig().getPageSizeParam() != null) {
            url.append(config.getFolderFetchConfig().getPaginationConfig().getPageSizeParam()).append("=").append(config.getFolderFetchConfig().getPaginationConfig().getPageSize()).append("&");
        }

        if (config.getFolderFetchConfig().getPaginationConfig().getOffsetParam() != null) {
            url.append(config.getFolderFetchConfig().getPaginationConfig().getOffsetParam()).append("=").append(offset).append("&");
        }

        return url.toString();
    }

    private void fetchArticles(ApiConfigMappingConfig config, List<Object> finalData) throws Exception {
        for (Object folderData : finalData) {
            if (folderData instanceof Map) {
                Map<String, Object> folderMap = (Map<String, Object>) folderData;
                List<String> requiredFieldValues = (List<String>) folderMap.get("requiredFieldValues");
                // remove the field values from the map
                folderMap.remove("requiredFieldValues");
                for (String fieldId : requiredFieldValues) {
                    fetchArticle(folderMap, fieldId, config);
                }
            }
        }
    }

    private void fetchArticle(Map<String, Object> folderMap, String fieldId, ApiConfigMappingConfig config) {
        // ResponseEntity<String> response = fetchArticleResponse(config, fieldId);
        // we use a hardcoded response to test the code
        ResponseEntity<String> response1 = ResponseEntity.ok("{\"folder_id : \":\"folder_1\",\"articles\":[{\"article_id\":\"article_1\",\"title\":\"Introduction to Java\",\"author\":\"John Doe\",\"content\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam in velit vel nunc malesuada commodo.\"},{\"article_id\":\"article_2\",\"title\":\"Understanding Spring Framework\",\"author\":\"Jane Smith\",\"content\":\"Praesent pretium aliquet erat, at ullamcorper dui rhoncus vel. Proin vehicula ultricies diam nec placerat.\"},{\"article_id\":\"article_3\",\"title\":\"Mastering RESTful APIs\",\"author\":\"Alex Johnson\",\"content\":\"Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nam volutpat, mauris eget blandit condimentum, arcu est laoreet odio, et dictum arcu metus id mauris.\"}],\"uselessField3\":\"value3\"}");
        if (response1.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode articlesNode = objectMapper.readTree(response1.getBody());
                List<JsonNode> articles = processJsonNodes(articlesNode, config, config.getArticleFetchConfig().getArticlesPath());
                folderMap.put("FolderArticles", articles);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ResponseEntity<String> fetchArticleResponse(ApiConfigMappingConfig config, String fieldId) {
        StringBuilder url = new StringBuilder(config.getArticleFetchConfig().getApiFetchConfig().getHostname() + config.getArticleFetchConfig().getApiFetchConfig().getPath() + "?");
        if (fieldId != null) {
            url.append(config.getArticleFetchConfig().getFieldParam()).append("=").append(fieldId).append("&");
        }
        HttpHeaders headers = constructHeaders(config);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url.toString(), HttpMethod.GET, entity, String.class);
    }

    private HttpHeaders constructHeaders(ApiConfigMappingConfig config) {
        HttpHeaders headers = new HttpHeaders();
        if (config.getFolderFetchConfig().getApiFetchConfig().getRequestHeaders() != null) {
            config.getFolderFetchConfig().getApiFetchConfig().getRequestHeaders().forEach(headers::set);
        }

        if (config.getFolderFetchConfig().getApiFetchConfig().getAuthToken() != null) {
            headers.set("Authorization", "Bearer " + config.getFolderFetchConfig().getApiFetchConfig().getAuthToken());
        }

        return headers;
    }

    private void fetchTranslations(ApiConfigMappingConfig config, List<Object> finalData) throws Exception {
        for (Object folderData : finalData) {
            if (folderData instanceof Map) {
                Map<String, Object> folderMap = (Map<String, Object>) folderData;
                if (folderMap.containsKey("FolderArticles")) {
                    List<JsonNode> articles = (List<JsonNode>) folderMap.get("FolderArticles");
                    for (JsonNode article : articles) {
                        fetchTranslation(article, config);
                    }
                }
            }
        }
    }

    private void fetchTranslation(JsonNode article, ApiConfigMappingConfig config) {
        List<String> translationFields = config.getTranslationFetchConfig().getTranslationFields();
        ObjectNode translationsNode = JsonNodeFactory.instance.objectNode();
        // create a list of JSONS for translations
        List<JsonNode> translations = new ArrayList<>();

        for (String translationField : translationFields) {
            JsonNode translation = null;
            if (config.getTranslationFetchConfig().getCommonTranslationFieldPath() != null && !config.getTranslationFetchConfig().getCommonTranslationFieldPath().isEmpty()) {
                JsonNode translationNode = article.at("/" + config.getTranslationFetchConfig().getCommonTranslationFieldPath().replace(config.getArticleFetchConfig().getNestedPathSeparator(), "/") + "/" + translationField);
                String translationParamValue = translationNode.asText();
                translation = fetchTranslationForParam(translationParamValue, config, article, translationField);
            } else {
                translation = fetchTranslationForParam(null, config, article, translationField);
            }

            if (translation != null) {
                translationsNode.set(translationField, translation);
            }
            translations.add(translation);
        }
        ((ObjectNode) article).set("translations", translationsNode);
    }

    private JsonNode fetchTranslationForParam(String translationParamValue, ApiConfigMappingConfig config, JsonNode article, String translationField) {
        String articleId = article.at("/" + config.getArticleFetchConfig().getArticleIdPath().replace(config.getArticleFetchConfig().getNestedPathSeparator(), "/")).asText();
        // ResponseEntity<String> response = fetchTranslationResponse(config, articleId, translationParamValue, translationField);

        // we use a hardcoded response to test the code
        ResponseEntity<String> response = ResponseEntity.ok("{\"article_id\":\"article_1\",\"title\":\"जावा का परिचय\",\"author\":\"जॉन डो\",\"content\":\"लोरेम इप्सम डोलर सिट एमेट, कॉन्सेक्टेटर एडिपिसिंग एलिट। नुल्लम इन वेलिट वेल नुंक मालेसुआदा कॉमोडो।\"}");
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode translationNode = objectMapper.readTree(response.getBody());
                return translationNode;                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private ResponseEntity<String> fetchTranslationResponse(ApiConfigMappingConfig config, String articleId, String translationParamValue, String translationField) {
        // use the same api to fetch articles
        StringBuilder url = new StringBuilder(config.getArticleFetchConfig().getApiFetchConfig().getHostname() + config.getArticleFetchConfig().getApiFetchConfig().getPath() + "?");
        String translationFetchParam = config.getTranslationFetchConfig().getTranslationFetchParam();
        if(translationFetchParam != null && !translationFetchParam.isEmpty()){
            url.append(translationFetchParam).append("=").append(translationParamValue).append("&");
            url.append(config.getTranslationFetchConfig().getLocaleParam()).append("=").append(translationField).append("&");
        }
        else{
            url.append(config.getArticleFetchConfig().getArticleIdPath()).append("=").append(articleId).append("&");
            url.append(config.getTranslationFetchConfig().getLocaleParam()).append("=").append(translationField).append("&");
        }
        HttpHeaders headers = constructHeaders(config);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url.toString(), HttpMethod.GET, entity, String.class);
    }

    @GetMapping("/MigrateData/{configId}")
    public ResponseEntity<?> migrateData(@PathVariable String configId) {
        ApiConfigMappingConfig config = apiConfigMappingConfigRepository.findById(configId).orElse(null);
        if (config == null) {
            return ResponseEntity.status(404).body("Config not found");
        }

        List<Object> finalData = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        PaginationInfo paginationInfo = new PaginationInfo(1, 0, true, 0);

        while (paginationInfo.isHasMoreFolders()) {
            // first fetch folders
            try {
                fetchFolders(config, paginationInfo, finalData, objectMapper);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error fetching folders: " + e.getMessage());
            }

            // now fetch articles
            try {
                fetchArticles(config, finalData);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error fetching articles: " + e.getMessage());
            }

            // now fetch translations
            try {
                fetchTranslations(config, finalData);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error fetching translations: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(finalData);
    }

}
