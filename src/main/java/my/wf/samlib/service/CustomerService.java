package my.wf.samlib.service;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Writing;

import java.util.List;

public interface CustomerService {

    Customer getCustomer(long customerId);

    Customer addAuthor(Customer customer, Author author);

    Customer removeAuthor(Customer customer, Author author);

    Customer markWritingAsUnread(Customer customer, Writing writing);

    List<Author> getAuthorsList(Customer customer);

    List<Author>  getUnreadAuthors(Customer activeCustomer);
}
