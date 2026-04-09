package models;

/**
 * Местоположение автора.
 * Содержит координаты x, y, z и необязательное название.
 */
public class Location {
    private int x;
    private int y;
    private float z;
    private String name; //Поле может быть null

    /**
     * Создаёт объект местоположения.
     * @param x координата X
     * @param y координата Y
     * @param z координата Z
     * @param name название (может быть null)
     */
    public Location(int x, int y, float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    // геттеры
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public float getZ() {
        return z;
    }
    public String getName() {
        return name;
    }

    // сеттеры
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setZ(float z) {
        this.z = z;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }
}
