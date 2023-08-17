package com.paas.app.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SFTPUtil {
	@Value("${serverAddress}")
	String serverAddress;
	@Value("${userId}")
	String userId;
	@Value("${password}")
	String password;
	@Value("${remoteDirectory}")
	String remoteDirectory;
	@Value("${localDirectory}")
	String localDirectory;

	public boolean uploadToFTP(String fileToFTP) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {

			String serverAddr = serverAddress.trim();
			String userName = userId.trim();
			String localDir = localDirectory.trim();
			String passwd = password.trim();
			String remoteDir = remoteDirectory.trim();

			// check if the file exists
			String filepath = localDir + fileToFTP;
			File file = new File(filepath);
			if (!file.exists())
				throw new RuntimeException("Error. Local file not found");

			// Initializes the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
			SftpFileSystemConfigBuilder.getInstance().setSessionTimeout(opts, Duration.ofMillis(10000));

			// Create the SFTP URI using the host name, userid, password, remote path and
			// file name
			String sftpUri = "sftp://" + userName + ":" + passwd + "@" + serverAddr + "/" + remoteDir + fileToFTP;

			// Create local file object
			FileObject localFile = manager.resolveFile(file.getAbsolutePath());

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);

			// Copy local file to sftp server
			remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
			System.out.println("File upload successful");
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			manager.close();
		}

		return true;
	}

	public boolean downloadFromFTP(String fileToDownload) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {

			String serverAddr = serverAddress.trim();
			String userName = userId.trim();
			String localDir = localDirectory.trim();
			String passwd = password.trim();
			String remoteDir = remoteDirectory.trim();
			// Initializes the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
			SftpFileSystemConfigBuilder.getInstance().setSessionTimeout(opts, Duration.ofMillis(10000));

			// Create the SFTP URI using the host name, userid, password, remote path and
			// file name
			String sftpUri = "sftp://" + userName + ":" + passwd + "@" + serverAddr + "/" + remoteDir + fileToDownload;

			// Create local file object
			String filepath = localDir + fileToDownload;
			File file = new File(filepath);
			FileObject localFile = manager.resolveFile(file.getAbsolutePath());

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);

			// Copy local file to sftp server
			localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
			System.out.println("File download successful");

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			manager.close();
		}

		return true;
	}

	public XSSFWorkbook readFileFromFTP(String fileToRead) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		XSSFWorkbook workbook = null;
		InputStream inputStream = null;
		try {

			String serverAddr = serverAddress.trim();
			String userName = userId.trim();
			String passwd = password.trim();
			String remoteDir = remoteDirectory.trim();
			// Initializes the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
			SftpFileSystemConfigBuilder.getInstance().setSessionTimeout(opts, Duration.ofMillis(10000));

			// Create the SFTP URI using the host name, userid, password, remote path and
			// file name
			String sftpUri = "sftp://" + userName + ":" + passwd + "@" + serverAddr + "/" + remoteDir + fileToRead;

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);

			// Copy local file to sftp server
			// localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
			if (remoteFile.exists()) {
				FileContent fileContent = remoteFile.getContent();
				inputStream = fileContent.getInputStream();
				workbook = new XSSFWorkbook(inputStream);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			manager.close();
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return workbook;
	}

}
