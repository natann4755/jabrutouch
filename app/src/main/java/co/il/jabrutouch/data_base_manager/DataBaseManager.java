package co.il.jabrutouch.data_base_manager;

import android.content.Context;
import com.google.gson.Gson;
import java.util.List;
import co.il.model.model.ChatObject;
import co.il.model.model.MessageObject;
import co.il.model.model.MishnayotItem;
import co.il.model.model.PagesItem;
import co.il.sqlcore.DBHandler;
import static co.il.sqlcore.DBKeys.GEMARA_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_TABLE;




public class DataBaseManager {




    /**
     * create table for gemara
     *
     * @param context Context
     */
    public void createGemaraTables(Context context) {

        DBHandler dbHandler = new DBHandler(context);
        dbHandler.createGemaraTable();
        dbHandler.createGemaraVideoPartsTable();
        dbHandler.createGemaraGalleryTable();


    }


    /**
     * create table for mishna
     *
     * @param context Context
     */
    public void createMishnaTables(Context context) {

        DBHandler dbHandler = new DBHandler(context);
        dbHandler.createMishnaTable();
        dbHandler.createMishnaVideoPartsTable();
        dbHandler.createMishnaGalleryTable();

    }




    /**
     * on audio of gemara finished download
     *
     * @param context      Context
     * @param pagesItem    PagesItem
     * @param pathName     String, the path where the audio saved
     * @param pagePathName String, the path where the audio page saved
     */
    public void onGeamraAudioDownloadFinished(Context context, PagesItem pagesItem, String pathName, String pagePathName) {

        Gson gson = new Gson();
        PagesItem newPagesItem = gson.fromJson(gson.toJson(pagesItem), PagesItem.class);

        generateAudioGemaraObject(newPagesItem, pathName, pagePathName);

        DBHandler dbHandler = new DBHandler(context);

        if (dbHandler.findGemaraById(String.valueOf(newPagesItem.getId()), GEMARA_TABLE) != null) {

            dbHandler.updateGemaraAudioToTable(String.valueOf(newPagesItem.getId()), newPagesItem.getAudio(), GEMARA_TABLE);

        } else {

            dbHandler.addTalmudToTable(newPagesItem, GEMARA_TABLE);
        }

        for (int i = 0; i < newPagesItem.getVideoPart().size(); i++) {
            dbHandler.addGemaraVideoPartsToTable(newPagesItem.getVideoPart().get(i), newPagesItem.getId());

        }

        for (int i = 0; i < newPagesItem.getGallery().size(); i++) {
            dbHandler.addGemaraGalleryToTable(newPagesItem.getGallery().get(i), newPagesItem.getId());

        }


    }




    /**
     * on audio of gemara finished download
     *
     * @param context       Context
     * @param mishnayotItem MishnayotItem
     * @param pathName      String, the path where the audio saved
     * @param pagePathName  String, the path where the audio page saved
     */
    public void onMishnaAudioDownloadFinished(Context context, MishnayotItem mishnayotItem, String pathName, String pagePathName) {

        Gson gson = new Gson();
        MishnayotItem newMishnayotItem = gson.fromJson(gson.toJson(mishnayotItem), MishnayotItem.class);

        generateAudioMishnaObject(newMishnayotItem, pathName, pagePathName);

        DBHandler dbHandler = new DBHandler(context);

        if (dbHandler.findMishnaById(String.valueOf(newMishnayotItem.getId()), MISHNA_TABLE) != null) {

            dbHandler.updateMishnaAudioToTable(String.valueOf(newMishnayotItem.getId()), newMishnayotItem.getAudio(), MISHNA_TABLE);

        } else {

            dbHandler.addMishnaToTable(newMishnayotItem);
        }


        if (newMishnayotItem.getVideoPart() != null) {

            for (int i = 0; i < newMishnayotItem.getVideoPart().size(); i++) {
                dbHandler.addMishnaVideoPartsToTable(newMishnayotItem.getVideoPart().get(i), newMishnayotItem.getId());

            }
        }

        if (newMishnayotItem.getGallery() != null) {

            for (int i = 0; i < newMishnayotItem.getGallery().size(); i++) {
                dbHandler.addMishnaGalleryToTable(newMishnayotItem.getGallery().get(i), newMishnayotItem.getId());

            }
        }


    }




