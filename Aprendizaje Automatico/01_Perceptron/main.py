import numpy as np
import matplotlib.pyplot as plt
from sklearn.datasets import make_classification
from Perceptron import Perceptron
import random

# Generació del conjunt de mostres
X, y = make_classification(n_samples=100, n_features=2, n_redundant=0, n_repeated=0,
                           n_classes=2, n_clusters_per_class=1, class_sep=1.0,
                           random_state=8)
y[y == 0] = -1  # La nostra implementació esta pensada per tenir les classes 1 i -1.

perceptron = Perceptron()
perceptron.fit(X, y)
y_prediction = perceptron.predict(X)

#  Mostram els resultats
plt.figure(1)
plt.scatter(X[:, 0], X[:, 1], c=y)

left, right = plt.xlim()
down, up = plt.ylim()
w_slope = -1*(perceptron.w_[1] / perceptron.w_[2])
plt.plot([-100, 100],[-100 * w_slope, 100 * w_slope])
plt.xlim(left, right)
plt.ylim(down, up)

plt.show()