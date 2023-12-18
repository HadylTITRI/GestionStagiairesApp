package com.example.stagaireservice;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Stagiaire{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nom;
	private String prenom;

	@Temporal(TemporalType.DATE)
	private Date dateNaissance;

	private String adresseMail;

}

@Repository
interface StagiaireRepository extends JpaRepository<Stagiaire, Long>{
	@RestResource(path="byNom")
	public List<Stagiaire> findByNomContains(@Param("nom") String nom);
	//public List<Stagiaire> findByMailContains(@Param("adresseMail") String mail);
}
@CrossOrigin("*")
@RestController
class stagiaireRestConroller{
	private StagiaireRepository stagiaireRepository;
	public stagiaireRestConroller(StagiaireRepository stagiaireRepository){
		this.stagiaireRepository = stagiaireRepository;
	}
	@GetMapping (path="/stagiaires")
	public List<Stagiaire> getAllStagiaires() {
		return stagiaireRepository.findAll();
	}

	@PostMapping(value = "/stagiaires")
	public Stagiaire save(@RequestBody Stagiaire stg){
		return stagiaireRepository.save(stg);
	}
	@GetMapping(value ="/stgiaires/{id}")
	public Stagiaire getStagiaire(@PathVariable(name = "id") Long id){
		return stagiaireRepository.findById(id).get();
	}
	@GetMapping(path="/stagiaires/searchByName")
	public List<Stagiaire> findByNom(@RequestParam("name") String nom){
		return stagiaireRepository.findByNomContains(nom);
	}
	@DeleteMapping(value="stagiaires/{id}")
	public void delete(@PathVariable(name="id") Long id){
		stagiaireRepository.deleteById(id);
	}
	@PutMapping(value="/stagaires/{id}")
	public Stagiaire updateStagiaire(@PathVariable(name="id") Long id, @RequestBody Stagiaire stg){
		stg.setId(id);
		return stagiaireRepository.save(stg);
	}
}

@SpringBootApplication
public class StagiaireServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(StagiaireServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner star(StagiaireRepository stagiaireRepository) {
		return args -> {
			stagiaireRepository.save(new Stagiaire(null, "Aichaoui", "Aicha",new Date("22/12/1999"), "aicha1.aicha1@adressemail.com"));
			stagiaireRepository.save(new Stagiaire(null, "Mohamed", "Ahmed", new Date(06/05/1998), "mohamed.ahmed@adressemail.com"));
			stagiaireRepository.save(new Stagiaire(null, "Gues", "Bilal", new Date("18-08-1995"), "gues.bilal@adressemail.com"));
			stagiaireRepository.findAll().forEach(
					stagiaire -> {
						System.out.println(stagiaire.toString());
					}
			);
		};
	}
}