    /**
     * generate mishna audio object
     *
     * @param mishnayotItem MishnayotItem
     * @param pathName      String
     * @param pagePathName  String
     * @return mishnayotItem
     */
    private MishnayotItem generateAudioMishnaObject(MishnayotItem mishnayotItem, String pathName, String pagePathName) {

        mishnayotItem.setSederOrder(mishnayotItem.getSederOrder());
        mishnayotItem.setMasechetOrder(mishnayotItem.getMasechetOrder());
        mishnayotItem.setMasechetName(mishnayotItem.getMasechetName());
        mishnayotItem.setVideo("");
        mishnayotItem.setAudio(pathName);
        mishnayotItem.setTimeLine(0);
        mishnayotItem.setMediaType(""); // TODO
        mishnayotItem.setMishna(mishnayotItem.getMishna());
        mishnayotItem.setPage(pagePathName);

        return mishnayotItem;
    }




    /**
     * on video of gemara finished download
     *
     * @param context      Context
     * @param pagesItem    PagesItem
     * @param pathName     String, the path where the audio saved
     * @param pagePathName String, the path where the audio page saved
     */
    public void onVideoGemaraDownloadFinished(Context context, PagesItem pagesItem, String pathName, String pagePathName) {

        Gson gson = new Gson();
        PagesItem newPagesItem = gson.fromJson(gson.toJson(pagesItem), PagesItem.class);

        generateVideoGemaraObject(newPagesItem, pathName, pagePathName);

        DBHandler dbHandler = new DBHandler(context);

        if (dbHandler.findGemaraById(String.valueOf(newPagesItem.getId()), GEMARA_TABLE) != null) {

            dbHandler.updateVideoToTable(String.valueOf(newPagesItem.getId()), newPagesItem.getVideo(), GEMARA_TABLE);

        } else {

            dbHandler.addTalmudToTable(newPagesItem, GEMARA_TABLE);
        }

        for (int i = 0; i < newPagesItem.getVideoPart().size(); i++) {
            dbHandler.addGemaraVideoPartsToTable(newPagesItem.getVideoPart().get(i), newPagesItem.getId());

        }

        for (int i = 0; i < newPagesItem.getGallery().size(); i++) {
            dbHandler.addGemaraGalleryToTable(newPagesItem.getGallery().get(i), newPagesItem.getId());

        }


    }





    /**
     * on video of gemara finished download
     *
     * @param context       Context
     * @param mishnayotItem MishnayotItem
     * @param pathName      String, the path where the audio saved
     * @param pagePathName  String, the path where the audio page saved
     */
    public void onVideoMishnaDownloadFinished(Context context, MishnayotItem mishnayotItem, String pathName, String pagePathName) {

        Gson gson = new Gson();
        MishnayotItem newMishnayotItem = gson.fromJson(gson.toJson(mishnayotItem), MishnayotItem.class);

        generateVideoMishnaObject(newMishnayotItem, pathName, pagePathName);

        DBHandler dbHandler = new DBHandler(context);

        if (dbHandler.findMishnaById(String.valueOf(newMishnayotItem.getId()), MISHNA_TABLE) != null) {

            dbHandler.updateVideoMishnaToTable(String.valueOf(newMishnayotItem.getId()), newMishnayotItem.getVideo(), MISHNA_TABLE);

        } else {

            dbHandler.addMishnaToTable(newMishnayotItem);
        }


        if (newMishnayotItem.getVideoPart() != null) {

            for (int i = 0; i < newMishnayotItem.getVideoPart().size(); i++) {
                dbHandler.addMishnaVideoPartsToTable(newMishnayotItem.getVideoPart().get(i), newMishnayotItem.getId());

            }
        }


        if (newMishnayotItem.getGallery() != null) {

            for (int i = 0; i < newMishnayotItem.getGallery().size(); i++) {
                dbHandler.addMishnaGalleryToTable(newMishnayotItem.getGallery().get(i), newMishnayotItem.getId());

            }
        }


    }




