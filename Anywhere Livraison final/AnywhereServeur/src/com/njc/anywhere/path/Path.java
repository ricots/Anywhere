package com.njc.anywhere.path;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class Path {

	private static List<Node> list_node = null;
	private static List<Way> list_way = null;
	
	
	public List<Node> getWay(Double lat1, Double lon1, Double lat2, Double lon2) {
		List<Node> nodes = null;//store nodes to be drawn
		for (Way way : list_way) {
			nodes = new ArrayList<Node>();
			List<Node> nl = way.getNodeList();
			int size = nl.size();
			for (int i = 0; i < size; i++) {
				// start point in this way
				if (nl.get(i).getLat() == lat1
						&& nl.get(i).getLon() == lon1) {
					int j = i;
					//points after the start point isn't the end point (in the same way)
					for ( ; j < size
							&& !(nl.get(j).getLat() == lat2 && nl.get(j).getLon() == lon2); j++) {
						
						nodes.add(nl.get(j));
						
					}
					//end point isn't in the points come after
					if(j == size){
						nodes = null; // clear object
						break;
					}
					//end point also in the same way
					else{
						nodes.add(nl.get(j));//add the end point into nodes
//						for(Node node : nodes){
//							line.addPoint(new GeoPoint(node.getLat(), node.getLon()));
//							mapView.getOverlays().add(line);
//							SimpleLocationOverlay simpleLocation = new SimpleLocationOverlay(this);
//							simpleLocation.setEnabled(true);
//							mapView.getOverlays().add(simpleLocation);
//						}
						return nodes;
					}
				}
			}
		}
		return null;//there is not a way pass these two points
	}
	
	
	public List<Node> PullParseXML_Node() {

		List<Node> list = null;
		Node node = null;

		try {
			XmlPullParserFactory pullParserFactory = XmlPullParserFactory
					.newInstance();

			XmlPullParser xmlPullParser = pullParserFactory.newPullParser();

			xmlPullParser.setInput(Thread.currentThread()
					.getContextClassLoader().getResourceAsStream("1etage.xml"),
					"UTF-8");

			int eventType = xmlPullParser.getEventType();

			try {
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String nodeName = xmlPullParser.getName();
					switch (eventType) {

					case XmlPullParser.START_DOCUMENT:
						list = new ArrayList<Node>();
						break;

					case XmlPullParser.START_TAG:

						if ("node".equals(nodeName)) {

							node = new Node();
							node.setId(xmlPullParser.getAttributeValue(0));
							node.setLat(Double.parseDouble(xmlPullParser
									.getAttributeValue(7)));
							node.setLon(Double.parseDouble(xmlPullParser
									.getAttributeValue(8)));
						}
						break;

					case XmlPullParser.END_TAG:
						if ("node".equals(nodeName)) {
							list.add(node);
							node = null;
						}
						break;
					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Way> PullParseXML_Way() {

		List<Way> list = null;
		List<Node> nodeList = null;
		Way way = null;
		Node node = null;

		try {
			XmlPullParserFactory pullParserFactory = XmlPullParserFactory
					.newInstance();

			XmlPullParser xmlPullParser = pullParserFactory.newPullParser();

			xmlPullParser.setInput(Thread.currentThread()
					.getContextClassLoader().getResourceAsStream("1etage.xml"),
					"UTF-8");

			int eventType = xmlPullParser.getEventType();

			try {
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String nodeName = xmlPullParser.getName();
					switch (eventType) {

					case XmlPullParser.START_DOCUMENT:
						list = new ArrayList<Way>();
						break;

					case XmlPullParser.START_TAG:

						if ("way".equals(nodeName)) {

							way = new Way();
							way.setId(xmlPullParser.getAttributeValue(0));
							nodeList = new ArrayList<Node>();
						} else if ("nd".equals(nodeName)) {
							String ref = (xmlPullParser.getAttributeValue(0));
							node = new Node();
							node = findNodeById(ref, list_node);
							// way.getNodeList().add(node);
							nodeList.add(node);
							way.setNodeList(nodeList);
						}
						break;

					case XmlPullParser.END_TAG:
						if ("way".equals(nodeName)) {
							list.add(way);
							way = null;
							node = null;
							nodeList = null;
						}
						break;
					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return list;
	}

	private Node findNodeById(String ref, List<Node> list) {
		for (int i = 0; i < list.size(); i++) {
			Node n = list.get(i);
			if (ref.equals(n.getId())) {
				return n;
			}
		}
		return null;

	}
}
