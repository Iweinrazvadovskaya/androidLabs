package by.bstu.razvod.lab2;

public enum ActivityEnum{
    LOW_ACTIVITY(1, 1.2f), NORMAL_ACTIVITY(2, 1.55f), HEIGHT_ACTIVITY(3, 1.9f);

    private Integer id;
    public Float value;

    ActivityEnum(Integer id, Float value){
        this.id = id;
        this.value = value;
    }
}
