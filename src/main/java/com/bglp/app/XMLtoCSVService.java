package com.bglp.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.bglp.jaxb.Patient;

@Service
public class XMLtoCSVService {

	private Map<String, String> dataset;

	private static final String COMMA = ",";

	@Value("${dataset.input.location}")
	private String datasetInputLoc;

	@Value("${dataset.output.dir}")
	private String datasetOutputDir;

	@EventListener(ApplicationReadyEvent.class)
	public boolean XMLtoCSV() {
		// Initizalize Dataset Map
		dataset = new HashMap<String, String>();

		try {
			Patient patient = readFromXML();

			processPatientData(patient);
			processAccelerationData(patient);
			processBasalData(patient);
			processBasisGSRData(patient);
			processBasisSkinTempData(patient);
			processFingerStickData(patient);
			processGlucoseLevels(patient);
			processMealData(patient);

			processSleepData(patient);
		} catch (Exception e) {
			// An exception occured while reading the XML
			e.printStackTrace();
		}

//		FileUtils.writeStringToFile(new File(), sb.app);

		return true;
	}

	private Patient readFromXML() throws Exception {
		// Read from XML
		JAXBContext jaxbContext = JAXBContext.newInstance(Patient.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		InputStream inStream = new FileInputStream(datasetInputLoc);

		return (Patient) jaxbUnmarshaller.unmarshal(inStream);
	}

	private void processPatientData(Patient patient) throws IOException {
		int patientId = patient.getId();
		String insulinType = patient.getInsulinType();

		StringBuilder sb = new StringBuilder();
		sb.append("patient_id").append(COMMA);
		sb.append("insulin_type").append(COMMA);
		sb.append(System.lineSeparator());

		sb.append(patientId).append(COMMA);
		sb.append(insulinType).append(COMMA);
		sb.append(System.lineSeparator());

		dataset.put("patient", sb.toString());

		writeToCSVFile("patient");
	}

	private void processGlucoseLevels(Patient patient) throws IOException {
		List<Patient.GlucoseLevel.Event> eventList = patient.getGlucoseLevel().getEvent();

		StringBuilder sb = new StringBuilder();
		// sb.append("patient_id").append(COMMA);
		sb.append("ts").append(COMMA);
		sb.append("glucose_level_val").append(COMMA);
		sb.append(System.lineSeparator());

		for (Patient.GlucoseLevel.Event event : eventList) {
			// sb.append(patient.getId()).append(COMMA);
			sb.append(event.getTs()).append(COMMA);
			sb.append(event.getValue()).append(COMMA);
			sb.append(System.lineSeparator());
		}

		dataset.put("glucose_level", sb.toString());

		writeToCSVFile("glucose_level");
	}

	private void processFingerStickData(Patient patient) throws IOException {
		List<Patient.FingerStick.Event> eventList = patient.getFingerStick().getEvent();

		StringBuilder sb = new StringBuilder();
		// sb.append("patient_id").append(COMMA);
		sb.append("ts").append(COMMA);
		sb.append("finger_stick_val").append(COMMA);
		sb.append(System.lineSeparator());

		for (Patient.FingerStick.Event event : eventList) {
			// sb.append(patient.getId()).append(COMMA);
			sb.append(event.getTs()).append(COMMA);
			sb.append(event.getValue()).append(COMMA);
			sb.append(System.lineSeparator());
		}

		dataset.put("finger_stick", sb.toString());

		writeToCSVFile("finger_stick");
	}

	private void processBasalData(Patient patient) throws IOException {
		List<Patient.Basal.Event> eventList = patient.getBasal().getEvent();

		StringBuilder sb = new StringBuilder();
		// sb.append("patient_id").append(COMMA);
		sb.append("ts").append(COMMA);
		sb.append("basal_val").append(COMMA);
		sb.append(System.lineSeparator());

		for (Patient.Basal.Event event : eventList) {
			// sb.append(patient.getId()).append(COMMA);
			sb.append(event.getTs()).append(COMMA);
			sb.append(event.getValue()).append(COMMA);
			sb.append(System.lineSeparator());
		}

		dataset.put("basal", sb.toString());

		writeToCSVFile("basal");
	}

	private void processSleepData(Patient patient) throws IOException {
		List<Patient.Sleep.Event> eventList = patient.getSleep().getEvent();

		StringBuilder sb = new StringBuilder();
		// sb.append("patient_id").append(COMMA);
		sb.append("ts_begin").append(COMMA);
		sb.append("ts_end").append(COMMA);
		sb.append("quality").append(COMMA);
		sb.append(System.lineSeparator());

		for (Patient.Sleep.Event event : eventList) {
			// sb.append(patient.getId()).append(COMMA);
			sb.append(event.getTsBegin()).append(COMMA);
			sb.append(event.getTsEnd()).append(COMMA);
			sb.append(event.getQuality()).append(COMMA);
			sb.append(System.lineSeparator());
		}

		dataset.put("sleep", sb.toString());

		writeToCSVFile("sleep");
	}

	private void processMealData(Patient patient) throws IOException {
		List<Patient.Meal.Event> eventList = patient.getMeal().getEvent();

		StringBuilder sb = new StringBuilder();
		// sb.append("patient_id").append(COMMA);
		sb.append("ts").append(COMMA);
		sb.append("meal_type").append(COMMA);
		sb.append("meal_carbs").append(COMMA);
		sb.append(System.lineSeparator());

		for (Patient.Meal.Event event : eventList) {
			// sb.append(patient.getId()).append(COMMA);
			sb.append(event.getTs()).append(COMMA);
			sb.append(event.getType()).append(COMMA);
			sb.append(event.getCarbs()).append(COMMA);
			sb.append(System.lineSeparator());
		}

		dataset.put("meal", sb.toString());

		writeToCSVFile("meal");
	}

	private void processBasisGSRData(Patient patient) throws IOException {
		List<Patient.BasisGsr.Event> eventList = patient.getBasisGsr().getEvent();

		StringBuilder sb = new StringBuilder();
		// sb.append("patient_id").append(COMMA);
		sb.append("ts").append(COMMA);
		sb.append("basis_gsr_val").append(COMMA);
		sb.append(System.lineSeparator());

		for (Patient.BasisGsr.Event event : eventList) {
			// sb.append(patient.getId()).append(COMMA);
			sb.append(event.getTs()).append(COMMA);
			sb.append(event.getValue()).append(COMMA);
			sb.append(System.lineSeparator());
		}

		dataset.put("basis_gsr", sb.toString());

		writeToCSVFile("basis_gsr");
	}

	private void processBasisSkinTempData(Patient patient) throws IOException {
		List<Patient.BasisSkinTemperature.Event> eventList = patient.getBasisSkinTemperature().getEvent();

		StringBuilder sb = new StringBuilder();
		// sb.append("patient_id").append(COMMA);
		sb.append("ts").append(COMMA);
		sb.append("basis_skin_temperature_val").append(COMMA);
		sb.append(System.lineSeparator());

		for (Patient.BasisSkinTemperature.Event event : eventList) {
			// sb.append(patient.getId()).append(COMMA);
			sb.append(event.getTs()).append(COMMA);
			sb.append(event.getValue()).append(COMMA);
			sb.append(System.lineSeparator());
		}

		dataset.put("basis_skin_temperature", sb.toString());

		writeToCSVFile("basis_skin_temperature");

	}

	private void processAccelerationData(Patient patient) throws IOException {
		List<Patient.Acceleration.Event> eventList = patient.getAcceleration().getEvent();

		StringBuilder sb = new StringBuilder();
		// sb.append("patient_id").append(COMMA);
		sb.append("ts").append(COMMA);
		sb.append("acceleration_val").append(COMMA);
		sb.append(System.lineSeparator());

		for (Patient.Acceleration.Event event : eventList) {
			// sb.append(patient.getId()).append(COMMA);
			sb.append(event.getTs()).append(COMMA);
			sb.append(event.getValue()).append(COMMA);
			sb.append(System.lineSeparator());
		}

		dataset.put("acceleration", sb.toString());

		writeToCSVFile("acceleration");
	}

	private void writeToCSVFile(String label) throws IOException {
		// Write data to CSV
		FileUtils.writeStringToFile(new File(datasetOutputDir + File.separator + label + ".csv"), dataset.get(label),
				StandardCharsets.UTF_8, false);
	}
}
