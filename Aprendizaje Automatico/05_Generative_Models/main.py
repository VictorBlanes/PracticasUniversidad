# Cuando hay que hacer reshape y cuando no hay que hacer, para el modelo de perceptron y regresion lineal hay que
# aplanar pero para PCA y gaussianMixture parece que no

import numpy as np
import matplotlib.pyplot as plt
from sklearn import datasets
from sklearn.decomposition import PCA
from sklearn.mixture import GaussianMixture


def plot_digits(data):
    fig, ax = plt.subplots(10, 10, figsize=(8, 8),
                           subplot_kw=dict(xticks=[], yticks=[]))
    fig.subplots_adjust(hspace=0.05, wspace=0.05)
    for i, axi in enumerate(ax.flat):
        im = axi.imshow(data[i].reshape(8, 8), cmap='binary')
        im.set_clim(0, 16)


digits = datasets.load_digits()

"""
# Hacemos PCA

pca = PCA()
pca.fit(digits.data)

# Mostramos evolucion

cumVariance = np.cumsum(pca.explained_variance_)
plt.figure(1)
plt.scatter(range(cumVariance.shape[0]), cumVariance[:])
plt.plot(range(cumVariance.shape[0]), cumVariance[:]) #Cogemos 42
plt.show()
"""

# Hacemos PCA con el numero de muestras escogido
pca = PCA(42)
digits_pca = pca.fit_transform(digits.data)

"""
# Hacemos Gaussian mixture y despues Bayesian information criterion

plt.figure(1)
components = np.arange(10, 500, 10)
modelos = [GaussianMixture(n_components=n, random_state=0)
           for n in components]
bics = [m.fit(digits_pca).bic(digits_pca)
        for m in modelos]
plt.plot(components, bics) # Cogemos 80
plt.show()
"""

# Generacion de nuevos ejemplos
modelo = GaussianMixture(n_components=80, random_state=0)
modelo.fit(digits_pca)
sample, _ = modelo.sample(100)
print(sample.shape)

newdata = pca.inverse_transform(sample)
plot_digits(newdata)
plt.show()
