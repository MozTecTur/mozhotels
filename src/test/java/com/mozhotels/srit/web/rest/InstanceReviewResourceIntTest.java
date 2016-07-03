package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.InstanceReview;
import com.mozhotels.srit.repository.InstanceReviewRepository;
import com.mozhotels.srit.repository.search.InstanceReviewSearchRepository;

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

import com.mozhotels.srit.domain.enumeration.GGEvaluation;

/**
 * Test class for the InstanceReviewResource REST controller.
 *
 * @see InstanceReviewResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceReviewResourceIntTest {


    private static final Float DEFAULT_CLEANLINESS = 1F;
    private static final Float UPDATED_CLEANLINESS = 2F;

    private static final Float DEFAULT_ROOM_CONFORT = 1F;
    private static final Float UPDATED_ROOM_CONFORT = 2F;

    private static final Float DEFAULT_LOCATION = 1F;
    private static final Float UPDATED_LOCATION = 2F;

    private static final Float DEFAULT_SERVICE_STAFF = 1F;
    private static final Float UPDATED_SERVICE_STAFF = 2F;

    private static final Float DEFAULT_SLEEP_QUALITY = 1F;
    private static final Float UPDATED_SLEEP_QUALITY = 2F;

    private static final Float DEFAULT_VALUE_PRICE = 1F;
    private static final Float UPDATED_VALUE_PRICE = 2F;

    private static final GGEvaluation DEFAULT_EVALUATION = GGEvaluation.WONDERFULL;
    private static final GGEvaluation UPDATED_EVALUATION = GGEvaluation.NICE;
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_APPROVAL = false;
    private static final Boolean UPDATED_APPROVAL = true;

    private static final Boolean DEFAULT_USER_APPROVAL = false;
    private static final Boolean UPDATED_USER_APPROVAL = true;

    @Inject
    private InstanceReviewRepository instanceReviewRepository;

    @Inject
    private InstanceReviewSearchRepository instanceReviewSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceReviewMockMvc;

    private InstanceReview instanceReview;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceReviewResource instanceReviewResource = new InstanceReviewResource();
        ReflectionTestUtils.setField(instanceReviewResource, "instanceReviewSearchRepository", instanceReviewSearchRepository);
        ReflectionTestUtils.setField(instanceReviewResource, "instanceReviewRepository", instanceReviewRepository);
        this.restInstanceReviewMockMvc = MockMvcBuilders.standaloneSetup(instanceReviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceReviewSearchRepository.deleteAll();
        instanceReview = new InstanceReview();
        instanceReview.setCleanliness(DEFAULT_CLEANLINESS);
        instanceReview.setRoomConfort(DEFAULT_ROOM_CONFORT);
        instanceReview.setLocation(DEFAULT_LOCATION);
        instanceReview.setServiceStaff(DEFAULT_SERVICE_STAFF);
        instanceReview.setSleepQuality(DEFAULT_SLEEP_QUALITY);
        instanceReview.setValuePrice(DEFAULT_VALUE_PRICE);
        instanceReview.setEvaluation(DEFAULT_EVALUATION);
        instanceReview.setTitle(DEFAULT_TITLE);
        instanceReview.setComment(DEFAULT_COMMENT);
        instanceReview.setActive(DEFAULT_ACTIVE);
        instanceReview.setApproval(DEFAULT_APPROVAL);
        instanceReview.setUserApproval(DEFAULT_USER_APPROVAL);
    }

    @Test
    @Transactional
    public void createInstanceReview() throws Exception {
        int databaseSizeBeforeCreate = instanceReviewRepository.findAll().size();

        // Create the InstanceReview

        restInstanceReviewMockMvc.perform(post("/api/instance-reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceReview)))
                .andExpect(status().isCreated());

        // Validate the InstanceReview in the database
        List<InstanceReview> instanceReviews = instanceReviewRepository.findAll();
        assertThat(instanceReviews).hasSize(databaseSizeBeforeCreate + 1);
        InstanceReview testInstanceReview = instanceReviews.get(instanceReviews.size() - 1);
        assertThat(testInstanceReview.getCleanliness()).isEqualTo(DEFAULT_CLEANLINESS);
        assertThat(testInstanceReview.getRoomConfort()).isEqualTo(DEFAULT_ROOM_CONFORT);
        assertThat(testInstanceReview.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testInstanceReview.getServiceStaff()).isEqualTo(DEFAULT_SERVICE_STAFF);
        assertThat(testInstanceReview.getSleepQuality()).isEqualTo(DEFAULT_SLEEP_QUALITY);
        assertThat(testInstanceReview.getValuePrice()).isEqualTo(DEFAULT_VALUE_PRICE);
        assertThat(testInstanceReview.getEvaluation()).isEqualTo(DEFAULT_EVALUATION);
        assertThat(testInstanceReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testInstanceReview.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testInstanceReview.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testInstanceReview.isApproval()).isEqualTo(DEFAULT_APPROVAL);
        assertThat(testInstanceReview.isUserApproval()).isEqualTo(DEFAULT_USER_APPROVAL);

        // Validate the InstanceReview in ElasticSearch
        InstanceReview instanceReviewEs = instanceReviewSearchRepository.findOne(testInstanceReview.getId());
        assertThat(instanceReviewEs).isEqualToComparingFieldByField(testInstanceReview);
    }

    @Test
    @Transactional
    public void getAllInstanceReviews() throws Exception {
        // Initialize the database
        instanceReviewRepository.saveAndFlush(instanceReview);

        // Get all the instanceReviews
        restInstanceReviewMockMvc.perform(get("/api/instance-reviews?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceReview.getId().intValue())))
                .andExpect(jsonPath("$.[*].cleanliness").value(hasItem(DEFAULT_CLEANLINESS.doubleValue())))
                .andExpect(jsonPath("$.[*].roomConfort").value(hasItem(DEFAULT_ROOM_CONFORT.doubleValue())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.doubleValue())))
                .andExpect(jsonPath("$.[*].serviceStaff").value(hasItem(DEFAULT_SERVICE_STAFF.doubleValue())))
                .andExpect(jsonPath("$.[*].sleepQuality").value(hasItem(DEFAULT_SLEEP_QUALITY.doubleValue())))
                .andExpect(jsonPath("$.[*].valuePrice").value(hasItem(DEFAULT_VALUE_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].evaluation").value(hasItem(DEFAULT_EVALUATION.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].approval").value(hasItem(DEFAULT_APPROVAL.booleanValue())))
                .andExpect(jsonPath("$.[*].userApproval").value(hasItem(DEFAULT_USER_APPROVAL.booleanValue())));
    }

    @Test
    @Transactional
    public void getInstanceReview() throws Exception {
        // Initialize the database
        instanceReviewRepository.saveAndFlush(instanceReview);

        // Get the instanceReview
        restInstanceReviewMockMvc.perform(get("/api/instance-reviews/{id}", instanceReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceReview.getId().intValue()))
            .andExpect(jsonPath("$.cleanliness").value(DEFAULT_CLEANLINESS.doubleValue()))
            .andExpect(jsonPath("$.roomConfort").value(DEFAULT_ROOM_CONFORT.doubleValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.doubleValue()))
            .andExpect(jsonPath("$.serviceStaff").value(DEFAULT_SERVICE_STAFF.doubleValue()))
            .andExpect(jsonPath("$.sleepQuality").value(DEFAULT_SLEEP_QUALITY.doubleValue()))
            .andExpect(jsonPath("$.valuePrice").value(DEFAULT_VALUE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.evaluation").value(DEFAULT_EVALUATION.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.approval").value(DEFAULT_APPROVAL.booleanValue()))
            .andExpect(jsonPath("$.userApproval").value(DEFAULT_USER_APPROVAL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceReview() throws Exception {
        // Get the instanceReview
        restInstanceReviewMockMvc.perform(get("/api/instance-reviews/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceReview() throws Exception {
        // Initialize the database
        instanceReviewRepository.saveAndFlush(instanceReview);
        instanceReviewSearchRepository.save(instanceReview);
        int databaseSizeBeforeUpdate = instanceReviewRepository.findAll().size();

        // Update the instanceReview
        InstanceReview updatedInstanceReview = new InstanceReview();
        updatedInstanceReview.setId(instanceReview.getId());
        updatedInstanceReview.setCleanliness(UPDATED_CLEANLINESS);
        updatedInstanceReview.setRoomConfort(UPDATED_ROOM_CONFORT);
        updatedInstanceReview.setLocation(UPDATED_LOCATION);
        updatedInstanceReview.setServiceStaff(UPDATED_SERVICE_STAFF);
        updatedInstanceReview.setSleepQuality(UPDATED_SLEEP_QUALITY);
        updatedInstanceReview.setValuePrice(UPDATED_VALUE_PRICE);
        updatedInstanceReview.setEvaluation(UPDATED_EVALUATION);
        updatedInstanceReview.setTitle(UPDATED_TITLE);
        updatedInstanceReview.setComment(UPDATED_COMMENT);
        updatedInstanceReview.setActive(UPDATED_ACTIVE);
        updatedInstanceReview.setApproval(UPDATED_APPROVAL);
        updatedInstanceReview.setUserApproval(UPDATED_USER_APPROVAL);

        restInstanceReviewMockMvc.perform(put("/api/instance-reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceReview)))
                .andExpect(status().isOk());

        // Validate the InstanceReview in the database
        List<InstanceReview> instanceReviews = instanceReviewRepository.findAll();
        assertThat(instanceReviews).hasSize(databaseSizeBeforeUpdate);
        InstanceReview testInstanceReview = instanceReviews.get(instanceReviews.size() - 1);
        assertThat(testInstanceReview.getCleanliness()).isEqualTo(UPDATED_CLEANLINESS);
        assertThat(testInstanceReview.getRoomConfort()).isEqualTo(UPDATED_ROOM_CONFORT);
        assertThat(testInstanceReview.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testInstanceReview.getServiceStaff()).isEqualTo(UPDATED_SERVICE_STAFF);
        assertThat(testInstanceReview.getSleepQuality()).isEqualTo(UPDATED_SLEEP_QUALITY);
        assertThat(testInstanceReview.getValuePrice()).isEqualTo(UPDATED_VALUE_PRICE);
        assertThat(testInstanceReview.getEvaluation()).isEqualTo(UPDATED_EVALUATION);
        assertThat(testInstanceReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInstanceReview.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testInstanceReview.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testInstanceReview.isApproval()).isEqualTo(UPDATED_APPROVAL);
        assertThat(testInstanceReview.isUserApproval()).isEqualTo(UPDATED_USER_APPROVAL);

        // Validate the InstanceReview in ElasticSearch
        InstanceReview instanceReviewEs = instanceReviewSearchRepository.findOne(testInstanceReview.getId());
        assertThat(instanceReviewEs).isEqualToComparingFieldByField(testInstanceReview);
    }

    @Test
    @Transactional
    public void deleteInstanceReview() throws Exception {
        // Initialize the database
        instanceReviewRepository.saveAndFlush(instanceReview);
        instanceReviewSearchRepository.save(instanceReview);
        int databaseSizeBeforeDelete = instanceReviewRepository.findAll().size();

        // Get the instanceReview
        restInstanceReviewMockMvc.perform(delete("/api/instance-reviews/{id}", instanceReview.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceReviewExistsInEs = instanceReviewSearchRepository.exists(instanceReview.getId());
        assertThat(instanceReviewExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceReview> instanceReviews = instanceReviewRepository.findAll();
        assertThat(instanceReviews).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceReview() throws Exception {
        // Initialize the database
        instanceReviewRepository.saveAndFlush(instanceReview);
        instanceReviewSearchRepository.save(instanceReview);

        // Search the instanceReview
        restInstanceReviewMockMvc.perform(get("/api/_search/instance-reviews?query=id:" + instanceReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].cleanliness").value(hasItem(DEFAULT_CLEANLINESS.doubleValue())))
            .andExpect(jsonPath("$.[*].roomConfort").value(hasItem(DEFAULT_ROOM_CONFORT.doubleValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.doubleValue())))
            .andExpect(jsonPath("$.[*].serviceStaff").value(hasItem(DEFAULT_SERVICE_STAFF.doubleValue())))
            .andExpect(jsonPath("$.[*].sleepQuality").value(hasItem(DEFAULT_SLEEP_QUALITY.doubleValue())))
            .andExpect(jsonPath("$.[*].valuePrice").value(hasItem(DEFAULT_VALUE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].evaluation").value(hasItem(DEFAULT_EVALUATION.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].approval").value(hasItem(DEFAULT_APPROVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].userApproval").value(hasItem(DEFAULT_USER_APPROVAL.booleanValue())));
    }
}
