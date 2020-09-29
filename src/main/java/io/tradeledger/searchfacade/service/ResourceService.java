package io.tradeledger.searchfacade.service;

import io.tradeledger.searchfacade.model.Filter;
import io.tradeledger.searchfacade.model.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ResourceService {

     Resource getResourceById(String  id);

     List<Resource> findAllResources();

    List<Resource> saveResources(Iterable<Resource> resources);

    void deleteResources();
    List<Resource> searchResourceByGivenCriteria(String correlationId ,Filter... filters);
    List<Resource> createResource(MultipartFile resources);
    void deleteResourceById(String id);
}
