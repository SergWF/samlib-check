package my.wf.samlib.model.dto.builder;

import my.wf.samlib.model.dto.WritingDto;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.model.entity.SubscriptionUnread;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.tools.LinkTool;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WritingDtoBuilder {

    public static WritingDto buildDto(Writing writing, Subscription subscription){
        WritingDto writingDto = new WritingDto();
        writingDto.setWritingId(writing.getId());
        writingDto.setName(writing.getName());
        writingDto.setLink(writing.getLink());
        writingDto.setDescription(writing.getDescription());
        writingDto.setGroupName(writing.getGroupName());
        writingDto.setLastChangedDate(writing.getLastChangedDate());
        writingDto.setSize(writing.getSize());
        writingDto.setPrevSize(writing.getPrevSize());
        writingDto.setExternalUrl(LinkTool.getFullWritingLink(writing));
        for(SubscriptionUnread subscriptionUnread: subscription.getSubscriptionUnreads()){
            if(subscriptionUnread.getWriting().equals(writing)){
                writingDto.setUnread(true);
            }
        }
        return writingDto;
    }

    public static Set<WritingDto> buildDtoSet(Collection<Writing> writings, Subscription subscription){
        Set<WritingDto> dtos = new HashSet<>(writings.size());
        for(Writing writing: writings){
            dtos.add(buildDto(writing, subscription));
        }
        return dtos;
    }
}
