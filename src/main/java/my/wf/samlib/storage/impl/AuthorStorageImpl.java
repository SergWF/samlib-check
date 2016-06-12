package my.wf.samlib.storage.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.storage.AuthorStorage;
import my.wf.samlib.storage.DataContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class AuthorStorageImpl implements AuthorStorage {

    private ConcurrentHashMap<Long, Author> authors = new ConcurrentHashMap<>();
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${samlib.check.storage.file}")
    private String storageFileName;
    @Value("${samlib.check.flush.delay.time.sec}")
    private int flushDelayTime;
    private LocalDateTime lastFlushTime;
    private LocalDateTime lastUpdateDate;

    protected ConcurrentHashMap<Long, Author> getAuthors() {
        return authors;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public void setStorageFileName(String storageFileName) {
        this.storageFileName = storageFileName;
    }


    @Override
    public Author addAuthor(Author author) throws IOException {
        Author byLink = findByLink(author.getLink());
        if(null != byLink) {
            return byLink;
        }
        author.setId(null);
        return save(author);
    }

    @Override
    public synchronized Author save(Author author) throws IOException {
        if(null == author.getId()) {
            author.setId(getNewId());
        }
        authors.put(author.getId(), author);
        flush();
        return author;
    }

    @Override
    public void delete(long authorId) throws IOException {
        if(authors.containsKey(authorId)) {
            authors.remove(authorId);
            flush();
        }
    }


    @Override
    public synchronized void delete(Author author) throws IOException {
        delete(author.getId());
    }

    @Override
    public Author findByLink(String authorUrl) {
        return authors.values()
                .stream()
                .filter((a) -> a.getLink()
                        .equals(authorUrl))
                .findFirst().orElse(null);
    }

    @Override
    public Author getById(long id) {
        return authors.get(id);
    }

    @Override
    public Set<Author> getAll() {
        return new HashSet<>(authors.values());
    }

    @Override
    public Writing findWritingByLink(String authorLink, String writingLink) {
        Author author = findByLink(authorLink);
        return null == author ?  null : author.getWritings()
                .stream()
                .filter((w) -> w.getLink().endsWith(writingLink))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<Author> getUpdatedAfter(LocalDateTime date) {
        return authors.values()
                .stream()
                .filter((a) -> !date.isAfter(a.getLastChangedDate()))
                .collect(Collectors.toSet());
    }

    @Override
    public long getCount() {
        return authors.size();
    }

    private Long getNewId() {
        Long currentMax = authors.isEmpty() ? 0L : Collections.max(Collections.list(authors.keys()));
        return 1 + currentMax;
    }

    protected void flush() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(lastFlushTime.plusSeconds(flushDelayTime))){
            saveToPhysicalStorage();
            lastFlushTime = now;
        }
    }

    protected void saveToPhysicalStorage() throws IOException {
        if(StringUtils.isEmpty(storageFileName)){
            throw new IllegalStateException("StorageFileName can not be empty");
        }
        DataContainer data = new DataContainer();
        data.setLastUpdateDate(lastUpdateDate);
        data.setAuthors(authors.values());
        objectMapper.writeValue(new File(storageFileName), data);
    }

    @Override
    public void loadFromPhysicalStorage() throws IOException {
        if(StringUtils.isEmpty(storageFileName)){
            throw new IllegalStateException("StorageFileName can not be empty");
        }
        File storageFile = new File(storageFileName);
        if(!storageFile.exists()) {
            saveToPhysicalStorage();
        }
        DataContainer data = objectMapper.readValue(storageFile, DataContainer.class);
        lastUpdateDate = data.getLastUpdateDate();
        data.getAuthors()
                    .stream()
                    .forEach((a) -> authors.putIfAbsent(a.getId(), a));
    }

    @Override
    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
