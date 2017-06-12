package com.jhipster.health.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.jhipster.health.Application;
import com.jhipster.health.domain.Bloodpressure;
import com.jhipster.health.domain.User;
import com.jhipster.health.repository.BloodpressureRepository;
import com.jhipster.health.repository.UserRepository;
import com.jhipster.health.repository.search.BloodpressureSearchRepository;
import com.jhipster.health.repository.search.UserSearchRepository;
import com.jhipster.health.web.rest.errors.ExceptionTranslator;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
/**
 * Test class for the BloodpressureResource REST controller.
 *
 * @see BloodpressureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BloodpressureResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SYSTOLIC = 1;
    private static final Integer UPDATED_SYSTOLIC = 2;

    private static final Integer DEFAULT_DIASTOLIC = 1;
    private static final Integer UPDATED_DIASTOLIC = 2;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private WebApplicationContext context;

    @Autowired
    private BloodpressureRepository bloodpressureRepository;

    @Autowired
    private BloodpressureSearchRepository bloodpressureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBloodpressureMockMvc;

    private Bloodpressure bloodpressure;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BloodpressureResource bloodpressureResource = new BloodpressureResource(bloodpressureRepository, bloodpressureSearchRepository);
        this.restBloodpressureMockMvc = MockMvcBuilders.standaloneSetup(bloodpressureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bloodpressure createEntity(EntityManager em) {
        Bloodpressure bloodpressure = new Bloodpressure()
            .date(DEFAULT_DATE)
            .systolic(DEFAULT_SYSTOLIC)
            .diastolic(DEFAULT_DIASTOLIC);
        return bloodpressure;
    }

    @Before
    public void initTest() {
        bloodpressureSearchRepository.deleteAll();
        bloodpressure = createEntity(em);
    }
    
    private void createBloodPressureByMonth(LocalDate firstDate, LocalDate firstDayOfLastMonth) {
    	User user = userRepository.findOneByLogin("user").get();
    	bloodpressure = new Bloodpressure(firstDate, 120, 80, user);
    	bloodpressureRepository.saveAndFlush(bloodpressure);
    	bloodpressure = new Bloodpressure(firstDate.plusDays(10), 125, 75, user);
    	bloodpressureRepository.saveAndFlush(bloodpressure);
    	bloodpressure = new Bloodpressure(firstDate.plusDays(20), 100, 69, user);
    	bloodpressureRepository.saveAndFlush(bloodpressure);
    	// last month
    	bloodpressure = new Bloodpressure(firstDayOfLastMonth, 130, 90, user);
    	bloodpressureRepository.saveAndFlush(bloodpressure);
    	bloodpressure = new Bloodpressure(firstDayOfLastMonth.plusDays(11), 135, 85, user);
    	bloodpressureRepository.saveAndFlush(bloodpressure);
    	bloodpressure = new Bloodpressure(firstDayOfLastMonth.plusDays(23), 130, 75, user);
    	bloodpressureRepository.saveAndFlush(bloodpressure);
    	}
   
    @Test
    @Transactional
    public void getBloodPressureForLast30Days() throws Exception {
    LocalDate now = LocalDate.now();
    LocalDate twentyNineDaysAgo = now.minusDays(29);
    LocalDate firstDayOfLastMonth = now.withDayOfMonth(1).minusMonths(1);
    createBloodPressureByMonth(twentyNineDaysAgo, firstDayOfLastMonth);
    // create security-aware mockMvc
    restBloodpressureMockMvc = MockMvcBuilders
    .webAppContextSetup(context)
    .apply(springSecurity())
    .build();
    // Get all the blood pressure readings
    restBloodpressureMockMvc.perform(get("/api/blood-pressures")
    .with(user("user").roles("USER")))
    .andExpect(status().isOk())
    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$", hasSize(6)));
    // Get the blood pressure readings for the last 30 days
    restBloodpressureMockMvc.perform(get("/api/bp-by-days/{days}", 30)
    .with(user("user").roles("USER")))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.period").value("Last 30 Days"))
    .andExpect(jsonPath("$.readings.[*].systolic").value(hasItem(120)))
    .andExpect(jsonPath("$.readings.[*].diastolic").value(hasItem(69)));
    }


    @Test
    @Transactional
    public void createBloodpressure() throws Exception {
        int databaseSizeBeforeCreate = bloodpressureRepository.findAll().size();

        // Create the Bloodpressure
        restBloodpressureMockMvc.perform(post("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isCreated());

        // Validate the Bloodpressure in the database
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeCreate + 1);
        Bloodpressure testBloodpressure = bloodpressureList.get(bloodpressureList.size() - 1);
        assertThat(testBloodpressure.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBloodpressure.getSystolic()).isEqualTo(DEFAULT_SYSTOLIC);
        assertThat(testBloodpressure.getDiastolic()).isEqualTo(DEFAULT_DIASTOLIC);

        // Validate the Bloodpressure in Elasticsearch
        Bloodpressure bloodpressureEs = bloodpressureSearchRepository.findOne(testBloodpressure.getId());
        assertThat(bloodpressureEs).isEqualToComparingFieldByField(testBloodpressure);
    }

    @Test
    @Transactional
    public void createBloodpressureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bloodpressureRepository.findAll().size();

        // Create the Bloodpressure with an existing ID
        bloodpressure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBloodpressureMockMvc.perform(post("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodpressureRepository.findAll().size();
        // set the field null
        bloodpressure.setDate(null);

        // Create the Bloodpressure, which fails.

        restBloodpressureMockMvc.perform(post("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isBadRequest());

        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSystolicIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodpressureRepository.findAll().size();
        // set the field null
        bloodpressure.setSystolic(null);

        // Create the Bloodpressure, which fails.

        restBloodpressureMockMvc.perform(post("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isBadRequest());

        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiastolicIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodpressureRepository.findAll().size();
        // set the field null
        bloodpressure.setDiastolic(null);

        // Create the Bloodpressure, which fails.

        restBloodpressureMockMvc.perform(post("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isBadRequest());

        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBloodpressures() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList
        restBloodpressureMockMvc.perform(get("/api/bloodpressures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodpressure.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));
    }

    @Test
    @Transactional
    public void getBloodpressure() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get the bloodpressure
        restBloodpressureMockMvc.perform(get("/api/bloodpressures/{id}", bloodpressure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bloodpressure.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.systolic").value(DEFAULT_SYSTOLIC))
            .andExpect(jsonPath("$.diastolic").value(DEFAULT_DIASTOLIC));
    }

    @Test
    @Transactional
    public void getNonExistingBloodpressure() throws Exception {
        // Get the bloodpressure
        restBloodpressureMockMvc.perform(get("/api/bloodpressures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBloodpressure() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);
        bloodpressureSearchRepository.save(bloodpressure);
        int databaseSizeBeforeUpdate = bloodpressureRepository.findAll().size();

        // Update the bloodpressure
        Bloodpressure updatedBloodpressure = bloodpressureRepository.findOne(bloodpressure.getId());
        updatedBloodpressure
            .date(UPDATED_DATE)
            .systolic(UPDATED_SYSTOLIC)
            .diastolic(UPDATED_DIASTOLIC);

        restBloodpressureMockMvc.perform(put("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBloodpressure)))
            .andExpect(status().isOk());

        // Validate the Bloodpressure in the database
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeUpdate);
        Bloodpressure testBloodpressure = bloodpressureList.get(bloodpressureList.size() - 1);
        assertThat(testBloodpressure.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBloodpressure.getSystolic()).isEqualTo(UPDATED_SYSTOLIC);
        assertThat(testBloodpressure.getDiastolic()).isEqualTo(UPDATED_DIASTOLIC);

        // Validate the Bloodpressure in Elasticsearch
        Bloodpressure bloodpressureEs = bloodpressureSearchRepository.findOne(testBloodpressure.getId());
        assertThat(bloodpressureEs).isEqualToComparingFieldByField(testBloodpressure);
    }

    @Test
    @Transactional
    public void updateNonExistingBloodpressure() throws Exception {
        int databaseSizeBeforeUpdate = bloodpressureRepository.findAll().size();

        // Create the Bloodpressure

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBloodpressureMockMvc.perform(put("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isCreated());

        // Validate the Bloodpressure in the database
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBloodpressure() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);
        bloodpressureSearchRepository.save(bloodpressure);
        int databaseSizeBeforeDelete = bloodpressureRepository.findAll().size();

        // Get the bloodpressure
        restBloodpressureMockMvc.perform(delete("/api/bloodpressures/{id}", bloodpressure.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bloodpressureExistsInEs = bloodpressureSearchRepository.exists(bloodpressure.getId());
        assertThat(bloodpressureExistsInEs).isFalse();

        // Validate the database is empty
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBloodpressure() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);
        bloodpressureSearchRepository.save(bloodpressure);

        // Search the bloodpressure
        restBloodpressureMockMvc.perform(get("/api/_search/bloodpressures?query=id:" + bloodpressure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodpressure.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bloodpressure.class);
        Bloodpressure bloodpressure1 = new Bloodpressure();
        bloodpressure1.setId(1L);
        Bloodpressure bloodpressure2 = new Bloodpressure();
        bloodpressure2.setId(bloodpressure1.getId());
        assertThat(bloodpressure1).isEqualTo(bloodpressure2);
        bloodpressure2.setId(2L);
        assertThat(bloodpressure1).isNotEqualTo(bloodpressure2);
        bloodpressure1.setId(null);
        assertThat(bloodpressure1).isNotEqualTo(bloodpressure2);
    }
}
