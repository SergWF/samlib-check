package my.wf.samlib.service;

import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Writing;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CustomerService {

    Customer getCustomer(long customerId);

    Customer addAuthor(Customer customer, Author author);

    Customer removeAuthor(Customer customer, Author author);

    Customer markWritingAsUnread(Customer customer, Writing writing);

    List<Author> getAuthorsList(Customer customer);

    Set<Author> getUnreadAuthors(Customer activeCustomer);

    StatisticDto getStatistic(Customer customer);

    void updateUnreadWritings(Date lastCheckDate);

}
