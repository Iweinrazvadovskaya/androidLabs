package by.bstu.razvod.listtviewcontextapp;

public class MainViewPresentation {
    private String model;
    private boolean isSelected;

    public MainViewPresentation(String model, boolean isSelected) {
        this.model = model;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getModel() {
        return model;
    }
}
