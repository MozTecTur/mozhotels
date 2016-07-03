package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.InstancePolicy;
import com.mozhotels.srit.repository.InstancePolicyRepository;
import com.mozhotels.srit.repository.search.InstancePolicySearchRepository;

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
 * Test class for the InstancePolicyResource REST controller.
 *
 * @see InstancePolicyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstancePolicyResourceIntTest {

    private static final String DEFAULT_INSTANCE_POLICTY_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_POLICTY_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private InstancePolicyRepository instancePolicyRepository;

    @Inject
    private InstancePolicySearchRepository instancePolicySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstancePolicyMockMvc;

    private InstancePolicy instancePolicy;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstancePolicyResource instancePolicyResource = new InstancePolicyResource();
        ReflectionTestUtils.setField(instancePolicyResource, "instancePolicySearchRepository", instancePolicySearchRepository);
        ReflectionTestUtils.setField(instancePolicyResource, "instancePolicyRepository", instancePolicyRepository);
        this.restInstancePolicyMockMvc = MockMvcBuilders.standaloneSetup(instancePolicyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instancePolicySearchRepository.deleteAll();
        instancePolicy = new InstancePolicy();
        instancePolicy.setInstancePolictyName(DEFAULT_INSTANCE_POLICTY_NAME);
        instancePolicy.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInstancePolicy() throws Exception {
        int databaseSizeBeforeCreate = instancePolicyRepository.findAll().size();

        // Create the InstancePolicy

        restInstancePolicyMockMvc.perform(post("/api/instance-policies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instancePolicy)))
                .andExpect(status().isCreated());

        // Validate the InstancePolicy in the database
        List<InstancePolicy> instancePolicies = instancePolicyRepository.findAll();
        assertThat(instancePolicies).hasSize(databaseSizeBeforeCreate + 1);
        InstancePolicy testInstancePolicy = instancePolicies.get(instancePolicies.size() - 1);
        assertThat(testInstancePolicy.getInstancePolictyName()).isEqualTo(DEFAULT_INSTANCE_POLICTY_NAME);
        assertThat(testInstancePolicy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the InstancePolicy in ElasticSearch
        InstancePolicy instancePolicyEs = instancePolicySearchRepository.findOne(testInstancePolicy.getId());
        assertThat(instancePolicyEs).isEqualToComparingFieldByField(testInstancePolicy);
    }

    @Test
    @Transactional
    public void checkInstancePolictyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instancePolicyRepository.findAll().size();
        // set the field null
        instancePolicy.setInstancePolictyName(null);

        // Create the InstancePolicy, which fails.

        restInstancePolicyMockMvc.perform(post("/api/instance-policies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instancePolicy)))
                .andExpect(status().isBadRequest());

        List<InstancePolicy> instancePolicies = instancePolicyRepository.findAll();
        assertThat(instancePolicies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstancePolicies() throws Exception {
        // Initialize the database
        instancePolicyRepository.saveAndFlush(instancePolicy);

        // Get all the instancePolicies
        restInstancePolicyMockMvc.perform(get("/api/instance-policies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instancePolicy.getId().intValue())))
                .andExpect(jsonPath("$.[*].instancePolictyName").value(hasItem(DEFAULT_INSTANCE_POLICTY_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInstancePolicy() throws Exception {
        // Initialize the database
        instancePolicyRepository.saveAndFlush(instancePolicy);

        // Get the instancePolicy
        restInstancePolicyMockMvc.perform(get("/api/instance-policies/{id}", instancePolicy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instancePolicy.getId().intValue()))
            .andExpect(jsonPath("$.instancePolictyName").value(DEFAULT_INSTANCE_POLICTY_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstancePolicy() throws Exception {
        // Get the instancePolicy
        restInstancePolicyMockMvc.perform(get("/api/instance-policies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstancePolicy() throws Exception {
        // Initialize the database
        instancePolicyRepository.saveAndFlush(instancePolicy);
        instancePolicySearchRepository.save(instancePolicy);
        int databaseSizeBeforeUpdate = instancePolicyRepository.findAll().size();

        // Update the instancePolicy
        InstancePolicy updatedInstancePolicy = new InstancePolicy();
        updatedInstancePolicy.setId(instancePolicy.getId());
        updatedInstancePolicy.setInstancePolictyName(UPDATED_INSTANCE_POLICTY_NAME);
        updatedInstancePolicy.setDescription(UPDATED_DESCRIPTION);

        restInstancePolicyMockMvc.perform(put("/api/instance-policies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstancePolicy)))
                .andExpect(status().isOk());

        // Validate the InstancePolicy in the database
        List<InstancePolicy> instancePolicies = instancePolicyRepository.findAll();
        assertThat(instancePolicies).hasSize(databaseSizeBeforeUpdate);
        InstancePolicy testInstancePolicy = instancePolicies.get(instancePolicies.size() - 1);
        assertThat(testInstancePolicy.getInstancePolictyName()).isEqualTo(UPDATED_INSTANCE_POLICTY_NAME);
        assertThat(testInstancePolicy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the InstancePolicy in ElasticSearch
        InstancePolicy instancePolicyEs = instancePolicySearchRepository.findOne(testInstancePolicy.getId());
        assertThat(instancePolicyEs).isEqualToComparingFieldByField(testInstancePolicy);
    }

    @Test
    @Transactional
    public void deleteInstancePolicy() throws Exception {
        // Initialize the database
        instancePolicyRepository.saveAndFlush(instancePolicy);
        instancePolicySearchRepository.save(instancePolicy);
        int databaseSizeBeforeDelete = instancePolicyRepository.findAll().size();

        // Get the instancePolicy
        restInstancePolicyMockMvc.perform(delete("/api/instance-policies/{id}", instancePolicy.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instancePolicyExistsInEs = instancePolicySearchRepository.exists(instancePolicy.getId());
        assertThat(instancePolicyExistsInEs).isFalse();

        // Validate the database is empty
        List<InstancePolicy> instancePolicies = instancePolicyRepository.findAll();
        assertThat(instancePolicies).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstancePolicy() throws Exception {
        // Initialize the database
        instancePolicyRepository.saveAndFlush(instancePolicy);
        instancePolicySearchRepository.save(instancePolicy);

        // Search the instancePolicy
        restInstancePolicyMockMvc.perform(get("/api/_search/instance-policies?query=id:" + instancePolicy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instancePolicy.getId().intValue())))
            .andExpect(jsonPath("$.[*].instancePolictyName").value(hasItem(DEFAULT_INSTANCE_POLICTY_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
