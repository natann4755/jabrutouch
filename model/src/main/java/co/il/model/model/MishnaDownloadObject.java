package co.il.model.model;

import java.util.ArrayList;
import java.util.List;

public class MishnaDownloadObject {

    private String masechetName;
    private List<MishnayotItem> mishnayotItemsDB = new ArrayList<>();

    public void setMasechetName(String masechetName) {
        this.masechetName = masechetName;
    }

    public String getMasechetName() {
        return masechetName;
    }

    public void setMishnayotItemsDB(List<MishnayotItem> mishnayotItemsDB) {
        this.mishnayotItemsDB = mishnayotItemsDB;
    }

    public List<MishnayotItem> getMishnayotItemsDB() {
        return mishnayotItemsDB;
    }
}
