package com.g200001.dutyapp.web.rest;

import com.g200001.dutyapp.Application;
import com.g200001.dutyapp.domain.BillAccount;
import com.g200001.dutyapp.repository.BillAccountRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
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
 * Test class for the BillAccountResource REST controller.
 *
 * @see BillAccountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BillAccountResourceTest {

    private static final String DEFAULT_COMPANY_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_COMPANY_NAME = "UPDATED_TEXT";

    @Inject
    private BillAccountRepository billAccountRepository;

    private MockMvc restBillAccountMockMvc;

    private BillAccount billAccount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BillAccountResource billAccountResource = new BillAccountResource();
        ReflectionTestUtils.setField(billAccountResource, "billAccountRepository", billAccountRepository);
        this.restBillAccountMockMvc = MockMvcBuilders.standaloneSetup(billAccountResource).build();
    }

    @Before
    public void initTest() {
        billAccount = new BillAccount();
        billAccount.setCompany_name(DEFAULT_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void createBillAccount() throws Exception {
        // Validate the database is empty
        assertThat(billAccountRepository.findAll()).hasSize(0);

        // Create the BillAccount
        restBillAccountMockMvc.perform(post("/api/billAccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billAccount)))
                .andExpect(status().isCreated());

        // Validate the BillAccount in the database
        List<BillAccount> billAccounts = billAccountRepository.findAll();
        assertThat(billAccounts).hasSize(1);
        BillAccount testBillAccount = billAccounts.iterator().next();
        assertThat(testBillAccount.getCompany_name()).isEqualTo(DEFAULT_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllBillAccounts() throws Exception {
        // Initialize the database
        billAccountRepository.saveAndFlush(billAccount);

        // Get all the billAccounts
        restBillAccountMockMvc.perform(get("/api/billAccounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(billAccount.getId()))
                .andExpect(jsonPath("$.[0].company_name").value(DEFAULT_COMPANY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getBillAccount() throws Exception {
        // Initialize the database
        billAccountRepository.saveAndFlush(billAccount);

        // Get the billAccount
        restBillAccountMockMvc.perform(get("/api/billAccounts/{id}", billAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(billAccount.getId()))
            .andExpect(jsonPath("$.company_name").value(DEFAULT_COMPANY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBillAccount() throws Exception {
        // Get the billAccount
        restBillAccountMockMvc.perform(get("/api/billAccounts/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillAccount() throws Exception {
        // Initialize the database
        billAccountRepository.saveAndFlush(billAccount);

        // Update the billAccount
        billAccount.setCompany_name(UPDATED_COMPANY_NAME);
        restBillAccountMockMvc.perform(put("/api/billAccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billAccount)))
                .andExpect(status().isOk());

        // Validate the BillAccount in the database
        List<BillAccount> billAccounts = billAccountRepository.findAll();
        assertThat(billAccounts).hasSize(1);
        BillAccount testBillAccount = billAccounts.iterator().next();
        assertThat(testBillAccount.getCompany_name()).isEqualTo(UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void deleteBillAccount() throws Exception {
        // Initialize the database
        billAccountRepository.saveAndFlush(billAccount);

        // Get the billAccount
        restBillAccountMockMvc.perform(delete("/api/billAccounts/{id}", billAccount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BillAccount> billAccounts = billAccountRepository.findAll();
        assertThat(billAccounts).hasSize(0);
    }
}
