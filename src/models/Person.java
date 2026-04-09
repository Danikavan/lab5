package models;

/**
 * Автор лабораторной работы.
 * Содержит имя, вес, цвет глаз, цвет волос, национальность и местоположение.
 */
public class Person {
    private String name; //Поле не может быть null, строка не может быть пустой
    private long weight; //Значение поля должно быть больше 0
    private EyeColor eyeColor; //Поле может быть null
    private HairColor hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле не может быть null

    /**
     * Создаёт объект автора.
     * @param name имя (не null, не пусто)
     * @param weight вес (>0)
     * @param eyeColor цвет глаз (может быть null)
     * @param hairColor цвет волос (может быть null)
     * @param nationality национальность (может быть null)
     * @param location местоположение (не null)
     * @throws IllegalArgumentException при нарушении ограничений
     */
    public Person(String name, long weight, EyeColor eyeColor, HairColor hairColor, Country nationality, Location location) {
        setName(name);
        setWeight(weight);
        setEyeColor(eyeColor);
        setHairColor(hairColor);
        setNationality(nationality);
        setLocation(location);
    }

    // геттеры
    public String getName() {
        return name;
    }
    public long getWeight() {
        return weight;
    }
    public EyeColor getEyeColor() {
        return eyeColor;
    }
    public HairColor getHairColor() {
        return hairColor;
    }
    public Country getNationality() {
        return nationality;
    }
    public Location getLocation() {
        return location;
    }

    // сеттеры с валидацией
    /**
     * Устанавливает имя автора.
     * @param name новое имя (не null, не пусто)
     * @throws IllegalArgumentException при нарушении
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("name не может быть null или пустым");
        this.name = name;
    }

    /**
     * Устанавливает вес автора.
     * @param weight новое значение (>0)
     * @throws IllegalArgumentException если weight <= 0
     */
    public void setWeight(long weight) {
        if (weight <= 0) throw new IllegalArgumentException("weight должно быть > 0");
        this.weight = weight;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    } // может быть null
    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }
    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    /**
     * Устанавливает местоположение автора.
     * @param location новое местоположение (не null)
     * @throws IllegalArgumentException если location = null
     */
    public void setLocation(Location location) {
        if (location == null) throw new IllegalArgumentException("location не может быть null");
        this.location = location;
    }

    /**
     * Проверяет корректность объекта
     * @return true, если все обязательные поля заполнены верно
     */
    public boolean isValid() {
        if (name == null || name.trim().isEmpty()) return false;
        if (weight <= 0) return false;
        // eyeColor, hairColor, nationality могут быть null
        if (location == null) return false; // достаточно проверки на null
        return true;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                ", location=" + location +
                '}';
    }
}
