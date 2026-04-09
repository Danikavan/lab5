package models;

/**
 * Координаты лабораторной работы.
 * Содержит значения x и y, оба не могут быть null.
 */
public class Coordinates {
    private Float x; //Поле не может быть null
    private Double y; //Поле не может быть null

    /**
     * Создаёт объект координат.
     * @param x координата X (не null)
     * @param y координата Y (не null)
     */
    public Coordinates(Float x, Double y) {
        setX(x);
        setY(y);
    }

    /**
     * @return координата X
     */
    public Float getX() {
        return x;
    }

    /**
     * @return координата Y
     */
    public Double getY() {
        return y;
    }

    /**
     * Устанавливает координату X.
     * @param x новое значение (не null)
     * @throws IllegalArgumentException если x = null
     */
    public void setX(Float x) {
        if (x == null) throw new IllegalArgumentException("x не может быть null");
        this.x = x;
    }

    /**
     * Устанавливает координату Y.
     * @param y новое значение (не null)
     * @throws IllegalArgumentException если y = null
     */
    public void setY(Double y) {
        if (y == null) throw new IllegalArgumentException("y не может быть null");
        this.y = y;
    }

    /**
     * Проверяет корректность координат.
     * @return true, если поля не null
     */
    public boolean isValid() {
        return x != null && y != null;
    }

    @Override
    public String toString() {
        return "Coordinates{" + "x=" + x + ", y=" + y + '}';
    }
}
