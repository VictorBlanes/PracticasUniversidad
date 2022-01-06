import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import SGDClassifier
from sklearn.metrics import confusion_matrix, classification_report
from sklearn.decomposition import PCA
from sklearn.model_selection import GridSearchCV

# Test
colores_test = pd.read_csv("Celulas/test/color_test.csv", sep=';', decimal=',')
shape_test = pd.read_csv("Celulas/test/shape_test.csv", sep=';', decimal=',')
textura_test = pd.read_csv("Celulas/test/texture_test.csv", sep=';', decimal=',')
idx = pd.read_csv("Celulas/test/info_test.csv")

textura_data_test = pd.DataFrame(textura_test.iloc[:, :-1])
valores_test = pd.DataFrame(textura_test.iloc[:, -1])


df1_t = colores_test.join(shape_test, lsuffix='_colores', rsuffix='_shape')
df2_t = df1_t.join(textura_data_test, rsuffix='_texture')

X_test = shape_test.to_numpy()
y_test = valores_test.to_numpy().ravel()

# Train
data = pd.read_csv("Celulas/train/info_train.csv", sep=';')
colores = pd.read_csv("Celulas/train/color_train.csv", sep=',', decimal=',')
shape = pd.read_csv("Celulas/train/shape_train.csv", sep=',', decimal=',')
textura = pd.read_csv("Celulas/train/texture_train.csv", sep=',', decimal=',')

valores = pd.DataFrame(data.iloc[:, -1])
colores_data = pd.DataFrame(colores.iloc[:, :-1])
textura_data = pd.DataFrame(textura.iloc[:, :-1])


df1 = colores_data.join(shape, lsuffix='_colores', rsuffix='_shape')
df2 = df1.join(textura_data, rsuffix='_texture')

X_train = shape.to_numpy()
y_train = valores.to_numpy().ravel()

scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.fit_transform(X_test)

# pca = PCA()
# X_train_scaled = pca.fit_transform(X_train_scaled)
# X_test_scaled = pca.fit_transform(X_test_scaled)

# Entrenamos el modelo

param_grid = {'max_iter': np.arange(100, 5000, 100).tolist(), 'eta0': np.arange(0.1, 1, 0.1).tolist(),
              'loss': ('log', 'modified_huber', 'hinge'), 'learning_rate': ('constant', 'optimal')}

clf_log = SGDClassifier(eta0=0.5, loss='log', max_iter=3000, learning_rate='constant', random_state=5)
gsv = GridSearchCV(clf_log, param_grid, cv=None, verbose=1)

clf_log.fit(X_train_scaled, y_train)
prediction_gsv_test = clf_log.predict(X_test_scaled)

"""
gsv.fit(data_train_PCA, y_train)
print(gsv.best_estimator_)

prediction_gsv_test = gsv.predict(data_test_PCA)
"""

# Avaluació
gsv_matrix_log = confusion_matrix(y_test, prediction_gsv_test)
print("Matrix de confusion:\n", gsv_matrix_log)
print("Classification report:\n", classification_report(y_test, prediction_gsv_test))


testerino = pd.DataFrame(prediction_gsv_test, columns=['class'])

result = idx.join(testerino)
result.to_csv('test.csv', index=False)
