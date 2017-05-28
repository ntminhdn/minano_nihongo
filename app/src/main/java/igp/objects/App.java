package igp.objects;

/**
 * Created by Administrator on 18/01/2017.
 */

public class App {

    public static final String FIELD_NAME               = "name";
    public static final String FIELD_LINK               = "link";
    public static final String FIELD_ICON               = "icon";
    public static final String FIELD_DESCRIPTION        = "description";
    public static final String FIELD_SHOW               = "show";

    private String mName;
    private String mLink;
    private String mIcon;
    private String mDecription;
    private boolean mIsShow;

    public App() {
    }

    public App(String name, String link, String decription, boolean show) {
        this.mName = name;
        this.mLink = link;
        this.mDecription = decription;
        this.mIsShow = show;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

    public String getDecription() {
        return mDecription;
    }

    public void setDecription(String decription) {
        this.mDecription = decription;
    }

    public boolean isShow() {
        return mIsShow;
    }

    public void setShow(boolean show) {
        this.mIsShow = show;
    }

    @Override
    public String toString() {
        return "App{"
                + mName + "\n"
                + mLink + "\n"
                + mIcon + "\n"
                + mDecription + "\n"
                + mIsShow + "\n" + '}';
    }
}
