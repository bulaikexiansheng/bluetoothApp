from pykalman import KalmanFilter
import numpy as np

# 三通道卡尔曼滤波
initial_value_guess = [0,0,0]
transition_matrix = np.eye(3)
observation_covariance = 1 * np.eye(3)
transition_covariance = np.eye(3)
kf = KalmanFilter(
    initial_state_mean=initial_value_guess,
    initial_state_covariance=observation_covariance,
    observation_covariance=observation_covariance,
    transition_covariance=transition_covariance,
    transition_matrices=transition_matrix
)
sequence = []

# observations = (channel, time step)
def Kalman3C(observations):
    global initial_value_guess, sequence, kf
    kf.__setattr__('initial_state_mean', initial_value_guess)
    pred_state, state_cov = kf.smooth(observations)
    initial_value_guess = pred_state[-1]

    # 窗口大小：300  滑动大小：20
    if len(sequence) < 300:
        if len(sequence) == 0:
            sequence = pred_state
        else:
            sequence = np.concatenate((sequence, pred_state), axis=0)
        return None
    else:
        sequence = sequence[20:, :]
        sequence = np.concatenate((sequence, pred_state), axis=0)
        return sequence