    private MishnayotItem generateVideoMishnaObject(MishnayotItem mishnayotItem, String pathName, String pagePathName) {

        mishnayotItem.setSederOrder(mishnayotItem.getSederOrder());
        mishnayotItem.setMasechetOrder(mishnayotItem.getMasechetOrder());
        mishnayotItem.setMasechetName(mishnayotItem.getMasechetName());
        mishnayotItem.setVideo(pathName);
        mishnayotItem.setAudio("");
        mishnayotItem.setTimeLine(0);
        mishnayotItem.setMediaType(""); // TODO
        mishnayotItem.setMishna(mishnayotItem.getMishna());
        mishnayotItem.setPage(pagePathName);

        return mishnayotItem;


    }




    /**
     * generate gemara to mishna
     *
     * @param pagesItem PagesItem
     * @return pagesItem
     */
    private MishnayotItem generateMishnaObject(PagesItem pagesItem) {

        MishnayotItem mishnayotItem = new MishnayotItem();
        mishnayotItem.setSederOrder(pagesItem.getSederOrder());
        mishnayotItem.setMasechetOrder(pagesItem.getMasechetOrder());
        mishnayotItem.setMasechetName(pagesItem.getMasechetName());
        mishnayotItem.setId(pagesItem.getId());
        mishnayotItem.setPosition(pagesItem.getPosition());
        mishnayotItem.setVideo("");
        mishnayotItem.setAudio("");
        mishnayotItem.setMishna(pagesItem.getPageNumber());
        mishnayotItem.setPage(pagesItem.getPage());
        mishnayotItem.setDuration(pagesItem.getDuration());
        mishnayotItem.setChapter(pagesItem.getChapter());

        return mishnayotItem;


    }




    /**
     * generate gemara audio object
     *
     * @param pagesItem    PagesItem
     * @param pathName     String
     * @param pagePathName String
     * @return pagesItem
     */
    private PagesItem generateAudioGemaraObject(PagesItem pagesItem, String pathName, String pagePathName) {


        pagesItem.setSederOrder(pagesItem.getSederOrder());
        pagesItem.setMasechetOrder(pagesItem.getMasechetOrder());
        pagesItem.setMasechetName(pagesItem.getMasechetName());
        pagesItem.setVideo("");
        pagesItem.setAudio(pathName);
        pagesItem.setTimeLine(0);
        pagesItem.setMediaType("");
        pagesItem.setPage(pagePathName);

        return pagesItem;

    }







    private PagesItem generateMishnaItemToPageItem(MishnayotItem mishnayotItem) {

        PagesItem pagesItem = new PagesItem();
        pagesItem.setDuration(mishnayotItem.getDuration());
        pagesItem.setChapter(mishnayotItem.getChapter());
        pagesItem.setPageNumber(mishnayotItem.getMishna());
        pagesItem.setPresenter(mishnayotItem.getPresenter());
        pagesItem.setVideoPart(mishnayotItem.getVideoPart());
        pagesItem.setId(mishnayotItem.getId());
        pagesItem.setAudio(mishnayotItem.getAudio());
        pagesItem.setVideo(mishnayotItem.getVideo());
        pagesItem.setPage(mishnayotItem.getPage());
        pagesItem.setGallery(mishnayotItem.getGallery());
        pagesItem.setMasechetOrder(mishnayotItem.getMasechetOrder());
        pagesItem.setSederOrder(mishnayotItem.getSederOrder());
        pagesItem.setMasechetName(mishnayotItem.getMasechetName());
        pagesItem.setTimeLine(mishnayotItem.getTimeLine());
        pagesItem.setMediaType(mishnayotItem.getMediaType());

        return pagesItem;


    }




    /**
     * generate gemara video object
     *
     * @param pagesItem    PagesItem
     * @param pathName     String
     * @param pagePathName String
     * @return pagesItem
     */
    private PagesItem generateVideoGemaraObject(PagesItem pagesItem, String pathName, String pagePathName) {

        pagesItem.setSederOrder(pagesItem.getSederOrder());
        pagesItem.setMasechetOrder(pagesItem.getMasechetOrder());
        pagesItem.setMasechetName(pagesItem.getMasechetName());
        pagesItem.setVideo(pathName);
        pagesItem.setAudio("");
        pagesItem.setTimeLine(0);
        pagesItem.setMediaType("");
        pagesItem.setPage(pagePathName);

        return pagesItem;


    }





