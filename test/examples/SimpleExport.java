package examples;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.one2team.highcharts.server.export.ExportType;
import org.one2team.highcharts.server.export.HighchartsExporter;
import org.one2team.highcharts.shared.ChartOptions;
import org.one2team.highcharts.shared.Jsonify;

public class SimpleExport {

	public static void main (String[] args) {
		
		// This executable expects an export directory as input
		File exportDirectory = new File (args [0]);
		
		final SamplesFactory highchartsSamples = SamplesFactory.getSingleton ();
		
		// ====================================================================
		// ChartOptions creation
		// ----------------------
		//  The createHighchartsDemoColumnBasic method describes the creation of 
		//   a java chartOption. It is a java equivalent to javascript Highcharts sample
		//   (see http://highcharts.com/demo/column-basic)
		ChartOptions chartOptions1 = highchartsSamples.createColumnBasic ();

		
		// ====================================================================
		// Chart export
		// ----------------
		// Inputs :
		//    1. chartOptions : the java ChartOptions to be exported,
		//    2. exportFile  : file to export to.
		HighchartsExporter<ChartOptions> pngExporter = ExportType.png.createExporter ();
		pngExporter.export (chartOptions1, null, new File (exportDirectory, "column-basic.png"));
		
//		// ====================================================================
//		// Another example using the same exporter
//		// ---------------------------------------
		ChartOptions chartOptions2 = highchartsSamples.createPieChart ();
		pngExporter.export (chartOptions2, null, new File (exportDirectory, "pie-chart.png"));
//		
//		// ====================================================================
//		// An example exporting to JPEG
//		// ---------------------------------------
		ChartOptions chartOptions3 = highchartsSamples.createTimeDataWithIrregularIntervals ();
		final HighchartsExporter<ChartOptions> jpegExporter = ExportType.jpeg.createExporter ();
		jpegExporter.export (chartOptions3, null, new File (exportDirectory, "time-data-with-irregular-intervals.jpeg"));
		
		// ====================================================================
		// Chart export with a json input (instead of a java one)
		// ----------------
		// Inputs :
		//    1. chartOptions : the json ChartOptions to be exported,
		//    2. exportFile  : file to export to.
		String chartOption = highchartsSamples.createJsonColumnBasic ();
		HighchartsExporter<String> pngFromJsonExporter = ExportType.png.createJsonExporter ();
		pngFromJsonExporter.export (chartOption, null, new File (exportDirectory, "column-basic-from-json.png"));
		
		// ====================================================================
		// json export
		// ----------------
		// Inputs :
		//    1. chartOptions : chartOptions1
		Jsonify jsonify = (Jsonify) highchartsSamples.createColumnBasic ();
		String json = jsonify.toJson ();
		System.out.println("json "+json);
		pngFromJsonExporter.export (json, null, new File (exportDirectory, "column-basic-from-jsonified-java.png"));
		
		
		// Chart png from JSON in file
		String filePath = "./default.json";
		if(args.length > 1){ filePath = args[1]; }
		StringBuilder optionsText = new StringBuilder();
			try {	 
				BufferedReader br = new BufferedReader(
					new FileReader(filePath));	 
				// capture file contents
				String line = null;
				while((line = br.readLine()) != null ){
					optionsText.append(line);
					optionsText.append(System.getProperty("line.separator"));
				}
				// print file contents
				System.out.println("Read json: \n" + optionsText);	 
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			// try rendering directly to stream
			HighchartsExporter<String> svgFromJsonExporter = ExportType.svg.createJsonExporter();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			svgFromJsonExporter.export (optionsText.toString(), null, baos);
			// show on console
			System.out.println("svg generated: " + baos.toString());
			
			// try rendering to File
			HighchartsExporter<String> svg2FromJsonExporter = ExportType.svg.createJsonExporter();
			svg2FromJsonExporter.export (optionsText.toString(), null, new File (exportDirectory, "svgFromJson.html"));
	}


}
