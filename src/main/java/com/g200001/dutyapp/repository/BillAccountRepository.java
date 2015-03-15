package com.g200001.dutyapp.repository;

import com.g200001.dutyapp.domain.BillAccount;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BillAccount entity.
 */
public interface BillAccountRepository extends JpaRepository<BillAccount, String> {

}
