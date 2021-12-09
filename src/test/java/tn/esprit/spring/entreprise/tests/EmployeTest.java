package tn.esprit.spring.entreprise.tests;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import tn.esprit.spring.controller.RestControlEmploye;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Contrat;


@SpringBootTest
@RunWith(SpringRunner.class)
public class EmployeTest {
	@Autowired
    RestControlEmploye controllerEmploye;
	Integer a,b;
	
	@Test
    public void testAjouterEmploye(){

		 Employe employe= controllerEmploye.ajouterEmploye (new Employe("Chedy","Rhaiem","chedy.rhaiem@esprit.tn",true,Role.ADMINISTRATEUR ));
		 assertNotEquals(employe.getId(),0);
		 controllerEmploye.deleteEmployeById(employe.getId());
    }
	/*@Test 
	public void testMettreAjourEmailByEmployeId() {
		Employe employe= controllerEmploye.ajouterEmploye (new Employe("Chedi","Rhaiem","chedy.rhaiemeu@esprit.tn",true,Role.ADMINISTRATEUR ));
		controllerEmploye.mettreAjourEmailByEmployeId("chedy.rhaiem2@esprit.tn", employe.getId());
		
		}*/
		
	/*@Test 
	public void testAffecterEmployeADepartement() {
		controllerEmploye.affecterEmployeADepartement(1, 5);
		controllerEmploye.affecterEmployeADepartement(2, 1);
	}*/
	/*@Test
	public void testDesaffecterEmployeDuDepartement() {
		controllerEmploye.desaffecterEmployeDuDepartement(2, 1);
	}*/
	@Test 
	public void testAjouterContrat() {
		Date  dateDebut = new Date( "08/07/2021" );
		
		Contrat contrat = controllerEmploye.ajouterContrat(new Contrat(dateDebut,"CDI",1202));
		assertNotEquals(contrat.getReference(),0);
		controllerEmploye.deleteContratById(contrat.getReference());
	}
	@Test
	//update contract
	public void testAffecterContratAEmploye() {
		Date  dateDebut = new Date( "08/07/2021" );
		Employe employe= controllerEmploye.ajouterEmploye (new Employe("Chedi","Rhaiem","chedy.rhaiemeu@esprit.tn",true,Role.ADMINISTRATEUR ));
		Contrat contrat = controllerEmploye.ajouterContrat(new Contrat(dateDebut,"CDI",1252));
		controllerEmploye.affecterContratAEmploye(contrat.getReference(), employe.getId());
		
		//controllerEmploye.deleteEmployeById(employe.getId());
		
	}
	@Test
	public void testGetEmployePrenomById() {
		Employe employe= controllerEmploye.ajouterEmploye (new Employe("Mejd","Rhaiem","mejd.rhaiemeu@gmail.tn",true,Role.ADMINISTRATEUR ));
		assertEquals("Rhaiem",controllerEmploye.getEmployePrenomById(employe.getId()));
		controllerEmploye.deleteEmployeById(employe.getId());
	}
	
	
	@Test 
	public void testGetNombreEmployeJPQL() {
		controllerEmploye.getNombreEmployeJPQL();
	}
	@Test 
	public void testGetAllEmployeNamesJPQL() {
		controllerEmploye.getAllEmployeNamesJPQL();
	}
	/*@Test 
	public void testGetAllEmployeByEntreprise() {
		controllerEmploye.getAllEmployeByEntreprise(1);
	}
	@Test */
	public void testMettreAjourEmailByEmployeIdJPQL() {
		controllerEmploye.mettreAjourEmailByEmployeIdJPQL("shadyrhaiem@gmail.com", 1);;
	}
	@Test
	public void testGetSalaireMoyenByDepartementId() {
		controllerEmploye.getSalaireMoyenByDepartementId(1);
	}
	@Test
	public void testDeleteEmployeById() {
		Employe employe= controllerEmploye.ajouterEmploye (new Employe("Chedi","Rh","chedy.rhaiemeu@esprit.tn",true,Role.ADMINISTRATEUR ));
		assertNotEquals(employe.getId(),0);
		controllerEmploye.deleteEmployeById(employe.getId());
		
	}
	@Test
	public void testDeleteContratById() {
		Date  dateDebut = new Date( "08/07/2021" );
		Contrat contrat = controllerEmploye.ajouterContrat(new Contrat(dateDebut,"CDF",2200));
		assertNotEquals(contrat.getReference(),0);
		controllerEmploye.deleteContratById(contrat.getReference());
		
		
	}
}
