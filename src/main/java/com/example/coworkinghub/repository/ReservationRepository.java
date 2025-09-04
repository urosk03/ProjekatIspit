package com.example.coworkinghub.repository;

import com.example.coworkinghub.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
           select case when count(r)>0 then true else false end
           from Reservation r
           where r.room = :room
             and r.status in (com.example.coworkinghub.model.ReservationStatus.APPROVED,
                              com.example.coworkinghub.model.ReservationStatus.PENDING)
             and r.startTime < :end
             and r.endTime   > :start
           """)
    boolean overlaps(Room room, LocalDateTime start, LocalDateTime end);

    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByStatus(ReservationStatus status);

    @Query("""
       select r.room.id, count(r)
       from Reservation r
       where r.status = com.example.coworkinghub.model.ReservationStatus.APPROVED
       group by r.room.id
    """)
    List<Object[]> popularityByRoom();
}