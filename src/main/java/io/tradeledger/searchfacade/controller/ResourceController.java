package io.tradeledger.searchfacade.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.tradeledger.searchfacade.exception.ResourceNotFoundException;
import io.tradeledger.searchfacade.model.Filter;
import io.tradeledger.searchfacade.model.Resource;
import io.tradeledger.searchfacade.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.tradeledger.searchfacade.constants.Constants.*;

@RestController
@CrossOrigin
@RequestMapping("/resource")
public class ResourceController {

    private final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceService resourceService;


    @GetMapping("")
    @ApiOperation(value = "This end-point gives all the available resources in the system")
    public List<Resource> getAllResources(@RequestHeader(name = "correlationId", required = false) String correlationId) {

        correlationId = Optional.ofNullable(correlationId).orElse(UUID.randomUUID().toString());

        long startTime = System.currentTimeMillis();

        LOGGER.info("loggingType=\"AuditLogging\" |  correlationId= {} | methodName=\"getAllResources\" | class={} | status={}", correlationId, this.getClass().getName(), INITIATED);

        List<Resource> resources = resourceService.findAllResources();

        if (resources == null || resources.isEmpty()) {
            String errorMessage = "There are no resources existing currently in the system";
            LOGGER.error("loggingType=\"ErrorLogging\" |  correlationId= {} | methodName=\"getAllResources\" | class={} | status={} | errorMessage={}", correlationId, this.getClass().getName(), FAIL, errorMessage);

            throw new ResourceNotFoundException(errorMessage);
        }

        LOGGER.info("loggingType=\"AuditLogging\" | correlationId= {} | methodName=\"getAllResources\" | class={} | status={} | duration={}", correlationId, this.getClass().getName(), SUCCESS, System.currentTimeMillis() - startTime);

        return resources;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "This end-point gives the resource matching to the given id", response = Resource.class)
    public Resource getResourceById(@PathVariable("id") String id,
                                    @RequestHeader(name = "correlationId", required = false) String correlationId) {

        long startTime = System.currentTimeMillis();

        correlationId = Optional.ofNullable(correlationId).orElse(UUID.randomUUID().toString());

        LOGGER.info("loggingType=\"AuditLogging\" |  correlationId= {} | id={} |methodName=\"getResourceById\" | class={} | status={}", correlationId, id, this.getClass().getName(), INITIATED);

        Resource resource = resourceService.getResourceById(id);

        if (resource == null) {
            String errorMessage = "There is no resource with the id=" + id;
            LOGGER.error("loggingType=\"ErrorLogging\" |  correlationId= {} | id={} |methodName=\"getResourceById\" | class={} | status={}", correlationId, id, this.getClass().getName(), FAIL);
            throw new ResourceNotFoundException(errorMessage);
        }
        LOGGER.info("loggingType=\"AuditLogging\" | correlationId= {} | id={} |methodName=\"getResourceById\" | class={} | status={} | duration={}", correlationId, id, this.getClass().getName(), SUCCESS, System.currentTimeMillis() - startTime);

        return resource;
    }

    @PostMapping("/search")
    @ApiOperation(value = "This end-point gives the results matching to the filter(s) if any")
    public List<Resource> search(@RequestHeader(name = "correlationId", required = false) String correlationId,
                                 @RequestBody final Filter... filters) {

        long startTime = System.currentTimeMillis();
        correlationId = Optional.ofNullable(correlationId).orElse(UUID.randomUUID().toString());

        LOGGER.info("loggingType=\"AuditLogging\" |  correlationId= {}  |methodName=\"search\" | class={} | status={}", correlationId, this.getClass().getName(), INITIATED);

        List<Resource> resources = resourceService.searchResourceByGivenCriteria(correlationId, filters);

        if (resources == null || resources.isEmpty()) {
            String errorMessage = "There are no resources matched to the given filter criteria";
            LOGGER.error("loggingType=\"ErrorLogging\" |  correlationId= {}  |methodName=\"search\" | class={} | status={} | errorMessage={}", correlationId, this.getClass().getName(), FAIL, errorMessage);

            throw new ResourceNotFoundException(errorMessage);
        }

        LOGGER.info("loggingType=\"AuditLogging\" |  correlationId= {}  |methodName=\"search\" | class={} | status={} | duration={}", correlationId, this.getClass().getName(), SUCCESS, System.currentTimeMillis() - startTime);

        return resources;
    }


    @ApiOperation(value = "This end-point would upload the selected json data, converts them in to resources and upload them")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Resource> createResource(@RequestHeader(name = "correlationId", required = false) String correlationId,
                                         @RequestPart("file") MultipartFile file) {

        long startTime = System.currentTimeMillis();
        correlationId = Optional.ofNullable(correlationId).orElse(UUID.randomUUID().toString());

        LOGGER.info("loggingType=\"AuditLogging\" |  correlationId= {}  |methodName=\"createResource\" | class={} | status={}", correlationId, this.getClass().getName(), INITIATED);

        List<Resource> resources = resourceService.createResource(file);
        if (resources == null || resources.isEmpty()) {
            String errorMessage = "Given file is empty. Please attach valid file which is not empty";
            LOGGER.error("loggingType=\"ErrorLogging\" |  correlationId= {}  |methodName=\"createResource\" | class={} | status={} | errorMessage = {}", correlationId, this.getClass().getName(), INITIATED, errorMessage);

            throw new ResourceNotFoundException(errorMessage);
        }

        LOGGER.info("loggingType=\"AuditLogging\" |  correlationId= {}  |methodName=\"createResource\" | class={} | status={} | duration= {}", correlationId, this.getClass().getName(), SUCCESS, System.currentTimeMillis() - startTime);


        return resources;
    }


    @DeleteMapping("")
    @ApiOperation(value = "This end-point would delete all the resources in the system")
    public void removeAll() {
        resourceService.deleteResources();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "This end-point would delete the resource matching to the id")
    public void removeById(String id) {
        resourceService.deleteResourceById(id);
    }


}
