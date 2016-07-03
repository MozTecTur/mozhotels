package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.Tourist;
import com.mozhotels.srit.repository.TouristRepository;
import com.mozhotels.srit.repository.search.TouristSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mozhotels.srit.domain.enumeration.ACountry;
import com.mozhotels.srit.domain.enumeration.ACountry;
import com.mozhotels.srit.domain.enumeration.HLanguage;
import com.mozhotels.srit.domain.enumeration.ECurrency;

/**
 * Test class for the TouristResource REST controller.
 *
 * @see TouristResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class TouristResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    private static final ACountry DEFAULT_COUNTRY_RESIDENCE = ACountry.MOZAMBIQUE;
    private static final ACountry UPDATED_COUNTRY_RESIDENCE = ACountry.MOZAMBIQUE;

    private static final ACountry DEFAULT_COUNTRY_BOOKING = ACountry.MOZAMBIQUE;
    private static final ACountry UPDATED_COUNTRY_BOOKING = ACountry.MOZAMBIQUE;

    private static final HLanguage DEFAULT_LANGUAGE = HLanguage.EN;
    private static final HLanguage UPDATED_LANGUAGE = HLanguage.PT;

    private static final ECurrency DEFAULT_CURRENCY = ECurrency.MZN;
    private static final ECurrency UPDATED_CURRENCY = ECurrency.USD;

    @Inject
    private TouristRepository touristRepository;

    @Inject
    private TouristSearchRepository touristSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTouristMockMvc;

    private Tourist tourist;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TouristResource touristResource = new TouristResource();
        ReflectionTestUtils.setField(touristResource, "touristSearchRepository", touristSearchRepository);
        ReflectionTestUtils.setField(touristResource, "touristRepository", touristRepository);
        this.restTouristMockMvc = MockMvcBuilders.standaloneSetup(touristResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        touristSearchRepository.deleteAll();
        tourist = new Tourist();
        tourist.setFirstName(DEFAULT_FIRST_NAME);
        tourist.setLastName(DEFAULT_LAST_NAME);
        tourist.setEmail(DEFAULT_EMAIL);
        tourist.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        tourist.setCountryResidence(DEFAULT_COUNTRY_RESIDENCE);
        tourist.setCountryBooking(DEFAULT_COUNTRY_BOOKING);
        tourist.setLanguage(DEFAULT_LANGUAGE);
        tourist.setCurrency(DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    public void createTourist() throws Exception {
        int databaseSizeBeforeCreate = touristRepository.findAll().size();

        // Create the Tourist

        restTouristMockMvc.perform(post("/api/tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourist)))
                .andExpect(status().isCreated());

        // Validate the Tourist in the database
        List<Tourist> tourists = touristRepository.findAll();
        assertThat(tourists).hasSize(databaseSizeBeforeCreate + 1);
        Tourist testTourist = tourists.get(tourists.size() - 1);
        assertThat(testTourist.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTourist.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTourist.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTourist.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTourist.getCountryResidence()).isEqualTo(DEFAULT_COUNTRY_RESIDENCE);
        assertThat(testTourist.getCountryBooking()).isEqualTo(DEFAULT_COUNTRY_BOOKING);
        assertThat(testTourist.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testTourist.getCurrency()).isEqualTo(DEFAULT_CURRENCY);

        // Validate the Tourist in ElasticSearch
        Tourist touristEs = touristSearchRepository.findOne(testTourist.getId());
        assertThat(touristEs).isEqualToComparingFieldByField(testTourist);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setFirstName(null);

        // Create the Tourist, which fails.

        restTouristMockMvc.perform(post("/api/tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourist)))
                .andExpect(status().isBadRequest());

        List<Tourist> tourists = touristRepository.findAll();
        assertThat(tourists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setLastName(null);

        // Create the Tourist, which fails.

        restTouristMockMvc.perform(post("/api/tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourist)))
                .andExpect(status().isBadRequest());

        List<Tourist> tourists = touristRepository.findAll();
        assertThat(tourists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setEmail(null);

        // Create the Tourist, which fails.

        restTouristMockMvc.perform(post("/api/tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourist)))
                .andExpect(status().isBadRequest());

        List<Tourist> tourists = touristRepository.findAll();
        assertThat(tourists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setPhoneNumber(null);

        // Create the Tourist, which fails.

        restTouristMockMvc.perform(post("/api/tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourist)))
                .andExpect(status().isBadRequest());

        List<Tourist> tourists = touristRepository.findAll();
        assertThat(tourists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryResidenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setCountryResidence(null);

        // Create the Tourist, which fails.

        restTouristMockMvc.perform(post("/api/tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourist)))
                .andExpect(status().isBadRequest());

        List<Tourist> tourists = touristRepository.findAll();
        assertThat(tourists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryBookingIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setCountryBooking(null);

        // Create the Tourist, which fails.

        restTouristMockMvc.perform(post("/api/tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tourist)))
                .andExpect(status().isBadRequest());

        List<Tourist> tourists = touristRepository.findAll();
        assertThat(tourists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTourists() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);

        // Get all the tourists
        restTouristMockMvc.perform(get("/api/tourists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tourist.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].countryResidence").value(hasItem(DEFAULT_COUNTRY_RESIDENCE.toString())))
                .andExpect(jsonPath("$.[*].countryBooking").value(hasItem(DEFAULT_COUNTRY_BOOKING.toString())))
                .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
                .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void getTourist() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);

        // Get the tourist
        restTouristMockMvc.perform(get("/api/tourists/{id}", tourist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tourist.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.countryResidence").value(DEFAULT_COUNTRY_RESIDENCE.toString()))
            .andExpect(jsonPath("$.countryBooking").value(DEFAULT_COUNTRY_BOOKING.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTourist() throws Exception {
        // Get the tourist
        restTouristMockMvc.perform(get("/api/tourists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTourist() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);
        touristSearchRepository.save(tourist);
        int databaseSizeBeforeUpdate = touristRepository.findAll().size();

        // Update the tourist
        Tourist updatedTourist = new Tourist();
        updatedTourist.setId(tourist.getId());
        updatedTourist.setFirstName(UPDATED_FIRST_NAME);
        updatedTourist.setLastName(UPDATED_LAST_NAME);
        updatedTourist.setEmail(UPDATED_EMAIL);
        updatedTourist.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedTourist.setCountryResidence(UPDATED_COUNTRY_RESIDENCE);
        updatedTourist.setCountryBooking(UPDATED_COUNTRY_BOOKING);
        updatedTourist.setLanguage(UPDATED_LANGUAGE);
        updatedTourist.setCurrency(UPDATED_CURRENCY);

        restTouristMockMvc.perform(put("/api/tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTourist)))
                .andExpect(status().isOk());

        // Validate the Tourist in the database
        List<Tourist> tourists = touristRepository.findAll();
        assertThat(tourists).hasSize(databaseSizeBeforeUpdate);
        Tourist testTourist = tourists.get(tourists.size() - 1);
        assertThat(testTourist.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTourist.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTourist.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTourist.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTourist.getCountryResidence()).isEqualTo(UPDATED_COUNTRY_RESIDENCE);
        assertThat(testTourist.getCountryBooking()).isEqualTo(UPDATED_COUNTRY_BOOKING);
        assertThat(testTourist.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testTourist.getCurrency()).isEqualTo(UPDATED_CURRENCY);

        // Validate the Tourist in ElasticSearch
        Tourist touristEs = touristSearchRepository.findOne(testTourist.getId());
        assertThat(touristEs).isEqualToComparingFieldByField(testTourist);
    }

    @Test
    @Transactional
    public void deleteTourist() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);
        touristSearchRepository.save(tourist);
        int databaseSizeBeforeDelete = touristRepository.findAll().size();

        // Get the tourist
        restTouristMockMvc.perform(delete("/api/tourists/{id}", tourist.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean touristExistsInEs = touristSearchRepository.exists(tourist.getId());
        assertThat(touristExistsInEs).isFalse();

        // Validate the database is empty
        List<Tourist> tourists = touristRepository.findAll();
        assertThat(tourists).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTourist() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);
        touristSearchRepository.save(tourist);

        // Search the tourist
        restTouristMockMvc.perform(get("/api/_search/tourists?query=id:" + tourist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tourist.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].countryResidence").value(hasItem(DEFAULT_COUNTRY_RESIDENCE.toString())))
            .andExpect(jsonPath("$.[*].countryBooking").value(hasItem(DEFAULT_COUNTRY_BOOKING.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }
}
