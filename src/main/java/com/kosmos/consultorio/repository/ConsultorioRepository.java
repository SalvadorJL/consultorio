package com.kosmos.consultorio.repository;

import com.kosmos.consultorio.models.entity.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
}
