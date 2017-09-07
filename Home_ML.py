import pyrebase
import time
from pprint import pprint
from operator import itemgetter
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression

config = {
  "apiKey": "****",
  "authDomain": "****",
  "databaseURL": "****",
  "storageBucket": "****"
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()

#Data Retrieval
sw = db.child('Switch_Duration').get().val().values()
sample = sorted(sw, key=itemgetter('Day'))
pprint(sample)

led_power_Kw = 0.015
rate_Kw = 5
unit = float(led_power_Kw) / float(3600000) * float(rate_Kw)

X_sample = []
Y_sample = []

for data in sample:
    day = data['Day']
    duration = 0
    temp = 0
    humid = 0
    no = 0
    label = []
    duDay = []
    for data1 in sample:
        if day==data1['Day']:
            no += 1
            duration += data1['Duration']
            temp += data1['Temperature']
            humid += data1['Humidity']

    if(duration not in label):
        label.append(float(duration) * unit)
        if(label not in Y_sample):
            Y_sample.append(label)

    if(data['Day'] not in duDay):
        duDay.append(data['Day'])
        duDay.append(float(temp) / no)
        duDay.append(float(humid) / no)
        if(duDay not in X_sample):
            X_sample.append(duDay)

print
print "Printing Labels"
print Y_sample
print "Printing Featurs"
pprint(X_sample)

print
print "Performing Linear Regression..."
lm = LinearRegression()
lm.fit(X_sample,Y_sample)
print "Coeifficient: ",
print lm.coef_
print "Intercept: ",
print lm.intercept_
print "Value: ",
X_test = [17415,28.0,63.5]
pred = lm.predict(X_test)
print pred[0][0]

db.child("Tommorow's Prediction").set(pred[0][0])

"""
plt.scatter(X_sample,Y_sample,color='blue')
plt.plot(X_sample,lm.predict(X_sample),color='yellow',linewidth=4)
plt.plot(17415, lm.predict(17415, 30, 78), marker='X', markersize=10, color="red")
plt.show()
"""
