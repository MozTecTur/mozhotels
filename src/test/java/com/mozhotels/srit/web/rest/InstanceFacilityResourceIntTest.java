package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.InstanceFacility;
import com.mozhotels.srit.repository.InstanceFacilityRepository;
import com.mozhotels.srit.repository.search.InstanceFacilitySearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mozhotels.srit.domain.enumeration.FInstanceArea;

/**
 * Test class for the InstanceFacilityResource REST controller.
 *
 * @see InstanceFacilityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceFacilityResourceIntTest {

    private static final String DEFAULT_INSTANCE_FACILITY_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_FACILITY_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final FInstanceArea DEFAULT_AREA = FInstanceArea.INDOOR;
    private static final FInstanceArea UPDATED_AREA = FInstanceArea.OUTDOOR;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    @Inject
    private InstanceFacilityRepository instanceFacilityRepository;

    @Inject
    private InstanceFacilitySearchRepository instanceFacilitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceFacilityMockMvc;

    private InstanceFacility instanceFacility;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceFacilityResource instanceFacilityResource = new InstanceFacilityResource();
        ReflectionTestUtils.setField(instanceFacilityResource, "instanceFacilitySearchRepository", instanceFacilitySearchRepository);
        ReflectionTestUtils.setField(instanceFacilityResource, "instanceFacilityRepository", instanceFacilityRepository);
        this.restInstanceFacilityMockMvc = MockMvcBuilders.standaloneSetup(instanceFacilityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceFacilitySearchRepository.deleteAll();
        instanceFacility = new InstanceFacility();
        instanceFacility.setInstanceFacilityName(DEFAULT_INSTANCE_FACILITY_NAME);
        instanceFacility.setDescription(DEFAULT_DESCRIPTION);
        instanceFacility.setQuantity(DEFAULT_QUANTITY);
        instanceFacility.setArea(DEFAULT_AREA);
        instanceFacility.setPrice(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createInstanceFacility() throws Exception {
        int databaseSizeBeforeCreate = instanceFacilityRepository.findAll().size();

        // Create the InstanceFacility

        restInstanceFacilityMockMvc.perform(post("/api/instance-facilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceFacility)))
                .andExpect(status().isCreated());

        // Validate the InstanceFacility in the database
        List<InstanceFacility> instanceFacilities = instanceFacilityRepository.findAll();
        assertThat(instanceFacilities).hasSize(databaseSizeBeforeCreate + 1);
        InstanceFacility testInstanceFacility = instanceFacilities.get(instanceFacilities.size() - 1);
        assertThat(testInstanceFacility.getInstanceFacilityName()).isEqualTo(DEFAULT_INSTANCE_FACILITY_NAME);
        assertThat(testInstanceFacility.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInstanceFacility.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInstanceFacility.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testInstanceFacility.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the InstanceFacility in ElasticSearch
        InstanceFacility instanceFacilityEs = instanceFacilitySearchRepository.findOne(testInstanceFacility.getId());
        assertThat(instanceFacilityEs).isEqualToComparingFieldByField(testInstanceFacility);
    }

    @Test
    @Transactional
    public void checkInstanceFacilityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceFacilityRepository.findAll().size();
        // set the field null
        instanceFacility.setInstanceFacilityName(null);

        // Create the InstanceFacility, which fails.

        restInstanceFacilityMockMvc.perform(post("/api/instance-facilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceFacility)))
                .andExpect(status().isBadRequest());

        List<InstanceFacility> instanceFacilities = instanceFacilityRepository.findAll();
        assertThat(instanceFacilities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceFacilityRepository.findAll().size();
        // set the field null
        instanceFacility.setPrice(null);

        // Create the InstanceFacility, which fails.

        restInstanceFacilityMockMvc.perform(post("/api/instance-facilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceFacility)))
                .andExpect(status().isBadRequest());

        List<InstanceFacility> instanceFacilities = instanceFacilityRepository.findAll();
        assertThat(instanceFacilities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceFacilities() throws Exception {
        // Initialize the database
        instanceFacilityRepository.saveAndFlush(instanceFacility);

        // Get all the instanceFacilities
        restInstanceFacilityMockMvc.perform(get("/api/instance-facilities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceFacility.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceFacilityName").value(hasItem(DEFAULT_INSTANCE_FACILITY_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getInstanceFacility() throws Exception {
        // Initialize the database
        instanceFacilityRepository.saveAndFlush(instanceFacility);

        // Get the instanceFacility
        restInstanceFacilityMockMvc.perform(get("/api/instance-facilities/{id}", instanceFacility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceFacility.getId().intValue()))
            .andExpect(jsonPath("$.instanceFacilityName").value(DEFAULT_INSTANCE_FACILITY_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceFacility() throws Exception {
        // Get the instanceFacility
        restInstanceFacilityMockMvc.perform(get("/api/instance-facilities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceFacility() throws Exception {
        // Initialize the database
        instanceFacilityRepository.saveAndFlush(instanceFacility);
        instanceFacilitySearchRepository.save(instanceFacility);
        int databaseSizeBeforeUpdate = instanceFacilityRepository.findAll().size();

        // Update the instanceFacility
        InstanceFacility updatedInstanceFacility = new InstanceFacility();
        updatedInstanceFacility.setId(instanceFacility.getId());
        updatedInstanceFacility.setInstanceFacilityName(UPDATED_INSTANCE_FACILITY_NAME);
        updatedInstanceFacility.setDescription(UPDATED_DESCRIPTION);
        updatedInstanceFacility.setQuantity(UPDATED_QUANTITY);
        updatedInstanceFacility.setArea(UPDATED_AREA);
        updatedInstanceFacility.setPrice(UPDATED_PRICE);

        restInstanceFacilityMockMvc.perform(put("/api/instance-facilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceFacility)))
                .andExpect(status().isOk());

        // Validate the InstanceFacility in the database
        List<InstanceFacility> instanceFacilities = instanceFacilityRepository.findAll();
        assertThat(instanceFacilities).hasSize(databaseSizeBeforeUpdate);
        InstanceFacility testInstanceFacility = instanceFacilities.get(instanceFacilities.size() - 1);
        assertThat(testInstanceFacility.getInstanceFacilityName()).isEqualTo(UPDATED_INSTANCE_FACILITY_NAME);
        assertThat(testInstanceFacility.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstanceFacility.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInstanceFacility.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testInstanceFacility.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the InstanceFacility in ElasticSearch
        InstanceFacility instanceFacilityEs = instanceFacilitySearchRepository.findOne(testInstanceFacility.getId());
        assertThat(instanceFacilityEs).isEqualToComparingFieldByField(testInstanceFacility);
    }

    @Test
    @Transactional
    public void deleteInstanceFacility() throws Exception {
        // Initialize the database
        instanceFacilityRepository.saveAndFlush(instanceFacility);
        instanceFacilitySearchRepository.save(instanceFacility);
        int databaseSizeBeforeDelete = instanceFacilityRepository.findAll().size();

        // Get the instanceFacility
        restInstanceFacilityMockMvc.perform(delete("/api/instance-facilities/{id}", instanceFacility.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceFacilityExistsInEs = instanceFacilitySearchRepository.exists(instanceFacility.getId());
        assertThat(instanceFacilityExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceFacility> instanceFacilities = instanceFacilityRepository.findAll();
        assertThat(instanceFacilities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceFacility() throws Exception {
        // Initialize the database
        instanceFacilityRepository.saveAndFlush(instanceFacility);
        instanceFacilitySearchRepository.save(instanceFacility);

        // Search the instanceFacility
        restInstanceFacilityMockMvc.perform(get("/api/_search/instance-facilities?query=id:" + instanceFacility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceFacility.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceFacilityName").value(hasItem(DEFAULT_INSTANCE_FACILITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }
}
