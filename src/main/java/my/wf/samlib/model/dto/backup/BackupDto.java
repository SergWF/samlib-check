package my.wf.samlib.model.dto.backup;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BackupDto {
    private Date createDate;

    private Set<AuthorBackupDto> authors = new HashSet<>();
    private Set<CustomerBackupDto> customers = new HashSet<>();

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Set<AuthorBackupDto> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorBackupDto> authors) {
        this.authors = authors;
    }

    public Set<CustomerBackupDto> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<CustomerBackupDto> customers) {
        this.customers = customers;
    }
}
