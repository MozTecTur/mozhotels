package com.mozhotels.srit.web.rest;

import com.mozhotels.srit.MozhotelsApp;
import com.mozhotels.srit.domain.Booking;
import com.mozhotels.srit.repository.BookingRepository;
import com.mozhotels.srit.repository.search.BookingSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mozhotels.srit.domain.enumeration.IBookingState;

/**
 * Test class for the BookingResource REST controller.
 *
 * @see BookingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsApp.class)
@WebAppConfiguration
@IntegrationTest
public class BookingResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CHECK_IN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CHECK_IN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CHECK_IN_STR = dateTimeFormatter.format(DEFAULT_CHECK_IN);

    private static final ZonedDateTime DEFAULT_CHECK_OUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CHECK_OUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CHECK_OUT_STR = dateTimeFormatter.format(DEFAULT_CHECK_OUT);

    private static final Integer DEFAULT_PEOPLE_ADULT = 1;
    private static final Integer UPDATED_PEOPLE_ADULT = 2;

    private static final Integer DEFAULT_PEOPLE_CHILD = 1;
    private static final Integer UPDATED_PEOPLE_CHILD = 2;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_EDIT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_EDIT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_EDIT_DATE_STR = dateTimeFormatter.format(DEFAULT_EDIT_DATE);

    private static final IBookingState DEFAULT_STATE = IBookingState.SUBMITED;
    private static final IBookingState UPDATED_STATE = IBookingState.IN_PROGRESS;

    private static final Integer DEFAULT_ROOMS = 1;
    private static final Integer UPDATED_ROOMS = 2;

    private static final Float DEFAULT_TAX = 1F;
    private static final Float UPDATED_TAX = 2F;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    @Inject
    private BookingRepository bookingRepository;

    @Inject
    private BookingSearchRepository bookingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBookingMockMvc;

    private Booking booking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookingResource bookingResource = new BookingResource();
        ReflectionTestUtils.setField(bookingResource, "bookingSearchRepository", bookingSearchRepository);
        ReflectionTestUtils.setField(bookingResource, "bookingRepository", bookingRepository);
        this.restBookingMockMvc = MockMvcBuilders.standaloneSetup(bookingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bookingSearchRepository.deleteAll();
        booking = new Booking();
        booking.setCheckIn(DEFAULT_CHECK_IN);
        booking.setCheckOut(DEFAULT_CHECK_OUT);
        booking.setPeopleAdult(DEFAULT_PEOPLE_ADULT);
        booking.setPeopleChild(DEFAULT_PEOPLE_CHILD);
        booking.setCreatedDate(DEFAULT_CREATED_DATE);
        booking.setEditDate(DEFAULT_EDIT_DATE);
        booking.setState(DEFAULT_STATE);
        booking.setRooms(DEFAULT_ROOMS);
        booking.setTax(DEFAULT_TAX);
        booking.setTotalPrice(DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookings.get(bookings.size() - 1);
        assertThat(testBooking.getCheckIn()).isEqualTo(DEFAULT_CHECK_IN);
        assertThat(testBooking.getCheckOut()).isEqualTo(DEFAULT_CHECK_OUT);
        assertThat(testBooking.getPeopleAdult()).isEqualTo(DEFAULT_PEOPLE_ADULT);
        assertThat(testBooking.getPeopleChild()).isEqualTo(DEFAULT_PEOPLE_CHILD);
        assertThat(testBooking.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBooking.getEditDate()).isEqualTo(DEFAULT_EDIT_DATE);
        assertThat(testBooking.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBooking.getRooms()).isEqualTo(DEFAULT_ROOMS);
        assertThat(testBooking.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testBooking.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);

        // Validate the Booking in ElasticSearch
        Booking bookingEs = bookingSearchRepository.findOne(testBooking.getId());
        assertThat(bookingEs).isEqualToComparingFieldByField(testBooking);
    }

    @Test
    @Transactional
    public void checkCheckInIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setCheckIn(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCheckOutIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setCheckOut(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeopleAdultIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setPeopleAdult(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeopleChildIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setPeopleChild(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setCreatedDate(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setState(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoomsIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setRooms(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setTax(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setTotalPrice(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookings
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
                .andExpect(jsonPath("$.[*].checkIn").value(hasItem(DEFAULT_CHECK_IN_STR)))
                .andExpect(jsonPath("$.[*].checkOut").value(hasItem(DEFAULT_CHECK_OUT_STR)))
                .andExpect(jsonPath("$.[*].peopleAdult").value(hasItem(DEFAULT_PEOPLE_ADULT)))
                .andExpect(jsonPath("$.[*].peopleChild").value(hasItem(DEFAULT_PEOPLE_CHILD)))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].editDate").value(hasItem(DEFAULT_EDIT_DATE_STR)))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
                .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
                .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.checkIn").value(DEFAULT_CHECK_IN_STR))
            .andExpect(jsonPath("$.checkOut").value(DEFAULT_CHECK_OUT_STR))
            .andExpect(jsonPath("$.peopleAdult").value(DEFAULT_PEOPLE_ADULT))
            .andExpect(jsonPath("$.peopleChild").value(DEFAULT_PEOPLE_CHILD))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.editDate").value(DEFAULT_EDIT_DATE_STR))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.rooms").value(DEFAULT_ROOMS))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        bookingSearchRepository.save(booking);
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = new Booking();
        updatedBooking.setId(booking.getId());
        updatedBooking.setCheckIn(UPDATED_CHECK_IN);
        updatedBooking.setCheckOut(UPDATED_CHECK_OUT);
        updatedBooking.setPeopleAdult(UPDATED_PEOPLE_ADULT);
        updatedBooking.setPeopleChild(UPDATED_PEOPLE_CHILD);
        updatedBooking.setCreatedDate(UPDATED_CREATED_DATE);
        updatedBooking.setEditDate(UPDATED_EDIT_DATE);
        updatedBooking.setState(UPDATED_STATE);
        updatedBooking.setRooms(UPDATED_ROOMS);
        updatedBooking.setTax(UPDATED_TAX);
        updatedBooking.setTotalPrice(UPDATED_TOTAL_PRICE);

        restBookingMockMvc.perform(put("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBooking)))
                .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookings.get(bookings.size() - 1);
        assertThat(testBooking.getCheckIn()).isEqualTo(UPDATED_CHECK_IN);
        assertThat(testBooking.getCheckOut()).isEqualTo(UPDATED_CHECK_OUT);
        assertThat(testBooking.getPeopleAdult()).isEqualTo(UPDATED_PEOPLE_ADULT);
        assertThat(testBooking.getPeopleChild()).isEqualTo(UPDATED_PEOPLE_CHILD);
        assertThat(testBooking.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBooking.getEditDate()).isEqualTo(UPDATED_EDIT_DATE);
        assertThat(testBooking.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBooking.getRooms()).isEqualTo(UPDATED_ROOMS);
        assertThat(testBooking.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testBooking.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);

        // Validate the Booking in ElasticSearch
        Booking bookingEs = bookingSearchRepository.findOne(testBooking.getId());
        assertThat(bookingEs).isEqualToComparingFieldByField(testBooking);
    }

    @Test
    @Transactional
    public void deleteBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        bookingSearchRepository.save(booking);
        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Get the booking
        restBookingMockMvc.perform(delete("/api/bookings/{id}", booking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bookingExistsInEs = bookingSearchRepository.exists(booking.getId());
        assertThat(bookingExistsInEs).isFalse();

        // Validate the database is empty
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        bookingSearchRepository.save(booking);

        // Search the booking
        restBookingMockMvc.perform(get("/api/_search/bookings?query=id:" + booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].checkIn").value(hasItem(DEFAULT_CHECK_IN_STR)))
            .andExpect(jsonPath("$.[*].checkOut").value(hasItem(DEFAULT_CHECK_OUT_STR)))
            .andExpect(jsonPath("$.[*].peopleAdult").value(hasItem(DEFAULT_PEOPLE_ADULT)))
            .andExpect(jsonPath("$.[*].peopleChild").value(hasItem(DEFAULT_PEOPLE_CHILD)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
            .andExpect(jsonPath("$.[*].editDate").value(hasItem(DEFAULT_EDIT_DATE_STR)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())));
    }
}
