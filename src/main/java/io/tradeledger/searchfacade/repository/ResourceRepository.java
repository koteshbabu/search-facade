package io.tradeledger.searchfacade.repository;

import io.tradeledger.searchfacade.model.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository  extends MongoRepository<Resource, String> {
}
