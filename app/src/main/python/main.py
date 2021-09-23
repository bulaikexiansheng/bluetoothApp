import re
import numpy as np
import pandas as pd
import Deployment

def delete_prefix(data):
    for i in range(len(data)):
        data[i] = data[i][2:]
    return data


if __name__ == '__main__':

    data_path = './test_data/'
    for index in range(6):
        raw_data = pd.read_table(filepath_or_buffer=data_path + 'txt/'+ str(index) + '.txt')
        raw_data = np.array(raw_data).reshape(-1)
        line = ''.join(raw_data)
        expression = ['(?:GX|GX-)\d+\.?\d*', '(?:GY|GY-)\d+\.?\d*', '(?:GZ|GZ-)\d+\.?\d*']
        data = []
        for i in range(len(expression)):
            channel = delete_prefix(re.compile(expression[i]).findall(line))
            channel = np.array(channel).astype(float)
            data.append(channel)
        data = np.array(data).astype(float)

        for i in range(int(len(data[0])/20)):
            label = Deployment.predict(data[:, i*20:(i+1)*20].transpose())
            print(data[:, i*20:(i+1)*20].transpose())
            # if label != 4:
            print(str(index) + ' label:' +str(label))

    pass