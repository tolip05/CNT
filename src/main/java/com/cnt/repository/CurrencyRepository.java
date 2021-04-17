package com.cnt.repository;

import com.cnt.domein.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,String> {
 Optional<Currency> findByName(String name);
}
