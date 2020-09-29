package io.tradeledger.searchfacade.validation;

import io.tradeledger.searchfacade.exception.BadRequestException;
import io.tradeledger.searchfacade.model.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static io.tradeledger.searchfacade.constants.Constants.*;

public class Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    public static void   validate(String correlationId, Filter ...filters){
        Arrays.stream(filters).forEach(filter -> validateFilter(filter, correlationId));
    }

    private static void validateFilter(Filter filter, String correlationId){

        String errorMessage = null;

        if (filter.getRange() == null && filter.getValue() == null){
            errorMessage = "Both value and range could be be null. 1 of them should be not null";
            LOGGER.error("loggingType=\"ErrorLogging\" |  correlationId= {} | methodName=\"validateFilter\" | class={} | status={} | errorMessage={}", correlationId, Validator.class.getName(), FAIL, errorMessage);
            throw new BadRequestException(errorMessage);
        }

        if (filter.getRange() != null && filter.getValue() != null){

            errorMessage = "Either value or range should be there at a time. Not both";
            LOGGER.error("loggingType=\"ErrorLogging\" |  correlationId= {} | methodName=\"validateFilter\" | class={} | status={} | errorMessage={}", correlationId, Validator.class.getName(), FAIL, errorMessage);
            throw new BadRequestException(errorMessage);
        }

        if (! EQUALS_OPERATOR.equals(filter.getOperator()) &&
            ! GREATER_THAN_OR_EQUAL_TO_OPERATOR.equals(filter.getOperator()) &&
            ! LESS_THAN_OR_EQUAL_TO_OPERATOR.equals(filter.getOperator())){

            errorMessage = "Invalid operator selected. Operator should be 1 of 'eq', 'gte', 'lte'";
            LOGGER.error("loggingType=\"ErrorLogging\" |  correlationId= {} | methodName=\"validateFilter\" | class={} | status={} | errorMessage={}", correlationId, Validator.class.getName(), FAIL, errorMessage);
            throw new BadRequestException(errorMessage);
        }

        if (GREATER_THAN_OR_EQUAL_TO_OPERATOR.equals(filter.getOperator()) ||
                LESS_THAN_OR_EQUAL_TO_OPERATOR.equals(filter.getOperator())){
            if (filter.getRange() != null){
                errorMessage = "When operator is either 'gte', or 'lte', then range is not allowed";
                LOGGER.error("loggingType=\"ErrorLogging\" |  correlationId= {} | methodName=\"validateFilter\" | class={} | status={} | errorMessage={}", correlationId, Validator.class.getName(), FAIL, errorMessage);
                throw new BadRequestException(errorMessage);

            }
        }

    }
}
