package uk.com.poodle.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<AppointmentEntity, String> {

    List<AppointmentEntity> findAllByPatientId(String id);

    List<AppointmentEntity> findAllByPatientIdAndDateTimeGreaterThanEqual(String id, LocalDateTime dateTime);

    List<AppointmentEntity> findAllByDateTimeAfter(LocalDateTime dateTime);
}
