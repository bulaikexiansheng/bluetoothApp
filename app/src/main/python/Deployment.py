import numpy as np
import torch

import endpoint_detection as ed
import KalmanFilter3C
from os.path import dirname,join



# sequence(length, channel=3)
def predict(sequence):

    fs = 100
    # 读取模型
    model = torch.load(join(dirname(__file__),'./model.pt'), map_location='cpu')
    # 卡尔曼滤波
    kalman_output = KalmanFilter3C.Kalman3C(sequence)
    # 4为静止状态的label
    predict_label = 4
    # 满一个窗口数据
    if kalman_output is not None:
        kalman_output = np.squeeze(kalman_output).transpose()
        # 动作检测与分割
        segment = ed.segment(kalman_output, fs)
        segment = torch.tensor(segment).t()
        segment = torch.unsqueeze(segment, dim=0)
        # 检测到动作
        if segment.shape[1] != 0:
            # 预测结果
            model_output = model(segment)
            predict_label = torch.argmax(model_output).item()


    return predict_label



