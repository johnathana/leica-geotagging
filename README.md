# Leica Geotagging

## What is it?

Recent Leica and Panasonic digital camera models provide a way to add 
location information to your images using a smartphone. This functionality 
is not documented and only available through the official 
android and iOS application which are rather outdated. 
This is a reverse engineered implementation of the geotagging functionality.

## Models supported

* Leica C (Typ 112)
* Leica V-LUX (Typ 114)
* Leica D-LUX (Typ 109)
* Panasonic Lumix DMC-LF1
* Panasonic Lumix DMC-FZ1000
* Panasonic Lumix DMC-LX100

And many others not listed here.

## Using Leica Geotagging

You have first to establish a WiFi connection with your camera.
Use your camera IP to start a GeoTaggingClient:

    GeoTaggingClient geoTagClient = new GeoTaggingClient("192.168.54.1");
    
Send a list of gps log entries with sendGpsEntries. This command will upload 
the log to your camera and start the write procedure. Geo-tagged images will 
show a "GPS" sign in the preview menu.
