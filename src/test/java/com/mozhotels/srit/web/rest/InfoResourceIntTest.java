package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.Info;
import com.mozhotels.srit.repository.InfoRepository;
import com.mozhotels.srit.repository.search.InfoSearchRepository;

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
 * Test class for the InfoResource REST controller.
 *
 * @see InfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InfoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private InfoRepository infoRepository;

    @Inject
    private InfoSearchRepository infoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInfoMockMvc;

    private Info info;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InfoResource infoResource = new InfoResource();
        ReflectionTestUtils.setField(infoResource, "infoSearchRepository", infoSearchRepository);
        ReflectionTestUtils.setField(infoResource, "infoRepository", infoRepository);
        this.restInfoMockMvc = MockMvcBuilders.standaloneSetup(infoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        infoSearchRepository.deleteAll();
        info = new Info();
        info.setName(DEFAULT_NAME);
        info.setValue(DEFAULT_VALUE);
        info.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInfo() throws Exception {
        int databaseSizeBeforeCreate = infoRepository.findAll().size();

        // Create the Info

        restInfoMockMvc.perform(post("/api/infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(info)))
                .andExpect(status().isCreated());

        // Validate the Info in the database
        List<Info> infos = infoRepository.findAll();
        assertThat(infos).hasSize(databaseSizeBeforeCreate + 1);
        Info testInfo = infos.get(infos.size() - 1);
        assertThat(testInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInfo.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testInfo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Info in ElasticSearch
        Info infoEs = infoSearchRepository.findOne(testInfo.getId());
        assertThat(infoEs).isEqualToComparingFieldByField(testInfo);
    }

    @Test
    @Transactional
    public void getAllInfos() throws Exception {
        // Initialize the database
        infoRepository.saveAndFlush(info);

        // Get all the infos
        restInfoMockMvc.perform(get("/api/infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(info.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInfo() throws Exception {
        // Initialize the database
        infoRepository.saveAndFlush(info);

        // Get the info
        restInfoMockMvc.perform(get("/api/infos/{id}", info.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(info.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInfo() throws Exception {
        // Get the info
        restInfoMockMvc.perform(get("/api/infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInfo() throws Exception {
        // Initialize the database
        infoRepository.saveAndFlush(info);
        infoSearchRepository.save(info);
        int databaseSizeBeforeUpdate = infoRepository.findAll().size();

        // Update the info
        Info updatedInfo = new Info();
        updatedInfo.setId(info.getId());
        updatedInfo.setName(UPDATED_NAME);
        updatedInfo.setValue(UPDATED_VALUE);
        updatedInfo.setDescription(UPDATED_DESCRIPTION);

        restInfoMockMvc.perform(put("/api/infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInfo)))
                .andExpect(status().isOk());

        // Validate the Info in the database
        List<Info> infos = infoRepository.findAll();
        assertThat(infos).hasSize(databaseSizeBeforeUpdate);
        Info testInfo = infos.get(infos.size() - 1);
        assertThat(testInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInfo.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testInfo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Info in ElasticSearch
        Info infoEs = infoSearchRepository.findOne(testInfo.getId());
        assertThat(infoEs).isEqualToComparingFieldByField(testInfo);
    }

    @Test
    @Transactional
    public void deleteInfo() throws Exception {
        // Initialize the database
        infoRepository.saveAndFlush(info);
        infoSearchRepository.save(info);
        int databaseSizeBeforeDelete = infoRepository.findAll().size();

        // Get the info
        restInfoMockMvc.perform(delete("/api/infos/{id}", info.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean infoExistsInEs = infoSearchRepository.exists(info.getId());
        assertThat(infoExistsInEs).isFalse();

        // Validate the database is empty
        List<Info> infos = infoRepository.findAll();
        assertThat(infos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInfo() throws Exception {
        // Initialize the database
        infoRepository.saveAndFlush(info);
        infoSearchRepository.save(info);

        // Search the info
        restInfoMockMvc.perform(get("/api/_search/infos?query=id:" + info.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(info.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
