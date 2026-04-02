package ru.skriplex.springhotelapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skriplex.springhotelapplication.entity.Hotel;

import java.util.Optional;


public interface HotelRepository extends JpaRepository<Hotel, Long> {}
