package com.jasper.ReportGen;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/jasperpdf")
public class JasperReportController {

    @Autowired
    private JasperReportService jasperReportService;

    @GetMapping("/export")
    public void createPDF(HttpServletResponse response) throws IOException, JRException {
        // Set the response type as PDF
        response.setContentType("application/pdf");

        // Generate a timestamped filename for the PDF
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=address_report_" + currentDateTime + ".pdf";

        // Add headers to the response
        response.setHeader(headerKey, headerValue);

        // Call the service to generate the PDF
        jasperReportService.exportJasperReport(response);
    }
}
