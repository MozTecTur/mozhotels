package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.InstancePolicyType;
import com.mozhotels.srit.repository.InstancePolicyTypeRepository;
import com.mozhotels.srit.repository.search.InstancePolicyTypeSearchRepository;

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


/**
 * Test class for the InstancePolicyTypeResource REST controller.
 *
 * @see InstancePolicyTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstancePolicyTypeResourceIntTest {

    private static final String DEFAULT_INSTANCE_POLICY_TYPE_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_POLICY_TYPE_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private InstancePolicyTypeRepository instancePolicyTypeRepository;

    @Inject
    private InstancePolicyTypeSearchRepository instancePolicyTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstancePolicyTypeMockMvc;

    private InstancePolicyType instancePolicyType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstancePolicyTypeResource instancePolicyTypeResource = new InstancePolicyTypeResource();
        ReflectionTestUtils.setField(instancePolicyTypeResource, "instancePolicyTypeSearchRepository", instancePolicyTypeSearchRepository);
        ReflectionTestUtils.setField(instancePolicyTypeResource, "instancePolicyTypeRepository", instancePolicyTypeRepository);
        this.restInstancePolicyTypeMockMvc = MockMvcBuilders.standaloneSetup(instancePolicyTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instancePolicyTypeSearchRepository.deleteAll();
        instancePolicyType = new InstancePolicyType();
        instancePolicyType.setInstancePolicyTypeName(DEFAULT_INSTANCE_POLICY_TYPE_NAME);
        instancePolicyType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInstancePolicyType() throws Exception {
        int databaseSizeBeforeCreate = instancePolicyTypeRepository.findAll().size();

        // Create the InstancePolicyType

        restInstancePolicyTypeMockMvc.perform(post("/api/instance-policy-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instancePolicyType)))
                .andExpect(status().isCreated());

        // Validate the InstancePolicyType in the database
        List<InstancePolicyType> instancePolicyTypes = instancePolicyTypeRepository.findAll();
        assertThat(instancePolicyTypes).hasSize(databaseSizeBeforeCreate + 1);
        InstancePolicyType testInstancePolicyType = instancePolicyTypes.get(instancePolicyTypes.size() - 1);
        assertThat(testInstancePolicyType.getInstancePolicyTypeName()).isEqualTo(DEFAULT_INSTANCE_POLICY_TYPE_NAME);
        assertThat(testInstancePolicyType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the InstancePolicyType in ElasticSearch
        InstancePolicyType instancePolicyTypeEs = instancePolicyTypeSearchRepository.findOne(testInstancePolicyType.getId());
        assertThat(instancePolicyTypeEs).isEqualToComparingFieldByField(testInstancePolicyType);
    }

    @Test
    @Transactional
    public void checkInstancePolicyTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instancePolicyTypeRepository.findAll().size();
        // set the field null
        instancePolicyType.setInstancePolicyTypeName(null);

        // Create the InstancePolicyType, which fails.

        restInstancePolicyTypeMockMvc.perform(post("/api/instance-policy-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instancePolicyType)))
                .andExpect(status().isBadRequest());

        List<InstancePolicyType> instancePolicyTypes = instancePolicyTypeRepository.findAll();
        assertThat(instancePolicyTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = instancePolicyTypeRepository.findAll().size();
        // set the field null
        instancePolicyType.setDescription(null);

        // Create the InstancePolicyType, which fails.

        restInstancePolicyTypeMockMvc.perform(post("/api/instance-policy-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instancePolicyType)))
                .andExpect(status().isBadRequest());

        List<InstancePolicyType> instancePolicyTypes = instancePolicyTypeRepository.findAll();
        assertThat(instancePolicyTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstancePolicyTypes() throws Exception {
        // Initialize the database
        instancePolicyTypeRepository.saveAndFlush(instancePolicyType);

        // Get all the instancePolicyTypes
        restInstancePolicyTypeMockMvc.perform(get("/api/instance-policy-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instancePolicyType.getId().intValue())))
                .andExpect(jsonPath("$.[*].instancePolicyTypeName").value(hasItem(DEFAULT_INSTANCE_POLICY_TYPE_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInstancePolicyType() throws Exception {
        // Initialize the database
        instancePolicyTypeRepository.saveAndFlush(instancePolicyType);

        // Get the instancePolicyType
        restInstancePolicyTypeMockMvc.perform(get("/api/instance-policy-types/{id}", instancePolicyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instancePolicyType.getId().intValue()))
            .andExpect(jsonPath("$.instancePolicyTypeName").value(DEFAULT_INSTANCE_POLICY_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstancePolicyType() throws Exception {
        // Get the instancePolicyType
        restInstancePolicyTypeMockMvc.perform(get("/api/instance-policy-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstancePolicyType() throws Exception {
        // Initialize the database
        instancePolicyTypeRepository.saveAndFlush(instancePolicyType);
        instancePolicyTypeSearchRepository.save(instancePolicyType);
        int databaseSizeBeforeUpdate = instancePolicyTypeRepository.findAll().size();

        // Update the instancePolicyType
        InstancePolicyType updatedInstancePolicyType = new InstancePolicyType();
        updatedInstancePolicyType.setId(instancePolicyType.getId());
        updatedInstancePolicyType.setInstancePolicyTypeName(UPDATED_INSTANCE_POLICY_TYPE_NAME);
        updatedInstancePolicyType.setDescription(UPDATED_DESCRIPTION);

        restInstancePolicyTypeMockMvc.perform(put("/api/instance-policy-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstancePolicyType)))
                .andExpect(status().isOk());

        // Validate the InstancePolicyType in the database
        List<InstancePolicyType> instancePolicyTypes = instancePolicyTypeRepository.findAll();
        assertThat(instancePolicyTypes).hasSize(databaseSizeBeforeUpdate);
        InstancePolicyType testInstancePolicyType = instancePolicyTypes.get(instancePolicyTypes.size() - 1);
        assertThat(testInstancePolicyType.getInstancePolicyTypeName()).isEqualTo(UPDATED_INSTANCE_POLICY_TYPE_NAME);
        assertThat(testInstancePolicyType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the InstancePolicyType in ElasticSearch
        InstancePolicyType instancePolicyTypeEs = instancePolicyTypeSearchRepository.findOne(testInstancePolicyType.getId());
        assertThat(instancePolicyTypeEs).isEqualToComparingFieldByField(testInstancePolicyType);
    }

    @Test
    @Transactional
    public void deleteInstancePolicyType() throws Exception {
        // Initialize the database
        instancePolicyTypeRepository.saveAndFlush(instancePolicyType);
        instancePolicyTypeSearchRepository.save(instancePolicyType);
        int databaseSizeBeforeDelete = instancePolicyTypeRepository.findAll().size();

        // Get the instancePolicyType
        restInstancePolicyTypeMockMvc.perform(delete("/api/instance-policy-types/{id}", instancePolicyType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instancePolicyTypeExistsInEs = instancePolicyTypeSearchRepository.exists(instancePolicyType.getId());
        assertThat(instancePolicyTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<InstancePolicyType> instancePolicyTypes = instancePolicyTypeRepository.findAll();
        assertThat(instancePolicyTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstancePolicyType() throws Exception {
        // Initialize the database
        instancePolicyTypeRepository.saveAndFlush(instancePolicyType);
        instancePolicyTypeSearchRepository.save(instancePolicyType);

        // Search the instancePolicyType
        restInstancePolicyTypeMockMvc.perform(get("/api/_search/instance-policy-types?query=id:" + instancePolicyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instancePolicyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].instancePolicyTypeName").value(hasItem(DEFAULT_INSTANCE_POLICY_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
