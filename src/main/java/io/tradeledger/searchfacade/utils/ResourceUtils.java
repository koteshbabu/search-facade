package io.tradeledger.searchfacade.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tradeledger.searchfacade.model.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ResourceUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();


    public static List<Resource> getResources(MultipartFile multipartFile) throws IOException {

        return objectMapper.readValue(multipartFile.getInputStream(), new TypeReference<List<Resource>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });



    }
}
