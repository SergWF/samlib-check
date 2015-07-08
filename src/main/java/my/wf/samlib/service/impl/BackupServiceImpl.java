package my.wf.samlib.service.impl;

import my.wf.samlib.model.dto.backup.AuthorBackupDto;
import my.wf.samlib.model.dto.backup.BackupDto;
import my.wf.samlib.model.dto.backup.CustomerBackupDto;
import my.wf.samlib.model.dto.backup.WritingBackupDto;
import my.wf.samlib.model.dto.builder.BackupDtoBuilder;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.BackupService;
import my.wf.samlib.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BackupServiceImpl implements BackupService {
    private static final Logger logger = LoggerFactory.getLogger(BackupServiceImpl.class);

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.S");

    @Autowired
    AuthorService authorService;
    @Autowired
    CustomerService customerService;


    @Override
    public String backup() {
        String label = createLabel();
        BackupDto backupDto = new BackupDto();
        backupDto.setCreateDate(new Date());
        backupDto.getAuthors().addAll(getAllAuthors());
        backupDto.getCustomers().addAll(getAllCustomers());
        saveToDisk(backupDto, label);
        return label;
    }

    private String createLabel() {
        return SIMPLE_DATE_FORMAT.format(new Date());
    }

    @Override
    public void restore(String backupLabel) {
        BackupDto backupDto = loadFromDisk(backupLabel);
        restoreAuthors(backupDto);
        restoreCustomers(backupDto);
    }

    @Override
    public void restore() {
        String lastLabel = findLastLabel();
        restore(lastLabel);
    }


    protected void restoreCustomers(BackupDto backupDto) {
        throw new NotImplementedException();
    }

    protected void restoreAuthors(BackupDto backupDto) {
        logger.info("restore authors");
        backupDto.getAuthors().stream().map(this::restoreSingleAuthor);
        logger.debug("authors restored");
    }

    protected Author restoreSingleAuthor(AuthorBackupDto authorBackupDto){
        logger.debug("restore author {} ({})", authorBackupDto.getName(), authorBackupDto.getLink());
        Author author = authorService.addAuthor(authorBackupDto.getLink());
        author = authorService.findAuthorWithWritings(author.getId());
        author.setName(authorBackupDto.getName());
        for(WritingBackupDto writingBackupDto: authorBackupDto.getWritings()){
            restoreSingleWriting(author, writingBackupDto);
        }
        return authorService.saveAuthor(author);
    }

    protected Writing restoreSingleWriting(Author author, WritingBackupDto writingBackupDto){
        throw new NotImplementedException();
    }

    protected void saveToDisk(BackupDto backupDto, String label) {
        throw new NotImplementedException();
    }

    protected BackupDto loadFromDisk(String backupLabel) {
        throw new NotImplementedException();
    }

    protected String findLastLabel() {
        throw new NotImplementedException();
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
