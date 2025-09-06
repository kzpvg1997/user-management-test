package co.com.globalogic.usermanagement.infrastructure.repository.helper;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class H2AdapterOperations <E,D,ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<D> dataClass;

    protected Function<E,D> fnToData;

    protected Function<D,E> fnToEntity;

    public H2AdapterOperations(Class<D> dataClass, Function<E,D> fnToData, Function<D,E> fnToEntity) {
        this.dataClass = dataClass;
        this.fnToData = fnToData;
        this.fnToEntity = fnToEntity;
    }

    public E getById(ID id) {
        return toEntity(entityManager.find(dataClass, id));
    }


    public E saveAndFlush(E entity){
        this.entityManager.persist(this.toData(entity));
        this.entityManager.flush();
        return entity;
    }

    public E saveAndFlushData(D data){
        this.entityManager.persist(data);
        this.entityManager.flush();
        return this.toEntity(data);
    }


    public List<E> saveAllAndFlush(List<E> entities) {
        List<D> dataList = entities.stream()
                .map(this::toData)
                .collect(Collectors.toList());

        for (D data : dataList) {
            this.entityManager.persist(data);
        }
        this.entityManager.flush();

        return entities;
    }

    protected E toEntity(D data){
        return fnToEntity.apply(data);
    }

    protected D toData(E entity){
        return fnToData.apply(entity);
    }
}
