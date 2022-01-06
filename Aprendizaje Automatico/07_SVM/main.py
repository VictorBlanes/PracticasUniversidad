import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.svm import SVC
from sklearn.metrics import roc_curve
from sklearn.impute import SimpleImputer

pulsar_train = pd.read_csv("Data/pulsar_data_train.csv")
pulsar_test = pd.read_csv("Data/pulsar_data_test.csv")

data_train = pd.DataFrame(pulsar_train.iloc[:, :-1])
data_test = pd.DataFrame(pulsar_test.iloc[:, :-1])

target_train = pd.DataFrame(pulsar_train.iloc[:, -1])
target_test = pd.DataFrame(pulsar_test.iloc[:, -1])

X_train = data_train.to_numpy()
y_train = target_train.to_numpy().ravel()

simp = SimpleImputer(missing_values=np.nan, strategy='mean')

simp.fit(X_train)

Xf_train = simp.transform(X_train)

"""
print(data_train)
print(target_train)
print(data_test)
print(target_test)
"""

svc = SVC(kernel="linear", probability=True)
svc.fit(Xf_train, y_train)
target_prob = svc.predict_proba(Xf_train)

# print(y_train)
# print(target_prob)
prob_clase = target_prob[:, -1]

fpr, tpr, tresholds = roc_curve(y_train, prob_clase)
# print(fpr)
# print(tpr)

plt.figure(1)
plt.plot(fpr, tpr)
plt.show()
