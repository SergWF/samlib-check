package my.wf.samlib.service.impl;

import my.wf.samlib.model.dto.backup.AuthorBackupDto;
import my.wf.samlib.model.dto.backup.BackupDto;
import my.wf.samlib.model.dto.backup.CustomerBackupDto;
import my.wf.samlib.model.dto.builder.BackupDtoBuilder;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.BackupService;
import my.wf.samlib.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BackupServiceImpl implements BackupService {

    @Autowired
    AuthorService authorService;
    @Autowired
    CustomerService customerService;


    @Override
    public BackupDto backup() {
        BackupDto backupDto = new BackupDto();
        backupDto.setCreateDate(new Date());
        backupDto.getAuthors().addAll(getAllAuthors());
        backupDto.getCustomers().addAll(getAllCustomers());
        return backupDto;
    }


    protected Set<CustomerBackupDto> getAllCustomers() {
        return customerService.getAllCustomersWithSubscriptions()
                .stream()
                .map(BackupDtoBuilder::createCustomerBackupDto)
                .collect(Collectors.toSet());
    }


    protected Set<AuthorBackupDto> getAllAuthors() {
        return authorService.findAllAuthorsWithWritings()
                .stream()
                .map(BackupDtoBuilder::createAuthorBackupDto)
                .collect(Collectors.toSet());
    }

}
