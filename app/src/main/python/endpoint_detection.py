import numpy as np
import pandas as pd
import dsp
import vad

# 删除前缀
def delete_prefix(data):
    for i in range(len(data)):
        data[i] = data[i][2:]
    return data


# 选择最佳的检测通道
def detection_channel(data, motion_size):

    # 求短时能量
    energy = []
    for i in range(3):
        temp = dsp.energy(data[i,:], int(motion_size), 1)
        energy.append(sum(temp))

    result = data[np.argmax(energy),:]
    return result



# VAD 窗口
def myvad(signal, motion_size, fs):

    result = vad.vad(signal, sr=fs)

    high = 0
    size = motion_size*0.3
    for i in range(len(result) - 1):
        if result[i] == 1:
            high = high + 1
        else:
            if high < size and high > 0:
                result[i - high: i] = (0,)
            high = 0

    vad_window = dsp.interp(result, len(signal))

    return vad_window


# 分割
def segment0(signal, vad_window, motion_size):
    if vad_window[0] == 0 and vad_window[-1] ==0:
        # 选择信号
        seg_signal = vad_window * signal
        # 截取信号
        seg_signal = pd.DataFrame(seg_signal).replace(0, np.NAN)
        seg_signal.dropna(inplace=True)
        seg_signal = np.array(seg_signal).reshape(-1)
    else:
        seg_signal = np.zeros(motion_size).reshape(-1)

    return seg_signal


def segment(data, fs):
    motion_size = 100
    # 选择最佳的检测通道
    signal = detection_channel(data, motion_size)
    # vad
    vad_window = myvad(signal, motion_size, fs)

    seg = []
    for i in range(3):
        temp = segment0(data[i,:], vad_window, motion_size)
        seg.append(temp)

    return seg






