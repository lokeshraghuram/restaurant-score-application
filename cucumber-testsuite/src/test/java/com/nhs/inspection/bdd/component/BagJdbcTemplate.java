package com.nhs.inspection.bdd.component;

import com.nhs.inspection.bdd.helper.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BagJdbcTemplate {
    private TestHelper testHelper = new TestHelper();

    @Autowired
    private LoadProperties loadProperties;


}
