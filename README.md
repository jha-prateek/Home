# HOME
    HAEP (Home Automation and Energy Prediction System) is a Project about Home Automation system which is built around the idea of Measuring and Predicting the Energy consumption of the House.
    Major goal of this Project is to perform analytics on the collected data and predict the Energy Consumption for next Day or Month. 
    We people waste a lot of energy in our daily usage either by not turning the lights off when not in use or by utilizing old and inefficient appliances that consume large amount of energy. Energy needs to be conserved not only to cut costs but also to preserve the resources for longer use.
    
#### WORKING
    The HAEP system works by continuously collecting user data and performing analytics on that data. 
    The data sent by the Embedded System(Arduino) to Firebase and stored in NoSQL document. From their data is downloaded at several endpoints such which include Analytics server and Android Application. 
    Analytics Server uses that data to perform Machine Learning and Android Application uses that data for the purpose Graph Plotting and Visualization to user. The system includes following steps:
    1.	 User will Switch On/Off the Appliance using the Android App built for the System.
    2.	Data from Android goes to Firebase document on Cloud.
    3.	Arduino is continuously listening for the change of data in the Firebase Document.
    4.	Based on the Field value in Firebase Document it changes the State of the Device.
    5.	Arduino keeps on collecting Current Temperature and Humidity.
    6.	These values are sent to the Firebase Document for analytics purpose.
    7.	Now the data is Collected by the Python Script running on the Server.
    8.	A Linear Regression Model is ran on the collected data and next day prediction is done.
    9.	The value is then sent to Android App via Firebase again.

![alt text](https://github.com/jha-prateek/Home/blob/master/Snapshots/Working.jpg)

#### SCREENSHOT

![alt text](https://github.com/jha-prateek/Home/blob/master/Snapshots/Screenshot_20171010-231050.png)
![alt text](https://github.com/jha-prateek/Home/blob/master/Snapshots/Screenshot_20171010-231113.png)