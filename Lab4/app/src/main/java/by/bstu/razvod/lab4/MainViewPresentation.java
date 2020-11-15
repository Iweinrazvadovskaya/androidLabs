package by.bstu.razvod.lab4;

import by.bstu.razvod.lab4.model.ContactModel;

public class MainViewPresentation {
    private ContactModel model;
    private boolean isSelected;

    public MainViewPresentation(ContactModel model, boolean isSelected) {
        this.model = model;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ContactModel getModel() {
        return model;
    }
}
