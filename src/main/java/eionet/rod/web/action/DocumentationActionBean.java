package eionet.rod.web.action;

import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import eionet.rod.RODUtil;
import eionet.rod.dto.DocumentationDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 *
 */
@UrlBinding("/documentation")
public class DocumentationActionBean extends AbstractRODActionBean {

	private List<DocumentationDTO> docList;
	private DocumentationDTO doc;
	private String areaId;
	
	/** 
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		String pathInfo = getContext().getRequest().getPathInfo();
		if(!RODUtil.isNullOrEmpty(pathInfo)){
			StringTokenizer st = new StringTokenizer(pathInfo,"/");
			if(st.hasMoreElements())
				areaId = st.nextToken();
		}
		
		if(!RODUtil.isNullOrEmpty(areaId)){
			doc = RODServices.getDbService().getHelpDao().getDoc(areaId);
		} else {
			docList = RODServices.getDbService().getHelpDao().getDocList();
		}
		
		return new ForwardResolution("/pages/documentation.jsp");
	}

	public List<DocumentationDTO> getDocList() {
		return docList;
	}

	public void setDocList(List<DocumentationDTO> docList) {
		this.docList = docList;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public DocumentationDTO getDoc() {
		return doc;
	}

	public void setDoc(DocumentationDTO doc) {
		this.doc = doc;
	}
		
}
