package io.tradeledger.searchfacade.controller;

import io.tradeledger.searchfacade.model.Filter;
import io.tradeledger.searchfacade.model.Range;
import io.tradeledger.searchfacade.model.Resource;
import io.tradeledger.searchfacade.repository.ResourceRepository;
import io.tradeledger.searchfacade.service.ResourceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ResourceService resourceService;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    private void setUp(){

        ReflectionTestUtils.setField(resourceService, "resourceRepository", resourceRepository);
        ReflectionTestUtils.setField(resourceService, "mongoTemplate", mongoTemplate);
    }


    @Test
    public void shouldSuccessfullyTestGetResourceById() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/resource/1";

        HttpHeaders headers = new HttpHeaders();


        // Notice here I am saying return a resource instance for any id
        when(resourceRepository.findById(anyString())).thenReturn(Optional.ofNullable(new Resource()));

        ResponseEntity<Resource> result = this.testRestTemplate.getForEntity(baseUrl, Resource.class);

        //Verify request succeed
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void shouldNotGetAnyResourceWhenThereIsNoResource() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/resource/1";

        HttpHeaders headers = new HttpHeaders();

        // Notice here I am saying no resource available for given id
        when(resourceRepository.findById(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Resource> result = this.testRestTemplate.getForEntity(baseUrl, Resource.class);

        // So, I am expecting status as 404 (Resource Not Found)
        Assertions.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void shouldSuccessfullyTestForResourceWithTheGivenFilterCriteria()
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/resource/search";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Filter []> httpEntity = new HttpEntity<Filter []>(new Filter[]{getFilter()}, headers);

        // Notice here I am saying return 1 resource
        when(mongoTemplate.find(any(Query.class), any())).thenReturn(Arrays.asList(getResource()));

        ResponseEntity<List> result = this.testRestTemplate.postForEntity(baseUrl,  httpEntity, List.class);

        // So, here I am expecting status as 200
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenBothRangeAndValueIsPresent()
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/resource/search";

        HttpHeaders headers = new HttpHeaders();

        // getFilter() contains value
        Filter filter = getFilter();

        // notice here I am setting range too
        Range range = new Range();
        range.setFrom("1");
        range.setTo("2");
        filter.setRange(range);

        HttpEntity<Filter []> httpEntity = new HttpEntity<Filter []>(new Filter[]{filter}, headers);

        ResponseEntity<Object> result = this.testRestTemplate.postForEntity(baseUrl,  httpEntity, Object.class);

        // So, here I am expecting status as 400
        Assertions.assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenBothRangeAndValueAreNotPresentPresent()
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/resource/search";

        HttpHeaders headers = new HttpHeaders();

        // getFilter() contains value
        Filter filter = getFilter();
        filter.setValue(null);

        HttpEntity<Filter []> httpEntity = new HttpEntity<Filter []>(new Filter[]{filter}, headers);
        ResponseEntity<Object> result = this.testRestTemplate.postForEntity(baseUrl,  httpEntity, Object.class);

        // So, here I am expecting status as 400
        Assertions.assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenInvalidOperatorIsPresent()
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/resource/search";

        HttpHeaders headers = new HttpHeaders();

        // getFilter() contains value
        Filter filter = getFilter();

        // Notice here I am setting invalid operator
        filter.setOperator("Invalid Operator");

        HttpEntity<Filter []> httpEntity = new HttpEntity<Filter []>(new Filter[]{filter}, headers);
        ResponseEntity<Object> result = this.testRestTemplate.postForEntity(baseUrl,  httpEntity, Object.class);

        // So, here I am expecting status as 400
        Assertions.assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    public void shouldFailToGetAnyResourceWhenNoResourceExistsForTheGivenResource() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/resource/search";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Filter []> httpEntity = new HttpEntity<Filter []>(new Filter[]{getFilter()}, headers);

        // Notice here I am saying no resource exists for the given filter
        when(mongoTemplate.find(any(Query.class), any())).thenReturn(null);

        ResponseEntity<Object> result = this.testRestTemplate.postForEntity(baseUrl,  httpEntity, Object.class);

        // So, here I am expecting status as 404
        Assertions.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void shouldSuccessfullyCreateAResource() throws URISyntaxException, IOException {
        final String baseUrl = "http://localhost:"+randomServerPort+"/resource";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ClassPathResource classPathResource = new ClassPathResource(Paths.get("static", "users.json").toString());


        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", classPathResource);

        HttpEntity< MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parameters, headers);

        // So, here I am saying return 1 resource
        when(resourceRepository.saveAll(anyIterable())).thenReturn(Arrays.asList(getResource()));

        ResponseEntity<List> result = this.testRestTemplate.postForEntity(baseUrl,  httpEntity, List.class);

        // So, here I am expecting status as 200
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void shouldFailToCreateAnyResourceWhenEmptyFileIsSupplied() throws URISyntaxException, IOException {
        final String baseUrl = "http://localhost:"+randomServerPort+"/resource";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ClassPathResource classPathResource = new ClassPathResource(Paths.get("static", "empty.json").toString());

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", classPathResource);

        HttpEntity< MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parameters, headers);

        // So, here I am saying no resource is available by returning null
        when(resourceRepository.saveAll(anyIterable())).thenReturn(null);

        ResponseEntity<Object> result = this.testRestTemplate.postForEntity(baseUrl,  httpEntity, Object.class);

        // So, here I am expecting status as 404
        Assertions.assertEquals(404, result.getStatusCodeValue());
    }




    private Filter getFilter(){
        Filter filter = new Filter();
        filter.setAttribute("id");
        filter.setOperator("eq");
        filter.setValue("1");
        return filter;
    }

    private Resource getResource(){
        Resource resource = new Resource();
        resource.setId("1");
        resource.setEmail("staffskoti@gmail.com");
        resource.setIp("192.168.0.1");
        resource.setName("koti");
        resource.setTime(12935329529l);
        resource.setType("LOGIN");
        return resource;
    }
}
