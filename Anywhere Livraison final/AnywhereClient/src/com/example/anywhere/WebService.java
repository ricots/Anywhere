package com.example.anywhere;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.example.anywhere.ScalingUtilities.ScalingLogic;

import Decoder.BASE64Decoder;
import android.graphics.Bitmap;
import android.util.Base64;

public class WebService {
	// Namespace of the Webservice - can be found in WSDL
	private static String NAMESPACE = "http://imp.anywhereservice.tlf.com/";
	// Webservice URL - WSDL File location
	// private static String URL = "http://10.0.2.2:8080/hello?WSDL";
	//private static String URL = "http://192.168.1.31:8080/hello?WSDL";
	private static String URL = "http://192.168.43.111:8080/hello?WSDL";

	// private static String URL = "http://172.20.10.6:8080/hello?WSDL";

	// SOAP Action URI again Namespace + Web method name
	// private static String SOAP_ACTION = "";

	public static String invokeHelloWorldWS(String name, String webMethName) {
		String resTxt = null;
		// Create request
		SoapObject request = new SoapObject(NAMESPACE, webMethName);
		// Property which holds input parameters
		/*
		 * PropertyInfo sayHelloPI = new PropertyInfo(); // Set Name
		 * sayHelloPI.setName("name"); // Set Value sayHelloPI.setValue(name);
		 * // Set dataType sayHelloPI.setType(String.class); // Add the property
		 * to request object request.addProperty(sayHelloPI);
		 */
		request.addProperty("arg0", name);
		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			// Invoke web service
			androidHttpTransport.call(webMethName, envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to resTxt variable static variable
			resTxt = response.toString();

		} catch (Exception e) {
			// Print error
			e.printStackTrace();
			// Assign error message to resTxt
			resTxt = e.toString();
		}
		// Return resTxt to calling object
		return resTxt;
	}

	public static String Upload(String lat, String lon, String city) {
		String resTxt = null;
		try {
			String srcUrl = "/sdcard/Anywhere/"; // path
			String fileName = "upload.jpg"; // file name
			FileInputStream fis = new FileInputStream(srcUrl + fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			String uploadBuffer = new String(Base64.encode(baos.toByteArray(),
					0)); 
			String methodName = "uploadImage";

			SoapObject request = new SoapObject(NAMESPACE, methodName);

			request.addProperty("arg0", uploadBuffer);
			request.addProperty("arg1", lat);
			request.addProperty("arg2", lon);
			request.addProperty("arg3", "Carras");
			request.addProperty("arg4", city);
			// Create envelope
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// Set output SOAP object
			envelope.setOutputSoapObject(request);
			// Create HTTP call object
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			// Invoke web service
			androidHttpTransport.call(methodName, envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to resTxt variable static variable
			resTxt = response.toString();

			fis.close();
		} catch (Exception e) {
			resTxt = e.toString();
		}

		return resTxt;
	}

	public String getCoordinate(String fileName, String city) {
		String resTxt = null;
		try {
			String srcUrl = "/sdcard/Anywhere/"; // path
			// String fileName = "AnywhereTemp.jpg"; // file name
			FileInputStream fis = new FileInputStream(srcUrl + fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			String uploadBuffer = new String(Base64.encode(baos.toByteArray(),
					0)); 
			String methodName = "getCoordinate";

			SoapObject request = new SoapObject(NAMESPACE, methodName);

			request.addProperty("arg0", uploadBuffer);
			request.addProperty("arg1", city);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// Set output SOAP object
			envelope.setOutputSoapObject(request);
			// Create HTTP call object
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			// Invoke web service
			androidHttpTransport.call(methodName, envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to resTxt variable static variable
			resTxt = response.toString();

			fis.close();
		} catch (Exception e) {
			resTxt = e.toString();
		}

		return resTxt;
	}

	public static void Download(String latitude, String longitude, String city) {
		String image = null;
		String methodName = "downloadImage";
		SoapObject request = new SoapObject(NAMESPACE, methodName);

		// marker.getPosition().getAltitude()
		request.addProperty("arg0", latitude);
		request.addProperty("arg1", longitude);
		request.addProperty("arg2", city);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		try {
			androidHttpTransport.call(methodName, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			image = response.toString();

			FileOutputStream fos = null;
			String toDir = "/sdcard/Anywhere/";
			byte[] buffer = new BASE64Decoder().decodeBuffer(image); // 对android传过来的图片字符串进行解码
			File destDir = new File(toDir);
			if (!destDir.exists())
				destDir.mkdir();
			File f = new File(destDir, "AnywhereTemp.jpg");
			if (f.exists()) {
				f.delete();
			}
			fos = new FileOutputStream(f); // save image
			fos.write(buffer);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

	}

	public static String decodeFile(String path, int DESIREDWIDTH,
			int DESIREDHEIGHT) {
		String strMyImagePath = null;
		Bitmap scaledBitmap = null;

		try {
			// Part 1: Decode image
			Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path,
					DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);

			if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap
					.getHeight() <= DESIREDHEIGHT)) {
				// Part 2: Scale image
				scaledBitmap = ScalingUtilities.createScaledBitmap(
						unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT,
						ScalingLogic.FIT);
			} else {
				unscaledBitmap.recycle();
				return path;
			}

			// Store to tmp file

			File mFolder = new File("/sdcard/Anywhere");
			if (!mFolder.exists()) {
				mFolder.mkdir();
			}

			String s = "departure.jpg";

			File f = new File(mFolder.getAbsolutePath(), s);

			strMyImagePath = f.getAbsolutePath();
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
				scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();
			}

			scaledBitmap.recycle();
		} catch (Throwable e) {
		}

		if (strMyImagePath == null) {
			return path;
		}
		return strMyImagePath;

	}

	public static String decodeFile2(String path, int DESIREDWIDTH,
			int DESIREDHEIGHT) {
		String strMyImagePath = null;
		Bitmap scaledBitmap = null;

		try {
			// Part 1: Decode image
			Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path,
					DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);

			if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap
					.getHeight() <= DESIREDHEIGHT)) {
				// Part 2: Scale image
				scaledBitmap = ScalingUtilities.createScaledBitmap(
						unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT,
						ScalingLogic.FIT);
			} else {
				unscaledBitmap.recycle();
				return path;
			}

			// Store to tmp file

			File mFolder = new File("/sdcard/Anywhere");
			if (!mFolder.exists()) {
				mFolder.mkdir();
			}

			String s = "upload.jpg";

			File f = new File(mFolder.getAbsolutePath(), s);

			strMyImagePath = f.getAbsolutePath();
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
				scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			scaledBitmap.recycle();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (strMyImagePath == null) {
			return path;
		}
		return strMyImagePath;
	}

