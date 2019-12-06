package util;

import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class Util {
	public final static ArrayList<String> pageNames = new ArrayList<String>() {{
	    add("front");
	    add("innerLeft");
	    add("innerRight");
	}};
	
	public static String generateUniqueId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String generateS3BucketUrl(String fileName) {
		return "https://justice509.s3.amazonaws.com/images/" + fileName;
	}
	
	public static URL GeneratePresignedUrl(String objectKey, String bucketName) throws Exception {
		AmazonS3 s3Client = AmazonS3ClientBuilder
				.standard()
				//.withCredentials(new ProfileCredentialsProvider())
				.withRegion(Regions.US_EAST_1).build();

		// Set the pre-signed URL to expire after one hour.
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60;
		expiration.setTime(expTimeMillis);

		// Generate the pre-signed URL.
		System.out.println("Generating pre-signed URL.");
		System.out.println("bucketName: " + bucketName);
		System.out.println("objectKey: " + objectKey);
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
				.withMethod(HttpMethod.PUT).withExpiration(expiration);
		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

		System.out.println("Pre-Signed URL: " + url.toString());

		return url;
	}
}