    /**
     * add page to data base
     *
     * @param context   Context
     * @param pagesItem PageItem
     * @param tableType String
     */
    public void addPageToDB(Context context, PagesItem pagesItem, String tableType) {

        Gson gson = new Gson();
        PagesItem newPagesItem = gson.fromJson(gson.toJson(pagesItem), PagesItem.class);

        generateVideoPageGemaraObject(newPagesItem);

        DBHandler dbHandler = new DBHandler(context);

        if (tableType.equals(GEMARA_TABLE)) {
            dbHandler.addTalmudToTable(newPagesItem, tableType);

            for (int i = 0; i < newPagesItem.getVideoPart().size(); i++) {
                dbHandler.addGemaraVideoPartsToTable(newPagesItem.getVideoPart().get(i), newPagesItem.getId());

            }

            for (int i = 0; i < newPagesItem.getGallery().size(); i++) {
                dbHandler.addGemaraGalleryToTable(newPagesItem.getGallery().get(i), newPagesItem.getId());

            }

        }


        else if (tableType.equals(MISHNA_TABLE)){

            dbHandler.addMishnaToTable(generateMishnaObject(newPagesItem));

            for (int i = 0; i < newPagesItem.getVideoPart().size(); i++) {
                dbHandler.addMishnaVideoPartsToTable(newPagesItem.getVideoPart().get(i), newPagesItem.getId());

            }

            for (int i = 0; i < newPagesItem.getGallery().size(); i++) {
                dbHandler.addMishnaGalleryToTable(newPagesItem.getGallery().get(i), newPagesItem.getId());

            }


        }
    }






    /**
     * generate gemara pge object
     *
     * @param newPagesItem PagesItem
     * @return newPagesItem
     */
    private PagesItem generateVideoPageGemaraObject(PagesItem newPagesItem) {

        newPagesItem.setSederOrder(newPagesItem.getSederOrder());
        newPagesItem.setMasechetOrder(newPagesItem.getMasechetOrder());
        newPagesItem.setMasechetName(newPagesItem.getMasechetName());
        newPagesItem.setVideo("");
        newPagesItem.setAudio("");
        newPagesItem.setTimeLine(0);
        newPagesItem.setMediaType("");
        newPagesItem.setPage(newPagesItem.getPage());
        newPagesItem.setGallery(newPagesItem.getGallery());
        newPagesItem.setVideoPart(newPagesItem.getVideoPart());

        return newPagesItem;


    }




    /**
     * create message table
     * @param context Context
     */
    public void createMessagesTables(Context context) {

        DBHandler dbHandler = new DBHandler(context);
        dbHandler.createChatTable();
        dbHandler.createMessageTable();

    }





    public void addMessageToMessageTable(Context context, MessageObject messageObject){

        DBHandler dbHandler = new DBHandler(context);
        dbHandler.addMessageToTable(messageObject);

    }





    public void addChatToChatTable(Context context, ChatObject chatObject){

        DBHandler dbHandler = new DBHandler(context);
        dbHandler.addChatToTable(chatObject);

    }





    public List<ChatObject> getAllChats(Context context, String tableName){

        DBHandler dbHandler = new DBHandler(context);
        return dbHandler.getAllChats(tableName);

    }





    public List<MessageObject> getAllMessages(Context context, String tableName, int chatId) {

        DBHandler dbHandler = new DBHandler(context);
        return dbHandler.getAllMessages(tableName, chatId);

    }





    public void updateChatToChatTable(Context context, ChatObject chatObject, String tableName) {


        DBHandler dbHandler = new DBHandler(context);
        dbHandler.updateChatTable(chatObject, tableName);


    }





    public void updateUnreadMessagesToChatTable(Context context, ChatObject chatObject, String tableName) {

        DBHandler dbHandler = new DBHandler(context);
        dbHandler.updateUnreadMessagesToChatTable(chatObject, tableName);

    }
}
