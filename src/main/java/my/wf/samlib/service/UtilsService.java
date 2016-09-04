package my.wf.samlib.service;

import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.model.dto.UpdatingInfo;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public interface UtilsService {

    StatisticDto getStatistic();

    UpdatingInfo getUpdatingState();

    Integer importAuthors(Collection<String> authorLinks) throws IOException;

    Set<String> exportAuthors();

}
