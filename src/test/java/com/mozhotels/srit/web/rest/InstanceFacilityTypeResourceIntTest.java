package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.InstanceFacilityType;
import com.mozhotels.srit.repository.InstanceFacilityTypeRepository;
import com.mozhotels.srit.repository.search.InstanceFacilityTypeSearchRepository;

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
 * Test class for the InstanceFacilityTypeResource REST controller.
 *
 * @see InstanceFacilityTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceFacilityTypeResourceIntTest {

    private static final String DEFAULT_INSTANCE_FACILITY_TYPE_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_FACILITY_TYPE_NAME = "BBBBB";

    private static final BFacility DEFAULT_FACILITY = BFacility.SERVICE;
    private static final BFacility UPDATED_FACILITY = BFacility.RESOURCE;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private InstanceFacilityTypeRepository instanceFacilityTypeRepository;

    @Inject
    private InstanceFacilityTypeSearchRepository instanceFacilityTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceFacilityTypeMockMvc;

    private InstanceFacilityType instanceFacilityType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceFacilityTypeResource instanceFacilityTypeResource = new InstanceFacilityTypeResource();
        ReflectionTestUtils.setField(instanceFacilityTypeResource, "instanceFacilityTypeSearchRepository", instanceFacilityTypeSearchRepository);
        ReflectionTestUtils.setField(instanceFacilityTypeResource, "instanceFacilityTypeRepository", instanceFacilityTypeRepository);
        this.restInstanceFacilityTypeMockMvc = MockMvcBuilders.standaloneSetup(instanceFacilityTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceFacilityTypeSearchRepository.deleteAll();
        instanceFacilityType = new InstanceFacilityType();
        instanceFacilityType.setInstanceFacilityTypeName(DEFAULT_INSTANCE_FACILITY_TYPE_NAME);
        instanceFacilityType.setFacility(DEFAULT_FACILITY);
        instanceFacilityType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInstanceFacilityType() throws Exception {
        int databaseSizeBeforeCreate = instanceFacilityTypeRepository.findAll().size();

        // Create the InstanceFacilityType

        restInstanceFacilityTypeMockMvc.perform(post("/api/instance-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceFacilityType)))
                .andExpect(status().isCreated());

        // Validate the InstanceFacilityType in the database
        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeCreate + 1);
        InstanceFacilityType testInstanceFacilityType = instanceFacilityTypes.get(instanceFacilityTypes.size() - 1);
        assertThat(testInstanceFacilityType.getInstanceFacilityTypeName()).isEqualTo(DEFAULT_INSTANCE_FACILITY_TYPE_NAME);
        assertThat(testInstanceFacilityType.getFacility()).isEqualTo(DEFAULT_FACILITY);
        assertThat(testInstanceFacilityType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the InstanceFacilityType in ElasticSearch
        InstanceFacilityType instanceFacilityTypeEs = instanceFacilityTypeSearchRepository.findOne(testInstanceFacilityType.getId());
        assertThat(instanceFacilityTypeEs).isEqualToComparingFieldByField(testInstanceFacilityType);
    }

    @Test
    @Transactional
    public void checkInstanceFacilityTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceFacilityTypeRepository.findAll().size();
        // set the field null
        instanceFacilityType.setInstanceFacilityTypeName(null);

        // Create the InstanceFacilityType, which fails.

        restInstanceFacilityTypeMockMvc.perform(post("/api/instance-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceFacilityType)))
                .andExpect(status().isBadRequest());

        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFacilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceFacilityTypeRepository.findAll().size();
        // set the field null
        instanceFacilityType.setFacility(null);

        // Create the InstanceFacilityType, which fails.

        restInstanceFacilityTypeMockMvc.perform(post("/api/instance-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceFacilityType)))
                .andExpect(status().isBadRequest());

        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceFacilityTypes() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);

        // Get all the instanceFacilityTypes
        restInstanceFacilityTypeMockMvc.perform(get("/api/instance-facility-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceFacilityType.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceFacilityTypeName").value(hasItem(DEFAULT_INSTANCE_FACILITY_TYPE_NAME.toString())))
                .andExpect(jsonPath("$.[*].facility").value(hasItem(DEFAULT_FACILITY.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInstanceFacilityType() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);

        // Get the instanceFacilityType
        restInstanceFacilityTypeMockMvc.perform(get("/api/instance-facility-types/{id}", instanceFacilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceFacilityType.getId().intValue()))
            .andExpect(jsonPath("$.instanceFacilityTypeName").value(DEFAULT_INSTANCE_FACILITY_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.facility").value(DEFAULT_FACILITY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceFacilityType() throws Exception {
        // Get the instanceFacilityType
        restInstanceFacilityTypeMockMvc.perform(get("/api/instance-facility-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceFacilityType() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);
        instanceFacilityTypeSearchRepository.save(instanceFacilityType);
        int databaseSizeBeforeUpdate = instanceFacilityTypeRepository.findAll().size();

        // Update the instanceFacilityType
        InstanceFacilityType updatedInstanceFacilityType = new InstanceFacilityType();
        updatedInstanceFacilityType.setId(instanceFacilityType.getId());
        updatedInstanceFacilityType.setInstanceFacilityTypeName(UPDATED_INSTANCE_FACILITY_TYPE_NAME);
        updatedInstanceFacilityType.setFacility(UPDATED_FACILITY);
        updatedInstanceFacilityType.setDescription(UPDATED_DESCRIPTION);

        restInstanceFacilityTypeMockMvc.perform(put("/api/instance-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceFacilityType)))
                .andExpect(status().isOk());

        // Validate the InstanceFacilityType in the database
        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeUpdate);
        InstanceFacilityType testInstanceFacilityType = instanceFacilityTypes.get(instanceFacilityTypes.size() - 1);
        assertThat(testInstanceFacilityType.getInstanceFacilityTypeName()).isEqualTo(UPDATED_INSTANCE_FACILITY_TYPE_NAME);
        assertThat(testInstanceFacilityType.getFacility()).isEqualTo(UPDATED_FACILITY);
        assertThat(testInstanceFacilityType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the InstanceFacilityType in ElasticSearch
        InstanceFacilityType instanceFacilityTypeEs = instanceFacilityTypeSearchRepository.findOne(testInstanceFacilityType.getId());
        assertThat(instanceFacilityTypeEs).isEqualToComparingFieldByField(testInstanceFacilityType);
    }

    @Test
    @Transactional
    public void deleteInstanceFacilityType() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);
        instanceFacilityTypeSearchRepository.save(instanceFacilityType);
        int databaseSizeBeforeDelete = instanceFacilityTypeRepository.findAll().size();

        // Get the instanceFacilityType
        restInstanceFacilityTypeMockMvc.perform(delete("/api/instance-facility-types/{id}", instanceFacilityType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceFacilityTypeExistsInEs = instanceFacilityTypeSearchRepository.exists(instanceFacilityType.getId());
        assertThat(instanceFacilityTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceFacilityType() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);
        instanceFacilityTypeSearchRepository.save(instanceFacilityType);

        // Search the instanceFacilityType
        restInstanceFacilityTypeMockMvc.perform(get("/api/_search/instance-facility-types?query=id:" + instanceFacilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceFacilityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceFacilityTypeName").value(hasItem(DEFAULT_INSTANCE_FACILITY_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].facility").value(hasItem(DEFAULT_FACILITY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
