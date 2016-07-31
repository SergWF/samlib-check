package my.wf.samlib.service;

import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.model.statistic.UpdateStatistic;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public interface UtilsService {

    StatisticDto getStatistic();

    UpdateStatistic getUpdatingState();

    Integer importAuthors(Collection<String> authorLinks) throws IOException;

    Set<String> exportAuthors();

}
