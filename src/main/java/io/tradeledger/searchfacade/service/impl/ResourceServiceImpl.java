package io.tradeledger.searchfacade.service.impl;

import io.tradeledger.searchfacade.exception.InternalServerException;
import io.tradeledger.searchfacade.model.Filter;
import io.tradeledger.searchfacade.model.Resource;
import io.tradeledger.searchfacade.repository.ResourceRepository;
import io.tradeledger.searchfacade.service.ResourceService;
import io.tradeledger.searchfacade.utils.QueryUtils;
import io.tradeledger.searchfacade.utils.ResourceUtils;
import io.tradeledger.searchfacade.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
   public Resource getResourceById(String  id){
        return resourceRepository.findById(id).orElse(null);

   }

    @Override
    public List<Resource> findAllResources() {
        return resourceRepository.findAll();
    }

    @Override
    public List<Resource> saveResources(Iterable<Resource> resources) {
        return resourceRepository.saveAll(resources);
    }

    @Override
    public void deleteResources() {
        resourceRepository.deleteAll();

    }

    @Override
    public List<Resource> searchResourceByGivenCriteria(String correlationId ,Filter... filters) {
        if (filters != null && filters.length > 0){
            Validator.validate(correlationId, filters);
            Query query = QueryUtils.formQueryFromFilters(filters);
            return mongoTemplate.find(query, Resource.class);
        }
        return findAllResources();
    }

    @Override
    public List<Resource> createResource(MultipartFile resource) {
        List<Resource> allResources = new ArrayList<>();
        try {
            allResources.addAll(ResourceUtils.getResources(resource));
        } catch (IOException e) {
            throw  new InternalServerException(e.getCause());
        }


        if (! allResources.isEmpty()){
            return saveResources(allResources);
        }
        return  allResources;
    }

    @Override
    public void deleteResourceById(String id) {
        resourceRepository.deleteById(id);
    }
}
