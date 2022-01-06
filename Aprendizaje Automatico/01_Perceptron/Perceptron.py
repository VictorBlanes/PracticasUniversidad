import numpy as np

class Perceptron:
    """Perceptron classifier.

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

    """
    def __init__(self, eta=0.01, n_iter=10):
        self.eta = eta
        self.n_iter = n_iter

    def fit(self, X, y):
        """Fit training data.

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
        self.w_ = np.zeros(1 + X.shape[1])
        for _ in range(self.n_iter):
            for i in range(X.shape[0]):
                self.__net_input(X[i])
                self.__update_w(X[i], y[i])
        return self

    def __net_input(self, X):
        self.res = np.dot(X, self.w_[1:])

    def __update_w(self, X, y):
        self.w_[1:] = self.w_[1:] + (self.eta * (y - self.res) * X[:])

    def predict(self, X):
        y_res = np.zeros(X.shape[0])
        for i in range(X.shape[0]):
            res = np.dot(X[i], self.w_[1:])
            if res >= 0:
                y_res[i] = 1
            else:
                y_res[i] = -1
        return y_res
