package my.wf.samlib.helpers;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class InitHelper {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CustomerRepository customerRepository;


    @Transactional
    public Map<Integer, Author>  initAuthors(int size){
        Map<Integer, Author> authors = new HashMap<>(size);
        for(int i = 0; i < size; i++){
            Author author = EntityCreator.createAuthor("http://" + i, "name_" + i);
            author.getWritings().add(EntityCreator.createWriting("w1_a" + i, author));
            author.getWritings().add(EntityCreator.createWriting("w2_a" + i, author));
            authors.put(i, authorRepository.save(author));
        }
        return authors;
    }

    @Transactional
    public Customer initDefaultCustomer(Author... authors){
        Customer defaultCustomer = customerRepository.findOne(1L);
        if(null == defaultCustomer){
            defaultCustomer = customerRepository.save(EntityCreator.createDefaultCustomer());
        }
        defaultCustomer.getAuthors().addAll(Arrays.asList(authors));
        return customerRepository.save(defaultCustomer);
    }

}
