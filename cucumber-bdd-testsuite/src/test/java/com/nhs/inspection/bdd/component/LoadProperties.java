package com.nhs.inspection.bdd.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoadProperties {

    private String posturl;

    @Autowired
    public LoadProperties(@Value("${post.url}") String posturl) {
        this.posturl = posturl;
    }

    public String getPosturl() {
        return posturl;
    }

    public void setPosturl(String posturl) {
        this.posturl = posturl;
    }
}
