# Si hago el prediction con el conjunto de train me va a salir unas metricas mucho mas altas que con el de test ya que
# ese es el dataset que se ha usado para entrenar el clasificador, asi que no se si tenia que sacar las metricas de esa
# manera o hay otra.

import matplotlib.pyplot as plt
from sklearn import datasets
from numpy import reshape
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import SGDClassifier
from sklearn.metrics import confusion_matrix, classification_report

digits = datasets.load_digits()

"""
# Imprimimos una muestra
mostra = digits.images[0] # accedim a la primera mostra del conjunt de dades
classe = digits.target[0]

print(mostra, classe)
"""

# Reshape de la muestra
reshaped_digits = (reshape(digits.images, (digits.images.shape[0], 64)), digits.target)

# Creamos conjunto de entrenamiento y testeo
X_train, X_test, y_train, y_test = \
    train_test_split(reshaped_digits[0], reshaped_digits[1], test_size=0.30, random_state=1)

# Normalizamos la muestra
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# Entrenament i predicció(REGRESION LOGISTICA)
clf_log = SGDClassifier(loss="log", eta0=1, max_iter=1000, learning_rate="constant", random_state=5)
clf_log.fit(X_train_scaled, y_train)
prediction_log_test = clf_log.predict(X_test_scaled)
prediction_log_train = clf_log.predict(X_train_scaled)
# Avaluació
cf_matrix_log = confusion_matrix(y_test, prediction_log_test)
print("Matrix de confusion:\n", cf_matrix_log)

# Metricas
print("Classification report(Test):\n", classification_report(y_test, prediction_log_test))
print("Classification report(Train):\n", classification_report(y_train, prediction_log_train))

"""
# Creamos un subconjunto y lo mostramos
_, axes = plt.subplots(nrows=1, ncols=4, figsize=(10, 3))
for ax, image, label in zip(axes, digits.images, digits.target):
    ax.set_axis_off()
    ax.imshow(image, cmap=plt.cm.gray_r, interpolation='nearest')
    ax.set_ti
    tle('Mostra del valor: %i' % label)
plt.show()
"""