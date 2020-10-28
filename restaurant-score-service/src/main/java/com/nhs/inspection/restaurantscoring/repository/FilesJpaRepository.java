package com.nhs.inspection.restaurantscoring.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class FilesJpaRepository {

    private Logger logger = LoggerFactory.getLogger(FilesJpaRepository.class);

    @PersistenceContext
    EntityManager em;
}
