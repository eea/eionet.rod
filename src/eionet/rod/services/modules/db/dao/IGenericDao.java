package eionet.rod.services.modules.db.dao;

import java.util.List;
import java.util.Vector;

import eionet.rod.dto.DocumentationDTO;
import eionet.rod.services.ServiceException;

public interface IGenericDao {

	/**
	 * @param tablename
	 * @return
	 * @throws ServiceException
	 */
	public Vector getTable(String tablename) throws ServiceException;

	/**
	 * @param tablename
	 * @return
	 * @throws ServiceException
	 */
	public Vector getTableDesc(String tablename) throws ServiceException;

	/**
	 * @param id
	 * @param table
	 * @return
	 * @throws ServiceException
	 */
	public boolean isIdAvailable(String id, String table) throws ServiceException ;
	
	
	/**
	 * @return
	 * @throws ServiceException
	 */
	public String getLastUpdate() throws ServiceException ;
	
	/**
	 * @param area_id
	 * @return html_text
	 * @throws ServiceException
	 */
	public String getHelpAreaHtml(String area_id) throws ServiceException;
	
	/**
	 * @return documentation list
	 * @throws ServiceException
	 */
	public List<DocumentationDTO> getDocList() throws ServiceException;
	
	/**
	 * @param area_id
	 * @return doc
	 * @throws ServiceException
	 */
	public DocumentationDTO getDoc(String area_id) throws ServiceException;
}