package my.wf.samlib.model.entity;

public enum Changed {
    NEW("NW"), NAME("NM"), DESCRIPTION("DS"),SIZE("SZ");

    private String label;

    Changed(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }


}
