# Si muestro la matriz de confusion del clasificador hecho con el perceptron los datos que me salen en la matrix y lo
# que puedo ver con la linea de decision no coinciden (Me salen 3 FP y solo veo 1 en la grafica)

# El F1 que calculo a mano sale constantemente mas alto que el F1 que me da sci_kit (En el del perceptros es casi un 1%
# mas alto
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split
from sklearn.linear_model import SGDClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import confusion_matrix
from sklearn.metrics import accuracy_score, f1_score
import matplotlib.pyplot as plt

X, y = make_classification(n_samples=1000, n_features=2, n_redundant=0, n_repeated=0,
                           n_classes=2, n_clusters_per_class=1, class_sep=2,
                           random_state=5)

# Tractament de les dades: Separació i estandaritzat
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20, random_state=1)

scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# Entrenament i predicció(PERCEPTRON)
clf_per = SGDClassifier(loss="perceptron", eta0=1, max_iter=1000, learning_rate="constant", random_state=5)
clf_per.fit(X_train_scaled, y_train)
prediction_per = clf_per.predict(X_test_scaled)

# Entrenament i predicció(REGRESION LOGISTICA)
clf_log = SGDClassifier(loss="log", eta0=1, max_iter=1000, learning_rate="constant", random_state=5)
clf_log.fit(X_train_scaled, y_train)
prediction_log = clf_log.predict(X_test_scaled)

# Avaluació
cf_matrix_per = confusion_matrix(y_test, prediction_per)
cf_matrix_log = confusion_matrix(y_test, prediction_log)

# Calcular F1 a mano
presicion_per = cf_matrix_per[0][0] / (cf_matrix_per[0][0] + cf_matrix_per[1][0])
sensibilidad_per = cf_matrix_per[0][0] / (cf_matrix_per[0][1] + cf_matrix_per[0][0])
presicion_log = cf_matrix_log[0][0] / (cf_matrix_log[0][0] + cf_matrix_log[1][0])
sensibilidad_log = cf_matrix_log[0][0] / (cf_matrix_log[0][1] + cf_matrix_log[0][0])
F1_per = (2 * presicion_per * sensibilidad_per) / (presicion_per + sensibilidad_per)
F1_log = (2 * presicion_log * sensibilidad_log) / (presicion_log + sensibilidad_log)
f1score_per = f1_score(y_test, prediction_per)
f1score_log = f1_score(y_test, prediction_log)
print("F1_per a mano: ", F1_per)
print("F1_per scikit: ", f1score_per)
print("F1_log a mano: ", F1_log)
print("F1_log scikit: ", f1score_log)

# TODO: Mostrar els resultats
plt.figure(1)
plt.scatter(X_test_scaled[:, 0], X_test_scaled[:, 1], c=y_test)

left, right = plt.xlim()
down, up = plt.ylim()
w_slope_per = -1*(clf_per.coef_[0][0] / clf_per.coef_[0][1])
w_slope_log = -1*(clf_log.coef_[0][0] / clf_log.coef_[0][1])
plt.plot([-100, 100],[-100 * w_slope_per, 100 * w_slope_per], color="Red")
plt.plot([-100, 100],[-100 * w_slope_log, 100 * w_slope_log], color="Blue")
plt.xlim(left, right)
plt.ylim(down, up)

plt.show()


    # Dibuixar la recta de separació de l'exemple anterior.
    # Calcula la mètrica F1 usant la matriu de confusió de l'exemple. Compara-la amb l'obtinguda amb la funció de Scikit.
    # Repetir el procés anterior substituint el Perceptron per la Regressió Logística de Scikit. (No és necessari calcular F1 a ma).
    # Compara els resultats obtinguts dels dos mètodes.

# Extra: dibuixa la recta de decisió de cada mètode en el mateix gràfic, per comparar el resultat obtingut.
