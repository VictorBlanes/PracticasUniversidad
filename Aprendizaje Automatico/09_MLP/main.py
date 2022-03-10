import numpy as np
import matplotlib.pyplot as plt
from sklearn.datasets import make_moons, make_circles, make_classification
from matplotlib.colors import ListedColormap
from sklearn.linear_model import LogisticRegression
from sklearn.neural_network import MLPClassifier


def method3():
    # Construïm 4 conjunts de dades per classificar:
    # En primer lloc un conjunt linealment separable on afegim renou
    X, y = make_classification(n_features=2, n_redundant=0, n_informative=2, random_state=33, n_clusters_per_class=1)
    rng = np.random.RandomState(2)
    X += 2 * rng.uniform(size=X.shape)
    linearly_separable = (X, y)

    # En segon lloc un que segueix una distribució xor
    X_xor = np.random.randn(200, 2)
    y_xor = np.logical_xor(X_xor[:, 0] > 0,
                       X_xor[:, 1] > 0)
    y_xor = np.where(y_xor, 1, -1)

    # Els afegim a una llista juntament amb els seus noms per tal de poder iterar
    # sobre ells
    datasets = [
        ("linear", linearly_separable),
        ("moons", make_moons(noise=0.3, random_state=33)),  # Tercer dataset
        ("circles", make_circles(noise=0.2, factor=0.5, random_state=33)),  # Darrer dataset
        ("xor", (X_xor, y_xor))]

    classifiers = [
        ("alpha1", MLPClassifier(random_state=0, hidden_layer_sizes=(5, 5,), alpha=1)),
        ("alpha3", MLPClassifier(random_state=0, hidden_layer_sizes=(5, 5,), alpha=3)),
        ("alpha5", MLPClassifier(random_state=0, hidden_layer_sizes=(5, 5,), alpha=5)),
        ("alpha7", MLPClassifier(random_state=0, hidden_layer_sizes=(5, 5,), alpha=7))
    ]

    # Cream una figura
    figure = plt.figure(figsize=(9, 9))
    for ds_cnt, out in enumerate(datasets):
        name, ds = out
        X, y = ds
        cm = plt.cm.RdBu  # Colormap Red -> Blue
        cm_bright = ListedColormap(["#FF0000", "#0000FF"])
        ax = plt.subplot(2, 2, ds_cnt + 1)  # Afegim la figura a una matriu de 2x2
        ax.set_title(name)

        x_min, x_max = X[:, 0].min() - 0.5, X[:, 0].max() + 0.5
        y_min, y_max = X[:, 1].min() - 0.5, X[:, 1].max() + 0.5
        h = 0.1  # paràmetres per determinar la densitat de punts de la graella
        xx, yy = np.meshgrid(np.arange(x_min, x_max, h), np.arange(y_min, y_max, h))

        clf = MLPClassifier(random_state=0, hidden_layer_sizes=(5, 5,)).fit(X, y)
        score = clf.score(X, y)

        Z = clf.predict_proba(np.c_[xx.ravel(), yy.ravel()])[:, 1]
        Z = Z.reshape(xx.shape)
        ax.contourf(xx, yy, Z, alpha=0.8)
        ax.text(
            xx.max() - 0.3,
            yy.min() + 0.3,
            ("Accuracy: %.2f" % score),  # .lstrip("0"),
            size=15,
            horizontalalignment="right",
        )
        # dibuixam els punts usats per entrenar diferenciant la seva classe (c=y)
        ax.scatter(X[:, 0], X[:, 1], c=y, cmap=cm_bright, edgecolors="k")
        ax.set_xlim(X[:, 0].min(), X[:, 0].max())
        ax.set_ylim(X[:, 1].min(), X[:, 1].max())
        ax.set_xticks(())
        ax.set_yticks(())
    plt.show()

    figure = plt.figure(figsize=(9, 9))
    for cs_cnt, out in enumerate(classifiers):
        dataset = datasets[3][1]
        name, cs = out
        X, y = dataset
        cm = plt.cm.RdBu  # Colormap Red -> Blue
        cm_bright = ListedColormap(["#FF0000", "#0000FF"])
        ax = plt.subplot(2, 2, cs_cnt + 1)  # Afegim la figura a una matriu de 2x2
        ax.set_title(name)

        x_min, x_max = X[:, 0].min() - 0.5, X[:, 0].max() + 0.5
        y_min, y_max = X[:, 1].min() - 0.5, X[:, 1].max() + 0.5
        h = 0.1  # paràmetres per determinar la densitat de punts de la graella
        xx, yy = np.meshgrid(np.arange(x_min, x_max, h), np.arange(y_min, y_max, h))

        clf = cs.fit(X, y)
        score = clf.score(X, y)

        Z = clf.predict_proba(np.c_[xx.ravel(), yy.ravel()])[:, 1]
        Z = Z.reshape(xx.shape)
        ax.contourf(xx, yy, Z, alpha=0.8)
        ax.text(
            xx.max() - 0.3,
            yy.min() + 0.3,
            ("Accuracy: %.2f" % score),  # .lstrip("0"),
            size=15,
            horizontalalignment="right",
        )
        # dibuixam els punts usats per entrenar diferenciant la seva classe (c=y)
        ax.scatter(X[:, 0], X[:, 1], c=y, cmap=cm_bright, edgecolors="k")
        ax.set_xlim(X[:, 0].min(), X[:, 0].max())
        ax.set_ylim(X[:, 1].min(), X[:, 1].max())
        ax.set_xticks(())
        ax.set_yticks(())
    plt.show()


method3()
