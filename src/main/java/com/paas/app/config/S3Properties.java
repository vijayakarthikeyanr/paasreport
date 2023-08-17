package com.paas.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
/** --------------------------------------------------------------------------------------------------------
 * Description    : A properties Interface to access SSS properties
 * Author         : Lithesh Anargha 
 * Email          : Lithesh.Anargha@rbs.co.uk
 * Date           : 26/01/2019
 * Project        : Vanquish 
 * Platform       : Bankline Direct Digital
 * Organization   : Royal Bank of Scotland plc.
-------------------------------------------------------------------------------------------------------- **/
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class S3Properties {

	public S3Properties() {
		
	}
	
	@Value("${sss.endpoint:}")
    public String endpoint;
	
	@Value("${sss.accesskeyid:}")
	public String accesskeyid;
	
	@Value("${sss.accesskeysecret:}")
	public String accesskeysecret;
	
	@Value("${sss.bucketname:}")
	public String bucketname;
	
	@Value("${sss.client.execution.time.in.millis:#{480000}}")
	public int clientExecutionTimeInMillis;
	
	@Value("${sss.connection.max.idle.time.in.millis:#{500}}")
	public long connectionMaxIdleInTimeMillis;
	
	@Value("${sss.connection.timeout.in.millis:#{60000}}")
	public int connectionTimeOutInMillis;
	
	@Value("${sss.connection.time.to.live.in.millis:#{500000}}")
	public long connectionTimeToLiveInMillis;
	
	@Value("${sss.max.number.of.connections:#{25}}")
	public int maxNumberOfConnections;
	
	@Value("${sss.max.number.of.retry.before.throttling:#{3}}")
	public int maxNumberOfRetriesBeforeThrottling;
	
	@Value("${sss.max.error.retries:#{3}}")
	public int maxErrorRetries;
	
	@Value("${sss.request.time.out.in.millis:#{480000}}")
	public int requestTimeOutInMillis;
	
	@Value("${sss.soket.time.out.in.millis:#{510000}}")
	public int soketTimeOutInMillis;
	
	@Value("${sss.soket.tcp.keep.alive:#{true}}")
	public boolean keepAlive;
	
}
