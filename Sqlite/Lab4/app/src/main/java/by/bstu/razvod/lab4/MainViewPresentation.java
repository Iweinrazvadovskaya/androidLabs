package by.bstu.razvod.lab4;

import by.bstu.razvod.lab4.database.ContactEntity;

public class MainViewPresentation {
    private ContactEntity model;
    private boolean isSelected;

    public MainViewPresentation(ContactEntity model, boolean isSelected) {
        this.model = model;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ContactEntity getModel() {
        if (model != null) {
            return model;
        } else {
            return null;
        }
    }
}
