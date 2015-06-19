package my.wf.samlib.helpers;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class InitHelper {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CustomerRepository customerRepository;


    @Transactional
    public Author initSingleAuthor(int writingsCount){
        String name = UUID.randomUUID().toString();
        Author author = EntityHelper.createAuthor("http://" + name, name);
        for(int i = 0; i < writingsCount; i++){
            author.getWritings().add(EntityHelper.createWriting("w_" + i + "_" + name, author));
        }
        return authorRepository.save(author);
    }

    @Transactional
    public Map<Integer, Author> initAuthors(int size){
        Map<Integer, Author> authors = new HashMap<>(size);
        for(int i = 0; i < size; i++){
            Author author = initSingleAuthor(2);
            authors.put(i, authorRepository.save(author));
        }
        return authors;
    }

    @Transactional
    public Customer initCustomer(Author... authors){
        return customerRepository.save(EntityHelper.createCustomer(UUID.randomUUID().toString(), authors));
    }

}
