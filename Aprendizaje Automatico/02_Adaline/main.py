# Autor: Víctor Manuel Blanes Castro
import random
import numpy as np
import matplotlib.pyplot as plt
from sklearn.datasets import make_classification
from sklearn.preprocessing import StandardScaler
from Adaline_SGD import AdalineSGD

# Generació del conjunt de mostres
X, y = make_classification(n_samples=100, n_features=2, n_redundant=0, n_repeated=0,
                           n_classes=2, n_clusters_per_class=1, class_sep=1.5,
                           random_state=8)
y[y == 0] = -1  # La nostra implementació esta pensada per tenir les classes 1 i -1.

# TODO: Normalitzar les dades
scaler = StandardScaler()
scaler.fit(X)
X_normalized = scaler.transform(X)
# TODO: Entrenar
adaline = AdalineSGD(n_iter=100, shuffle=True, random_state=7)
adaline.fit(X_normalized, y)
# TODO: Mostrar els resultats
adaline.showMSE()
plt.figure(1)
plt.scatter(X_normalized[:, 0], X_normalized[:, 1], c=y)

left, right = plt.xlim()
down, up = plt.ylim()
w_slope = -1*(adaline.w_[1] / adaline.w_[2])
plt.plot([-100, 100],[-100 * w_slope, 100 * w_slope])
plt.xlim(left, right)
plt.ylim(down, up)

plt.show()