	public static String decodeFile3(String path, int DESIREDWIDTH,
			int DESIREDHEIGHT) {
		String strMyImagePath = null;
		Bitmap scaledBitmap = null;

		try {
			// Part 1: Decode image
			Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path,
					DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);

			if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap
					.getHeight() <= DESIREDHEIGHT)) {
				// Part 2: Scale image
				scaledBitmap = ScalingUtilities.createScaledBitmap(
						unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT,
						ScalingLogic.FIT);
			} else {
				unscaledBitmap.recycle();
				return path;
			}

			// Store to tmp file

			File mFolder = new File("/sdcard/Anywhere");
			if (!mFolder.exists()) {
				mFolder.mkdir();
			}

			String s = "destination.jpg";

			File f = new File(mFolder.getAbsolutePath(), s);

			strMyImagePath = f.getAbsolutePath();
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
				scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			scaledBitmap.recycle();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (strMyImagePath == null) {
			return path;
		}
		return strMyImagePath;
	}

	public static boolean getIndoorPlan(String path) {

		String image = null;
		try {
			String srcUrl = "/sdcard/Anywhere/"; // path
			String fileName = path; // file name
			FileInputStream fis = new FileInputStream(srcUrl + fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			String uploadBuffer = new String(Base64.encode(baos.toByteArray(),
					0)); 
			String methodName = "getIndoorPlan";

			SoapObject request = new SoapObject(NAMESPACE, methodName);

			request.addProperty("arg0", uploadBuffer);
			// Create envelope
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// Set output SOAP object
			envelope.setOutputSoapObject(request);
			// Create HTTP call object
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			// Invoke web service
			androidHttpTransport.call(methodName, envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to resTxt variable static variable
			image = response.toString();

			fis.close();
		} catch (Exception e) {
			image = e.toString();
		}
		if (!image.equals("0")) {
			FileOutputStream fos = null;
			String toDir = "/sdcard/Anywhere/";
			byte[] buffer = null;
			try {
				buffer = new BASE64Decoder().decodeBuffer(image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			File destDir = new File(toDir);
			if (!destDir.exists())
				destDir.mkdir();
			File f = new File(destDir, "indoorMap.jpg");
			if (f.exists()) {
				f.delete();
			}
			try {
				fos = new FileOutputStream(f);
				fos.write(buffer);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // save image
			return true;
		}
		return false;
	}

	public static String Upload(String lat, String lon, String city,
			String floor) {
		String resTxt = null;
		try {
			String srcUrl = "/sdcard/Anywhere/"; // path
			String fileName = "upload.jpg"; // file name
			FileInputStream fis = new FileInputStream(srcUrl + fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			String uploadBuffer = new String(Base64.encode(baos.toByteArray(),
					0)); 
			String methodName = "uploadImage";

			SoapObject request = new SoapObject(NAMESPACE, methodName);

			request.addProperty("arg0", uploadBuffer);
			request.addProperty("arg1", lat);
			request.addProperty("arg2", lon);
			request.addProperty("arg3", floor);
			request.addProperty("arg4", city);
			// Create envelope
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// Set output SOAP object
			envelope.setOutputSoapObject(request);
			// Create HTTP call object
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			// Invoke web service
			androidHttpTransport.call(methodName, envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to resTxt variable static variable
			resTxt = response.toString();

			fis.close();
		} catch (Exception e) {
			resTxt = e.toString();
		}

		return resTxt;
	}
}