package my.wf.samlib.service;

import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.model.entity.Customer;

import java.util.Collection;
import java.util.Set;

public interface UtilsService {

    StatisticDto getStatistic(Customer customer);

    Integer importAuthors(Customer customer, Collection<String> authorLinks);

    Set<String> exportAuthors();

}
