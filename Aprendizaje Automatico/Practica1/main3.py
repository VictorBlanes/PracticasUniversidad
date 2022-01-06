import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.model_selection import GridSearchCV
import matplotlib.pyplot as plt
from sklearn.decomposition import PCA
from sklearn.metrics import classification_report


def info_arch(name):
    # Exploratory data Analysis
    # pd.set_option('display.max_columns', None)
    print(name.shape, "\n")
    print(name.head(), "\n")
    print(name.columns, "\n")
    print(name.info(), "\n")
    print(round(name.describe(), 2), "\n")


def evaluate_model(model, xtr, xts, ytr, yts):
    # Training model
    model.fit(xtr, ytr)
    #print(model.best_estimator_)

    # Prediction
    ypr_tr = model.predict(xtr)
    ypr_ts = model.predict(xts)

    # Classification report
    clrp_tr = classification_report(ytr, ypr_tr)
    #print("Classification report(Train):\n", classification_report(ytr, ypr_tr))
    clrp_ts = classification_report(yts, ypr_ts)
    #print("Classification report(Test):\n", classification_report(yts, ypr_ts))

    # Overfitting
    mdsc_tr = model.score(xtr, ytr)
    #print("Training score:\n", mdsc_tr)
    mdsc_ts = model.score(xts, yts)
    print("Test score:\n", mdsc_ts)
    return ypr_tr, ypr_ts, mdsc_tr, mdsc_ts, clrp_tr, clrp_ts


def single_pca(num, xtr, xts, ytr, yts):
    pca_s = PCA(num)
    x_tr = pca_s.fit_transform(xtr)
    x_ts = pca_s.transform(xts)
    param_grid_s = {'C': np.arange(0.1, 2.0, 0.01).tolist()}
    svc2_s = SVC(kernel='poly', random_state=5)
    gsv2_s = GridSearchCV(svc2_s, param_grid_s, cv=None, verbose=1)
    print("\nkernel='poly' - PCA ", num)
    ypr_tr, ypr_ts, mdsc_tr, mdsc_ts, clrp_tr, clrp_ts = evaluate_model(gsv2_s, x_tr, x_ts, ytr, yts)
    return ypr_tr, ypr_ts, mdsc_tr, mdsc_ts, clrp_tr, clrp_ts


def single_pca(num, xtr, xts, ytr, yts):
    pca_s = PCA(num)
    x_tr = pca_s.fit_transform(xtr)
    x_ts = pca_s.transform(xts)
    param_grid_s = {'C': np.arange(0.1, 2.0, 0.01).tolist()}
    svc2_s = SVC(kernel='poly', random_state=5)
    gsv2_s = GridSearchCV(svc2_s, param_grid_s, cv=None, verbose=1)
    print("\nkernel='poly' - PCA ", num)
    ypr_tr, ypr_ts, mdsc_tr, mdsc_ts, clrp_tr, clrp_ts = evaluate_model(gsv2_s, x_tr, x_ts, ytr, yts)
    return ypr_tr, ypr_ts, mdsc_tr, mdsc_ts, clrp_tr, clrp_ts


def grafica_pca(xtr, xts, ytr, yts):
    result = []
    for i in range(len(X_train[0])):
        _, _, _, res, _, _ = single_pca(i + 1, xtr, xts, ytr, yts)
        result += [res]

    print(len(xtr[0]))
    print(len(result[:]))
    plt.figure(1)
    plt.scatter(range(1, len(xtr[0]) + 1), result[:])
    plt.plot(range(1, len(xtr[0]) + 1), result[:])
    plt.show()


def varianca_pca(xtr):
    pca_v = PCA()
    pca_v.fit(xtr)
    cumvariance = np.cumsum(pca_v.explained_variance_)
    plt.figure(1)
    plt.scatter(range(cumvariance.shape[0]), cumvariance[:])
    plt.plot(range(cumvariance.shape[0]), cumvariance[:])
    plt.show()


# Import Dataset
# Test
col_test = pd.read_csv("Celulas/test/color_test.csv", sep=';', decimal=',')
sh_test = pd.read_csv("Celulas/test/shape_test.csv", sep=';', decimal=',')
text_test = pd.read_csv("Celulas/test/texture_test.csv", sep=';', decimal=',')
info_test = pd.read_csv("Celulas/test/info_test.csv")

target_test = pd.DataFrame(text_test.iloc[:, -1])
text_test = pd.DataFrame(text_test.iloc[:, :-1])

