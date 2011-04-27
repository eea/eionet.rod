package eionet.rod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import eionet.rod.services.FileServiceIF;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

public class DPSIRValuesFromExcel extends HttpServlet {

    /*
     *  (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void service(HttpServletRequest req, HttpServletResponse res)
                                            throws ServletException, IOException {

        try {

            String fileName = RODServices.getFileService().getStringProperty(FileServiceIF.DPSIR_VALUES_FILE);
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            int rowCnt = sheet.getLastRowNum();
            int cnt = 0;
            for(int i = 0; i < rowCnt; i++) {
                HSSFRow row = sheet.getRow(i+1);
                HSSFCell cellA = row.getCell((short)0);
                HSSFCell cellD = row.getCell((short)3);
                int id = 0;
                String dpsir = null;
                if (cellA != null)
                    id = new Double(cellA.getNumericCellValue()).intValue();
                if (cellD != null)
                    dpsir = cellD.getStringCellValue();
                if (dpsir != null) {
                    int length = dpsir.length();
                    for(int z = 1; z <= length; z++) {
                        String value = dpsir.substring(z-1,z);
                        if (!value.equals(" ") && !value.equals(",")) {
                            RODServices.getDbService().getObligationDao().dpsirValuesFromExcelToDB(id,value);
                        }
                    }
                }
                cnt++;
            }

            res.sendRedirect("index.html");

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
