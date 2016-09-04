package my.wf.samlib.model.entity;


public abstract class BaseEntity{
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(BaseEntity.class.isAssignableFrom(o.getClass()))) return false;

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
