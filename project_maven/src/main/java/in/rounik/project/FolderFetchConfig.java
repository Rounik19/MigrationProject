package in.rounik.project;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderFetchConfig {
    private ApiFetchConfig apiFetchConfig;
    private PaginationConfig paginationConfig;

    // Folder-specific attributes
    private String foldersPath; // path to the field "folders" in the response(if response is a List of folders)
    private String folderIdPath; // path to the field "folder_id" inside a folder
}