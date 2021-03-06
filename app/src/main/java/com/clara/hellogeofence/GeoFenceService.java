package com.clara.hellogeofence;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by clara on 11/6/16.
 */

public class GeoFenceService extends IntentService {

	//TODO check that you've registed this IntentService in AndroidManifest.xml

	private static final String TAG = "GeoFenceService";

	//Constructor required
	public GeoFenceService() {
		super(TAG);
		Log.d(TAG, "GeoFenceService object created");
	}



	@Override
	protected void onHandleIntent(Intent intent) {


		//Log event. For a real app, could save to a Firebase DB?
		//The Geofence notifications may happen when user is not viewing the app so
		//it's not recommended to send them to an Activity or Fragment. Instead, send them
		//to an IntentService class that can start in the background and handle the event, by saving it somewhere
		//or issuing a notification, as in the Google example.

		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

		Log.d(TAG, "Geofence event received");

		//todo check for errors

		//If more than one GeoFence used, figure out which GeoFence was entered/exited

		List<Geofence> listOfGeoFences = geofencingEvent.getTriggeringGeofences();

		Log.d(TAG, "The request IDs of all GeoFences triggered:");

		for (Geofence geofence : listOfGeoFences) {
			Log.d(TAG, "Geofence ID: " + geofence.getRequestId());  //this is configured in MainActivity

			int transition = geofencingEvent.getGeofenceTransition();
			String transitionString = "";
			if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
				transitionString = "entered";
			} else if (transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
				transitionString = "is dwelling in";
			} else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
				transitionString = "exited";
			}

			Date currentTime = new Date();
			String eventMessage = "Device " + transitionString + " GeoFence with tag " + geofence.getRequestId() + " at " + currentTime ;
			// e.g. "Device entered GeoFence with tag MCTC_Geofence at November 8 2016 15.12:12..."

			Firebase firebase = new Firebase();
			firebase.addGeoFenceEvent(eventMessage);   //TODO an object to store more detail about event

		}


		//You'd probably need some more logic here to do whatever you need to do when a particular GeoFence is triggered.


	}
}
