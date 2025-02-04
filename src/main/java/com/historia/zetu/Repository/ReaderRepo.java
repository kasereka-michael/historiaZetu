package com.historia.zetu.Repository;

import com.historia.zetu.model.ReaderInformations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReaderRepo extends JpaRepository<ReaderInformations,Long> {
    boolean existsByCountry(String countryName);
}
