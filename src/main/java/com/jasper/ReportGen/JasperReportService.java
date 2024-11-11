package com.jasper.ReportGen;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private AddressRepository repository;

    public void exportJasperReport(HttpServletResponse response) throws JRException, IOException {
        // Fetch data for the report
        List<Address> addressList = repository.findAll();
        InputStream templateStream = this.getClass().getResourceAsStream("/employee.jrxml");
        if (templateStream == null) {
            throw new FileNotFoundException("Jasper template 'employee.jrxml' not found in classpath");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
//        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a JRBeanCollectionDataSource using the address list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(addressList);

        // Define parameters to pass to the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Your Company Name");

        // Fill the Jasper report with data and parameters
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export the report to the HTTP response output stream
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }
}