# Train
info_train = pd.read_csv("Celulas/train/info_train.csv", sep=';')
col_train = pd.read_csv("Celulas/train/color_train.csv", sep=',', decimal=',')
sh_train = pd.read_csv("Celulas/train/shape_train.csv", sep=',', decimal=',')
text_train = pd.read_csv("Celulas/train/texture_train.csv", sep=',', decimal=',')

target_train = pd.DataFrame(info_train.iloc[:, -1])
text_train = pd.DataFrame(text_train.iloc[:, :-1])
col_train = pd.DataFrame(col_train.iloc[:, :-1])

# Concatenate
# X_tr = col_train.join(sh_train.join(text_train))
# X_ts = col_test.join(sh_test.join(text_test))
X_tr = sh_train.join(text_train)
X_ts = sh_test.join(text_test)
# X_tr = sh_train
# X_ts = sh_test

# Transform to Numpy
X_train = X_tr.to_numpy()
X_test = X_ts.to_numpy()
y_train = target_train.to_numpy().ravel()
y_test = target_test.to_numpy().ravel()
cols = sh_train.columns  # Los nombres de las columnas

# Feature Scaling
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

"""
# Graficas PCA
grafica_pca(X_train, X_test, y_train, y_test)
varianca_pca(X_train)

"""

# PCA
pca = PCA(9)
X_train = pca.fit_transform(X_train)
X_test = pca.transform(X_test)


"""Data: Solo Shape
# Model definition

svc = SVC(kernel='linear', C=1.10)
svc1 = SVC(kernel='poly', C=0.8)
svc2 = SVC(kernel='sigmoid', C=0.05)
svc3 = SVC(kernel='rbf', C=0.04)

# Training, Classification report & Overfitting
print("\nkernel='linear'")
y_pred1 = evaluate_model(svc, X_train, X_test, y_train, y_test)
print("\nkernel='poly'")
y_pred2 = evaluate_model(svc1, X_train, X_test, y_train, y_test)
print("\nkernel='sigmoid'")
y_pred3 = evaluate_model(svc2, X_train, X_test, y_train, y_test)
print("\nkernel='rbf'")
y_pred4 = evaluate_model(svc3, X_train, X_test, y_train, y_test)
"""


# Model definition
# param_grid = {'C': np.arange(0.01, 2.0, 0.01).tolist(), 'gamma': np.logspace(-9, 3, 13).tolist()}
#svc1 = SVC(kernel='linear')
svc2 = SVC(kernel='poly', random_state=5)
#svc3 = SVC(kernel='sigmoid', random_state=5)
#svc4 = SVC(kernel='rbf')
svc2 = SVC(C=1.4599999999999995, gamma=0.019000000000000003, kernel='poly',
    random_state=5)
#gsv1 = GridSearchCV(svc1, param_grid, cv=None, verbose=1)
#gsv2 = GridSearchCV(svc2, param_grid, cv=None, verbose=1)
#gsv3 = GridSearchCV(svc3, param_grid, cv=None, verbose=1)
#gsv4 = GridSearchCV(svc4, param_grid, cv=None, verbose=1)
gsv2 = svc2
# Training, Classification report & Overfitting
#print("\nkernel='linear'")
#y_pred1 = evaluate_model(gsv1, X_train, X_test, y_train, y_test)

print("\nkernel='poly'")
_, y_pred2, _, _, _, _ = evaluate_model(gsv2, X_train, X_test, y_train, y_test)

print("\nkernel='sigmoid'")
#_, y_pred3, _, _, _, _ = evaluate_model(gsv3, X_train, X_test, y_train, y_test)

#print("\nkernel='rbf'")
#y_pred4 = evaluate_model(gsv4, X_train, X_test, y_train, y_test)

# Create CSV
#results1 = pd.DataFrame(y_pred1, columns=['class'])
results2 = pd.DataFrame(y_pred2, columns=['class'])
#results3 = pd.DataFrame(y_pred3, columns=['class'])
#results4 = pd.DataFrame(y_pred4, columns=['class'])

# result1 = info_test.join(results1)
result2 = info_test.join(results2)
#result3 = info_test.join(results3)
# result4 = info_test.join(results4)

#result1.to_csv('test1.csv', index=False)
result2.to_csv('test2.csv', index=False)
#result3.to_csv('test3.csv', index=False)
#result4.to_csv('test4.csv', index=False)


