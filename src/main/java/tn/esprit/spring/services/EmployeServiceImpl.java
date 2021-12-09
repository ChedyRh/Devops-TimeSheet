package tn.esprit.spring.services;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	private static final Logger logger = Logger.getLogger(EmployeServiceImpl.class);
	public int ajouterEmploye(Employe employe) {
		try {
		logger.debug("lancement de l'ajout  d'un employee ");
		employeRepository.save(employe);
		logger.info("l'ajout est terminé avec succés! ");
		}catch(Exception e) {
			logger.error("Erreur dans la methode ajouterEmploye():"+ e);
		}finally {
			logger.info("Methode ajouterEmploye est terminée");
		}

		return employe.getId();
	}

	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		try {
			logger.debug("lancement de mise ajour de l'email d'un employee par id ");
		Employe employe = employeRepository.findById(employeId).get();
		employe.setEmail(email);
		employeRepository.save(employe);
		logger.info("l'employé "+employe.getNom()+"a changé son email à"+email );
		}catch(Exception e) {
			logger.error("Erreur dans la methode mettreAjourEmailByEmployeId:"+ e);
		}finally {
			logger.info("Methode mettreAjourEmailByEmployeId est terminée sans erreur");
		}

	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		Departement depManagedEntity = deptRepoistory.findById(depId).get();
		Employe employeManagedEntity = employeRepository.findById(employeId).get();

		if(depManagedEntity.getEmployes() == null){
			logger.info("verifier s'il y'a deja une liste d'employe et creation d'une nouvelle s'il n'ya pas");
			List<Employe> employes = new ArrayList<>();
			employes.add(employeManagedEntity);
			depManagedEntity.setEmployes(employes);
		}else{

			depManagedEntity.getEmployes().add(employeManagedEntity);

		}
		logger.info("affecterEmployeADepartement finit avec succes");
	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		logger.debug("essai de desaffecterEmployeDuDepartement");
		Departement dep = deptRepoistory.findById(depId).get();

		int employeNb = dep.getEmployes().size();
		for(int index = 0; index < employeNb; index++){
			if(dep.getEmployes().get(index).getId() == employeId){
				logger.info("l'id fournit doit etre existante");
				dep.getEmployes().remove(index);
				break;//a revoir
			}
		}
	}

	public int ajouterContrat(Contrat contrat) {
		try {
			logger.debug("lancement de l'ajout  d'un contrat ");
		contratRepoistory.save(contrat);
		}catch(Exception e) {
			logger.error("Erreur dans la methode ajouterContrat:"+ e);
		}finally {
			logger.info("Méthode ajouterContrat términé !");
		}
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		logger.debug("on est dans affecterContratAEmploye ");
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		Employe employeManagedEntity = employeRepository.findById(employeId).get();

		contratManagedEntity.setEmploye(employeManagedEntity);
		contratRepoistory.save(contratManagedEntity);
		logger.info("affecterContratAEmploye términé !");
	}

	public String getEmployePrenomById(int employeId) {
		logger.debug("lancement de getEmployePrenomById");
		Employe employeManagedEntity = employeRepository.findById(employeId).get();
		logger.info("selection de l'employe selon l'id et passage au retour de son nom ");
		return employeManagedEntity.getPrenom();
	}
	public void deleteEmployeById(int employeId)
	{
		logger.debug("lancement de la supression de l'employe selon l'id");
		Employe employe = employeRepository.findById(employeId).get();
		logger.info("selection de l'employe selon l'id et apssage a l'etape d e suppression de tous les departement");
		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
		}

		employeRepository.delete(employe);
	}

	public void deleteContratById(int contratId) {
		logger.debug("lancement de la supression d'un contrat selon l'id");
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		contratRepoistory.delete(contratManagedEntity);

	}
	//hello
	public int getNombreEmployeJPQL() {
		logger.debug("lancement de l'affichage de nombre des employes");
		return employeRepository.countemp();
	}
	
	public List<String> getAllEmployeNamesJPQL() {
		logger.debug("lancement de l'affichage de la liste des noms des employes");
		return employeRepository.employeNames();

	}
	
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		List<Employe> employes=null;
		try {
			logger.debug("lancement de l'affichage de la liste des employes selon entreprise");
		employes=employeRepository.getAllEmployeByEntreprisec(entreprise);
		logger.info("liste des employes selon entreprise!");
		}catch (Exception e) {
			logger.error("Erreur dans la méthode   getAllEmployeByEntreprise(): "+ e);
		}
		return employes;
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		logger.debug("nous somme dans mettreAjourEmailByEmployeIdJPQL");
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
		logger.debug("lancement de deleteAllContratJPQL");
         employeRepository.deleteAllContratJPQL();
	}
	
	public float getSalaireByEmployeIdJPQL(int employeId) {
		logger.debug("lancement de getSalaireByEmployeIdJPQL");
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		logger.debug("lancement de getSalaireMoyenByDepartementId");
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}
	
	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		List<Timesheet> timesheets=null;
		try {
			logger.debug("lancement de l'affichage de la liste des timesheets avec missions et date");
		 timesheets = timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
		 logger.info("liste des timesheet avec missions et date !!");
		}catch(Exception e) {logger.error("Erreur dans la méthode  getTimesheetsByMissionAndDate(): "+ e);}
		return timesheets;
	}

	public List<Employe> getAllEmployes() {
		logger.debug("lancement de l'affichage de la liste des employes ");
				return (List<Employe>) employeRepository.findAll();
	}

}
