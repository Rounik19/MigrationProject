package in.rounik.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationConfig {
    private String pageParam;
    private String offsetParam;
    private String pageSizeParam;
    private int pageSize;
    private int offsetSize;
}
