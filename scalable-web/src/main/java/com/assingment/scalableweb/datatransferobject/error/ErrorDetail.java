package com.assingment.scalableweb.datatransferobject.error;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Generic response for error cases
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */
public class ErrorDetail {

    private String title;
    private int status;
    private String details;
    private long timeStamp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
       return ToStringBuilder.reflectionToString(this);
    }
}
