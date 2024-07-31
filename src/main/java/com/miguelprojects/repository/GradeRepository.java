package com.miguelprojects.repository;

import com.miguelprojects.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {

//    Grade findByStudentNameAndSectionId(String studentName, String section);
//    List<Grade> findByStudentNameContaining(String studentName);
//    List<Grade> findByStudentNameLike(String studentName);

    @Query(value ="SELECT g from Grade g INNER JOIN Student s ON g.studentId = s.studentId" +
            " WHERE s.name = :studentName AND g.sectionId = :section")
    Grade findByStudentNameAndSectionId(String studentName, String section);

    @Query(value="SELECT g.sectionId, AVG(g.score) FROM Grade g GROUP BY g.sectionId ORDER BY AVG(g.score) ASC")
    List<Object[]> findAverageScoreBySection();

    @Query(value="SELECT g.sectionId, AVG(g.score) FROM Grade g GROUP BY g.sectionId HAVING COUNT(*) >= ?1 ORDER BY AVG(g.score)")
    List<Object[]> findAverageScoreBySectionWithCapacity(Long minEnrolled);

    @Query(value="SELECT g.sectionId, AVG(g.score) FROM Grade g GROUP BY g.sectionId HAVING COUNT(*) >= :minEnrolled ORDER BY AVG(g.score)")
    List<Object[]> findAverageScoreBySectionWithCapacity2(@Param("minEnrolled") long minEnrolled);

    @Query(value="SELECT students.name, CAST(AVG(score) AS double) FROM Grades " +
            "INNER JOIN Students ON grades.student_id = students.student_id" +
            " GROUP BY name HAVING AVG(score) < :score  ORDER BY name DESC", nativeQuery=true)
    List<Object[]> findAverageScoreBySectionWithCapacityNative(@Param("score") double score);

    @Query(value="SELECT s.name as Nombre, g.score as Calificacion FROM Grade g " +
            "INNER JOIN Student s ON g.studentId = s.studentId" +
            " WHERE g.score > 50 ORDER BY g.score")
    List<Object[]> findScoreGreaterThan50();

    @Query(value="SELECT s.name as Nombre, g.score as Calificación FROM Grade g " +
            "INNER JOIN Student s ON g.studentId = s.studentId" +
            " WHERE g.score > 70 ORDER BY s.name")
    List<Object[]> findScoreGreaterThan70Sorted();

    @Query(value="SELECT s.name as Nombre, g.sectionId as Seccion FROM Grade g " +
            "INNER JOIN Student s ON g.studentId = s.studentId" +
            " WHERE g.sectionId != 'CS101-A'")
    List<Object[]> studentsExcludingSection();

    @Query("SELECT g.sectionId as Seccion, MAX(g.score) as Calificación FROM Grade g GROUP BY g.sectionId")
    List<Object[]> findMaxScoreBySection();

    @Query("SELECT s.name as Nombre, AVG(g.score) as Calificacion FROM Grade g " +
            "INNER JOIN Student s ON g.studentId = s.studentId" +
            " GROUP BY s.name HAVING AVG(g.score) < ?1")
    List<Object[]> findByAvgScoreLessThan(Double score);
}
