package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.model.Employee;
import ru.gb.model.Project;

import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Modifying
    @Transactional
//    @Query(nativeQuery = true, value = "update projects set name = :name where id = :id")
    @Query("update Project p set p.name = :name where p.id = :id")
    void update(Long id, String name);

    @Query("select e from Employee e")
    Set<Employee> findAllEmployee(Project project);
}
