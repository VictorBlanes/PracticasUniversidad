import numpy as np
import matplotlib.pyplot as plt
from sklearn.datasets import make_classification
from sklearn.linear_model import LogisticRegression


X, y = make_classification(n_features=2, n_redundant=0, n_informative=2, random_state=1, n_clusters_per_class=1)

# La idea és crear una graella uniformement distribuïda sobre la qual farem prediccions
# d'aquesta manera tendrem tots els punts del plot amb un valor de predicció.

x_min, x_max = X[:, 0].min() - 0.5, X[:, 0].max() + 0.5
y_min, y_max = X[:, 1].min() - 0.5, X[:, 1].max() + 0.5
h = 0.1  # paràmetres per determinar la densitat de punts de la graella
xx, yy = np.meshgrid(np.arange(x_min, x_max, h), np.arange(y_min, y_max, h))

# Classificador d'exemple
clf = LogisticRegression(random_state=0).fit(X, y)
score = clf.score(X, y)
# Realitzam la predicció de tots els valors de la graella (predict_proba)
# dibuixam la graella i la seva predicció com un contorn (plt.contourf)
Z = clf.predict_proba(np.c_[xx.ravel(), yy.ravel()])[:, 1]
Z = Z.reshape(xx.shape)
plt.contourf(xx, yy, Z, alpha=0.8)
plt.text(
    xx.max() - 0.3,
    yy.min() + 0.3,
    ("Accuracy: %.2f" % score), #.lstrip("0"),
    size=15,
    horizontalalignment="right",
    )
# dibuixam els punts usats per entrenar diferenciant la seva classe (c=y)
plt.scatter(X[:, 0], X[:, 1], c=y, edgecolors="k")
plt.show()
