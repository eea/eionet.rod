package eionet.rod.services.modules.db.dao;

import java.util.List;

import eionet.rod.dto.DocumentationDTO;
import eionet.rod.dto.HelpDTO;
import eionet.rod.services.ServiceException;

public interface IHelpDao {

    /**
     * @param area_id
     * @return html_text
     * @throws ServiceException
     */
    String getHelpAreaHtml(String area_id) throws ServiceException;

    /**
     * @return documentation list
     * @throws ServiceException
     */
    List<DocumentationDTO> getDocList() throws ServiceException;

    /**
     * @param area_id
     * @return doc
     * @throws ServiceException
     */
    DocumentationDTO getDoc(String area_id) throws ServiceException;

    /**
     * @param help id
     * @return help dto
     * @throws ServiceException
     */
    HelpDTO getHelp(String id) throws ServiceException;

    /**
     * @param help id
     * @return list of help dto's
     * @throws ServiceException
     */
    List<HelpDTO> getHelpList(String id) throws ServiceException;

    /**
     * @param help dto
     * @throws ServiceException
     */
    void editHelp(HelpDTO help) throws ServiceException;
}
