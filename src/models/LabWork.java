package models;

import java.time.ZonedDateTime;

/**
 * Класс лабораторной работы.
 * Является основным элементом коллекции.
 * Сравнение по умолчанию — по имени, затем по id.
 */
public class LabWork implements Comparable<LabWork> {
    private Integer id; // >0, уникальный, авто
    private String name; // не null, не пусто
    private Coordinates coordinates; // не null
    private ZonedDateTime creationDate; // не null, авто
    private Float minimalPoint; // >0 или null
    private long personalQualitiesMaximum; // >0
    private String description; // не null
    private Difficulty difficulty; // может быть null
    private Person author; // не null

    /**
     * Конструктор для создания объекта из CSV (со всеми полями).
     * @param id идентификатор (должен быть >0)
     * @param name название (не null, не пусто)
     * @param coordinates координаты (не null)
     * @param creationDate дата создания (не null)
     * @param minimalPoint минимальный балл (null или >0)
     * @param personalQualitiesMaximum максимум качеств (>0)
     * @param description описание (не null)
     * @param difficulty сложность (может быть null)
     * @param author автор (не null)
     * @throws IllegalArgumentException при нарушении ограничений
     */
    public LabWork(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Float minimalPoint, long personalQualitiesMaximum, String description, Difficulty difficulty, Person author) {
        if (id != null) {
            setId(id);
        }
        setName(name);
        setCoordinates(coordinates);
        if (creationDate != null) {
            setCreationDate(creationDate);
        }
        setMinimalPoint(minimalPoint);
        setPersonalQualitiesMaximum(personalQualitiesMaximum);
        setDescription(description);
        setDifficulty(difficulty);
        setAuthor(author);
    }

    /**
     * Конструктор для нового объекта (без id и creationDate).
     * @param name название
     * @param coordinates координаты
     * @param minimalPoint минимальный балл
     * @param personalQualitiesMaximum максимум качеств
     * @param description описание
     * @param difficulty сложность
     * @param author автор
     */
    public LabWork(String name, Coordinates coordinates, Float minimalPoint, long personalQualitiesMaximum, String description, Difficulty difficulty, Person author) {
        this(null, name, coordinates, null, minimalPoint, personalQualitiesMaximum, description, difficulty, author);
    }

    // Геттеры
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Float getMinimalPoint() {
        return minimalPoint;
    }

    public long getPersonalQualitiesMaximum() {
        return personalQualitiesMaximum;
    }

    public String getDescription() {
        return description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Person getAuthor() {
        return author;
    }

    // Сеттеры с валидацией
    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id должен быть > 0 и не null");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name не может быть null или пустым");
        }
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("coordinates не может быть null");
        }
        this.coordinates = coordinates;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("creationDate не может быть null");
        }
        this.creationDate = creationDate;
    }

    public void setMinimalPoint(Float minimalPoint) {
        if (minimalPoint != null && minimalPoint <= 0) {
            throw new IllegalArgumentException("minimalPoint должен быть > 0 или null");
        }
        this.minimalPoint = minimalPoint;
    }

    public void setPersonalQualitiesMaximum(long personalQualitiesMaximum) {
        if (personalQualitiesMaximum <= 0) {
            throw new IllegalArgumentException("personalQualitiesMaximum должен быть > 0");
        }
        this.personalQualitiesMaximum = personalQualitiesMaximum;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description не может быть null");
        }
        this.description = description;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty; // может быть null
    }

    public void setAuthor(Person author) {
        if (author == null) {
            throw new IllegalArgumentException("author не может быть null");
        }
        this.author = author;
    }

    /**
     * Сравнение по имени, при равенстве — по id.
     * @param o другой объект LabWork
     * @return результат сравнения
     */
    @Override
    public int compareTo(LabWork o) {
        int nameCmp = this.name.compareTo(o.name);
        if (nameCmp != 0) return nameCmp;
        return this.id.compareTo(o.id);
    }
    /**
     * Проверяет корректность объекта.
     * @return true, если все поля соответствуют ограничениям
     */
    public boolean isValid() {
        if (id == null || id <= 0) return false;
        if (name == null || name.trim().isEmpty()) return false;
        if (coordinates == null || !coordinates.isValid()) return false;
        if (creationDate == null) return false;
        if (minimalPoint != null && minimalPoint <= 0) return false;
        if (personalQualitiesMaximum <= 0) return false;
        if (description == null) return false;
        if (author == null || !author.isValid()) return false;
        return true;
    }

    @Override
    public String toString() {
        return "LabWork{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", minimalPoint=" + minimalPoint +
                ", personalQualitiesMaximum=" + personalQualitiesMaximum +
                ", description='" + description + '\'' +
                ", difficulty=" + difficulty +
                ", author=" + author +
                '}';
    }
}