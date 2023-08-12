package cat.soft.src.parking.repository;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
	Report findReportByTimeAfter(ZonedDateTime time);

	Integer countBysuspect(Integer suspect);
}
