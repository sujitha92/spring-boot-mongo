package com.spring.springbootmongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/student"))
public class StudentController {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@PostMapping
	public void saveStudent(@RequestBody Student student) {
		
		studentRepository.findStudentByEmail(student.getEmail())
		.ifPresentOrElse(s->{System.out.println("Email already present");}, 
				()->studentRepository.save(student));
		
	}
	@GetMapping
	public List<Student> getAllStudents() {
		
		return studentRepository.findAll();
		
	}
	
	@DeleteMapping(path="{studentId}")
	public void deleteStudent(@PathVariable("studentId") String id) {
		studentRepository.deleteById(id);
	}
	
	private void mongoTemplateQuery(Student student, String email) {
		
		Query query =new Query();
		query.addCriteria(Criteria.where("email").is(email));
		
		List<Student> students = mongoTemplate.find(query, Student.class);
		
		if(students.isEmpty())
			studentRepository.save(student);
		
	}

}
