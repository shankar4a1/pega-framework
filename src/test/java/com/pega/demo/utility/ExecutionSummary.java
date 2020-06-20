package com.pega.demo.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;


public class ExecutionSummary {

	public static void main(String[] args) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(args[0])));

        JSONArray featureFileArr = new JSONArray(jsonContent);

        int total = 0, passed = 0, failed = 0;

        for(int i=0; i<featureFileArr.length(); i++){
            JSONObject featureFileObj = featureFileArr.getJSONObject(i);
            JSONArray scenarioArr = (JSONArray) featureFileObj.get("elements");
            total = total + scenarioArr.length();
            for(int j = 0; j < scenarioArr.length(); j++){
                   JSONObject scenarioObj = scenarioArr.getJSONObject(j);
                   String withSpace = scenarioObj.toString();
                   String withoutSpace = withSpace.replaceAll("\\s","");
                   if(withoutSpace.contains("\"status\":\"failed\"")){
                       failed = failed + 1;
                   } else {
                       passed = passed + 1;
                   }

            }
        }
        System.out.println("===============Test Execution Summary===============");
        System.out.println("**********  Total:  " + total);
        System.out.println("**********  Passed: " + passed);
        System.out.println("**********  Failed: " + failed);
        System.out.println("====================================================");
    }
}