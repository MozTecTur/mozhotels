package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.InstanceRoomFacility;
import com.mozhotels.srit.repository.InstanceRoomFacilityRepository;
import com.mozhotels.srit.repository.search.InstanceRoomFacilitySearchRepository;

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

import com.mozhotels.srit.domain.enumeration.BFacility;
import com.mozhotels.srit.domain.enumeration.FInstanceArea;

/**
 * Test class for the InstanceRoomFacilityResource REST controller.
 *
 * @see InstanceRoomFacilityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceRoomFacilityResourceIntTest {

    private static final String DEFAULT_INSTANCE_ROOM_FACILITY_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_ROOM_FACILITY_NAME = "BBBBB";

    private static final BFacility DEFAULT_FACILITY = BFacility.SERVICE;
    private static final BFacility UPDATED_FACILITY = BFacility.RESOURCE;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final FInstanceArea DEFAULT_AREA = FInstanceArea.INDOOR;
    private static final FInstanceArea UPDATED_AREA = FInstanceArea.OUTDOOR;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    @Inject
    private InstanceRoomFacilityRepository instanceRoomFacilityRepository;

    @Inject
    private InstanceRoomFacilitySearchRepository instanceRoomFacilitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceRoomFacilityMockMvc;

    private InstanceRoomFacility instanceRoomFacility;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceRoomFacilityResource instanceRoomFacilityResource = new InstanceRoomFacilityResource();
        ReflectionTestUtils.setField(instanceRoomFacilityResource, "instanceRoomFacilitySearchRepository", instanceRoomFacilitySearchRepository);
        ReflectionTestUtils.setField(instanceRoomFacilityResource, "instanceRoomFacilityRepository", instanceRoomFacilityRepository);
        this.restInstanceRoomFacilityMockMvc = MockMvcBuilders.standaloneSetup(instanceRoomFacilityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceRoomFacilitySearchRepository.deleteAll();
        instanceRoomFacility = new InstanceRoomFacility();
        instanceRoomFacility.setInstanceRoomFacilityName(DEFAULT_INSTANCE_ROOM_FACILITY_NAME);
        instanceRoomFacility.setFacility(DEFAULT_FACILITY);
        instanceRoomFacility.setQuantity(DEFAULT_QUANTITY);
        instanceRoomFacility.setArea(DEFAULT_AREA);
        instanceRoomFacility.setPrice(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createInstanceRoomFacility() throws Exception {
        int databaseSizeBeforeCreate = instanceRoomFacilityRepository.findAll().size();

        // Create the InstanceRoomFacility

        restInstanceRoomFacilityMockMvc.perform(post("/api/instance-room-facilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomFacility)))
                .andExpect(status().isCreated());

        // Validate the InstanceRoomFacility in the database
        List<InstanceRoomFacility> instanceRoomFacilities = instanceRoomFacilityRepository.findAll();
        assertThat(instanceRoomFacilities).hasSize(databaseSizeBeforeCreate + 1);
        InstanceRoomFacility testInstanceRoomFacility = instanceRoomFacilities.get(instanceRoomFacilities.size() - 1);
        assertThat(testInstanceRoomFacility.getInstanceRoomFacilityName()).isEqualTo(DEFAULT_INSTANCE_ROOM_FACILITY_NAME);
        assertThat(testInstanceRoomFacility.getFacility()).isEqualTo(DEFAULT_FACILITY);
        assertThat(testInstanceRoomFacility.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInstanceRoomFacility.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testInstanceRoomFacility.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the InstanceRoomFacility in ElasticSearch
        InstanceRoomFacility instanceRoomFacilityEs = instanceRoomFacilitySearchRepository.findOne(testInstanceRoomFacility.getId());
        assertThat(instanceRoomFacilityEs).isEqualToComparingFieldByField(testInstanceRoomFacility);
    }

    @Test
    @Transactional
    public void checkInstanceRoomFacilityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceRoomFacilityRepository.findAll().size();
        // set the field null
        instanceRoomFacility.setInstanceRoomFacilityName(null);

        // Create the InstanceRoomFacility, which fails.

        restInstanceRoomFacilityMockMvc.perform(post("/api/instance-room-facilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomFacility)))
                .andExpect(status().isBadRequest());

        List<InstanceRoomFacility> instanceRoomFacilities = instanceRoomFacilityRepository.findAll();
        assertThat(instanceRoomFacilities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFacilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceRoomFacilityRepository.findAll().size();
        // set the field null
        instanceRoomFacility.setFacility(null);

        // Create the InstanceRoomFacility, which fails.

        restInstanceRoomFacilityMockMvc.perform(post("/api/instance-room-facilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomFacility)))
                .andExpect(status().isBadRequest());

        List<InstanceRoomFacility> instanceRoomFacilities = instanceRoomFacilityRepository.findAll();
        assertThat(instanceRoomFacilities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceRoomFacilityRepository.findAll().size();
        // set the field null
        instanceRoomFacility.setPrice(null);

        // Create the InstanceRoomFacility, which fails.

        restInstanceRoomFacilityMockMvc.perform(post("/api/instance-room-facilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomFacility)))
                .andExpect(status().isBadRequest());

        List<InstanceRoomFacility> instanceRoomFacilities = instanceRoomFacilityRepository.findAll();
        assertThat(instanceRoomFacilities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceRoomFacilities() throws Exception {
        // Initialize the database
        instanceRoomFacilityRepository.saveAndFlush(instanceRoomFacility);

        // Get all the instanceRoomFacilities
        restInstanceRoomFacilityMockMvc.perform(get("/api/instance-room-facilities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceRoomFacility.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceRoomFacilityName").value(hasItem(DEFAULT_INSTANCE_ROOM_FACILITY_NAME.toString())))
                .andExpect(jsonPath("$.[*].facility").value(hasItem(DEFAULT_FACILITY.toString())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getInstanceRoomFacility() throws Exception {
        // Initialize the database
        instanceRoomFacilityRepository.saveAndFlush(instanceRoomFacility);

        // Get the instanceRoomFacility
        restInstanceRoomFacilityMockMvc.perform(get("/api/instance-room-facilities/{id}", instanceRoomFacility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceRoomFacility.getId().intValue()))
            .andExpect(jsonPath("$.instanceRoomFacilityName").value(DEFAULT_INSTANCE_ROOM_FACILITY_NAME.toString()))
            .andExpect(jsonPath("$.facility").value(DEFAULT_FACILITY.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceRoomFacility() throws Exception {
        // Get the instanceRoomFacility
        restInstanceRoomFacilityMockMvc.perform(get("/api/instance-room-facilities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceRoomFacility() throws Exception {
        // Initialize the database
        instanceRoomFacilityRepository.saveAndFlush(instanceRoomFacility);
        instanceRoomFacilitySearchRepository.save(instanceRoomFacility);
        int databaseSizeBeforeUpdate = instanceRoomFacilityRepository.findAll().size();

        // Update the instanceRoomFacility
        InstanceRoomFacility updatedInstanceRoomFacility = new InstanceRoomFacility();
        updatedInstanceRoomFacility.setId(instanceRoomFacility.getId());
        updatedInstanceRoomFacility.setInstanceRoomFacilityName(UPDATED_INSTANCE_ROOM_FACILITY_NAME);
        updatedInstanceRoomFacility.setFacility(UPDATED_FACILITY);
        updatedInstanceRoomFacility.setQuantity(UPDATED_QUANTITY);
        updatedInstanceRoomFacility.setArea(UPDATED_AREA);
        updatedInstanceRoomFacility.setPrice(UPDATED_PRICE);

        restInstanceRoomFacilityMockMvc.perform(put("/api/instance-room-facilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceRoomFacility)))
                .andExpect(status().isOk());

        // Validate the InstanceRoomFacility in the database
        List<InstanceRoomFacility> instanceRoomFacilities = instanceRoomFacilityRepository.findAll();
        assertThat(instanceRoomFacilities).hasSize(databaseSizeBeforeUpdate);
        InstanceRoomFacility testInstanceRoomFacility = instanceRoomFacilities.get(instanceRoomFacilities.size() - 1);
        assertThat(testInstanceRoomFacility.getInstanceRoomFacilityName()).isEqualTo(UPDATED_INSTANCE_ROOM_FACILITY_NAME);
        assertThat(testInstanceRoomFacility.getFacility()).isEqualTo(UPDATED_FACILITY);
        assertThat(testInstanceRoomFacility.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInstanceRoomFacility.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testInstanceRoomFacility.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the InstanceRoomFacility in ElasticSearch
        InstanceRoomFacility instanceRoomFacilityEs = instanceRoomFacilitySearchRepository.findOne(testInstanceRoomFacility.getId());
        assertThat(instanceRoomFacilityEs).isEqualToComparingFieldByField(testInstanceRoomFacility);
    }

    @Test
    @Transactional
    public void deleteInstanceRoomFacility() throws Exception {
        // Initialize the database
        instanceRoomFacilityRepository.saveAndFlush(instanceRoomFacility);
        instanceRoomFacilitySearchRepository.save(instanceRoomFacility);
        int databaseSizeBeforeDelete = instanceRoomFacilityRepository.findAll().size();

        // Get the instanceRoomFacility
        restInstanceRoomFacilityMockMvc.perform(delete("/api/instance-room-facilities/{id}", instanceRoomFacility.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceRoomFacilityExistsInEs = instanceRoomFacilitySearchRepository.exists(instanceRoomFacility.getId());
        assertThat(instanceRoomFacilityExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceRoomFacility> instanceRoomFacilities = instanceRoomFacilityRepository.findAll();
        assertThat(instanceRoomFacilities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceRoomFacility() throws Exception {
        // Initialize the database
        instanceRoomFacilityRepository.saveAndFlush(instanceRoomFacility);
        instanceRoomFacilitySearchRepository.save(instanceRoomFacility);

        // Search the instanceRoomFacility
        restInstanceRoomFacilityMockMvc.perform(get("/api/_search/instance-room-facilities?query=id:" + instanceRoomFacility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceRoomFacility.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceRoomFacilityName").value(hasItem(DEFAULT_INSTANCE_ROOM_FACILITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].facility").value(hasItem(DEFAULT_FACILITY.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }
}
