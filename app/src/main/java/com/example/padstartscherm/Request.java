package com.example.padstartscherm;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to make a request to the online API
 */
public class Request {

    /**
     * Method to make a request to the API
     *
     * @param data     is the ArrayList of the passed in data
     * @param URL      is the POST URL of the API
     * @param activity De huidige activity waarin wordt gewerkt.
     * @param userId   (can be null) This is a variable that you can pass to the next activity
     * @param melding  Kan een melding meegegeven wanneer de request is gelukt.
     * @return
     */
    public static void makeRequest(final Map<String, String> data, String URL, final Activity activity, final String userId, final String melding) {

        final int SUCCESVOL_REQUEST = 200;

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject object = new JSONObject();

                // Try to convert returned data to a JSON object
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    Toast.makeText(activity.getApplication(), "Er is een fout opgetreden", Toast.LENGTH_LONG).show();
                }

                try {
                    if (Integer.valueOf(object.get("code").toString()) == SUCCESVOL_REQUEST) {
                        if (activity instanceof MainActivity) {
                            Intent intent = new Intent(activity, Main2Activity.class);
                            intent.putExtra("license_plate", userId.toUpperCase());
                            ((MainActivity) activity).usr.setText("");
                            ((MainActivity) activity).pas.setText("");
                            activity.startActivity(intent);
                        } // Check if Main2Activity triggered this request
                        else if (activity instanceof Main2Activity) {
                            ((Main2Activity) activity).aantalContainers.setText("0");
                            ((Main2Activity) activity).aantalKlikos.setText("0");
                            ((Main2Activity) activity).aantalzakken.setText("0");
                        }

                        Toast.makeText(activity.getApplication(), melding, Toast.LENGTH_LONG).show();
                    } else {
                        // Let the user know the reason why the request has failed (The API gives a detailed reason)
                        Toast.makeText(activity.getApplication(), object.get("message").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            // Volley error response
            @Override
            public void onErrorResponse(VolleyError error) {
                // Let the user know the request couldn't be done successfully
                Toast.makeText(activity.getApplication(), "FOUT: Het verzoek is niet succesvol gegaan", Toast.LENGTH_LONG).show();
            }
        }) {
            // Get the params for the POST request
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                for (Map.Entry<String, String> entry : data.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }

                // Also pass in API key as param
                params.put("api_key", "MY_API_KEY");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
}

