package eionet.rod.services.modules.db.dao;

import java.util.List;
import java.util.Vector;

import eionet.rod.dto.HierarchyInstrumentDTO;
import eionet.rod.dto.InstrumentFactsheetDTO;
import eionet.rod.dto.InstrumentsDueDTO;
import eionet.rod.dto.InstrumentsListDTO;
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
    
    /**
     * Returns instrument factsheet by id
     * @param id
     * @return instrument factsheet by id
     * @throws ServiceException
     */
    public InstrumentFactsheetDTO getInstrumentFactsheet(String id) throws ServiceException;
    
    /**
     * Returns dgenv name by instrument id
     * @param id
     * @return dgenv name by instrument id
     * @throws ServiceException
     */
    public String getDGEnvNameByInstrumentId(String id) throws ServiceException;
    
    /**
     * Returns instrument for hierarchy
     * @param id
     * @throws ServiceException
     */
    public InstrumentsListDTO getHierarchyInstrument(String id) throws ServiceException;
    
    /**
     * Returns instruments hierarchy
     * @param id
     * @param hasParent
     * @param mode
     * @throws ServiceException
     */
    public String getHierarchy(String id, boolean hasParent, String mode) throws ServiceException;
    
    /**
     * Returns hierarchy instruments
     * @param id
     * @throws ServiceException
     */
    public List<HierarchyInstrumentDTO> getHierarchyInstruments(String id) throws ServiceException;
	
}