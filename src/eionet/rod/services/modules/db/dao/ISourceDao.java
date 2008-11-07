package eionet.rod.services.modules.db.dao;

import java.util.List;
import java.util.Vector;

import eionet.rod.dto.InstrumentsDueDTO;
import eionet.rod.services.ServiceException;

public interface ISourceDao {

	/**
	 * Legal Instruments ARRAY of STRUCTs for RDF
	 * @return array of hashes
	 * @throws ServiceException
	 */
	public Vector getInstruments() throws ServiceException;
    
    /**
     * Returns instrument by id
     * @param id
     * @return instrument by id
     * @throws ServiceException
     */
    public Vector getInstrumentById(Integer id) throws ServiceException;

	/**
	 * @return
	 * @throws ServiceException
	 */
	public String[][] getInstrumentsRSS() throws ServiceException;

	
	/**
	 * @param childId
	 * @throws ServiceException
	 */
	public void deleteChildLink(Integer childId) throws ServiceException;
	
	/**
	 * @param parentId
	 * @throws ServiceException
	 */
	public void deleteParentLink(Integer parentId) throws ServiceException;
	
	
	/**
	 * Deletes source from database
	 * @param sourceId
	 * @throws ServiceException
	 */
	public void deleteSource(Integer sourceId) throws ServiceException;
	
	
	/**
	 * Returns instruments ID numbers
	 * @return instruments ID numbers
	 * @throws ServiceException
	 */
	public String[][] getInstrumentIds() throws ServiceException ;	
    
    /**
     * Returns reporting theme name by value
     * @param value
     * @return reporting theme name by value
     * @throws ServiceException
     */
    public String getDGEnvName(String value) throws ServiceException ;  
    
    /**
     * Returns legal instruments sorted on next update
     * @return legal instruments sorted on next update
     * @throws ServiceException
     */
    public List<InstrumentsDueDTO> getInstrumentsDue() throws ServiceException;
	
	
	
}