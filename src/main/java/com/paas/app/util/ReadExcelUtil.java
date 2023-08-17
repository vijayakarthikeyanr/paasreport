package com.paas.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.paas.app.config.S3Properties;
import com.paas.app.model.AppPlatform;
import com.paas.app.model.Response;

@Component
public class ReadExcelUtil {
	@Autowired
	SFTPUtil sftpUtil;

	@Autowired
	private S3Properties s3Properties;

	@Value("${sss.excel.file.name}")
	public String excelFileName;

	private static final String CONNECTION = "Connection";
	private static final String KEEP_ALIVE = "Keep-Alive";
	private static final String TIME_OUT = "timeout=";

	public Response readExcel() {
		ObjectMapper mapper = new ObjectMapper();
		Response response = new Response();
		List<AppPlatform> appPlatformList = Lists.newArrayList();
		// Get first/desired sheet from the workbook
		XSSFSheet sheet = loadExcelFromLocal().getSheetAt(0);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum() != 0) {
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				AppPlatform appPlatform = new AppPlatform();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {
					case STRING:
						if (StringUtils.isNotEmpty(cell.getStringCellValue())) {
							setData(cell, appPlatform);
						}
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							if (null != cell.getDateCellValue()) {
								setData(cell, appPlatform);
							}
						} else {
							if (cell.getNumericCellValue() >= 0) {
								setData(cell, appPlatform);
							}
						}
						break;
					case BOOLEAN:
						setData(cell, appPlatform);
						break;
					default:

					}
				}
				// System.out.println("");
				if (null != appPlatform && StringUtils.isNotEmpty(appPlatform.getDomain())) {
					appPlatformList.add(appPlatform);
				}
			}
		}
		try {
			response.setAppPlatformList(appPlatformList);
			System.out.println(mapper.writeValueAsString(response));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	private XSSFWorkbook loadExcelFromS3() {
		XSSFWorkbook workbook = null;
		S3ObjectInputStream inputStream = null;
		try {
			/*
			 * FileInputStream file = new FileInputStream( new
			 * File("C:\\MS Office Patching 2021\\CaaS\\Reports\\PaaS Platform Matrix_Macro.xlsm"
			 * ));
			 */
			// Create Workbook instance holding reference to .xlsx file
			/*
			 * AWSCredentials credentials = new BasicAWSCredentials("devlab0042user",
			 * "+UDMIHTPSFXEg2x74GsZGyTaxmzV/VVJr9iMYDiU");
			 */
			/*
			 * AmazonS3 s3client = AmazonS3ClientBuilder.standard() .withCredentials(new
			 * AWSStaticCredentialsProvider(credentials)).withRegion(Regions.DEFAULT_REGION)
			 * .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
			 * "https://<accountid>.r2.cloudflarestorage.com", "auto")) new
			 * AwsClientBuilder.EndpointConfiguration(
			 * "http://paasmatrix.lonec4203.server.rbsgrp.net:9020", "auto")
			 */
			ClientConfiguration loClientConfiguration = new ClientConfiguration();
			loClientConfiguration.addHeader(CONNECTION, KEEP_ALIVE);
			String lsTimeOut = TIME_OUT + String.valueOf(s3Properties.connectionTimeToLiveInMillis);
			loClientConfiguration.addHeader(KEEP_ALIVE, lsTimeOut);
			loClientConfiguration.setClientExecutionTimeout(s3Properties.clientExecutionTimeInMillis);
			loClientConfiguration.setConnectionMaxIdleMillis(s3Properties.connectionMaxIdleInTimeMillis);
			loClientConfiguration.setConnectionTimeout(s3Properties.connectionTimeOutInMillis);
			loClientConfiguration.setConnectionTTL(s3Properties.connectionTimeToLiveInMillis);
			loClientConfiguration.setMaxConnections(s3Properties.maxNumberOfConnections);
			loClientConfiguration
					.setMaxConsecutiveRetriesBeforeThrottling(s3Properties.maxNumberOfRetriesBeforeThrottling);
			loClientConfiguration.setMaxErrorRetry(s3Properties.maxErrorRetries);
			loClientConfiguration.setRequestTimeout(s3Properties.requestTimeOutInMillis);
			loClientConfiguration.setSocketTimeout(s3Properties.soketTimeOutInMillis);
			loClientConfiguration.setRetryPolicy(ClientConfiguration.DEFAULT_RETRY_POLICY);
			loClientConfiguration.setUseThrottleRetries(true);
			loClientConfiguration.setUseTcpKeepAlive(s3Properties.keepAlive);
			AWSCredentials loCredentials = new BasicAWSCredentials(s3Properties.accesskeyid,
					s3Properties.accesskeysecret);
			AwsClientBuilder.EndpointConfiguration loEndpoint = new AwsClientBuilder.EndpointConfiguration(
					s3Properties.endpoint, null);
			AWSStaticCredentialsProvider loAWSStaticCredentialsProvider = new AWSStaticCredentialsProvider(
					loCredentials);
			AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials(loAWSStaticCredentialsProvider)
					.withClientConfiguration(loClientConfiguration).withEndpointConfiguration(loEndpoint)
					.withPathStyleAccessEnabled(true).build();
			String lsBucketName = s3Properties.bucketname;
			if (s3client.doesBucketExist(lsBucketName)) {
				System.out.println("Bucket Exists named : " + lsBucketName);
				S3Object object = s3client.getObject(s3Properties.bucketname, excelFileName);
				inputStream = object.getObjectContent();
				workbook = new XSSFWorkbook(inputStream);
			} else {
				throw new RuntimeException();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return workbook;
	}

	private void setData(Cell cell, AppPlatform appPlatform) {
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		switch (cell.getColumnIndex()) {
		case 0:
			appPlatform.setDomain(cell.getStringCellValue());
			break;
		case 1:
			appPlatform.setOrg(cell.getStringCellValue());
			break;
		case 2:
			appPlatform.setApplicationInstance(cell.getStringCellValue());
			break;
		case 3:
			appPlatform.setTechnologyServiceOwner(cell.getStringCellValue());
			break;
		case 4:
			appPlatform.setClNo(cell.getStringCellValue());
			break;
		case 5:
			appPlatform.setEnvironment(cell.getStringCellValue());
			break;
		case 6:
			appPlatform.setMicroservices(cell.getNumericCellValue());
			break;
		}
	}

	private XSSFWorkbook loadExcelFromLocal() {
		XSSFWorkbook workbook = null;
		S3ObjectInputStream inputStream = null;
		try {

			FileInputStream file = new FileInputStream(
					new File("C:\\Documents\\Hex\\Personal\\Udaya\\fwdms\\PaaS Platform Matrix_Macro.xlsm"));
			workbook = new XSSFWorkbook(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}return workbook;
	}

}
