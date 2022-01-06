import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import SGDClassifier
from sklearn.metrics import confusion_matrix, classification_report
from sklearn.decomposition import PCA
import matplotlib.pyplot as plt
from sklearn.model_selection import GridSearchCV
from sklearn.svm import SVC

# Test
colores_test = pd.read_csv("Celulas/test/color_test.csv", sep=';', decimal=',')
shape_test = pd.read_csv("Celulas/test/shape_test.csv", sep=';', decimal=',')
textura_test = pd.read_csv("Celulas/test/texture_test.csv", sep=';', decimal=',')
idx = pd.read_csv("Celulas/test/info_test.csv")

textura_data_test = pd.DataFrame(textura_test.iloc[:, 0:1])
valores_test = pd.DataFrame(textura_test.iloc[:, -1])


df1_t = colores_test.join(shape_test, lsuffix='_colores', rsuffix='_shape')
df2_t = df1_t.join(textura_data_test, rsuffix='_texture')
df3_t = shape1_test.join(textura_data_test)
df4_t = shape_test

# Train
data = pd.read_csv("Celulas/train/info_train.csv", sep=';')
colores = pd.read_csv("Celulas/train/color_train.csv", sep=',', decimal=',')
shape = pd.read_csv("Celulas/train/shape_train.csv", sep=',', decimal=',')
textura = pd.read_csv("Celulas/train/texture_train.csv", sep=',', decimal=',')

valores = pd.DataFrame(data.iloc[:, -1])
colores_data = pd.DataFrame(colores.iloc[:, :-1])
textura_data = pd.DataFrame(textura.iloc[:, 0:1])


df1 = colores_data.join(shape, lsuffix='_colores', rsuffix='_shape')
df2 = df1.join(textura_data, rsuffix='_texture')
df3 = shape.join(textura_data)
df4 = shape

X_train = df3.to_numpy()
y_train = valores.to_numpy().ravel()

X_test = df3_t.to_numpy()
y_test = valores_test.to_numpy().ravel()

scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.fit_transform(X_test)

"""
#PCA
pca = PCA()
pca.fit(X_train_scaled)
cumVariance = np.cumsum(pca.explained_variance_)
plt.figure(1)
plt.scatter(range(cumVariance.shape[0]), cumVariance[:])
plt.plot(range(cumVariance.shape[0]), cumVariance[:]) #Cogemos 42
plt.show()
pca = PCA()
data_pca_train = pca.fit_transform(X_train_scaled)
data_pca_test = pca.fit_transform(X_test_scaled)
"""

# Entrenamos el modelo

param_grid = {'C': np.logspace(-2, 10, 13).tolist(), 'gamma': np.logspace(-9, 3, 13).tolist(), 'kernel': ('linear',
                                                                                                          'poly', 'rbf',
                                                                                                          'sigmoid')}

svc = SVC()
gsv = GridSearchCV(svc, param_grid, cv=None, verbose=1)
# gsv = svc

gsv.fit(X_train_scaled, y_train)
prediction_gsv_test = gsv.predict(X_test_scaled)
print(gsv.best_estimator_)

# Avaluació
gsv_matrix_log = confusion_matrix(y_test, prediction_gsv_test)
print("Matrix de confusion:\n", gsv_matrix_log)
print("Classification report:\n", classification_report(y_test, prediction_gsv_test))


testerino = pd.DataFrame(prediction_gsv_test, columns=['class'])

result = idx.join(testerino)
result.to_csv('test.csv', index=False)
