package in.rounik.project;

public class PaginationInfo {
    private int page;
    private int offset;
    private boolean hasMoreFolders;
    private int cnt = 0; // Counter to keep track of the number of folders processed

    public PaginationInfo(int page, int offset, boolean hasMoreFolders, int cnt) {
        this.page = page;
        this.offset = offset;
        this.hasMoreFolders = hasMoreFolders;
        this.cnt = cnt;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isHasMoreFolders() {
        return hasMoreFolders;
    }

    public void setHasMoreFolders(boolean hasMoreFolders) {
        this.hasMoreFolders = hasMoreFolders;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
