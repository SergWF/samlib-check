package my.wf.samlib.model.entity;


import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity{
    private Long id;


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;

        BaseEntity that = (BaseEntity) o;
        if(id != null){
            return id.equals(that.id);
        }else{
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{" +
                "id=" + id +
                '}';
    }
}
