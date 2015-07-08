package my.wf.samlib.model.dto.backup;

import java.util.HashSet;
import java.util.Set;

public class AuthorBackupDto {
    private String link;
    private String name;
    private Set<WritingBackupDto> writings = new HashSet<>();

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<WritingBackupDto> getWritings() {
        return writings;
    }

    public void setWritings(Set<WritingBackupDto> writings) {
        this.writings = writings;
    }
}
