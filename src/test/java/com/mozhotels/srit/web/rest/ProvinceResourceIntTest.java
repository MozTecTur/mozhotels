package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.Province;
import com.mozhotels.srit.repository.ProvinceRepository;
import com.mozhotels.srit.repository.search.ProvinceSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ProvinceResource REST controller.
 *
 * @see ProvinceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProvinceResourceIntTest {

    private static final String DEFAULT_PROVINCE_NAME = "AAAAA";
    private static final String UPDATED_PROVINCE_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_PHOTO_PRINCIPAL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_PRINCIPAL = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/png";

    @Inject
    private ProvinceRepository provinceRepository;

    @Inject
    private ProvinceSearchRepository provinceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProvinceMockMvc;

    private Province province;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProvinceResource provinceResource = new ProvinceResource();
        ReflectionTestUtils.setField(provinceResource, "provinceSearchRepository", provinceSearchRepository);
        ReflectionTestUtils.setField(provinceResource, "provinceRepository", provinceRepository);
        this.restProvinceMockMvc = MockMvcBuilders.standaloneSetup(provinceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        provinceSearchRepository.deleteAll();
        province = new Province();
        province.setProvinceName(DEFAULT_PROVINCE_NAME);
        province.setDescription(DEFAULT_DESCRIPTION);
        province.setPhotoPrincipal(DEFAULT_PHOTO_PRINCIPAL);
        province.setPhotoPrincipalContentType(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // Create the Province

        restProvinceMockMvc.perform(post("/api/provinces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(province)))
                .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinces.get(provinces.size() - 1);
        assertThat(testProvince.getProvinceName()).isEqualTo(DEFAULT_PROVINCE_NAME);
        assertThat(testProvince.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProvince.getPhotoPrincipal()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL);
        assertThat(testProvince.getPhotoPrincipalContentType()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);

        // Validate the Province in ElasticSearch
        Province provinceEs = provinceSearchRepository.findOne(testProvince.getId());
        assertThat(provinceEs).isEqualToComparingFieldByField(testProvince);
    }

    @Test
    @Transactional
    public void checkProvinceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setProvinceName(null);

        // Create the Province, which fails.

        restProvinceMockMvc.perform(post("/api/provinces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(province)))
                .andExpect(status().isBadRequest());

        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProvinces() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinces
        restProvinceMockMvc.perform(get("/api/provinces?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
                .andExpect(jsonPath("$.[*].provinceName").value(hasItem(DEFAULT_PROVINCE_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))));
    }

    @Test
    @Transactional
    public void getProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc.perform(get("/api/provinces/{id}", province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.provinceName").value(DEFAULT_PROVINCE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.photoPrincipalContentType").value(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoPrincipal").value(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL)));
    }

    @Test
    @Transactional
    public void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get("/api/provinces/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        provinceSearchRepository.save(province);
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province
        Province updatedProvince = new Province();
        updatedProvince.setId(province.getId());
        updatedProvince.setProvinceName(UPDATED_PROVINCE_NAME);
        updatedProvince.setDescription(UPDATED_DESCRIPTION);
        updatedProvince.setPhotoPrincipal(UPDATED_PHOTO_PRINCIPAL);
        updatedProvince.setPhotoPrincipalContentType(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);

        restProvinceMockMvc.perform(put("/api/provinces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProvince)))
                .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinces.get(provinces.size() - 1);
        assertThat(testProvince.getProvinceName()).isEqualTo(UPDATED_PROVINCE_NAME);
        assertThat(testProvince.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProvince.getPhotoPrincipal()).isEqualTo(UPDATED_PHOTO_PRINCIPAL);
        assertThat(testProvince.getPhotoPrincipalContentType()).isEqualTo(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);

        // Validate the Province in ElasticSearch
        Province provinceEs = provinceSearchRepository.findOne(testProvince.getId());
        assertThat(provinceEs).isEqualToComparingFieldByField(testProvince);
    }

    @Test
    @Transactional
    public void deleteProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        provinceSearchRepository.save(province);
        int databaseSizeBeforeDelete = provinceRepository.findAll().size();

        // Get the province
        restProvinceMockMvc.perform(delete("/api/provinces/{id}", province.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean provinceExistsInEs = provinceSearchRepository.exists(province.getId());
        assertThat(provinceExistsInEs).isFalse();

        // Validate the database is empty
        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        provinceSearchRepository.save(province);

        // Search the province
        restProvinceMockMvc.perform(get("/api/_search/provinces?query=id:" + province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].provinceName").value(hasItem(DEFAULT_PROVINCE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))));
    }
}
