package by.bstu.razvod.lab2;

public class Calculait {

    public static Double bennedMethod(GenderEnum genderEnum, Float act, int height, int age, int weight){
        if (genderEnum.equals(GenderEnum.FEMALE)){
            return (655.0955 + (9.5634 * weight) + (1.8496 * height) - (4.6756 * age)) * act;
        } else {
            return (664.0955 + (13.5634 * weight) + (5 * height) - (6.6756 * age)) * act;
        }
    }
}
