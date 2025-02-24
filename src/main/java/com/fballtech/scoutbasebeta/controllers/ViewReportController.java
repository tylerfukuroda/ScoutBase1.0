package com.fballtech.scoutbasebeta.controllers;

import java.io.File;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;



import com.fballtech.scoutbasebeta.dao.ReportDAO;
import com.fballtech.scoutbasebeta.entities.Report;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ViewReportController {
	
	@FXML
	private Text firstNameText;
	
	@FXML
	private Text lastNameText;
	
	@FXML
	private Text schoolText;
	
	@FXML
	private Text positionText;
	
	@FXML
	private Text heightText;
	
	@FXML
	private Text weightText;
	
	@FXML
	private Text draftClassText;
	
	@FXML
	private TextField gradeText;
	
	@FXML
	private TextArea summary;
	
	@FXML
	private Button exportButton;
	
	@FXML
	private Button exitButton;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private Button editButton;
	
	private Long reportId;
	
	private Boolean canEdit = false;
	
	Report viewReport;
	
	@FXML
	public void initialize() {
		
	}
	
	public void setReportDetails(Report report) {
		firstNameText.setText(report.getPlayerFirstName());
		System.out.println("Position: " + report.getPlayerPosition().toString());
		lastNameText.setText(report.getPlayerLastName());
		positionText.setText(report.getPlayerPosition().toString());
		schoolText.setText(report.getPlayerSchool());
		heightText.setText(report.getPlayerHeight().toString());
		weightText.setText(report.getPlayerWeight().toString());
		draftClassText.setText(report.getDraftClass().toString());
		summary.setText(report.getSummary());
		gradeText.setText(report.getGrade().toString());
		reportId = report.getId();
		viewReport = report;
	}
	
	public void deleteReport() {
		Report report = new Report();
		report = viewReport;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		System.out.println("Player selected: " + report.getPlayerFirstName());
		alert.setHeaderText("DELETE REPORT CONFIRMATION");
		alert.setContentText("Are you sure you would like to delete this report? This action cannot be reversed.");
		if(alert.showAndWait().get() == ButtonType.OK) {
			ReportDAO.deleteFromServer(report);
			ReportDAO.deleteReport(report);
			Stage stage = (Stage) deleteButton.getScene().getWindow();
			stage.close();
		}else {
			alert.close();
		}
		
		
	}
	
	public void generatePDF() {
		String path = firstNameText.getText() + lastNameText.getText() + "draftreport.pdf";
		Report report = viewReport;
		FileChooser fc = new FileChooser();
		fc.setTitle("Export Draft Report");
		fc.setInitialFileName(path);
		FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
		fc.getExtensionFilters().add(pdfFilter);
		Stage s = new Stage();
		File file = fc.showSaveDialog(s);
		if(file != null) {
			try {
				PdfWriter pdfWriter = new PdfWriter(file.getAbsolutePath());
				PdfDocument pdfDocument = new PdfDocument(pdfWriter);
				pdfDocument.addNewPage();
				Document document = new Document(pdfDocument);
				
				Paragraph header = new Paragraph(String.format("ScoutBase Draft Report: %s %s",
						report.getPlayerFirstName(),
						report.getPlayerLastName()))
						.setFontSize(24)
						.setBold().setHorizontalAlignment(HorizontalAlignment.CENTER)
						.setMarginBottom(20);
				document.add(header)
					.setBold()
					.setFontSize(14);
				
				document.add(new Paragraph("	Position: " + report.getPlayerPosition().toString()))
					.setBold()
					.setFontSize(14);
				document.add(new Paragraph("	Draft Class: " + report.getDraftClass().toString()))
					.setBold()
					.setFontSize(14);
				document.add(new Paragraph("	Height: " + report.getPlayerHeight().toString()))
					.setBold()
					.setFontSize(14);
				document.add(new Paragraph("	Weight: " + report.getPlayerWeight().toString()))
					.setBold()
					.setFontSize(14);
				document.add(new Paragraph("	School: " + report.getPlayerSchool()))
					.setBold()
					.setFontSize(14);
				document.add(new Paragraph("	Evaluation Grade: " + report.getGrade().toString()))
					.setBold()
					.setFontSize(14);
				document.add(new Paragraph("	Date Created: " + report.getDateCreated().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
				document.add(new Paragraph("\n"))
					.setBold()
					.setFontSize(18);
				document.add(new Paragraph("Player Summary:"))
					.setFontSize(12);
				Paragraph summary = new Paragraph(report.getSummary());
				document.add(summary);
					
				
				document.close();
				System.out.println("File saved at: " + file.getAbsolutePath());
				
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		} else {
			System.out.println("Export operation canceled.");
		}
		
	}
	
	@FXML
	public void saveEditedReport() {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("ATTENTION: CONFIRMATION");
			alert.setContentText("Are you sure you would like to save? This will overwrite the previous version of this report.");
			if(alert.showAndWait().get() == ButtonType.OK) {
				viewReport.setGrade(Double.parseDouble(gradeText.getText())); //Refactor this later. Delete this comment when done.
				viewReport.setSummary(summary.getText());
				ReportDAO.updateReportInLocalStorage(reportId, Double.parseDouble(gradeText.getText()), summary.getText());
				ReportDAO.updateReportInServer(viewReport);
				reset();
			} else {
				alert.close();
			}
		
	}
	
	@FXML
	public void makeReportEditable() {
		
			summary.setEditable(true);
			gradeText.setEditable(true);
			exportButton.setVisible(false);
			deleteButton.setVisible(false);
			editButton.setText("SAVE");
			exitButton.setText("CANCEL");
			editButton.setOnAction(e -> saveEditedReport());
			exitButton.setOnAction(e -> reset());
			
	}
	
	@FXML
	public void reset() {
			summary.setEditable(false);
			gradeText.setEditable(false);
			exportButton.setVisible(true);
			deleteButton.setVisible(true);
			editButton.setText("EDIT");
			exitButton.setText("EXIT");
			editButton.setOnAction(e -> makeReportEditable());
			exitButton.setOnAction(e -> closeReport());
	}
	
	@FXML
	public void closeReport() {
			
			Stage stage = (Stage) exitButton.getScene().getWindow();
			stage.close();
		
	}
	
}
