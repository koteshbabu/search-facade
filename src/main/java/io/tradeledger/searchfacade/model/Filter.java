package io.tradeledger.searchfacade.model;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "This is the filter to search the data")
public class Filter {

    private String attribute;
    private String operator;
    private String value;
    private Range range;

    public Filter(){

    }
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("attribute=").append(this.attribute)
                     .append("operator=").append(this.operator)
                     .append("value=").append(value)
                     .append("from=").append(this.range != null ? this.range.getFrom(): null)
                     .append("to=").append(this.range !=null ?  this.range.getTo(): null)
                     .toString();

    }
}



