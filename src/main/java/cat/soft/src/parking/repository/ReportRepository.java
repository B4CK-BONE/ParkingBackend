package cat.soft.src.parking.repository;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
	Report findReportByVictimAndSuspectAndTimeAfter(Long victim, Long suspect, ZonedDateTime time);

	Long countBysuspect(Long suspect);
}
