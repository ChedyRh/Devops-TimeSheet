package tn.esprit.spring.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.TimesheetPK;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class TimesheetServiceImpl implements ITimesheetService {
	

	@Autowired
	MissionRepository missionRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;
	@Autowired
	EmployeRepository employeRepository;
	
	private static final Logger logger = Logger.getLogger(TimesheetServiceImpl.class);

	
	public int ajouterMission(Mission mission) {
		try {
		logger.debug("lancement de l'ajout  d'une mission!!! ");
		missionRepository.save(mission);
		logger.info("l'ajout est terminé avec succés!!! ");

	}catch (Exception e){
		logger.error("Erreur dans la methode ajouterMission():"+ e);
	}finally {
		logger.info("Methode ajouterMission() est terminée");
	}

		
		return mission.getId();
	}
    
	public void affecterMissionADepartement(int missionId, int depId) {
		try {
			logger.debug("lancement de l'affectation d'une mission");
			
		
			
			Optional<Mission> mission = missionRepository.findById(missionId);
			Optional<Departement> dep = deptRepoistory.findById(depId);
			
			if(mission.isPresent() &&  dep.isPresent()) {
		mission.get().setDepartement(dep.get());
		missionRepository.save(mission.get());}
		logger.info("affectation d'une mission terminé avec succés");}
		catch (Exception e){
			logger.error("Erreur dans la méthode affecterMissisionADepartement():"+ e);
		}finally {
			logger.info("Méthode ajouterMission() términé !!!!");
		}
		
	}

	public void ajouterTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin) {
		TimesheetPK timesheetPK = new TimesheetPK();
		timesheetPK.setDateDebut(dateDebut);
		timesheetPK.setDateFin(dateFin);
		timesheetPK.setIdEmploye(employeId);
		timesheetPK.setIdMission(missionId);
		
		Timesheet timesheet = new Timesheet();
		timesheet.setTimesheetPK(timesheetPK);
		timesheet.setValide(false); //par defaut non valide
		try {
			logger.debug("lancement de l'ajout de Timesheet");
		timesheetRepository.save(timesheet);
		logger.info("ajout terminé avec succés");}
		catch (Exception e){
			logger.error("Erreur dans la methode ajouterTimesheet():"+ e);
		}finally {
			logger.info("Méthode ajouterMission() términé !!!!");
		}
		
	}

	
	public void validerTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin, int validateurId) {
		logger.info("In valider Timesheet");
		Optional<Employe> validateur = employeRepository.findById(validateurId);
		
		
		Optional<Mission> mission = missionRepository.findById(missionId);
		
		if(validateur.isPresent() &&  mission.isPresent()) {
		
		//verifier s'il est un chef de departement (interet des enum)
		if(!validateur.get().getRole().equals(Role.CHEF_DEPARTEMENT)){
			logger.info("l'employe doit etre chef de departement pour valider une feuille de temps !");
			return;
		}
		//verifier s'il est le chef de departement de la mission en question
		boolean chefDeLaMission = false;
		for(Departement dep : validateur.get().getDepartements()){
			if(dep.getId() == mission.get().getDepartement().getId()){
				chefDeLaMission = true;
				break;
			}
		}
		if(!chefDeLaMission){

			logger.info("l'employe doit etre chef de departement de la mission en question");
			return;
		}
//
		TimesheetPK timesheetPK = new TimesheetPK(missionId, employeId, dateDebut, dateFin);
		Timesheet timesheet =timesheetRepository.findBytimesheetPK(timesheetPK);
		timesheet.setValide(true);
		
		//Comment Lire une date de la base de données
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		logger.info("dateDebut : " + dateFormat.format(timesheet.getTimesheetPK().getDateDebut()));
		}
	}

	
	public List<Mission> findAllMissionByEmployeJPQL(int employeId) {
		List<Mission> missions=null;
		try {
			logger.debug("lancement  de l'affichage de la liste des missions avec les employes ");
		missions= timesheetRepository.findAllMissionByEmployeJPQL(employeId);
		logger.info("liste des missons avec les employes !!!");}
		catch (Exception e){
			logger.error("Erreur dans la méthode  findAllMissionByEmployeJPQL(): "+ e);
		}
		return missions;
	}

	
	public List<Employe> getAllEmployeByMission(int missionId) {
		List<Employe> employes=null;
		try {
			logger.debug("lancement de l'affichage de la liste des employes avec missions");
		employes= timesheetRepository.getAllEmployeByMission(missionId);
		logger.info("liste des employes avec leurs missions !!!");}
		catch (Exception e){
			logger.error("Erreur dans la méthode  getAllEmployeByMission(): "+ e);
		}
		return employes;
	}
	}

