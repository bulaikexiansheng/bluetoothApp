import numpy as np
import scipy


def rms(data):
    return np.mean((data*data))**0.5

def energy(data, kernel_size, stride):
    out_len = int((len(data)+1-kernel_size)/stride)
    energy_data = np.zeros(out_len)
    for i in range(out_len):
        energy_data[i] = rms(data[i*stride:i*stride+kernel_size])
    return energy_data


# 插值
def interp(y, length, usescipy=False):
    x_old = np.linspace(0, len(y) - 1, num=len(y))
    x_new = np.linspace(0, len(y) - 1, num=length)
    if usescipy:
        f = scipy.interpolate.interp1d(x_old, y, kind='cubic')
        result = f(x_new)
    else:
        result = np.interp(x_new, x_old, y)

    return result
