package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.InstanceRoomFacilityType;
import com.mozhotels.srit.repository.InstanceRoomFacilityTypeRepository;
import com.mozhotels.srit.repository.search.InstanceRoomFacilityTypeSearchRepository;

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

import com.mozhotels.srit.domain.enumeration.BFacility;

/**
 * Test class for the InstanceRoomFacilityTypeResource REST controller.
 *
 * @see InstanceRoomFacilityTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceRoomFacilityTypeResourceIntTest {

    private static final String DEFAULT_INSTANCE_ROOM_FACILITY_TYPE_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_ROOM_FACILITY_TYPE_NAME = "BBBBB";

    private static final BFacility DEFAULT_FACILITY = BFacility.SERVICE;
    private static final BFacility UPDATED_FACILITY = BFacility.RESOURCE;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private InstanceRoomFacilityTypeRepository instanceRoomFacilityTypeRepository;

    @Inject
    private InstanceRoomFacilityTypeSearchRepository instanceRoomFacilityTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceRoomFacilityTypeMockMvc;

    private InstanceRoomFacilityType instanceRoomFacilityType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceRoomFacilityTypeResource instanceRoomFacilityTypeResource = new InstanceRoomFacilityTypeResource();
        ReflectionTestUtils.setField(instanceRoomFacilityTypeResource, "instanceRoomFacilityTypeSearchRepository", instanceRoomFacilityTypeSearchRepository);
        ReflectionTestUtils.setField(instanceRoomFacilityTypeResource, "instanceRoomFacilityTypeRepository", instanceRoomFacilityTypeRepository);
        this.restInstanceRoomFacilityTypeMockMvc = MockMvcBuilders.standaloneSetup(instanceRoomFacilityTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceRoomFacilityTypeSearchRepository.deleteAll();
        instanceRoomFacilityType = new InstanceRoomFacilityType();
        instanceRoomFacilityType.setInstanceRoomFacilityTypeName(DEFAULT_INSTANCE_ROOM_FACILITY_TYPE_NAME);
        instanceRoomFacilityType.setFacility(DEFAULT_FACILITY);
        instanceRoomFacilityType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInstanceRoomFacilityType() throws Exception {
        int databaseSizeBeforeCreate = instanceRoomFacilityTypeRepository.findAll().size();

        // Create the InstanceRoomFacilityType

        restInstanceRoomFacilityTypeMockMvc.perform(post("/api/instance-room-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomFacilityType)))
                .andExpect(status().isCreated());

        // Validate the InstanceRoomFacilityType in the database
        List<InstanceRoomFacilityType> instanceRoomFacilityTypes = instanceRoomFacilityTypeRepository.findAll();
        assertThat(instanceRoomFacilityTypes).hasSize(databaseSizeBeforeCreate + 1);
        InstanceRoomFacilityType testInstanceRoomFacilityType = instanceRoomFacilityTypes.get(instanceRoomFacilityTypes.size() - 1);
        assertThat(testInstanceRoomFacilityType.getInstanceRoomFacilityTypeName()).isEqualTo(DEFAULT_INSTANCE_ROOM_FACILITY_TYPE_NAME);
        assertThat(testInstanceRoomFacilityType.getFacility()).isEqualTo(DEFAULT_FACILITY);
        assertThat(testInstanceRoomFacilityType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the InstanceRoomFacilityType in ElasticSearch
        InstanceRoomFacilityType instanceRoomFacilityTypeEs = instanceRoomFacilityTypeSearchRepository.findOne(testInstanceRoomFacilityType.getId());
        assertThat(instanceRoomFacilityTypeEs).isEqualToComparingFieldByField(testInstanceRoomFacilityType);
    }

    @Test
    @Transactional
    public void checkInstanceRoomFacilityTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceRoomFacilityTypeRepository.findAll().size();
        // set the field null
        instanceRoomFacilityType.setInstanceRoomFacilityTypeName(null);

        // Create the InstanceRoomFacilityType, which fails.

        restInstanceRoomFacilityTypeMockMvc.perform(post("/api/instance-room-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomFacilityType)))
                .andExpect(status().isBadRequest());

        List<InstanceRoomFacilityType> instanceRoomFacilityTypes = instanceRoomFacilityTypeRepository.findAll();
        assertThat(instanceRoomFacilityTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFacilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceRoomFacilityTypeRepository.findAll().size();
        // set the field null
        instanceRoomFacilityType.setFacility(null);

        // Create the InstanceRoomFacilityType, which fails.

        restInstanceRoomFacilityTypeMockMvc.perform(post("/api/instance-room-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomFacilityType)))
                .andExpect(status().isBadRequest());

        List<InstanceRoomFacilityType> instanceRoomFacilityTypes = instanceRoomFacilityTypeRepository.findAll();
        assertThat(instanceRoomFacilityTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceRoomFacilityTypes() throws Exception {
        // Initialize the database
        instanceRoomFacilityTypeRepository.saveAndFlush(instanceRoomFacilityType);

        // Get all the instanceRoomFacilityTypes
        restInstanceRoomFacilityTypeMockMvc.perform(get("/api/instance-room-facility-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceRoomFacilityType.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceRoomFacilityTypeName").value(hasItem(DEFAULT_INSTANCE_ROOM_FACILITY_TYPE_NAME.toString())))
                .andExpect(jsonPath("$.[*].facility").value(hasItem(DEFAULT_FACILITY.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInstanceRoomFacilityType() throws Exception {
        // Initialize the database
        instanceRoomFacilityTypeRepository.saveAndFlush(instanceRoomFacilityType);

        // Get the instanceRoomFacilityType
        restInstanceRoomFacilityTypeMockMvc.perform(get("/api/instance-room-facility-types/{id}", instanceRoomFacilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceRoomFacilityType.getId().intValue()))
            .andExpect(jsonPath("$.instanceRoomFacilityTypeName").value(DEFAULT_INSTANCE_ROOM_FACILITY_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.facility").value(DEFAULT_FACILITY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceRoomFacilityType() throws Exception {
        // Get the instanceRoomFacilityType
        restInstanceRoomFacilityTypeMockMvc.perform(get("/api/instance-room-facility-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceRoomFacilityType() throws Exception {
        // Initialize the database
        instanceRoomFacilityTypeRepository.saveAndFlush(instanceRoomFacilityType);
        instanceRoomFacilityTypeSearchRepository.save(instanceRoomFacilityType);
        int databaseSizeBeforeUpdate = instanceRoomFacilityTypeRepository.findAll().size();

        // Update the instanceRoomFacilityType
        InstanceRoomFacilityType updatedInstanceRoomFacilityType = new InstanceRoomFacilityType();
        updatedInstanceRoomFacilityType.setId(instanceRoomFacilityType.getId());
        updatedInstanceRoomFacilityType.setInstanceRoomFacilityTypeName(UPDATED_INSTANCE_ROOM_FACILITY_TYPE_NAME);
        updatedInstanceRoomFacilityType.setFacility(UPDATED_FACILITY);
        updatedInstanceRoomFacilityType.setDescription(UPDATED_DESCRIPTION);

        restInstanceRoomFacilityTypeMockMvc.perform(put("/api/instance-room-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceRoomFacilityType)))
                .andExpect(status().isOk());

        // Validate the InstanceRoomFacilityType in the database
        List<InstanceRoomFacilityType> instanceRoomFacilityTypes = instanceRoomFacilityTypeRepository.findAll();
        assertThat(instanceRoomFacilityTypes).hasSize(databaseSizeBeforeUpdate);
        InstanceRoomFacilityType testInstanceRoomFacilityType = instanceRoomFacilityTypes.get(instanceRoomFacilityTypes.size() - 1);
        assertThat(testInstanceRoomFacilityType.getInstanceRoomFacilityTypeName()).isEqualTo(UPDATED_INSTANCE_ROOM_FACILITY_TYPE_NAME);
        assertThat(testInstanceRoomFacilityType.getFacility()).isEqualTo(UPDATED_FACILITY);
        assertThat(testInstanceRoomFacilityType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the InstanceRoomFacilityType in ElasticSearch
        InstanceRoomFacilityType instanceRoomFacilityTypeEs = instanceRoomFacilityTypeSearchRepository.findOne(testInstanceRoomFacilityType.getId());
        assertThat(instanceRoomFacilityTypeEs).isEqualToComparingFieldByField(testInstanceRoomFacilityType);
    }

    @Test
    @Transactional
    public void deleteInstanceRoomFacilityType() throws Exception {
        // Initialize the database
        instanceRoomFacilityTypeRepository.saveAndFlush(instanceRoomFacilityType);
        instanceRoomFacilityTypeSearchRepository.save(instanceRoomFacilityType);
        int databaseSizeBeforeDelete = instanceRoomFacilityTypeRepository.findAll().size();

        // Get the instanceRoomFacilityType
        restInstanceRoomFacilityTypeMockMvc.perform(delete("/api/instance-room-facility-types/{id}", instanceRoomFacilityType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceRoomFacilityTypeExistsInEs = instanceRoomFacilityTypeSearchRepository.exists(instanceRoomFacilityType.getId());
        assertThat(instanceRoomFacilityTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceRoomFacilityType> instanceRoomFacilityTypes = instanceRoomFacilityTypeRepository.findAll();
        assertThat(instanceRoomFacilityTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceRoomFacilityType() throws Exception {
        // Initialize the database
        instanceRoomFacilityTypeRepository.saveAndFlush(instanceRoomFacilityType);
        instanceRoomFacilityTypeSearchRepository.save(instanceRoomFacilityType);

        // Search the instanceRoomFacilityType
        restInstanceRoomFacilityTypeMockMvc.perform(get("/api/_search/instance-room-facility-types?query=id:" + instanceRoomFacilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceRoomFacilityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceRoomFacilityTypeName").value(hasItem(DEFAULT_INSTANCE_ROOM_FACILITY_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].facility").value(hasItem(DEFAULT_FACILITY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
