package com.jhipster.health.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.health.domain.Bloodpressure;
import com.jhipster.health.repository.BloodpressureRepository;
import com.jhipster.health.repository.search.BloodpressureSearchRepository;
import com.jhipster.health.security.SecurityUtils;
import com.jhipster.health.web.rest.util.HeaderUtil;
import com.jhipster.health.web.rest.util.PaginationUtil;
import com.jhipster.health.web.rest.vm.BloodpressureByPeriod;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Bloodpressure.
 */
@RestController
@RequestMapping("/api")
public class BloodpressureResource {

    private final Logger log = LoggerFactory.getLogger(BloodpressureResource.class);

    private static final String ENTITY_NAME = "bloodpressure";
        
    private final BloodpressureRepository bloodpressureRepository;

    private final BloodpressureSearchRepository bloodpressureSearchRepository;

    public BloodpressureResource(BloodpressureRepository bloodpressureRepository, BloodpressureSearchRepository bloodpressureSearchRepository) {
        this.bloodpressureRepository = bloodpressureRepository;
        this.bloodpressureSearchRepository = bloodpressureSearchRepository;
    }

    /**
     * POST  /bloodpressures : Create a new bloodpressure.
     *
     * @param bloodpressure the bloodpressure to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bloodpressure, or with status 400 (Bad Request) if the bloodpressure has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bloodpressures")
    @Timed
    public ResponseEntity<Bloodpressure> createBloodpressure(@Valid @RequestBody Bloodpressure bloodpressure) throws URISyntaxException {
        log.debug("REST request to save Bloodpressure : {}", bloodpressure);
        if (bloodpressure.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bloodpressure cannot already have an ID")).body(null);
        }
        Bloodpressure result = bloodpressureRepository.save(bloodpressure);
        bloodpressureSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bloodpressures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bloodpressures : Updates an existing bloodpressure.
     *
     * @param bloodpressure the bloodpressure to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bloodpressure,
     * or with status 400 (Bad Request) if the bloodpressure is not valid,
     * or with status 500 (Internal Server Error) if the bloodpressure couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bloodpressures")
    @Timed
    public ResponseEntity<Bloodpressure> updateBloodpressure(@Valid @RequestBody Bloodpressure bloodpressure) throws URISyntaxException {
        log.debug("REST request to update Bloodpressure : {}", bloodpressure);
        if (bloodpressure.getId() == null) {
            return createBloodpressure(bloodpressure);
        }
        Bloodpressure result = bloodpressureRepository.save(bloodpressure);
        bloodpressureSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bloodpressure.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bloodpressures : get all the bloodpressures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bloodpressures in body
     */
    @GetMapping("/bloodpressures")
    @Timed
    public ResponseEntity<List<Bloodpressure>> getAllBloodpressures(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Bloodpressures");
        Page<Bloodpressure> page = bloodpressureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bloodpressures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bloodpressures/:id : get the "id" bloodpressure.
     *
     * @param id the id of the bloodpressure to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bloodpressure, or with status 404 (Not Found)
     */
    @GetMapping("/bloodpressures/{id}")
    @Timed
    public ResponseEntity<Bloodpressure> getBloodpressure(@PathVariable Long id) {
        log.debug("REST request to get Bloodpressure : {}", id);
        Bloodpressure bloodpressure = bloodpressureRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bloodpressure));
    }

    /**
     * DELETE  /bloodpressures/:id : delete the "id" bloodpressure.
     *
     * @param id the id of the bloodpressure to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bloodpressures/{id}")
    @Timed
    public ResponseEntity<Void> deleteBloodpressure(@PathVariable Long id) {
        log.debug("REST request to delete Bloodpressure : {}", id);
        bloodpressureRepository.delete(id);
        bloodpressureSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bloodpressures?query=:query : search for the bloodpressure corresponding
     * to the query.
     *
     * @param query the query of the bloodpressure search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bloodpressures")
    @Timed
    public ResponseEntity<List<Bloodpressure>> searchBloodpressures(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Bloodpressures for query {}", query);
        Page<Bloodpressure> page = bloodpressureSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bloodpressures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     *  GET /bp-by-days : get all the blood pressure readings by last x days.
     */
    @RequestMapping(value = "/bp-by-days/{days}")
    @Timed
    public ResponseEntity<BloodpressureByPeriod> getByDays(@PathVariable int days) {
    	LocalDate rightNow = LocalDate.now();
    	LocalDate daysAgo = rightNow.minusDays(days);
    	List<Bloodpressure> readings =bloodpressureRepository.findAllByTimestampBetweenAndUserLoginOrderByTimestampDesc(daysAgo, rightNow, SecurityUtils.getCurrentUserLogin());
    	BloodpressureByPeriod response = new BloodpressureByPeriod("Last " + days + "Days", readings);
    	return new ResponseEntity<>(response, HttpStatus.OK);
    	}
    
   /* private List<Bloodpressure> filterByUser(List<Bloodpressure> readings) {
    	Stream<Bloodpressure> userReadings = readings.stream().filter(bp -> bp.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin()));
    	return userReadings.collect(Collectors.toList());
    }*/
    
    


}
