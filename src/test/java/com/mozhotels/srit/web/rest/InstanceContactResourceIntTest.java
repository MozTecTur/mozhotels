package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.InstanceContact;
import com.mozhotels.srit.repository.InstanceContactRepository;
import com.mozhotels.srit.repository.search.InstanceContactSearchRepository;

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
 * Test class for the InstanceContactResource REST controller.
 *
 * @see InstanceContactResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceContactResourceIntTest {


    private static final Integer DEFAULT_CONTACT_NUMBER = 1;
    private static final Integer UPDATED_CONTACT_NUMBER = 2;

    @Inject
    private InstanceContactRepository instanceContactRepository;

    @Inject
    private InstanceContactSearchRepository instanceContactSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceContactMockMvc;

    private InstanceContact instanceContact;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceContactResource instanceContactResource = new InstanceContactResource();
        ReflectionTestUtils.setField(instanceContactResource, "instanceContactSearchRepository", instanceContactSearchRepository);
        ReflectionTestUtils.setField(instanceContactResource, "instanceContactRepository", instanceContactRepository);
        this.restInstanceContactMockMvc = MockMvcBuilders.standaloneSetup(instanceContactResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceContactSearchRepository.deleteAll();
        instanceContact = new InstanceContact();
        instanceContact.setContactNumber(DEFAULT_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    public void createInstanceContact() throws Exception {
        int databaseSizeBeforeCreate = instanceContactRepository.findAll().size();

        // Create the InstanceContact

        restInstanceContactMockMvc.perform(post("/api/instance-contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceContact)))
                .andExpect(status().isCreated());

        // Validate the InstanceContact in the database
        List<InstanceContact> instanceContacts = instanceContactRepository.findAll();
        assertThat(instanceContacts).hasSize(databaseSizeBeforeCreate + 1);
        InstanceContact testInstanceContact = instanceContacts.get(instanceContacts.size() - 1);
        assertThat(testInstanceContact.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);

        // Validate the InstanceContact in ElasticSearch
        InstanceContact instanceContactEs = instanceContactSearchRepository.findOne(testInstanceContact.getId());
        assertThat(instanceContactEs).isEqualToComparingFieldByField(testInstanceContact);
    }

    @Test
    @Transactional
    public void checkContactNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceContactRepository.findAll().size();
        // set the field null
        instanceContact.setContactNumber(null);

        // Create the InstanceContact, which fails.

        restInstanceContactMockMvc.perform(post("/api/instance-contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceContact)))
                .andExpect(status().isBadRequest());

        List<InstanceContact> instanceContacts = instanceContactRepository.findAll();
        assertThat(instanceContacts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceContacts() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);

        // Get all the instanceContacts
        restInstanceContactMockMvc.perform(get("/api/instance-contacts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceContact.getId().intValue())))
                .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)));
    }

    @Test
    @Transactional
    public void getInstanceContact() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);

        // Get the instanceContact
        restInstanceContactMockMvc.perform(get("/api/instance-contacts/{id}", instanceContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceContact.getId().intValue()))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceContact() throws Exception {
        // Get the instanceContact
        restInstanceContactMockMvc.perform(get("/api/instance-contacts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceContact() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);
        instanceContactSearchRepository.save(instanceContact);
        int databaseSizeBeforeUpdate = instanceContactRepository.findAll().size();

        // Update the instanceContact
        InstanceContact updatedInstanceContact = new InstanceContact();
        updatedInstanceContact.setId(instanceContact.getId());
        updatedInstanceContact.setContactNumber(UPDATED_CONTACT_NUMBER);

        restInstanceContactMockMvc.perform(put("/api/instance-contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceContact)))
                .andExpect(status().isOk());

        // Validate the InstanceContact in the database
        List<InstanceContact> instanceContacts = instanceContactRepository.findAll();
        assertThat(instanceContacts).hasSize(databaseSizeBeforeUpdate);
        InstanceContact testInstanceContact = instanceContacts.get(instanceContacts.size() - 1);
        assertThat(testInstanceContact.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);

        // Validate the InstanceContact in ElasticSearch
        InstanceContact instanceContactEs = instanceContactSearchRepository.findOne(testInstanceContact.getId());
        assertThat(instanceContactEs).isEqualToComparingFieldByField(testInstanceContact);
    }

    @Test
    @Transactional
    public void deleteInstanceContact() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);
        instanceContactSearchRepository.save(instanceContact);
        int databaseSizeBeforeDelete = instanceContactRepository.findAll().size();

        // Get the instanceContact
        restInstanceContactMockMvc.perform(delete("/api/instance-contacts/{id}", instanceContact.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceContactExistsInEs = instanceContactSearchRepository.exists(instanceContact.getId());
        assertThat(instanceContactExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceContact> instanceContacts = instanceContactRepository.findAll();
        assertThat(instanceContacts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceContact() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);
        instanceContactSearchRepository.save(instanceContact);

        // Search the instanceContact
        restInstanceContactMockMvc.perform(get("/api/_search/instance-contacts?query=id:" + instanceContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)));
    }
}
