package my.wf.samlib.service.impl;

import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UtilsServiceImpl implements UtilsService {


    @Autowired
    AuthorService authorService;



    @Override
    public StatisticDto getStatistic() {
        return new StatisticDto(
                authorService.getAllAuthorsCount(),
                authorService.getAllAuthorsCount(),
                authorService.getUnreadAuthors().size(),
                authorService.getLastUpdateDate()
        );
    }

    @Override
    public Integer importAuthors(Collection<String> authorLinks) throws IOException {
        for(String authorLink: authorLinks){
            authorService.addAuthor(authorLink);
        }
        return authorLinks.size();
    }

    @Override
    public Set<String> exportAuthors() {
        return authorService.findAllAuthors().stream()
                .map((a)->a.getLink()).collect(Collectors.toSet());
    }


}
