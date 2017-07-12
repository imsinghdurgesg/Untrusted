package singh.durgesh.com.applocker.model;

/**
 * Created by DSingh on 6/19/2017.
 */

public class CheckBoxState {
    private int position;
    private boolean isSelected;
    private String packageName;

    public CheckBoxState()
    {
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    private String appLabel;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}
