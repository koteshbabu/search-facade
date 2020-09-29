package io.tradeledger.searchfacade.utils;

import io.tradeledger.searchfacade.model.Filter;
import io.tradeledger.searchfacade.model.Range;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;

import static io.tradeledger.searchfacade.constants.Constants.*;
import static io.tradeledger.searchfacade.constants.Constants.ID;

public class QueryUtils {


    public static Query formQueryFromFilters(Filter... filters){

        Query query = new Query();

        Arrays.stream(filters).forEach(filter ->  {

            Criteria criteria = formQuery(filter);

            if (criteria!=null){
                query.addCriteria(criteria);
            }

        });
        return query;
    }

    private static Criteria formQuery(Filter filter){


        if (filter.getAttribute().equals(ID)){
            return formQueryFromId(filter);
        }

        if (filter.getAttribute().equals(NAME)){
            return fromQueryFromName(filter);
        }

        if (filter.getAttribute().equals(IP)){
            return fromQueryFromIp(filter);
        }

        if (filter.getAttribute().equals(EMAIL)){
            return fromQueryFromEmail(filter);
        }

        if (filter.getAttribute().equals(TIME)){
            return formQueryFromTime(filter);
        }

        if (filter.getAttribute().equals(TYPE)){
            return formQueryFromType(filter);
        }
        return null;

    }

    private static Criteria fromQueryFromName(Filter filter){
        return Criteria.where(NAME).is(filter.getValue());

    }

    private static Criteria fromQueryFromIp(Filter filter){
        return Criteria.where(IP).is(filter.getValue());

    }

    private static Criteria fromQueryFromEmail(Filter filter){
        return Criteria.where(EMAIL).is(filter.getValue());

    }

    private static Criteria formQueryFromTime(Filter filter){

        if (filter.getRange()!= null){
            Range range = filter.getRange();
            return Criteria.where(TIME).gte(new Long(range.getFrom())).lte(new Long(range.getTo()).longValue());
        }else {
            return Criteria.where(TIME).is(new Long(filter.getValue()).longValue());
        }
    }

    private static Criteria formQueryFromType(Filter filter){

        return Criteria.where(TYPE).is(filter.getValue());
    }

    private static Criteria formQueryFromId(Filter filter){

        return Criteria.where(ID).is(filter.getValue());

    }
}
