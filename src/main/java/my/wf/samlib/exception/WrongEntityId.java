package my.wf.samlib.exception;

public class WrongEntityId extends SamlibException {

    private final Class<?> entityClass;
    private final long entityId;

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public long getEntityId() {
        return entityId;
    }

    public WrongEntityId(Class<?> entityClass, long entityId) {
        super("Wrong entity id " + entityId + " for class " + entityClass);
        this.entityClass = entityClass;
        this.entityId = entityId;
    }
}
