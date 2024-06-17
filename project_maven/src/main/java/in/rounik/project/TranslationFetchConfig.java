package in.rounik.project;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TranslationFetchConfig {
    private String localeParam;
    private String translationFetchParam; // translation parameter to be used to fetch translations(eg. "article_id", "locale")
    private List<String> translationFields; // translation parameters at provider's end(eg. "en", "fr", "hin")
    private String commonTranslationFieldPath; // path to the field(upto the common level) for translations
}