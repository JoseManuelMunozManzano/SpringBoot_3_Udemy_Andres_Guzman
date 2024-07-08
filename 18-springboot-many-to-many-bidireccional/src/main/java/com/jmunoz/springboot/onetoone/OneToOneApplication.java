package com.jmunoz.springboot.onetoone;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.jmunoz.springboot.onetoone.entities.Course;
import com.jmunoz.springboot.onetoone.entities.Student;
import com.jmunoz.springboot.onetoone.repositories.CourseRepository;
import com.jmunoz.springboot.onetoone.repositories.StudentRepository;

@SpringBootApplication
public class OneToOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneToOneApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepository, CourseRepository courseRepository) {

		return runner -> {
			// manyToManyBidireccional(studentRepository);
			// manyToManyBidireccionalRemove(studentRepository, courseRepository);
			// manyToManyBidireccionalFind(studentRepository, courseRepository);
			manyToManyRemoveBidireccionalFind(studentRepository, courseRepository);
		};
	}

	@Transactional
	public void manyToManyBidireccional(StudentRepository studentRepository) {

		Student student1 = new Student("Jano", "Pura");
		Student student2 = new Student("Erba", "Doe");

		Course course1 = new Course("Curso de Java Master", "Andrés");
		Course course2 = new Course("Curso de Spring Boot", "Andrés");

		student1.addCourse(course1);
		student1.addCourse(course2);
		student2.addCourse(course2);

		// Persistiendo al estudiante, en cascada persiste sus cursos.
		// Como es una relación @ManyToMany se creará una tabla intermedia que contenga las relaciones
		// entre estudiantes y cursos.
		studentRepository.saveAll(List.of(student1, student2));

		System.out.println(student1);
		System.out.println(student2);
	}

	// Creando nuevos estudiantes y cursos, y eliminando luego, para el estudiante 3, el curso 3.
	// No olvidar que en resources tenemos el archivo import.sql donde ya crea 2 estudiantes y 2 cursos.
	@Transactional
	public void manyToManyBidireccionalRemove(StudentRepository studentRepository, CourseRepository courseRepository) {

		Student student1 = new Student("Jano", "Pura");
		Student student2 = new Student("Erba", "Doe");

		Course course1 = new Course("Curso de Java Master", "Andrés");
		Course course2 = new Course("Curso de Spring Boot", "Andrés");

		student1.addCourse(course1);
		student1.addCourse(course2);
		student2.addCourse(course2);

		// Persistiendo al estudiante, en cascada persiste sus cursos.
		// Como es una relación @ManyToMany se creará una tabla intermedia que contenga las relaciones
		// entre estudiantes y cursos.
		studentRepository.saveAll(List.of(student1, student2));

		System.out.println(student1);
		System.out.println(student2);

		// Aquí empieza la parte de eliminar
		// Y para que funcione tenemos que: 
		//   Agregar hashCode() y equals() a la clase Course, la dependiente.
		//   Consulta personalizada para traer los cursos y evitar el problema throwLazyInitializationException
		Optional<Student> studentOptionalBd = studentRepository.findOneWithCourses(3L);
		if (studentOptionalBd.isPresent()) {

			Student studentBd = studentOptionalBd.get();
			
			// Para evitar el error throwLazyInitializationException aquí, hay que hacer la consulta personalizada.
			// Recordar que estos errores no suceden cuando trabajamos en un contexto web con @Service.
			Optional<Course> courseOptionalBd = courseRepository.findOneWithStudents(3L);
			if (courseOptionalBd.isPresent()) {
				Course courseBd = courseOptionalBd.get();
				studentBd.removeCourse(courseBd);

				// Recordar que la parte de los courses se hace en cascada.
				studentRepository.save(studentBd);
 
				System.out.println(studentBd);
			}
		}
	}

	// Búsqueda de estudiantes y cursos existentes en BD.
	@Transactional
	public void manyToManyBidireccionalFind(StudentRepository studentRepository, CourseRepository courseRepository) {

		Optional<Student> studentOptional1 = studentRepository.findOneWithCourses(1L);
		Optional<Student> studentOptional2 = studentRepository.findOneWithCourses(2L);

		Student student1 = studentOptional1.get();
		Student student2 = studentOptional2.get();

		Course course1 = courseRepository.findOneWithStudents(1L).get();
		Course course2 = courseRepository.findOneWithStudents(2L).get();

		student1.addCourse(course1);
		student1.addCourse(course2);
		student2.addCourse(course2);

		// Persistiendo al estudiante, en cascada persiste sus cursos.
		// Como es una relación @ManyToMany se creará una tabla intermedia que contenga las relaciones
		// entre estudiantes y cursos.
		studentRepository.saveAll(List.of(student1, student2));

		System.out.println(student1);
		System.out.println(student2);
	}

	// Eliminar curso 2 del estudiante 1
	@Transactional
	public void manyToManyRemoveBidireccionalFind(StudentRepository studentRepository, CourseRepository courseRepository) {

		// To-do esto es para tener datos relacionados.
		Optional<Student> studentOptional1 = studentRepository.findOneWithCourses(1L);
		Optional<Student> studentOptional2 = studentRepository.findOneWithCourses(2L);

		Student student1 = studentOptional1.get();
		Student student2 = studentOptional2.get();

		Course course1 = courseRepository.findOneWithStudents(1L).get();
		Course course2 = courseRepository.findOneWithStudents(2L).get();

		student1.addCourse(course1);
		student1.addCourse(course2);
		student2.addCourse(course2);

		studentRepository.saveAll(List.of(student1, student2));

		System.out.println(student1);
		System.out.println(student2);

		// Aquí empieza la parte de eliminar
		// Y para que funcione tenemos que: 
		//   Agregar hashCode() y equals() a la clase Course, la dependiente.
		//   Consulta personalizada para traer los cursos y evitar el problema throwLazyInitializationException
		Optional<Student> studentOptionalBd = studentRepository.findOneWithCourses(1L);
		if (studentOptionalBd.isPresent()) {

			Student studentBd = studentOptionalBd.get();
			
			Optional<Course> courseOptionalBd = courseRepository.findOneWithStudents(2L);
			if (courseOptionalBd.isPresent()) {
				Course courseBd = courseOptionalBd.get();
				studentBd.getCourses().remove(courseBd);

				// Recordar que la parte de los courses se hace en cascada.
				studentRepository.save(studentBd);
 
				System.out.println(studentBd);
			}
		}
	}
}
