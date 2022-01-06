# Autor: Víctor Manuel Blanes Castro
import numpy as np
from numpy.random import seed


class AdalineSGD(object):
    """ADAptive LInear NEuron classifier.

    Parameters
    ------------
    eta : float
        Learning rate (between 0.0 and 1.0)
    n_iter : int
        Passes over the training dataset.

    Attributes
    -----------
    w_ : 1d-array
        Weights after fitting.
    errors_ : list
        Number of misclassifications in every epoch.
    shuffle : bool (default: True)
        Shuffles training data every epoch if True to prevent cycles.
    random_state : int (default: None)
        Set random state for shuffling and initializing the weights.

    """

    def __init__(self, eta=0.01, n_iter=10, shuffle=True, random_state=None):
        self.eta = eta
        self.n_iter = n_iter
        self.w_initialized = False
        self.shuffle = shuffle
        if random_state:
            seed(random_state)

    def fit(self, X, y):
        """ Fit training data.

        Parameters
        ----------
        X : {array-like}, shape = [n_samples, n_features]
            Training vectors, where n_samples is the number of samples and
            n_features is the number of features.
        y : array-like, shape = [n_samples]
            Target values.

        Returns
        -------
        self : object

        """

        # TODO: Put your code here. Notau que heu d'avaluar si s'han de mesclar les mostres.
        if not self.w_initialized:
            self.w_ = np.zeros(X.shape[1] + 1)
        self.mse_ = np.zeros(self.n_iter)
        self.res = np.zeros(X.shape[0])
        for i in range(self.n_iter):
            if self.shuffle:
                self.__shuffle(X, y)
            for j in range(X.shape[0]):
                self.res[j] = self.activation(X[j])
                self.__act_w(X[j], y[j], j)
            self.mse_[i] = self.__mse(y)
        return self

    def __shuffle(self, X, y):
        """Shuffle training data"""
        r = np.random.permutation(len(y))
        return X[r], y[r]

    def net_input(self, X):
        """Calculate net input"""
        return np.dot(X, self.w_[1:]) + self.w_[0]

    def activation(self, X):
        """Compute linear activation"""
        return self.net_input(X)

    def __act_w(self, X, y, j):
        self.w_[1:] = self.w_[1:] + (self.eta * (y - self.res[j]) * X[:])

    def __mse(self, y):
          return np.square(np.subtract(y, self.res)).mean()

    def showMSE(self):
        for i in range(self.mse_.shape[0]):
            print("Iteracion %d: %f" % (i, self.mse_[i]))

    def predict(self, X):
        """Return class label after unit step"""
        return np.where(self.activation(X) >= 0.0, 1, -1)
