package my.wf.samlib.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.wf.samlib.storage.DataContainer;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class BeanOverridingTestConfig {

    @Bean
    public ObjectMapper objectMapper() throws IOException {
        DataContainer dataContainer = new DataContainer();
        ObjectMapper mapper =  Mockito.mock(ObjectMapper.class);
        Mockito.doReturn(dataContainer).when(mapper).readValue(Mockito.any(File.class), Mockito.eq(DataContainer.class));
        return mapper;
    }
}
