package co.il.model.model;

public class DownloadQueue {

    private int position;
    private PagesItem pagesItem;
    private String type;


    public void setPosition(int position) {
        this.position = position;
    }

    public void setPagesItem(PagesItem pagesItem) {
        this.pagesItem = pagesItem;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public PagesItem getPagesItem() {
        return pagesItem;
    }

    public String getType() {
        return type;
    }
}
