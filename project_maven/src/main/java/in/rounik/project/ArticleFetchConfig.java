package in.rounik.project;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleFetchConfig {
    private ApiFetchConfig apiFetchConfig;
    private String articlesPath; // path to the field "articles" in the response(if response is a List of articles)
    private String articleIdPath; // path to the field "article_id" inside an article
    private String nestedFieldPath; // path to the field(upto the level of nesting that is a list of JSONS)
    private String fieldParam; // field parameter to be used to fetch articles(eg. "folder_id", "article_id")
    private String NestedPathSeparator; // separator used by user(eg. ".")
    private List<String> fieldParamPaths; // path to the FieldParam inside the nestedFieldPath
    private String fieldParamRegex; // regex to be used to extract the fieldParam from the fieldParamPath
}
