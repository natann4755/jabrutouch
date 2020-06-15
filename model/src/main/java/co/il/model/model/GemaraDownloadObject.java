package co.il.model.model;

import java.util.ArrayList;
import java.util.List;

public class GemaraDownloadObject {


    private String masechetName;
    private List<PagesItem> pagesItemsDB = new ArrayList<>();

    public void setMasechetName(String masechetName) {
        this.masechetName = masechetName;
    }

    public String getMasechetName() {
        return masechetName;
    }

    public void setPagesItemsDB(List<PagesItem> pagesItemsDB) {
        this.pagesItemsDB = pagesItemsDB;
    }

    public List<PagesItem> getPagesItemsDB() {
        return pagesItemsDB;
    }
}
