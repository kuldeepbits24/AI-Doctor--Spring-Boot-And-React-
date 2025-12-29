package com.jdoc.hospital.service;

import com.jdoc.hospital.model.FinalDiagnosis;
import com.jdoc.hospital.model.PatientDischargeDetails;
import com.jdoc.hospital.util.JsonUtils;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfService {

  public byte[] buildDischargePdf(PatientDischargeDetails d) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document(PageSize.A4);
    try {
      PdfWriter.getInstance(doc, out);
      doc.open();

      doc.add(new Paragraph("Hospital Management System - Discharge Bill", font(16, true)));
      doc.add(new Paragraph(" "));

      addPair(doc, "Patient Name", d.getPatientName());
      addPair(doc, "Assigned Doctor", d.getAssignedDoctorName());
      addPair(doc, "Address", d.getAddress());
      addPair(doc, "Mobile", d.getMobile());
      addPair(doc, "Symptoms", d.getSymptoms());
      addPair(doc, "Admit Date", String.valueOf(d.getAdmitDate()));
      addPair(doc, "Release Date", String.valueOf(d.getReleaseDate()));
      addPair(doc, "Days Spent", String.valueOf(d.getDaySpent()));

      doc.add(new Paragraph(" "));
      doc.add(new Paragraph("Charges", font(14, true)));
      addPair(doc, "Room Charge", String.valueOf(d.getRoomCharge()));
      addPair(doc, "Medicine Cost", String.valueOf(d.getMedicineCost()));
      addPair(doc, "Doctor Fee", String.valueOf(d.getDoctorFee()));
      addPair(doc, "Other Charge", String.valueOf(d.getOtherCharge()));
      doc.add(new Paragraph(" "));
      addPair(doc, "TOTAL", String.valueOf(d.getTotal()));

      doc.close();
      return out.toByteArray();
    } catch (Exception e) {
      doc.close();
      return new byte[0];
    }
  }

  public byte[] buildPrescriptionPdf(String patientName, String address, String mobile, String doctorName, FinalDiagnosis fd) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document(PageSize.A4);
    try {
      PdfWriter.getInstance(doc, out);
      doc.open();

      doc.add(new Paragraph("Hospital Management System - Prescription", font(16, true)));
      doc.add(new Paragraph(" "));

      addPair(doc, "Patient Name", patientName);
      addPair(doc, "Doctor", doctorName);
      addPair(doc, "Address", address);
      addPair(doc, "Mobile", mobile);
      doc.add(new Paragraph(" "));

      doc.add(new Paragraph("Diagnosis", font(14, true)));
      addPair(doc, "Symptoms", safePretty(fd.getSymptomsJson()));
      addPair(doc, "Diseases", safePretty(fd.getDiseasesJson()));
      addPair(doc, "Medicines", safePretty(fd.getMedicinesJson()));
      addPair(doc, "Medical Tests", safePretty(fd.getMedicalTestsJson()));

      doc.close();
      return out.toByteArray();
    } catch (Exception e) {
      doc.close();
      return new byte[0];
    }
  }

  private void addPair(Document doc, String k, String v) throws DocumentException {
    doc.add(new Paragraph(k + ": " + (v == null ? "" : v), font(11, false)));
  }

  private Font font(int size, boolean bold) {
    return new Font(Font.HELVETICA, size, bold ? Font.BOLD : Font.NORMAL);
  }

  private String safePretty(String json) {
    if (json == null) return "";
    try {
      Object o = JsonUtils.mapper().readValue(json, Object.class);
      if (o instanceof Map<?,?> m) {
        return m.toString();
      }
      return String.valueOf(o);
    } catch (Exception e) {
      return json;
    }
  }
}
