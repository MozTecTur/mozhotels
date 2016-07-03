package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.InstanceRoomType;
import com.mozhotels.srit.repository.InstanceRoomTypeRepository;
import com.mozhotels.srit.repository.search.InstanceRoomTypeSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the InstanceRoomTypeResource REST controller.
 *
 * @see InstanceRoomTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceRoomTypeResourceIntTest {

    private static final String DEFAULT_INSTANCE_ROOM_TYPE_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_ROOM_TYPE_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_CAPACITY_ADULTS = 1;
    private static final Integer UPDATED_CAPACITY_ADULTS = 2;

    private static final Integer DEFAULT_CAPACITY_CHILDREN = 1;
    private static final Integer UPDATED_CAPACITY_CHILDREN = 2;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final byte[] DEFAULT_PHOTO_PRINCIPAL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_PRINCIPAL = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/png";

    @Inject
    private InstanceRoomTypeRepository instanceRoomTypeRepository;

    @Inject
    private InstanceRoomTypeSearchRepository instanceRoomTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceRoomTypeMockMvc;

    private InstanceRoomType instanceRoomType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceRoomTypeResource instanceRoomTypeResource = new InstanceRoomTypeResource();
        ReflectionTestUtils.setField(instanceRoomTypeResource, "instanceRoomTypeSearchRepository", instanceRoomTypeSearchRepository);
        ReflectionTestUtils.setField(instanceRoomTypeResource, "instanceRoomTypeRepository", instanceRoomTypeRepository);
        this.restInstanceRoomTypeMockMvc = MockMvcBuilders.standaloneSetup(instanceRoomTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceRoomTypeSearchRepository.deleteAll();
        instanceRoomType = new InstanceRoomType();
        instanceRoomType.setInstanceRoomTypeName(DEFAULT_INSTANCE_ROOM_TYPE_NAME);
        instanceRoomType.setDescription(DEFAULT_DESCRIPTION);
        instanceRoomType.setQuantity(DEFAULT_QUANTITY);
        instanceRoomType.setCapacityAdults(DEFAULT_CAPACITY_ADULTS);
        instanceRoomType.setCapacityChildren(DEFAULT_CAPACITY_CHILDREN);
        instanceRoomType.setPrice(DEFAULT_PRICE);
        instanceRoomType.setPhotoPrincipal(DEFAULT_PHOTO_PRINCIPAL);
        instanceRoomType.setPhotoPrincipalContentType(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createInstanceRoomType() throws Exception {
        int databaseSizeBeforeCreate = instanceRoomTypeRepository.findAll().size();

        // Create the InstanceRoomType

        restInstanceRoomTypeMockMvc.perform(post("/api/instance-room-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomType)))
                .andExpect(status().isCreated());

        // Validate the InstanceRoomType in the database
        List<InstanceRoomType> instanceRoomTypes = instanceRoomTypeRepository.findAll();
        assertThat(instanceRoomTypes).hasSize(databaseSizeBeforeCreate + 1);
        InstanceRoomType testInstanceRoomType = instanceRoomTypes.get(instanceRoomTypes.size() - 1);
        assertThat(testInstanceRoomType.getInstanceRoomTypeName()).isEqualTo(DEFAULT_INSTANCE_ROOM_TYPE_NAME);
        assertThat(testInstanceRoomType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInstanceRoomType.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInstanceRoomType.getCapacityAdults()).isEqualTo(DEFAULT_CAPACITY_ADULTS);
        assertThat(testInstanceRoomType.getCapacityChildren()).isEqualTo(DEFAULT_CAPACITY_CHILDREN);
        assertThat(testInstanceRoomType.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testInstanceRoomType.getPhotoPrincipal()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL);
        assertThat(testInstanceRoomType.getPhotoPrincipalContentType()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);

        // Validate the InstanceRoomType in ElasticSearch
        InstanceRoomType instanceRoomTypeEs = instanceRoomTypeSearchRepository.findOne(testInstanceRoomType.getId());
        assertThat(instanceRoomTypeEs).isEqualToComparingFieldByField(testInstanceRoomType);
    }

    @Test
    @Transactional
    public void checkInstanceRoomTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceRoomTypeRepository.findAll().size();
        // set the field null
        instanceRoomType.setInstanceRoomTypeName(null);

        // Create the InstanceRoomType, which fails.

        restInstanceRoomTypeMockMvc.perform(post("/api/instance-room-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomType)))
                .andExpect(status().isBadRequest());

        List<InstanceRoomType> instanceRoomTypes = instanceRoomTypeRepository.findAll();
        assertThat(instanceRoomTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceRoomTypeRepository.findAll().size();
        // set the field null
        instanceRoomType.setPrice(null);

        // Create the InstanceRoomType, which fails.

        restInstanceRoomTypeMockMvc.perform(post("/api/instance-room-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceRoomType)))
                .andExpect(status().isBadRequest());

        List<InstanceRoomType> instanceRoomTypes = instanceRoomTypeRepository.findAll();
        assertThat(instanceRoomTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceRoomTypes() throws Exception {
        // Initialize the database
        instanceRoomTypeRepository.saveAndFlush(instanceRoomType);

        // Get all the instanceRoomTypes
        restInstanceRoomTypeMockMvc.perform(get("/api/instance-room-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceRoomType.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceRoomTypeName").value(hasItem(DEFAULT_INSTANCE_ROOM_TYPE_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].capacityAdults").value(hasItem(DEFAULT_CAPACITY_ADULTS)))
                .andExpect(jsonPath("$.[*].capacityChildren").value(hasItem(DEFAULT_CAPACITY_CHILDREN)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))));
    }

    @Test
    @Transactional
    public void getInstanceRoomType() throws Exception {
        // Initialize the database
        instanceRoomTypeRepository.saveAndFlush(instanceRoomType);

        // Get the instanceRoomType
        restInstanceRoomTypeMockMvc.perform(get("/api/instance-room-types/{id}", instanceRoomType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceRoomType.getId().intValue()))
            .andExpect(jsonPath("$.instanceRoomTypeName").value(DEFAULT_INSTANCE_ROOM_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.capacityAdults").value(DEFAULT_CAPACITY_ADULTS))
            .andExpect(jsonPath("$.capacityChildren").value(DEFAULT_CAPACITY_CHILDREN))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.photoPrincipalContentType").value(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoPrincipal").value(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL)));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceRoomType() throws Exception {
        // Get the instanceRoomType
        restInstanceRoomTypeMockMvc.perform(get("/api/instance-room-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceRoomType() throws Exception {
        // Initialize the database
        instanceRoomTypeRepository.saveAndFlush(instanceRoomType);
        instanceRoomTypeSearchRepository.save(instanceRoomType);
        int databaseSizeBeforeUpdate = instanceRoomTypeRepository.findAll().size();

        // Update the instanceRoomType
        InstanceRoomType updatedInstanceRoomType = new InstanceRoomType();
        updatedInstanceRoomType.setId(instanceRoomType.getId());
        updatedInstanceRoomType.setInstanceRoomTypeName(UPDATED_INSTANCE_ROOM_TYPE_NAME);
        updatedInstanceRoomType.setDescription(UPDATED_DESCRIPTION);
        updatedInstanceRoomType.setQuantity(UPDATED_QUANTITY);
        updatedInstanceRoomType.setCapacityAdults(UPDATED_CAPACITY_ADULTS);
        updatedInstanceRoomType.setCapacityChildren(UPDATED_CAPACITY_CHILDREN);
        updatedInstanceRoomType.setPrice(UPDATED_PRICE);
        updatedInstanceRoomType.setPhotoPrincipal(UPDATED_PHOTO_PRINCIPAL);
        updatedInstanceRoomType.setPhotoPrincipalContentType(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);

        restInstanceRoomTypeMockMvc.perform(put("/api/instance-room-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceRoomType)))
                .andExpect(status().isOk());

        // Validate the InstanceRoomType in the database
        List<InstanceRoomType> instanceRoomTypes = instanceRoomTypeRepository.findAll();
        assertThat(instanceRoomTypes).hasSize(databaseSizeBeforeUpdate);
        InstanceRoomType testInstanceRoomType = instanceRoomTypes.get(instanceRoomTypes.size() - 1);
        assertThat(testInstanceRoomType.getInstanceRoomTypeName()).isEqualTo(UPDATED_INSTANCE_ROOM_TYPE_NAME);
        assertThat(testInstanceRoomType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstanceRoomType.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInstanceRoomType.getCapacityAdults()).isEqualTo(UPDATED_CAPACITY_ADULTS);
        assertThat(testInstanceRoomType.getCapacityChildren()).isEqualTo(UPDATED_CAPACITY_CHILDREN);
        assertThat(testInstanceRoomType.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testInstanceRoomType.getPhotoPrincipal()).isEqualTo(UPDATED_PHOTO_PRINCIPAL);
        assertThat(testInstanceRoomType.getPhotoPrincipalContentType()).isEqualTo(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);

        // Validate the InstanceRoomType in ElasticSearch
        InstanceRoomType instanceRoomTypeEs = instanceRoomTypeSearchRepository.findOne(testInstanceRoomType.getId());
        assertThat(instanceRoomTypeEs).isEqualToComparingFieldByField(testInstanceRoomType);
    }

    @Test
    @Transactional
    public void deleteInstanceRoomType() throws Exception {
        // Initialize the database
        instanceRoomTypeRepository.saveAndFlush(instanceRoomType);
        instanceRoomTypeSearchRepository.save(instanceRoomType);
        int databaseSizeBeforeDelete = instanceRoomTypeRepository.findAll().size();

        // Get the instanceRoomType
        restInstanceRoomTypeMockMvc.perform(delete("/api/instance-room-types/{id}", instanceRoomType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceRoomTypeExistsInEs = instanceRoomTypeSearchRepository.exists(instanceRoomType.getId());
        assertThat(instanceRoomTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceRoomType> instanceRoomTypes = instanceRoomTypeRepository.findAll();
        assertThat(instanceRoomTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceRoomType() throws Exception {
        // Initialize the database
        instanceRoomTypeRepository.saveAndFlush(instanceRoomType);
        instanceRoomTypeSearchRepository.save(instanceRoomType);

        // Search the instanceRoomType
        restInstanceRoomTypeMockMvc.perform(get("/api/_search/instance-room-types?query=id:" + instanceRoomType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceRoomType.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceRoomTypeName").value(hasItem(DEFAULT_INSTANCE_ROOM_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].capacityAdults").value(hasItem(DEFAULT_CAPACITY_ADULTS)))
            .andExpect(jsonPath("$.[*].capacityChildren").value(hasItem(DEFAULT_CAPACITY_CHILDREN)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))));
    }
}
