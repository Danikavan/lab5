package managers;

import models.LabWork;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Vector;

/**
 * Управляет коллекцией объектов LabWork.
 * Хранит вектор элементов, дату инициализации, предоставляет методы для добавления, удаления, обновления, сортировки и других операций.
 */
public class CollectionManager {
    private final Vector<LabWork> collection;
    private final ZonedDateTime initDate;
    private final FileManager fileManager;

    /**
     * Создаёт менеджер коллекции, загружая данные из файла.
     * @param fileManager менеджер файлов
     */
    public CollectionManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.collection = fileManager.load();
        this.initDate = ZonedDateTime.now();
    }

    /**
     * Добавляет новый элемент в коллекцию.
     * Автоматически генерирует id и дату создания.
     * @param labWork объект для добавления (без id и creationDate)
     */
    public void add(LabWork labWork) {
        labWork.setId(generateId());
        labWork.setCreationDate(ZonedDateTime.now());
        collection.add(labWork);
    }

    /**
     * Обновляет существующий элемент по id.
     * @param id идентификатор элемента
     * @param newLabWork новый объект с обновлёнными данными
     */
    public void update(Integer id, LabWork newLabWork) {
        LabWork existing = findById(id);
        if (existing != null) {
            existing.setName(newLabWork.getName());
            existing.setCoordinates(newLabWork.getCoordinates());
            existing.setMinimalPoint(newLabWork.getMinimalPoint());
            existing.setPersonalQualitiesMaximum(newLabWork.getPersonalQualitiesMaximum());
            existing.setDescription(newLabWork.getDescription());
            existing.setDifficulty(newLabWork.getDifficulty());
            existing.setAuthor(newLabWork.getAuthor());
        }
    }

    /**
     * Удаляет элемент по id.
     * @param id идентификатор элемента
     * @return true, если элемент был удалён, иначе false
     */
    public boolean removeById(Integer id) {
        return collection.removeIf(lw -> lw.getId().equals(id));
    }

    /** Очищает коллекцию. */
    public void clear() {
        collection.clear();
    }

    /** Сохраняет коллекцию в файл. */
    public void save() {
        fileManager.save(collection); }

    /** Перемешивает элементы коллекции в случайном порядке. */
    public void shuffle() {
        Collections.shuffle(collection);
    }

    /** Сортирует коллекцию в порядке, обратном естественному. */
    public void reorder() {
        Collections.sort(collection, Collections.reverseOrder());
    }

    /**
     * Удаляет все элементы, превышающие заданный.
     * @param o эталонный объект
     */
    public void removeGreater(LabWork o) {
        collection.removeIf(lw -> lw.compareTo(o) > 0);
    }

    /**
     * Удаляет все элементы с указанным значением personalQualitiesMaximum.
     * @param value значение для удаления
     */
    public void removeAllByPersonalQualitiesMaximum(long value) {
        collection.removeIf(lw -> lw.getPersonalQualitiesMaximum() == value);
    }

    /**
     * Возвращает элемент с максимальным именем (лексикографически).
     * @return объект LabWork или null, если коллекция пуста
     */
    public LabWork maxByName() {
        return collection.isEmpty() ? null : Collections.max(collection);
    }

    /**
     * Подсчитывает количество элементов, описание которых больше заданного.
     * @param description строка для сравнения
     * @return количество элементов
     */
    public long countGreaterThanDescription(String description) {
        return collection.stream().filter(lw -> lw.getDescription().compareTo(description) > 0).count();
    }

    /**
     * Генерирует новый уникальный id.
     * @return id = максимальный существующий + 1
     */
    private Integer generateId() {
        return collection.stream().mapToInt(LabWork::getId).max().orElse(0) + 1;
    }

    /**
     * Ищет элемент по id.
     * @param id идентификатор
     * @return найденный объект или null
     */
    public LabWork findById(Integer id) {
        return collection.stream().filter(lw -> lw.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Возвращает коллекцию.
     * @return вектор с элементами
     */
    public Vector<LabWork> getCollection() {
        return collection;
    }

    /**
     * Возвращает дату инициализации.
     * @return дата создания менеджера
     */
    public ZonedDateTime getInitDate() {
        return initDate;
    }
}