package cat.soft.src.parking.repository;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import cat.soft.src.parking.model.Report;
import jakarta.persistence.LockModeType;

public interface ReportRepository extends JpaRepository<Report, Long> {
	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	Report findReportByVictimAndSuspectAndTimeAfter(Long victim, Long suspect, ZonedDateTime time);

	Long countBysuspect(Long suspect);
}
